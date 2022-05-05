package vip.ylove.server.advice.handler;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import vip.ylove.annotation.StAuth;
import vip.ylove.sdk.common.StAuthInfo;
import vip.ylove.sdk.util.StAuthUtil;

public class StAuthInfoHandler implements HandlerMethodArgumentResolver {
    // 使用自定义的注解
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(StAuth.class);
    }
    // 将值注入参数
    @Override
    public StAuthInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,NativeWebRequest webRequest, WebDataBinderFactory binderFactory)  {
        return StAuthUtil.auth.get();
    }
}