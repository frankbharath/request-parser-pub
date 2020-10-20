package com.bharath.rm.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.filters.CsrfHeaderFilter;

/**
 * The Class SpringSecurityConfig.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 29, 2020 5:01:07 PM
 */

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	/** The user details service. */
	private final UserDetailsService userDetailsService;
	
	/** The password encoder. */
	private final PasswordEncoder passwordEncoder;
	
	/** The data source. */
	private final DataSource dataSource;
	
	/** The persistence token repository. */
	@Autowired
	private PersistentTokenRepository persistenceTokenRepository;
	
	/** The Constant REMEMBERMESECONDS. */
	private static final Integer REMEMBERMESECONDS=2592000;
	
	/**
	 * Instantiates a new spring security config.
	 *
	 * @param userDetailsService the user details service
	 * @param passwordEncoder the password encoder
	 * @param dataSource the data source
	 */
	@Autowired
	public SpringSecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, DataSource dataSource) {
		this.userDetailsService=userDetailsService;
		this.passwordEncoder=passwordEncoder;
		this.dataSource=dataSource;
	}
	
	/**
	 * Authentication filter.
	 *
	 * @return the ajax authentication filter
	 * @throws Exception the exception
	 */
	@Bean
    public AjaxAuthenticationFilter authenticationFilter() throws Exception {
		AjaxAuthenticationFilter authenticationFilter = new AjaxAuthenticationFilter();
        authenticationFilter.setAuthenticationFailureHandler(this::loginFailureHandler);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        authenticationFilter.setRememberMeServices(getPersistentTokenBasedRememberMeServices());
        return authenticationFilter;
    }
	
	/**
	 * Dao authentication provider.
	 *
	 * @return the dao authentication provider
	 */
	@Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }
	
	/**
	 * Authentication entry point handler.
	 *
	 * @return the custom authentication entry point handler
	 */
	@Bean
	public CustomAuthenticationEntryPointHandler authenticationEntryPointHandler() {
		return new CustomAuthenticationEntryPointHandler();
	}
	
	/**
	 * Access denied handler.
	 *
	 * @return the custom access denied handler
	 */
	@Bean
	public CustomAccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	/**
	 * Configure.
	 *
	 * @param auth the auth
	 * @throws Exception the exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	/**
	 * Persistent token repository.
	 *
	 * @return the persistent token repository
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}
	
	/**
	 * Gets the persistent token based remember me services.
	 *
	 * @return the persistent token based remember me services
	 */
	@Bean
	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
	    PersistentTokenBasedRememberMeServices persistenceTokenBasedservice = new PersistentTokenBasedRememberMeServices("rememberme", userDetailsService, persistenceTokenRepository);
	    persistenceTokenBasedservice.setAlwaysRemember(true);
	    persistenceTokenBasedservice.setTokenValiditySeconds(REMEMBERMESECONDS);
	    //persistenceTokenBasedservice.setUseSecureCookie(true);
	    return persistenceTokenBasedservice;
	  }
	
	/**
	 * Configure.
	 *
	 * @param http the http
	 * @throws Exception the exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.exceptionHandling().authenticationEntryPoint(authenticationEntryPointHandler())
			.accessDeniedHandler(accessDeniedHandler())
		.and()
			.csrf()
			.csrfTokenRepository(csrfTokenRepository())
        .and()
        	.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
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
        	.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        	.logoutSuccessUrl("/login")
        	.permitAll()
        .and()
	        .rememberMe()
	        .rememberMeServices(getPersistentTokenBasedRememberMeServices());
	}
	
	/**
	 * Csrf token repository.
	 *
	 * @return the csrf token repository
	 */
	private CsrfTokenRepository csrfTokenRepository() {
	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	    repository.setHeaderName("X-XSRF-TOKEN");
	    return repository;
	}
	
    /**
     * Login failure handler.
     *
     * @param request the request
     * @param response the response
     * @param e the e
     * @throws IOException Signals that an I/O exception has occurred.
     */
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
