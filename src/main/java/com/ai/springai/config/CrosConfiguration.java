package com.ai.springai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrosConfiguration implements WebMvcConfigurer {

    /**
     * 重新配置跨域规则
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")   // 允许跨域访问所有后端接口
                .allowedMethods("PUT","GET","POST","DELETE")  // 允许跨域的请求方式
                .allowedOrigins("*")  // 允许所有来源的跨域请求
                .allowedHeaders("*");  // 允许所有请求头

//                .allowedOrigins("http://localhost:5173")  // 允许跨域访问的地址，从这个地址发来的请求都允许跨域
//                .allowCredentials(true)  // 允许跨域时携带一些验证信息
//                .maxAge(3600);  // 预检请求缓存1小时（减少重复请求）类似连接池

    }
}
