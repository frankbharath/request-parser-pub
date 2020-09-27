package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.tables.RM_Property;
import com.bharath.rm.dto.PropertyDTO;
import com.bharath.rm.model.domain.Address;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 18, 2020 2:16:20 AM
 	* Class Description
*/
public class PropertyRowMapper implements RowMapper<PropertyDTO> {
	
	@Override
    public PropertyDTO mapRow(ResultSet rs, int index) throws SQLException {
		return QueryUtils.getPropertyDTO(rs);
	}

}
