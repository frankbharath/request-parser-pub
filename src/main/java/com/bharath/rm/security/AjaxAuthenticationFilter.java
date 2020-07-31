package com.bharath.rm.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 22, 2020 5:46:46 PM
 	* Class Description
*/
public class AjaxAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getParameter("email"), request.getParameter("password"));
		setDetails(request, token);
		return this.getAuthenticationManager().authenticate(token);
    }

}
