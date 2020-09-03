package com.bharath.rm.dao.interfaces;

import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.PropertyDetails;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 2, 2020 6:53:42 PM
 	* Class Description
*/
public interface PropertyDAO {
	public boolean propertyNameExists(String name);
	public Long getPropertyTypeId(String propertyType);
	public Long addHouse(House house);
	public void addAddressToProperty(Address address);
	public void addPropertyDetails(PropertyDetails propertyDetails);
}
