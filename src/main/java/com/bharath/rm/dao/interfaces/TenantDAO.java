package com.bharath.rm.dao.interfaces;

import java.util.List;

import com.bharath.rm.dto.TenantDTO;
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
	public TenantDTO getTenantInfo(Long userId, Long tenantId);
	public void updateTenant(Tenant tenant);
	public void deleteTenants(Long userId, List<Long> tenantIds);
	public List<TenantDTO> getTenants(Long userId, String searchQuery, Integer pageNo);
	public Integer getTenantsCount(Long userId, String searchQuery);
}
