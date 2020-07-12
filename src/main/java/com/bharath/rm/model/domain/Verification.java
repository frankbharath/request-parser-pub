package com.bharath.rm.model.domain;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 12, 2020 8:15:17 PM
 	* Class Description
*/
public class Verification {
	private Long userid;
	private String token;
	private Long creationtime;
	/**
	 * @return the userid
	 */
	public Long getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return the creationtime
	 */
	public Long getCreationtime() {
		return creationtime;
	}
	/**
	 * @param creationtime the creationtime to set
	 */
	public void setCreationtime(Long creationtime) {
		this.creationtime = creationtime;
	}
}
