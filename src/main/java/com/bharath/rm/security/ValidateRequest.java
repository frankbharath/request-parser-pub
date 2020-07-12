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
import com.bharath.rm.security.SecurityXMLConfig;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.constants.SecurityXMLUtilConstants;
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
	public static JSONObject checkValidity(HttpServletRequest request) {
		URL url=SecurityXMLConfig.getURL(request);
		if(url==null) {
			log.error("The URL "+request.getRequestURI()+" for the HTTP protocol "+request.getMethod()+" is not configured in the security.xml file.");
			return Utils.getErrorObject(ErrorCodes.URL_NOT_FOUND,I18NConfig.getMessage("error.url_validity.not_found"));
		}
		JSONObject data=new JSONObject();
		Enumeration<String> parameterNames=request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			data.put(paramName, request.getParameter(paramName));
		}
		return validateParameters(data,url.getParameters());
	}
	
	/**
	 * Validate parameters.
	 *
	 * @param data the data
	 * @param parameters the parameters
	 * @return the JSON object
	 */
	public static JSONObject validateParameters(JSONObject data, HashMap<String, Parameter> parameters) {
		for(Map.Entry<String, Parameter>entry:parameters.entrySet()) {
			Parameter parameter=entry.getValue();
			if(parameter.isRequired() && !data.has(parameter.getParameterName())) {
				return Utils.getErrorObject(ErrorCodes.REQURIED_PARAMETER_NOT_FOUND,I18NConfig.getMessage("error.url_validity.required_parameter_not_found",new Object[] {parameter.getParameterName()}));
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
							JSONObject resp=validateParameters(object,parameter.getParameters());
							if(Constants.FAILED.equals(resp.get(Constants.STATUS))) {
								return resp;
							}
						}catch (JSONException e) {
							log.error("The parameter "+paramName+" is not an object type.",e);
							return Utils.getErrorObject(ErrorCodes.INVALID_PARAMETER,I18NConfig.getMessage("error.url_validity.invalid_parameter",new Object[] {paramName}));
					    }
					}else {
						try {
							JSONArray jsonObjects=new JSONArray(data.get(paramName));
							for(Object j:jsonObjects) {
								JSONObject object=new JSONObject(j);
								JSONObject resp=validateParameters(object,parameter.getParameters());
								if(Constants.FAILED.equals(resp.get(Constants.STATUS))) {
									return resp;
								}
						}
						}catch (JSONException e) {
							log.error("The parameter "+paramName+" is not an object or array type.",e);
							return Utils.getErrorObject(ErrorCodes.INVALID_PARAMETER,I18NConfig.getMessage("error.url_validity.invalid_parameter",new Object[] {paramName}));
					    }
					}
				}else {
					Pattern pattern = Pattern.compile(parameter.getRegex());
					if(SecurityXMLUtilConstants.ARRAY.equals(parameter.getType())) {
						try {
							JSONArray array=new JSONArray(data.getString(paramName));
							for(Object value:array) {
								Matcher matcher = pattern.matcher(value.toString());
								if(!matcher.matches()) {
									log.error("The value of the "+paramName+" does not match the regex pattern "+parameter.getRegex()+".");
									return Utils.getErrorObject(ErrorCodes.INVALID_PARAMETER,I18NConfig.getMessage("error.url_validity.invalid_parameter",new Object[] {paramName}));
								}
							}
						}catch (JSONException e) {
							log.error("The parameter "+paramName+" is not an array type.",e);
							return Utils.getErrorObject(ErrorCodes.INVALID_PARAMETER,I18NConfig.getMessage("error.url_validity.invalid_parameter",new Object[] {paramName}));
					    }
					}else {
						String paramValue = data.get(paramName).toString();
						Matcher matcher = pattern.matcher(paramValue);
						if(!matcher.matches()) {
							log.error("The value of the "+paramName+" does not match the regex pattern "+parameter.getRegex()+".");
							return Utils.getErrorObject(ErrorCodes.INVALID_PARAMETER,I18NConfig.getMessage("error.url_validity.invalid_parameter",new Object[] {paramName}));
						}
					}
				}
			}else {
				log.warn("The parameter "+paramName+" is not defined");
				//return ClientUtil.getErrorObject(ErrorCodes.NO_CONFIG_PARAMETER,I18N.getMessage("error.url_validity.no_config_paramter",new Object[] {paramName}));
			}
		}
		return Utils.getSuccessResponse();
	}
}