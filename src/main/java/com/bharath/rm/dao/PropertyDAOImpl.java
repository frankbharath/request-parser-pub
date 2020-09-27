package com.bharath.rm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.tables.RM_AppartmentPropertyDetails;
import com.bharath.rm.constants.tables.RM_Property;
import com.bharath.rm.constants.tables.RM_PropertyDetails;
import com.bharath.rm.constants.tables.RM_PropertyType;
import com.bharath.rm.constants.tables.RM_Tenant;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.ApartmentPropertyDetailDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.PropertyDTO;
import com.bharath.rm.dto.PropertyDetailsDTO;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.AppartmentPropertyDetails;
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

	private JdbcTemplate jdbcTemplate;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public static final int BATCH_SIZE=500;
	
	public static final int PAGE_LIMIT=50;
	
	@Autowired
	public PropertyDAOImpl(JdbcTemplate template, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate=template;
		this.namedParameterJdbcTemplate=namedParameterJdbcTemplate;
	}
	
	@Override
	public boolean propertyNameExists(String name) {
		return propertyNameExists(name, null);
	}
	
	@Override
	public boolean propertyNameExists(String name, Long propertyId) {
		StringBuilder query=new StringBuilder("SELECT EXISTS (SELECT 1 FROM ").append(RM_Property.TABLENAME).append(" WHERE ")
				.append(RM_Property.PROPERTYNAME).append("= :").append(RM_Property.PROPERTYNAME);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Property.PROPERTYNAME, name, Types.VARCHAR);
		if(propertyId!=null) {
			query.append(" AND ").append(RM_Property.PROPERTYID).append("!= :").append(RM_Property.PROPERTYID);
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
		namedParameters.addValue(RM_Property.PROPERTYID, propertyId, Types.BIGINT);
		namedParameters.addValue(RM_Property.PROPERTYTYPE, type, Types.VARCHAR);
		
		query.append(" WHERE ").append(RM_Property.USERID).append("=:").append(RM_Property.USERID)
		.append(" AND ").append(RM_Property.PROPERTYID).append("=:").append(RM_Property.PROPERTYID)
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
		Address address=property.getAddress();
		
		String query=QueryUtils.getInsertQuery(RM_Property.TABLENAME, cols);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Property.PROPERTYNAME, property.getPropertyname(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.USERID, property.getUserid(), Types.BIGINT);
		namedParameters.addValue(RM_Property.PROPERTYTYPE, property.getPropertytype(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.CREATIONTIME, property.getCreationtime(), Types.BIGINT);
		namedParameters.addValue(RM_Property.ADDRESSLINE_1, address.getAddressline_1(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.ADDRESSLINE_2, address.getAddressline_2(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.POSTAL, address.getPostal(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.CITY, address.getCity(), Types.VARCHAR);
		
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
		Address address=property.getAddress();
		
		String query=QueryUtils.getUpdateQuery(RM_Property.TABLENAME, cols);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Property.PROPERTYNAME, property.getPropertyname(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.PROPERTYTYPE, property.getPropertytype(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.ADDRESSLINE_1, address.getAddressline_1(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.ADDRESSLINE_2, address.getAddressline_2(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.POSTAL, address.getPostal(), Types.VARCHAR);
		namedParameters.addValue(RM_Property.CITY, address.getCity(), Types.VARCHAR);
		
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
		StringBuilder query=new StringBuilder("UPDATE ").append(RM_PropertyDetails.TABLENAME).append(" SET ")
				.append(String.join(",", cols.stream().map(val -> val.concat("=?")).collect(Collectors.toList())));
		query.append(" WHERE ").append(RM_PropertyDetails.PROPERTYID).append("=?");
		query.append(" AND ").append(RM_PropertyDetails.PROPERTYDETAILSID).append("=?");
		Object[] params=new Object[] {propertyDetails.getArea(), propertyDetails.getCapacity(), propertyDetails.getRent(), propertyDetails.getPropertyid(), propertyDetails.getPropertydetailsid()};
		jdbcTemplate.update(query.toString(), params);
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
	
	@Override
	public void updateAppartmentDetailsToProperty(List<AppartmentPropertyDetails> appartmentPropertyDetails) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_AppartmentPropertyDetails.AREA);
		cols.add(RM_AppartmentPropertyDetails.CAPACITY);
		cols.add(RM_AppartmentPropertyDetails.RENT);
		cols.add(RM_AppartmentPropertyDetails.DOORNO);
		cols.add(RM_AppartmentPropertyDetails.FLOORNO);
		cols.stream().map(val -> val.concat("=?"));
		StringBuilder query=new StringBuilder("UPDATE ").append(RM_AppartmentPropertyDetails.TABLENAME).append(" SET ")
				.append(String.join(",", cols.stream().map(val -> val.concat("=?")).collect(Collectors.toList())));
		query.append(" WHERE ").append(RM_Property.PROPERTYID).append("=?")
		.append(" AND ").append(RM_AppartmentPropertyDetails.PROPERTYDETAILSID).append("=?");
		jdbcTemplate.batchUpdate(query.toString(), appartmentPropertyDetails, BATCH_SIZE, new ParameterizedPreparedStatementSetter<AppartmentPropertyDetails>() {
			   @Override
				public void setValues(PreparedStatement ps, AppartmentPropertyDetails appartmentDetails) throws SQLException {
	                ps.setFloat(1, appartmentDetails.getArea());
	                ps.setInt(2, appartmentDetails.getCapacity());
	                ps.setFloat(3, appartmentDetails.getRent());
	                ps.setString(4, appartmentDetails.getDoorno());
	                ps.setInt(5, appartmentDetails.getFloorno());
	                ps.setLong(6, appartmentDetails.getPropertyid());
	                ps.setLong(7, appartmentDetails.getPropertydetailsid());
			   }
		});
	}
	
	@Override
	public void deleteAppartmentDetailsToProperty(Long propertyId, List<Long> deleteIds) {
		Map<String, Object> params = new HashMap<>();
		params.put(RM_AppartmentPropertyDetails.PROPERTYID, propertyId);
		params.put(RM_AppartmentPropertyDetails.PROPERTYDETAILSID, deleteIds);
		StringBuilder query=new StringBuilder("DELETE ").append(" FROM ").append(RM_AppartmentPropertyDetails.TABLENAME)
		.append(" WHERE ").append(RM_AppartmentPropertyDetails.PROPERTYID).append("=").append(RM_AppartmentPropertyDetails.PROPERTYID);
		query.append(" AND ").append(RM_AppartmentPropertyDetails.PROPERTYDETAILSID).append(" IN (:").append(RM_AppartmentPropertyDetails.PROPERTYDETAILSID).append(")");
		namedParameterJdbcTemplate.update(query.toString(), params);
	}
	
	@Override
	public List<PropertyDTO> getAllProperties(Long userId, String searchQuery, Integer pageNo){
		return getAllProperties(userId, searchQuery, pageNo, false);
	}
	
	@Override
	public List<PropertyDTO> getAllProperties(Long userId, String searchQuery, Integer pageNo, boolean allProperties) {
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
		Map<String, Object> params = new HashMap<>();
		params.put(RM_Property.USERID, userId);
		query.append(" WHERE ").append(RM_Property.USERID).append("= :").append(RM_Property.USERID);
		if(searchQuery!=null) {
			query.append(" AND ").append(RM_Property.PROPERTYSEARCH_TSV).append(" @@ to_tsquery( :").append(RM_Property.PROPERTYSEARCH_TSV).append(")");
			params.put(RM_Property.PROPERTYSEARCH_TSV, searchQuery);
		}
		query.append(" ORDER BY ").append(RM_Property.PROPERTYNAME);
		if(!allProperties) {
			query.append(" LIMIT ").append(PAGE_LIMIT);
			if(pageNo!=null) {
				query.append(" OFFSET ").append(PAGE_LIMIT*(pageNo-1));
			}
		}
		
		return namedParameterJdbcTemplate.query(query.toString(),params,new PropertyRowMapper());
	}
	
	@Override
	public Integer getPropertiesCount(Long userId, String searchQuery){
		StringBuilder query=new StringBuilder("SELECT COUNT(*) FROM ").append(RM_Property.TABLENAME);
		Map<String, Object> params = new HashMap<>();
		params.put(RM_Property.USERID, userId);
		query.append(" WHERE ").append(RM_Property.USERID).append("= :").append(RM_Property.USERID);
		if(searchQuery!=null) {
			query.append(" AND ").append(RM_Property.PROPERTYSEARCH_TSV).append(" @@ to_tsquery( :").append(RM_Property.PROPERTYSEARCH_TSV).append(")");
			params.put(RM_Property.PROPERTYSEARCH_TSV, searchQuery);
		}
		return namedParameterJdbcTemplate.queryForObject(query.toString(), params, Integer.class);
	}
	
	@Override
	public HouseDTO getHouse(Long userId, Long propertyId) {
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
				.append("=").append(RM_PropertyDetails.TABLENAME+"."+RM_PropertyDetails.PROPERTYID);
		Map<String, Object> params = new HashMap<>();
		params.put(RM_Property.USERID, userId);
		params.put(RM_Property.PROPERTYID, propertyId);
		params.put(RM_Property.PROPERTYTYPE, Constants.PropertyType.HOUSE.toString());
		query.append(" WHERE ").append(RM_Property.USERID).append("= :").append(RM_Property.USERID);
		query.append(" AND ").append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID).append("= :").append(RM_Property.PROPERTYID);
		query.append(" AND ").append(RM_Property.PROPERTYTYPE).append("= :").append(RM_Property.PROPERTYTYPE);
		return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(query.toString(), params, new HouseMapper()));
	}
	
	@Override
	public HashMap<Long, PropertyDetailsDTO> getHouseDetails(List<Long> propertyIds) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_PropertyDetails.PROPERTYID);
		cols.add(RM_PropertyDetails.PROPERTYDETAILSID);
		cols.add(RM_PropertyDetails.AREA);
		cols.add(RM_PropertyDetails.CAPACITY);
		cols.add(RM_PropertyDetails.RENT);
		cols.add(RM_PropertyDetails.OCCUPIED);
		Map<String, Object> params = new HashMap<>();
		params.put(RM_PropertyDetails.PROPERTYID, propertyIds);
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_PropertyDetails.TABLENAME)
				.append(" WHERE ").append(RM_PropertyDetails.PROPERTYID).append(" IN (:").append(RM_PropertyDetails.PROPERTYID).append(")");
		return namedParameterJdbcTemplate.query(query.toString(), params, (ResultSet rs) -> {
			HashMap<Long, PropertyDetailsDTO> map=new HashMap<>();
			while (rs.next()) {
				map.put(rs.getLong(RM_PropertyDetails.PROPERTYID), QueryUtils.gePropertyDetailsDTO(rs));
			}
			return map;
		});
	}
	
	@Override
	public ApartmentDTO getAppartment(Long userId, Long propertyId) {
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
		Map<String, Object> params = new HashMap<>();
		params.put(RM_Property.USERID, userId);
		params.put(RM_Property.PROPERTYID, propertyId);
		params.put(RM_Property.PROPERTYTYPE, Constants.PropertyType.APPARTMENT.toString());
		query.append(" WHERE ").append(RM_Property.USERID).append("= :").append(RM_Property.USERID);
		query.append(" AND ").append(RM_Property.TABLENAME+"."+RM_Property.PROPERTYID).append("= :").append(RM_Property.PROPERTYID);
		query.append(" AND ").append(RM_Property.PROPERTYTYPE).append("= :").append(RM_Property.PROPERTYTYPE);
		return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(query.toString(), params, new ApartmentRowMapper()));
	}
	
	@Override
	public List<ApartmentPropertyDetailDTO> getApartmentUnits(Long propertyId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_AppartmentPropertyDetails.PROPERTYID);
		cols.add(RM_AppartmentPropertyDetails.PROPERTYDETAILSID);
		cols.add(RM_AppartmentPropertyDetails.AREA);
		cols.add(RM_AppartmentPropertyDetails.CAPACITY);
		cols.add(RM_AppartmentPropertyDetails.RENT);
		cols.add(RM_AppartmentPropertyDetails.DOORNO);
		cols.add(RM_AppartmentPropertyDetails.FLOORNO);
		cols.add(RM_AppartmentPropertyDetails.OCCUPIED);
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_AppartmentPropertyDetails.TABLENAME);
		Map<String, Object> params = new HashMap<>();
		params.put(RM_AppartmentPropertyDetails.PROPERTYID, propertyId);
		query.append(" WHERE ").append(RM_AppartmentPropertyDetails.PROPERTYID).append("=:").append(RM_AppartmentPropertyDetails.PROPERTYID);
		return namedParameterJdbcTemplate.query(query.toString(),params,new AppartmentDetailsMapper());
	}
	
	@Override
	public HashMap<Long, List<ApartmentPropertyDetailDTO>> getApartmentUnits(List<Long> propertyIds) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_AppartmentPropertyDetails.PROPERTYID);
		cols.add(RM_AppartmentPropertyDetails.PROPERTYDETAILSID);
		cols.add(RM_AppartmentPropertyDetails.AREA);
		cols.add(RM_AppartmentPropertyDetails.CAPACITY);
		cols.add(RM_AppartmentPropertyDetails.RENT);
		cols.add(RM_AppartmentPropertyDetails.DOORNO);
		cols.add(RM_AppartmentPropertyDetails.FLOORNO);
		cols.add(RM_AppartmentPropertyDetails.OCCUPIED);
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_AppartmentPropertyDetails.TABLENAME);
		Map<String, Object> params = new HashMap<>();
		params.put(RM_AppartmentPropertyDetails.PROPERTYID, propertyIds);
		query.append(" WHERE ").append(RM_AppartmentPropertyDetails.PROPERTYID).append(" IN (:").append(RM_AppartmentPropertyDetails.PROPERTYID).append(")");
		return namedParameterJdbcTemplate.query(query.toString(), params, (ResultSet rs) -> {
			HashMap<Long, List<ApartmentPropertyDetailDTO>> map=new HashMap<>();
			while(rs.next()) {
				map.computeIfAbsent(rs.getLong(RM_AppartmentPropertyDetails.PROPERTYID), k -> new ArrayList<>()).add(QueryUtils.geApartmentPropertyDetailDTO(rs));
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
		Map<String, Object> params = new HashMap<>();
		params.put(RM_Property.PROPERTYID, propertyIds);
		params.put(RM_Property.USERID, userId);
		StringBuilder query=new StringBuilder("DELETE ").append(" FROM ").append(RM_Property.TABLENAME)
				.append(" WHERE ").append(RM_Property.PROPERTYID).append(" IN (:").append(RM_Property.PROPERTYID).append(")");
		query.append(" AND ").append(RM_Property.USERID).append("= :").append(RM_Property.USERID);
		namedParameterJdbcTemplate.update(query.toString(), params);
	}

	@Override
	public PropertyDetailsDTO getPropertyDetails(Long propertyDetailsId) {
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
		String updateQuery=QueryUtils.getUpdateQuery(RM_PropertyDetails.TABLENAME, cols);
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_PropertyDetails.OCCUPIED, occupants, Types.BIGINT);
		namedParameters.addValue(RM_PropertyDetails.PROPERTYDETAILSID, propertyDetailsId, Types.BIGINT);
		StringBuilder query=new StringBuilder(updateQuery).append(" WHERE ").append(RM_PropertyDetails.PROPERTYDETAILSID)
				.append("=:").append(RM_PropertyDetails.PROPERTYDETAILSID);
		namedParameterJdbcTemplate.update(query.toString(), namedParameters);
	}
}
