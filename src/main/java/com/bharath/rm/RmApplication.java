package com.bharath.rm;

import com.bharath.rm.service.interfaces.PropertyService;
import com.bharath.rm.service.interfaces.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bharath.rm.constants.Constants;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.House;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.model.domain.PropertyType;

@SpringBootApplication
public class RmApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RmApplication.class, args);		
	}

}
