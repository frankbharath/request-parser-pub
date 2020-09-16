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
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bharath.rm.constants.tables.RM_AppartmentPropertyDetails;
import com.bharath.rm.constants.tables.RM_Property;
import com.bharath.rm.constants.tables.RM_PropertyAddress;
import com.bharath.rm.constants.tables.RM_PropertyDetails;
import com.bharath.rm.constants.tables.RM_PropertyType;
import com.bharath.rm.constants.tables.RM_Users;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.AppartmentPropertyDetails;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.Property;
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
	
	public static final int BATCH_SIZE=500;
	
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
	public Long addProperty(Property property) {
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
	            pst.setString(1, property.getName());
	            pst.setLong(2, property.getUserid());
	            pst.setLong(3, property.getType().getPropertytypeid());
	            pst.setLong(4, property.getCreationtime());
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
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement(query.toString());
				pst.setLong(1, address.getPropertyid());
				pst.setString(2, address.getLine_1());
				pst.setString(3, address.getLine_2());
				pst.setLong(4, address.getPostalcode());
				pst.setString(5, address.getCity());
				return pst;
			}
			
		});
	}
	
	@Override
	public Long addPropertyDetails(PropertyDetails propertyDetails) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		StringBuilder query=new StringBuilder("INSERT INTO ").append(RM_PropertyDetails.TABLENAME).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				 PreparedStatement pst = con.prepareStatement(query.toString(), new String[] {RM_PropertyDetails.PROPERTYDETAILSID});
				 pst.setLong(1, propertyDetails.getPropertyid());
				 pst.setFloat(2, propertyDetails.getArea());
				 pst.setInt(3, propertyDetails.getCapacity());
				 pst.setFloat(4, propertyDetails.getRent());
				 return pst;
			}
		},keyHolder);
		return (long) keyHolder.getKey();
	}

	@Override
	public void addAppartmentDetailsToProperty(List<AppartmentPropertyDetails> appartmentPropertyDetails) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_AppartmentPropertyDetails.PROPERTYID);
		cols.add(RM_AppartmentPropertyDetails.AREA);
		cols.add(RM_AppartmentPropertyDetails.CAPACITY);
		cols.add(RM_AppartmentPropertyDetails.RENT);
		cols.add(RM_AppartmentPropertyDetails.DOORNO);
		cols.add(RM_AppartmentPropertyDetails.FLOORNO);
		StringBuilder query=new StringBuilder("INSERT INTO ").append(RM_AppartmentPropertyDetails.TABLENAME).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		jdbcTemplate.batchUpdate(query.toString(), appartmentPropertyDetails, BATCH_SIZE, new ParameterizedPreparedStatementSetter<AppartmentPropertyDetails>() {
            @Override
			public void setValues(PreparedStatement ps, AppartmentPropertyDetails appartmentDetails)
				throws SQLException {
                ps.setLong(1, appartmentDetails.getPropertyid());
                ps.setFloat(2, appartmentDetails.getArea());
                ps.setInt(3, appartmentDetails.getCapacity());
                ps.setFloat(4, appartmentDetails.getRent());
                ps.setString(5, appartmentDetails.getDoorno());
                ps.setInt(6, appartmentDetails.getFloorno());
            }
        });
	}
}
