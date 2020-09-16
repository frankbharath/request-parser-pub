package com.bharath.rm.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bharath.rm.configuration.RentPalThreadLocal;
import com.bharath.rm.exception.APIException;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.SpringUserDetails;
import com.bharath.rm.security.SecurityXMLConfig;
import com.bharath.rm.security.UserSession;
import com.bharath.rm.security.ValidateRequest;
import com.bharath.rm.service.SpringUserDetailsService;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 15, 2020 3:01:17 PM
 	* Class Description
*/

public class ValidateURLInterceptor implements HandlerInterceptor {
	
	private final static List<String> SKIPVALIDITYCHECK= new ArrayList<>();
	
	static {
		SKIPVALIDITYCHECK.add("/");
		SKIPVALIDITYCHECK.add("/resources/.*");
		SKIPVALIDITYCHECK.add("/favicon.ico");
		SKIPVALIDITYCHECK.add("/error");
	}
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url=request.getRequestURI();
		boolean skip=SKIPVALIDITYCHECK.stream().anyMatch((str)->{
			Pattern pattern = Pattern.compile(str);
			return pattern.matcher(url).matches();
		});
		if(!skip) {
			ValidateRequest.checkValidity(request);
		}
		return true;
	}
}
