package com.bharath.rm.controller;

import java.lang.StackWalker.Option;
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

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.dto.APIRequestResponse;
import com.bharath.rm.model.domain.Tenant;
import com.bharath.rm.service.interfaces.TenantService;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 25, 2020 8:04:49 PM
 	* Class Description
*/

@Controller
@RequestMapping("/api/tenant")
public class TenantController {
	
	TenantService tenantService;
	
	@InitBinder
    public void initBinder(WebDataBinder binder) 
    {
      binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	public TenantController(TenantService tenantService) {
		this.tenantService=tenantService;
	}
	
	@PostMapping
	public ResponseEntity<Object> addTenant(@ModelAttribute Tenant tenant) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.tenant.added_success"), tenantService.addTenant(tenant));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<Object> updateTenant(@ModelAttribute Tenant tenant) {
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.tenant.updated_success"), tenantService.updateTenant(tenant));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping
	public ResponseEntity<Object> deleteTenant(@RequestParam List<Long> tenantIds) {
		tenantService.deleteTenants(tenantIds);
		APIRequestResponse response=Utils.getApiRequestResponse(I18NConfig.getMessage("success.tenant.deleted_success"), "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
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
	
	@GetMapping(value="/{tenantId}")
	public ResponseEntity<Object> getTenantDetails(@PathVariable Long tenantId) {
		APIRequestResponse response=Utils.getApiRequestResponse("", tenantService.getTenantInfo(tenantId));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
