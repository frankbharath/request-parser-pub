package com.bharath.rm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.dao.interfaces.TenantDAO;
import com.bharath.rm.dto.PropertyDetailsDTO;
import com.bharath.rm.dto.TenantDTO;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.Tenant;
import com.bharath.rm.service.interfaces.TenantService;


/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 25, 2020 5:50:45 PM
 	* Class Description
*/

@Service
@Transactional(rollbackFor = Exception.class)
public class TenantServiceImpl implements TenantService {
	
	private TenantDAO tenantDAO;
	
	private PropertyDAO propertyDAO;
	
	@Autowired
	public TenantServiceImpl(TenantDAO tenantDAO, PropertyDAO propertyDAO) {
		this.tenantDAO=tenantDAO;
		this.propertyDAO=propertyDAO;
	}
	
	@Override
	public TenantDTO addTenant(Tenant tenant) {
		Long userId=Utils.getUserId();
		tenant.setTenantuserid(userId);
		if(tenantDAO.tenantExists(tenant)) {
			throw new APIRequestException(I18NConfig.getMessage("error.tenant_exists"));
		}
		Long tenantId=tenantDAO.addTenant(tenant);
		return tenantDAO.getTenantInfo(userId, tenantId);
	}
	
	@Override
	public TenantDTO updateTenant(Tenant tenant) {
		Long userId=Utils.getUserId();
		tenant.setTenantuserid(userId);
		if(!tenantDAO.tenantExists(userId, tenant.getTenantid())) {
			throw new APIRequestException(I18NConfig.getMessage("error.tenant_does_not_exists"));
		}
		if(tenantDAO.tenantExists(tenant)) {
			throw new APIRequestException(I18NConfig.getMessage("error.tenant_exists"));
		}
		tenantDAO.updateTenant(tenant);
		return tenantDAO.getTenantInfo(userId, tenant.getTenantid());
	}
	
	@Override
	public void deleteTenants(List<Long> tenantIds) {
		Long userId=Utils.getUserId();
		tenantDAO.deleteTenants(userId, tenantIds);
	}
	
	@Override
	public List<TenantDTO> getTenants(String searchQuery, Integer pageNo) {
		Long userId=Utils.getUserId();
		return tenantDAO.getTenants(userId, searchQuery, pageNo);
	}
	
	@Override
	public Integer getTenantsCount(String searchQuery) {
		Long userId=Utils.getUserId();
		return tenantDAO.getTenantsCount(userId, searchQuery);
	}
	
	@Override
	public void addLease(Lease lease, String propertyType, Long propertyId) {
		Long userId=Utils.getUserId();
		boolean exists=propertyDAO.propertyExists(userId, propertyId, propertyType, lease.getTenantspropertydetailid());
		if(!exists) {
			throw new APIRequestException(I18NConfig.getMessage("error.property.not_found"));
		}
		PropertyDetailsDTO detailsDTO=propertyDAO.getPropertyDetails(lease.getTenantspropertydetailid());
		int totalOccupants=lease.getOccupants()+detailsDTO.getOccupied();
		if(totalOccupants>detailsDTO.getCapacity()) {
			throw new APIRequestException(I18NConfig.getMessage("error.property_full"));
		}
		propertyDAO.updatePropertyOccupancy(lease.getTenantspropertydetailid(), totalOccupants);
		tenantDAO.addLease(lease);
	}
	
	@Override
	public TenantDTO getTenantInfo(Long tenantId) {
		Long userId=Utils.getUserId();
		TenantDTO tenantDTO=tenantDAO.getTenantInfo(userId, tenantId);
		if(tenantDTO==null) {
			throw new APIRequestException(I18NConfig.getMessage("error.tenant_does_not_exists"));
		}
		return tenantDTO;
	}
}
