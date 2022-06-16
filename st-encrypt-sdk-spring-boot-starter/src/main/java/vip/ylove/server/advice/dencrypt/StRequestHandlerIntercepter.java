package vip.ylove.server.advice.dencrypt;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

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

    /**
     * 判断请你求是否需要解密解密
     * @param request request
     * @param handler handler
     * @param consumer 操作方式
     * @return
     */
    private boolean hasStEncrypt(HttpServletRequest request,Object handler,  Consumer<StEncrypt> consumer){
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //判断是否需要解密参数
        StEncrypt se = handlerMethod.getMethodAnnotation(StEncrypt.class);
        if (se != null) {
            if (se.req()) {
                //需要进行加密解密
                consumer.accept(se);
                return true;
            }
        } else if (stConfig.isEnableGlobalEncrypt()) { //开启全局验证
            if (!handlerMethod.getMethod().isAnnotationPresent(StEncryptSkip.class)) { //是否跳过方法
                //需要进行加密解密
                consumer.accept(se);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        hasStEncrypt(request, handler,(StEncrypt stEncrypt)->{
            dencryptRequest(stEncrypt, request);
        });
        return true;
    }

//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        hasStEncrypt(request, handler,(StEncrypt stEncrypt)->{
//            System.out.println("响应结果需要进行加密");
//        });
//    }

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
     * 上传文件表单提交
     **/
    private void updateByUploadForm(StEncrypt stEncrypt, HttpServletRequest request)  {
        StStandardMultipartHttpServletRequest rq = (StStandardMultipartHttpServletRequest) request;
        String key = rq.getParameterValue(StConst.KEY);;
        String data = rq.getParameterValue(StConst.DATA);
        rq.removeStEncryptParams();
        byte[] dencrypt = stDencrypt.dencrypt(stConfig.getPrivateKey(), key, data, stEncrypt, stAuth);
        Map<String, Object> params = null;
        if (dencrypt != null) {
            params = JSONUtil.toBean(new String(dencrypt, StConst.DEFAULT_CHARSET), Map.class);
            rq.addParameters(params);
        }
        if( rq.getFileMap() != null &&  !rq.getFileMap().isEmpty()){
            for (MultipartFile f:rq.getFileMap().values()){
                try(InputStream in = f.getInputStream()){
                    System.out.println("fileName:"+f.getOriginalFilename());
                    System.out.println("fileType:"+f.getContentType());
                    System.out.println("fileSize:"+f.getSize());
                    System.out.println("argName:"+f.getName());
                    //获取文件md5
                    String fileMD5 = DigestUtil.md5Hex(in);
                    if(params == null || params.get(fileMD5) == null){
                      StException.throwExec(StException.ErrorCode.UPLOAD_FILE_MD5_VERIFICATION_ERROR,"文件["+f.getOriginalFilename()+"]可能被篡改,在"+StConst.DATA+"加密数据中未发现上传文件的md5["+fileMD5+"]的参数");
                    }
                }catch(IOException e){
                    StException.throwExec(36,"验证文件md5异常");
                }
            }
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
