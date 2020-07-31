package com.bharath.rm.configuration;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 22, 2020 12:24:16 AM
 	* Class Description
*/
@Component
public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
    	factory.addConnectorCustomizers(connector -> {
    		connector.setParseBodyMethods("POST,PUT,DELETE");
    	});
    }
}