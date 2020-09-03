package com.bharath.rm.model.domain;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Aug 11, 2020 2:50:39 PM
 	* Class Description
*/
public class Property {
	private Long userid;
	private Long propertyid;
	private String name;
	private Long creationtime;
	private PropertyType type;
	private Address address;
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
	 * @return the propertyid
	 */
	public Long getPropertyid() {
		return propertyid;
	}
	/**
	 * @param propertyid the propertyid to set
	 */
	public void setPropertyid(Long propertyid) {
		this.propertyid = propertyid;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	public Long getCreationtime() {
		return creationtime;
	}
	public void setCreationtime(Long creationtime) {
		this.creationtime = creationtime;
	}
	/**
	 * @return the type
	 */
	public PropertyType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(PropertyType type) {
		this.type = type;
	}
	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
}
