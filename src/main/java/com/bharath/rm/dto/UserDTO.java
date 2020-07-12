package com.bharath.rm.dto;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 11, 2020 1:49:31 AM
 	* Class Description
*/
public class UserDTO {
	private Long userid;
	private String email;
	private String creationtime;
	private String type;
	private boolean verified;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the creationtime
	 */
	public String getCreationtime() {
		return creationtime;
	}
	/**
	 * @param creationtime the creationtime to set
	 */
	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the verified
	 */
	public boolean isVerified() {
		return verified;
	}
	/**
	 * @param verified the verified to set
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}
