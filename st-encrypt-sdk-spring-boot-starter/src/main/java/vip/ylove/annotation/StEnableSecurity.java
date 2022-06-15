package vip.ylove.annotation;

import org.springframework.context.annotation.Import;
import vip.ylove.config.InitStDefaultBean;
import vip.ylove.config.StConfig;
import vip.ylove.config.StFilterConfig;
import vip.ylove.server.advice.dencrypt.StRequestHandlerIntercepter;
import vip.ylove.config.StIntercepterConfig;
import vip.ylove.server.advice.encrypt.StServerEncryptResponseBodyAdvice;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({StConfig.class,
        InitStDefaultBean.class,
        StServerEncryptResponseBodyAdvice.class,
        StRequestHandlerIntercepter.class,
        StFilterConfig.class,
        StIntercepterConfig.class})
public @interface StEnableSecurity {

}
