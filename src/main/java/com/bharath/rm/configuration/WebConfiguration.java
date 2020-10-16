package com.bharath.rm.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bharath.rm.interceptor.ValidateURLInterceptor;

/**
 * The Class WebConfiguration.
 * 
 * @author bharath
 * @version 1.0
 * Creation time: Jul 20, 2020 10:24:16 PM
 * 
 * This class contains WebConfiguration
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer  {



	/**
	 * Adds the resource handlers.
	 *
	 * @param registry the registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/");
	}
	
	/**
	 * Adds the view controllers.
	 *
	 * @param registry the registry
	 */
	@Override
	public void addViewControllers (ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/login");
	}
	
	/**
	 * Adds the argument resolvers.
	 *
	 * @param argumentResolvers the argument resolvers
	 */
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PropertyArgumentResolver());
    }
	
	/**
	 * Validate URL interceptor.
	 *
	 * @return the validate URL interceptor
	 */
	@Bean
	ValidateURLInterceptor validateURLInterceptor() {
         return new ValidateURLInterceptor();
    }
	
	/**
	 * Adds the interceptors.
	 *
	 * @param registry the registry
	 */
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(validateURLInterceptor());
    }
	
	/**
	 * The Class FaviconController.
	 */
	@Controller
    static class FaviconController {
        
        /**
         * Forwards favicon.ico to favicon.svg.
         *
         * @return the string
         */
        @RequestMapping("favicon.ico")
        String favicon() {
            return "forward:/resources/images/favicon.svg";
        }
    }
}