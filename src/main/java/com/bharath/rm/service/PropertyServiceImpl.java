package com.bharath.rm.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.transform.impl.AddPropertyTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.Appartment;
import com.bharath.rm.model.domain.AppartmentPropertyDetails;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.Property;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.service.interfaces.PropertyService;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 2, 2020 6:50:17 PM
 	* Class Description
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class PropertyServiceImpl implements PropertyService {
	
	private PropertyDAO propertyDAO;
	
	@Autowired
	public PropertyServiceImpl(PropertyDAO propertyDAO) {
		this.propertyDAO=propertyDAO;
	}

	@Override
	public House addHouse(House house) {
		if(propertyDAO.propertyNameExists(house.getName())) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.name_exists"));
		}
		Long propertyId=addProperty(house);
		house.setPropertyid(propertyId);
		PropertyDetails propertyDetails = house.getPropertydetails();
		propertyDetails.setPropertyid(propertyId);
		propertyDetails.setPropertydetailsid(propertyDAO.addPropertyDetails(propertyDetails));
		return house;
	}
	
	@Override
	public Appartment addAppartment(Appartment appartment) {
		if(propertyDAO.propertyNameExists(appartment.getName())) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.name_exists"));
		}
		Long propertyId=addProperty(appartment);
		List<AppartmentPropertyDetails> appartmentDetails=appartment.getList();
		for(AppartmentPropertyDetails appartmentDetail:appartmentDetails) {
			appartmentDetail.setPropertyid(propertyId);
		}
		propertyDAO.addAppartmentDetailsToProperty(appartmentDetails);
		return appartment;
	}

	@Override
	public Long addProperty(Property property) {
		Long propertyTypeId=propertyDAO.getPropertyTypeId(property.getType().getPropertytype());
		property.getType().setPropertytypeid(propertyTypeId);
		Long propertyId=propertyDAO.addProperty(property);
		Address address=property.getAddress();
		address.setPropertyid(propertyId);
		propertyDAO.addAddressToProperty(address);
		return propertyId;
	}

	public void getAllProperties() {
		
	}
}
