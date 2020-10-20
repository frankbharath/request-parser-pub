package com.bharath.rm.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.ErrorCodes;

/**
 * The Class CustomAuthenticationEntryPointHandler.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 8, 2020 9:38:16 PM
 */

public class CustomAuthenticationEntryPointHandler implements  AuthenticationEntryPoint   {
	 
	/**
	 * Commence.
	 *
	 * @param request the request
	 * @param response the response
	 * @param authException the auth exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		if(Utils.isAjaxRequest(request)) {
	        Utils.sendJSONErrorResponse(response, Utils.getApiException(I18NConfig.getMessage("error.session_timeout"), HttpStatus.UNAUTHORIZED, ErrorCodes.SESSION_EXPIRED));
		}else {
		    new DefaultRedirectStrategy().sendRedirect(request, response, "/login");
		}
	}

}
