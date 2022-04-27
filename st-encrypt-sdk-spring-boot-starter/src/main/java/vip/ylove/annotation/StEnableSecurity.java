package vip.ylove.annotation;

import org.springframework.context.annotation.Import;
import vip.ylove.config.InitStDefaultBean;
import vip.ylove.config.StConfig;
import vip.ylove.server.advice.dencrypt.StServerEncryptResponseBodyAdvice;
import vip.ylove.server.advice.encrypt.StServerDencryptRequestBodyAdvice;

import java.lang.annotation.*;

/**
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({StConfig.class,
        InitStDefaultBean.class,
        StServerEncryptResponseBodyAdvice.class,
        StServerDencryptRequestBodyAdvice.class})
public @interface StEnableSecurity {

}
