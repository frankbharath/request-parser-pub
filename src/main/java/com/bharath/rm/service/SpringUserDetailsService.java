package com.bharath.rm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.dao.UserDAO;
import com.bharath.rm.dao.UserDAOImpl;
import com.bharath.rm.model.SpringUserDetails;
import com.bharath.rm.model.domain.User;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 29, 2020 7:50:41 PM
 	* Class Description
*/
@Service
public class SpringUserDetailsService implements UserDetailsService {
	
	UserDAO UserDAO;
	
	@Autowired
	public SpringUserDetailsService(UserDAOImpl userDAOImpl) {
		this.UserDAO=userDAOImpl;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user=UserDAO.getUser(email);
		if(user!=null) {
			SpringUserDetails userDetails = new SpringUserDetails(user.getEmail(), user.getPassword());
			userDetails.setUserId(user.getUserid());
			return userDetails;
		}
		throw new UsernameNotFoundException(I18NConfig.getMessage("error.user.no_account"));
	}
	
	public boolean isAuthenticated() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || AnonymousAuthenticationToken.class.
	      isAssignableFrom(authentication.getClass())) {
	        return false;
	    }
	    return authentication.isAuthenticated();
	}
	
	public SpringUserDetails getLoggedInUserDetails() {
		return (SpringUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
