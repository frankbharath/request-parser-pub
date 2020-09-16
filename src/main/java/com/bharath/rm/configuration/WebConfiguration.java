package com.bharath.rm.configuration;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bharath.rm.interceptor.ValidateURLInterceptor;
import com.bharath.rm.security.UserSession;

/**
 * @author bharath
 * @version 1.0 Creation time: Jul 16, 2020 12:26:40 AM Class Description
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer  {

	private static final Logger log = LoggerFactory.getLogger(WebConfiguration.class);

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/");
	}
	
	@Override
	public void addViewControllers (ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/login");
	}
	
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PropertyArgumentResolver());
    }
	
	@Bean
	ValidateURLInterceptor validateURLInterceptor() {
         return new ValidateURLInterceptor();
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(validateURLInterceptor());
    }
	
	@Controller
    static class FaviconController {
        @RequestMapping("favicon.ico")
        String favicon() {
            return "forward:/resources/images/favicon.svg";
        }
    }
}