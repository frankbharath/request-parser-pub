package com.bharath.rm.constants.tables;

import java.util.List;

/**
 * The Interface Table.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 30, 2020 2:21:07 PM
 * Class Description
 */
public interface Table {
	
	/**
	 * Gets the table name.
	 *
	 * @return the table name
	 */
	public String getTableName();
	
	/**
	 * Gets the all columns.
	 *
	 * @return the all columns
	 */
	public List<Column> getAllColumns();
}
