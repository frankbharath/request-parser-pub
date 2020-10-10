package com.bharath.rm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.model.domain.Lease;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Oct 9, 2020 11:01:04 PM
 	* Class Description
*/
public class LeaseRowMapper implements RowMapper<Lease> {

	@Override
	public Lease mapRow(ResultSet rs, int rowNum) throws SQLException {
		return QueryUtils.getLease(rs);
	}

}
