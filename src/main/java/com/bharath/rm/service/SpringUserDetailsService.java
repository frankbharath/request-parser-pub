package com.bharath.rm.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bharath.rm.model.SpringUserDetails;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 29, 2020 7:50:41 PM
 	* Class Description
*/
@Service
public class SpringUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return new SpringUserDetails(username);
	}

}
