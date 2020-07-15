package com.bharath.rm.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.dao.UserDAO;
import com.bharath.rm.model.Mail;
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
	public JSONObject addUser(User user) throws MessagingException {
		if(userDAO.userExist(user.getEmail())) {
			return Utils.getErrorObject(ErrorCodes.EMAIL_EXISTS, I18NConfig.getMessage("error.user.email_exists"));
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		long userid=userDAO.addUser(user);
		long typeId=userDAO.getUserType(user.getType());
		userDAO.mapUserToType(userid, typeId);
		String token=addVerificationCodeForUser(userid);
		emailService.sendVerificationEmailToUser(userid,user.getEmail(),token);
		return Utils.getSuccessResponse(null, I18NConfig.getMessage("success.user.added_success"));
	}
	
	@Override
	public String addVerificationCodeForUser(long userid) {
		Verification verification=new Verification();
		verification.setUserid(userid);
		String token=Utils.generateAlphaNumericString(Constants.ALPHANUMLEN);
		verification.setToken(token);
		long currentTime=System.currentTimeMillis();
		verification.setCreationtime(currentTime);
		userDAO.addVerificationCode(verification);
		return token;
	}
	
	@Override
	public JSONObject verifyAccountForUser(Verification verification) {
		boolean isValid=userDAO.validateVerificationCode(verification);
		if(isValid) {
			userDAO.deleteVerificationCode(verification);
			userDAO.verifyUserAccount(verification.getUserid());
			return Utils.getSuccessResponse(null, I18NConfig.getMessage("success.user.verify_success"));
		}else {
			return Utils.getErrorObject(ErrorCodes.LINK_EXPIRED, I18NConfig.getMessage("error.user.verification"));
		}
	}
	
}
