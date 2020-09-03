package com.bharath.rm.model.domain;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Aug 11, 2020 2:59:28 PM
 	* Class Description
*/
public class House extends Property{
	private PropertyDetails propertydetails;

	/**
	 * @return the propertydetails
	 */
	public PropertyDetails getPropertydetails() {
		return propertydetails;
	}

	/**
	 * @param propertydetails the propertydetails to set
	 */
	public void setPropertydetails(PropertyDetails propertydetails) {
		this.propertydetails = propertydetails;
	}
}
