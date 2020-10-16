package com.bharath.rm.service;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.rm.common.DTOModelMapper;
import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.Constants.Tokentype;
import com.bharath.rm.dao.interfaces.UserDAO;
import com.bharath.rm.dto.UserDTO;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.domain.User;
import com.bharath.rm.model.domain.Verification;
import com.bharath.rm.service.interfaces.UserService;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 9, 2020 10:02:38 PM
 	* Class Description
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

	private UserDAO userDAO;
	
	private PasswordEncoder passwordEncoder;
	
	private EmailServiceImpl emailService;
	
	private DTOModelMapper dtoModelMapper;
	
	@Autowired
	public UserServiceImpl(UserDAO userDAO, DTOModelMapper dtoModelMapper, PasswordEncoder passwordEncoder, EmailServiceImpl emailUtil) {
		this.userDAO=userDAO;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailUtil;
		this.dtoModelMapper=dtoModelMapper;
	}
	
	@Override
	public UserDTO addUser(User user, String confirmPassword) throws MessagingException {
		if(!user.getPassword().equals(confirmPassword)) {
			throw new APIRequestException(I18NConfig.getMessage("error.register.password_mismatch"));
		}
		if(userDAO.userExist(user.getEmail())) {
			throw new APIRequestException(I18NConfig.getMessage("error.user.email_exists"));
		}
		user.setCreationtime(System.currentTimeMillis());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		long typeId=userDAO.getUserType(user.getUsertype().getType());
		user.getUsertype().setTypeid(typeId);
		long userid=userDAO.addUser(user);
		sendVerificationLinkToUser(userid, user.getEmail());
		user.setUserid(userid);
		return dtoModelMapper.userModelDTOMapper(user);
	}
	
	@Override
	public void verifyAccountForUser(String token) {
		Verification verification=userDAO.getVerificationCode(token,Constants.Tokentype.VERIFY);
		if(verification==null || verification.getCreationtime()+Constants.EXPIRATIONINTERVAL<System.currentTimeMillis()) {
			if(verification!=null) {
				userDAO.deleteVerificationCode(verification);
				userDAO.deleteUserAccount(verification.getUserid());
			}
			throw new APIRequestException(I18NConfig.getMessage("error.user.verification"));
		}else {
			userDAO.deleteVerificationCode(verification);
			userDAO.verifyUserAccount(verification.getUserid());
		}
	}
	
	@Override
	public void sendVerificationLinkToUser(long userid, String email) throws MessagingException {
		Verification verification=new Verification();
		verification.setUserid(userid);
		String token=Utils.generateAlphaNumericString(Constants.ALPHANUMLEN);
		verification.setToken(token);
		long currentTime=System.currentTimeMillis();
		verification.setCreationtime(currentTime);
		verification.setType(Constants.Tokentype.VERIFY.getValue());
		userDAO.addVerificationCode(verification);
		emailService.sendTokenEmailToUser(userid,email,token,Tokentype.VERIFY);
	}
	
	@Override
	public void resetPassword(String email) throws MessagingException {
		Long userId=userDAO.getUserId(email);
		if(userId!=null && userDAO.isUserAccountVerified(userId)) {
			String token=Utils.generateAlphaNumericString(Constants.ALPHANUMLEN);
			Verification verification=new Verification();
			verification.setUserid(userId);
			verification.setCreationtime(System.currentTimeMillis());
			verification.setToken(token);
			verification.setType(Tokentype.RESET.getValue());
			userDAO.deleteVerificationCode(verification);
			userDAO.addVerificationCode(verification);
			emailService.sendTokenEmailToUser(userId,email,token,Tokentype.RESET);
		}
	}
	
	@Override
	public String getUserEmailForResetToken(String token) {
		String email=userDAO.getUserEmailForToken(token, Tokentype.RESET);
		if(email==null) {
			throw new APIRequestException(I18NConfig.getMessage("html.resetpassword.linkexpiredmessage"));
		}
		return email;
	}
	
	@Override
	public void updatePassword(String token, String password, String confirmPassword) {
		if(!password.equals(confirmPassword)) {
			throw new APIRequestException(I18NConfig.getMessage("error.register.password_mismatch"));
		}
		Verification verification=userDAO.getVerificationCode(token,Constants.Tokentype.RESET);
		if(verification==null || verification.getCreationtime()+Constants.EXPIRATIONINTERVAL<System.currentTimeMillis()) {
			if(verification!=null) {
				userDAO.deleteVerificationCode(verification);
			}
			throw new APIRequestException(I18NConfig.getMessage("error.user.reset"));
		}else {
			userDAO.deleteVerificationCode(verification);
			userDAO.updatePassword(verification.getUserid(), passwordEncoder.encode(password));
		}
	}
	
	@Override
	public void changePassword(String password, String confirmPassword) {
		if(!password.equals(confirmPassword)) {
			throw new APIRequestException(I18NConfig.getMessage("error.register.password_mismatch"));
		}
		userDAO.updatePassword(Utils.getUserId(), passwordEncoder.encode(password));
	}
	
	@Override
	public Boolean userVerificationStatus(long userId) {
		return userDAO.verificationStatus(userId);
	}
	
	@Override
	public String getUserType() {
		return userDAO.getUserType(Utils.getUserId());
	}
}
