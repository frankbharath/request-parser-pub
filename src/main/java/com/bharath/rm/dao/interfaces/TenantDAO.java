package com.bharath.rm.dao.interfaces;

import java.util.List;

import com.bharath.rm.constants.Constants.ContractStatus;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.Tenant;

/**
 * The Interface TenantDAO.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 25, 2020 3:27:06 PM
 * This interface contains all mandatory function that should be implemented when class implements.
 */
public interface TenantDAO {
	
	/**
	 * Adds the tenant.
	 *
	 * @param tenant the tenant
	 * @return the long
	 */
	public Long addTenant(Tenant tenant);
	
	/**
	 * Adds the lease.
	 *
	 * @param lease the lease
	 * @return the long
	 */
	public Long addLease(Lease lease);
	
	/**
	 * Tenant exists.
	 *
	 * @param tenant the tenant
	 * @return true, if successful
	 */
	public boolean tenantExists(Tenant tenant);
	
	/**
	 * Tenant exists.
	 *
	 * @param userId the user id
	 * @param tenantId the tenant id
	 * @return true, if successful
	 */
	public boolean tenantExists(Long userId, Long tenantId);
	
	/**
	 * Gets the tenant info.
	 *
	 * @param userId the user id
	 * @param tenantId the tenant id
	 * @return the tenant info
	 */
	public Tenant getTenantInfo(Long userId, Long tenantId);
	
	/**
	 * Update tenant.
	 *
	 * @param tenant the tenant
	 */
	public void updateTenant(Tenant tenant);
	
	/**
	 * Delete tenants.
	 *
	 * @param userId the user id
	 * @param tenantIds the tenant ids
	 */
	public void deleteTenants(Long userId, List<Long> tenantIds);
	
	/**
	 * Gets the tenants.
	 *
	 * @param userId the user id
	 * @param searchQuery the search query
	 * @param pageNo the page no
	 * @return the tenants
	 */
	public List<Tenant> getTenants(Long userId, String searchQuery, Integer pageNo);
	
	/**
	 * Gets the tenants count.
	 *
	 * @param userId the user id
	 * @param searchQuery the search query
	 * @return the tenants count
	 */
	public Integer getTenantsCount(Long userId, String searchQuery);
	
	/**
	 * Gets the contract status.
	 *
	 * @param status the status
	 * @return the contract status
	 */
	public Long getContractStatus(ContractStatus status);
	
	/**
	 * Update contract id.
	 *
	 * @param leaseId the lease id
	 * @param requestId the request id
	 */
	public void updateContractId(Long leaseId, String requestId);
	
	/**
	 * Gets the lease.
	 *
	 * @param userId the user id
	 * @param leaseId the lease id
	 * @return the lease
	 */
	public Lease getLease(Long userId, Long leaseId);
	
	/**
	 * Lease exist.
	 *
	 * @param userId the user id
	 * @param leaseId the lease id
	 * @return the boolean
	 */
	public Boolean leaseExist(Long userId, Long leaseId);
	
	/**
	 * Gets the contract status.
	 *
	 * @param leaseId the lease id
	 * @return the contract status
	 */
	public String getContractStatus(Long leaseId);
	
	/**
	 * Update lease.
	 *
	 * @param lease the lease
	 */
	public void updateLease(Lease lease);
	
	/**
	 * Gets the all lease for tenant.
	 *
	 * @param userId the user id
	 * @param tenantId the tenant id
	 * @return the all lease for tenant
	 */
	public List<Lease> getAllLeaseForTenant(Long userId, Long tenantId);
	
	/**
	 * Delete lease.
	 *
	 * @param leaseId the lease id
	 */
	public void deleteLease(Long leaseId);
}
