package com.bharath.rm.dao.interfaces;

import java.util.List;

import com.bharath.rm.constants.Constants.ContractStatus;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.Tenant;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 25, 2020 3:27:06 PM
 	* Class Description
*/
public interface TenantDAO {
	public Long addTenant(Tenant tenant);
	public Long addLease(Lease lease);
	public boolean tenantExists(Tenant tenant);
	public boolean tenantExists(Long userId, Long tenantId);
	public Tenant getTenantInfo(Long userId, Long tenantId);
	public void updateTenant(Tenant tenant);
	public void deleteTenants(Long userId, List<Long> tenantIds);
	public List<Tenant> getTenants(Long userId, String searchQuery, Integer pageNo);
	public Integer getTenantsCount(Long userId, String searchQuery);
	public Long getContractStatus(ContractStatus status);
	public void updateContractId(Long leaseId, String requestId);
	public Lease getLease(Long userId, Long leaseId);
	public Boolean leaseExist(Long userId, Long leaseId);
	public String getContractStatus(Long leaseId);
	public void updateLease(Lease lease);
	public List<Lease> getAllLeaseForTenant(Long userId, Long tenantId);
	public void deleteLease(Long leaseId);
}
