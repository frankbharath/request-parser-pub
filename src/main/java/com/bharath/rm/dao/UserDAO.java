package com.bharath.rm.dao;

import com.bharath.rm.model.domain.User;
import com.bharath.rm.model.domain.Verification;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 9, 2020 8:55:32 PM
 	* Class Description
*/
public interface UserDAO {
	 public boolean userExist(String email);
	 public long getUserType(String type); 
	 public long addUser(User user);
	 public void mapUserToType(long userid,long typeid);
	 public void addVerificationCode(Verification verification);
}
