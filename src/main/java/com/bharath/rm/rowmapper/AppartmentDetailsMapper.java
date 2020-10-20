package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.model.domain.ApartmentPropertyDetails;

/**
 * The Class AppartmentDetailsMapper.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 19, 2020 12:43:41 AM
 */

public class AppartmentDetailsMapper implements RowMapper<ApartmentPropertyDetails>{

	/**
	 * Map row.
	 *
	 * @param rs the rs
	 * @param rowNum the row num
	 * @return the apartment property details
	 * @throws SQLException the SQL exception
	 */
	@Override
	public ApartmentPropertyDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.getApartmentPropertyDetail(rs);
	}

}
