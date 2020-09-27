package com.bharath.rm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.transform.impl.AddPropertyTransformer;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.PropertyDTO;
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
	public HouseDTO addHouse(House house) {
		Long propertyId=addProperty(house);
		house.setPropertyid(propertyId);
		PropertyDetails propertyDetails = house.getPropertydetails();
		propertyDetails.setPropertyid(propertyId);
		propertyDetails.setPropertydetailsid(propertyDAO.addPropertyDetails(propertyDetails));
		return propertyDAO.getHouse(house.getUserid(), house.getPropertyid());
	}
	
	@Override
	public ApartmentDTO addAppartment(Appartment appartment) {
		Long propertyId=addProperty(appartment);
		appartment.setPropertyid(propertyId);
		List<AppartmentPropertyDetails> appartmentDetails=appartment.getList();
		for(AppartmentPropertyDetails appartmentDetail:appartmentDetails) {
			appartmentDetail.setPropertyid(propertyId);
		}
		propertyDAO.addAppartmentDetailsToProperty(appartmentDetails);
		ApartmentDTO apartmentDTO=propertyDAO.getAppartment(appartment.getUserid(), appartment.getPropertyid());
		apartmentDTO.setUnits(propertyDAO.getApartmentUnits(appartment.getPropertyid()));
		return apartmentDTO;
	}

	@Override
	public Long addProperty(Property property) {
		if(propertyDAO.propertyNameExists(property.getPropertyname())) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.name_exists"));
		}
		Long propertyId=propertyDAO.addProperty(property);
		return propertyId;
	}
	
	@Override
	public HouseDTO updateHouse(House house) {
		updateProperty(house);
		PropertyDetails propertyDetails = house.getPropertydetails();
		propertyDetails.setPropertyid(house.getPropertyid());
		propertyDAO.updatePropertyDetails(propertyDetails);
		return propertyDAO.getHouse(house.getUserid(), house.getPropertyid());
	}

	@Override
	public ApartmentDTO updateAppartment(Appartment appartment, List<Long> deleteUnits) {
		updateProperty(appartment);
		List<AppartmentPropertyDetails> appartmentDetails=appartment.getList();
		List<AppartmentPropertyDetails> newUnits=new ArrayList<>();
		List<AppartmentPropertyDetails> updateUnits=new ArrayList<>();
		for(AppartmentPropertyDetails detail:appartmentDetails) {
			detail.setPropertyid(appartment.getPropertyid());
			if(detail.getPropertydetailsid()==null) {
				newUnits.add(detail);
			}else {
				updateUnits.add(detail);
			}
		}
		if(!deleteUnits.isEmpty()) {
			propertyDAO.deleteAppartmentDetailsToProperty(appartment.getUserid(), deleteUnits);
		}
		if(newUnits.isEmpty() && updateUnits.isEmpty()) {
			throw new APIRequestException(I18NConfig.getMessage("error.appartment_property_no_unit"));
		}
		if(!newUnits.isEmpty()) {
			propertyDAO.addAppartmentDetailsToProperty(newUnits);
		}
		if(!updateUnits.isEmpty()) {
			propertyDAO.updateAppartmentDetailsToProperty(updateUnits);
		}
		ApartmentDTO apartmentDTO=propertyDAO.getAppartment(appartment.getUserid(), appartment.getPropertyid());
		apartmentDTO.setUnits(propertyDAO.getApartmentUnits(appartment.getPropertyid()));
		return apartmentDTO;
	}
	
	@Override
	public void updateProperty(Property property) {
		if(!propertyDAO.propertyExists(property.getUserid(), property.getPropertyid(), property.getPropertytype())) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.not_found"));
		}
		if(propertyDAO.propertyNameExists(property.getPropertyname(), property.getPropertyid())) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.name_exists"));
		}
		propertyDAO.updateProperty(property);
	}
	
	@Override
	public List<PropertyDTO> getAllProperties(String searchQuery, Integer pageNo) {
		Long userId=1l;
		return propertyDAO.getAllProperties(userId, searchQuery, pageNo);
	}
	
	@Override
	public Map<String, Object> getAllPropertiesWithMetaData() {
		Long userId=1l;
		Map<String, Object> map=new HashMap<>();
		List<PropertyDTO> propertyDTOs=propertyDAO.getAllProperties(userId, null, null, true);
		map.put("properties", propertyDTOs);
		List<Long> houseIds=new ArrayList<>();
		List<Long> appartmentIds=new ArrayList<>();
		for(PropertyDTO propertyDTO:propertyDTOs) {
			if(Constants.PropertyType.APPARTMENT.toString().equals(propertyDTO.getPropertytype())) {
				appartmentIds.add(propertyDTO.getPropertyid());
			}else {
				houseIds.add(propertyDTO.getPropertyid());
			}
		}
		if(!houseIds.isEmpty()) {
			map.put("housedetails",propertyDAO.getHouseDetails(houseIds));
		}
		if(!appartmentIds.isEmpty()) {
			map.put("apartmentdetails",propertyDAO.getApartmentUnits(appartmentIds));
		}
		return map;
	}
	
	@Override
	public Integer getPropertiesCount(String searchQuery) {
		Long userId=1l;
		return propertyDAO.getPropertiesCount(userId, searchQuery);
	}
	
	@Override
	public HouseDTO getHouseDetails(Long propertyId) {
		Long userId=1l;
		HouseDTO houseDTO=propertyDAO.getHouse(userId, propertyId);
		if(houseDTO==null) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.not_found"));
		}
		return houseDTO;
	}
	
	@Override
	public ApartmentDTO getApartmentDetails(Long propertyId) {
		Long userId=1l;
		ApartmentDTO apartmentDTO=propertyDAO.getAppartment(userId, propertyId);
		if(apartmentDTO==null) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.not_found"));
		}
		apartmentDTO.setUnits(propertyDAO.getApartmentUnits(propertyId));
		return apartmentDTO;
	}
	
	@Override
	public void deleteProperty(List<Long> propertyIds) {
		Long userId=1l;
		propertyDAO.deleteProperty(propertyIds, userId);
	}
}
