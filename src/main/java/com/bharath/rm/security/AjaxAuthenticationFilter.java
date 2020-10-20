package com.bharath.rm.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class AjaxAuthenticationFilter.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 22, 2020 5:46:46 PM
 */
public class AjaxAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	/**
	 * Attempt authentication.
	 *
	 * @param request the request
	 * @param response the response
	 * @return the authentication
	 */
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getParameter("email"), request.getParameter("password"));
		setDetails(request, token);
		return this.getAuthenticationManager().authenticate(token);
    }

}
