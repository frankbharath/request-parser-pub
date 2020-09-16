package com.bharath.rm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 16, 2020 12:39:40 AM
 	* Class Description
*/
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController implements ErrorController {

	 @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	 public String handleError(HttpServletRequest request, Model model) {
		 model.addAttribute("message", request.getAttribute("message"));
		 return "error";
	 }
	 
	 @Deprecated
	 @Override
	 public String getErrorPath() {
		return null;
	 }

}
