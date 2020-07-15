package com.bharath.rm.controller;

import javax.mail.MessagingException;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bharath.rm.model.domain.User;
import com.bharath.rm.model.domain.Verification;
import com.bharath.rm.service.interfaces.UserService;
import com.stripe.exception.StripeException;

@Controller
@RequestMapping("/api/user")
public class UserController {
	
	UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() throws StripeException {
		//System.out.println(testInject.toString());
		
		//test.getUserList();
		//logger.info(allParams.entrySet());
		//testDAO.getUserList();
		return "test";
	}
	  
	@ResponseBody
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signupPost(@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("type") String type) throws MessagingException {
		User user=new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setType(type);
		user.setCreationtime(System.currentTimeMillis());
		return userService.addUser(user).toString();
	}
	
	@RequestMapping(value = "/verify/{userid}", method = RequestMethod.GET)
	public String registerGet(@PathParam("userid") Long userid, @RequestParam("token") String token, Model model) {
		Verification verification=new Verification();
		verification.setUserid(userid);
		verification.setToken(token);
		model.addAttribute("response", userService.verifyAccountForUser(verification));
		return "verificationstatus";
	}
	
}
