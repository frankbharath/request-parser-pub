package com.bharath.rm.model.domain;
// TODO: Auto-generated Javadoc

/**
 * The Class Property.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Aug 11, 2020 2:50:39 PM
 * Class Description
 */
public class Property {
	
	/** The userid. */
	private Long userid;
	
	/** The propertyid. */
	private Long propertyid;
	
	/** The name. */
	private String propertyname;
	
	/** The creationtime. */
	private Long creationtime;
	
	/** The type. */
	private String propertytype;
	
	/** The address. */
	private Address address;
	
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
	 * Gets the propertyid.
	 *
	 * @return the propertyid
	 */
	public Long getPropertyid() {
		return propertyid;
	}
	
	/**
	 * Sets the propertyid.
	 *
	 * @param propertyid the propertyid to set
	 */
	public void setPropertyid(Long propertyid) {
		this.propertyid = propertyid;
	}
	
	
	
	/**
	 * @return the propertyname
	 */
	public String getPropertyname() {
		return propertyname;
	}

	/**
	 * @param propertyname the propertyname to set
	 */
	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
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
	 * @param creationtime the new creationtime
	 */
	public void setCreationtime(Long creationtime) {
		this.creationtime = creationtime;
	}
	
	/**
	 * @return the propertytype
	 */
	public String getPropertytype() {
		return propertytype;
	}

	/**
	 * @param propertytype the propertytype to set
	 */
	public void setPropertytype(String propertytype) {
		this.propertytype = propertytype;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 *
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
}
