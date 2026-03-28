package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/category_img/**")
                .addResourceLocations("file:uploads/category_img/");
        
        // Product images
        registry.addResourceHandler("/product_img/**")
                .addResourceLocations("file:uploads/product_img/");
        
        // Profile images
        registry.addResourceHandler("/profile_img/**")
                .addResourceLocations("file:uploads/profile_img/");
    }
}

