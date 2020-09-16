package com.bharath.rm.security;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.exception.APIException;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 8, 2020 9:38:16 PM
 	* Class Description
*/
public class CustomAuthenticationEntryPointHandler implements  AuthenticationEntryPoint   {
	 
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
