package com.bharath.rm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.dto.APIRequestResponse;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.Appartment;
import com.bharath.rm.model.domain.AppartmentPropertyDetails;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.Property;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.model.domain.PropertyType;
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

	@RequestMapping(value = "/house", method = RequestMethod.POST)
	public ResponseEntity<Object> addHouse(House house) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.property.added_success"), propertyService.addHouse(house));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/appartment", method = RequestMethod.POST)
	@ResponseBody
	public String addAppartment(Appartment appartment) {
		return propertyService.addAppartment(appartment).toString();
	}
}
