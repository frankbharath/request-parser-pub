package com.bharath.rm.exception;

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
import org.springframework.web.servlet.NoHandlerFoundException;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;

/**
 * The Class GlobalExceptionHandler.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 14, 2020 6:53:45 PM
 * This class is common exception handler that catches all exceptions and sends it to the client
 */

@ControllerAdvice
public class GlobalExceptionHandler {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Handle API request exception.
	 *
	 * @param request the request
	 * @param response the response
	 * @param e the e
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
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
	
	/**
	 * Handle URL exception.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value= {URLException.class})
	public ResponseEntity<Object> handleURLException(URLException e){
		log.error(e.getMessage(), e);
		APIException apiException=Utils.getApiException(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		return new ResponseEntity<>(apiException, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	/**
	 * Handle global exception.
	 *
	 * @param request the request
	 * @param response the response
	 * @param e the e
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
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
	
    /**
     * Handle no handler found exception.
     *
     * @param request the request
     * @param response the response
     * @param e the e
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
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
