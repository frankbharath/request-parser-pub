package com.bharath.rm.security;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.constants.SecurityXMLUtilConstants;
import com.bharath.rm.exception.URLException;
import com.bharath.rm.model.Parameter;
import com.bharath.rm.model.URL;

/**
 * The Class ValidateRequest.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 22, 2020 4:26:22 PM
 * Class Description
 */
public class ValidateRequest {
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(ValidateRequest.class);

	/**
	 * Check validity.
	 *
	 * @param request the request
	 * @return the JSON object
	 */
	public static void checkValidity(HttpServletRequest request) {
		URL url=SecurityXMLConfig.getURL(request);
		if(url==null) {
			log.error("The URL "+request.getRequestURI()+" for the HTTP protocol "+request.getMethod()+" is not configured in the security.xml file.");
			throw new URLException(I18NConfig.getMessage("error.url_validity.not_found"));
		}
		JSONObject data=new JSONObject();
		Enumeration<String> parameterNames=request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			if(request.getParameterValues(paramName).length>1) {
				data.put(paramName, String.join(",", request.getParameterValues(paramName)));
			}else {
				data.put(paramName, request.getParameter(paramName));
			}
		}
		validateParameters(data,url.getParameters());
	}
	
	/**
	 * Validate parameters.
	 *
	 * @param data the data
	 * @param parameters the parameters
	 * @return the JSON object
	 */
	public static void validateParameters(JSONObject data, HashMap<String, Parameter> parameters) {
		for(Map.Entry<String, Parameter>entry:parameters.entrySet()) {
			Parameter parameter=entry.getValue();
			if(parameter.isRequired() && !data.has(parameter.getParameterName())) {
				throw new URLException(I18NConfig.getMessage("error.url_validity.required_parameter_not_found",new Object[] {parameter.getParameterName()}));
			}
		}
		Iterator<String> parameterNames= data.keys();
		while (parameterNames.hasNext()) {
			String paramName = parameterNames.next();
			if(parameters.containsKey(paramName)) {
				Parameter parameter=parameters.get(paramName);
				if(Utils.isValidString(parameter.getTemplate())) {
					if(SecurityXMLUtilConstants.OBJECT.equals(parameter.getType())) {
						try {
							JSONObject object=new JSONObject(data.get(paramName).toString());
							validateParameters(object,parameter.getParameters());
						}catch (JSONException e) {
							log.error("The parameter "+paramName+" is not an object type.",e);
							throw new URLException(I18NConfig.getMessage("error.url_validity.invalid_parameter",new Object[] {paramName}));
					    }
					}else {
						try {
							JSONArray jsonObjects=new JSONArray(data.get(paramName).toString());
							if(parameter.isRequired() && jsonObjects.length()==0) {
								throw new URLException(I18NConfig.getMessage("error.url_validity.array_empty",new Object[] {paramName}));
							}
							for(int i=0;i<jsonObjects.length();i++) {
								JSONObject object=new JSONObject(jsonObjects.get(i).toString());
								validateParameters(object,parameter.getParameters());
						}
						}catch (JSONException e) {
							log.error("The parameter "+paramName+" is not an object or array type.",e);
							throw new URLException(I18NConfig.getMessage("error.url_validity.invalid_parameter",new Object[] {paramName}));
					    }
					}
				}else {
					if(SecurityXMLUtilConstants.ARRAY.equals(parameter.getType())) {
						try {
							JSONArray array=new JSONArray(data.getString(paramName));
							for(Object value:array) {
								validateParam(parameter, value.toString());
							}
						}catch (JSONException e) {
							log.error("The parameter "+paramName+" is not an array type.",e);
							throw new URLException(I18NConfig.getMessage("error.url_validity.invalid_parameter",new Object[] {paramName}));
					    }
					}else {
						String value=data.get(paramName).toString();
						if(SecurityXMLUtilConstants.LIST.equals(parameter.getType())) {
							String[] splitValues=value.split(",");
							for(String splitValue:splitValues) {
								validateParam(parameter, splitValue);
							}
						}else {
							validateParam(parameter, value);
						}
					}
				}
			}else {
				log.warn("The parameter "+paramName+" is not defined");
				//return ClientUtil.getErrorObject(ErrorCodes.NO_CONFIG_PARAMETER,I18N.getMessage("error.url_validity.no_config_paramter",new Object[] {paramName}));
			}
		}
	}
	
	/**
	 * Validate param.
	 *
	 * @param parameter the parameter
	 * @param value the value
	 */
	public static void validateParam(Parameter parameter, String value) {
		value=value.trim();
		Pattern pattern = Pattern.compile(parameter.getRegex());
		Matcher matcher = pattern.matcher(value);
		if(!matcher.matches()) {
			throw new URLException(I18NConfig.getMessage("error.url_validity.invalid_parameter",new Object[] {parameter.getParameterName()}));
		}else if(parameter.getMinLength()>value.length()) {
			throw new URLException(I18NConfig.getMessage("error.url_validity.min_length",new Object[] {parameter.getParameterName(), parameter.getMinLength()}));
		}else if(parameter.getMaxLength()!=0 && parameter.getMaxLength()<value.length()) {
			throw new URLException(I18NConfig.getMessage("error.url_validity.max_length",new Object[] {parameter.getParameterName(), parameter.getMaxLength()}));
		}
	}
}
