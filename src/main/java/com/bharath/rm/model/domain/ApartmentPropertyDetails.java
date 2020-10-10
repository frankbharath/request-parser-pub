package com.bharath.rm.model.domain;

// TODO: Auto-generated Javadoc
/**
 * The Class ApartmentPropertyDetails.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Aug 11, 2020 3:13:29 PM
 * Class Description
 */
public class ApartmentPropertyDetails {
	
	/** The property details. */
	private PropertyDetails propertyDetails;
	
	/** The apartmentpropertydetailsid. */
	private Long apartmentpropertydetailsid;
	
	/** The doorno. */
	private String doorno;
	
	/** The floorno. */
	private int floorno;
	
	/**
	 * Gets the apartmentpropertydetailsid.
	 *
	 * @return the apartmentpropertydetailsid
	 */
	public Long getApartmentpropertydetailsid() {
		return apartmentpropertydetailsid;
	}

	/**
	 * Sets the apartmentpropertydetailsid.
	 *
	 * @param apartmentpropertydetailsid the apartmentpropertydetailsid to set
	 */
	public void setApartmentpropertydetailsid(Long apartmentpropertydetailsid) {
		this.apartmentpropertydetailsid = apartmentpropertydetailsid;
	}

	/**
	 * Gets the doorno.
	 *
	 * @return the doorno
	 */
	public String getDoorno() {
		return doorno;
	}
	
	/**
	 * Sets the doorno.
	 *
	 * @param doorno the doorno to set
	 */
	public void setDoorno(String doorno) {
		this.doorno = doorno;
	}
	
	/**
	 * Gets the floorno.
	 *
	 * @return the floorno
	 */
	public int getFloorno() {
		return floorno;
	}
	
	/**
	 * Sets the floorno.
	 *
	 * @param floorno the floorno to set
	 */
	public void setFloorno(int floorno) {
		this.floorno = floorno;
	}

	/**
	 * Gets the property details.
	 *
	 * @return the property details
	 */
	public PropertyDetails getPropertyDetails() {
		return propertyDetails;
	}

	/**
	 * Sets the property details.
	 *
	 * @param propertyDetails the new property details
	 */
	public void setPropertyDetails(PropertyDetails propertyDetails) {
		this.propertyDetails = propertyDetails;
	}
}
