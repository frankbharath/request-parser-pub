package com.bharath.rm.common;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.ApartmentPropertyDetailDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.LeaseDTO;
import com.bharath.rm.dto.PropertyDTO;
import com.bharath.rm.dto.PropertyDetailsDTO;
import com.bharath.rm.dto.TenantDTO;
import com.bharath.rm.dto.UserDTO;
import com.bharath.rm.model.domain.Apartment;
import com.bharath.rm.model.domain.ApartmentPropertyDetails;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.Property;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.model.domain.Tenant;
import com.bharath.rm.model.domain.User;

/**
 * The Class DTOModelMapper.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 29, 2020 5:44:38 PM
 * This class converts domain model to DTO and vice versa.
 */

public class DTOModelMapper {
	
	/**
	 * Converts user domain model to user DTO.
	 *
	 * @param user the user
	 * @return the user DTO
	 */
	public UserDTO userModelDTOMapper(User user) {
		UserDTO userDTO=new UserDTO();
		userDTO.setEmail(user.getEmail());
		userDTO.setUserid(user.getUserid());
		userDTO.setUsertype(user.getUsertype().getType());
		userDTO.setCreationtime(Utils.getDate(user.getCreationtime()));
		return userDTO;
	}
	
	/**
	 * Convert property domain model to property DTO
	 *
	 * @param property the property
	 * @return the property DTO
	 */
	public PropertyDTO propertyModelDTOMapper(Property property) {
		PropertyDTO propertyDTO=new PropertyDTO();
		propertyDTO.setUserid(property.getUserid());
		propertyDTO.setPropertyid(property.getPropertyid());
		propertyDTO.setPropertyname(property.getPropertyname());
		propertyDTO.setPropertytype(property.getPropertytype());
		propertyDTO.setAddressline_1(property.getAddressline_1());
		propertyDTO.setAddressline_2(property.getAddressline_2());
		propertyDTO.setPostal(property.getPostal());
		propertyDTO.setCity(property.getCity());
		propertyDTO.setCreationtime(Utils.getDate(property.getCreationtime()));
		return propertyDTO;
	}
	
	/**
	 * Converts house DTO to house domain model
	 *
	 * @param house the house
	 * @return the house DTO
	 */
	public HouseDTO houseModelDTOMapper(House house) {
		HouseDTO houseDTO=new HouseDTO();
		houseDTO.setUserid(house.getUserid());
		houseDTO.setPropertyid(house.getPropertyid());
		houseDTO.setPropertyname(house.getPropertyname());
		houseDTO.setPropertytype(house.getPropertytype());
		houseDTO.setAddressline_1(house.getAddressline_1());
		houseDTO.setAddressline_2(house.getAddressline_2());
		houseDTO.setPostal(house.getPostal());
		houseDTO.setCity(house.getCity());
		houseDTO.setCreationtime(Utils.getDate(house.getCreationtime()));
		
		PropertyDetails details=house.getPropertydetails();
		PropertyDetailsDTO detailsDTO=new PropertyDetailsDTO();
		detailsDTO.setPropertyid(details.getPropertyid());
		detailsDTO.setPropertydetailsid(details.getPropertydetailsid());
		detailsDTO.setArea(details.getArea());
		detailsDTO.setCapacity(details.getCapacity());
		detailsDTO.setRent(details.getRent());
		detailsDTO.setOccupied(details.getOccupied());
		houseDTO.setDetailsDTO(detailsDTO);

		return houseDTO;
	}
	
	/**
	 * Converts house domain model to house DTO
	 *
	 * @param houseDTO the house DTO
	 * @return the house
	 */
	public House houseDTOModelMapper(HouseDTO houseDTO) {
		
		House house=new House();
		house.setUserid(Utils.getUserId());
		house.setPropertyid(houseDTO.getPropertyid());
		house.setPropertyname(houseDTO.getPropertyname());
		house.setPropertytype(houseDTO.getPropertytype());
		house.setAddressline_1(houseDTO.getAddressline_1());
		house.setAddressline_2(houseDTO.getAddressline_2());
		house.setPostal(houseDTO.getPostal());
		house.setCity(houseDTO.getCity());
		house.setCreationtime(System.currentTimeMillis());
		
		PropertyDetails details=new PropertyDetails();
		PropertyDetailsDTO detailsDTO=houseDTO.getDetailsDTO();
		details.setPropertyid(detailsDTO.getPropertyid());
		details.setPropertydetailsid(detailsDTO.getPropertydetailsid());
		details.setArea(detailsDTO.getArea());
		details.setCapacity(detailsDTO.getCapacity());
		details.setRent(detailsDTO.getRent());
		details.setOccupied(detailsDTO.getOccupied());
		
		house.setPropertydetails(details);
		return house;
		
	}

