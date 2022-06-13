//package vip.ylove.server.advice.dencrypt;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
//import vip.ylove.config.StConfig;
//import vip.ylove.sdk.annotation.StEncrypt;
//import vip.ylove.sdk.annotation.StEncryptSkip;
//import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
//import vip.ylove.sdk.server.dencrypt.StAbstractRequestDencrypt;
//
//import java.lang.reflect.Type;
//
///**
// *
// * @author catcancry
// **/
//@ControllerAdvice
//public class StServerDencryptRequestBodyAdvice implements RequestBodyAdvice {
//
//    private Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    private StAbstractRequestDencrypt stDencrypt;
//    @Autowired
//    private StConfig stConfig;
//    @Autowired
//    private StAbstractAuth stAuth ;
//
//
//    @Override
//    public boolean supports(MethodParameter p, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        StEncrypt se = p.getMethodAnnotation(StEncrypt.class);
//        if (se != null ) {
//            if(!se.req()){
//                return false;
//            }
//            return true;
//        }else  if(stConfig.isEnableGlobalEncrypt()){ //开启全局验证
//            if (!p.getMethod().isAnnotationPresent(StEncryptSkip.class) ){ //是否跳过方法
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
//        return body;
//    }
//
//    @Override
//    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
//                                           Class<? extends HttpMessageConverter<?>> converterType){
//        log.debug("线程[{}]->解密请求-->开始解密",Thread.currentThread().getId());
//        StEncrypt stEncrypt = parameter.getMethod().getAnnotation(StEncrypt.class);
//        return new DecryptHttpInputMessage(stEncrypt,stConfig.getPrivateKey(),stDencrypt,stAuth,inputMessage, stConfig.getPrivateKey());
//    }
//
//    @Override
//    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
//                                Class<? extends HttpMessageConverter<?>> converterType) {
//        log.debug("线程[{}]->解密请求-->完成",Thread.currentThread().getId());
//        return body;
//    }
//}
