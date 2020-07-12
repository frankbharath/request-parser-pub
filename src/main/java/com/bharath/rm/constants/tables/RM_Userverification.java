package com.bharath.rm.constants.tables;

import java.util.ArrayList;
import java.util.List;

import com.bharath.rm.constants.tables.RM_Users.Columns;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 12, 2020 7:09:04 PM
 	* Class Description
*/
public enum RM_Userverification implements Table {

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
		USERID{
			@Override
			public String getColumnName() {
				return this.name().toLowerCase();
			}
		},
		TOKEN{
			@Override
			public String getColumnName() {
				return this.name().toLowerCase();
			}
		},
		CREATIONTIME{
			@Override
			public String getColumnName() {
				return this.name().toLowerCase();
			}
		}
	}
}