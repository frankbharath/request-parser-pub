package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.model.domain.Property;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 18, 2020 2:16:20 AM
 	* Class Description
*/
public class PropertyRowMapper implements RowMapper<Property> {
	
	@Override
    public Property mapRow(ResultSet rs, int index) throws SQLException {
		return QueryUtils.getProperty(rs);
	}

}
