package vip.ylove.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.ylove.sdk.server.dencrypt.StDefaultRequestAbstractAuth;
import vip.ylove.sdk.server.dencrypt.StDefaultRequestDencrypt;
import vip.ylove.sdk.server.dencrypt.StAbstractAuth;
import vip.ylove.sdk.server.dencrypt.StAbstractRequestDencrypt;
import vip.ylove.sdk.server.encrypt.StDefaultResponseEncrypt;
import vip.ylove.sdk.server.encrypt.StAbstractResponseEncrypt;
import vip.ylove.server.advice.handler.StAuthInfoHandler;

import java.util.List;

@Component
public class InitStDefaultBean {

    private static final Logger log = LoggerFactory.getLogger(InitStDefaultBean.class);

    @Bean
    @ConditionalOnMissingBean(StAbstractRequestDencrypt.class)
    public StAbstractRequestDencrypt StAbstractDencryptInitDefault(){
        log.debug("没有自定义StAbstractDencrypt,使用默认设置");
        return new StDefaultRequestDencrypt();
    }

    @Bean
    @ConditionalOnMissingBean(StAbstractResponseEncrypt.class)
    public StAbstractResponseEncrypt StAbstractEncryptInitDefault(){
        log.debug("没有自定义StAbstractEncrypt,使用默认设置");
        return new StDefaultResponseEncrypt();
    }

    @Bean
    @ConditionalOnMissingBean(StAbstractAuth.class)
    public StAbstractAuth StAbstractAuthInitDefault(){
        log.debug("没有自定义StAbstractAuth,使用默认设置");
        return new StDefaultRequestAbstractAuth();
    }

    @Bean
    public WebMvcConfigurer StAuthHandlerConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                WebMvcConfigurer.super.addArgumentResolvers(resolvers);
                resolvers.add(new StAuthInfoHandler());
            }
        };
    }
}
