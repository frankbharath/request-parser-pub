package com.bharath.rm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bharath.rm.constants.Constants;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.House;
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
	@ResponseBody
	public String addHouse() {
		House house=new House();
		house.setUserid(1l);
		house.setName("Maison Alfort");
		house.setCreationtime(System.currentTimeMillis());
		Address address=new Address();
		address.setAddress_1("10 Bis");
		address.setAddress_2("Place des Martrys De La Deportation");
		address.setPostal(94400l);
		address.setVille("Vitry Sur Seine");
		house.setAddress(address);
		PropertyType type=new PropertyType();
		type.setPropertytype(Constants.PropertyType.HOUSE.toString());
		house.setType(type);
		PropertyDetails details=new PropertyDetails();
		details.setArea(10);
		details.setCapacity(10);
		details.setRent(50.0f);
		house.setPropertydetails(details);
		return propertyService.addHouse(house).toString();
	}
}
