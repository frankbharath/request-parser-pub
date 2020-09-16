package com.bharath.rm.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang3.Validate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.constants.SuccessCode;
import com.bharath.rm.constants.Constants.Tokentype;
import com.bharath.rm.dao.interfaces.UserDAO;
import com.bharath.rm.dto.UserDTO;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.Mail;
import com.bharath.rm.model.domain.UserType;
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
	
	@Autowired
	public UserServiceImpl(UserDAO userDAO) {
		this.userDAO=userDAO;
	}
	
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	public void setEmailUtil(EmailServiceImpl emailUtil) {
		this.emailService = emailUtil;
	}
	
	@Override
	public UserDTO addUser(User user) throws MessagingException {
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
		UserDTO userDTO=new UserDTO();
		userDTO.setEmail(user.getEmail());
		userDTO.setUserid(userid);
		userDTO.setUsertype(user.getUsertype().getType());
		userDTO.setCreationtime(Utils.getDate(user.getCreationtime()));
		return userDTO;
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
	public void updatePassword(String token, String password) {
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
	public Boolean userVerificationStatus(long userId) {
		return userDAO.verificationStatus(userId);
	}
	
	@Override
	public Boolean userCreatedStripeAccount() {
		return userDAO.userCreatedStripeAccount(Utils.getUserId());
	}
	
	@Override
	public String getUserType() {
		return userDAO.getUserType(Utils.getUserId());
	}
}
