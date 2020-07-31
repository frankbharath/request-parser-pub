package com.bharath.rm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stripe.exception.StripeException;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 24, 2020 10:06:22 PM
 	* Class Description
*/

@Controller
@RequestMapping("/api/stripe")
public class StripeController {

	@ResponseBody
	@RequestMapping(value = "/response", method = RequestMethod.GET)
	public String home() throws StripeException {
		//System.out.println(testInject.toString());
		
		//test.getUserList();
		//logger.info(allParams.entrySet());
		//testDAO.getUserList();
		return "test";
	}
	
}
