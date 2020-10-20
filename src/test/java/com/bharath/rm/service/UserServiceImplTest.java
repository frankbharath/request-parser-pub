package com.bharath.rm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.bharath.rm.common.DTOModelMapper;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.configuration.SpringConfig;
import com.bharath.rm.configuration.ThymeleafTemplateConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.dao.interfaces.UserDAO;
import com.bharath.rm.dto.UserDTO;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.domain.User;
import com.bharath.rm.model.domain.UserType;
import com.bharath.rm.model.domain.Verification;
import com.bharath.rm.service.interfaces.UserService;

/**
 * The Class UserServiceImplTest.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Oct 17, 2020 11:30:09 PM
 * This class perform unit test for important methods in UserService class.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {SpringConfig.class, ThymeleafTemplateConfig.class})
class UserServiceImplTest extends AbstractTest {

	/** The user service. */
	@Autowired
	UserService userService;
	
	/** The user DAO. */
	@MockBean
	UserDAO userDAO;
	
	/** The password encoder. */
	@MockBean
	PasswordEncoder passwordEncoder;
	
	/** The email service. */
	@MockBean
	EmailServiceImpl emailService;
	
	/** The dto model mapper. */
	@MockBean
	DTOModelMapper dtoModelMapper;
	
	/** The platform. */
	@MockBean
	PlatformTransactionManager platform;
	
	/**
	 * Test add user for password mismatch.
	 */
	@Test
	public void testAddUserForPasswordMismatch() {
		User user=mock(User.class);
		
		when(user.getPassword()).thenReturn("test@123");
		
		Exception exception = assertThrows(APIRequestException.class, () -> {
			userService.addUser(user, null);
		});
		assertEquals(I18NConfig.getMessage("error.register.password_mismatch"), exception.getMessage());
	}
	
	/**
	 * Test add user for duplicate email.
	 */
	@Test
	public void testAddUserForDuplicateEmail() {
		User user=mock(User.class);
		
		when(user.getPassword()).thenReturn("test@123");
		when(user.getEmail()).thenReturn("test@test.com");
		when(userDAO.userExist(user.getEmail())).thenReturn(true);
		
		Exception exception = assertThrows(APIRequestException.class, () -> {
			userService.addUser(user, "test@123");
		});
		assertEquals(I18NConfig.getMessage("error.user.email_exists"), exception.getMessage());
	}

	/**
	 * Test add user.
	 */
	@Test
	public void testAddUser() {
		User user=mock(User.class);
		
		when(user.getPassword()).thenReturn("test@123");
		when(user.getEmail()).thenReturn("test@test.com");
		when(userDAO.userExist(user.getEmail())).thenReturn(false);
		when(dtoModelMapper.userModelDTOMapper(user)).thenReturn(mock(UserDTO.class));
		when(user.getUsertype()).thenReturn(mock(UserType.class));
		when(user.getUsertype().getType()).thenReturn("OWNER");
		when(userDAO.getUserType(user.getUsertype().getType())).thenReturn(1l);
		
		try {
			assertNotNull(userService.addUser(user, "test@123"));
		} catch (MessagingException e) {}
	}
	
	/**
	 * Test verify account for user.
	 */
	@Test
	public void testVerifyAccountForUser() {
		when(userDAO.getVerificationCode("token", Constants.Tokentype.VERIFY)).thenReturn(null);
		Exception exception = assertThrows(APIRequestException.class, () -> {
			userService.verifyAccountForUser("token");
		});
		assertEquals(I18NConfig.getMessage("error.user.verification"), exception.getMessage());
	}
	
	/**
	 * Test update password for password mismatch.
	 */
	@Test
	public void testUpdatePasswordForPasswordMismatch() {
		Exception exception = assertThrows(APIRequestException.class, () -> {
			userService.updatePassword("token", "test@123", "test@12");
		});
		assertEquals(I18NConfig.getMessage("error.register.password_mismatch"), exception.getMessage());
	}
	
	/**
	 * Test update password for verification.
	 */
	@Test
	public void testUpdatePasswordForVerification() {
		when(userDAO.getVerificationCode("token", Constants.Tokentype.RESET)).thenReturn(null);
		Exception exception = assertThrows(APIRequestException.class, () -> {
			userService.updatePassword("token", "test@123", "test@123");
		});
		assertEquals(I18NConfig.getMessage("error.user.reset"), exception.getMessage());
	}
	
	/**
	 * Test update password.
	 */
	@Test
	public void testUpdatePassword() {
		Verification verification=mock(Verification.class);
		when(verification.getCreationtime()).thenReturn(System.currentTimeMillis());
		when(userDAO.getVerificationCode("token", Constants.Tokentype.RESET)).thenReturn(verification);
		userService.updatePassword("token", "test@123", "test@123");
	}
	
	/**
	 * Test change password for password mismatch.
	 */
	@Test
	public void testChangePasswordForPasswordMismatch() {
		Exception exception = assertThrows(APIRequestException.class, () -> {
			userService.changePassword("test@12", "test@123");
		});
		assertEquals(I18NConfig.getMessage("error.register.password_mismatch"), exception.getMessage());
	}
}
