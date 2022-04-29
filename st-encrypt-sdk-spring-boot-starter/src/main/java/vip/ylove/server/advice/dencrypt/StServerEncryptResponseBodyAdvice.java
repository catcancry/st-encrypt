package vip.ylove.server.advice.dencrypt;

import cn.hutool.core.date.StopWatch;
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
import vip.ylove.annotation.StEncryptSkip;
import vip.ylove.config.StConfig;
import vip.ylove.sdk.dto.StBody;
import vip.ylove.sdk.dto.StResult;
import vip.ylove.sdk.server.encrypt.StAbstractResponseEncrypt;
import vip.ylove.sdk.util.StKeyUtil;

import java.util.concurrent.TimeUnit;

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

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getMethod().isAnnotationPresent(StEncrypt.class) ) {
            log.debug("线程[{}]->加密请求结果-->是否需要加密:{}",Thread.currentThread().getId(),"是");
            return true;
        }else  if(stConfig.isEnableGlobalEncrypt()){ //开启全局验证
            if (!returnType.getMethod().isAnnotationPresent(StEncryptSkip.class) ){ //是否跳过方法
                log.debug("线程[{}]->加密请求结果-->是否需要加密:{}",Thread.currentThread().getId(),"是");
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.debug("线程[{}]->加密请求结果-->开始加密",Thread.currentThread().getId());
        StopWatch stopWatch = new StopWatch("加密响应");
        stopWatch.start("开始加密");
        Object result = null;
        //响应结果实现了StResult则能判断请求结果正确性
        if( body instanceof  StResult){
            StResult stBody =(StResult) body;
            if(stBody.isSuccess()){
                result = stEncrypt.encrypt(stConfig.getPrivateKey(),StKeyUtil.getKey(), JSONUtil.toJsonStr(body));
            }else{
                result = body;
            }
        }else{
            log.debug("响应结果未实现StResult接口，将会导致异常结果不能直接显示的，也会被加密的问题");
            result = stEncrypt.encrypt(stConfig.getPrivateKey(),StKeyUtil.getKey(), JSONUtil.toJsonStr(body));
        }

        stopWatch.stop();
        log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        log.debug("线程[{}]->加密请求结果-->加密完成",Thread.currentThread().getId());
        return result;
    }
}
