package com.abs.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.http.CacheControl;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imgPath = "file:" + System.getProperty("user.dir") + "/images/";
        registry.addResourceHandler("/images/**")
                .addResourceLocations(imgPath)
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic());

    }
}