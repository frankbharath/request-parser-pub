/**
 * 
 */
package com.bharath.rm.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.bharath.rm.security.SecurityXMLConfig;
import com.bharath.rm.configuration.CreateTableConfig;


/**
 * @author bharath
 * @version 1.0
 * Creation time: Jun 08, 2020 12:20:14 PM
 * The class Listener will be called when on Tomcat startup. The purpose of this class is to initialize database with tables and to parse the security.xml 
 * security.xml 
 * 
 */
@WebListener 
public class Listener implements ServletContextListener{

	/**
	 * This method will be invoked by Tomcat on startup.
	 *
	 * @param sce the sce
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContextListener.super.contextInitialized(sce);
		CreateTableConfig.init();
		SecurityXMLConfig.init();
	}
}
