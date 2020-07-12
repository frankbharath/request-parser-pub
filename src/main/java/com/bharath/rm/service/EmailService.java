package com.bharath.rm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bharath.rm.dto.UserDTO;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 12, 2020 1:49:10 AM
 	* Class Description
*/
@Component
public class EmailService {
	
	private final JavaMailSender javaMailSender;
	
	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender=javaMailSender;
	}
	
	
	public void sendEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("rentpal@hotmail.com");
        msg.setTo("barathkumaras@gmail.com");
        
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");
        
        
        javaMailSender.send(msg);
    }
}
