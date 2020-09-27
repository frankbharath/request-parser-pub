package com.bharath.rm.dto;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 18, 2020 11:53:36 PM
 	* Class Description
*/
public class HouseDTO extends PropertyDTO {
	
	private PropertyDetailsDTO detailsDTO;

	/**
	 * @return the detailsDTO
	 */
	public PropertyDetailsDTO getDetailsDTO() {
		return detailsDTO;
	}

	/**
	 * @param detailsDTO the detailsDTO to set
	 */
	public void setDetailsDTO(PropertyDetailsDTO detailsDTO) {
		this.detailsDTO = detailsDTO;
	}
}
