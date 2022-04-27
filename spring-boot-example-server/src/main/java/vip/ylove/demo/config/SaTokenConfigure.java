package vip.ylove.demo.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token的路由拦截器
        registry.addInterceptor(new SaRouteInterceptor())
            //功能特殊性.所以只验证码上传相关接口
            .addPathPatterns("/upload/**","/file/**")
             //不验证相关地址，相对于上面多余
            .excludePathPatterns("/user/doLogin");
    }

    // Sa-Token 整合 jwt (Style模式) Stateless 模式：服务器完全无状态
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForStateless();
    }

}
