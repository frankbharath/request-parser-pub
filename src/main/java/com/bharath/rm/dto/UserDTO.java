package com.bharath.rm.dto;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 15, 2020 12:18:39 AM
*/

public class UserDTO {
	/** The userid. */
	private Long userid;
	
	/** The email. */
	private String email;
	
	/** The creationtime. */
	private String creationtime;
	
	/** The verified. */
	private boolean verified;
	
	/** The user type. */
	private String usertype;
	
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
	 * Gets the creationtime.
	 *
	 * @return the creationtime
	 */
	public String getCreationtime() {
		return creationtime;
	}
	
	/**
	 * Sets the creationtime.
	 *
	 * @param creationtime the creationtime to set
	 */
	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}
	
	
	/**
	 * Gets the usertype.
	 *
	 * @return the usertype
	 */
	public String getUsertype() {
		return usertype;
	}

	/**
	 * Sets the usertype.
	 *
	 * @param usertype the new usertype
	 */
	public void setUsertype(String usertype) {
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
