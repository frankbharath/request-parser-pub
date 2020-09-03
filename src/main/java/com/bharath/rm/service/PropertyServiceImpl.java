package com.bharath.rm.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.House;
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
	public JSONObject addHouse(House house) {
		if(propertyDAO.propertyNameExists(house.getName())) {
			return Utils.getErrorObject(ErrorCodes.PROPERTY_NAME_EXISTS, I18NConfig.getMessage("error.property.name_exists"));
		}else {
			Long propertyTypeId=propertyDAO.getPropertyTypeId(house.getType().getPropertytype());
			house.getType().setPropertytypeid(propertyTypeId);
			Long propertyId=propertyDAO.addHouse(house);
			Address address=house.getAddress();
			address.setPropertyid(propertyId);
			propertyDAO.addAddressToProperty(address);
			PropertyDetails propertyDetails = house.getPropertydetails();
			System.out.println(propertyId);
			propertyDetails.setPropertyid(propertyId);
			propertyDAO.addPropertyDetails(propertyDetails);
			return Utils.getSuccessResponse(null, I18NConfig.getMessage("success.property.added_success"));
		}
	}


}
