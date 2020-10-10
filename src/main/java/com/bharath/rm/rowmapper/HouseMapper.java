package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.model.domain.House;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 19, 2020 12:01:11 AM
 	* Class Description
*/
public class HouseMapper implements RowMapper<House>{

	@Override
	public House mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.getHouse(rs);
	}

}
