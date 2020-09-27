package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.tables.RM_Property;
import com.bharath.rm.constants.tables.RM_PropertyDetails;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.PropertyDTO;
import com.bharath.rm.dto.PropertyDetailsDTO;
import com.bharath.rm.model.domain.Address;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 19, 2020 12:01:11 AM
 	* Class Description
*/
public class HouseMapper implements RowMapper<HouseDTO>{

	@Override
	public HouseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.getHouseDTO(rs);
	}

}
