package com.bharath.rm.service.interfaces;

import javax.mail.MessagingException;

import com.bharath.rm.dto.UserDTO;
import com.bharath.rm.model.domain.User;


/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 30, 2020 7:27:39 PM
 	* Class Description
*/
public interface UserService {
	public UserDTO addUser(User user, String confirmPassword) throws MessagingException;
	public void verifyAccountForUser(String token);
	public void sendVerificationLinkToUser(long userid, String email) throws MessagingException;
	public void resetPassword(String email) throws MessagingException;
	public String getUserEmailForResetToken(String token);
	public void updatePassword(String token, String password, String confirmPassword);
	public Boolean userVerificationStatus(long userId);
	public String getUserType();
	public void changePassword(String password, String confirmPassword);
}
