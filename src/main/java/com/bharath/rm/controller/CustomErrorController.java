package com.bharath.rm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bharath.rm.configuration.I18NConfig;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 16, 2020 12:39:40 AM
 	* Class Description
*/
@Controller
public class CustomErrorController implements ErrorController {
	
	 @RequestMapping(value="/error", produces = MediaType.TEXT_HTML_VALUE)
	 public String handleError(HttpServletRequest request, Model model) {
		 String errorMessage=I18NConfig.getMessage("error.unkown_issue");
		 if(request.getAttribute("message")!=null) {
			 errorMessage=request.getAttribute("message").toString();
		 }
		 model.addAttribute("message", errorMessage);
		 return "error";
	 }
	 
	 @Deprecated
	 @Override
	 public String getErrorPath() {
		return null;
	 }

}
