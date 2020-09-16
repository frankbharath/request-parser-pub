package com.bharath.rm.exception;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 14, 2020 6:19:08 PM
 	* Class Description
*/
public class APIRequestException extends RuntimeException {
	
	public APIRequestException(String message) {
		super(message);
	}
	
	public APIRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}