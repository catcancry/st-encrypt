package vip.ylove.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import vip.ylove.server.advice.dencrypt.StFilter;

@Configuration
public class StFilterConfig {

    @Autowired
    private StConfig config;

    @Bean
    public FilterRegistrationBean<StFilter> xssFilterRegistrationBean() {
        StFilter stFilter = new StFilter();
        FilterRegistrationBean<StFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(stFilter);
        registration.setName(config.getStFilterConfig().getName());
        registration.addUrlPatterns(config.getStFilterConfig().getUrlPatterns());
        registration.setOrder(config.getStFilterConfig().getOrder());
        return registration;
    }

}