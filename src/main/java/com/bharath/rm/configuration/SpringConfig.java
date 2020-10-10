package com.bharath.rm.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.bharath.rm.dao.UserDAOImpl;
import com.bharath.rm.dao.interfaces.UserDAO;
import com.bharath.rm.model.OAuthDetails;
import com.bharath.rm.payment.StripePayment;


/**
 * @author bharath
 * @version 1.0
 * Creation time: Jun 15, 2020 10:59:14 PM
 *
 * This class contains spring configuration that are required to run the spring application. 
 * 
 */
/**
 * This means it is a spring configuration written in java instead of XML. 
 */
@Configuration

/** 
 * This means enabling spring MVC for the spring application
 */
@EnableWebMvc

/**
 * Will scan for components mentioned in the below packages, spring will create and maintain the objects for the components class 
 * and can be autowired wherever required
*/
@ComponentScan("com.bharath.rm.controller,com.bharath.rm.dao,com.bharath.rm.service")

@ServletComponentScan(basePackages = { 
	    "com.bharath.rm.filters", 
	    "com.bharath.rm.listeners"
	   })

@EnableTransactionManagement
public class SpringConfig  {
	
	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties getDatasourceProperties() {
	    return new DataSourceProperties();
	}
	
	@Bean
	@ConfigurationProperties("spring.security.oauth2")
	public OAuthDetails getOAuthDetails() {
	    return new OAuthDetails();
	}
	
	/**
	 * This method initializes PostgreSQL data source which will be used further used by JdbcTemplate.
	 *
	 * @return the data source
	 */
	@Bean
    public DataSource dataSource() {
		return getDatasourceProperties().initializeDataSourceBuilder().build();
    }
	
	/**
	 * JdbcTemplate object is used to execute SQL queries in PostgresSQL. JdbcTemplate is thread safe once it is configured as long as the state is not modified by different threads.
	 * But if a thread tries to change the state of JdbcTemplate by setting fetchSize or maxRows, this changes the state of the JdbcTemplate making it not thread safe. Either use
	 * prototype bean or do not change the state of JdbcTemplate.
	 * @return the jdbc template
	 */
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
	
	@Bean
	public NamedParameterJdbcTemplate namedJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource());
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
