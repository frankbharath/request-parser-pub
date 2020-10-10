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
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 2, 2020 6:39:41 PM
 	* Class Description
*/
@Controller
@RequestMapping("/api/property")
public class PropertyController {
	
	PropertyService propertyService;
	
	@Autowired
	public PropertyController(PropertyService propertyService) {
		this.propertyService=propertyService;
	}

	@DeleteMapping
	public ResponseEntity<Object> deleteProperties(@RequestParam List<Long> propertyIds) {
		propertyService.deleteProperty(propertyIds);
		APIRequestResponse response=Utils.getApiRequestResponse(propertyIds.size()>1?I18NConfig.getMessage("success.properties.deleted_success"):I18NConfig.getMessage("success.property.deleted_success"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/house", method = RequestMethod.POST)
	public ResponseEntity<Object> addHouse(HouseDTO houseDTO) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.property.added_success"), propertyService.addHouse(houseDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/house", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateHouse(HouseDTO houseDTO) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.property.updated_success"), propertyService.updateHouse(houseDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/appartment", method = RequestMethod.POST)
	public ResponseEntity<Object> addAppartment(ApartmentDTO apartmentDTO) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.property.added_success"), propertyService.addAppartment(apartmentDTO));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/appartment", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateAppartment(ApartmentDTO apartmentDTO, @RequestParam Optional<List<Long>> deleteIds) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.property.updated_success"), propertyService.updateAppartment(apartmentDTO, deleteIds.orElse(new ArrayList<>())));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
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
	
	@RequestMapping(value = "/house/{propertyId}", method = RequestMethod.GET)
	public ResponseEntity<Object> getHouseDetails(@PathVariable Long propertyId) {
		APIRequestResponse response=Utils.getApiRequestResponse("", propertyService.getHouseDetails(propertyId));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/appartment/{propertyId}", method = RequestMethod.GET)
	public ResponseEntity<Object> getAppartmentDetails(@PathVariable Long propertyId) {
		APIRequestResponse response=Utils.getApiRequestResponse("", propertyService.getApartmentDetails(propertyId));
		return new ResponseEntity<>(response, HttpStatus.OK);
	} 
}
