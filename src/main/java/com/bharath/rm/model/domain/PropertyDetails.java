package com.bharath.rm.model.domain;

/**
 * The Class PropertyDetails.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Aug 11, 2020 2:59:50 PM
 */

public class PropertyDetails {
	
	/** The propertyid. */
	private Long propertyid;
	
	/** The propertydetailsid. */
	private Long propertydetailsid;
	
	/** The area. */
	private Float area;
	
	/** The capacity. */
	private Integer capacity;
	
	/** The rent. */
	private Float rent;
	
	/** The occupied. */
	private Integer occupied;
	
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
	 * Gets the propertydetailsid.
	 *
	 * @return the propertydetailsid
	 */
	public Long getPropertydetailsid() {
		return propertydetailsid;
	}
	
	/**
	 * Sets the propertydetailsid.
	 *
	 * @param propertydetailsid the propertydetailsid to set
	 */
	public void setPropertydetailsid(Long propertydetailsid) {
		this.propertydetailsid = propertydetailsid;
	}
	
	/**
	 * Gets the area.
	 *
	 * @return the area
	 */
	public Float getArea() {
		return area;
	}
	
	/**
	 * Sets the area.
	 *
	 * @param area the area to set
	 */
	public void setArea(Float area) {
		this.area = area;
	}
	
	/**
	 * Gets the capacity.
	 *
	 * @return the capacity
	 */
	public Integer getCapacity() {
		return capacity;
	}
	
	/**
	 * Sets the capacity.
	 *
	 * @param capacity the capacity to set
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * Gets the rent.
	 *
	 * @return the rent
	 */
	public Float getRent() {
		return rent;
	}
	
	/**
	 * Sets the rent.
	 *
	 * @param rent the rent to set
	 */
	public void setRent(Float rent) {
		this.rent = rent;
	}
	
	/**
	 * Gets the occupied.
	 *
	 * @return the occupied
	 */
	public Integer getOccupied() {
		return occupied;
	}
	
	/**
	 * Sets the occupied.
	 *
	 * @param occupied the occupied to set
	 */
	public void setOccupied(Integer occupied) {
		this.occupied = occupied;
	}
}
