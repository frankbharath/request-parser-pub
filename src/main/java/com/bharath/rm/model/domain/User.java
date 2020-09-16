package com.bharath.rm.model.domain;
// TODO: Auto-generated Javadoc

/**
 * The Class User.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 9, 2020 9:58:03 PM
 * Class Description
 */
public class User {
	
	/** The userid. */
	private Long userid;
	
	/** The email. */
	private String email;
	
	/** The password. */
	private String password;
	
	/** The creationtime. */
	private Long creationtime;
	
	/** The verified. */
	private boolean verified;
	
	/** The user type. */
	private UserType usertype;
	
	/**
	 * Gets the userid.
	 *
	 * @return the userid
	 */
	public Long getUserid() {
		return userid;
	}
	
	/**
	 * Sets the userid.
	 *
	 * @param userid the userid to set
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
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
	
	/**
	 * Gets the creationtime.
	 *
	 * @return the creationtime
	 */
	public Long getCreationtime() {
		return creationtime;
	}
	
	/**
	 * Sets the creationtime.
	 *
	 * @param creationtime the creationtime to set
	 */
	public void setCreationtime(Long creationtime) {
		this.creationtime = creationtime;
	}
	
	
	/**
	 * Gets the usertype.
	 *
	 * @return the usertype
	 */
	public UserType getUsertype() {
		return usertype;
	}

	/**
	 * Sets the usertype.
	 *
	 * @param usertype the new usertype
	 */
	public void setUsertype(UserType usertype) {
		this.usertype = usertype;
	}

	/**
	 * Checks if is verified.
	 *
	 * @return true, if is verified
	 */
	public boolean isVerified() {
		return verified;
	}
	
	/**
	 * Sets the verified.
	 *
	 * @param verified the new verified
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}	
}
