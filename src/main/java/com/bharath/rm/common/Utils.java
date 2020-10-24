package com.bharath.rm.common;

/**
 * The Class Utils.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 08, 2020 03:00:10 PM
 * This class has basic common utility function that will be used by this application.
 */

public final class Utils {
	
	/**
	 * Gets Classloader from thread context for static references .
	 *
	 * @return the class loader
	 */
	public static ClassLoader classloader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static boolean isValidString(String value) {
		return value!=null && value.trim().length()!=0;
	}
	
}
