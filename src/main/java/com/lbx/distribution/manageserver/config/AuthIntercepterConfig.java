package com.lbx.distribution.manageserver.config;

import com.lbx.distribution.manageserver.auth.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName: 拦截器配置
 * @Description: //
 */
@Configuration
public class AuthIntercepterConfig extends WebMvcConfigurerAdapter {

    //关键，将拦截器作为bean写入配置中
    @Bean
    public AuthInterceptor authInterceptor(){
        return new AuthInterceptor();
    }

    /**
     * <p>Description:重写增加自定义拦截器的注册，某一个拦截器需要先注册进来，才能工作</p>
     * param[1]: null
     * return:
     */
     @Override
     public void addInterceptors(InterceptorRegistry registry) {

         registry.addInterceptor(authInterceptor()).addPathPatterns("/**");

         super.addInterceptors(registry);

     }

}