	/**
	 * Converts apartment DTO to apartment domain model
	 *
	 * @param apartmentDTO the apartment DTO
	 * @return the apartment
	 */
	public Apartment apartmentDTOModelMapper(ApartmentDTO apartmentDTO) {
		Apartment apartment=new Apartment();
		apartment.setUserid(Utils.getUserId());
		apartment.setPropertyid(apartmentDTO.getPropertyid());
		apartment.setPropertyname(apartmentDTO.getPropertyname());
		apartment.setPropertytype(apartmentDTO.getPropertytype());
		apartment.setAddressline_1(apartmentDTO.getAddressline_1());
		apartment.setAddressline_2(apartmentDTO.getAddressline_2());
		apartment.setPostal(apartmentDTO.getPostal());
		apartment.setCity(apartmentDTO.getCity());
		apartment.setCreationtime(System.currentTimeMillis());
		
		List<ApartmentPropertyDetails> appartmentPropertyDetails=new ArrayList<>();
		List<ApartmentPropertyDetailDTO> apartmentPropertyDetailDTOs=apartmentDTO.getUnits(); 
		for(ApartmentPropertyDetailDTO details:apartmentPropertyDetailDTOs) {
			appartmentPropertyDetails.add(appartmentPropertyDetails(details));
		}
		apartment.setUnits(appartmentPropertyDetails);
		return apartment;
	}
	
	/**
	 * Converts apartment domain model to apartment DTO
	 *
	 * @param apartment the apartment
	 * @return the apartment DTO
	 */
	public ApartmentDTO apartmentModelDTOMapper(Apartment apartment) {
		ApartmentDTO apartmentDTO=new ApartmentDTO();
		apartmentDTO.setUserid(apartment.getUserid());
		apartmentDTO.setPropertyid(apartment.getPropertyid());
		apartmentDTO.setPropertyname(apartment.getPropertyname());
		apartmentDTO.setPropertytype(apartment.getPropertytype());
		apartmentDTO.setAddressline_1(apartment.getAddressline_1());
		apartmentDTO.setAddressline_2(apartment.getAddressline_2());
		apartmentDTO.setPostal(apartment.getPostal());
		apartmentDTO.setCity(apartment.getCity());
		apartmentDTO.setCreationtime(Utils.getDate(apartment.getCreationtime()));
		
		List<ApartmentPropertyDetails> units=apartment.getUnits();
		List<ApartmentPropertyDetailDTO> unitDTOs=new ArrayList<>();
		for(ApartmentPropertyDetails unit:units) {
			unitDTOs.add(apartmentPropertyDetailsModelDTOMapper(unit));
		}
		apartmentDTO.setUnits(unitDTOs);
		
		return apartmentDTO;
	}
	
	/**
	 * Converts property details domain model to property details DTO
	 *
	 * @param propertyDetails the property details
	 * @return the property details DTO
	 */
	public PropertyDetailsDTO propertyDetailModelDTOMapper(PropertyDetails propertyDetails) {
		PropertyDetailsDTO propertyDetailsDTO=new PropertyDetailsDTO();
		propertyDetailsDTO.setPropertyid(propertyDetails.getPropertyid());
		propertyDetailsDTO.setPropertydetailsid(propertyDetails.getPropertydetailsid());
		propertyDetailsDTO.setArea(propertyDetails.getArea());
		propertyDetailsDTO.setCapacity(propertyDetails.getCapacity());
		propertyDetailsDTO.setOccupied(propertyDetails.getOccupied());
		propertyDetailsDTO.setRent(propertyDetails.getRent());
		return propertyDetailsDTO;
	}
	
	/**
	 * Converts apartment property details domain model to property details DTO
	 *
	 * @param details the details
	 * @return the apartment property detail DTO
	 */
	public ApartmentPropertyDetailDTO apartmentPropertyDetailsModelDTOMapper(ApartmentPropertyDetails details) {
		ApartmentPropertyDetailDTO unitDTO=new ApartmentPropertyDetailDTO();
		PropertyDetails propertyDetails=details.getPropertyDetails();
		unitDTO.setPropertyid(propertyDetails.getPropertyid());
		unitDTO.setPropertydetailsid(propertyDetails.getPropertydetailsid());
		unitDTO.setArea(propertyDetails.getArea());
		unitDTO.setCapacity(propertyDetails.getCapacity());
		unitDTO.setRent(propertyDetails.getRent());
		unitDTO.setOccupied(propertyDetails.getOccupied());
		unitDTO.setDoorno(details.getDoorno());
		unitDTO.setFloorno(details.getFloorno());
		return unitDTO;
	}
	
