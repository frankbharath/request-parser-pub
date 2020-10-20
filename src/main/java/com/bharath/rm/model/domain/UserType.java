package com.bharath.rm.model.domain;

/**
 * The Class UserType.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 27, 2020 10:18:09 PM
 */

public class UserType {
	
	/** The typeid. */
	private Long typeid;
	
	/** The type. */
	private String type;
	
	/**
	 * Gets the typeid.
	 *
	 * @return the typeid
	 */
	public Long getTypeid() {
		return typeid;
	}
	
	/**
	 * Sets the typeid.
	 *
	 * @param typeid the typeid to set
	 */
	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
