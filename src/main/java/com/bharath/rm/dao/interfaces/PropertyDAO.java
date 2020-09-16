package com.bharath.rm.dao.interfaces;

import java.util.List;

import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.AppartmentPropertyDetails;
import com.bharath.rm.model.domain.Property;
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
	public Long addProperty(Property property);
	public void addAddressToProperty(Address address);
	public Long addPropertyDetails(PropertyDetails propertyDetails);
	public void addAppartmentDetailsToProperty(List<AppartmentPropertyDetails> appartmentPropertyDetails);
}
