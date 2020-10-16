package com.bharath.rm.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 	* @author bharath
 	* @version 1.0
 	* Creation time: Jun 10, 2020 11:00:09 AM
 	* The class ApplicationProperties is a singleton class that stores properties from application.properties file
*/

public class ApplicationProperties {
	
	/** The logger will be used to log information such as exceptions, info or debug. **/
	private static final Logger log = LoggerFactory.getLogger(ApplicationProperties.class);

	/** The single instance of ApplicationProperties */
	private static ApplicationProperties SINGLE_INSTANCE = null;
	
	/** The application.properties is located in the /src/main/resources. */
	private static final String APPLICATION_PROPERTIES="application.properties";
	
	/** Holds all the properties from application.properties file. */
	private Properties properties = new Properties();
    
    /**
     * Instantiates a new application properties.
     */
    private ApplicationProperties() {
    	//To make sure the object is not created via Java Reflection API
    	if( ApplicationProperties.SINGLE_INSTANCE != null ) {
            throw new InstantiationError( "Creating of this object is not allowed." );
        }
    }
    
    /**
     * Gets the single instance of ApplicationProperties.
     *
     * @return single instance of ApplicationProperties
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static ApplicationProperties getInstance()  {
        if (SINGLE_INSTANCE == null) {
        	// To prevent multiple threads from trying to initiate the APPLICATION_PROPERTIES 
            synchronized (ApplicationProperties.class) {
            	// Once when thread holds access to APPLICATION_PROPERTIES, make sure whether the variable has been initiated or not
                if (SINGLE_INSTANCE == null) {
                	log.info("Creating a singleton for ApplicationProperties");
                	final ClassLoader classLoader = Utils.classloader();
                	try (final InputStream inputStream = classLoader.getResourceAsStream(APPLICATION_PROPERTIES)) {
                		SINGLE_INSTANCE = new ApplicationProperties();
                        SINGLE_INSTANCE.properties.load(inputStream);
                	} catch (IOException e) {
						log.error(e.getMessage());
					} 
                }
            }
        }
        return SINGLE_INSTANCE;
    }
    
   /**
    * Gets the property value.
    *
    * @param propertyKey the property key
    * @return the property value
    */
   public String getProperty(String propertyKey) {
	   if (propertyKey != null && !propertyKey.isEmpty()) {
		   return properties.getProperty(propertyKey);
	   }
	   return null;
    }
}
