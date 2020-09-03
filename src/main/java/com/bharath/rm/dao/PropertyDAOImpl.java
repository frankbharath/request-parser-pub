package com.bharath.rm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bharath.rm.constants.tables.RM_Property;
import com.bharath.rm.constants.tables.RM_PropertyAddress;
import com.bharath.rm.constants.tables.RM_PropertyDetails;
import com.bharath.rm.constants.tables.RM_PropertyType;
import com.bharath.rm.constants.tables.RM_Users;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.PropertyDetails;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 2, 2020 6:54:15 PM
 	* Class Description
*/
@Repository
public class PropertyDAOImpl implements PropertyDAO {

	JdbcTemplate jdbcTemplate;
	
	@Autowired
	public PropertyDAOImpl(JdbcTemplate template) {
		this.jdbcTemplate=template;
	}
	
	@Override
	public boolean propertyNameExists(String name) {
		String query=new StringBuilder("SELECT EXISTS (SELECT 1 FROM ").append(RM_Property.TABLENAME).append(" WHERE ")
				.append(RM_Property.PROPERTYNAME).append("=?)").toString();
		return jdbcTemplate.queryForObject(query, new Object[]{name}, Boolean.class);
		
	}
	
	@Override
	public Long getPropertyTypeId(String propertyType) {
		StringBuilder query=new StringBuilder("SELECT ").append(RM_PropertyType.PROPERTYTYPEID).append(" FROM ").append(RM_PropertyType.TABLENAME)
				.append(" WHERE ").append(RM_PropertyType.PROPERTYTYPE).append("=?");
		return DataAccessUtils.singleResult(jdbcTemplate.queryForList(query.toString(), new Object[]{propertyType}, Long.class));
	}
	
	@Override
	public Long addHouse(House house) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Property.PROPERTYNAME);
		cols.add(RM_Property.USERID);
		cols.add(RM_Property.PROPERTYTYPEID);
		cols.add(RM_Property.CREATIONTIME);
		StringBuilder query=new StringBuilder("INSERT INTO ").append(RM_Property.TABLENAME).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
	        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	            PreparedStatement pst = con.prepareStatement(query.toString(), new String[] {RM_Property.PROPERTYID});
	            pst.setObject(1, house.getName());
	            pst.setObject(2, house.getUserid());
	            pst.setObject(3, house.getType().getPropertytypeid());
	            pst.setObject(4, house.getCreationtime());
	            return pst;
	        }
	    },keyHolder);
    	return (long) keyHolder.getKey();
	}
	
	@Override
	public void addAddressToProperty(Address address) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyAddress.PROPERTYID);
		cols.add(RM_PropertyAddress.ADDRESS_1);
		cols.add(RM_PropertyAddress.ADDRESS_2);
		cols.add(RM_PropertyAddress.POSTAL);
		cols.add(RM_PropertyAddress.VILLE);
		StringBuilder query=new StringBuilder("INSERT INTO ").append(RM_PropertyAddress.TABLENAME).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		Object[] objects=new Object[5];
		objects[0]=address.getPropertyid();
		objects[1]=address.getAddress_1();
		objects[2]=address.getAddress_2();
		objects[3]=address.getPostal();
		objects[4]=address.getVille();
		jdbcTemplate.update(query.toString(), objects);	
	}
	
	@Override
	public void addPropertyDetails(PropertyDetails propertyDetails) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		StringBuilder query=new StringBuilder("INSERT INTO ").append(RM_PropertyDetails.TABLENAME).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		Object[] objects=new Object[4];
		objects[0]=propertyDetails.getPropertyid();
		objects[1]=propertyDetails.getArea();
		objects[2]=propertyDetails.getCapacity();
		objects[3]=propertyDetails.getRent();
		jdbcTemplate.update(query.toString(), objects);	
	}
	
	

}
