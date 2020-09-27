package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.PropertyDetailsDTO;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 23, 2020 6:40:29 PM
 	* Class Description
*/
public class PropertyDetailRowMapper implements RowMapper<PropertyDetailsDTO>{

	@Override
	public PropertyDetailsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {		
		return QueryUtils.gePropertyDetailsDTO(rs);
	}

}
