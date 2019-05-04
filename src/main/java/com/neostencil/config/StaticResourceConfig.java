package com.neostencil.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
@EnableWebMvc
public class StaticResourceConfig implements WebMvcConfigurer {


  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    registry.addResourceHandler( "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg" ,
        "**/*.jpeg", "**/*.ttf", "**/*.svg", "**/*.woff")
        .addResourceLocations("classpath:/static/")
        .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)).resourceChain(false)
        .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
  }
}