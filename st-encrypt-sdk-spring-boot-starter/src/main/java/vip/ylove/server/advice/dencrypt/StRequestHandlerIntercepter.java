package vip.ylove.server.advice.dencrypt;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
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
import java.io.UnsupportedEncodingException;
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
    /**
     *  默认的空body
     **/
    private static final  byte[] nullBody = new String("{}").getBytes(StConst.DEFAULT_CHARSET);

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
        String ct = request.getContentType();
        try {
            if ("GET".equals(request.getMethod())) {
                this.updateByParam(stEncrypt, request);
                return;
            }
            if (ct == null) {
                this.updateByParam(stEncrypt, request);
                return;
            }
            if (ct.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                this.updateByParam(stEncrypt,request);
                return;
            }
            //如果是文件上传请求,直接跳过，暂时不支持加密解密
            if (ServletFileUpload.isMultipartContent(request)) {
                this.updateByUploadForm(stEncrypt,request);
                return;
            }
            if (ct.contains(MediaType.APPLICATION_JSON_VALUE)) {
                this.updateByBodyJson(stEncrypt, request);
                return;
            }
            log.error("当前请求类型contentType:{},不支持的加密头,请求方式:{}", ct,request.getMethod());
            StException.throwExec(StException.ErrorCode.NOT_SUPPORT_CONTENT_TYPE, "当前请求类型contentType:" + ct + ",不支持的加密头,请求方式:"+ request.getMethod() );
        } catch (StException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解密请求参数发生异常:", e.getMessage());
            StException.throwExec(StException.ErrorCode.REQUEST_DENCRYPT_UNKNOWN_ERROR, "解密请求参数发生未知异常:" + e.getMessage());
        }
    }

    /**
     * 表单提交
     **/
    private void updateByUploadForm(StEncrypt stEncrypt, HttpServletRequest request) {
        if(request instanceof StandardMultipartHttpServletRequest){
            System.out.println("这是个文件上传请求");
            

        }else if(request instanceof StHttpServletRequestWrapper){
            System.out.println("一般文件上传请求");
        }
    }

    /**
     * 解码放在url参数中的加密信息
     **/
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
    /**
     * post json参数获取并解码
     **/
    private void updateByBodyJson(StEncrypt stEncrypt, HttpServletRequest request) {
        StHttpServletRequestWrapper requestWrapper = (StHttpServletRequestWrapper) request;
        String body = new String(requestWrapper.getBody(),StConst.DEFAULT_CHARSET);
        byte[] bodyData = stDencrypt.dencrypt(stConfig.getPrivateKey(), body, stEncrypt, stAuth);
        if (bodyData == null) {
            requestWrapper.setBody(nullBody);
        } else {
            requestWrapper.setBody(bodyData);
        }
    }

}
