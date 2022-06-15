package vip.ylove.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.ylove.server.advice.dencrypt.StRequestHandlerIntercepter;

@Configuration
public class StIntercepterConfig implements WebMvcConfigurer {

    @Autowired
    private StRequestHandlerIntercepter stRequestHandlerIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(stRequestHandlerIntercepter);
    }
}