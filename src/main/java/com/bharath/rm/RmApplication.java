package com.bharath.rm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RmApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RmApplication.class, args);	
		//JSONArray jsonObjects=new JSONArray("[{\"area\":1,\"capacity\":1,\"door\":1,\"floor\":1,\"rent\":1}]");
		//System.out.println(jsonObjects);
		/*Pattern pattern = Pattern.compile("/");
		System.out.println(pattern.pattern());
		Matcher matcher = pattern.matcher("/");
		System.out.println(matcher.matches());*/
	}

}
