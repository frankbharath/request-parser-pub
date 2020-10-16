package com.bharath.rm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bharath.rm.configuration.I18NConfig;

/**
 * The Class CustomErrorController.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 16, 2020 12:39:40 AM
 * This class provides a custom error handling page for run time exceptions
 */
@Controller
public class CustomErrorController implements ErrorController {
	
	 /**
 	 * Handle error.
 	 *
 	 * @param request the request
 	 * @param model the model
 	 * @return the string
 	 */
 	@RequestMapping(value="/error", produces = MediaType.TEXT_HTML_VALUE)
	 public String handleError(HttpServletRequest request, Model model) {
		 String errorMessage=I18NConfig.getMessage("error.unkown_issue");
		 if(request.getAttribute("message")!=null) {
			 errorMessage=request.getAttribute("message").toString();
		 }
		 model.addAttribute("message", errorMessage);
		 return "error";
	 }
	 
	 /**
 	 * Gets the error path.
 	 *
 	 * @return the error path
 	 */
 	@Deprecated
	 @Override
	 public String getErrorPath() {
		return null;
	 }

}
