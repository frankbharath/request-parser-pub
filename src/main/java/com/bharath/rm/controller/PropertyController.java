package com.bharath.rm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.dto.APIRequestResponse;
import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.service.interfaces.PropertyService;

/**
 * The Class PropertyController.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 2, 2020 6:39:41 PM
 * Property controller provides end points for all property related operations
 */
@Controller
@RequestMapping("/api/property")
public class PropertyController {
	
	/** The property service. */
	PropertyService propertyService;
	
	/**
	 * Instantiates a new property controller.
	 *
	 * @param propertyService the property service
	 */
	@Autowired
	public PropertyController(PropertyService propertyService) {
		this.propertyService=propertyService;
	}

	/**
	 * Deletes properties.
	 *
	 * @param propertyIds the property ids
	 * @return the response entity
	 */
	@DeleteMapping
	public ResponseEntity<Object> deleteProperties(@RequestParam List<Long> propertyIds) {
		propertyService.deleteProperty(propertyIds);
		APIRequestResponse response=Utils.getApiRequestResponse(propertyIds.size()>1?I18NConfig.getMessage("success.properties.deleted_success"):I18NConfig.getMessage("success.property.deleted_success"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Adds the house property.
	 *
	 * @param houseDTO the house DTO
	 * @return the response entity
	 */
	@RequestMapping(value = "/house", method = RequestMethod.POST)
	public ResponseEntity<Object> addHouse(HouseDTO houseDTO) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.property.added_success"), propertyService.addHouse(houseDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Updates the house property.
	 *
	 * @param houseDTO the house DTO
	 * @return the response entity
	 */
	@RequestMapping(value = "/house", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateHouse(HouseDTO houseDTO) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.property.updated_success"), propertyService.updateHouse(houseDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Adds the apartment property.
	 *
	 * @param apartmentDTO the apartment DTO
	 * @return the response entity
	 */
	@RequestMapping(value = "/appartment", method = RequestMethod.POST)
	public ResponseEntity<Object> addApartment(ApartmentDTO apartmentDTO) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.property.added_success"), propertyService.addAppartment(apartmentDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Updates the apartment property.
	 *
	 * @param apartmentDTO the apartment DTO
	 * @param deleteIds the delete ids
	 * @return the response entity
	 */
	@RequestMapping(value = "/appartment", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateAppartment(ApartmentDTO apartmentDTO, @RequestParam Optional<List<Long>> deleteIds) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.property.updated_success"), propertyService.updateAppartment(apartmentDTO, deleteIds.orElse(new ArrayList<>())));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Gets all the properties.
	 *
	 * @param searchQuery the search query
	 * @param pageNo the page no
	 * @param countRequired the count required
	 * @param allPropwithMeta the all propwith meta
	 * @return the properties
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> getProperties(@RequestParam Optional<String> searchQuery, @RequestParam Optional<Integer> pageNo, @RequestParam Optional<Boolean> countRequired, @RequestParam Optional<Boolean> allPropwithMeta) {
		Map<String, Object> map=new HashMap<>();
		APIRequestResponse response;
		if(allPropwithMeta.orElse(false)) {
			response=Utils.getApiRequestResponse("", propertyService.getAllPropertiesWithMetaData());
		}else {
			map.put("propertylist", propertyService.getAllProperties(searchQuery.orElse(null), pageNo.orElse(null)));
			if(countRequired.orElse(false)) {
				map.put("count", propertyService.getPropertiesCount(searchQuery.orElse(null)));
			}
			response=Utils.getApiRequestResponse("", map);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Gets the house details.
	 *
	 * @param propertyId the property id
	 * @return the house details
	 */
	@RequestMapping(value = "/house/{propertyId}", method = RequestMethod.GET)
	public ResponseEntity<Object> getHouseDetails(@PathVariable Long propertyId) {
		APIRequestResponse response=Utils.getApiRequestResponse("", propertyService.getHouseDetails(propertyId));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Gets the apartment details.
	 *
	 * @param propertyId the property id
	 * @return the appartment details
	 */
	@RequestMapping(value = "/appartment/{propertyId}", method = RequestMethod.GET)
	public ResponseEntity<Object> getApartmentDetails(@PathVariable Long propertyId) {
		APIRequestResponse response=Utils.getApiRequestResponse("", propertyService.getApartmentDetails(propertyId));
		return new ResponseEntity<>(response, HttpStatus.OK);
	} 
	
	/**
	 * Gets the properties statistics.
	 *
	 * @return the properties statistics
	 */
	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public ResponseEntity<Object> getPropertiesStatistics() {
		APIRequestResponse response=Utils.getApiRequestResponse("", propertyService.getStatistics());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
