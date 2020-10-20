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
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.tables.RM_Contractstatus;
import com.bharath.rm.constants.tables.RM_Lease;
import com.bharath.rm.constants.tables.RM_Tenant;
import com.bharath.rm.dao.interfaces.TenantDAO;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.Tenant;
import com.bharath.rm.rowmapper.LeaseRowMapper;

/**
 * The Class TenantDAOImpl.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 25, 2020 3:27:50 PM
 */

@Repository
public class TenantDAOImpl implements TenantDAO {

	/** The named parameter jdbc template. */
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/** The Constant PAGE_LIMIT. */
	public static final int PAGE_LIMIT=50;
	
	/**
	 * Instantiates a new tenant DAO impl.
	 *
	 * @param namedParameterJdbcTemplate the named parameter jdbc template
	 */
	@Autowired
	public TenantDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate=namedParameterJdbcTemplate;
	}
	
	/**
	 * Tenant exists.
	 *
	 * @param tenant the tenant
	 * @return true, if successful
	 */
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
	
	/**
	 * Tenant exists.
	 *
	 * @param userId the user id
	 * @param tenantId the tenant id
	 * @return true, if successful
	 */
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
	
	/**
	 * Adds the tenant.
	 *
	 * @param tenant the tenant
	 * @return the long
	 */
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
	
	/**
	 * Update tenant.
	 *
	 * @param tenant the tenant
	 */
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
	
	/**
	 * Gets the tenant info.
	 *
	 * @param userId the user id
	 * @param tenantId the tenant id
	 * @return the tenant info
	 */
	@Override
	public Tenant getTenantInfo(Long userId, Long tenantId) {
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
		
		return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(query.toString(), namedParameters, new BeanPropertyRowMapper<Tenant>(Tenant.class)));
	}
	
	/**
	 * Gets the tenants.
	 *
	 * @param userId the user id
	 * @param searchQuery the search query
	 * @param pageNo the page no
	 * @return the tenants
	 */
	@Override
	public List<Tenant> getTenants(Long userId, String searchQuery, Integer pageNo) {
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
		
		return namedParameterJdbcTemplate.query(query.toString(), namedParameters, new BeanPropertyRowMapper<Tenant>(Tenant.class));
	}
	
	/**
	 * Gets the tenants count.
	 *
	 * @param userId the user id
	 * @param searchQuery the search query
	 * @return the tenants count
	 */
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
	
	/**
	 * Delete tenants.
	 *
	 * @param userId the user id
	 * @param tenantIds the tenant ids
	 */
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
	
	/**
	 * Adds the lease.
	 *
	 * @param lease the lease
	 * @return the long
	 */
	@Override
	public Long addLease(Lease lease) {
		List<String> cols=new ArrayList<>();
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		cols.add(RM_Lease.LEASETENANTID);
		cols.add(RM_Lease.TENANTSPROPERTYDETAILID);
		cols.add(RM_Lease.MOVEIN);
		cols.add(RM_Lease.MOVEOUT);
		cols.add(RM_Lease.OCCUPANTS);
		cols.add(RM_Lease.STATUSID);
		cols.add(RM_Lease.RENT);
		
		namedParameters.addValue(RM_Lease.LEASETENANTID, lease.getLeasetenantid(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.TENANTSPROPERTYDETAILID, lease.getTenantspropertydetailid(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.MOVEIN, lease.getMovein(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.MOVEOUT, lease.getMoveout(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.OCCUPANTS, lease.getOccupants(), Types.INTEGER);
		namedParameters.addValue(RM_Lease.STATUSID, lease.getContractstatus().getContractstatusid(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.RENT, lease.getRent(), Types.FLOAT);
		
		String query=QueryUtils.getInsertQuery(RM_Lease.TABLENAME, cols);
		namedParameterJdbcTemplate.update(query, namedParameters, keyHolder,  new String[] {RM_Lease.LEASEID});
		return (Long) keyHolder.getKey();
	}
	
	/**
	 * Update lease.
	 *
	 * @param lease the lease
	 */
	@Override
	public void updateLease(Lease lease) {
		List<String> cols=new ArrayList<>();
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
	
		cols.add(RM_Lease.TENANTSPROPERTYDETAILID);
		cols.add(RM_Lease.MOVEIN);
		cols.add(RM_Lease.MOVEOUT);
		cols.add(RM_Lease.OCCUPANTS);
		cols.add(RM_Lease.STATUSID);
		cols.add(RM_Lease.RENT);
		
		namedParameters.addValue(RM_Lease.TENANTSPROPERTYDETAILID, lease.getTenantspropertydetailid(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.MOVEIN, lease.getMovein(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.MOVEOUT, lease.getMoveout(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.OCCUPANTS, lease.getOccupants(), Types.INTEGER);
		namedParameters.addValue(RM_Lease.STATUSID, lease.getContractstatus().getContractstatusid(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.RENT, lease.getRent(), Types.FLOAT);
		
		StringBuilder query=new StringBuilder(QueryUtils.getUpdateQuery(RM_Lease.TABLENAME, cols))
				.append(" WHERE ").append(RM_Lease.LEASEID).append("=:").append(RM_Lease.LEASEID)
				.append(" AND ").append(RM_Lease.LEASETENANTID).append("=:").append(RM_Lease.LEASETENANTID);
		
		namedParameters.addValue(RM_Lease.LEASEID, lease.getLeaseid(), Types.BIGINT);
		namedParameters.addValue(RM_Lease.LEASETENANTID, lease.getLeasetenantid(), Types.BIGINT);
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameters);
	}
	
	/**
	 * Lease exist.
	 *
	 * @param userId the user id
	 * @param leaseId the lease id
	 * @return the boolean
	 */
	@Override
	public Boolean leaseExist(Long userId, Long leaseId) {
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		
		StringBuilder query=new StringBuilder("SELECT EXISTS (SELECT 1 FROM ").append(RM_Tenant.TABLENAME)
				.append(" INNER JOIN ").append(RM_Lease.TABLENAME).append(" ON ").append(RM_Tenant.TENANTID).append("=").append(RM_Lease.LEASETENANTID)
				.append(" WHERE ").append(RM_Tenant.USERID).append("=:").append(RM_Tenant.USERID)
				.append(" AND ").append(RM_Lease.LEASEID).append("=:").append(RM_Lease.LEASEID)
				.append(")");
		
		namedParameters.addValue(RM_Tenant.USERID, userId, Types.BIGINT);
		namedParameters.addValue(RM_Lease.LEASEID, leaseId, Types.BIGINT);
		
		return namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, Boolean.class);
	}
	
	
	/**
	 * Gets the lease.
	 *
	 * @param userId the user id
	 * @param leaseId the lease id
	 * @return the lease
	 */
	@Override
	public Lease getLease(Long userId, Long leaseId) {
		List<String> cols=new ArrayList<>();
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		
		cols.add(RM_Lease.LEASEID);
		cols.add(RM_Lease.LEASETENANTID);
		cols.add(RM_Lease.TENANTSPROPERTYDETAILID);
		cols.add(RM_Lease.MOVEIN);
		cols.add(RM_Lease.MOVEOUT);
		cols.add(RM_Lease.OCCUPANTS);
		cols.add(RM_Lease.RENT);
		cols.add(RM_Lease.CONTRACTID);
		cols.add(RM_Lease.STATUSID);
		
		StringBuilder query=new StringBuilder(" SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Lease.TABLENAME)
				.append(" INNER JOIN ").append(RM_Tenant.TABLENAME).append(" ON ").append(RM_Tenant.TENANTID).append("=").append(RM_Lease.LEASETENANTID)
				.append(" INNER JOIN ").append(RM_Contractstatus.TABLENAME).append(" ON ").append(RM_Contractstatus.CONTRACTSTATUSID).append("=").append(RM_Lease.STATUSID)
				.append(" WHERE ").append(RM_Tenant.USERID).append("=:").append(RM_Tenant.USERID)
				.append(" AND ").append(RM_Lease.LEASEID).append("=:").append(RM_Lease.LEASEID);

		namedParameters.addValue(RM_Tenant.USERID, userId, Types.BIGINT);
		namedParameters.addValue(RM_Lease.LEASEID, leaseId, Types.BIGINT);
		
		return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(query.toString(), namedParameters, new LeaseRowMapper()));
	}

	/**
	 * Gets the all lease for tenant.
	 *
	 * @param userId the user id
	 * @param tenantId the tenant id
	 * @return the all lease for tenant
	 */
	@Override
	public List<Lease> getAllLeaseForTenant(Long userId, Long tenantId) {
		List<String> cols=new ArrayList<>();
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		
		cols.add(RM_Lease.LEASEID);
		cols.add(RM_Lease.LEASETENANTID);
		cols.add(RM_Lease.TENANTSPROPERTYDETAILID);
		cols.add(RM_Lease.MOVEIN);
		cols.add(RM_Lease.MOVEOUT);
		cols.add(RM_Lease.OCCUPANTS);
		cols.add(RM_Lease.RENT);
		cols.add(RM_Lease.CONTRACTID);
		cols.add(RM_Lease.STATUSID);
		
		StringBuilder query=new StringBuilder(" SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Lease.TABLENAME)
				.append(" INNER JOIN ").append(RM_Tenant.TABLENAME).append(" ON ").append(RM_Tenant.TENANTID).append("=").append(RM_Lease.LEASETENANTID)
				.append(" INNER JOIN ").append(RM_Contractstatus.TABLENAME).append(" ON ").append(RM_Contractstatus.CONTRACTSTATUSID).append("=").append(RM_Lease.STATUSID)
				.append(" WHERE ").append(RM_Tenant.USERID).append("=:").append(RM_Tenant.USERID)
				.append(" AND ").append(RM_Lease.LEASETENANTID).append("=:").append(RM_Lease.LEASETENANTID);

		namedParameters.addValue(RM_Tenant.USERID, userId, Types.BIGINT);
		namedParameters.addValue(RM_Lease.LEASETENANTID, tenantId, Types.BIGINT);
		
		return namedParameterJdbcTemplate.query(query.toString(), namedParameters, new BeanPropertyRowMapper<Lease>(Lease.class));
	}
	
	/**
	 * Gets the contract status.
	 *
	 * @param leaseId the lease id
	 * @return the contract status
	 */
	@Override
	public String getContractStatus(Long leaseId) {
		List<String> cols=new ArrayList<>();
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		
		cols.add(RM_Contractstatus.CONTRACTSTATUS);
		
		StringBuilder query=new StringBuilder(" SELECT ").append(String.join(",", cols)).append(" FROM ").append(RM_Lease.TABLENAME)
				.append(" INNER JOIN ").append(RM_Contractstatus.TABLENAME).append(" ON ").append(RM_Contractstatus.CONTRACTSTATUSID).append("=").append(RM_Lease.STATUSID)
				.append(" AND ").append(RM_Lease.LEASEID).append("=:").append(RM_Lease.LEASEID);
		
		namedParameters.addValue(RM_Lease.LEASEID, leaseId, Types.BIGINT);
		return namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameters, String.class);
	}
	
	/**
	 * Gets the contract status.
	 *
	 * @param status the status
	 * @return the contract status
	 */
	@Override
	public Long getContractStatus(Constants.ContractStatus status) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Contractstatus.CONTRACTSTATUSID);
		
		StringBuilder query=new StringBuilder("SELECT ").append(RM_Contractstatus.CONTRACTSTATUSID).append(" FROM ").append(RM_Contractstatus.TABLENAME);
		query.append(" WHERE ").append(RM_Contractstatus.CONTRACTSTATUS).append("=:").append(RM_Contractstatus.CONTRACTSTATUS);
		
		MapSqlParameterSource  namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_Contractstatus.CONTRACTSTATUS, status.toString(), Types.VARCHAR);
		
		return namedParameterJdbcTemplate.queryForObject(query.toString(), namedParameter, Long.class);
	}
	
	/**
	 * Update contract id.
	 *
	 * @param leaseId the lease id
	 * @param requestId the request id
	 */
	@Override
	public void updateContractId(Long leaseId, String requestId) {
		List<String> cols=new ArrayList<>();
		cols.add(RM_Lease.CONTRACTID);
		
		StringBuilder query=new StringBuilder(QueryUtils.getUpdateQuery(RM_Lease.TABLENAME, cols))
				.append(" WHERE ").append(RM_Lease.LEASEID).append("=:").append(RM_Lease.LEASEID);
		
		MapSqlParameterSource  namedParameter = new MapSqlParameterSource();
		namedParameter.addValue(RM_Lease.CONTRACTID, requestId, Types.VARCHAR);
		namedParameter.addValue(RM_Lease.LEASEID, leaseId, Types.BIGINT);
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameter);
		
	}
	
	/**
	 * Delete lease.
	 *
	 * @param leaseId the lease id
	 */
	@Override
	public void deleteLease(Long leaseId) {
		MapSqlParameterSource  namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(RM_Lease.LEASEID, leaseId, Types.BIGINT);
		
		StringBuilder query=new StringBuilder("DELETE ").append(" FROM ").append(RM_Lease.TABLENAME)
				.append(" WHERE ").append(RM_Lease.LEASEID).append("= :").append(RM_Lease.LEASEID);
		
		namedParameterJdbcTemplate.update(query.toString(), namedParameters);
	}
}
