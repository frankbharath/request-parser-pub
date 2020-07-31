package com.bharath.rm.constants.tables;

import java.util.ArrayList;
import java.util.List;

import com.bharath.rm.constants.tables.RM_Users.Columns;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 27, 2020 9:32:48 PM
 	* Class Description
*/
public enum RM_Stripeusers implements Table {
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
		STRIPEID{
			@Override
			public String getColumnName() {
				return this.name().toLowerCase();
			}
		},
		REFRESHTOKEN{
			@Override
			public String getColumnName() {
				return this.name().toLowerCase();
			}
		},
		ACCESSTOKEN{
			@Override
			public String getColumnName() {
				return this.name().toLowerCase();
			}
		},
		PUBLISHABLEKEY{
			@Override
			public String getColumnName() {
				return this.name().toLowerCase();
			}
		}
	}
}
