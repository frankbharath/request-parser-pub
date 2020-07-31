package com.bharath.rm.dao;

import com.bharath.rm.constants.Constants.Tokentype;
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
	public void addVerificationCode(Verification verification);
	public Verification getVerificationCode(String token, Tokentype type);
	public void deleteVerificationCode(Verification verification);
	public void verifyUserAccount(long userid);
	boolean isUserAccountVerified(long userid);
	public String getUserEmail(long userid);
	public void deleteUserAccount(long userId);
	public Long getUserId(String email);
	public String getUserEmailForToken(String token, Tokentype type);
	public void updatePassword(long userId, String password);
	public User getUser(String email);
	public Boolean verificationStatus(long userId);
	public Boolean userCreatedStripeAccount(long userId);
	public String getUserType(long userId);
}
