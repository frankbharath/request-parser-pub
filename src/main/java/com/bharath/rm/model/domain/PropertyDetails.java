package com.bharath.rm.model.domain;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Aug 11, 2020 2:59:50 PM
 	* Class Description
*/
public class PropertyDetails {
	private Long propertyid;
	private Long propertydetailsid;
	private float area;
	private int capacity;
	private float rent;
	
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
	 * @return the propertydetailsid
	 */
	public Long getPropertydetailsid() {
		return propertydetailsid;
	}
	/**
	 * @param propertydetailsid the propertydetailsid to set
	 */
	public void setPropertydetailsid(Long propertydetailsid) {
		this.propertydetailsid = propertydetailsid;
	}
	/**
	 * @return the area
	 */
	public float getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(int area) {
		this.area = area;
	}
	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the rent
	 */
	public float getRent() {
		return rent;
	}
	/**
	 * @param rent the rent to set
	 */
	public void setRent(float rent) {
		this.rent = rent;
	}
}
