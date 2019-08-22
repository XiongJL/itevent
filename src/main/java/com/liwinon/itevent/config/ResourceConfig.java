package com.liwinon.itevent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ResourceConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //addResourceHandler是指你想在url请求的路径
    //addResourceLocations是图片存放的真实路径
        registry.addResourceHandler("/itevent/img/**").addResourceLocations("file:D://ITEvent/file/export/img/");
        super.addResourceHandlers(registry);
    }

}
