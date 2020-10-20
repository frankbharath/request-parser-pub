package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.model.domain.Lease;

/**
 * The Class LeaseRowMapper.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Oct 9, 2020 11:01:04 PM
 */

public class LeaseRowMapper implements RowMapper<Lease> {

	/**
	 * Map row.
	 *
	 * @param rs the rs
	 * @param rowNum the row num
	 * @return the lease
	 * @throws SQLException the SQL exception
	 */
	@Override
	public Lease mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.getLease(rs);
	}

}
