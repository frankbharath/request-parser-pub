package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.constants.tables.RM_AppartmentPropertyDetails;
import com.bharath.rm.dto.ApartmentPropertyDetailDTO;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 19, 2020 12:43:41 AM
 	* Class Description
*/
public class AppartmentDetailsMapper implements RowMapper<ApartmentPropertyDetailDTO>{

	@Override
	public ApartmentPropertyDetailDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.geApartmentPropertyDetailDTO(rs);
	}

}
