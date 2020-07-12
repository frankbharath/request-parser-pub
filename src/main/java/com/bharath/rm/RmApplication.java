package com.bharath.rm;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RmApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmApplication.class, args);
		/*Pattern pattern = Pattern.compile("^(TENANT|OWNER)$");
		Matcher matcher = pattern.matcher("OWNER");
		System.out.println(matcher.matches());*/
	}

}
