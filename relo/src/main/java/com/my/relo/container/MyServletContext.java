package com.my.relo.container;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
//@ComponentScan(basePackages = {"com.my.customer.control",
//		"com.my.product.control",
//		"com.my.cart.control",
//		"com.my.order.control",
//		"com.my.advice",
//		"com.my.board.control"})
@EnableWebMvc  //WebApplicationContext 컨테이너
public class MyServletContext implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("http://192.168.0.154:5500")
//		.allowedOrigins("http://192.168.200.146:5500")
		.allowCredentials(true)
		.allowedMethods("GET","POST","PUT","DELETE");
	}
	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver irvr = new InternalResourceViewResolver();
		irvr.setPrefix("/WEB-INF/views/");
		irvr.setSuffix(".jsp");
		return irvr;
	}
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver cmr = new CommonsMultipartResolver();
		cmr.setDefaultEncoding("UTF-8");
		cmr.setMaxUploadSize(1000*10240);
		cmr.setMaxUploadSizePerFile(100*10240);
		return cmr;
	}
	
//  @Override
//  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
////      converters.add(jackson2HttpMessageConverter());
//      converters.add(stringHttpMessageConverter());
//  }
//
//  @Bean
//  public StringHttpMessageConverter stringHttpMessageConverter() {
//      return new StringHttpMessageConverter();
//  }

}
