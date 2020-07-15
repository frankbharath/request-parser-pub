package com.bharath.rm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.tables.Column;
import com.bharath.rm.constants.tables.RM_UserType;
import com.bharath.rm.constants.tables.RM_UserTypeMapper;
import com.bharath.rm.constants.tables.RM_Users;
import com.bharath.rm.constants.tables.RM_Userverification;
import com.bharath.rm.dto.UserDTO;
import com.bharath.rm.model.domain.User;
import com.bharath.rm.model.domain.Verification;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 9, 2020 8:58:19 PM
 	* Class Description
*/
@Repository

public class UserDAOImpl implements UserDAO {

	JdbcTemplate jdbcTemplate;
	
	@Autowired
	public UserDAOImpl(JdbcTemplate template) {
		this.jdbcTemplate=template;
	}
	
	@Override
	public long addUser(User user) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Users.Columns.EMAIL.getColumnName());
		cols.add(RM_Users.Columns.PASSWORD.getColumnName());
		cols.add(RM_Users.Columns.CREATIONTIME.getColumnName());
		cols.add(RM_Users.Columns.VERIFIED.getColumnName());
		StringBuilder builer=new StringBuilder("INSERT INTO ").append(RM_Users.TABLE.getTableName()).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		KeyHolder keyHolder = new GeneratedKeyHolder();
    	jdbcTemplate.update(new PreparedStatementCreator() {
	        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	            PreparedStatement pst = con.prepareStatement(builer.toString(), new String[] {RM_Users.Columns.USERID.getColumnName()});
	            pst.setObject(1, user.getEmail());
	            pst.setObject(2, user.getPassword());
	            pst.setObject(3, user.getCreationtime());
	            pst.setObject(4, false);
	            return pst;
	        }
	    },keyHolder);
    	return (long) keyHolder.getKey();
	}
	
	@Override
	public boolean userExist(String email) {
		String query=new StringBuilder("SELECT EXISTS (SELECT 1 FROM ").append(RM_Users.TABLE.getTableName()).append(" WHERE ")
				.append(RM_Users.Columns.EMAIL.getColumnName()).append("=?)").toString();
		return jdbcTemplate.queryForObject(query, new Object[]{email}, Boolean.class);
	}

	@Override
	public long getUserType(String type) {
		StringBuilder query=new StringBuilder("SELECT ").append(RM_UserType.Columns.TYPEID.getColumnName());
		query.append(" FROM ").append(RM_UserType.TABLE.getTableName()).append(" WHERE ").append(RM_UserType.Columns.TYPE.getColumnName())
		.append("=?");
		return jdbcTemplate.queryForObject(query.toString(), new Object[]{type}, Long.class);
	}

	@Override
	public void mapUserToType(long userid, long typeid) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_UserTypeMapper.Columns.USERID.getColumnName());
		cols.add(RM_UserTypeMapper.Columns.TYPEID.getColumnName());
		StringBuilder builer=new StringBuilder("INSERT INTO ").append(RM_UserTypeMapper.TABLE.getTableName()).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		jdbcTemplate.update(builer.toString(), new Object[]{userid,typeid});
	}

	@Override
	public void addVerificationCode(Verification verification) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Userverification.Columns.USERID.getColumnName());
		cols.add(RM_Userverification.Columns.TOKEN.getColumnName());
		cols.add(RM_Userverification.Columns.CREATIONTIME.getColumnName());
		StringBuilder builer=new StringBuilder("INSERT INTO ").append(RM_Userverification.TABLE.getTableName()).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		jdbcTemplate.update(builer.toString(), new Object[]{verification.getUserid(),verification.getToken(),verification.getCreationtime()});
	}
	
	@Override
	public boolean validateVerificationCode(Verification verification) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Userverification.Columns.USERID.getColumnName());
		cols.add(RM_Userverification.Columns.TOKEN.getColumnName());
		StringBuilder query=new StringBuilder("SELECT EXISTS (SELECT * FROM ").append(RM_Userverification.TABLE.getTableName()).append(" WHERE ")
		.append(RM_Userverification.Columns.USERID.getColumnName()).append("=?")
		.append(" AND ").append(RM_Userverification.Columns.TOKEN.getColumnName()).append("=?")
		.append(" AND ").append(RM_Userverification.Columns.CREATIONTIME.getColumnName()+"+"+Constants.EXPIRATIONINTERVAL).append(">?)");
		System.out.println(query.toString());
		return jdbcTemplate.queryForObject(query.toString(), new Object[] {verification.getUserid(),verification.getToken(),System.currentTimeMillis()}, Boolean.class);
	}
	
	@Override
	public void deleteVerificationCode(Verification verification) {
		StringBuilder builder=new StringBuilder(" DELETE FROM ").append(RM_Userverification.TABLE.getTableName()).append(" WHERE ")
				.append(RM_Userverification.Columns.USERID.getColumnName()).append("=?")
				.append(" AND ").append(RM_Userverification.Columns.TOKEN.getColumnName()).append("=?");
		jdbcTemplate.update(builder.toString(), new Object[] {verification.getUserid(),verification.getToken()});
	}
	
	@Override
	public void verifyUserAccount(long userid) {
		StringBuilder builder=new StringBuilder(" UPDATE ").append(RM_Users.TABLE.getTableName()).append(" SET ").append(RM_Users.Columns.VERIFIED.getColumnName())
				.append("=? WHERE ").append(RM_Users.Columns.USERID.getColumnName()).append("=?");
		jdbcTemplate.update(builder.toString(), new Object[] {true,userid});
	}
}
