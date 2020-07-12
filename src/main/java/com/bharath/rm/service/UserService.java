package com.bharath.rm.service;

import java.io.IOException;

import org.json.JSONObject;

import com.bharath.rm.model.domain.User;


/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 30, 2020 7:27:39 PM
 	* Class Description
*/
public interface UserService {
	public JSONObject addUser(User user);
	public void sendVerificationCodeForUser(long userid);
}
