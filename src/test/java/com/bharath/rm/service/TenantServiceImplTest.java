package com.bharath.rm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.bharath.rm.common.DTOModelMapper;
import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.configuration.SpringConfig;
import com.bharath.rm.configuration.ThymeleafTemplateConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.dao.interfaces.TenantDAO;
import com.bharath.rm.dto.LeaseDTO;
import com.bharath.rm.dto.TenantDTO;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.model.domain.Tenant;
import com.bharath.rm.service.interfaces.TenantService;

/**
 * The Class TenantServiceImplTest.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Oct 17, 2020 1:12:07 PM
 * This class perform unit test for important methods in TenantService class.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {SpringConfig.class, ThymeleafTemplateConfig.class})
class TenantServiceImplTest extends AbstractTest {

	/** The tenant service. */
	@Autowired
	TenantService tenantService;
	
	/** The property DAO. */
	@MockBean
	PropertyDAO propertyDAO;
	
	/** The tenant DAO. */
	@MockBean
	TenantDAO tenantDAO;
	
	/** The zoho service imp. */
	@MockBean
	ZohoServiceImp zohoServiceImp;
	
	/** The dto model mapper. */
	@MockBean
	DTOModelMapper dtoModelMapper;
	
	/** The platform. */
	@MockBean
	PlatformTransactionManager platform;
	
	/**
	 * Test if tenant exists.
	 */
	@Test
	public void testifTenantExists() {
		Tenant tenant=mock(Tenant.class);
		when(tenantDAO.tenantExists(tenant)).thenReturn(true);
		Exception exception = assertThrows(APIRequestException.class, () -> {
			tenantService.addTenant(tenant);
		});
		assertEquals(I18NConfig.getMessage("error.tenant_exists"), exception.getMessage());
	}
	
	/**
	 * Test add tenant.
	 */
	@Test
	public void testAddTenant() {
		Tenant tenant=mock(Tenant.class);
		when(tenantDAO.tenantExists(tenant)).thenReturn(false);
		when(dtoModelMapper.tenantModelDTOMapper(tenant)).thenReturn(mock(TenantDTO.class));
		assertNotNull(tenantService.addTenant(tenant));
	}
	
	/**
	 * Test if tenant exists for update.
	 */
	@Test
	public void testIfTenantExistsForUpdate() {
		Tenant tenant=mock(Tenant.class);
		when(tenantDAO.tenantExists(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
		Exception exception = assertThrows(APIRequestException.class, () -> {
			tenantService.updateTenant(tenant);
		});
		assertEquals(I18NConfig.getMessage("error.tenant_does_not_exists"), exception.getMessage());
	}

	/**
	 * Test update tenant.
	 */
	@Test
	public void testUpdateTenant() {
		Tenant tenant=mock(Tenant.class);
		when(tenantDAO.tenantExists(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
		when(tenantDAO.tenantExists(Mockito.any(Tenant.class))).thenReturn(false);
		when(dtoModelMapper.tenantModelDTOMapper(tenant)).thenReturn(mock(TenantDTO.class));
		assertNotNull(tenantService.addTenant(tenant));
	}
	
	/**
	 * Test get tenants.
	 */
	@Test
	public void testGetTenants() {
		List<Tenant> list=new ArrayList<>();
		list.add(mock(Tenant.class));
		list.add(mock(Tenant.class));
		
		when(tenantDAO.getTenants(TenantServiceImplTest.testUserId, null, null)).thenReturn(list);
		
		List<TenantDTO> respDTOs=tenantService.getTenants(null, null);
		assertEquals(2, respDTOs.size());
	}
	
	/**
	 * Test for invalid lease period.
	 */
	@Test
	public void testForInvalidLeasePeriod() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		try {
			
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			when(lease.getMovein()).thenReturn(System.currentTimeMillis());
			when(lease.getMoveout()).thenReturn(System.currentTimeMillis());
			Exception exception = assertThrows(APIRequestException.class, () -> {
				tenantService.addLease(leaseDTO,null, null, null);
			});
			assertEquals(I18NConfig.getMessage("error.invalid_lease_date"), exception.getMessage());
		} catch (ParseException e) {}
	}
	
	/**
	 * Test if property exists for leasing.
	 */
	@Test
	public void testIfPropertyExistsForLeasing() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		try {
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			
			Long timeStamp=System.currentTimeMillis();
			when(lease.getMovein()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(1));
			when(lease.getMoveout()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(Constants.MINDAYS+1));
			when(lease.getTenantspropertydetailid()).thenReturn(null);
			when(propertyDAO.propertyExists(TenantServiceImplTest.testUserId, null, null, null))
			.thenReturn(false);
			
			Exception exception = assertThrows(APIRequestException.class, () -> {
				tenantService.addLease(leaseDTO, null, null, null);
			});
			assertEquals(I18NConfig.getMessage("error.property.not_found"), exception.getMessage());
		} catch (ParseException e) {}
	}
	
	/**
	 * Test if tenant exists for leasing.
	 */
	@Test
	public void testIfTenantExistsForLeasing() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		try {
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			
			Long timeStamp=System.currentTimeMillis();
			when(lease.getMovein()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(1));
			when(lease.getMoveout()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(Constants.MINDAYS+1));
			when(lease.getTenantspropertydetailid()).thenReturn(null);
			when(lease.getLeasetenantid()).thenReturn(null);
			when(propertyDAO.propertyExists(TenantServiceImplTest.testUserId, null, null, null))
			.thenReturn(true);
			when(tenantDAO.tenantExists(TenantServiceImplTest.testUserId, null)).thenReturn(false);
			
			Exception exception = assertThrows(APIRequestException.class, () -> {
				tenantService.addLease(leaseDTO, null, null, null);
			});
			assertEquals(I18NConfig.getMessage("error.tenant_does_not_exists"), exception.getMessage());
		} catch (ParseException e) {}
	}
	
	/**
	 * Test if property is full.
	 */
	@Test
	public void testIfPropertyIsFull() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		PropertyDetails details=mock(PropertyDetails.class);
		try {
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			
			Long timeStamp=System.currentTimeMillis();
			when(lease.getMovein()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(1));
			when(lease.getMoveout()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(Constants.MINDAYS+1));
			when(lease.getTenantspropertydetailid()).thenReturn(null);
			when(lease.getLeasetenantid()).thenReturn(null);
			when(lease.getOccupants()).thenReturn(3);
			when(details.getOccupied()).thenReturn(3);
			when(details.getCapacity()).thenReturn(5);
			when(propertyDAO.propertyExists(TenantServiceImplTest.testUserId, null, null, null))
			.thenReturn(true);
			when(tenantDAO.tenantExists(TenantServiceImplTest.testUserId, null)).thenReturn(true);
			when(propertyDAO.getPropertyDetails(lease.getTenantspropertydetailid())).thenReturn(details);
			
			
			Exception exception = assertThrows(APIRequestException.class, () -> {
				tenantService.addLease(leaseDTO, null, null, false);
			});
			assertEquals(I18NConfig.getMessage("error.property_full"), exception.getMessage());
		} catch (ParseException e) {}
	}
	
	/**
	 * Test add lease.
	 */
	@Test
	public void testAddLease() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		PropertyDetails details=mock(PropertyDetails.class);
		try {
			Long timeStamp=System.currentTimeMillis();
			
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			when(lease.getMovein()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(1));
			when(lease.getMoveout()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(Constants.MINDAYS+1));
			when(lease.getTenantspropertydetailid()).thenReturn(null);
			when(lease.getLeasetenantid()).thenReturn(null);
			when(lease.getLeaseid()).thenReturn(1l);
			when(lease.getOccupants()).thenReturn(3);
			when(details.getOccupied()).thenReturn(3);
			when(details.getCapacity()).thenReturn(6);
			when(propertyDAO.propertyExists(TenantServiceImplTest.testUserId, null, null, null))
			.thenReturn(true);
			when(tenantDAO.tenantExists(TenantServiceImplTest.testUserId, null)).thenReturn(true);
			when(propertyDAO.getPropertyDetails(lease.getTenantspropertydetailid())).thenReturn(details);
			when(tenantDAO.addLease(lease)).thenReturn(1l);
			when(dtoModelMapper.leaseModelDTOMapper(lease)).thenReturn(leaseDTO);
			
			assertNotNull(tenantService.addLease(leaseDTO, null, null, false));
			
		} catch (ParseException e) {}
	}
	
	/**
	 * Test update lease for invalid lease period.
	 */
	@Test
	public void testUpdateLeaseForInvalidLeasePeriod() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		try {
			
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			when(lease.getMovein()).thenReturn(System.currentTimeMillis());
			when(lease.getMoveout()).thenReturn(System.currentTimeMillis());
			Exception exception = assertThrows(APIRequestException.class, () -> {
				tenantService.updateLease(leaseDTO,null, null, null);
			});
			assertEquals(I18NConfig.getMessage("error.invalid_lease_date"), exception.getMessage());
		} catch (ParseException e) {}
	}
	
	/**
	 * Test update lease if property exists for leasing.
	 */
	@Test
	public void testUpdateLeaseIfPropertyExistsForLeasing() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		try {
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			
			Long timeStamp=System.currentTimeMillis();
			when(lease.getMovein()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(1));
			when(lease.getMoveout()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(Constants.MINDAYS+1));
			when(lease.getTenantspropertydetailid()).thenReturn(null);
			when(propertyDAO.propertyExists(TenantServiceImplTest.testUserId, null, null, null))
			.thenReturn(false);
			
			Exception exception = assertThrows(APIRequestException.class, () -> {
				tenantService.updateLease(leaseDTO, null, null, null);
			});
			assertEquals(I18NConfig.getMessage("error.property.not_found"), exception.getMessage());
		} catch (ParseException e) {}
	}
	
	/**
	 * Test update lease if tenant exists.
	 */
	@Test
	public void testUpdateLeaseIfTenantExists() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		try {
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			
			Long timeStamp=System.currentTimeMillis();
			when(lease.getMovein()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(1));
			when(lease.getMoveout()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(Constants.MINDAYS+1));
			when(lease.getTenantspropertydetailid()).thenReturn(null);
			when(propertyDAO.propertyExists(TenantServiceImplTest.testUserId, null, null, null))
			.thenReturn(true);
			when(tenantDAO.tenantExists(TenantServiceImplTest.testUserId, null)).thenReturn(false);
			
			Exception exception = assertThrows(APIRequestException.class, () -> {
				tenantService.updateLease(leaseDTO, null, null, null);
			});
			assertEquals(I18NConfig.getMessage("error.tenant_does_not_exists"), exception.getMessage());
		} catch (ParseException e) {}
	}
	
	/**
	 * Test update lease if lease exists.
	 */
	@Test
	public void testUpdateLeaseIfLeaseExists() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		try {
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			
			Long timeStamp=System.currentTimeMillis();
			when(lease.getMovein()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(1));
			when(lease.getMoveout()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(Constants.MINDAYS+1));
			when(lease.getTenantspropertydetailid()).thenReturn(null);
			when(lease.getLeasetenantid()).thenReturn(null);
			when(lease.getLeaseid()).thenReturn(null);
			when(propertyDAO.propertyExists(TenantServiceImplTest.testUserId, null, null, null))
			.thenReturn(true);
			when(tenantDAO.tenantExists(TenantServiceImplTest.testUserId, null)).thenReturn(true);
			when(tenantDAO.getLease(TenantServiceImplTest.testUserId, null)).thenReturn(null);
			
			Exception exception = assertThrows(APIRequestException.class, () -> {
				tenantService.updateLease(leaseDTO, null, null, null);
			});
			assertEquals(I18NConfig.getMessage("error.lease_does_not_exists"), exception.getMessage());
		} catch (ParseException e) {}
	}
	
	/**
	 * Test update lease if property full.
	 */
	@Test
	public void testUpdateLeaseIfPropertyFull() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		PropertyDetails details=mock(PropertyDetails.class);
		try {
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			
			Long timeStamp=System.currentTimeMillis();
			when(lease.getMovein()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(1));
			when(lease.getMoveout()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(Constants.MINDAYS+1));
			when(lease.getTenantspropertydetailid()).thenReturn(null);
			when(lease.getLeasetenantid()).thenReturn(null);
			when(lease.getLeaseid()).thenReturn(null);
			when(propertyDAO.propertyExists(TenantServiceImplTest.testUserId, null, null, null))
			.thenReturn(true);
			when(tenantDAO.tenantExists(TenantServiceImplTest.testUserId, null)).thenReturn(true);
			when(tenantDAO.getLease(TenantServiceImplTest.testUserId, null)).thenReturn(lease);
			when(tenantDAO.getContractStatus(lease.getLeaseid())).thenReturn(Constants.ContractStatus.NOCONTRACT.toString());
			when(lease.getOccupants()).thenReturn(3);
			when(details.getOccupied()).thenReturn(6);
			when(details.getCapacity()).thenReturn(5);
			when(propertyDAO.getPropertyDetails(null)).thenReturn(details);
			
			Exception exception = assertThrows(APIRequestException.class, () -> {
				tenantService.updateLease(leaseDTO, null, null, false);
			});
			assertEquals(I18NConfig.getMessage("error.property_full"), exception.getMessage());
		} catch (ParseException e) {}
	}
	
	/**
	 * Test update lease.
	 */
	@Test
	public void testUpdateLease() {
		Lease lease=mock(Lease.class);
		LeaseDTO leaseDTO=mock(LeaseDTO.class);
		PropertyDetails details=mock(PropertyDetails.class);
		try {
			when(dtoModelMapper.leaseDTOModelMapper(leaseDTO)).thenReturn(lease);
			
			Long timeStamp=System.currentTimeMillis();
			when(lease.getMovein()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(1));
			when(lease.getMoveout()).thenReturn(timeStamp+Utils.convertDaysToMilliseconds(Constants.MINDAYS+1));
			when(lease.getTenantspropertydetailid()).thenReturn(null);
			when(lease.getLeasetenantid()).thenReturn(null);
			when(lease.getLeaseid()).thenReturn(null);
			when(propertyDAO.propertyExists(TenantServiceImplTest.testUserId, null, null, null))
			.thenReturn(true);
			when(tenantDAO.tenantExists(TenantServiceImplTest.testUserId, null)).thenReturn(true);
			when(tenantDAO.getLease(TenantServiceImplTest.testUserId, null)).thenReturn(lease);
			when(tenantDAO.getContractStatus(lease.getLeaseid())).thenReturn(Constants.ContractStatus.NOCONTRACT.toString());
			when(lease.getOccupants()).thenReturn(3);
			when(details.getOccupied()).thenReturn(3);
			when(details.getCapacity()).thenReturn(5);
			when(propertyDAO.getPropertyDetails(null)).thenReturn(details);
			
			when(dtoModelMapper.leaseModelDTOMapper(lease)).thenReturn(leaseDTO);
			
			assertNotNull(tenantService.updateLease(leaseDTO, null, null, false));
		} catch (ParseException e) {}
	}
}
