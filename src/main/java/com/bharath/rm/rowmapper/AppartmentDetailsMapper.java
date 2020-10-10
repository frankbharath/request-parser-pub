package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.model.domain.ApartmentPropertyDetails;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 19, 2020 12:43:41 AM
 	* Class Description
*/
public class AppartmentDetailsMapper implements RowMapper<ApartmentPropertyDetails>{

	@Override
	public ApartmentPropertyDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.getApartmentPropertyDetail(rs);
	}

}
