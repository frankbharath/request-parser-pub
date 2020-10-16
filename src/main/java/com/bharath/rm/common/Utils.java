package com.bharath.rm.common;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.HttpStatus;

import com.bharath.rm.configuration.RentPalThreadLocal;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.constants.SuccessCode;
import com.bharath.rm.dto.APIRequestResponse;
import com.bharath.rm.exception.APIException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
	* @author bharath
	* @version 1.0
	* Creation time: Jun 08, 2020 03:00:10 PM
	* This class has basic common utility function that will be used by this application.
*/

public final class Utils {
	
	/**
	 * Gets Classloader from thread context for static references 
	 *
	 * @return the class loader
	 */
	public static ClassLoader classloader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * Gets the data source. Even though there is a spring bean for data source, it will be handy if there is a requirement to create database 
	 * connection prior to bean creation for data source
	 *
	 * @return the data source
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static DataSource getDataSource() throws IOException {
		ApplicationProperties properties=ApplicationProperties.getInstance(); 
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(properties.getProperty("spring.datasource.driverClassName"));
		dataSourceBuilder.url(properties.getProperty("spring.datasource.url"));
		dataSourceBuilder.username(properties.getProperty("spring.datasource.username"));
		dataSourceBuilder.password(properties.getProperty("spring.datasource.password"));
		return dataSourceBuilder.build();
	}
	
	/**
	 * Gets the DB connection.
	 *
	 * @return the DB connection
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Connection getDBConnection() throws SQLException, IOException {
		return getDataSource().getConnection();
	}
	
	/**
	 * Gets the server path.
	 *
	 * @return the server path
	 */
	public static String getServerPath(){
        return getCatalinaBase() + File.separator + "webapps" + File.separator + "ROOT";
    }
 
	/**
	  * Gets the catalina base.
	  *
	  * @return the catalina base
	  */
	public static String getCatalinaBase() { 
		 return System.getProperty("catalina.base"); 
	}
	
	/**
	 * Checks if the String is valid or not. A string is considered invalid if it is null or the length of the string is equal to 0.
	 *
	 * @param value the value
	 * @return true, if it is a valid string
	 */
	public static boolean isValidString(String value) {
		return value!=null && value.trim().length()!=0;
	}
	
	public static void sendJSONResponse(HttpServletResponse response, String jsonResponse)throws IOException
	{
		response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=resp.txt;");
        response.setHeader("X-Download-Options", "noopen");
        response.setCharacterEncoding("UTF-8");
        try (ServletOutputStream servletStream = response.getOutputStream()) {
            servletStream.write(jsonResponse.getBytes("UTF-8"));
            servletStream.flush();
        }
    }
	
	public static void sendJSONErrorResponse(HttpServletResponse response, APIException exception) throws IOException
	{
		response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=resp.txt;");
        response.setHeader("X-Download-Options", "noopen");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(exception.getHttpStatus().value());
        try (ServletOutputStream out = response.getOutputStream()) {
        	ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, exception);
            out.flush();
        }        
	}
	public static JSONObject getSuccessResponse() {
		return getSuccessResponse(null, null);
	}
	public static JSONObject getSuccessResponse(JSONObject data, String message) {
		return getSuccessResponse(data,message,null);
	}
	public static JSONObject getSuccessResponse(JSONObject data, String message, SuccessCode code) {
		JSONObject resp=new JSONObject();
		resp.put(Constants.STATUS, Constants.SUCCESS);
		if(data!=null) {
			resp.put(Constants.DATA, data.toString());
		}
		if(message!=null) {
			resp.put(Constants.MESSAGE, message);
		}
		if(code!=null) {
			resp.put(Constants.SUCCESS_CODE, code);
		}
		return resp;
	}	
	
	public static JSONObject getErrorObject(ErrorCodes code, String message) {
		JSONObject resp=new JSONObject();
		resp.put(Constants.STATUS, Constants.FAILED);
		resp.put(Constants.ERROR_CODE, code.toString());
		if(message!=null) {
			resp.put(Constants.MESSAGE, message);
		}
		return resp;
	}
	public static APIException getApiException(String message, HttpStatus status) {
		return getApiException(message, status ,null);
	}
	public static APIException getApiException(String message, HttpStatus status, ErrorCodes errorCode) {
		APIException exception=new APIException();
		exception.setTimestamp(Utils.getDate(System.currentTimeMillis()));
		exception.setHttpStatus(status);
		exception.setMessage(message);
		if(errorCode!=null) {
			exception.setErrorCode(errorCode.toString());
		}
		exception.setStatus(Constants.FAILED);
		return exception;
	}
	public static APIRequestResponse getApiRequestResponse(String message) {
		return getApiRequestResponse(message,null);
	}
	public static APIRequestResponse getApiRequestResponse(String message, Object data) {
		APIRequestResponse response=new APIRequestResponse();
		if(data!=null) {
			response.setData(data);
		}
		response.setMessage(message);
		response.setHttpStatus(HttpStatus.OK);
		response.setStatus(Constants.SUCCESS);
		return response;
	}
	public static String generateAlphaNumericString(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}
	
	public static String getHostURL() {
		return ApplicationProperties.getInstance().getProperty("server.address");
	}
	
	public static String getHostURLWithPort() {
		ApplicationProperties properties=ApplicationProperties.getInstance();
		return properties.getProperty("server.http")+properties.getProperty("server.address")+":"+properties.getProperty("server.port");
	}
	
	public static long getUserId() {
		return Long.parseLong(RentPalThreadLocal.get("userId").toString());
	}
	
	public static String getUserEmail() {
		return RentPalThreadLocal.get("email").toString();
	}
	
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}
	
	public static String getDate(Long milliseconds) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
		return simpleDateFormat.format(new Date(milliseconds));
	}
	
	public static Long parseDateToMilliseconds(String date) throws ParseException{
		return parseDateToMilliseconds(date, "MMM d, yyyy");
	}
	
	public static Long parseDateToMilliseconds(String dateStr, String format) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		Date date = sdf.parse(dateStr);
		return date.getTime();
	}
	
	public static int diffDays(Long startMilliseconds, Long endMilliseconds) {
		return (int) ((endMilliseconds-startMilliseconds) / (1000*60*60*24));
	}
}
