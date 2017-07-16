package com.salesmanager.shop.application;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.salesmanager.core.business.configuration.CoreApplicationConfiguration;

@Configuration
@ComponentScan({"com.salesmanager.shop","com.salesmanager.core.business"})
@EnableAutoConfiguration
@Import(CoreApplicationConfiguration.class)//import sm-core configurations
@ImportResource("classpath:/spring/shopizer-shop-context.xml")
@EnableWebSecurity
public class ShopApplicationConfiguration extends WebMvcConfigurerAdapter{
/*	 @Override
	 public void addCorsMappings(CorsRegistry registry) {
	   registry.addMapping("/**")
	     .allowedOrigins("*")
	     .allowedMethods("POST", "GET",  "PUT", "OPTIONS", "DELETE")
	     .allowedHeaders("X-Auth-Token", "Content-Type")
	     .allowCredentials(false)
	     .maxAge(4800);
	 }*/
}