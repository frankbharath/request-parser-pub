package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.model.domain.House;

/**
 * The Class HouseMapper.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 19, 2020 12:01:11 AM
 */

public class HouseMapper implements RowMapper<House>{

	/**
	 * Map row.
	 *
	 * @param rs the rs
	 * @param rowNum the row num
	 * @return the house
	 * @throws SQLException the SQL exception
	 */
	@Override
	public House mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.getHouse(rs);
	}

}
