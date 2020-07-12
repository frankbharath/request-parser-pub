package com.bharath.rm.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 8, 2020 8:50:45 PM
 	* Class Description
*/
@Component
@PropertySource("classpath:application.properties")
public class StripePayment {
	
	@Autowired
	public StripePayment(@Value("${stripe.secretkey}") String secretKey) {
        Stripe.apiKey = secretKey;
    }
	
	public void createAccount() throws StripeException {
		List<Object> requestedCapabilities = new ArrayList<>();
		requestedCapabilities.add("card_payments");
		requestedCapabilities.add("transfers");
		Map<String, Object> params = new HashMap<>();
		params.put("type", "custom");
		params.put("country", "US");
		params.put("email", "jenny.rosen@example.com");
		params.put("requested_capabilities",requestedCapabilities);
		Account account = Account.create(params);
		System.out.println(account);
	}
}
