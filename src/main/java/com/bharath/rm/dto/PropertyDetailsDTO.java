package com.bharath.rm.dto;
// TODO: Auto-generated Javadoc

/**
 * The Class PropertyDetailsDTO.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 18, 2020 11:59:20 PM
 * Class Description
 */
public class PropertyDetailsDTO {
	
	/** The propertydetailsid. */
	private Long propertyid;
	
	/** The propertydetailsid. */
	private Long propertydetailsid;
	
	/** The area. */
	private float area;
	
	/** The capacity. */
	private int capacity;
	
	/** The rent. */
	private float rent;
	
	/** The occupied. */
	private int occupied;
	
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
	public float getArea() {
		return area;
	}
	
	/**
	 * Sets the area.
	 *
	 * @param area the area to set
	 */
	public void setArea(float area) {
		this.area = area;
	}
	
	/**
	 * Gets the capacity.
	 *
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * Sets the capacity.
	 *
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * Gets the rent.
	 *
	 * @return the rent
	 */
	public float getRent() {
		return rent;
	}
	
	/**
	 * Sets the rent.
	 *
	 * @param rent the rent to set
	 */
	public void setRent(float rent) {
		this.rent = rent;
	}

	/**
	 * @return the occupied
	 */
	public int getOccupied() {
		return occupied;
	}

	/**
	 * @param occupied the occupied to set
	 */
	public void setOccupied(int occupied) {
		this.occupied = occupied;
	}

}
