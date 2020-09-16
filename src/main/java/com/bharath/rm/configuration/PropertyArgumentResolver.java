package com.bharath.rm.configuration;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.Appartment;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.model.domain.PropertyType;
import com.bharath.rm.model.domain.AppartmentPropertyDetails;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 14, 2020 12:28:28 AM
 	* Class Description
*/
public class PropertyArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(House.class) || parameter.getParameterType().equals(Appartment.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		JSONObject addressDetail=new JSONObject(webRequest.getParameter("address"));
		Address address=new Address();
		address.setLine_1(addressDetail.getString("line_1"));
		address.setLine_2(addressDetail.getString("line_2"));
		address.setCity(addressDetail.getString("city"));
		address.setPostalcode(addressDetail.getLong("postalcode"));
		PropertyType type=new PropertyType();
		type.setPropertytype(webRequest.getParameter("propertytype"));
		
		if(parameter.getParameterType().equals(House.class)) {
			House house=new House();
			house.setUserid(1l);
			house.setName(webRequest.getParameter("name"));
			house.setCreationtime(System.currentTimeMillis());
			house.setAddress(address);
			house.setType(type);
			JSONObject propertyDetail=new JSONObject(webRequest.getParameter("propertydetails"));
			PropertyDetails details=new PropertyDetails();
			details.setArea(propertyDetail.getInt("area"));
			details.setCapacity(propertyDetail.getInt("capacity"));
			details.setRent(propertyDetail.getInt("rent"));
			house.setPropertydetails(details);
			return house;
		}else {
			Appartment appartment=new Appartment();
			appartment.setUserid(1l);
			appartment.setName(webRequest.getParameter("name"));
			appartment.setCreationtime(System.currentTimeMillis());
			appartment.setAddress(address);
			appartment.setType(type);
			JSONArray units=new JSONArray(webRequest.getParameter("units"));
			List<AppartmentPropertyDetails> unitList=new ArrayList<>();
			for(int i=0;i<units.length();i++) {
				JSONObject unitObject=new JSONObject(units.get(i).toString());
				AppartmentPropertyDetails details=new AppartmentPropertyDetails();
				details.setDoorno(unitObject.getString("doorno"));
				details.setFloorno(unitObject.getInt("floorno"));
				details.setArea(unitObject.getInt("area"));
				details.setCapacity(unitObject.getInt("capacity"));
				details.setRent(unitObject.getInt("rent"));
				unitList.add(details);
			}
			appartment.setList(unitList);
			return appartment;
		}
		
	}

}
