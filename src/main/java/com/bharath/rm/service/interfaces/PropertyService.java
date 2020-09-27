package com.bharath.rm.service.interfaces;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.PropertyDTO;
import com.bharath.rm.model.domain.Appartment;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.Property;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 2, 2020 6:42:30 PM
 	* Class Description
*/
public interface PropertyService {
	public HouseDTO addHouse(House house);
	public Long addProperty(Property property);
	public ApartmentDTO addAppartment(Appartment appartment);
	public List<PropertyDTO> getAllProperties(String searchQuery, Integer pageNo);
	public Map<String, Object> getAllPropertiesWithMetaData();
	public Integer getPropertiesCount(String searchQuery);
	public HouseDTO getHouseDetails(Long propertyId);
	public ApartmentDTO getApartmentDetails(Long propertyId);
	public void deleteProperty(List<Long> propertyIds);
	public void updateProperty(Property property);
	public HouseDTO updateHouse(House house);
	public ApartmentDTO updateAppartment(Appartment appartment, List<Long> deleteUnits);
}
