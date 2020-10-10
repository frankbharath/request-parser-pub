package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.model.domain.Apartment;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 19, 2020 12:38:58 AM
 	* Class Description
*/
public class ApartmentRowMapper implements RowMapper<Apartment>{

	@Override
	public Apartment mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.getApartment(rs);
	}

}
