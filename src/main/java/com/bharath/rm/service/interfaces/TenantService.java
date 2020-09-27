package com.bharath.rm.service.interfaces;

import java.util.List;

import com.bharath.rm.dto.TenantDTO;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.Tenant;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 25, 2020 5:50:18 PM
 	* Class Description
*/
public interface TenantService {
	public TenantDTO addTenant(Tenant tenant);
	public void addLease(Lease lease, String propertyType, Long propertyId);
	public TenantDTO updateTenant(Tenant tenant);
	public void deleteTenants(List<Long> tenantIds);
	public List<TenantDTO> getTenants(String searchQuery, Integer pageNo);
	public Integer getTenantsCount(String searchQuery);
	public TenantDTO getTenantInfo(Long tenantId);
}
