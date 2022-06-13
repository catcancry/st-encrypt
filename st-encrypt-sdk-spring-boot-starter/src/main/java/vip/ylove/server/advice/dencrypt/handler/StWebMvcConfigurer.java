package vip.ylove.server.advice.dencrypt.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private StRequestHandlerIntercepter serverDencryNotRequestBodyHandlerIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serverDencryNotRequestBodyHandlerIntercepter);
    }

    @Bean
    public FilterRegistrationBean servletRegistrationBean() {
        StFilter userInfoFilter = new StFilter();
        FilterRegistrationBean<StFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(userInfoFilter);
        bean.setName("st-encrypt-filter");
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return bean;
    }
}