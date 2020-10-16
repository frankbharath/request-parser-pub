package com.bharath.rm.dto;

import java.util.List;

/**
 * The Class ApartmentDTO.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Sep 19, 2020 12:36:01 AM
 * 
 */
public class ApartmentDTO extends PropertyDTO{
	
	/** The units. */
	private List<ApartmentPropertyDetailDTO> units;

	/**
	 * Gets the units.
	 *
	 * @return the list
	 */
	public List<ApartmentPropertyDetailDTO> getUnits() {
		return units;
	}

	/**
	 * Sets the units.
	 *
	 * @param units the new units
	 */
	public void setUnits(List<ApartmentPropertyDetailDTO> units) {
		this.units = units;
	}
}
