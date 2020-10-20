package com.bharath.rm.service.interfaces;

import java.util.List;

import com.bharath.rm.dto.LeaseDTO;
import com.bharath.rm.dto.TenantDTO;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.model.domain.Tenant;

/**
 * The Interface TenantService.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 25, 2020 5:50:18 PM
 */

public interface TenantService {
	
	/**
	 * Adds the tenant.
	 *
	 * @param tenant the tenant
	 * @return the tenant DTO
	 */
	public TenantDTO addTenant(Tenant tenant);
	
	/**
	 * Update tenant.
	 *
	 * @param tenant the tenant
	 * @return the tenant DTO
	 */
	public TenantDTO updateTenant(Tenant tenant);
	
	/**
	 * Delete tenants.
	 *
	 * @param tenantIds the tenant ids
	 */
	public void deleteTenants(List<Long> tenantIds);
	
	/**
	 * Gets the tenants.
	 *
	 * @param searchQuery the search query
	 * @param pageNo the page no
	 * @return the tenants
	 */
	public List<TenantDTO> getTenants(String searchQuery, Integer pageNo);
	
	/**
	 * Gets the tenants count.
	 *
	 * @param searchQuery the search query
	 * @return the tenants count
	 */
	public Integer getTenantsCount(String searchQuery);
	
	/**
	 * Gets the tenant info.
	 *
	 * @param tenantId the tenant id
	 * @return the tenant info
	 */
	public TenantDTO getTenantInfo(Long tenantId);
	
	/**
	 * Adds the lease.
	 *
	 * @param lease the lease
	 * @param propertyType the property type
	 * @param propertyId the property id
	 * @param contactRequired the contact required
	 * @return the lease DTO
	 */
	public LeaseDTO addLease(LeaseDTO lease, String propertyType, Long propertyId, Boolean contactRequired);
	
	/**
	 * Update lease.
	 *
	 * @param leaseDTO the lease DTO
	 * @param propertyType the property type
	 * @param propertyId the property id
	 * @param contractRequired the contract required
	 * @return the lease DTO
	 */
	public LeaseDTO updateLease(LeaseDTO leaseDTO, String propertyType, Long propertyId, Boolean contractRequired);
	
	/**
	 * Gets the all leases for tenant.
	 *
	 * @param tenantId the tenant id
	 * @return the all leases for tenant
	 */
	public List<LeaseDTO> getAllLeasesForTenant(Long tenantId);
	
	/**
	 * Update contract status.
	 *
	 * @param userId the user id
	 * @param lease the lease
	 * @param propertyId the property id
	 * @param propertyType the property type
	 * @param details the details
	 */
	public void updateContractStatus(Long userId, Lease lease, Long propertyId, String propertyType, PropertyDetails details);
	
	/**
	 * Delete lease.
	 *
	 * @param leaseId the lease id
	 */
	public void deleteLease(Long leaseId);
}
