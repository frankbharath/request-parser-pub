package com.bharath.rm.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.bharath.rm.common.ApplicationProperties;
import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.Constants.Tokentype;
import com.bharath.rm.dto.UserDTO;
import com.bharath.rm.model.Mail;
import com.bharath.rm.service.interfaces.EmailService;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 12, 2020 1:49:10 AM
 	* Class Description
*/
@Component
public class EmailServiceImpl implements EmailService{
	
	public static final String VERIFICAIONTEMPLATE="mail/verification";
	
	public static final String RESTPASSWORDTEMPLATE="mail/resetpassword";
	
	public static final String VERIFICAIONAPI="/verify";
	
	public static final String RESETPASSWORDAPI="/reset";
	
	private final JavaMailSender javaMailSender;
	
	
    private final SpringTemplateEngine templateEngine;
	
	@Autowired
	public EmailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
		this.javaMailSender=javaMailSender;
		this.templateEngine=templateEngine;
	}
	
	public void sendEmail(Mail mail) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
	        
        helper.setTo(mail.getTo());
        if(mail.getTemplate()!=null) {
        	Context context = new Context();
        	if(mail.getModel()!=null) {
                context.setVariables(mail.getModel());
            }
        	String html = templateEngine.process(mail.getTemplate(), context);
        	helper.setText(html, true);
        }
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        javaMailSender.send(message);
    }
	
	public void sendTokenEmailToUser(Long userid, String email, String token, Tokentype type) throws MessagingException {
		Map<String,Object> model=new HashMap<>();
		model.put("user", email.split("@")[0]);
		String url=Utils.getHostURLWithPort()+(type==Tokentype.VERIFY?VERIFICAIONAPI:RESETPASSWORDAPI)+"?token="+token;
		model.put("url", url);
		switch(type) {
			case RESET:
				Mail resetemail=new Mail(getSupportEmail(),email,I18NConfig.getMessage("email.resetpassword.subject"));
				resetemail.setTemplate(RESTPASSWORDTEMPLATE);
				resetemail.setModel(model);
				sendEmail(resetemail);
			break;
			case VERIFY:
				Mail verifymail=new Mail(getSupportEmail(),email,I18NConfig.getMessage("email.verification.subject"));
				verifymail.setTemplate(VERIFICAIONTEMPLATE);
				verifymail.setModel(model);
				sendEmail(verifymail);
			break;
		}
	}
	
	public static String getSupportEmail() {
		return ApplicationProperties.getInstance().getProperty("spring.mail.username");
	}
	
}
