package ca.uhn.fhir.jpa.starter;

import ca.uhn.fhir.jpa.starter.controller.DummyAuthController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpHead;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@EnableWebMvc
public class DummyAuthConfig implements WebMvcConfigurer {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
      converters.add(new MappingJackson2HttpMessageConverter());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    if (HapiProperties.getCorsEnabled()) {
      registry.addMapping("/login")
        .allowedOrigins(
          HapiProperties.getCorsAllowedOrigin().split(",")
        )
        .allowedHeaders(
          HttpHeaders.ORIGIN,
          HttpHeaders.ACCEPT,
          HttpHeaders.CONTENT_TYPE,
          HttpHeaders.AUTHORIZATION,
          HttpHeaders.CACHE_CONTROL,
          "x-fhir-starter",
          "X-Requested-With",
          "Prefer"
        ).exposedHeaders(
          HttpHeaders.LOCATION,
          HttpHeaders.CONTENT_LOCATION
        ).allowedMethods(
         "GET",
         "POST",
         "PUT",
         "DELETE",
         "OPTIONS",
         "PATCH",
         "HEAD"
      );
    }


  }

  @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.indentOutput(true);
        ObjectMapper m = b.build();
        return m;
    }

    @Bean
    public DummyAuthController dummyAuthController() {
        return new DummyAuthController();
    }
}
