package vip.ylove.server.advice.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import vip.ylove.config.StConfig;
import vip.ylove.sdk.annotation.StEncrypt;
import vip.ylove.sdk.annotation.StEncryptSkip;
import vip.ylove.sdk.dto.StResult;
import vip.ylove.sdk.json.StAbstractJsonDcode;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
import vip.ylove.sdk.server.encrypt.StAbstractResponseEncrypt;

/**
 *
 * @author catcancry
 **/
@ControllerAdvice
public class StServerEncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StAbstractResponseEncrypt stEncrypt;
    @Autowired
    private StConfig stConfig;
    @Autowired
    private StAbstractAuth stAuth;
    @Autowired
    private StAbstractJsonDcode stJson;


    @Override
    public boolean supports(MethodParameter p, Class<? extends HttpMessageConverter<?>> converterType) {
        StEncrypt se = p.getMethodAnnotation(StEncrypt.class);
        if (se != null ) {
            //在使用注解开发测试时，允许临时关闭注解
            if(stConfig.isCloseGlobalEncrypt()){
                return false;
            }
            if(!se.resp()){
                return false;
            }
            return true;
        }else  if(stConfig.isEnableGlobalEncrypt()){ //开启全局验证
            //在使用注解开发测试时，允许临时关闭注解
            if(stConfig.isCloseGlobalEncrypt()){
                return false;
            }
            if (!p.getMethod().isAnnotationPresent(StEncryptSkip.class) ){ //是否跳过方法
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.debug("线程[{}]->加密请求结果-->开始加密",Thread.currentThread().getId());
        Object result = null;
        //响应结果实现了StResult则能判断请求结果正确性

        if( body instanceof  StResult){
            StResult stBody =(StResult) body;
            if(stBody.isSuccess()){
                result = stEncrypt.encrypt(stConfig.getPrivateKey(), stJson.toJson(body),stAuth);
            }else{
                result = body;
            }
        }else{
            log.debug("响应结果未实现StResult接口，将会导致异常结果不能直接显示的，也会被加密的问题");
            result = stEncrypt.encrypt(stConfig.getPrivateKey(), stJson.toJson(body), stAuth);
        }
        log.debug("线程[{}]->加密请求结果-->加密完成",Thread.currentThread().getId());
        return result;
    }
}
