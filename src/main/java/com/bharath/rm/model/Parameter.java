package com.bharath.rm.model;

import java.util.HashMap;

import com.bharath.rm.common.Utils;

/**
 * @author bharath:
 * @version 1.0
 * Creation time: Jun 15, 2020 9:10:02 PM
 * This class used to create Parameter object for parameters present in the request
 */
public class Parameter {
	/** Holds the parameter name. */
	private String parameterName;
	
	/** Holds the minimun length, applicable if the parameter value is String. */
	private int minLength;
	
	/** Holds the maximum length, applicable if the parameter value is String. */
	private int maxLength;
	
	/** Holds boolean, to check whether parameter is mandatory or not. */
	private boolean required;
	
	/** Regex helps to validate the value of the parameter. */
	private String regex;
	
	/**  Stores template name if the parameter has key value pair. */
	private String template;
	
	/**   Stores key value pairs. */
	private HashMap<String, Parameter> parameters;
	
	/**  If type is provided, the parameter should be an array or an object. */
	private String type;
	
	/**
	 * Instantiates a new parameter.
	 *
	 * @param parameterBuilder the parameter builder
	 */
	private Parameter(ParameterBuilder parameterBuilder) {
		String error=null;
		if(!Utils.isValidString(parameterBuilder.getParameterName())) {
			error="No parameter name found";
		}else if(Utils.isValidString(parameterBuilder.getTemplate())) {
			if(!Utils.isValidString(parameterBuilder.getType())) {
				error="Associate a type for the "+parameterBuilder.getParameterName()+", it shoud be either an object or array of objects ";
			}else if(parameterBuilder.getParameters()==null || parameterBuilder.getParameters().isEmpty()) {
				error="It is madatory for an object to be associated with an template "+parameterBuilder.getParameterName();
			}
		}else if(!Utils.isValidString(parameterBuilder.getRegex())) {
			error="If a parameter is not an object then it is mandatory to associate a regex with it.";
		}
		if(error!=null) {
			throw new NullPointerException(error);
		}
		this.parameterName=parameterBuilder.getParameterName();
		this.minLength=parameterBuilder.getMinLength();
		this.maxLength=parameterBuilder.getMaxLength();
		this.parameters=parameterBuilder.getParameters();
		this.regex=parameterBuilder.getRegex();
		this.required=parameterBuilder.isRequired();
		this.template=parameterBuilder.getTemplate();
		this.type=parameterBuilder.getType();
	}

	/**
	 * Gets the parameter name.
	 *
	 * @return the parameter name
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * Gets the min length.
	 *
	 * @return the min length
	 */
	public int getMinLength() {
		return minLength;
	}

	/**
	 * Gets the max length.
	 *
	 * @return the max length
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * Checks if is required.
	 *
	 * @return true, if is required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Gets the regex.
	 *
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public String getTemplate() {
		return template;
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
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	public static class ParameterBuilder {
		
		/** Holds the parameter name. */
		private String parameterName;
		
		/** Holds the minimun length, applicable if the parameter value is String. */
		private int minLength;
		
		/** Holds the maximum length, applicable if the parameter value is String. */
		private int maxLength;
		
		/** Holds boolean, to check whether parameter is mandatory or not. */
		private boolean required;
		
		/** Regex helps to validate the value of the parameter. */
		private String regex;
		
		/** Stores template name if the parameter has key value pair */
		private String template;
		
		/**  Stores key value pairs */
		private HashMap<String, Parameter> parameters;
		
		/** If type is provided, the parameter should be an array or an object */
		private String type;
		
		/**
		 * Gets the parameter name.
		 *
		 * @return the parameterName
		 */
		public String getParameterName() {
			return parameterName;
		}
		
		/**
		 * Sets the parameter name.
		 *
		 * @param parameterName the parameterName to set
		 */
		public void setParameterName(String parameterName) {
			this.parameterName = parameterName;
		}
		
		/**
		 * Gets the min length.
		 *
		 * @return the minLength
		 */
		public int getMinLength() {
			return minLength;
		}
		
		/**
		 * Sets the min length.
		 *
		 * @param minLength the minLength to set
		 */
		public void setMinLength(Integer minLength) {
			this.minLength = minLength;
		}
		
		/**
		 * Gets the max length.
		 *
		 * @return the maxLength
		 */
		public int getMaxLength() {
			return maxLength;
		}
		
		/**
		 * Sets the max length.
		 *
		 * @param maxLength the maxLength to set
		 */
		public void setMaxLength(Integer maxLength) {
			this.maxLength = maxLength;
		}
		
		/**
		 * Checks if is required.
		 *
		 * @return the required
		 */
		public boolean isRequired() {
			return required;
		}
		
		/**
		 * Sets the required.
		 *
		 * @param required the required to set
		 */
		public void setRequired(boolean required) {
			this.required = required;
		}
		
		/**
		 * Gets the regex.
		 *
		 * @return the regex
		 */
		public String getRegex() {
			return regex;
		}
		
		/**
		 * Sets the regex.
		 *
		 * @param regex the regex to set
		 */
		public void setRegex(String regex) {
			this.regex = regex;
		}
		
		/**
		 * @return the template
		 */
		public String getTemplate() {
			return template;
		}

		/**
		 * @param template the template to set
		 */
		public void setTemplate(String template) {
			this.template = template;
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
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		public Parameter build() {
			return new Parameter(this);
		}
	}
}
