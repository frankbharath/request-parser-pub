package com.bharath.rm.controller;

import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.dto.APIRequestResponse;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.domain.User;
import com.bharath.rm.service.interfaces.UserService;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 17, 2020 7:55:12 PM
 	* Class Description
*/
@Controller
public class AccountController {
	
	UserService userService;
	
	@Autowired
	public AccountController(UserService userService) {
		this.userService=userService;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet() {
		return "login";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerGet() {
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Object> registerPost(@ModelAttribute("user") User user, @RequestParam("confirmpassword") String confirmpassword) throws MessagingException {
		if(!user.getPassword().equals(confirmpassword)) {
			throw new APIRequestException(I18NConfig.getMessage("error.register.password_mismatch"));
		}
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.user.added_success",new Object[] {I18NConfig.getMessage("verification.sent")}), userService.addUser(user));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public String verifyUser(@RequestParam("token") String token, Model model) {
		userService.verifyAccountForUser(token);
		return "verificationstatus";
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String resetPasswordGet(Model model, @RequestParam("token") Optional<String> token) {
		if(!token.isPresent()) {
			model.addAttribute("display", "reset");
		}else {
			String email=userService.getUserEmailForResetToken(token.get());
			model.addAttribute("resettext",I18NConfig.getMessage("html.resetpassword.resetpassword", new Object[] {email}));
			model.addAttribute("display", "passwordreset");
			model.addAttribute("token", token.get());
		}
		return "resetpassword";
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ResponseEntity<Object> resetPasswordpost(@RequestParam("email") String email) throws MessagingException {
		userService.resetPassword(email);
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("html.resetpassword.resetsent"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> resetPasswordput(@RequestParam("password") String password, @RequestParam("confirmpassword") String confirmPassword, @RequestParam("token") String token) throws MessagingException {
		userService.updatePassword(token, password, confirmPassword);
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("html.resetpassword.success"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

	@PutMapping(value = "/api/settings")
	public ResponseEntity<Object> changePassword(@RequestParam("password") String password, @RequestParam("confirmpassword") String confirmPassword) throws MessagingException {
		userService.changePassword(password, confirmPassword);
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.password.change"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String method(CsrfToken token, Model model) {
		return "home";
	}
	
}
