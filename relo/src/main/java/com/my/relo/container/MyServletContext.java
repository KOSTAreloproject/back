package com.my.relo.container;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@PropertySource("classpath:application.properties")
@Configuration
@EnableWebMvc // WebApplicationContext컨테이너용 설정클래스이다
public class MyServletContext implements WebMvcConfigurer {

   @Value("${client.ip}")
   private String clientIp;
   
   @Value("${client.port}")
   private String clientPort;
   
   @Override
   public void addCorsMappings(CorsRegistry registry) {

      registry.addMapping("/**").allowedOrigins("http://"+clientIp+":"+clientPort)
//      .allowedHeaders("*")
            .allowCredentials(true).allowedMethods("GET", "POST", "PUT", "DELETE");
   }

//   @Bean
//   public InternalResourceViewResolver viewResolver() {
//      InternalResourceViewResolver irvr = new InternalResourceViewResolver();
//      irvr.setPrefix("/static/");
//      irvr.setSuffix(".html");
//      return irvr;
//   }

   @Bean
   public CommonsMultipartResolver multipartResolver() {
      CommonsMultipartResolver cmr = new CommonsMultipartResolver();
      cmr.setDefaultEncoding("UTF-8");
      cmr.setMaxUploadSize(20 * 1024 * 1024);
      cmr.setMaxUploadSizePerFile(5 * 1024 * 1024);
      return cmr;
   }
   
   @Bean
   public ModelMapper modelMapper(){
       return new ModelMapper();
   }

}