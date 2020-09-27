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
		address.setAddressline_1(addressDetail.getString("line_1"));
		address.setAddressline_2(addressDetail.getString("line_2"));
		address.setCity(addressDetail.getString("city"));
		address.setPostal(addressDetail.getString("postalcode"));
		
		if(parameter.getParameterType().equals(House.class)) {
			House house=new House();
			house.setUserid(1l);
			house.setPropertyname(webRequest.getParameter("name"));
			house.setAddress(address);
			house.setPropertytype(webRequest.getParameter("propertytype"));
			JSONObject propertyDetail=new JSONObject(webRequest.getParameter("propertydetails"));
			PropertyDetails details=new PropertyDetails();
			details.setArea(propertyDetail.getInt("area"));
			details.setCapacity(propertyDetail.getInt("capacity"));
			details.setRent(propertyDetail.getInt("rent"));
			house.setPropertydetails(details);
			if(webRequest.getParameter("propertyid")!=null) {
				house.setPropertyid(Long.parseLong(webRequest.getParameter("propertyid")));
				details.setPropertydetailsid(propertyDetail.getLong("propertydetailsid"));
			}else {
				house.setCreationtime(System.currentTimeMillis());
			}			
			return house;
		}else {
			Appartment appartment=new Appartment();
			appartment.setUserid(1l);
			appartment.setPropertyname(webRequest.getParameter("name"));
			if(webRequest.getParameter("propertyid")!=null) {
				appartment.setPropertyid(Long.parseLong(webRequest.getParameter("propertyid")));
			}else {
				appartment.setCreationtime(System.currentTimeMillis());
			}			
			appartment.setAddress(address);
			appartment.setPropertytype(webRequest.getParameter("propertytype"));
			JSONArray units=new JSONArray(webRequest.getParameter("units"));
			List<AppartmentPropertyDetails> unitList=new ArrayList<>();
			for(int i=0;i<units.length();i++) {
				JSONObject unitObject=new JSONObject(units.get(i).toString());
				AppartmentPropertyDetails details=new AppartmentPropertyDetails();
				if(unitObject.has("propertydetailsid")) {
					details.setPropertydetailsid(unitObject.getLong("propertydetailsid"));
				}
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
