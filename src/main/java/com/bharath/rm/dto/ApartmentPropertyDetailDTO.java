package com.bharath.rm.dto;
// TODO: Auto-generated Javadoc

/**
 * The Class ApartmentPropertyDetailDTO.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 19, 2020 12:37:03 AM
 * Class Description
 */
public class ApartmentPropertyDetailDTO extends PropertyDetailsDTO{
	
	/** The doorno. */
	private String doorno;
	
	/** The floorno. */
	private Integer floorno;
	
	/**
	 * Gets the doorno.
	 *
	 * @return the doorno
	 */
	public String getDoorno() {
		return doorno;
	}
	
	/**
	 * Sets the doorno.
	 *
	 * @param doorno the doorno to set
	 */
	public void setDoorno(String doorno) {
		this.doorno = doorno;
	}
	
	/**
	 * Gets the floorno.
	 *
	 * @return the floorno
	 */
	public Integer getFloorno() {
		return floorno;
	}
	
	/**
	 * Sets the floorno.
	 *
	 * @param floorno the floorno to set
	 */
	public void setFloorno(Integer floorno) {
		this.floorno = floorno;
	}
}
