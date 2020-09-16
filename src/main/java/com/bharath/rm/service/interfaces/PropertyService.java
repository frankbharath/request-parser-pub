package com.bharath.rm.service.interfaces;

import org.json.JSONObject;

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
	public House addHouse(House house);
	public Long addProperty(Property property);
	public Appartment addAppartment(Appartment appartment);
}
