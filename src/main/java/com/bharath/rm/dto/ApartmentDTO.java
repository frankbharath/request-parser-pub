package com.bharath.rm.dto;

import java.util.List;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Sep 19, 2020 12:36:01 AM
 	* Class Description
*/
public class ApartmentDTO extends PropertyDTO{
	
	private List<ApartmentPropertyDetailDTO> units;

	/**
	 * @return the list
	 */
	public List<ApartmentPropertyDetailDTO> getUnits() {
		return units;
	}

	/**
	 * @param list the list to set
	 */
	public void setUnits(List<ApartmentPropertyDetailDTO> units) {
		this.units = units;
	}
}
