package com.bharath.rm.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bharath.rm.model.SpringUserDetails;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 15, 2020 3:42:16 PM
 	* Class Description
*/
@Configuration
public class UserSession {
	
	public boolean isAuthenticated() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
	        return false;
	    }
	    return authentication.isAuthenticated();
	}
    
    public SpringUserDetails getLoggedInUserDetails() {
		return (SpringUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
    
}
