package com.bharath.rm.model.domain;

/**
 * The Class Verification.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 12, 2020 8:15:17 PM
 */

public class Verification {
	
	/** The userid. */
	private Long userid;
	
	/** The token. */
	private String token;
	
	/** The creationtime. */
	private Long creationtime;
	
	/** The type. */
	private int type;
	
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
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * Sets the token.
	 *
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
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
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}
}
