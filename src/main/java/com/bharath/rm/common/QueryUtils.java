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

import com.bharath.rm.constants.tables.RM_ApartmentPropertyDetails;
import com.bharath.rm.constants.tables.RM_Lease;
import com.bharath.rm.constants.tables.RM_Property;
import com.bharath.rm.constants.tables.RM_PropertyDetails;
import com.bharath.rm.model.domain.Apartment;
import com.bharath.rm.model.domain.ApartmentPropertyDetails;
import com.bharath.rm.model.domain.ContractStatus;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.Property;
import com.bharath.rm.model.domain.PropertyDetails;

public class QueryUtils {
	
	public static PropertyDetails getPropertyDetails(ResultSet rs) throws SQLException {
		PropertyDetails details=new PropertyDetails();
		details.setPropertyid(rs.getLong(RM_PropertyDetails.PROPERTYID));
		details.setPropertydetailsid(rs.getLong(RM_PropertyDetails.PROPERTYDETAILSID));
		details.setArea(rs.getFloat(RM_PropertyDetails.AREA));
		details.setCapacity(rs.getInt(RM_PropertyDetails.CAPACITY));
		details.setRent(rs.getFloat(RM_PropertyDetails.RENT));
		details.setOccupied(rs.getInt(RM_PropertyDetails.OCCUPIED));
		return details;
	}
	
	public static Property getProperty(ResultSet rs) throws SQLException {
		Property property=new Property();
		property.setPropertyid(rs.getLong(RM_Property.PROPERTYID));
		property.setPropertyname(rs.getString(RM_Property.PROPERTYNAME));
		property.setUserid(rs.getLong(RM_Property.USERID));
		property.setPropertytype(rs.getString(RM_Property.PROPERTYTYPE));
		property.setCreationtime(rs.getLong(RM_Property.CREATIONTIME));
		property.setAddressline_1(rs.getString(RM_Property.ADDRESSLINE_1));
		property.setAddressline_2(rs.getString(RM_Property.ADDRESSLINE_2));
		property.setCity(rs.getString(RM_Property.CITY));
		property.setPostal(rs.getString(RM_Property.POSTAL));
        return property;
	}
	
	public static House getHouse(ResultSet rs) throws SQLException {
		House house=new House();
		house.setPropertyid(rs.getLong(RM_Property.PROPERTYID));
		house.setPropertyname(rs.getString(RM_Property.PROPERTYNAME));
		house.setUserid(rs.getLong(RM_Property.USERID));
		house.setPropertytype(rs.getString(RM_Property.PROPERTYTYPE));
		house.setCreationtime(rs.getLong(RM_Property.CREATIONTIME));
		house.setAddressline_1(rs.getString(RM_Property.ADDRESSLINE_1));
		house.setAddressline_2(rs.getString(RM_Property.ADDRESSLINE_2));
		house.setCity(rs.getString(RM_Property.CITY));
		house.setPostal(rs.getString(RM_Property.POSTAL));
		house.setPropertydetails(getPropertyDetails(rs));
		return house;
	}
	
	public static Apartment getApartment(ResultSet rs) throws SQLException {
		Apartment apartment=new Apartment();
		apartment.setPropertyid(rs.getLong(RM_Property.PROPERTYID));
		apartment.setPropertyname(rs.getString(RM_Property.PROPERTYNAME));
		apartment.setUserid(rs.getLong(RM_Property.USERID));
		apartment.setPropertytype(rs.getString(RM_Property.PROPERTYTYPE));
		apartment.setCreationtime(rs.getLong(RM_Property.CREATIONTIME));
		apartment.setAddressline_1(rs.getString(RM_Property.ADDRESSLINE_1));
		apartment.setAddressline_2(rs.getString(RM_Property.ADDRESSLINE_2));
		apartment.setCity(rs.getString(RM_Property.CITY));
		apartment.setPostal(rs.getString(RM_Property.POSTAL));
		return apartment;
	}
	
	public static ApartmentPropertyDetails getApartmentPropertyDetail(ResultSet rs) throws SQLException {
		ApartmentPropertyDetails details=new ApartmentPropertyDetails();
		PropertyDetails propertyDetails=new PropertyDetails();
		propertyDetails.setPropertyid(rs.getLong(RM_PropertyDetails.PROPERTYID));
		propertyDetails.setPropertydetailsid(rs.getLong(RM_PropertyDetails.PROPERTYDETAILSID));
		propertyDetails.setArea(rs.getFloat(RM_PropertyDetails.AREA));
		propertyDetails.setCapacity(rs.getInt(RM_PropertyDetails.CAPACITY));
		propertyDetails.setRent(rs.getFloat(RM_PropertyDetails.RENT));
		propertyDetails.setOccupied(rs.getInt(RM_PropertyDetails.OCCUPIED));
		details.setPropertyDetails(propertyDetails);
		details.setApartmentpropertydetailsid(rs.getLong(RM_PropertyDetails.PROPERTYDETAILSID));
		details.setDoorno(rs.getString(RM_ApartmentPropertyDetails.DOORNO));
		details.setFloorno(rs.getInt(RM_ApartmentPropertyDetails.FLOORNO));
		return details;
	}
	
	public static Lease getLease(ResultSet rs) throws SQLException {
		Lease lease=new Lease();
		lease.setContractid(rs.getString(RM_Lease.CONTRACTID));
		lease.setLeaseid(rs.getLong(RM_Lease.LEASEID));
		lease.setLeasetenantid(rs.getLong(RM_Lease.LEASETENANTID));
		lease.setMovein(rs.getLong(RM_Lease.MOVEIN));
		lease.setMoveout(rs.getLong(RM_Lease.MOVEOUT));
		lease.setOccupants(rs.getInt(RM_Lease.OCCUPANTS));
		lease.setRent(rs.getFloat(RM_Lease.RENT));
		lease.setTenantspropertydetailid(rs.getLong(RM_Lease.TENANTSPROPERTYDETAILID));
		ContractStatus contractStatus=new ContractStatus();
		contractStatus.setContractstatusid(rs.getLong(RM_Lease.STATUSID));
		lease.setContractstatus(contractStatus);
		return lease;
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
