package com.bharath.rm.dao.interfaces;

import java.util.HashMap;
import java.util.List;

import com.bharath.rm.model.domain.Apartment;
import com.bharath.rm.model.domain.ApartmentPropertyDetails;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.Property;
import com.bharath.rm.model.domain.PropertyDetails;

// TODO: Auto-generated Javadoc
/**
 * The Interface PropertyDAO.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 2, 2020 6:53:42 PM
 * Class Description
 */

public interface PropertyDAO {
	
	/**
	 * Property name exists.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public boolean propertyNameExists(String name);
	
	/**
	 * Property name exists.
	 *
	 * @param name the name
	 * @param propertyId the property id
	 * @return true, if successful
	 */
	public boolean propertyNameExists(String name, Long propertyId);
	
	/**
	 * Adds the property.
	 *
	 * @param property the property
	 * @return the long
	 */
	public Long addProperty(Property property);
	
	/**
	 * Adds the property details.
	 *
	 * @param propertyDetails the property details
	 * @return the long
	 */
	public Long addPropertyDetails(PropertyDetails propertyDetails);
	
	/**
	 * Adds the appartment details to property.
	 *
	 * @param appartmentPropertyDetails the appartment property details
	 */
	public void addAppartmentDetailsToProperty(List<ApartmentPropertyDetails> appartmentPropertyDetails, Long propertyId);
	
	/**
	 * Gets the house.
	 *
	 * @param userId the user id
	 * @param propertyId the property id
	 * @return the house
	 */
	public House getHouse(Long userId, Long propertyId);
	
	/**
	 * Gets the all properties.
	 *
	 * @param userId the user id
	 * @param searchQuery the search query
	 * @param pageNo the page no
	 * @return the all properties
	 */
	public List<Property> getAllProperties(Long userId, String searchQuery, Integer pageNo);
	
	/**
	 * Gets the all properties.
	 *
	 * @param userId the user id
	 * @param searchQuery the search query
	 * @param pageNo the page no
	 * @param allProperties the all properties
	 * @return the all properties
	 */
	public List<Property> getAllProperties(Long userId, String searchQuery, Integer pageNo, boolean allProperties);
	
	/**
	 * Gets the properties count.
	 *
	 * @param userId the user id
	 * @param searchQuery the search query
	 * @return the properties count
	 */
	public Integer getPropertiesCount(Long userId, String searchQuery);
	
	/**
	 * Gets the appartment.
	 *
	 * @param userId the user id
	 * @param propertyId the property id
	 * @return the appartment
	 */
	public Apartment getAppartment(Long userId, Long propertyId);
	
	/**
	 * Gets the apartment units.
	 *
	 * @param propertyId the property id
	 * @return the apartment units
	 */
	public List<ApartmentPropertyDetails> getApartmentUnits(Long propertyId);
	
	/**
	 * Delete property.
	 *
	 * @param propertyIds the property ids
	 * @param userId the user id
	 */
	public void deleteProperty(List<Long> propertyIds, Long userId);
	
	/**
	 * Delete property.
	 *
	 * @param propertyId the property id
	 * @param userId the user id
	 */
	public void deleteProperty(Long propertyId, Long userId);
	
	/**
	 * Update property.
	 *
	 * @param property the property
	 */
	public void updateProperty(Property property);
	
	/**
	 * Update property details.
	 *
	 * @param propertyDetails the property details
	 */
	public void updatePropertyDetails(PropertyDetails propertyDetails);
	
	/**
	 * Update appartment details to property.
	 *
	 * @param appartmentPropertyDetails the appartment property details
	 */
	public void updateAppartmentDetailsToProperty(List<ApartmentPropertyDetails> appartmentPropertyDetails, Long propertyId);
	
	/**
	 * Property exists.
	 *
	 * @param userId the user id
	 * @param propertyId the property id
	 * @param type the type
	 * @return true, if successful
	 */
	public boolean propertyExists(Long userId, Long propertyId, String type);
	
	/**
	 * Property exists.
	 *
	 * @param userId the user id
	 * @param propertyId the property id
	 * @param type the type
	 * @param propertyDetailId the property detail id
	 * @return true, if successful
	 */
	public boolean propertyExists(Long userId, Long propertyId, String type, Long propertyDetailId);
	
	/**
	 * Delete appartment details to property.
	 *
	 * @param propertyId the property id
	 * @param providedIds the provided ids
	 */
	public void deleteAppartmentDetailsToProperty(Long propertyId, List<Long> providedIds);
	
	/**
	 * Gets the house details.
	 *
	 * @param propertyIds the property ids
	 * @return the house details
	 */
	public HashMap<Long, PropertyDetails> getHouseDetails(List<Long> propertyIds);
	
	/**
	 * Gets the apartment units.
	 *
	 * @param propertyIds the property ids
	 * @return the apartment units
	 */
	public HashMap<Long, List<ApartmentPropertyDetails>> getApartmentUnits(List<Long> propertyIds);
	
	/**
	 * Gets the property details.
	 *
	 * @param propertyDetailsId the property details id
	 * @return the property details
	 */
	public PropertyDetails getPropertyDetails(Long propertyDetailsId);
	
	/**
	 * Update property occupancy.
	 *
	 * @param propertyDetailsId the property details id
	 * @param occupants the occupants
	 */
	public void updatePropertyOccupancy(Long propertyDetailsId, int occupants);

	/**
	 * Gets the property.
	 *
	 * @param userId the user id
	 * @param propertyId the property id
	 * @return the property
	 */
	public Property getProperty(Long userId, Long propertyId);

	/**
	 * Gets the apartment unit.
	 *
	 * @param propertyId the property id
	 * @param propertyDetailId the property detail id
	 * @return the apartment unit
	 */
	public ApartmentPropertyDetails getApartmentUnit(Long propertyId, Long propertyDetailId);

	public HashMap<Long, Property> getAllPropertiesForPropertyDetails(Long userId, List<Long> propertyDetailsId);

	
}
