package com.three.transfer.config.web;

import com.three.transfer.interceptor.UserLoginInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements WebMvcConfigurer {

    /**
     * 权限拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String interceptorPath = "/transfer/**";
        //注册拦截器
        InterceptorRegistration loginIR = registry.addInterceptor(new UserLoginInterceptor());
        loginIR.addPathPatterns(interceptorPath);
        loginIR.excludePathPatterns("/transfer");
        loginIR.excludePathPatterns("/transfer/login");
        loginIR.excludePathPatterns("/transfer/register");
        loginIR.excludePathPatterns("/transfer/logincheck");
        loginIR.excludePathPatterns("/transfer/registercheck");
        loginIR.excludePathPatterns("/transfer/share/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

    }
}
