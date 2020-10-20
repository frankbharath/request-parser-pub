package com.bharath.rm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.dao.interfaces.UserDAO;
import com.bharath.rm.model.SpringUserDetails;
import com.bharath.rm.model.domain.User;

/**
 * The Class SpringUserDetailsService.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 29, 2020 7:50:41 PM
 */

@Service
public class SpringUserDetailsService implements UserDetailsService {
	
	/** The User DAO. */
	UserDAO UserDAO;
	
	/**
	 * Instantiates a new spring user details service.
	 *
	 * @param userDAOImpl the user DAO impl
	 */
	@Autowired
	public SpringUserDetailsService(UserDAO userDAOImpl) {
		this.UserDAO=userDAOImpl;
	}

	/**
	 * Load user by username.
	 *
	 * @param email the email
	 * @return the user details
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user=UserDAO.getUser(email);
		if(user!=null) {
			SpringUserDetails userDetails = new SpringUserDetails(user.getEmail(), user.getPassword());
			userDetails.setEnabled(user.isVerified());
			userDetails.setUserId(user.getUserid());
			return userDetails;
		}
		throw new UsernameNotFoundException(I18NConfig.getMessage("error.user.no_account"));
	}
}
