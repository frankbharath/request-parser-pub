package com.bharath.rm.exception;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 15, 2020 10:48:25 PM
 	* Class Description
*/
public class AjaxNoHandlerException extends RuntimeException {
	
	public AjaxNoHandlerException(String message) {
		super(message);
	}
	
	public AjaxNoHandlerException(String message, Throwable cause) {
		super(message, cause);
	}
}
