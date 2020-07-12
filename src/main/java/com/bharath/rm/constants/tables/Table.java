package com.bharath.rm.constants.tables;

import java.util.List;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 30, 2020 2:21:07 PM
 	* Class Description
*/
public interface Table {
	public String getTableName();
	public List<Column> getAllColumns();
}
