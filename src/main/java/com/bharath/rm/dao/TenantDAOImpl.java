package com.bharath.rm.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bharath.rm.common.QueryUtils;
import com.bharath.rm.constants.tables.RM_Lease;
import com.bharath.rm.constants.tables.RM_Tenant;
import com.bharath.rm.dao.interfaces.TenantDAO;
import com.bharath.rm.dto.TenantDTO;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.Tenant;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 25, 2020 3:27:50 PM
 	* Class Description
*/

@Repository
public class TenantDAOImpl implements TenantDAO {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public static final int PAGE_LIMIT=50;
	
	@Autowired
	public TenantDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate=namedParameterJdbcTemplate;
	}
	
	@Override
	public boolean tenantExists(Tenant tenant) {
		StringBuilder query=new StringBuilder("SELECT EXISTS (SELECT 1 FROM ").append(RM_Tenant.TABLENAME);
		
		query.append(" WHERE ").append(RM_Tenant.FIRSTNAME).append("=:").append(RM_Tenant.FIRSTNAME)
		.append(" AND ").append(RM_Tenant.LASTNAME).append("=:").append(RM_Tenant.LASTNAME)
		.append(" AND ").append(RM_Tenant.EMAIL).append("=:").append(RM_Tenant.EMAIL)
		.append(" AND ").append(RM_Tenant.DOB).append("=:").append(RM_Tenant.DOB)
		.append(" AND ").append(RM_Tenant.NATIONALITY).append("=:").append(RM_Tenant.NATIONALITY)
		.append(" AND ").append(RM_Tenant.USERID).append("=:").append(RM_Tenant.USERID);
		if(tenant.getTenantid()!=null) {
			query.append(" AND ").append(RM_Tenant.TENANTID).append("!=:").append(RM_Tenant.TENANTID);
		}
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Tenant.FIRSTNAME, tenant.getFirstname(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.LASTNAME, tenant.getLastname(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.EMAIL, tenant.getEmail(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.DOB, tenant.getDob(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.NATIONALITY, tenant.getNationality(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.USERID, tenant.getTenantuserid(), Types.BIGINT);
		if(tenant.getTenantid()!=null) {
			namedParameters.addValue(RM_Tenant.TENANTID, tenant.getTenantid(), Types.BIGINT);
		}
		
		query.append(")");
		
		return namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Boolean.class);
	}
	
	@Override
	public boolean tenantExists(Long userId, Long tenantId) {
		StringBuilder query=new StringBuilder("SELECT EXISTS (SELECT 1 FROM ").append(RM_Tenant.TABLENAME);
		query.append(" WHERE ").append(RM_Tenant.TENANTID).append("=:").append(RM_Tenant.TENANTID)
		.append(" AND ").append(RM_Tenant.USERID).append("=:").append(RM_Tenant.USERID).append(")");
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Tenant.USERID, userId, Types.BIGINT);
		namedParameters.addValue(RM_Tenant.TENANTID, tenantId, Types.BIGINT);
		
		return namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Boolean.class);
	}
	
	@Override
	public Long addTenant(Tenant tenant) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Tenant.FIRSTNAME);
		cols.add(RM_Tenant.LASTNAME);
		cols.add(RM_Tenant.EMAIL);
		cols.add(RM_Tenant.DOB);
		cols.add(RM_Tenant.NATIONALITY);
		cols.add(RM_Tenant.USERID);
		
		String query=QueryUtils.getInsertQuery(RM_Tenant.TABLENAME, cols);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Tenant.FIRSTNAME, tenant.getFirstname(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.LASTNAME, tenant.getLastname(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.EMAIL, tenant.getEmail(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.DOB, tenant.getDob(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.NATIONALITY, tenant.getNationality(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.USERID, tenant.getTenantuserid(), Types.BIGINT);
		
		namedParameterJdbcTemplate.update(query, namedParameters, keyHolder, new String[]{RM_Tenant.TENANTID});
		return (Long) keyHolder.getKey();
	}
	
	@Override
	public void updateTenant(Tenant tenant) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Tenant.FIRSTNAME);
		cols.add(RM_Tenant.LASTNAME);
		cols.add(RM_Tenant.EMAIL);
		cols.add(RM_Tenant.DOB);
		cols.add(RM_Tenant.NATIONALITY);
		cols.add(RM_Tenant.USERID);
		cols.add(RM_Tenant.TENANTID);
		
		StringBuilder query=new StringBuilder(QueryUtils.getUpdateQuery(RM_Tenant.TABLENAME, cols));
		query.append(" WHERE ").append(RM_Tenant.USERID).append("=:").append(RM_Tenant.USERID)
		.append(" AND ").append(RM_Tenant.TENANTID).append("=:").append(RM_Tenant.TENANTID);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Tenant.FIRSTNAME, tenant.getFirstname(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.LASTNAME, tenant.getLastname(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.EMAIL, tenant.getEmail(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.DOB, tenant.getDob(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.NATIONALITY, tenant.getNationality(), Types.VARCHAR);
		namedParameters.addValue(RM_Tenant.USERID, tenant.getTenantuserid(), Types.BIGINT);
		namedParameters.addValue(RM_Tenant.TENANTID, tenant.getTenantid(), Types.BIGINT);
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameters);
	}
	
	@Override
	public TenantDTO getTenantInfo(Long userId, Long tenantId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Tenant.TENANTID);
		cols.add(RM_Tenant.FIRSTNAME);
		cols.add(RM_Tenant.LASTNAME);
		cols.add(RM_Tenant.EMAIL);
		cols.add(RM_Tenant.DOB);
		cols.add(RM_Tenant.NATIONALITY);
		cols.add(RM_Tenant.USERID);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Tenant.TABLENAME);
		query.append(" WHERE ").append(RM_Tenant.TENANTID).append("=:").append(RM_Tenant.TENANTID)
		.append(" AND ").append(RM_Tenant.USERID).append("=:").append(RM_Tenant.USERID);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Tenant.TENANTID, tenantId, Types.BIGINT);
		namedParameters.addValue(RM_Tenant.USERID, userId, Types.BIGINT);
		
		return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(query.toString(), namedParameters, new BeanPropertyRowMapper<TenantDTO>(TenantDTO.class)));
	}
	
	@Override
	public List<TenantDTO> getTenants(Long userId, String searchQuery, Integer pageNo) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Tenant.TENANTID);
		cols.add(RM_Tenant.FIRSTNAME);
		cols.add(RM_Tenant.LASTNAME);
		cols.add(RM_Tenant.EMAIL);
		cols.add(RM_Tenant.DOB);
		cols.add(RM_Tenant.NATIONALITY);
		cols.add(RM_Tenant.USERID);
		
		StringBuilder query=new StringBuilder("SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Tenant.TABLENAME);
		query.append(" WHERE ").append(RM_Tenant.USERID).append("=:").append(RM_Tenant.USERID);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Tenant.USERID, userId, Types.BIGINT);
		if(searchQuery!=null) {
			query.append(" AND ").append(RM_Tenant.FIRSTNAME).append(" LIKE :").append(RM_Tenant.FIRSTNAME)
			.append(" OR ").append(RM_Tenant.LASTNAME).append(" LIKE :").append(RM_Tenant.LASTNAME)
			.append(" OR ").append(RM_Tenant.EMAIL).append(" LIKE :").append(RM_Tenant.EMAIL);
			namedParameters.addValue(RM_Tenant.FIRSTNAME, "%"+searchQuery+"%", Types.VARCHAR);
			namedParameters.addValue(RM_Tenant.LASTNAME, "%"+searchQuery+"%", Types.VARCHAR);
			namedParameters.addValue(RM_Tenant.EMAIL, "%"+searchQuery+"%", Types.VARCHAR);
		}
		query.append(" ORDER BY ").append(RM_Tenant.FIRSTNAME).append(", ").append(RM_Tenant.LASTNAME);
		query.append(" LIMIT ").append(PAGE_LIMIT);
		if(pageNo!=null) {
			query.append(" OFFSET ").append(PAGE_LIMIT*(pageNo-1));
		}
		
		return namedParameterJdbcTemplate.query(query.toString(), namedParameters, new BeanPropertyRowMapper<TenantDTO>(TenantDTO.class));
	}
	
	@Override
	public Integer getTenantsCount(Long userId, String searchQuery) {
		StringBuilder query=new StringBuilder("SELECT COUNT(*) FROM ").append(RM_Tenant.TABLENAME);
		query.append(" WHERE ").append(RM_Tenant.USERID).append("=:").append(RM_Tenant.USERID);
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Tenant.USERID, userId, Types.BIGINT);
		if(searchQuery!=null) {
			query.append(" AND ").append(RM_Tenant.FIRSTNAME).append(" LIKE :").append(RM_Tenant.FIRSTNAME)
			.append(" OR ").append(RM_Tenant.LASTNAME).append(" LIKE :").append(RM_Tenant.LASTNAME)
			.append(" OR ").append(RM_Tenant.EMAIL).append(" LIKE :").append(RM_Tenant.EMAIL);
			namedParameters.addValue(RM_Tenant.FIRSTNAME, "%"+searchQuery+"%", Types.VARCHAR);
			namedParameters.addValue(RM_Tenant.LASTNAME, "%"+searchQuery+"%", Types.VARCHAR);
			namedParameters.addValue(RM_Tenant.EMAIL, "%"+searchQuery+"%", Types.VARCHAR);
		}
		return namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Integer.class);
	}
	
	@Override
	public void deleteTenants(Long userId, List<Long> tenantIds) {
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Tenant.USERID, userId, Types.BIGINT);
		
		namedParameters.addValue(RM_Tenant.TENANTID, tenantIds);
		
		StringBuilder query=new StringBuilder("DELETE ").append(" FROM ").append(RM_Tenant.TABLENAME)
				.append(" WHERE ").append(RM_Tenant.TENANTID).append(" IN (:").append(RM_Tenant.TENANTID).append(")");
		query.append(" AND ").append(RM_Tenant.USERID).append("= :").append(RM_Tenant.USERID);
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameters);
	}
	
	@Override
	public Long addLease(Lease lease) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Lease.LEASETENANTID);
		cols.add(RM_Lease.TENANTSPROPERTYDETAILID);
		cols.add(RM_Lease.MOVEIN);
		cols.add(RM_Lease.MOVEOUT);
		cols.add(RM_Lease.OCCUPANTS);
		
		String query=QueryUtils.getInsertQuery(RM_Lease.TABLENAME, cols);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Lease.LEASETENANTID, lease.getLeasetenantid(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.TENANTSPROPERTYDETAILID, lease.getTenantspropertydetailid(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.MOVEIN, lease.getMovein(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.MOVEOUT, lease.getMoveout(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.OCCUPANTS, lease.getOccupants(), Types.INTEGER);
		
		namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
		return (Long) keyHolder.getKey();
	}
}
