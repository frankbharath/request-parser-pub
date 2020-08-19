package com.bharath.rm.model;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Aug 1, 2020 7:01:13 PM
 	* Class Description
*/

import java.util.HashMap;

public class StripeConnect {
	private String url;
	private String endpoint;
	private HashMap<String, String> parameters;
	
	public StripeConnect(String url, String endpoint) {
		this.url=url;
		this.endpoint=endpoint;
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return endpoint;
	}
	
	/**
	 * @return the parameters
	 */
	public HashMap<String, String> getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}
	public void build() {
		
	}
}
