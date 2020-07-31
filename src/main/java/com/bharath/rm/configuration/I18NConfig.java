package com.bharath.rm.configuration;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 23, 2020 7:25:31 PM
 	* Class Description
*/
@Configuration
public class I18NConfig implements WebMvcConfigurer  {
	
	private static MessageSource messageResource;
	
	//This bean is used in Utils.java
	@Bean("messageSource")
	public MessageSource messageSource() {
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasenames("i18n/messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    messageResource=messageSource;
	    return messageSource;
	}
	
	@Bean(name = "localeResolver")
	public LocaleResolver localeResolver() {
		CookieLocaleResolver clr = new CookieLocaleResolver();
		clr.setDefaultLocale(Locale.US);
	    return clr;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	    localeChangeInterceptor.setParamName("locale");
	    localeChangeInterceptor.setIgnoreInvalidLocale(true);
	    registry.addInterceptor(localeChangeInterceptor);
	 }
	
	public static String getMessage(String key) {
		return getMessage(key,null);
	}
	public static String getMessage(String key, Object[] objs) {
		return messageResource.getMessage(key, objs,LocaleContextHolder.getLocale());
	}
}
