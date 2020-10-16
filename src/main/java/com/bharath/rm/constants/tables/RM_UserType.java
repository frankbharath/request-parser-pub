package com.bharath.rm.constants.tables;

import java.util.ArrayList;
import java.util.List;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 10, 2020 8:29:33 PM
 	* This class represents rm_usertype table and its column names
*/
public enum RM_UserType implements Table{
	TABLE{
		@Override
		public String getTableName() {
			return this.getDeclaringClass().getSimpleName().toLowerCase();
		}
		@Override
		public List<Column> getAllColumns() {
			List<Column> cols=new ArrayList<>();
			for(Column col:Columns.values()) {
				cols.add(col);
			}
			return cols;
		}
	};
	public enum Columns implements Column{
		TYPEID{
			@Override
			public String getColumnName() {
				return this.name().toLowerCase();
			}
		},
		TYPE{
			@Override
			public String getColumnName() {
				return this.name().toLowerCase();
			}
		}
	}
}
