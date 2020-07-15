package com.bharath.rm.constants.tables;

import java.util.ArrayList;
import java.util.List;


/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 10, 2020 8:20:46 PM
 	* Class Description
*/
public enum RM_Users implements Table{
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
				return TABLE.getTableName()+"."+this.name().toLowerCase();
			}
		},
		EMAIL{
			@Override
			public String getColumnName() {
				return TABLE.getTableName()+"."+this.name().toLowerCase();
			}
		},
		PASSWORD{
			@Override
			public String getColumnName() {
				return TABLE.getTableName()+"."+this.name().toLowerCase();
			}
		},
		CREATIONTIME{
			@Override
			public String getColumnName() {
				return TABLE.getTableName()+"."+this.name().toLowerCase();
			}
		},
		VERIFIED{
			@Override
			public String getColumnName() {
				return TABLE.getTableName()+"."+this.name().toLowerCase();
			}
		}
		
	}

}
