package com.bharath.rm.common;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 23, 2020 8:39:39 PM
 	* Class Description
*/

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.bharath.rm.constants.tables.RM_AppartmentPropertyDetails;
import com.bharath.rm.constants.tables.RM_Property;
import com.bharath.rm.constants.tables.RM_PropertyDetails;
import com.bharath.rm.constants.tables.RM_Tenant;
import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.ApartmentPropertyDetailDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.PropertyDTO;
import com.bharath.rm.dto.PropertyDetailsDTO;
import com.bharath.rm.model.domain.Address;

public class QueryUtils {
	
	public static PropertyDetailsDTO gePropertyDetailsDTO(ResultSet rs) throws SQLException {
		PropertyDetailsDTO detailsDTO=new PropertyDetailsDTO();
		detailsDTO.setPropertyid(rs.getLong(RM_PropertyDetails.PROPERTYID));
		detailsDTO.setPropertydetailsid(rs.getLong(RM_PropertyDetails.PROPERTYDETAILSID));
		detailsDTO.setArea(rs.getFloat(RM_PropertyDetails.AREA));
		detailsDTO.setCapacity(rs.getInt(RM_PropertyDetails.CAPACITY));
		detailsDTO.setRent(rs.getFloat(RM_PropertyDetails.RENT));
		detailsDTO.setRent(rs.getFloat(RM_PropertyDetails.OCCUPIED));
		return detailsDTO;
	}
	
	public static PropertyDTO getPropertyDTO(ResultSet rs) throws SQLException {
		PropertyDTO propertyDTO = new PropertyDTO();
		propertyDTO.setPropertyid(rs.getLong(RM_Property.PROPERTYID));
		propertyDTO.setPropertyname(rs.getString(RM_Property.PROPERTYNAME));
		propertyDTO.setUserid(rs.getLong(RM_Property.USERID));
		propertyDTO.setPropertytype(rs.getString(RM_Property.PROPERTYTYPE));
		propertyDTO.setCreationtime(Utils.getDate(rs.getLong(RM_Property.CREATIONTIME)));
		Address address=new Address();
		address.setAddressline_1(rs.getString(RM_Property.ADDRESSLINE_1));
		address.setAddressline_2(rs.getString(RM_Property.ADDRESSLINE_2));
		address.setCity(rs.getString(RM_Property.CITY));
		address.setPostal(rs.getString(RM_Property.POSTAL));
		propertyDTO.setAddress(address);
        return propertyDTO;
	}
	
	public static HouseDTO getHouseDTO(ResultSet rs) throws SQLException {
		HouseDTO houseDTO=new HouseDTO();
		houseDTO.setPropertyid(rs.getLong(RM_Property.PROPERTYID));
		houseDTO.setPropertyname(rs.getString(RM_Property.PROPERTYNAME));
		houseDTO.setUserid(rs.getLong(RM_Property.USERID));
		houseDTO.setPropertytype(rs.getString(RM_Property.PROPERTYTYPE));
		houseDTO.setCreationtime(Utils.getDate(rs.getLong(RM_Property.CREATIONTIME)));
		Address address=new Address();
		address.setAddressline_1(rs.getString(RM_Property.ADDRESSLINE_1));
		address.setAddressline_2(rs.getString(RM_Property.ADDRESSLINE_2));
		address.setCity(rs.getString(RM_Property.CITY));
		address.setPostal(rs.getString(RM_Property.POSTAL));
		houseDTO.setAddress(address);
		houseDTO.setDetailsDTO(gePropertyDetailsDTO(rs));
		return houseDTO;
	}
	
	public static ApartmentDTO getApartmentDTO(ResultSet rs) throws SQLException {
		ApartmentDTO apartmentDTO=new ApartmentDTO();
		apartmentDTO.setPropertyid(rs.getLong(RM_Property.PROPERTYID));
		apartmentDTO.setPropertyname(rs.getString(RM_Property.PROPERTYNAME));
		apartmentDTO.setUserid(rs.getLong(RM_Property.USERID));
		apartmentDTO.setPropertytype(rs.getString(RM_Property.PROPERTYTYPE));
		apartmentDTO.setCreationtime(Utils.getDate(rs.getLong(RM_Property.CREATIONTIME)));
		Address address=new Address();
		address.setAddressline_1(rs.getString(RM_Property.ADDRESSLINE_1));
		address.setAddressline_2(rs.getString(RM_Property.ADDRESSLINE_2));
		address.setCity(rs.getString(RM_Property.CITY));
		address.setPostal(rs.getString(RM_Property.POSTAL));
		apartmentDTO.setAddress(address);
		return apartmentDTO;
	}
	
	public static ApartmentPropertyDetailDTO geApartmentPropertyDetailDTO(ResultSet rs) throws SQLException {
		ApartmentPropertyDetailDTO detailsDTO=new ApartmentPropertyDetailDTO();
		detailsDTO.setPropertyid(rs.getLong(RM_AppartmentPropertyDetails.PROPERTYID));
		detailsDTO.setPropertydetailsid(rs.getLong(RM_AppartmentPropertyDetails.PROPERTYDETAILSID));
		detailsDTO.setArea(rs.getLong(RM_AppartmentPropertyDetails.AREA));
		detailsDTO.setCapacity(rs.getInt(RM_AppartmentPropertyDetails.CAPACITY));
		detailsDTO.setRent(rs.getFloat(RM_AppartmentPropertyDetails.RENT));
		detailsDTO.setDoorno(rs.getString(RM_AppartmentPropertyDetails.DOORNO));
		detailsDTO.setFloorno(rs.getInt(RM_AppartmentPropertyDetails.FLOORNO));
		detailsDTO.setOccupied(rs.getInt(RM_AppartmentPropertyDetails.OCCUPIED));
		return detailsDTO;
	}
	
	public static String getInsertQuery(String tableName, List<String> cols) {
		StringBuilder query=new StringBuilder("INSERT INTO ").append(tableName).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(String.join(",", cols.stream().map(val -> ":".concat(val)).collect(Collectors.toList()))).append(")");
		return query.toString();
	}
	
	public static String getUpdateQuery(String tableName, List<String> cols) {
		StringBuilder query=new StringBuilder("UPDATE ").append(tableName).append(" SET ")
				.append(String.join(",", cols.stream().map(val -> val.concat("=:").concat(val)).collect(Collectors.toList())));
		return query.toString();
	}
}
