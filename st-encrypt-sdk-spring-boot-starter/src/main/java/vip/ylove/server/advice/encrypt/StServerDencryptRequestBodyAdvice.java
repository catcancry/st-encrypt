package vip.ylove.server.advice.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import vip.ylove.annotation.StEncrypt;
import vip.ylove.config.StConfig;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
import vip.ylove.sdk.server.dencrypt.StAbstractRequestDencrypt;

import java.lang.reflect.Type;

/**
 * Author:Bobby
 * DateTime:2019/4/9
 **/
@ControllerAdvice
public class StServerDencryptRequestBodyAdvice implements RequestBodyAdvice {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StConfig secretKeyConfig;

    @Autowired
    private StAbstractRequestDencrypt stDencrypt;
    @Autowired
    private StAbstractAuth stAuth ;


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (methodParameter.getMethod().isAnnotationPresent(StEncrypt.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType){
        log.debug("线程[{}]->解密请求-->开始解密",Thread.currentThread().getId());
        return new DecryptHttpInputMessage(secretKeyConfig.getPrivateKey(),stDencrypt,stAuth,inputMessage, secretKeyConfig.getPrivateKey());
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("线程[{}]->解密请求-->完成",Thread.currentThread().getId());
        return body;
    }
}
