package com.bharath.rm.service.interfaces;

import java.io.IOException;

import javax.mail.MessagingException;

import org.json.JSONObject;

import com.bharath.rm.model.domain.User;
import com.bharath.rm.model.domain.Verification;


/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 30, 2020 7:27:39 PM
 	* Class Description
*/
public interface UserService {
	public JSONObject addUser(User user) throws MessagingException;
	public String addVerificationCodeForUser(long userid);
	public JSONObject verifyAccountForUser(Verification verification);
}
