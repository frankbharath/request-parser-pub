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
	
	/** The line 1. */
	private String addressline_1;
	
	/** The line 2. */
	private String addressline_2;
	
	/** The city. */
	private String city;
	
	/** The postal. */
	private String postal;
	
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
	 * @return the addressline_1
	 */
	public String getAddressline_1() {
		return addressline_1;
	}

	/**
	 * @param addressline_1 the addressline_1 to set
	 */
	public void setAddressline_1(String addressline_1) {
		this.addressline_1 = addressline_1;
	}

	/**
	 * @return the addressline_2
	 */
	public String getAddressline_2() {
		return addressline_2;
	}

	/**
	 * @param addressline_2 the addressline_2 to set
	 */
	public void setAddressline_2(String addressline_2) {
		this.addressline_2 = addressline_2;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the postal
	 */
	public String getPostal() {
		return postal;
	}

	/**
	 * @param postal the postal to set
	 */
	public void setPostal(String postal) {
		this.postal = postal;
	}

}
