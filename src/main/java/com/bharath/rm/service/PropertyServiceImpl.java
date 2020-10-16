package com.bharath.rm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.rm.common.DTOModelMapper;
import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.ApartmentPropertyDetailDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.PropertyDTO;
import com.bharath.rm.dto.PropertyDetailsDTO;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.domain.Apartment;
import com.bharath.rm.model.domain.ApartmentPropertyDetails;
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
	
	private DTOModelMapper dtoModelMapper;
	
	@Autowired
	public PropertyServiceImpl(PropertyDAO propertyDAO, DTOModelMapper dtoModelMapper) {
		this.dtoModelMapper=dtoModelMapper;
		this.propertyDAO=propertyDAO;
	}

	@Override
	public HouseDTO addHouse(HouseDTO houseDTO) {
		//adding the property to the database
		House house=dtoModelMapper.houseDTOModelMapper(houseDTO);
		Long propertyId=addProperty(house);
		
		// updating the property id 
		house.setPropertyid(propertyId);
		
		// updating the property id to property details
		PropertyDetails propertyDetails = house.getPropertydetails();
		propertyDetails.setPropertyid(propertyId);
		
		// adding the property details to the database
		Long propertyDetailsId=propertyDAO.addPropertyDetails(propertyDetails);
		
		// updating the property details id to property details
		propertyDetails.setPropertydetailsid(propertyDetailsId);
	
		// convert domain model to DTO
		return dtoModelMapper.houseModelDTOMapper(house);
	}
	
	@Override
	public ApartmentDTO addAppartment(ApartmentDTO apartmentDTO) {
		//adding the property to the database
		Apartment apartment=dtoModelMapper.apartmentDTOModelMapper(apartmentDTO);
		Long propertyId=addProperty(apartment);
		
		// updating the property id 
		apartment.setPropertyid(propertyId);
		
		// add apartment details to the database 
		propertyDAO.addAppartmentDetailsToProperty(apartment.getUnits(), propertyId);
		
		// get apartment information
		//Apartment getApartment=propertyDAO.getAppartment(apartment.getUserid(), apartment.getPropertyid());
		//getApartment.setUnits(propertyDAO.getApartmentUnits(getApartment.getPropertyid()));
		
		// convert domain model to DTO
		return dtoModelMapper.apartmentModelDTOMapper(apartment);
	}

	@Override
	public Long addProperty(Property property) {
		//check if property name already exists
		if(propertyDAO.propertyNameExists(property.getUserid(), property.getPropertyname())) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.name_exists"));
		}
		Long propertyId=propertyDAO.addProperty(property);
		return propertyId;
	}
	
	@Override
	public HouseDTO updateHouse(HouseDTO houseDTO) {
		//update the property
		House house=dtoModelMapper.houseDTOModelMapper(houseDTO);
		updateProperty(house);
		
		//update the property id to property details, just in case
		PropertyDetails propertyDetails = house.getPropertydetails();
		propertyDetails.setPropertyid(house.getPropertyid());
		
		//update the property details to the database
		propertyDAO.updatePropertyDetails(propertyDetails);
		
		// convert domain model to DTO
		return dtoModelMapper.houseModelDTOMapper(house);
	}

	@Override
	public ApartmentDTO updateAppartment(ApartmentDTO apartmentDTO, List<Long> deleteUnits) {
		//update the property
		Apartment apartment=dtoModelMapper.apartmentDTOModelMapper(apartmentDTO);
		updateProperty(apartment);
		
		List<ApartmentPropertyDetails> appartmentDetails=apartment.getUnits();
		// for new units
		List<ApartmentPropertyDetails> newUnits=new ArrayList<>();
		// for old units
		List<ApartmentPropertyDetails> updateUnits=new ArrayList<>();
		
		for(ApartmentPropertyDetails detail:appartmentDetails) {
			if(detail.getApartmentpropertydetailsid()==null) {
				newUnits.add(detail);
			}else {
				updateUnits.add(detail);
			}
		}
		
		//delete the units
		if(!deleteUnits.isEmpty()) {
			propertyDAO.deleteAppartmentDetailsToProperty(apartment.getPropertyid(), deleteUnits);
		}
		// at least one unit should be present in the apartment
		if(newUnits.isEmpty() && updateUnits.isEmpty()) {
			throw new APIRequestException(I18NConfig.getMessage("error.appartment_property_no_unit"));
		}
		// add the new units
		if(!newUnits.isEmpty()) {
			propertyDAO.addAppartmentDetailsToProperty(newUnits, apartment.getPropertyid());
		}
		// update the old units
		if(!updateUnits.isEmpty()) {
			propertyDAO.updateAppartmentDetailsToProperty(updateUnits, apartment.getPropertyid());
		}
		
		// get apartment information
		Apartment getApartment=propertyDAO.getAppartment(apartment.getUserid(), apartment.getPropertyid());
		getApartment.setUnits(propertyDAO.getApartmentUnits(apartment.getPropertyid()));
		
		// convert domain model to DTO
		return dtoModelMapper.apartmentModelDTOMapper(getApartment);
	}
	
	@Override
	public void updateProperty(Property property) {
		// check if property exists for the user for given type
		if(!propertyDAO.propertyExists(property.getUserid(), property.getPropertyid(), property.getPropertytype())) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.not_found"));
		}
		
		// check if there is another property with same name
		if(propertyDAO.propertyNameExists(property.getUserid(), property.getPropertyname(), property.getPropertyid())) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.name_exists"));
		}
		propertyDAO.updateProperty(property);
	}
	
	@Override
	public List<PropertyDTO> getAllProperties(String searchQuery, Integer pageNo) {
		Long userId=Utils.getUserId();
		List<Property> propertyList=propertyDAO.getAllProperties(userId, searchQuery, pageNo);
		List<PropertyDTO> propertyDTOList=new ArrayList<>();
		for(Property property:propertyList) {
			propertyDTOList.add(dtoModelMapper.propertyModelDTOMapper(property));
		}
		return propertyDTOList;
	}
	
	@Override
	public HashMap<String, Object> getAllPropertiesWithMetaData() {
		Long userId=Utils.getUserId();
		HashMap<String, Object> map=new HashMap<>();
		List<Property> propertyList=propertyDAO.getAllProperties(userId, null, null, true);
		List<PropertyDTO> propertyDTOList=new ArrayList<>();
		for(Property property:propertyList) {
			propertyDTOList.add(dtoModelMapper.propertyModelDTOMapper(property));
		}
		map.put("properties", propertyDTOList);
		List<Long> houseIds=new ArrayList<>();
		List<Long> appartmentIds=new ArrayList<>();
		for(PropertyDTO propertyDTO:propertyDTOList) {
			if(Constants.PropertyType.APPARTMENT.toString().equals(propertyDTO.getPropertytype())) {
				appartmentIds.add(propertyDTO.getPropertyid());
			}else {
				houseIds.add(propertyDTO.getPropertyid());
			}
		}
		if(!houseIds.isEmpty()) {
			HashMap<Long, PropertyDetails> houseDetail=propertyDAO.getHouseDetails(houseIds);
			HashMap<Long, PropertyDetailsDTO> houseDetailDTO=new HashMap<>();
			for (Map.Entry<Long, PropertyDetails>  entry : houseDetail.entrySet()) {
				houseDetailDTO.put(entry.getKey(), dtoModelMapper.propertyDetailModelDTOMapper(entry.getValue()));
			}
			map.put("housedetails", houseDetailDTO);
		}
		
		if(!appartmentIds.isEmpty()) {
			HashMap<Long, List<ApartmentPropertyDetails>> apartmentDetails=propertyDAO.getApartmentUnits(appartmentIds);
			HashMap<Long, List<ApartmentPropertyDetailDTO>> apartmentDetailsDTO= new HashMap<Long, List<ApartmentPropertyDetailDTO>>();
			for(Map.Entry<Long, List<ApartmentPropertyDetails>>  entry:apartmentDetails.entrySet()) {
				List<ApartmentPropertyDetails> units=entry.getValue();
				List<ApartmentPropertyDetailDTO> unitsDTO=new ArrayList<>();
				for(ApartmentPropertyDetails unit:units) {
					unitsDTO.add(dtoModelMapper.apartmentPropertyDetailsModelDTOMapper(unit));
				}
				apartmentDetailsDTO.put(entry.getKey(), unitsDTO);
			}
			map.put("apartmentdetails", apartmentDetailsDTO);
		}
		
		return map;
	}
	
	@Override
	public Integer getPropertiesCount(String searchQuery) {
		Long userId=Utils.getUserId();
		return propertyDAO.getPropertiesCount(userId, searchQuery);
	}
	
	@Override
	public HouseDTO getHouseDetails(Long propertyId) {
		Long userId=Utils.getUserId();
		House house=propertyDAO.getHouse(userId, propertyId);
		if(house==null) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.not_found"));
		}
		return dtoModelMapper.houseModelDTOMapper(house);
	}
	
	@Override
	public ApartmentDTO getApartmentDetails(Long propertyId) {
		Long userId=Utils.getUserId();
		Apartment apartment=propertyDAO.getAppartment(userId, propertyId);
		if(apartment==null) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.not_found"));
		}
		apartment.setUnits(propertyDAO.getApartmentUnits(propertyId));
		return dtoModelMapper.apartmentModelDTOMapper(apartment);
	}
	
	@Override
	public void deleteProperty(List<Long> propertyIds) {
		Long userId=Utils.getUserId();
		propertyDAO.deleteProperty(propertyIds, userId);
	}
	
	@Override
	public HashMap<String, Object> getStatistics() {
		HashMap<String, Object> respMap=new HashMap<>();
		Long userId=Utils.getUserId();
		respMap.put("propertiesCountByType", propertyDAO.getPropertiesCountByType(userId));
		respMap.put("getPropertiesOccupantInfo", propertyDAO.getPropertiesOccupantInfo(userId));
		return respMap;
	}
}
