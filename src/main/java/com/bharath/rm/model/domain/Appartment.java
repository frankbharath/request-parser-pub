package com.bharath.rm.model.domain;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Aug 11, 2020 3:15:57 PM
 	* Class Description
*/

import java.util.List;

public class Appartment extends Property{
	private List<AppartmentPropertyDetails> list;

	/**
	 * @return the list
	 */
	public List<AppartmentPropertyDetails> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<AppartmentPropertyDetails> list) {
		this.list = list;
	}
}
