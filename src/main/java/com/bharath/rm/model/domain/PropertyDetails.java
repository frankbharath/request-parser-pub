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
	private Float area;
	private Integer capacity;
	private Float rent;
	private Integer occupied;
	
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
	public Float getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(Float area) {
		this.area = area;
	}
	/**
	 * @return the capacity
	 */
	public Integer getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the rent
	 */
	public Float getRent() {
		return rent;
	}
	/**
	 * @param rent the rent to set
	 */
	public void setRent(Float rent) {
		this.rent = rent;
	}
	/**
	 * @return the occupied
	 */
	public Integer getOccupied() {
		return occupied;
	}
	/**
	 * @param occupied the occupied to set
	 */
	public void setOccupied(Integer occupied) {
		this.occupied = occupied;
	}
}
