package com.bharath.rm.service;

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
import com.bharath.rm.model.domain.User;
import com.bharath.rm.model.domain.Verification;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 9, 2020 10:02:38 PM
 	* Class Description
*/
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private UserDAO userDAO;
	
	private PasswordEncoder passwordEncoder;
	
	private EmailService emailService;
	
	@Autowired
	public UserServiceImpl(UserDAO userDAO) {
		this.userDAO=userDAO;
	}
	
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	public void setEmailUtil(EmailService emailUtil) {
		this.emailService = emailUtil;
	}
	
	
	@Override
	public JSONObject addUser(User user) {
		emailService.sendEmail();
		if(userDAO.userExist(user.getEmail())) {
			return Utils.getErrorObject(ErrorCodes.EMAIL_EXISTS, I18NConfig.getMessage("error.admin.email_exists"));
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		long userid=userDAO.addUser(user);
		long typeId=userDAO.getUserType(user.getType());
		userDAO.mapUserToType(userid, typeId);
		sendVerificationCodeForUser(userid);
		return Utils.getSuccessResponse(null, I18NConfig.getMessage("success.admin.added_success"));
	}
	
	@Override
	public void sendVerificationCodeForUser(long userid) {
		Verification verification=new Verification();
		verification.setUserid(userid);
		verification.setToken(Utils.generateAlphaNumericString(Constants.ALPHANUMLEN));
		long currentTime=System.currentTimeMillis();
		verification.setCreationtime(currentTime);
		
	}

}
