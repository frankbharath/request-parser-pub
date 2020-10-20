package com.bharath.rm.service.interfaces;

import javax.mail.MessagingException;

import com.bharath.rm.dto.UserDTO;
import com.bharath.rm.model.domain.User;

/**
 * The Interface UserService.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 30, 2020 7:27:39 PM
 */

public interface UserService {
	
	/**
	 * Adds the user.
	 *
	 * @param user the user
	 * @param confirmPassword the confirm password
	 * @return the user DTO
	 * @throws MessagingException the messaging exception
	 */
	public UserDTO addUser(User user, String confirmPassword) throws MessagingException;
	
	/**
	 * Verify account for user.
	 *
	 * @param token the token
	 */
	public void verifyAccountForUser(String token);
	
	/**
	 * Send verification link to user.
	 *
	 * @param userid the userid
	 * @param email the email
	 * @throws MessagingException the messaging exception
	 */
	public void sendVerificationLinkToUser(long userid, String email) throws MessagingException;
	
	/**
	 * Reset password.
	 *
	 * @param email the email
	 * @throws MessagingException the messaging exception
	 */
	public void resetPassword(String email) throws MessagingException;
	
	/**
	 * Gets the user email for reset token.
	 *
	 * @param token the token
	 * @return the user email for reset token
	 */
	public String getUserEmailForResetToken(String token);
	
	/**
	 * Update password.
	 *
	 * @param token the token
	 * @param password the password
	 * @param confirmPassword the confirm password
	 */
	public void updatePassword(String token, String password, String confirmPassword);
	
	/**
	 * User verification status.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	public Boolean userVerificationStatus(long userId);
	
	/**
	 * Gets the user type.
	 *
	 * @return the user type
	 */
	public String getUserType();
	
	/**
	 * Change password.
	 *
	 * @param password the password
	 * @param confirmPassword the confirm password
	 */
	public void changePassword(String password, String confirmPassword);
}
