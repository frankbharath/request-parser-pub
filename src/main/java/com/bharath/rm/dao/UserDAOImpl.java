package com.bharath.rm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bharath.rm.constants.Constants.Tokentype;
import com.bharath.rm.constants.tables.RM_UserType;
import com.bharath.rm.constants.tables.RM_Users;
import com.bharath.rm.constants.tables.RM_Userverification;
import com.bharath.rm.dao.interfaces.UserDAO;
import com.bharath.rm.model.domain.User;
import com.bharath.rm.model.domain.Verification;

/**
 * The Class UserDAOImpl.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 9, 2020 8:58:19 PM
 */

@Repository

public class UserDAOImpl implements UserDAO {

	/** The jdbc template. */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Instantiates a new user DAO impl.
	 *
	 * @param template the template
	 */
	@Autowired
	public UserDAOImpl(JdbcTemplate template) {
		this.jdbcTemplate=template;
	}
	
	/**
	 * Adds the user.
	 *
	 * @param user the user
	 * @return the long
	 */
	@Override
	public long addUser(User user) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Users.Columns.EMAIL.getColumnName());
		cols.add(RM_Users.Columns.PASSWORD.getColumnName());
		cols.add(RM_Users.Columns.CREATIONTIME.getColumnName());
		cols.add(RM_Users.Columns.VERIFIED.getColumnName());
		cols.add(RM_Users.Columns.TYPEID.getColumnName());
		StringBuilder query=new StringBuilder("INSERT INTO ").append(RM_Users.TABLE.getTableName()).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		KeyHolder keyHolder = new GeneratedKeyHolder();
    	jdbcTemplate.update(new PreparedStatementCreator() {
	        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	            PreparedStatement pst = con.prepareStatement(query.toString(), new String[] {RM_Users.Columns.USERID.getColumnName()});
	            pst.setObject(1, user.getEmail());
	            pst.setObject(2, user.getPassword());
	            pst.setObject(3, user.getCreationtime());
	            pst.setObject(4, false);
	            pst.setObject(5, user.getUsertype().getTypeid());
	            return pst;
	        }
	    },keyHolder);
    	return (long) keyHolder.getKey();
	}
	
	/**
	 * User exist.
	 *
	 * @param email the email
	 * @return true, if successful
	 */
	@Override
	public boolean userExist(String email) {
		String query=new StringBuilder("SELECT EXISTS (SELECT 1 FROM ").append(RM_Users.TABLE.getTableName()).append(" WHERE ")
				.append(RM_Users.Columns.EMAIL.getColumnName()).append("=?)").toString();
		return jdbcTemplate.queryForObject(query, new Object[]{email}, Boolean.class);
	}

	/**
	 * Gets the user type.
	 *
	 * @param type the type
	 * @return the user type
	 */
	@Override
	public long getUserType(String type) {
		StringBuilder query=new StringBuilder("SELECT ").append(RM_UserType.Columns.TYPEID.getColumnName());
		query.append(" FROM ").append(RM_UserType.TABLE.getTableName()).append(" WHERE ").append(RM_UserType.Columns.TYPE.getColumnName())
		.append("=?");
		return jdbcTemplate.queryForObject(query.toString(), new Object[]{type}, Long.class);
	}

	/**
	 * Adds the verification code.
	 *
	 * @param verification the verification
	 */
	@Override
	public void addVerificationCode(Verification verification) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Userverification.Columns.USERID.getColumnName());
		cols.add(RM_Userverification.Columns.TYPE.getColumnName());
		cols.add(RM_Userverification.Columns.TOKEN.getColumnName());
		cols.add(RM_Userverification.Columns.CREATIONTIME.getColumnName());
		StringBuilder builer=new StringBuilder("INSERT INTO ").append(RM_Userverification.TABLE.getTableName()).append("(")
				.append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(cols.size()).collect(Collectors.joining(","))).append(")");
		jdbcTemplate.update(builer.toString(), new Object[]{verification.getUserid(),verification.getType(), verification.getToken(),verification.getCreationtime()});
	}
	
	/**
	 * Gets the verification code.
	 *
	 * @param token the token
	 * @param type the type
	 * @return the verification code
	 */
	@Override
	public Verification getVerificationCode(String token, Tokentype type) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Userverification.Columns.TOKEN.getColumnName());
		StringBuilder query=new StringBuilder("SELECT * FROM ").append(RM_Userverification.TABLE.getTableName()).append(" WHERE ")
		.append(RM_Userverification.Columns.TOKEN.getColumnName()).append("=?")
		.append(" AND ").append(RM_Userverification.Columns.TYPE.getColumnName()).append("=?");
		return DataAccessUtils.singleResult(jdbcTemplate.query(query.toString(), new Object[] {token, type.getValue()}, new BeanPropertyRowMapper<Verification>(Verification.class)));
	}
	
	/**
	 * Delete verification code.
	 *
	 * @param verification the verification
	 */
	@Override
	public void deleteVerificationCode(Verification verification) {
		StringBuilder query=new StringBuilder(" DELETE FROM ").append(RM_Userverification.TABLE.getTableName()).append(" WHERE ")
			.append(RM_Userverification.Columns.USERID.getColumnName()).append("=?")
			.append(" AND ").append(RM_Userverification.Columns.TYPE.getColumnName()).append("=?");
		jdbcTemplate.update(query.toString(), new Object[] {verification.getUserid(), verification.getType()});
	}
	
	/**
	 * Delete user account.
	 *
	 * @param userId the user id
	 */
	@Override
	public void deleteUserAccount(long userId) {
		StringBuilder query=new StringBuilder(" DELETE FROM ").append(RM_Users.TABLE.getTableName()).append(" WHERE ")
				.append(RM_Users.Columns.USERID.getColumnName()).append("=?");
		jdbcTemplate.update(query.toString(), new Object[] {userId});
	}
	
	/**
	 * Verify user account.
	 *
	 * @param userid the userid
	 */
	@Override
	public void verifyUserAccount(long userid) {
		StringBuilder query=new StringBuilder(" UPDATE ").append(RM_Users.TABLE.getTableName()).append(" SET ").append(RM_Users.Columns.VERIFIED.getColumnName())
				.append("=? WHERE ").append(RM_Users.Columns.USERID.getColumnName()).append("=?");
		jdbcTemplate.update(query.toString(), new Object[] {true,userid});
	}
	
	/**
	 * Checks if is user account verified.
	 *
	 * @param userid the userid
	 * @return true, if is user account verified
	 */
	@Override
	public boolean isUserAccountVerified(long userid) {
		StringBuilder query=new StringBuilder("SELECT ").append(RM_Users.Columns.VERIFIED.getColumnName()).append(" FROM ").append(RM_Users.TABLE.getTableName())
				.append(" WHERE ").append(RM_Users.Columns.USERID.getColumnName()).append("=?");
		return jdbcTemplate.queryForObject(query.toString(), new Object[]{userid}, Boolean.class);
	}
	
	/**
	 * Gets the user email.
	 *
	 * @param userid the userid
	 * @return the user email
	 */
	@Override
	public String getUserEmail(long userid) {
		StringBuilder query=new StringBuilder("SELECT ").append(RM_Users.Columns.EMAIL.getColumnName()).append(" FROM ").append(RM_Users.TABLE.getTableName())
				.append(" WHERE ").append(RM_Users.Columns.USERID).append("=?");
		return DataAccessUtils.singleResult(jdbcTemplate.queryForList(query.toString(), new Object[]{userid}, String.class));
	}
	
	/**
	 * Gets the user id.
	 *
	 * @param email the email
	 * @return the user id
	 */
	@Override
	public Long getUserId(String email) {
		StringBuilder query=new StringBuilder("SELECT ").append(RM_Users.Columns.USERID.getColumnName()).append(" FROM ").append(RM_Users.TABLE.getTableName())
				.append(" WHERE ").append(RM_Users.Columns.EMAIL.getColumnName()).append("=?");
		return DataAccessUtils.singleResult(jdbcTemplate.queryForList(query.toString(), new Object[]{email}, Long.class));
	}
	
	/**
	 * Gets the user email for token.
	 *
	 * @param token the token
	 * @param type the type
	 * @return the user email for token
	 */
	@Override
	public String getUserEmailForToken(String token, Tokentype type) {
		StringBuilder query=new StringBuilder("SELECT ").append(RM_Users.Columns.EMAIL).append(" FROM ").append(RM_Users.TABLE.getTableName())
				.append(" INNER JOIN ").append(RM_Userverification.TABLE.getTableName()).append(" ON ")
				.append(RM_Users.TABLE.getTableName()+"."+RM_Users.Columns.USERID).append("=")
				.append(RM_Userverification.TABLE.getTableName()+"."+RM_Userverification.Columns.USERID.getColumnName()).append(" WHERE ")
				.append(RM_Userverification.Columns.TOKEN.getColumnName()).append("=?")
				.append(" AND ").append(RM_Userverification.Columns.TYPE.getColumnName()).append("=?");
		return DataAccessUtils.singleResult(jdbcTemplate.queryForList(query.toString(), new Object[] {token, type.getValue()}, String.class));
	}
	
	/**
	 * Update password.
	 *
	 * @param userId the user id
	 * @param password the password
	 */
	@Override
	public void updatePassword(long userId, String password) {
		StringBuilder query=new StringBuilder(" UPDATE ").append(RM_Users.TABLE.getTableName()).append(" SET ").append(RM_Users.Columns.PASSWORD.getColumnName())
				.append("=? WHERE ").append(RM_Users.Columns.USERID.getColumnName()).append("=?");
		jdbcTemplate.update(query.toString(), new Object[] {password,userId});
	}
	
	/**
	 * Gets the user.
	 *
	 * @param email the email
	 * @return the user
	 */
	@Override
	public User getUser(String email) {
		StringBuilder query=new StringBuilder("SELECT *").append(" FROM ").append(RM_Users.TABLE.getTableName())
				.append(" WHERE ").append(RM_Users.Columns.EMAIL.getColumnName()).append("=?");
		return DataAccessUtils.singleResult(jdbcTemplate.query(query.toString(), new Object[] {email}, new BeanPropertyRowMapper<User>(User.class)));
	}
	
	/**
	 * Verification status.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	@Override
	public Boolean verificationStatus(long userId) {
		StringBuilder query=new StringBuilder("SELECT ").append(RM_Users.Columns.VERIFIED.getColumnName()).append(" FROM ").append(RM_Users.TABLE.getTableName())
				.append(" WHERE ").append(RM_Users.Columns.USERID.getColumnName()).append("=?");
		return DataAccessUtils.singleResult(jdbcTemplate.queryForList(query.toString(), new Object[] {userId}, Boolean.class));
	}
	
	/**
	 * Gets the user type.
	 *
	 * @param userId the user id
	 * @return the user type
	 */
	@Override
	public String getUserType(long userId) {
		StringBuilder query=new StringBuilder("SELECT ").append(RM_UserType.Columns.TYPE.getColumnName()).append(" FROM ").append(RM_Users.TABLE.getTableName())
				.append(" INNER JOIN ").append(RM_UserType.TABLE.getTableName()).append(" ON ")
				.append(RM_Users.TABLE.getTableName()+"."+RM_Users.Columns.TYPEID).append("=").append(RM_UserType.TABLE.getTableName()+"."+RM_UserType.Columns.TYPEID)
				.append(" WHERE ").append(RM_Users.Columns.USERID.getColumnName()).append("=?");
		return DataAccessUtils.singleResult(jdbcTemplate.queryForList(query.toString(), new Object[]{userId}, String.class));
	}
	
	
}
