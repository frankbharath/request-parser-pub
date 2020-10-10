package com.bharath.rm.dao;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.tables.RM_ApartmentPropertyDetails;
import com.bharath.rm.constants.tables.RM_Property;
import com.bharath.rm.constants.tables.RM_PropertyDetails;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.model.domain.Apartment;
import com.bharath.rm.model.domain.ApartmentPropertyDetails;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.Property;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.rowmapper.ApartmentRowMapper;
import com.bharath.rm.rowmapper.AppartmentDetailsMapper;
import com.bharath.rm.rowmapper.HouseMapper;
import com.bharath.rm.rowmapper.PropertyDetailRowMapper;
import com.bharath.rm.rowmapper.PropertyRowMapper;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 2, 2020 6:54:15 PM
 	* Class Description
*/

@Repository
public class PropertyDAOImpl implements PropertyDAO {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public static final int PAGE_LIMIT=50;
	
	@Autowired
	public PropertyDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate=namedParameterJdbcTemplate;
	}
	
	@Override
	public boolean propertyNameExists(String name) {
		return propertyNameExists(name, null);
	}
	
	@Override
	public boolean propertyNameExists(String name, Long propertyId) {
		StringBuilder query=new StringBuilder("SELECT EXISTS (SELECT 1 FROM ").append(RM_Property.TABLENAME).append(" WHERE ")
				.append(RM_Property.PROPERTYNAME).append("=:").append(RM_Property.PROPERTYNAME);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Property.PROPERTYNAME, name, Types.VARCHAR);
		if(propertyId!=null) {
			query.append(" AND ").append(RM_Property.PROPERTYID).append("!=:").append(RM_Property.PROPERTYID);
			namedParameters.addValue(RM_Property.PROPERTYID, propertyId, Types.BIGINT);
		}
		query.append(")");
		
		return namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Boolean.class);
	}
	
	@Override
	public boolean propertyExists(Long userId, Long propertyId, String type) {
		return propertyExists(userId, propertyId, type, null);
	}
	
	@Override
	public boolean propertyExists(Long userId, Long propertyId, String type, Long propertyDetailId) {
		StringBuilder query=new StringBuilder("SELECT EXISTS (SELECT 1 FROM ").append(RM_Property.TABLENAME);
		if(propertyDetailId!=null) {
			query.append(" INNER JOIN ").append(RM_PropertyDetails.TABLENAME).append(" ON ")
			.append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID).append("=").append(RM_PropertyDetails.TABLENAME+"."+RM_PropertyDetails.PROPERTYID);
		}
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Property.USERID, userId, Types.BIGINT);
		namedParameters.addValue(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID, propertyId, Types.BIGINT);
		namedParameters.addValue(RM_Property.PROPERTYTYPE, type, Types.VARCHAR);
		
		query.append(" WHERE ").append(RM_Property.USERID).append("=:").append(RM_Property.USERID)
		.append(" AND ").append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID).append("=:").append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID)
		.append(" AND ").append(RM_Property.PROPERTYTYPE).append("=:").append(RM_Property.PROPERTYTYPE);
		
		if(propertyDetailId!=null) {
			namedParameters.addValue(RM_PropertyDetails.PROPERTYDETAILSID, propertyDetailId, Types.BIGINT);
			query.append(" AND ").append(RM_PropertyDetails.PROPERTYDETAILSID).append("=:").append(RM_PropertyDetails.PROPERTYDETAILSID);
		}	
		query.append(")");
		
		return namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Boolean.class);
	}
	
	@Override
	public Long addProperty(Property property) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Property.PROPERTYNAME);
		cols.add(RM_Property.USERID);
		cols.add(RM_Property.PROPERTYTYPE);
		cols.add(RM_Property.CREATIONTIME);
		cols.add(RM_Property.ADDRESSLINE_1);
		cols.add(RM_Property.ADDRESSLINE_2);
		cols.add(RM_Property.POSTAL);
		cols.add(RM_Property.CITY);
		
		String query=QueryUtils.getInsertQuery(RM_Property.TABLENAME, cols);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Property.PROPERTYNAME, property.getPropertyname(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.USERID, property.getUserid(), Types.BIGINT);
		namedParameters.addValue(RM_Property.PROPERTYTYPE, property.getPropertytype(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.CREATIONTIME, property.getCreationtime(), Types.BIGINT);
		namedParameters.addValue(RM_Property.ADDRESSLINE_1, property.getAddressline_1(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.ADDRESSLINE_2, property.getAddressline_2(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.POSTAL, property.getPostal(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.CITY, property.getCity(), Types.VARCHAR);
		
		namedParameterJdbcTemplate.update(query, namedParameters, keyHolder, new String[] {RM_Property.PROPERTYID});

    	return (Long) keyHolder.getKey();
	}
	
	@Override
	public void updateProperty(Property property) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Property.PROPERTYNAME);
		cols.add(RM_Property.ADDRESSLINE_1);
		cols.add(RM_Property.ADDRESSLINE_2);
		cols.add(RM_Property.POSTAL);
		cols.add(RM_Property.CITY);
		
		StringBuilder query=new StringBuilder(QueryUtils.getUpdateQuery(RM_Property.TABLENAME, cols));
		query.append(" WHERE ").append(RM_Property.PROPERTYID).append("=:").append(RM_Property.PROPERTYID);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Property.PROPERTYNAME, property.getPropertyname(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.PROPERTYTYPE, property.getPropertytype(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.ADDRESSLINE_1, property.getAddressline_1(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.ADDRESSLINE_2, property.getAddressline_2(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.POSTAL, property.getPostal(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.CITY, property.getCity(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.PROPERTYID, property.getPropertyid(), Types.BIGINT);
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameters);
	}
	
	@Override
	public Long addPropertyDetails(PropertyDetails propertyDetails) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		
		String query=QueryUtils.getInsertQuery(RM_PropertyDetails.TABLENAME, cols);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_PropertyDetails.PROPERTYID, propertyDetails.getPropertyid(), Types.BIGINT);
		namedParameters.addValue(RM_PropertyDetails.AREA, propertyDetails.getArea(), Types.DECIMAL);
		namedParameters.addValue(RM_PropertyDetails.CAPACITY, propertyDetails.getCapacity(), Types.INTEGER);
		namedParameters.addValue(RM_PropertyDetails.RENT, propertyDetails.getRent(), Types.FLOAT);
		
		namedParameterJdbcTemplate.update(query, namedParameters, keyHolder, new String[] {RM_PropertyDetails.PROPERTYDETAILSID});
		
		return (Long) keyHolder.getKey();
	}

	@Override
	public void updatePropertyDetails(PropertyDetails propertyDetails) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		StringBuilder query=new StringBuilder(QueryUtils.getUpdateQuery(RM_PropertyDetails.TABLENAME, cols));
		
		query.append(" WHERE ").append(RM_PropertyDetails.PROPERTYID).append("=:").append(RM_PropertyDetails.PROPERTYID);
		query.append(" AND ").append(RM_PropertyDetails.PROPERTYDETAILSID).append("=:").append(RM_PropertyDetails.PROPERTYDETAILSID);
		query.append(" AND ").append(RM_PropertyDetails.OCCUPIED).append("<=:").append(RM_PropertyDetails.CAPACITY);
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_PropertyDetails.AREA, propertyDetails.getArea(), Types.FLOAT);
		namedParameters.addValue(RM_PropertyDetails.CAPACITY, propertyDetails.getCapacity(), Types.INTEGER);
		namedParameters.addValue(RM_PropertyDetails.RENT, propertyDetails.getRent(), Types.FLOAT);
		namedParameters.addValue(RM_PropertyDetails.PROPERTYID, propertyDetails.getPropertyid(), Types.BIGINT);
		namedParameters.addValue(RM_PropertyDetails.PROPERTYDETAILSID, propertyDetails.getPropertydetailsid(), Types.BIGINT);
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameters);
	}
	
	@Override
	public void addAppartmentDetailsToProperty(List<ApartmentPropertyDetails> appartmentPropertyDetails, Long propertyId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		
		StringBuilder withQuery=new StringBuilder("WITH rm_propertydetailsins AS (");
		String insertPropertyDetailsQuery=QueryUtils.getInsertQuery(RM_PropertyDetails.TABLENAME, cols);
		withQuery.append(insertPropertyDetailsQuery).append(" RETURNING ").append(RM_PropertyDetails.PROPERTYDETAILSID).append(") ");
		
		cols=new ArrayList<>();
		cols.add(RM_ApartmentPropertyDetails.APARTMENTPROPERTYDETAILSID);
		cols.add(RM_ApartmentPropertyDetails.DOORNO);
		cols.add(RM_ApartmentPropertyDetails.FLOORNO);
		
		StringBuilder insertApartmentDetails=new StringBuilder("INSERT INTO ").append(RM_ApartmentPropertyDetails.TABLENAME).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append("(SELECT propertydetailsid from rm_propertydetailsins),");
		cols.remove(0);
		insertApartmentDetails.append(String.join(",", cols.stream().map(val -> ":".concat(val)).collect(Collectors.toList()))).append(")");
	
		withQuery.append(insertApartmentDetails);
		
		MapSqlParameterSource[] namedParameters=new MapSqlParameterSource[appartmentPropertyDetails.size()];
		for(int i=0;i<appartmentPropertyDetails.size();i++) {
			ApartmentPropertyDetails apartmentPropertyDetail=appartmentPropertyDetails.get(i);
			PropertyDetails detail=apartmentPropertyDetail.getPropertyDetails();
			MapSqlParameterSource namedParameter = new MapSqlParameterSource();
			namedParameter.addValue(RM_PropertyDetails.PROPERTYID, propertyId, Types.BIGINT);
			namedParameter.addValue(RM_PropertyDetails.AREA, detail.getArea(), Types.FLOAT);
			namedParameter.addValue(RM_PropertyDetails.CAPACITY, detail.getCapacity(), Types.INTEGER);
			namedParameter.addValue(RM_PropertyDetails.RENT, detail.getRent(), Types.FLOAT);
			namedParameter.addValue(RM_ApartmentPropertyDetails.DOORNO, apartmentPropertyDetail.getDoorno(), Types.VARCHAR);
			namedParameter.addValue(RM_ApartmentPropertyDetails.FLOORNO, apartmentPropertyDetail.getFloorno(), Types.INTEGER);
			namedParameters[i]=namedParameter;
		}
		
		namedParameterJdbcTemplate.batchUpdate(withQuery.toString(), namedParameters);
	}
	
	@Override
	public void updateAppartmentDetailsToProperty(List<ApartmentPropertyDetails> appartmentPropertyDetails, Long propertyId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		
		StringBuilder withQuery=new StringBuilder("WITH rm_propertydetailsupd AS (");
		StringBuilder updatePropertyDetailsQuery=new StringBuilder(QueryUtils.getUpdateQuery(RM_PropertyDetails.TABLENAME, cols))
				.append(" WHERE ").append(RM_PropertyDetails.PROPERTYID).append("=:").append(RM_PropertyDetails.PROPERTYID)
				.append(" AND ").append(RM_PropertyDetails.PROPERTYDETAILSID).append("=:").append(RM_PropertyDetails.PROPERTYDETAILSID)
				.append(" AND ").append(RM_PropertyDetails.OCCUPIED).append("<=:").append(RM_PropertyDetails.CAPACITY);
		withQuery.append(updatePropertyDetailsQuery).append(" RETURNING ").append(RM_PropertyDetails.PROPERTYDETAILSID).append(") ");
		
		cols=new ArrayList<>();
		cols.add(RM_ApartmentPropertyDetails.DOORNO);
		cols.add(RM_ApartmentPropertyDetails.FLOORNO);
		
		StringBuilder updateApartmentDetails=new StringBuilder("UPDATE ").append(RM_ApartmentPropertyDetails.TABLENAME).append(" SET ")
				.append(String.join(",", cols.stream().map(val -> val.concat("=:").concat(val)).collect(Collectors.toList())))
				.append(" WHERE ").append(RM_ApartmentPropertyDetails.APARTMENTPROPERTYDETAILSID).append("=")
				.append("(SELECT propertydetailsid from rm_propertydetailsupd)");
		
		withQuery.append(updateApartmentDetails);
		
		MapSqlParameterSource[] namedParameters=new MapSqlParameterSource[appartmentPropertyDetails.size()];
		for(int i=0;i<appartmentPropertyDetails.size();i++) {
			ApartmentPropertyDetails apartmentPropertyDetail=appartmentPropertyDetails.get(i);
			PropertyDetails detail=apartmentPropertyDetail.getPropertyDetails();
			MapSqlParameterSource namedParameter = new MapSqlParameterSource();
			namedParameter.addValue(RM_PropertyDetails.AREA, detail.getArea(), Types.FLOAT);
			namedParameter.addValue(RM_PropertyDetails.CAPACITY, detail.getCapacity(), Types.INTEGER);
			namedParameter.addValue(RM_PropertyDetails.RENT, detail.getRent(), Types.FLOAT);			
			namedParameter.addValue(RM_ApartmentPropertyDetails.DOORNO, apartmentPropertyDetail.getDoorno(), Types.VARCHAR);
			namedParameter.addValue(RM_ApartmentPropertyDetails.FLOORNO, apartmentPropertyDetail.getFloorno(), Types.INTEGER);
			namedParameter.addValue(RM_PropertyDetails.PROPERTYID, propertyId, Types.BIGINT);
			namedParameter.addValue(RM_PropertyDetails.PROPERTYDETAILSID, detail.getPropertydetailsid(), Types.BIGINT);
			namedParameters[i]=namedParameter;
		}
		
		namedParameterJdbcTemplate.batchUpdate(withQuery.toString(), namedParameters);
	}
	
	@Override
	public void deleteAppartmentDetailsToProperty(Long propertyId, List<Long> deleteIds) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_PropertyDetails.PROPERTYID, propertyId, Types.BIGINT);
		namedParameter.addValue(RM_PropertyDetails.PROPERTYDETAILSID, deleteIds);
		
		StringBuilder query=new StringBuilder("DELETE ").append(" FROM ").append(RM_PropertyDetails.TABLENAME)
		.append(" WHERE ").append(RM_PropertyDetails.PROPERTYID).append("=:").append(RM_PropertyDetails.PROPERTYID)
		.append(" AND ").append(RM_PropertyDetails.PROPERTYDETAILSID).append(" IN (:").append(RM_PropertyDetails.PROPERTYDETAILSID)
		.append(")");
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameter);
	}
	
	@Override
	public List<Property> getAllProperties(Long userId, String searchQuery, Integer pageNo){
		return getAllProperties(userId, searchQuery, pageNo, false);
	}
	
	@Override
	public List<Property> getAllProperties(Long userId, String searchQuery, Integer pageNo, boolean allProperties) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Property.PROPERTYID);
		cols.add(RM_Property.PROPERTYNAME);
		cols.add(RM_Property.USERID);
		cols.add(RM_Property.PROPERTYTYPE);
		cols.add(RM_Property.CREATIONTIME);
		cols.add(RM_Property.ADDRESSLINE_1);
		cols.add(RM_Property.ADDRESSLINE_2);
		cols.add(RM_Property.POSTAL);
		cols.add(RM_Property.CITY);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Property.TABLENAME);
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_Property.USERID, userId, Types.BIGINT);
		query.append(" WHERE ").append(RM_Property.USERID).append("=:").append(RM_Property.USERID);
		if(searchQuery!=null) {
			query.append(" AND ").append(RM_Property.PROPERTYSEARCH_TSV).append(" @@ to_tsquery( :").append(RM_Property.PROPERTYSEARCH_TSV).append(")");
			namedParameter.addValue(RM_Property.PROPERTYSEARCH_TSV, searchQuery, Types.VARCHAR);
		}
		query.append(" ORDER BY ").append(RM_Property.PROPERTYNAME);
		if(!allProperties) {
			query.append(" LIMIT ").append(PAGE_LIMIT);
			if(pageNo!=null) {
				query.append(" OFFSET ").append(PAGE_LIMIT*(pageNo-1));
			}
		}
		
		return namedParameterJdbcTemplate.query(query.toString(),namedParameter,new PropertyRowMapper());
	}
	
	@Override
	public HashMap<Long, Property> getAllPropertiesForPropertyDetails(Long userId, List<Long> propertyDetailsId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID);
		cols.add(RM_Property.PROPERTYNAME);
		cols.add(RM_Property.USERID);
		cols.add(RM_Property.PROPERTYTYPE);
		cols.add(RM_Property.CREATIONTIME);
		cols.add(RM_Property.ADDRESSLINE_1);
		cols.add(RM_Property.ADDRESSLINE_2);
		cols.add(RM_Property.POSTAL);
		cols.add(RM_Property.CITY);
		cols.add(RM_PropertyDetails.PROPERTYDETAILSID);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Property.TABLENAME)
				.append(" INNER JOIN ").append(RM_PropertyDetails.TABLENAME).append(" ON ")
				.append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID).append("=").append(RM_PropertyDetails.TABLENAME+"."+RM_PropertyDetails.PROPERTYID)
				.append(" WHERE ").append(RM_Property.USERID).append("=:").append(RM_Property.USERID)
				.append(" AND ").append(RM_PropertyDetails.PROPERTYDETAILSID).append(" IN (:").append(RM_PropertyDetails.PROPERTYDETAILSID).append(")");
		
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_Property.USERID, userId, Types.BIGINT);
		namedParameter.addValue(RM_PropertyDetails.PROPERTYDETAILSID, propertyDetailsId);
		
		return namedParameterJdbcTemplate.query(query.toString(), namedParameter, (ResultSet rs) -> {
			HashMap<Long, Property> map=new HashMap<>();
			while (rs.next()) {
				map.put(rs.getLong(RM_PropertyDetails.PROPERTYDETAILSID), QueryUtils.getProperty(rs));
			}
			return map;
		});
	}
	
	@Override
	public Integer getPropertiesCount(Long userId, String searchQuery){
		StringBuilder query=new StringBuilder("SELECT COUNT(*) FROM ").append(RM_Property.TABLENAME);
		
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_Property.USERID, userId, Types.BIGINT);
		query.append(" WHERE ").append(RM_Property.USERID).append("= :").append(RM_Property.USERID);
		if(searchQuery!=null) {
			query.append(" AND ").append(RM_Property.PROPERTYSEARCH_TSV).append(" @@ to_tsquery( :").append(RM_Property.PROPERTYSEARCH_TSV).append(")");
			namedParameter.addValue(RM_Property.PROPERTYSEARCH_TSV, searchQuery, Types.VARCHAR);
		}
		
		return namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameter, Integer.class);
	}
	
	@Override
	public Property getProperty(Long userId, Long propertyId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Property.PROPERTYID);
		cols.add(RM_Property.PROPERTYNAME);
		cols.add(RM_Property.USERID);
		cols.add(RM_Property.PROPERTYTYPE);
		cols.add(RM_Property.CREATIONTIME);
		cols.add(RM_Property.ADDRESSLINE_1);
		cols.add(RM_Property.ADDRESSLINE_2);
		cols.add(RM_Property.POSTAL);
		cols.add(RM_Property.CITY);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Property.TABLENAME)
				.append(" WHERE ").append(RM_Property.USERID).append("=:").append(RM_Property.USERID)
				.append(" AND ").append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID).append("=:").append(RM_Property.PROPERTYID);
		
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_Property.USERID, userId, Types.BIGINT);
		namedParameter.addValue(RM_Property.PROPERTYID, propertyId, Types.BIGINT);
		return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(query.toString(), namedParameter, new PropertyRowMapper()));
	}
	
	@Override
	public House getHouse(Long userId, Long propertyId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID);
		cols.add(RM_Property.PROPERTYNAME);
		cols.add(RM_Property.USERID);
		cols.add(RM_Property.PROPERTYTYPE);
		cols.add(RM_Property.CREATIONTIME);
		cols.add(RM_Property.ADDRESSLINE_1);
		cols.add(RM_Property.ADDRESSLINE_2);
		cols.add(RM_Property.POSTAL);
		cols.add(RM_Property.CITY);
		cols.add(RM_PropertyDetails.PROPERTYDETAILSID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		cols.add(RM_PropertyDetails.OCCUPIED);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Property.TABLENAME)
				.append(" INNER JOIN ").append(RM_PropertyDetails.TABLENAME).append(" ON ").append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID)
				.append("=").append(RM_PropertyDetails.TABLENAME+"."+RM_PropertyDetails.PROPERTYID)
				.append(" WHERE ").append(RM_Property.USERID).append("=:").append(RM_Property.USERID)
				.append(" AND ").append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID).append("=:").append(RM_Property.PROPERTYID)
				.append(" AND ").append(RM_Property.PROPERTYTYPE).append("=:").append(RM_Property.PROPERTYTYPE);
		
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_Property.USERID, userId, Types.BIGINT);
		namedParameter.addValue(RM_Property.PROPERTYID, propertyId, Types.BIGINT);
		namedParameter.addValue(RM_Property.PROPERTYTYPE, Constants.PropertyType.HOUSE.toString(), Types.VARCHAR);
		
		return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(query.toString(), namedParameter, new HouseMapper()));
	}
	
	@Override
	public HashMap<Long, PropertyDetails> getHouseDetails(List<Long> propertyIds) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.PROPERTYDETAILSID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		cols.add(RM_PropertyDetails.OCCUPIED);
		
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_PropertyDetails.PROPERTYID, propertyIds, Types.BIGINT);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_PropertyDetails.TABLENAME)
				.append(" WHERE ").append(RM_PropertyDetails.PROPERTYID).append(" IN (:").append(RM_PropertyDetails.PROPERTYID).append(")");
		
		return namedParameterJdbcTemplate.query(query.toString(), namedParameter, (ResultSet rs) -> {
			HashMap<Long, PropertyDetails> map=new HashMap<>();
			while (rs.next()) {
				map.put(rs.getLong(RM_PropertyDetails.PROPERTYID), QueryUtils.getPropertyDetails(rs));
			}
			return map;
		});
	}
	
	@Override
	public Apartment getAppartment(Long userId, Long propertyId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID);
		cols.add(RM_Property.PROPERTYNAME);
		cols.add(RM_Property.USERID);
		cols.add(RM_Property.PROPERTYTYPE);
		cols.add(RM_Property.CREATIONTIME);
		cols.add(RM_Property.ADDRESSLINE_1);
		cols.add(RM_Property.ADDRESSLINE_2);
		cols.add(RM_Property.POSTAL);
		cols.add(RM_Property.CITY);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Property.TABLENAME);
		query.append(" WHERE ").append(RM_Property.USERID).append("= :").append(RM_Property.USERID);
		query.append(" AND ").append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID).append("= :").append(RM_Property.PROPERTYID);
		query.append(" AND ").append(RM_Property.PROPERTYTYPE).append("= :").append(RM_Property.PROPERTYTYPE);
		
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_Property.USERID, userId, Types.BIGINT);
		namedParameter.addValue(RM_Property.PROPERTYID, propertyId, Types.BIGINT);
		namedParameter.addValue(RM_Property.PROPERTYTYPE, Constants.PropertyType.APPARTMENT.toString(), Types.VARCHAR);
		
		return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(query.toString(), namedParameter, new ApartmentRowMapper()));
	}
	
	@Override
	public List<ApartmentPropertyDetails> getApartmentUnits(Long propertyId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.PROPERTYDETAILSID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		cols.add(RM_PropertyDetails.OCCUPIED);
		
		cols.add(RM_ApartmentPropertyDetails.DOORNO);
		cols.add(RM_ApartmentPropertyDetails.FLOORNO);
		
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_PropertyDetails.TABLENAME)
			.append(" INNER JOIN ").append(RM_ApartmentPropertyDetails.TABLENAME).append(" ON ")
			.append(RM_PropertyDetails.PROPERTYDETAILSID).append("=").append(RM_ApartmentPropertyDetails.APARTMENTPROPERTYDETAILSID)
			.append(" WHERE ").append(RM_PropertyDetails.PROPERTYID).append("=:").append(RM_PropertyDetails.PROPERTYID);
		
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_PropertyDetails.PROPERTYID, propertyId, Types.BIGINT);
		
		return namedParameterJdbcTemplate.query(query.toString(),namedParameter,new AppartmentDetailsMapper());
	}
	
	@Override
	public ApartmentPropertyDetails getApartmentUnit(Long propertyId, Long propertyDetailId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.PROPERTYDETAILSID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		cols.add(RM_PropertyDetails.OCCUPIED);
		
		cols.add(RM_ApartmentPropertyDetails.DOORNO);
		cols.add(RM_ApartmentPropertyDetails.FLOORNO);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_PropertyDetails.TABLENAME)
				.append(" INNER JOIN ").append(RM_ApartmentPropertyDetails.TABLENAME).append(" ON ")
				.append(RM_PropertyDetails.PROPERTYDETAILSID).append("=").append(RM_ApartmentPropertyDetails.APARTMENTPROPERTYDETAILSID)
				.append(" WHERE ").append(RM_PropertyDetails.PROPERTYID).append("=:").append(RM_PropertyDetails.PROPERTYID)
				.append(" AND ").append(RM_PropertyDetails.PROPERTYDETAILSID).append("=:").append(RM_PropertyDetails.PROPERTYDETAILSID);
			
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_PropertyDetails.PROPERTYID, propertyId, Types.BIGINT);
		namedParameter.addValue(RM_PropertyDetails.PROPERTYDETAILSID, propertyDetailId, Types.BIGINT);
		
		return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(query.toString(),namedParameter,new AppartmentDetailsMapper()));
	}
	
	@Override
	public HashMap<Long, List<ApartmentPropertyDetails>> getApartmentUnits(List<Long> propertyIds) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.PROPERTYDETAILSID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		cols.add(RM_PropertyDetails.OCCUPIED);
		
		cols.add(RM_ApartmentPropertyDetails.DOORNO);
		cols.add(RM_ApartmentPropertyDetails.FLOORNO);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_PropertyDetails.TABLENAME)
				.append(" INNER JOIN ").append(RM_ApartmentPropertyDetails.TABLENAME).append(" ON ")
				.append(RM_PropertyDetails.PROPERTYDETAILSID).append("=").append(RM_ApartmentPropertyDetails.APARTMENTPROPERTYDETAILSID)
				.append(" WHERE ").append(RM_PropertyDetails.PROPERTYID).append(" IN (:").append(RM_PropertyDetails.PROPERTYID).append(")");
		
		Map<String, Object> params = new HashMap<>();
		params.put(RM_PropertyDetails.PROPERTYID, propertyIds);
		
		return namedParameterJdbcTemplate.query(query.toString(), params, (ResultSet rs) -> {
			HashMap<Long, List<ApartmentPropertyDetails>> map=new HashMap<>();
			while(rs.next()) {
				map.computeIfAbsent(rs.getLong(RM_PropertyDetails.PROPERTYID), k -> new ArrayList<>()).add(QueryUtils.getApartmentPropertyDetail(rs));
			}
			return map;
		});
	}
	
	@Override
	public void deleteProperty(Long propertyId, Long userId) {
		List<Long> propertyIdList=new ArrayList<Long>();
		propertyIdList.add(propertyId);
		deleteProperty(propertyIdList, userId);
	}
	
	@Override
	public void deleteProperty(List<Long> propertyIds, Long userId) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_Property.PROPERTYID, propertyIds);
		namedParameter.addValue(RM_Property.USERID, userId, Types.BIGINT);
		
		StringBuilder query=new StringBuilder("DELETE ").append(" FROM ").append(RM_Property.TABLENAME)
				.append(" WHERE ").append(RM_Property.PROPERTYID).append(" IN (:").append(RM_Property.PROPERTYID).append(")");
		query.append(" AND ").append(RM_Property.USERID).append("= :").append(RM_Property.USERID);
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameter);
	}

	@Override
	public PropertyDetails getPropertyDetails(Long propertyDetailsId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.PROPERTYDETAILSID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		cols.add(RM_PropertyDetails.OCCUPIED);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_PropertyDetails.PROPERTYDETAILSID, propertyDetailsId, Types.BIGINT);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_PropertyDetails.TABLENAME)
				.append(" WHERE ").append(RM_PropertyDetails.PROPERTYDETAILSID).append(" =:").append(RM_PropertyDetails.PROPERTYDETAILSID);
		return namedParameterJdbcTemplate.queryForObject(query.toString(),namedParameters,new PropertyDetailRowMapper());
	}
	
	@Override
	public void updatePropertyOccupancy(Long propertyDetailsId, int occupants) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.OCCUPIED);
		
		StringBuilder query=new StringBuilder(QueryUtils.getUpdateQuery(RM_PropertyDetails.TABLENAME, cols))
				.append(" WHERE ").append(RM_PropertyDetails.PROPERTYDETAILSID)
				.append("=:").append(RM_PropertyDetails.PROPERTYDETAILSID);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_PropertyDetails.OCCUPIED, occupants, Types.BIGINT);
		namedParameters.addValue(RM_PropertyDetails.PROPERTYDETAILSID, propertyDetailsId, Types.BIGINT);
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameters);
	}
}
