package com.bharath.rm.service.interfaces;

import javax.mail.MessagingException;

import com.bharath.rm.constants.Constants.Tokentype;
import com.bharath.rm.model.Mail;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 12, 2020 10:54:44 PM
 	* Class Description
*/
public interface EmailService {
	public void sendEmail(Mail mail) throws MessagingException;
	public void sendTokenEmailToUser(Long userid, String email, String token, Tokentype type) throws MessagingException;
}
