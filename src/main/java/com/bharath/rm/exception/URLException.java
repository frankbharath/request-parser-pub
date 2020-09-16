package com.bharath.rm.exception;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 15, 2020 4:21:54 PM
 	* Class Description
*/
public class URLException extends RuntimeException{
	
	public URLException(String message) {
		super(message);
	}
	
	public URLException(String message, Throwable cause) {
		super(message, cause);
	}
}
