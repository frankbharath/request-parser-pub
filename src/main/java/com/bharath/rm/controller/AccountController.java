package com.bharath.rm.controller;

import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.model.domain.Type;
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
	
	@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String registerPost(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("confirmpassword") String confirmpassword, @RequestParam("type") String type) throws MessagingException {
		if(!password.equals(confirmpassword)) {
			return Utils.getErrorObject(ErrorCodes.PASSWORD_MISMATCH, I18NConfig.getMessage("error.register.password_mismatch")).toString();
		}
		User user=new User();
		user.setEmail(email);
		user.setPassword(password);
		Type usertype=new Type();
		usertype.setType(type);
		user.setType(usertype);
		user.setCreationtime(System.currentTimeMillis());
		return userService.addUser(user).toString();
	}
	
	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public String verifyUser(@RequestParam("token") Optional<String> token, Model model) {
		if(token.isEmpty()) {
			model.addAttribute(Constants.STATUS, Constants.VERIFY);
		}else {
			model.addAttribute("response", userService.verifyAccountForUser(token.get()));
		}
		
		return "verificationstatus";
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String resetPasswordGet(Model model, @RequestParam("token") Optional<String> token) {
		if(token.isEmpty()) {
			model.addAttribute("display", "reset");
		}else {
			String email=userService.getUserEmailForResetToken(token.get());
			if(email!=null) {
				model.addAttribute("resettext",I18NConfig.getMessage("html.resetpassword.resetpassword", new Object[] {email}));
				model.addAttribute("display", "passwordreset");
				model.addAttribute("token", token.get());
			}else {
				model.addAttribute("display", "error");
			}
		}
		model.addAttribute("unkown_issue", I18NConfig.getMessage("error.unkown_issue"));
		return "resetpassword";
	}
	
	@ResponseBody
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String resetPasswordpost(@RequestParam("email") String email) throws MessagingException {
		return userService.resetPassword(email).toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/reset", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public String resetPasswordput(@RequestParam("password") String password, @RequestParam("confirmpassword") String confirmPassword, @RequestParam("token") String token) throws MessagingException {
		if(!password.equals(confirmPassword)) {
			return Utils.getErrorObject(ErrorCodes.PASSWORD_MISMATCH, I18NConfig.getMessage("error.register.password_mismatch")).toString();
		}
		return userService.updatePassword(token,password).toString();
	}
	
	/*@CrossOrigin
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(CsrfToken token) {
		String stripe="https://connect.stripe.com/express/oauth/authorize?redirect_uri=http://localhost:8080/api/stripe/response&client_id=ca_HbtDEi0Wpetq79s4WFTTAB5fVQFxjRIa&state="+token.getToken();
		return "redirect:" + stripe;
	}*/
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String method(CsrfToken token, Model model) {
		if(userService.userCreatedStripeAccount()) {
			model.addAttribute("show", "home");
		}else {
			String type=userService.getUserType();
			if(type.equals(Constants.UserTye.OWNER.toString())) {
				model.addAttribute("message", I18NConfig.getMessage("html.home.stripeownertext"));
			}else {
				model.addAttribute("message", I18NConfig.getMessage("html.home.stripetenanttext"));
			}
			model.addAttribute("show", "showstripe");
		}
		return "home";
	}
	
}
