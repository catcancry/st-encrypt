package vip.ylove.server.advice.dencrypt.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import vip.ylove.config.StConfig;
import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.annotation.StEncryptSkip;
import vip.ylove.sdk.common.StConst;
import vip.ylove.sdk.exception.StException;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
import vip.ylove.sdk.server.dencrypt.StAbstractRequestDencrypt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class StRequestHandlerIntercepter implements HandlerInterceptor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StAbstractRequestDencrypt stDencrypt;
    @Autowired
    private StConfig stConfig;
    @Autowired
    private StAbstractAuth stAuth;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //判断是否需要解密参数
        StEncrypt se = handlerMethod.getMethodAnnotation(StEncrypt.class);
        if (se != null) {
            if (se.req()) {
                //需要进行解密
                dencryptRequest(se, request);
                return true;
            }
        } else if (stConfig.isEnableGlobalEncrypt()) { //开启全局验证
            if (!handlerMethod.getMethod().isAnnotationPresent(StEncryptSkip.class)) { //是否跳过方法
                //需要进行解密
                dencryptRequest(se, request);
                return true;
            }
        }
        return true;
    }

    private void dencryptRequest(StEncrypt stEncrypt, HttpServletRequest request) {
        StHttpServletRequestWrapper requestWrapper = (StHttpServletRequestWrapper) request;
        String ct = request.getContentType();
        try {
            if ("GET".equals(request.getMethod())) {
                this.updateByParam(stEncrypt, request);
                return;
            }
            //如果是文件上传请求,直接跳过，暂时不支持加密解密
            if (ServletFileUpload.isMultipartContent(request)) {
                StException.throwExec(StException.ErrorCode.REQUEST_DENCRYPT_UNKNOWN_ERROR, "暂时不支持上传文件请求进行加密解密");
                return;
            }
            if (ct == null) {
                this.updateByParam(stEncrypt, request);
                return;
            }
            if (ct.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
                this.updateByBodyJson(stEncrypt, request);
                return;
            }
            if (ct.startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                String body = requestWrapper.getBody();
                if (!StrUtil.isBlankIfStr(body)) {
                    String[] ps = body.split("&");
                    if (ps != null) {
                        String key = null;
                        String data = null;
                        for (int i = 0; i < ps.length; i++) {
                            String[] p = ps[i].split("=");
                            if (StConst.KEY.equals(p[0])) {
                                key = p[1];
                            } else if (StConst.DATA.equals(p[0])) {
                                data = p[1];
                            }
                        }
                        byte[] dencrypt = stDencrypt.dencrypt(stConfig.getPrivateKey(), key, data, stEncrypt, stAuth);
                        Map<String, Object> params = JSONUtil.toBean(new String(dencrypt, StConst.DEFAULT_CHARSET), Map.class);
                        StringBuffer buffer = new StringBuffer();
                        params.entrySet().forEach(e -> {
                            buffer.append(e.getKey()).append("=").append(e.getValue()).append("&");
                        });
                        if (buffer.length() > 0) {
                            buffer.substring(0, buffer.length() - 1);
                        }
                        System.out.println(buffer.toString());
                    }
                }
                return;
            }
            log.error("当前请求类型contentType:{},不支持的加密头,请求方式:{}", ct,request.getMethod());
            StException.throwExec(StException.ErrorCode.NOT_SUPPORT_CONTENT_TYPE, "当前请求类型contentType:" + ct + ",不支持的加密头,请求方式:"+ request.getMethod() );
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解密请求参数发生异常:", e.getMessage());
            StException.throwExec(StException.ErrorCode.REQUEST_DENCRYPT_UNKNOWN_ERROR, "解密请求参数发生未知异常:" + e.getMessage());
        }
    }

    private void updateByParam(StEncrypt stEncrypt, HttpServletRequest request) {
        StHttpServletRequestWrapper requestWrapper = (StHttpServletRequestWrapper) request;
        String key = request.getParameter(StConst.KEY);
        String data = request.getParameter(StConst.DATA);
        //get请求不带参数的请求
        if (data == null || data.trim().isEmpty() || "null".equals(data.trim())) {
            data = null;
        }
        //移除参数中的StEncryptParams
        requestWrapper.removeStEncryptParams();
        byte[] dencrypt = stDencrypt.dencrypt(stConfig.getPrivateKey(), key, data, stEncrypt, stAuth);
        if (dencrypt != null) {
            Map<String, Object> params = JSONUtil.toBean(new String(dencrypt, StConst.DEFAULT_CHARSET), Map.class);
            requestWrapper.addParameters(params);
        }
    }

    private void updateByBodyJson(StEncrypt stEncrypt, HttpServletRequest request) {
        StHttpServletRequestWrapper requestWrapper = (StHttpServletRequestWrapper) request;
        String body = requestWrapper.getBody();
        byte[] bodyData = stDencrypt.dencrypt(stConfig.getPrivateKey(), body, stEncrypt, stAuth);
        if (bodyData == null) {
            requestWrapper.setBody("{}");
        } else {
            String deBody = new String(bodyData);
            requestWrapper.setBody(deBody);
        }
    }

}
