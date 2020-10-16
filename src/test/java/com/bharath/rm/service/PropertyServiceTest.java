package com.bharath.rm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.bharath.rm.common.DTOModelMapper;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.configuration.SpringConfig;
import com.bharath.rm.configuration.ThymeleafTemplateConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.dao.interfaces.PropertyDAO;
import com.bharath.rm.dto.ApartmentDTO;
import com.bharath.rm.dto.ApartmentPropertyDetailDTO;
import com.bharath.rm.dto.HouseDTO;
import com.bharath.rm.dto.PropertyDTO;
import com.bharath.rm.dto.PropertyDetailsDTO;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.domain.Apartment;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.Property;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.model.domain.ApartmentPropertyDetails;
import com.bharath.rm.service.interfaces.PropertyService;

/**
 * @author bharath
 * @version 1.0
 * Creation time: Oct 15, 2020 4:55:30 PM
 * This class perform unit test for important methods in PropertyService class.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SpringConfig.class, ThymeleafTemplateConfig.class})
@Transactional
class PropertyServiceTest extends AbstractTest {

	/** The property service. */
	@Autowired
	PropertyService propertyService;
	
	/** The property DAO. */
	@MockBean
	PropertyDAO propertyDAO;
	
	/** The dto model mapper. */
	@SpyBean
	DTOModelMapper dtoModelMapper;
	
	/** The platform. */
	@MockBean
	PlatformTransactionManager platform;
	
	/**
	 * Tests whether exception is thrown or when a user tries to add property with a name that already exists in the database.
	 */
	@Test
	void testPropertyNameExists() {
		Property property=mock(Property.class);
		when(propertyDAO.propertyNameExists(property.getUserid(), property.getPropertyname())).thenReturn(true);
		Exception exception = assertThrows(APIRequestException.class, () -> {
			propertyService.addProperty(property);
		});
		assertTrue(I18NConfig.getMessage("error.property.name_exists").contains(exception.getMessage()));
	}
	
	/**
	 * On successful addition of property house, the addHouse function must return a HouseDTO object 
	 * with given property id 1l.
	 */
	@Test
	void testAddHouse() {
		HouseDTO houseDTO=mock(HouseDTO.class);
		
		House house=spy(House.class);
		PropertyDetails details=spy(PropertyDetails.class);
		
		doReturn(house).when(dtoModelMapper).houseDTOModelMapper(houseDTO);
		
		when(house.getCreationtime()).thenReturn(1l);
		when(house.getPropertydetails()).thenReturn(details);
		when(propertyDAO.propertyNameExists(house.getUserid(), house.getPropertyname())).thenReturn(false);
		when(propertyDAO.addProperty(house)).thenReturn(1l);
		when(propertyDAO.addPropertyDetails(details)).thenReturn(2l);
		
		HouseDTO resp=propertyService.addHouse(houseDTO);
		
		assertEquals(1l, resp.getPropertyid());
		assertEquals(2l, resp.getDetailsDTO().getPropertydetailsid());
	}
	
	/**
	 * On successful addition of property apartment, the addAppartment function must return a ApartmentDTO
	 * object with given property id 1l.
	 */
	@Test
	void testAddApartment() {
		ApartmentDTO apartmentDTO=mock(ApartmentDTO.class);

		Apartment apartment=spy(Apartment.class);
		List<ApartmentPropertyDetails> unitList=new ArrayList<>();
		
		doReturn(apartment).when(dtoModelMapper).apartmentDTOModelMapper(apartmentDTO);
		
		when(apartment.getCreationtime()).thenReturn(1l);
		when(apartment.getUnits()).thenReturn(unitList);
		
		when(propertyDAO.propertyNameExists(apartment.getUserid(), apartment.getPropertyname())).thenReturn(false);
		when(propertyDAO.addProperty(apartment)).thenReturn(1l);
		
		ApartmentDTO resp=propertyService.addAppartment(apartmentDTO);
		assertEquals(1l, resp.getPropertyid());
	}
	
	/**
	 * Test whether exception is thrown or not when user tries a property with a name that already exists in the database
	 */
	@Test
	void testPropertyNameExistsForUpdate() {
		Property property=mock(Property.class);
		when(propertyDAO.propertyExists(property.getUserid(), property.getPropertyid(), property.getPropertytype()))
		.thenReturn(true);
		when(propertyDAO.propertyNameExists(property.getUserid(), property.getPropertyname(), property.getPropertyid()))
		.thenReturn(true);
		Exception exception = assertThrows(APIRequestException.class, () -> {
			propertyService.updateProperty(property);
		});
		assertTrue(I18NConfig.getMessage("error.property.name_exists").contains(exception.getMessage()));
	}
	
	/**
	 * Test whether exception is thrown or not when user tries to update a property that does not exist in the database.
	 */
	@Test
	void testPropertyNotExistsForUpdate() {
		Property property=mock(Property.class);
		when(propertyDAO.propertyExists(property.getUserid(), property.getPropertyid(), property.getPropertytype()))
		.thenReturn(false);
		Exception exception = assertThrows(APIRequestException.class, () -> {
			propertyService.updateProperty(property);
		});
		assertTrue(I18NConfig.getMessage("error.property.not_found").contains(exception.getMessage()));
	}

	/**
	 * On successful update of property house, the updateHouse must return HouseDTO object with give property id 1l.
	 */
	@Test
	void testUpdateHouse() {
		HouseDTO houseDTO=mock(HouseDTO.class);
		
		House house=spy(House.class);
		house.setPropertyid(1l);
		house.setCreationtime(1l);
		
		PropertyDetails details=spy(PropertyDetails.class);
		
		doReturn(house).when(dtoModelMapper).houseDTOModelMapper(houseDTO);
		
		when(house.getPropertydetails()).thenReturn(details);
		when(propertyDAO.propertyExists(house.getUserid(), house.getPropertyid(), house.getPropertytype()))
		.thenReturn(true);
		when(propertyDAO.propertyNameExists(house.getUserid(), house.getPropertyname(), house.getPropertyid()))
		.thenReturn(false);
		
		HouseDTO resp=propertyService.updateHouse(houseDTO);
		assertEquals(1l, resp.getPropertyid());
	}
	
	/**
	 * Tests whether exception is thrown or not when apartment does not contain a unit. It is mandatory for an apartment 
	 * to have atlease one unit.
	 */
	@Test
	void testUpdateApartmentForMinUnits() {
		ApartmentDTO apartmentDTO=mock(ApartmentDTO.class);

		Apartment apartment=spy(Apartment.class);
		List<ApartmentPropertyDetails> unitList=new ArrayList<>();
		
		doReturn(apartment).when(dtoModelMapper).apartmentDTOModelMapper(apartmentDTO);
		
		when(apartment.getCreationtime()).thenReturn(1l);
		when(apartment.getUnits()).thenReturn(unitList);
		when(propertyDAO.propertyExists(apartment.getUserid(), apartment.getPropertyid(), apartment.getPropertytype()))
		.thenReturn(true);
		when(propertyDAO.propertyNameExists(apartment.getUserid(), apartment.getPropertyname(), apartment.getPropertyid()))
		.thenReturn(false);
		
		Exception exception = assertThrows(APIRequestException.class, () -> {
			propertyService.updateAppartment(apartmentDTO, new ArrayList<>());
		});
		
		assertTrue(I18NConfig.getMessage("error.appartment_property_no_unit").contains(exception.getMessage()));
	}
	
	/**
	 * On successful update of property apartment, the updateHouse must return HouseDTO object with give property id 1l.
	 */
	@Test
	void testUpdateApartment() {
		ApartmentDTO apartmentDTO=mock(ApartmentDTO.class);

		Apartment apartment=spy(Apartment.class);
		apartment.setPropertyid(1l);
		List<ApartmentPropertyDetails> unitList=new ArrayList<>();
		ApartmentPropertyDetails details=new ApartmentPropertyDetails();
		details.setPropertyDetails(new PropertyDetails());
		unitList.add(details);
		
		doReturn(apartment).when(dtoModelMapper).apartmentDTOModelMapper(apartmentDTO);
		
		when(apartment.getCreationtime()).thenReturn(1l);
		when(apartment.getUnits()).thenReturn(unitList);
		when(propertyDAO.propertyExists(apartment.getUserid(), apartment.getPropertyid(), apartment.getPropertytype()))
		.thenReturn(true);
		when(propertyDAO.propertyNameExists(apartment.getUserid(), apartment.getPropertyname(), apartment.getPropertyid()))
		.thenReturn(false);
		when(propertyDAO.getAppartment(apartment.getUserid(), apartment.getPropertyid())).thenReturn(apartment);
		when(propertyDAO.getApartmentUnits(apartment.getPropertyid())).thenReturn(unitList);
		
		ApartmentDTO resp=propertyService.updateAppartment(apartmentDTO, new ArrayList<>());
		assertEquals(1l, resp.getPropertyid());
	}
	
	/**
	 * Tests whether all properties are returned and the returned properties should be of size 2.
	 */
	@Test
	void testGetAllProperties() {
		List<Property> list=new ArrayList<>();
		Property property=mock(Property.class);
		list.add(property);
		list.add(property);
		PropertyDTO propertyDTO=mock(PropertyDTO.class);
		
		when(propertyDAO.getAllProperties(PropertyServiceTest.testUserId, null, null)).thenReturn(list);
		doReturn(propertyDTO).when(dtoModelMapper).propertyModelDTOMapper(property);
		
		assertEquals(2, propertyService.getAllProperties(null, null).size());
	}
	
	/**
	 * Test get all properties with meta data are returned. The returned map will contain house and property details if everything goes
	 * right
	 */
	@Test
	void testGetAllPropertiesWithMetaData() {
		List<Property> list=new ArrayList<>();
		Property propertyOne=spy(Property.class);
		propertyOne.setPropertytype(Constants.PropertyType.APPARTMENT.toString());
		propertyOne.setCreationtime(1l);
		list.add(propertyOne);
		Property propertyTwo=spy(Property.class);
		propertyTwo.setPropertytype(Constants.PropertyType.HOUSE.toString());
		propertyTwo.setCreationtime(1l);
		list.add(propertyTwo);
		
		HashMap<Long, PropertyDetails> propertyDetailsMap=new HashMap<>();
		propertyDetailsMap.put(2l, mock(PropertyDetails.class));
		
		HashMap<Long, List<ApartmentPropertyDetails>> apartmentDetails=new HashMap<>();
		List<ApartmentPropertyDetails> apartmentPropertyDetails=new ArrayList<>();
		apartmentPropertyDetails.add(mock(ApartmentPropertyDetails.class));
		apartmentDetails.put(3l, apartmentPropertyDetails);
		
		when(propertyDAO.getAllProperties(PropertyServiceTest.testUserId, null, null, true)).thenReturn(list);
		when(propertyDAO.getHouseDetails(Mockito.anyList())).thenReturn(propertyDetailsMap);
		when(propertyDAO.getApartmentUnits(Mockito.anyList())).thenReturn(apartmentDetails);
		
		doReturn(mock(PropertyDetailsDTO.class)).when(dtoModelMapper)
		.propertyDetailModelDTOMapper(Mockito.any(PropertyDetails.class));
		
		doReturn(mock(ApartmentPropertyDetailDTO.class)).when(dtoModelMapper)
		.apartmentPropertyDetailsModelDTOMapper(Mockito.any(ApartmentPropertyDetails.class));
		
		HashMap<String, Object> resultHashMap=propertyService.getAllPropertiesWithMetaData();
		
		assertTrue(resultHashMap.containsKey("housedetails"));
		assertTrue(resultHashMap.containsKey("apartmentdetails"));
		
	}
	
}
