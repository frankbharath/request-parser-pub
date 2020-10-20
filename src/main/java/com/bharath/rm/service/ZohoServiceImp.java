package com.bharath.rm.service;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bharath.rm.common.ApplicationProperties;
import com.bharath.rm.common.Utils;
import com.bharath.rm.configuration.I18NConfig;
import com.bharath.rm.exception.APIRequestException;
import com.bharath.rm.model.OAuthDetails;
import com.bharath.rm.model.domain.Address;
import com.bharath.rm.model.domain.Lease;
import com.bharath.rm.model.domain.PropertyDetails;
import com.bharath.rm.model.domain.Tenant;

/**
 * The Class ZohoServiceImp.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 28, 2020 11:05:41 PM
 */

@Service
public class ZohoServiceImp {

	/** The oauth details. */
	private OAuthDetails oAuthDetails;
	
	/** The access token. */
	private String accessToken=null;
	
	/**
	 * Instantiates a new zoho service imp.
	 *
	 * @param oAuthDetails the o auth details
	 */
	@Autowired
	public ZohoServiceImp(OAuthDetails oAuthDetails) {
		this.oAuthDetails=oAuthDetails;
	}
	
	/**
	 * Generate access token.
	 */
	public void generateAccessToken() {
		this.accessToken=null;
		HttpPost postMethod = new HttpPost(ApplicationProperties.getInstance().getProperty("zoho.accounts.oauth")+"?client_id="+oAuthDetails.getClientId()+"&client_secret="+oAuthDetails.getClientSecret()+"&grant_type=refresh_token&refresh_token="+oAuthDetails.getRefreshToken());
		HttpResponse response;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			response = httpClient.execute(postMethod);
			String responseJSON = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject submitResponse = new JSONObject(responseJSON);
			if(submitResponse.has("access_token")) {
				this.accessToken=submitResponse.getString("access_token");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the request details.
	 *
	 * @param requestId the request id
	 * @return the request details
	 */
	public JSONObject getRequestDetails(Long requestId) {
		HttpGet getMethod = new HttpGet(ApplicationProperties.getInstance().getProperty("zoho.sign.requests")+"/"+requestId);
		getMethod.setHeader("Authorization","Zoho-oauthtoken "+ accessToken);
		HttpResponse response;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			response = httpClient.execute(getMethod);
			String responseJSON = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject submitResponse = new JSONObject(responseJSON);
			return submitResponse;
		} catch (ClientProtocolException e) {
			throw new APIRequestException(I18NConfig.getMessage("error.unkown_issue"));
		} catch (IOException e) {
			throw new APIRequestException(I18NConfig.getMessage("error.unkown_issue"));
		}
	}
	
	/**
	 * Creates the sign request.
	 *
	 * @param tenant the tenant
	 * @param lease the lease
	 * @param address the address
	 * @param propertyDetails the property details
	 * @param retry the retry
	 * @return the string
	 */
	public String createSignRequest(Tenant tenant, Lease lease, Address address, PropertyDetails propertyDetails, int retry) {
		JSONArray actions = new JSONArray();
		JSONObject actionJson = new JSONObject();
		//First recipient role - your internal user
		actionJson.put("action_type","SIGN");
		actionJson.put("recipient_email",Utils.getUserEmail());
		actionJson.put("recipient_name",Utils.getUserEmail().split("@")[0]);
		actionJson.put("action_id","16312000000011020");
		actionJson.put("verify_recipient", false);   
		actionJson.put("signing_order", 1);   
		actionJson.put("role", "owner");   
		actions.put(actionJson);
		
		actionJson = new JSONObject();
		actionJson.put("action_type","SIGN");
		actionJson.put("recipient_email", tenant.getEmail());
		actionJson.put("recipient_name", tenant.getFirstname()+" "+tenant.getLastname());
		actionJson.put("action_id","16312000000011026");
		actionJson.put("verify_recipient", false);   
		actionJson.put("signing_order", 2);   
		actionJson.put("role", "tenant");   
		actions.put(actionJson);
		
		
		JSONObject fieldTextData=new JSONObject();
		fieldTextData.put("address_1", address.getAddressline_1());
		fieldTextData.put("address_2", address.getAddressline_2());
		fieldTextData.put("address_3", address.getPostal()+", "+address.getCity());
		fieldTextData.put("area", propertyDetails.getArea()+" "+I18NConfig.getMessage("lease.squarearea"));
		fieldTextData.put("firstname", tenant.getFirstname());
		fieldTextData.put("lastname", tenant.getLastname());
		fieldTextData.put("dob", tenant.getDob());
		fieldTextData.put("nationality", I18NConfig.getMessage(tenant.getNationality()));
		fieldTextData.put("movein", Utils.getDate(lease.getMovein()));
		fieldTextData.put("moveout", Utils.getDate(lease.getMoveout()));
		fieldTextData.put("occupants", lease.getOccupants());
		fieldTextData.put("rent", lease.getRent()+" "+I18NConfig.getMessage("lease.monthrent"));
		
		JSONObject fieldData=new JSONObject();
		fieldData.put("field_text_data", fieldTextData);
		
		JSONObject templatesJson = new JSONObject();
		templatesJson.put("actions", actions);
		templatesJson.put("field_data", fieldData);
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("templates", templatesJson);
		
		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		reqEntity.addTextBody("data",dataJson.toString());
		reqEntity.addTextBody("is_quicksend", "true"); 
		HttpEntity multipart = reqEntity.build();
		
		HttpPost postMethod = new HttpPost(ApplicationProperties.getInstance().getProperty("zoho.sign.template"));
		postMethod.setHeader("Authorization","Zoho-oauthtoken "+ this.accessToken);
		postMethod.setEntity(multipart);
		
		HttpResponse response;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			response = httpClient.execute(postMethod);
			
			String responseJSON = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject submitResponse = new JSONObject(responseJSON);
			if(submitResponse.has("status") && submitResponse.getString("status").equals("success")){
				JSONObject responseObj=submitResponse.getJSONObject("requests");
				return responseObj.get("request_id").toString();    
			}else if(response.getStatusLine().getStatusCode()==401 && retry==0) {
				generateAccessToken();
				return createSignRequest(tenant, lease, address, propertyDetails, 1);
			}
		} catch (IOException e) {
			throw new APIRequestException(I18NConfig.getMessage("error.unkown_issue"));
		}
		
		return null;
	}
}
