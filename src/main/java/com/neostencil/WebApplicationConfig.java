
package com.neostencil;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebApplicationConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/notFound").setViewName("we-are-sorry");
    registry.addViewController("/internalServerError").setViewName("error");
    registry.addViewController("/badRequest").setViewName("error");
    registry.addViewController("/forbidden").setViewName("error");
    registry.addViewController("/unauthorized").setViewName("error");

  }


  @Bean
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
    return container -> {
      container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notFound"),
          new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/internalServerError"),
          new ErrorPage(HttpStatus.BAD_REQUEST, "/badRequest"),
          new ErrorPage(HttpStatus.FORBIDDEN, "/forbidden"),
          new ErrorPage(HttpStatus.UNAUTHORIZED, "/unauthorized"));
    };
  }

  // Not needed as of now
  /*
   * @Override public void configurePathMatch(PathMatchConfigurer configurer) { AntPathMatcher
   * matcher = new AntPathMatcher(); matcher.setCaseSensitive(false);
   * configurer.setPathMatcher(matcher); }
   */
}