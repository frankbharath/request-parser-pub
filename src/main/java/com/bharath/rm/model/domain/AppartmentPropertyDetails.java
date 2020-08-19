package com.bharath.rm.model.domain;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Aug 11, 2020 3:13:29 PM
 	* Class Description
*/
public class AppartmentPropertyDetails extends PropertyDetails {
	private String doorno;
	private int floorno;
	/**
	 * @return the doorno
	 */
	public String getDoorno() {
		return doorno;
	}
	/**
	 * @param doorno the doorno to set
	 */
	public void setDoorno(String doorno) {
		this.doorno = doorno;
	}
	/**
	 * @return the floorno
	 */
	public int getFloorno() {
		return floorno;
	}
	/**
	 * @param floorno the floorno to set
	 */
	public void setFloorno(int floorno) {
		this.floorno = floorno;
	}
}
