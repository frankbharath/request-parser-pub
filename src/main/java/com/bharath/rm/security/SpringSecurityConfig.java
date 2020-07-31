package com.bharath.rm.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.ErrorCodes;


/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 29, 2020 5:01:07 PM
 	* Class Description
*/
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final Logger log = LoggerFactory.getLogger(SpringSecurityConfig.class);

	private final UserDetailsService userDetailsService;
	
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public SpringSecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService=userDetailsService;
		this.passwordEncoder=passwordEncoder;
	}
	
	@Bean
    public AjaxAuthenticationFilter authenticationFilter() throws Exception {
		AjaxAuthenticationFilter authenticationFilter = new AjaxAuthenticationFilter();
        authenticationFilter.setAuthenticationFailureHandler(this::loginFailureHandler);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }
	
	@Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
        	.antMatchers(new String[] {"/resources/**","/favicon.ico"})
        	.permitAll()
        	.antMatchers(SecurityXMLConfig.getNoAuthUrl())
        	.permitAll()
        	.anyRequest()
        	.authenticated()
        .and()
        	.addFilterBefore(
                authenticationFilter(),
                UsernamePasswordAuthenticationFilter.class)
        	.formLogin()
        	.loginPage("/login")
        	.loginProcessingUrl("/login")
        	.permitAll()
        .and()
        	.logout()
        	.logoutSuccessUrl("/login")
        	.permitAll();
	}
	
    private void loginFailureHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        JSONObject resp;
        if(e instanceof BadCredentialsException) {
        	resp=Utils.getErrorObject(ErrorCodes.INVALID_CREDENTIALS, I18NConfig.getMessage("error.user.bad_credentials"));
        }else if(e instanceof UsernameNotFoundException){
        	resp=Utils.getErrorObject(ErrorCodes.INVALID_CREDENTIALS, e.getMessage());
        }else {
        	resp=Utils.getErrorObject(ErrorCodes.UNKNOWN_ISSUE, I18NConfig.getMessage("error.unkown_issue"));
        	log.error(e.getMessage());
        }
        Utils.sendJSONResponse(response, resp.toString());
    }
}
