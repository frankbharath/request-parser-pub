package com.bharath.rm.model;

/**
 * The Class LoginRequest.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 22, 2020 6:01:23 PM
 */
public class LoginRequest {
	
	/** The email. */
	private String email;
	
	/** The password. */
	private String password;
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
