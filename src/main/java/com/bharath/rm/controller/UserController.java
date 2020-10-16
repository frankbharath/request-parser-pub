package com.bharath.rm.controller;

import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.dto.APIRequestResponse;
import com.bharath.rm.model.domain.User;
import com.bharath.rm.service.interfaces.UserService;

/**
 * The Class UserController.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 17, 2020 7:55:12 PM
 * User controller provides all the user related operations
 */

@Controller
public class UserController {
	
	/** The user service. */
	UserService userService;
	
	/**
	 * Instantiates a new user controller.
	 *
	 * @param userService the user service
	 */
	@Autowired
	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	/**
	 * Get login view for the user.
	 *
	 * @return login.html
	 */
	@GetMapping(value = "/login")
	public String loginGet() {
		return "login";
	}
	
	/**
	 * Gets register view for the user.
	 *
	 * @return register.html
	 */
	@GetMapping(value = "/register")
	public String registerGet() {
		return "register";
	}
	
	/**
	 * Adds new user to the database.
	 *
	 * @param user the user
	 * @param confirmpassword the confirmpassword
	 * @return the response entity
	 * @throws MessagingException the messaging exception
	 */
	@PostMapping(value = "/register")
	public ResponseEntity<Object> registerPost(@ModelAttribute("user") User user, @RequestParam("confirmpassword") String confirmpassword) throws MessagingException {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.user.added_success",new Object[] {I18NConfig.getMessage("verification.sent")}), userService.addUser(user, confirmpassword));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Verifies user account.
	 *
	 * @param token the token
	 * @param model the model
	 * @return verificationstatus.html
	 */
	@GetMapping(value = "/verify")
	public String verifyUser(@RequestParam("token") String token, Model model) {
		userService.verifyAccountForUser(token);
		return "verificationstatus";
	}
	
	/**
	 * Gets reset password view for the user.
	 *
	 * @param model the model
	 * @param token the token
	 * @return resetpassword.html
	 */
	@GetMapping(value = "/reset")
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

	/**
	 * Sends a reset password link to user
	 *
	 * @param email the email
	 * @return the response entity
	 * @throws MessagingException the messaging exception
	 */
	@PostMapping(value = "/reset")
	public ResponseEntity<Object> resetPasswordpost(@RequestParam("email") String email) throws MessagingException {
		userService.resetPassword(email);
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("html.resetpassword.resetsent"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Updates user password
	 *
	 * @param password the password
	 * @param confirmPassword the confirm password
	 * @param token the token
	 * @return the response entity
	 * @throws MessagingException the messaging exception
	 */
	@PutMapping(value = "/reset")
	public ResponseEntity<Object> resetPasswordput(@RequestParam("password") String password, @RequestParam("confirmpassword") String confirmPassword, @RequestParam("token") String token) throws MessagingException {
		userService.updatePassword(token, password, confirmPassword);
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("html.resetpassword.success"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

	/**
	 * Changes the user password within the session.
	 *
	 * @param password the password
	 * @param confirmPassword the confirm password
	 * @return the response entity
	 * @throws MessagingException the messaging exception
	 */
	@PutMapping(value = "/api/settings")
	public ResponseEntity<Object> changePassword(@RequestParam("password") String password, @RequestParam("confirmpassword") String confirmPassword) throws MessagingException {
		userService.changePassword(password, confirmPassword);
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.password.change"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Gets the home view.
	 *
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(value = "/home")
	public String home(Model model) {
		model.addAttribute("username", Utils.getUserEmail().split("@")[0]);
		return "home";
	}
	
}
