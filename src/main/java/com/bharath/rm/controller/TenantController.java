package com.bharath.rm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.dto.APIRequestResponse;
import com.bharath.rm.dto.LeaseDTO;
import com.bharath.rm.model.domain.Tenant;
import com.bharath.rm.service.interfaces.TenantService;

/**
 * The Class TenantController.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 25, 2020 8:04:49 PM
 * Tenant controller provides end point to all tenant and lease related operations
 */

@Controller
@RequestMapping("/api/tenant")
public class TenantController {
	
	/** The tenant service. */
	TenantService tenantService;
	
	/**
	 * Inits the binder.
	 *
	 * @param binder the binder
	 */
	@InitBinder
    public void initBinder(WebDataBinder binder) 
    {
      binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	/**
	 * Instantiates a new tenant controller.
	 *
	 * @param tenantService the tenant service
	 */
	public TenantController(TenantService tenantService) {
		this.tenantService=tenantService;
	}
	
	/**
	 * Adds the tenant.
	 *
	 * @param tenant the tenant
	 * @return the response entity
	 */
	@PostMapping
	public ResponseEntity<Object> addTenant(@ModelAttribute Tenant tenant) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.tenant.added_success"), tenantService.addTenant(tenant));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Updates the tenant.
	 *
	 * @param tenant the tenant
	 * @return the response entity
	 */
	@PutMapping
	public ResponseEntity<Object> updateTenant(@ModelAttribute Tenant tenant) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.tenant.updated_success"), tenantService.updateTenant(tenant));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Deletes the tenant.
	 *
	 * @param tenantIds the tenant ids
	 * @return the response entity
	 */
	@DeleteMapping
	public ResponseEntity<Object> deleteTenant(@RequestParam List<Long> tenantIds) {
		tenantService.deleteTenants(tenantIds);
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.tenant.deleted_success"), "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Gets all the tenants.
	 *
	 * @param searchQuery the search query
	 * @param pageNo the page no
	 * @param countRequired the count required
	 * @return the tenants
	 */
	@GetMapping
	public ResponseEntity<Object> getTenants(@RequestParam Optional<String> searchQuery, @RequestParam Optional<Integer> pageNo, @RequestParam Optional<Boolean> countRequired) {
		Map<String, Object> map=new HashMap<>();
		map.put("tenantlist", tenantService.getTenants(searchQuery.orElse(null), pageNo.orElse(null)));
		if(countRequired.orElse(false)) {
			map.put("count", tenantService.getTenantsCount(searchQuery.orElse(null)));
		}
		APIRequestResponse response=Utils.getApiRequestResponse("", map);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Gets the tenant details.
	 *
	 * @param tenantId the tenant id
	 * @return the tenant details
	 */
	@GetMapping(value="/{tenantId}")
	public ResponseEntity<Object> getTenantDetails(@PathVariable Long tenantId) {
		APIRequestResponse response=Utils.getApiRequestResponse("", tenantService.getTenantInfo(tenantId));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * Creates the lease.
	 *
	 * @param leaseDTO the lease DTO
	 * @param propertyType the property type
	 * @param propertyId the property id
	 * @param contractReq the contract req
	 * @return the response entity
	 */
	@PostMapping(value="/lease")
	@ResponseBody
	public ResponseEntity<Object> createLease(LeaseDTO leaseDTO, @RequestParam String propertyType, @RequestParam Long propertyId, Optional<Boolean> contractReq) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.tenant.lease_added_success"), tenantService.addLease(leaseDTO, propertyType, propertyId, contractReq.orElse(false)));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Updates the lease.
	 *
	 * @param leaseDTO the lease DTO
	 * @param propertyType the property type
	 * @param propertyId the property id
	 * @param contractReq the contract req
	 * @return the response entity
	 */
	@PutMapping(value="/lease")
	@ResponseBody
	public ResponseEntity<Object> updateLease(LeaseDTO leaseDTO, @RequestParam String propertyType, @RequestParam Long propertyId, Optional<Boolean> contractReq) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.tenant.lease_updated_success"), tenantService.updateLease(leaseDTO, propertyType, propertyId, contractReq.orElse(false)));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Gets the all leases.
	 *
	 * @param tenantId the tenant id
	 * @return the all leases
	 */
	@GetMapping(value="/lease/{tenantId}")
	@ResponseBody
	public ResponseEntity<Object> getAllLeases(@PathVariable Long tenantId){
		APIRequestResponse response=Utils.getApiRequestResponse("", tenantService.getAllLeasesForTenant(tenantId));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Deletes the lease.
	 *
	 * @param leaseId the lease id
	 * @return the response entity
	 */
	@DeleteMapping(value="/lease/{leaseId}")
	@ResponseBody
	public ResponseEntity<Object> deleteLease(@PathVariable Long leaseId){
		tenantService.deleteLease(leaseId);
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.tenant.lease_deleted_success"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
