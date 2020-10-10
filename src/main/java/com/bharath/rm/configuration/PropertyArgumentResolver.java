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

import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.ApartmentPropertyDetailDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.PropertyDetailsDTO;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 14, 2020 12:28:28 AM
 	* Class Description
*/
public class PropertyArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(HouseDTO.class) || parameter.getParameterType().equals(ApartmentDTO.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		
		if(parameter.getParameterType().equals(HouseDTO.class)) {
			HouseDTO houseDTO=new HouseDTO();
			houseDTO.setPropertyname(webRequest.getParameter("propertyname"));
			houseDTO.setAddressline_1(webRequest.getParameter("addressline_1"));
			houseDTO.setAddressline_2(webRequest.getParameter("addressline_2"));
			houseDTO.setCity(webRequest.getParameter("city"));
			houseDTO.setPostal(webRequest.getParameter("postal"));
			houseDTO.setPropertytype(webRequest.getParameter("propertytype"));
			
			JSONObject propertyDetail=new JSONObject(webRequest.getParameter("propertydetails"));
			PropertyDetailsDTO detailsDTO=new PropertyDetailsDTO();
			detailsDTO.setArea((float) propertyDetail.getInt("area"));
			detailsDTO.setCapacity(propertyDetail.getInt("capacity"));
			detailsDTO.setRent((float) propertyDetail.getInt("rent"));
			houseDTO.setDetailsDTO(detailsDTO);
			if(webRequest.getParameter("propertyid")!=null) {
				houseDTO.setPropertyid(Long.parseLong(webRequest.getParameter("propertyid")));
				detailsDTO.setPropertydetailsid(propertyDetail.getLong("propertydetailsid"));
			}			
			return houseDTO;
		}else {
			ApartmentDTO apartmentDTO=new ApartmentDTO();
			apartmentDTO.setPropertyname(webRequest.getParameter("propertyname"));
			if(webRequest.getParameter("propertyid")!=null) {
				apartmentDTO.setPropertyid(Long.parseLong(webRequest.getParameter("propertyid")));
			}		
			apartmentDTO.setPropertyname(webRequest.getParameter("propertyname"));
			apartmentDTO.setAddressline_1(webRequest.getParameter("addressline_1"));
			apartmentDTO.setAddressline_2(webRequest.getParameter("addressline_2"));
			apartmentDTO.setCity(webRequest.getParameter("city"));
			apartmentDTO.setPostal(webRequest.getParameter("postal"));
			apartmentDTO.setPropertytype(webRequest.getParameter("propertytype"));
			
			JSONArray units=new JSONArray(webRequest.getParameter("units"));
			List<ApartmentPropertyDetailDTO> unitList=new ArrayList<>();
			for(int i=0;i<units.length();i++) {
				JSONObject unitObject=new JSONObject(units.get(i).toString());
				ApartmentPropertyDetailDTO detailsDTO=new ApartmentPropertyDetailDTO();
				if(unitObject.has("propertydetailsid")) {
					detailsDTO.setPropertydetailsid(unitObject.getLong("propertydetailsid"));
				}
				detailsDTO.setDoorno(unitObject.getString("doorno"));
				detailsDTO.setFloorno(unitObject.getInt("floorno"));
				detailsDTO.setArea((float) unitObject.getInt("area"));
				detailsDTO.setCapacity(unitObject.getInt("capacity"));
				detailsDTO.setRent((float) unitObject.getInt("rent"));
				unitList.add(detailsDTO);
			}
			apartmentDTO.setUnits(unitList);
			return apartmentDTO;
		}
	}

}
