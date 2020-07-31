package com.bharath.rm.common;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.boot.jdbc.DataSourceBuilder;

import com.bharath.rm.configuration.RentPalThreadLocal;
import com.bharath.rm.constants.Constants;
import com.bharath.rm.constants.ErrorCodes;
import com.bharath.rm.constants.SuccessCode;

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
	
	public static JSONObject getErrorObject(ErrorCodes code) {
		return getErrorObject(code,null);
	}
	
	public static JSONObject getErrorObject(ErrorCodes code,String message) {
		JSONObject resp=new JSONObject();
		resp.put(Constants.STATUS, Constants.FAILED);
		resp.put(Constants.ERROR_CODE, code.toString());
		if(message!=null) {
			resp.put(Constants.MESSAGE, message);
		}
		return resp;
	}
	
	public static String generateAlphaNumericString(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}
	
	public static String getHostURL() {
		return ApplicationProperties.getInstance().getProperty("server.address");
	}
	
	public static String getHostURLWithPort() {
		ApplicationProperties properties=ApplicationProperties.getInstance();
		return "http://"+properties.getProperty("server.address")+":"+properties.getProperty("server.port");
	}
	
	public static long getUserId() {
		return Long.parseLong(RentPalThreadLocal.get("userId").toString());
	}
	
	public static String getUserEmail() {
		return RentPalThreadLocal.get("email").toString();
	}
}
