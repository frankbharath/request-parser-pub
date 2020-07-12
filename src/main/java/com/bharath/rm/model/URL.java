package com.bharath.rm.model;

import java.util.HashMap;
import java.util.List;

import com.bharath.rm.common.Utils;

/**
	* @author bharath
	* @version 1
	* Creation time: Jun 14, 2020 11:51:09 PM
	* The class URI will create an object for incoming HTTP request. The purpose of this class is to validate the request and its parameter.
*/

public class URL {
	
	/** This will hold the URL of the current request, eg., /admin/signup */
	private String url;
	
	/**  This will hold HTTP method, such as post, get. */
	private String method;
	
	/**  The will hold whether the request requires authentication to access the resource. */
	private boolean authentication;
	
	/**  This will hold roles such as user, admin or superadmin. This can be used to dictate who can access the requested resource. */
	private List<String> roles;
	
	/**  The will hold the parameters that are passed in the current request. */
	private HashMap<String, Parameter> parameters;
	
	/**
	 * Instantiates a new uri.
	 *
	 * @param builder the builder
	 */
	public URL(URIBuilder builder) {
		String error=null;
		if(!Utils.isValidString(builder.getUrl())) {
			error="URL has not been set for the request";
		}else if(!Utils.isValidString(builder.getMethod())) {
			error="No HTTP method has been set for the "+builder.getUrl();
		}/*else if(roles==null || roles.isEmpty()) {
			error="No roles have been assigned for the "+uri;
		}*/
		if(error!=null) {
			throw new NullPointerException(error);
		}
		this.url = builder.getUrl();
		this.method = builder.getMethod();
		this.authentication = builder.isAuthentication();
		this.roles = builder.getRoles();
		this.parameters = builder.getParameters();
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Checks if is authentication.
	 *
	 * @return true, if is authentication
	 */
	public boolean isAuthentication() {
		return authentication;
	}

	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public HashMap<String, Parameter> getParameters() {
		return parameters;
	}

	/**
	 * Gets the method.
	 *
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	public static class URIBuilder {
		
		/** This will hold the URL of the current request, eg., /admin/signup */
		private String url;
		
		/**  This will hold HTTP method, such as post, get. */
		private String method;
		
		/**  The will hold whether the request requires authentication to access the resource. */
		private boolean authentication;
		
		/**  This will hold roles such as user, admin or superadmin. This can be used to dictate who can access the requested resource. */
		private List<String> roles;
		
		/**  The will hold the parameters that are passed in the current request. */
		private HashMap<String, Parameter> parameters;

		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @param uri the uri to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		/**
		 * @return the method
		 */
		public String getMethod() {
			return method;
		}

		/**
		 * @param method the method to set
		 */
		public void setMethod(String method) {
			this.method = method;
		}

		/**
		 * @return the authentication
		 */
		public boolean isAuthentication() {
			return authentication;
		}

		/**
		 * @param authentication the authentication to set
		 */
		public void setAuthentication(boolean authentication) {
			this.authentication = authentication;
		}

		/**
		 * @return the roles
		 */
		public List<String> getRoles() {
			return roles;
		}

		/**
		 * @param roles the roles to set
		 */
		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

		/**
		 * @return the parameters
		 */
		public HashMap<String, Parameter> getParameters() {
			return parameters;
		}

		/**
		 * @param parameters the parameters to set
		 */
		public void setParameters(HashMap<String, Parameter> parameters) {
			this.parameters = parameters;
		}

		/**
		 * Builds the URI.
		 *
		 * @return the uri
		 */
		public URL build() {
			return new URL(this);
		}

		@Override
		public String toString() {
			return "URIBuilder [url=" + url + ", method=" + method + ", authentication=" + authentication + ", roles="
					+ roles + ", parameters=" + parameters + "]";
		}
	}
	
	@Override
	public String toString() {
		return "URL [url=" + url + ", method=" + method + ", authentication=" + authentication + ", roles=" + roles
				+ ", parameters=" + parameters + "]";
	}	
}
