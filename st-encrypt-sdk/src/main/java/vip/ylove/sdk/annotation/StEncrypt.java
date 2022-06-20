package vip.ylove.sdk.annotation;

import java.lang.annotation.*;

/**
 *  完成自动加密解密
 *  req  是否需要解密请求参数
 *  resp 是否需要加密响应参数
 *  auth 是否需要进行auth验证
 *  data 补充参数，用于在进行auth时使用,传递额外参数
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StEncrypt {
    /**
     * 是否需要解密请求参数
     * @return 是否需要
     */
    boolean req() default true;

    /**
     * 是否需要加密响应参数
     * @return 是否需要
     */
    boolean resp() default true;

    /**
     * 是否需要进行auth验证
     * @return 是否需要
     */
    boolean auth() default true;

    /**
     * 补充参数，用于在进行auth时使用,传递额外参数
     * @return 补充参数
     */
    String[] data() default {};



}
