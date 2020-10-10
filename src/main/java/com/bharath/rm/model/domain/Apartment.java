package com.bharath.rm.model.domain;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Aug 11, 2020 3:15:57 PM
 	* Class Description
*/

import java.util.List;

/**
 * The Class Appartment.
 */
public class Apartment extends Property{
	
	/** The units. */
	private List<ApartmentPropertyDetails> units;

	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	public List<ApartmentPropertyDetails> getUnits() {
		return units;
	}

	/**
	 * Sets the units.
	 *
	 * @param units the units to set
	 */
	public void setUnits(List<ApartmentPropertyDetails> units) {
		this.units = units;
	}

	
}
