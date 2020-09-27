package com.bharath.rm.security;

import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.filters.CsrfHeaderFilter;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 29, 2020 5:01:07 PM
 	* Class Description
*/

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
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
	
	@Bean
	public CustomAuthenticationEntryPointHandler authenticationEntryPointHandler() {
		return new CustomAuthenticationEntryPointHandler();
	}
	
	@Bean
	public CustomAccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		/*.exceptionHandling().authenticationEntryPoint(authenticationEntryPointHandler())
		.accessDeniedHandler(accessDeniedHandler())
		.and().csrf()
		.csrfTokenRepository(csrfTokenRepository())
        .and()*/.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
        .authorizeRequests()
        	.antMatchers(new String[] {"/resources/**","/favicon.ico"})
        	.permitAll()
        	.antMatchers(SecurityXMLConfig.getNoAuthUrl())
        	.permitAll()
        	.antMatchers(SecurityXMLConfig.getAuthReqUrl())
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
	private CsrfTokenRepository csrfTokenRepository() {
	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	    repository.setHeaderName("X-XSRF-TOKEN");
	    return repository;
	}
    private void loginFailureHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        if(e instanceof BadCredentialsException) {
        	Utils.sendJSONErrorResponse(response, Utils.getApiException(I18NConfig.getMessage("error.user.bad_credentials"), HttpStatus.UNAUTHORIZED, ErrorCodes.INVALID_CREDENTIALS));
        }else if(e instanceof UsernameNotFoundException){
        	Utils.sendJSONErrorResponse(response, Utils.getApiException(e.getMessage(), HttpStatus.UNAUTHORIZED, ErrorCodes.EMAIL_NOT_EXISTS));
        }else if(e instanceof DisabledException) {
        	Utils.sendJSONErrorResponse(response, Utils.getApiException(I18NConfig.getMessage("error.user.email_not_verified"), HttpStatus.UNAUTHORIZED, ErrorCodes.EMAIL_NOT_VERIFIED));
    	}else {
        	Utils.sendJSONErrorResponse(response, Utils.getApiException(I18NConfig.getMessage("error.unkown_issue"), HttpStatus.UNAUTHORIZED, ErrorCodes.UNKNOWN_ISSUE));
        }
    }
}
