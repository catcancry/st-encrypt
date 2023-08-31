package vip.ylove.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.ylove.server.advice.dencrypt.StRequestHandlerIntercepter;

@Configuration
public class StIntercepterConfig implements WebMvcConfigurer {
    @Autowired
    private StConfig stConfig;

    @Autowired
    private StRequestHandlerIntercepter stRequestHandlerIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(stRequestHandlerIntercepter);
        if(!stConfig.getStIntercepterConfig().getPatterns().isEmpty()){
            interceptorRegistration.addPathPatterns(stConfig.getStIntercepterConfig().getPatterns());
        }
        if(!stConfig.getStIntercepterConfig().getExcludePatterns().isEmpty()){
            interceptorRegistration.excludePathPatterns(stConfig.getStIntercepterConfig().getExcludePatterns());
        }
        interceptorRegistration.order(stConfig.getStIntercepterConfig().getOrder());
    }
}