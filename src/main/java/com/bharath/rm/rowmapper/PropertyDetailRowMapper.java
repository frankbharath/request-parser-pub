package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.model.domain.PropertyDetails;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 23, 2020 6:40:29 PM
 	* Class Description
*/
public class PropertyDetailRowMapper implements RowMapper<PropertyDetails>{

	@Override
	public PropertyDetails mapRow(ResultSet rs, int rowNum) throws SQLException {		
		return QueryUtils.getPropertyDetails(rs);
	}

}
