package com.bharath.rm.exception;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 14, 2020 6:53:45 PM
 	* Class Description
*/

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value= {APIRequestException.class})
	public void handleAPIRequestException(HttpServletRequest request, HttpServletResponse response, APIRequestException e) throws IOException, ServletException{
		log.error(e.getMessage(), e);
		if(Utils.isAjaxRequest(request)) {
			APIException apiException=Utils.getApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
			Utils.sendJSONErrorResponse(response, apiException);
		}else {
			request.setAttribute("message", e.getMessage());
			RequestDispatcher rd = request.getRequestDispatcher("/error");
			rd.forward(request, response);
		}
	}
	
	@ExceptionHandler(value= {URLException.class})
	public ResponseEntity<Object> handleURLException(URLException e){
		log.error(e.getMessage(), e);
		APIException apiException=Utils.getApiException(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		return new ResponseEntity<>(apiException, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(value= {Exception.class})
	public void handleGlobalException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException, ServletException{
		log.error(e.getMessage(), e);
		if(Utils.isAjaxRequest(request)) {
			APIException apiException=Utils.getApiException(I18NConfig.getMessage("error.unkown_issue"), HttpStatus.INTERNAL_SERVER_ERROR);
			Utils.sendJSONErrorResponse(response, apiException);
		}else {
			request.setAttribute("message", I18NConfig.getMessage("error.unkown_issue"));
			RequestDispatcher rd = request.getRequestDispatcher("/error");
			
			rd.forward(request, response);
		}
	}
	
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response,  NoHandlerFoundException e) throws IOException, ServletException {
    	if(Utils.isAjaxRequest(request)) {
    		APIException apiException=Utils.getApiException(e.getMessage(), HttpStatus.NOT_FOUND);
    		Utils.sendJSONErrorResponse(response, apiException);
    	}else{
    		response.sendRedirect(request.getContextPath() + "/home");
    	}
	}
}