	/**
	 * Converts apartment property details DTO to apartment property details domain model
	 *
	 * @param unitDTO the unit DTO
	 * @return the apartment property details
	 */
	public ApartmentPropertyDetails appartmentPropertyDetails(ApartmentPropertyDetailDTO unitDTO) {
		ApartmentPropertyDetails appartmentPropertyDetails=new ApartmentPropertyDetails();
		PropertyDetails propertyDetails=new PropertyDetails();
		propertyDetails.setPropertyid(unitDTO.getPropertyid());
		propertyDetails.setPropertydetailsid(unitDTO.getPropertydetailsid());
		propertyDetails.setArea(unitDTO.getArea());
		propertyDetails.setCapacity(unitDTO.getCapacity());
		propertyDetails.setRent(unitDTO.getRent());
		propertyDetails.setOccupied(unitDTO.getOccupied());
		
		appartmentPropertyDetails.setPropertyDetails(propertyDetails);
		appartmentPropertyDetails.setApartmentpropertydetailsid(unitDTO.getPropertydetailsid());
		appartmentPropertyDetails.setDoorno(unitDTO.getDoorno());
		appartmentPropertyDetails.setFloorno(unitDTO.getFloorno());
		return appartmentPropertyDetails;
	}
	
	/**
	 * Converts tenant domain model to tenant DTO
	 *
	 * @param tenant the tenant
	 * @return the tenant DTO
	 */
	public TenantDTO tenantModelDTOMapper(Tenant tenant) {
		TenantDTO tenantDTO=new TenantDTO();
		tenantDTO.setTenantid(tenant.getTenantid());
		tenantDTO.setTenantuserid(tenant.getTenantuserid());
		tenantDTO.setFirstname(tenant.getFirstname());
		tenantDTO.setLastname(tenant.getLastname());
		tenantDTO.setEmail(tenant.getEmail());
		tenantDTO.setDob(tenant.getDob());
		tenantDTO.setNationality(tenant.getNationality());
		return tenantDTO;
	}
	
	/**
	 * Converts tenant DTO to tenant domain model
	 *
	 * @return the tenant
	 */
	public Tenant tenantDTOModelMapper() {
		Tenant tenant=new Tenant();
		tenant.setTenantid(tenant.getTenantid());
		tenant.setTenantuserid(tenant.getTenantuserid());
		tenant.setFirstname(tenant.getFirstname());
		tenant.setLastname(tenant.getLastname());
		tenant.setEmail(tenant.getEmail());
		tenant.setDob(tenant.getDob());
		tenant.setNationality(tenant.getNationality());
		return tenant;
	}
	
	/**
	 * Converts lease domain model to lease DTO
	 *
	 * @param leaseDTO the lease DTO
	 * @return the lease
	 * @throws ParseException the parse exception
	 */
	public Lease leaseDTOModelMapper(LeaseDTO leaseDTO) throws ParseException {
		Lease lease=new Lease();
		lease.setLeaseid(leaseDTO.getLeaseid());
		lease.setLeasetenantid(leaseDTO.getLeasetenantid());
		lease.setOccupants(leaseDTO.getOccupants());
		lease.setMovein(Utils.parseDateToMilliseconds(leaseDTO.getMovein()));
		lease.setMoveout(Utils.parseDateToMilliseconds(leaseDTO.getMoveout()));
		lease.setTenantspropertydetailid(leaseDTO.getTenantspropertydetailid());
		return lease;
	}
	
	/**
	 * Converts lease DTO to lease domain model
	 *
	 * @param lease the lease
	 * @return the lease DTO
	 */
	public LeaseDTO leaseModelDTOMapper(Lease lease) {
		LeaseDTO leaseDTO=new LeaseDTO();
		leaseDTO.setLeaseid(lease.getLeaseid());
		leaseDTO.setLeasetenantid(lease.getLeasetenantid());
		leaseDTO.setMovein(Utils.getDate(lease.getMovein()));
		leaseDTO.setMoveout(Utils.getDate(lease.getMoveout()));
		leaseDTO.setOccupants(lease.getOccupants());
		leaseDTO.setRent(lease.getRent());
		leaseDTO.setTenantspropertydetailid(lease.getTenantspropertydetailid());
		leaseDTO.setContractid(lease.getContractid());
		leaseDTO.setContractstatus(lease.getContractstatus());
		return leaseDTO;
	}
	
}