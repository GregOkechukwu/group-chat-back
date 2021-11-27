package com.groupchatback.config;

import com.groupchatback.interceptor.CookieInterceptor;
import com.groupchatback.util.EnvConfigUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptorAdapter[] interceptors = new HandlerInterceptorAdapter[] {
                new CookieInterceptor()
        };

        for (int i = 0; i < interceptors.length; i++) {
            registry.addInterceptor(interceptors[i])
                    .addPathPatterns(EnvConfigUtil.getProperty("ALL_PATHS"))
                    .excludePathPatterns(EnvConfigUtil.getProperty("ADMIN_PATHS"));
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("Content-Type", "application/json")
                .allowedMethods("GET", "PUT", "POST", "DELETE")
                .allowedOrigins("*");
    }

    @Bean(name = "localMetaData")
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public LocalMetaData localMetaData() {
        return new LocalMetaData();
    }

}


