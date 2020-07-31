package com.bharath.rm.configuration;

import java.util.HashMap;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 24, 2020 12:01:13 AM
 	* Class Description
*/
public class RentPalThreadLocal {
	 private static final ThreadLocal<HashMap<String, Object>> LOCAL = new ThreadLocal<>();
	 
	 public static void init() {
		 LOCAL.set(new HashMap<String, Object>());
	 }
	 
	 public static void add(String key, Object value) {
		 LOCAL.get().put(key, value);
	 }
	 
	 public static Object get(String key) {
		 HashMap<String, Object> map=LOCAL.get();
		 return map.getOrDefault(key, null);
	 }
	 public static void clear() {
		 LOCAL.get().clear();
	 }
}
