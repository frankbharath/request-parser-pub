package com.bharath.rm.dao.interfaces;

import com.bharath.rm.constants.Constants.Tokentype;
import com.bharath.rm.model.domain.User;
import com.bharath.rm.model.domain.Verification;

/**
 * The Interface UserDAO.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 9, 2020 8:55:32 PM
 * This interface contains all mandatory function that should be implemented when class implements.
 */
public interface UserDAO {
	
	/**
	 * User exist.
	 *
	 * @param email the email
	 * @return true, if successful
	 */
	public boolean userExist(String email);
	
	/**
	 * Gets the user type.
	 *
	 * @param type the type
	 * @return the user type
	 */
	public long getUserType(String type); 
	
	/**
	 * Adds the user.
	 *
	 * @param user the user
	 * @return the long
	 */
	public long addUser(User user);
	
	/**
	 * Adds the verification code.
	 *
	 * @param verification the verification
	 */
	public void addVerificationCode(Verification verification);
	
	/**
	 * Gets the verification code.
	 *
	 * @param token the token
	 * @param type the type
	 * @return the verification code
	 */
	public Verification getVerificationCode(String token, Tokentype type);
	
	/**
	 * Delete verification code.
	 *
	 * @param verification the verification
	 */
	public void deleteVerificationCode(Verification verification);
	
	/**
	 * Verify user account.
	 *
	 * @param userid the userid
	 */
	public void verifyUserAccount(long userid);
	
	/**
	 * Checks if is user account verified.
	 *
	 * @param userid the userid
	 * @return true, if is user account verified
	 */
	boolean isUserAccountVerified(long userid);
	
	/**
	 * Gets the user email.
	 *
	 * @param userid the userid
	 * @return the user email
	 */
	public String getUserEmail(long userid);
	
	/**
	 * Delete user account.
	 *
	 * @param userId the user id
	 */
	public void deleteUserAccount(long userId);
	
	/**
	 * Gets the user id.
	 *
	 * @param email the email
	 * @return the user id
	 */
	public Long getUserId(String email);
	
	/**
	 * Gets the user email for token.
	 *
	 * @param token the token
	 * @param type the type
	 * @return the user email for token
	 */
	public String getUserEmailForToken(String token, Tokentype type);
	
	/**
	 * Update password.
	 *
	 * @param userId the user id
	 * @param password the password
	 */
	public void updatePassword(long userId, String password);
	
	/**
	 * Gets the user.
	 *
	 * @param email the email
	 * @return the user
	 */
	public User getUser(String email);
	
	/**
	 * Verification status.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	public Boolean verificationStatus(long userId);
	
	/**
	 * Gets the user type.
	 *
	 * @param userId the user id
	 * @return the user type
	 */
	public String getUserType(long userId);
}
