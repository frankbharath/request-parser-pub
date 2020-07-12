package com.bharath.rm.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 29, 2020 5:01:07 PM
 	* Class Description
*/
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		//System.out.println("checkie");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		//http.authorizeRequests().antMatchers("/*").permitAll().and().csrf().disable();
		http.csrf().disable().authorizeRequests().antMatchers("/").permitAll()
		//.antMatchers("/").hasRole("USER")
		.and().formLogin();
		//http.authorizeRequests().antMatchers("**").permitAll().and().csrf().and().headers().frameOptions().sameOrigin().xssProtection();
	}
}
