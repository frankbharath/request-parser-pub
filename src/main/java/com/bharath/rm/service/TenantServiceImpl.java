package com.bharath.rm.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.bharath.rm.common.DTOModelMapper;
import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.dao.interfaces.TenantDAO;
import com.bharath.rm.dto.LeaseDTO;
import com.bharath.rm.dto.TenantDTO;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.ApartmentPropertyDetails;
import com.bharath.rm.model.domain.ContractStatus;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.Property;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.model.domain.Tenant;
import com.bharath.rm.service.interfaces.TenantService;
import com.lowagie.text.DocumentException;


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
	
	private SpringTemplateEngine templateEngine;
	
	private ZohoServiceImp zohoServiceImp;
	
	@Autowired
	public TenantServiceImpl(TenantDAO tenantDAO, PropertyDAO propertyDAO, SpringTemplateEngine templateEngine, ZohoServiceImp zohoServiceImp) {
		this.tenantDAO=tenantDAO;
		this.propertyDAO=propertyDAO;
		this.templateEngine=templateEngine;
		this.zohoServiceImp=zohoServiceImp;
	}
	
	@Override
	public TenantDTO addTenant(Tenant tenant) {
		Long userId=Utils.getUserId();
		tenant.setTenantuserid(userId);
		if(tenantDAO.tenantExists(tenant)) {
			throw new APIRequestException(I18NConfig.getMessage("error.tenant_exists"));
		}
		Long tenantId=tenantDAO.addTenant(tenant);
		tenant.setTenantid(tenantId);
		return DTOModelMapper.tenantModelDTOMapper(tenant);
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
		return DTOModelMapper.tenantModelDTOMapper(tenant);
	}
	
	@Override
	public void deleteTenants(List<Long> tenantIds) {
		Long userId=Utils.getUserId();
		tenantDAO.deleteTenants(userId, tenantIds);
	}
	
	@Override
	public List<TenantDTO> getTenants(String searchQuery, Integer pageNo) {
		Long userId=Utils.getUserId();
		List<TenantDTO> tenantListDTO=new ArrayList<>();
		List<Tenant> tenantList=tenantDAO.getTenants(userId, searchQuery, pageNo);
		for(Tenant tenant:tenantList) {
			tenantListDTO.add(DTOModelMapper.tenantModelDTOMapper(tenant));
		}
		return tenantListDTO;
	}
	
	@Override
	public Integer getTenantsCount(String searchQuery) {
		Long userId=Utils.getUserId();
		return tenantDAO.getTenantsCount(userId, searchQuery);
	}
	
	@Override
	public TenantDTO getTenantInfo(Long tenantId) {
		Long userId=Utils.getUserId();
		Tenant tenant=tenantDAO.getTenantInfo(userId, tenantId);
		if(tenant==null) {
			throw new APIRequestException(I18NConfig.getMessage("error.tenant_does_not_exists"));
		}
		return DTOModelMapper.tenantModelDTOMapper(tenant);
	}
	
	@Override
	public LeaseDTO addLease(LeaseDTO leaseDTO, String propertyType, Long propertyId, Boolean contractRequired) {
		try {
			// convert DTO to domain model
			Lease lease=DTOModelMapper.leaseDTOModelMapper(leaseDTO);
			Long currentDate = Utils.parseDateToMilliseconds(new SimpleDateFormat("MMM d, yyyy").format(new Date()));
			
			if(lease.getMovein()<currentDate || Utils.diffDays(lease.getMovein(), lease.getMoveout())<Constants.MINDAYS) {
				throw new APIRequestException(I18NConfig.getMessage("error.invalid_lease_date"));
			}
			
			Long userId=Utils.getUserId();
			
			// check if property exists
			boolean exists=propertyDAO.propertyExists(userId, propertyId, propertyType, lease.getTenantspropertydetailid());
			if(!exists) {
				throw new APIRequestException(I18NConfig.getMessage("error.property.not_found"));
			}
			
			//check if tenant exists
			boolean tenantExists=tenantDAO.tenantExists(userId, lease.getLeasetenantid());
			if(!tenantExists) {
				throw new APIRequestException(I18NConfig.getMessage("error.tenant_does_not_exists"));
			}
			
			ContractStatus contractStatus=new ContractStatus();
			if(contractRequired) {
				Long statusid=tenantDAO.getContractStatus(Constants.ContractStatus.INITIATED);
				contractStatus.setContractstatusid(statusid);
			}else {
				Long statusid=tenantDAO.getContractStatus(Constants.ContractStatus.NOCONTRACT);
				contractStatus.setContractstatusid(statusid);
			}
			lease.setContractstatus(contractStatus);
			
			// check if there are accommodation for new tenants
			PropertyDetails details=propertyDAO.getPropertyDetails(lease.getTenantspropertydetailid());
			int totalOccupants=lease.getOccupants()+details.getOccupied();
			if(totalOccupants>details.getCapacity()) {
				throw new APIRequestException(I18NConfig.getMessage("error.property_full"));
			}
			lease.setRent(lease.getOccupants()*details.getRent());
			propertyDAO.updatePropertyOccupancy(lease.getTenantspropertydetailid(), totalOccupants);
			Long leaseId=tenantDAO.addLease(lease);
			lease.setLeaseid(leaseId);
			if(contractRequired) {
				updateContractStatus(userId, lease, propertyId, propertyType, details);
			}
			
			return DTOModelMapper.leaseModelDTOMapper(lease);
		} catch (ParseException e) {
			throw new APIRequestException(I18NConfig.getMessage("error.invalid_lease_date"));
		}
	}
	
	@Override
	public LeaseDTO updateLease(LeaseDTO leaseDTO, String propertyType, Long propertyId, Boolean contractRequired) {
		try {
			Lease lease = DTOModelMapper.leaseDTOModelMapper(leaseDTO);
			Long userId=Utils.getUserId();
			
			Long currentDate = Utils.parseDateToMilliseconds(new SimpleDateFormat("MMM d, yyyy").format(new Date()));
			// check if lease date is valid
			if(lease.getMovein()<currentDate || Utils.diffDays(lease.getMovein(), lease.getMoveout())<Constants.MINDAYS) {
				throw new APIRequestException(I18NConfig.getMessage("error.invalid_lease_date"));
			}
			
			// check if property exists
			boolean exists=propertyDAO.propertyExists(userId, propertyId, propertyType, leaseDTO.getTenantspropertydetailid());
			if(!exists) {
				throw new APIRequestException(I18NConfig.getMessage("error.property.not_found"));
			}
			
			//check if tenant exists
			boolean tenantExists=tenantDAO.tenantExists(userId, lease.getLeasetenantid());
			if(!tenantExists) {
				throw new APIRequestException(I18NConfig.getMessage("error.tenant_does_not_exists"));
			}
			
			// check if lease exists
			Lease oldLease=tenantDAO.getLease(userId, lease.getLeaseid());
			if(oldLease==null) {
				throw new APIRequestException(I18NConfig.getMessage("error.lease_does_not_exists"));
			}
			// check if the agreement is not initiated
			String status=tenantDAO.getContractStatus(lease.getLeaseid());
			if(!Constants.ContractStatus.NOCONTRACT.toString().equals(status)) {
				throw new APIRequestException(I18NConfig.getMessage("error.lease_contract_exists"));
			}
			
			if(contractRequired) {
				Long statusid=tenantDAO.getContractStatus(Constants.ContractStatus.INITIATED);
				ContractStatus contractStatus=new ContractStatus();
				contractStatus.setContractstatusid(statusid);
				lease.setContractstatus(contractStatus);
			}else {
				lease.setContractstatus(oldLease.getContractstatus());
			}
			
			// check if there are accommodation for new tenants
			PropertyDetails details=propertyDAO.getPropertyDetails(lease.getTenantspropertydetailid());
			int totalOccupants=lease.getOccupants()+details.getOccupied();
			if(oldLease.getTenantspropertydetailid()==lease.getTenantspropertydetailid()) {
				totalOccupants-=oldLease.getOccupants();
			}else {
				PropertyDetails oldDetails=propertyDAO.getPropertyDetails(oldLease.getTenantspropertydetailid());
				propertyDAO.updatePropertyOccupancy(oldLease.getTenantspropertydetailid(), oldDetails.getOccupied()-oldLease.getOccupants());
			}
			
			if(totalOccupants>details.getCapacity()) {
				throw new APIRequestException(I18NConfig.getMessage("error.property_full"));
			}
			
			lease.setRent(lease.getOccupants()*details.getRent());
			propertyDAO.updatePropertyOccupancy(lease.getTenantspropertydetailid(), totalOccupants);
			tenantDAO.updateLease(lease);
			if(contractRequired) {
				updateContractStatus(userId, lease, propertyId, propertyType, details);
			}
			return DTOModelMapper.leaseModelDTOMapper(lease);
		} catch (ParseException e) {
			throw new APIRequestException(I18NConfig.getMessage("error.invalid_lease_date"));
		}	
	}
	
	@Override
	public List<LeaseDTO> getAllLeasesForTenant(Long tenantId) {
		Long userId=Utils.getUserId();
		boolean tenantExists=tenantDAO.tenantExists(userId, tenantId);
		if(!tenantExists) {
			throw new APIRequestException(I18NConfig.getMessage("error.tenant_does_not_exists"));
		}
		List<Lease> leases=tenantDAO.getAllLeaseForTenant(userId, tenantId);
		List<LeaseDTO> leaseDTOs=new ArrayList<>();
		List<Long> propertyDetailsId=new ArrayList<>();
		for(Lease lease:leases) {
			leaseDTOs.add(DTOModelMapper.leaseModelDTOMapper(lease));
			propertyDetailsId.add(lease.getTenantspropertydetailid());
		}
		if(!propertyDetailsId.isEmpty()) {
			HashMap<Long, Property> hashMap=propertyDAO.getAllPropertiesForPropertyDetails(userId, propertyDetailsId);
			for(LeaseDTO leaseDTO:leaseDTOs) {
				if(hashMap.containsKey(leaseDTO.getTenantspropertydetailid())){
					Property property=hashMap.get(leaseDTO.getTenantspropertydetailid());
					leaseDTO.setPropertyid(property.getPropertyid());
					leaseDTO.setPropertyname(property.getPropertyname());
				}
			}
		}
		
		return leaseDTOs;
	}
	
	@Override
	public void updateContractStatus(Long userId, Lease lease, Long propertyId, String propertyType, PropertyDetails details) {
		Tenant tenant=tenantDAO.getTenantInfo(userId, lease.getLeasetenantid());
		Property property=propertyDAO.getProperty(userId, propertyId);
		Address address=new Address();
		address.setAddressline_1(property.getAddressline_1());
		address.setAddressline_2(property.getAddressline_2());
		address.setCity(property.getCity());
		address.setPostal(property.getPostal());
		if(Constants.PropertyType.APPARTMENT.toString().equals(propertyType)) {
			ApartmentPropertyDetails unit=propertyDAO.getApartmentUnit(propertyId, lease.getTenantspropertydetailid());
			String doorFloor="#"+unit.getDoorno()+" , Floor no "+unit.getFloorno()+", "+address.getAddressline_1();
			address.setAddressline_1(doorFloor);
		}
		String contractId=zohoServiceImp.createSignRequest(tenant, lease, address, details, 0);
		if(contractId==null) {
			throw new APIRequestException(I18NConfig.getMessage("error.unkown_issue"));
		}else {
			tenantDAO.updateContractId(lease.getLeaseid(), contractId);
		}
	}
	
	@Override
	public void deleteLease(Long leaseId) {
		Long userId=Utils.getUserId();
		// check if lease exists
		Lease lease=tenantDAO.getLease(userId, leaseId);
		if(lease!=null) {
			PropertyDetails details=propertyDAO.getPropertyDetails(lease.getTenantspropertydetailid());
			propertyDAO.updatePropertyOccupancy(lease.getTenantspropertydetailid(), details.getOccupied()-lease.getOccupants());
			tenantDAO.deleteLease(leaseId);
		}
	}
	
	@Override
	public void check() {
		String html = this.templateEngine.process("rentalagreement", new Context());

		String outputFolder = "thymeleaf.pdf";
	    OutputStream outputStream=null;
		try {
			outputStream = new FileOutputStream(outputFolder);
			ITextRenderer renderer = new ITextRenderer();
		    renderer.setDocumentFromString(html);
		    renderer.layout();
		    renderer.createPDF(outputStream);
		 
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(outputStream!=null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	    
		
	}
}
