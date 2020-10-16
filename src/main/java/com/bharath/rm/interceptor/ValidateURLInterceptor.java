package com.bharath.rm.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.bharath.rm.security.ValidateRequest;

/**
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 15, 2020 3:01:17 PM
 * This intercepts request and validates the parameters contained in each request
 */

public class ValidateURLInterceptor implements HandlerInterceptor {
	
	/** The Constant SKIPVALIDITYCHECK. */
	private final static List<String> SKIPVALIDITYCHECK= new ArrayList<>();
	
	/**
	 * Skips validity check for below URLs
	 */
	static {
		SKIPVALIDITYCHECK.add("/");
		SKIPVALIDITYCHECK.add("/resources/.*");
		SKIPVALIDITYCHECK.add("/favicon.ico");
		SKIPVALIDITYCHECK.add("/error");
	}
	
	/**
	 * Validates the request before it is handled by corresponding controllers
	 *
	 * @param request the request
	 * @param response the response
	 * @param handler the handler
	 * @return true, if successful
	 * @throws Exception the exception
	 */
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
