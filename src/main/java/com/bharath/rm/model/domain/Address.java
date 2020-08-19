package com.bharath.rm.model.domain;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Aug 11, 2020 2:43:55 PM
 	* Class Description
*/
public class Address {
	private String address_1;
	private String address_2;
	private String ville;
	private Long postal;
	/**
	 * @return the address_1
	 */
	public String getAddress_1() {
		return address_1;
	}
	/**
	 * @param address_1 the address_1 to set
	 */
	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}
	/**
	 * @return the address_2
	 */
	public String getAddress_2() {
		return address_2;
	}
	/**
	 * @param address_2 the address_2 to set
	 */
	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}
	/**
	 * @return the ville
	 */
	public String getVille() {
		return ville;
	}
	/**
	 * @param ville the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}
	/**
	 * @return the postal
	 */
	public Long getPostal() {
		return postal;
	}
	/**
	 * @param postal the postal to set
	 */
	public void setPostal(Long postal) {
		this.postal = postal;
	}
}
