package vip.ylove.server.advice.dencrypt;

import cn.hutool.json.JSONUtil;
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
import vip.ylove.annotation.StEncrypt;
import vip.ylove.sdk.util.StKeyUtil;
import vip.ylove.sdk.server.encrypt.StAbstractResponseEncrypt;
import vip.ylove.sdk.util.StStopWatch;

import java.util.concurrent.TimeUnit;

/**
 * Author:Bobby
 * DateTime:2019/4/9
 **/
@ControllerAdvice
public class StServerEncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StAbstractResponseEncrypt stEncrypt;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getMethod().isAnnotationPresent(StEncrypt.class)) {
            log.debug("线程[{}]->加密请求结果-->是否需要加密:{}",Thread.currentThread().getId(),"是");
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.debug("线程[{}]->加密请求结果-->开始加密",Thread.currentThread().getId());
        StStopWatch stopWatch = new StStopWatch("加密响应");
        stopWatch.start("开始加密");
        Object encrypt = stEncrypt.encrypt(StKeyUtil.getKey(), JSONUtil.toJsonStr(body));
        stopWatch.stop();
        log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        log.debug("线程[{}]->加密请求结果-->加密完成",Thread.currentThread().getId());
        return encrypt;
    }
}
