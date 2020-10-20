package com.bharath.rm.service.interfaces;

import java.util.HashMap;
import java.util.List;

import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.PropertyDTO;
import com.bharath.rm.model.domain.Property;

/**
 * The Interface PropertyService.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 2, 2020 6:42:30 PM
 */

public interface PropertyService {
	
	/**
	 * Adds the house.
	 *
	 * @param houseDTO the house DTO
	 * @return the house DTO
	 */
	public HouseDTO addHouse(HouseDTO houseDTO);
	
	/**
	 * Adds the property.
	 *
	 * @param property the property
	 * @return the long
	 */
	public Long addProperty(Property property);
	
	/**
	 * Adds the appartment.
	 *
	 * @param apartmentDTO the apartment DTO
	 * @return the apartment DTO
	 */
	public ApartmentDTO addAppartment(ApartmentDTO apartmentDTO);
	
	/**
	 * Gets the all properties.
	 *
	 * @param searchQuery the search query
	 * @param pageNo the page no
	 * @return the all properties
	 */
	public List<PropertyDTO> getAllProperties(String searchQuery, Integer pageNo);
	
	/**
	 * Gets the all properties with meta data.
	 *
	 * @return the all properties with meta data
	 */
	public HashMap<String, Object> getAllPropertiesWithMetaData();
	
	/**
	 * Gets the properties count.
	 *
	 * @param searchQuery the search query
	 * @return the properties count
	 */
	public Integer getPropertiesCount(String searchQuery);
	
	/**
	 * Gets the house details.
	 *
	 * @param propertyId the property id
	 * @return the house details
	 */
	public HouseDTO getHouseDetails(Long propertyId);
	
	/**
	 * Gets the apartment details.
	 *
	 * @param propertyId the property id
	 * @return the apartment details
	 */
	public ApartmentDTO getApartmentDetails(Long propertyId);
	
	/**
	 * Delete property.
	 *
	 * @param propertyIds the property ids
	 */
	public void deleteProperty(List<Long> propertyIds);
	
	/**
	 * Update property.
	 *
	 * @param property the property
	 */
	public void updateProperty(Property property);
	
	/**
	 * Update house.
	 *
	 * @param houseDTO the house DTO
	 * @return the house DTO
	 */
	public HouseDTO updateHouse(HouseDTO houseDTO);
	
	/**
	 * Update appartment.
	 *
	 * @param apartmentDTO the apartment DTO
	 * @param deleteUnits the delete units
	 * @return the apartment DTO
	 */
	public ApartmentDTO updateAppartment(ApartmentDTO apartmentDTO, List<Long> deleteUnits);
	
	/**
	 * Gets the statistics.
	 *
	 * @return the statistics
	 */
	public HashMap<String, Object> getStatistics();
}
