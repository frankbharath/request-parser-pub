package com.bharath.rm.service.interfaces;

import org.json.JSONObject;

import com.bharath.rm.model.domain.House;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 2, 2020 6:42:30 PM
 	* Class Description
*/
public interface PropertyService {
	public JSONObject addHouse(House house);
}
