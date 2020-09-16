package com.bharath.rm.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.ErrorCodes;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 16, 2020 12:11:45 AM
 	* Class Description
*/
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if(Utils.isAjaxRequest(request)) {
	        Utils.sendJSONErrorResponse(response, Utils.getApiException(I18NConfig.getMessage("error.forbidden_access"), HttpStatus.FORBIDDEN, ErrorCodes.FORBIDDEN));
		}else {
			request.setAttribute("message", I18NConfig.getMessage("error.forbidden_access"));
			RequestDispatcher rd = request.getRequestDispatcher("/error");
			rd.forward(request, response);
		}
	}

}
