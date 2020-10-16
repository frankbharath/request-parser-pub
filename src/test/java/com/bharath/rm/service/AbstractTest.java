package com.bharath.rm.service;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.bharath.rm.configuration.RentPalThreadLocal;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Oct 15, 2020 7:09:03 PM
 	* Class Description
*/
public abstract class AbstractTest {
	
	protected static Long testUserId=1l;
	protected static String testEmail="test@test.com";
	
	@BeforeAll
	public static void init() {
		RentPalThreadLocal.init();
		RentPalThreadLocal.add("userId", testUserId);
		RentPalThreadLocal.add("email", testEmail);
	}
	
	@AfterAll
	public static void destroy() {
		 //RentPalThreadLocal.clear();
	}
}
