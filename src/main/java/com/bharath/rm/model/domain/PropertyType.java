package com.bharath.rm.model.domain;

/**
 * The Class PropertyType.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Aug 11, 2020 2:53:30 PM
 * Class Description
 */
public class PropertyType {
	
	/** The propertytypeid. */
	private Long propertytypeid;
	
	/** The propertytype. */
	private String propertytype;
	
	/**
	 * Gets the propertytypeid.
	 *
	 * @return the propertytypeid
	 */
	public Long getPropertytypeid() {
		return propertytypeid;
	}
	
	/**
	 * Sets the propertytypeid.
	 *
	 * @param propertytypeid the propertytypeid to set
	 */
	public void setPropertytypeid(Long propertytypeid) {
		this.propertytypeid = propertytypeid;
	}
	
	/**
	 * Gets the propertytype.
	 *
	 * @return the propertytype
	 */
	public String getPropertytype() {
		return propertytype;
	}
	
	/**
	 * Sets the propertytype.
	 *
	 * @param propertytype the propertytype to set
	 */
	public void setPropertytype(String propertytype) {
		this.propertytype = propertytype;
	}
}
