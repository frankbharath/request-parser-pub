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
import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.PropertyDetailsDTO;
import com.bharath.rm.model.domain.Address;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 19, 2020 12:38:58 AM
 	* Class Description
*/
public class ApartmentRowMapper implements RowMapper<ApartmentDTO>{

	@Override
	public ApartmentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.getApartmentDTO(rs);
	}

}
