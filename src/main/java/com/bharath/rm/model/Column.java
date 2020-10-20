package com.bharath.rm.model;

/**
 * The Class Column.
 */
public class Column {
	
	/** The column name. */
	private String columnName;
	
	/** The type. */
	private String type;
	
	/** The is null. */
	private boolean isNull;
	
	/** The is unique. */
	private boolean isUnique;
	
	/** The allowed values. */
	private String allowedValues;
	
	/** The default value. */
	private String defaultValue; 
	
	/**
	 * Gets the column name.
	 *
	 * @return the column name
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Checks if is null.
	 *
	 * @return true, if is null
	 */
	public boolean isNull() {
		return isNull;
	}
	
	/**
	 * Gets the allowed values.
	 *
	 * @return the allowed values
	 */
	public String getAllowedValues() {
		return allowedValues;
	}

	/**
	 * Checks if is unique.
	 *
	 * @return true, if is unique
	 */
	public boolean isUnique() {
		return isUnique;
	}

	/**
	 * Gets the default value.
	 *
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the default value.
	 *
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Instantiates a new column.
	 *
	 * @param columnBuilder the column builder
	 */
	private Column(ColumnBuilder columnBuilder) {
		String error=null;
		if(columnBuilder.getColumnName()==null) {
			error="Column Name not found";
		}else if(columnBuilder.getType()==null) {
			error="The column type is not associated for the column "+columnBuilder.getType();
		}
		if(error!=null) {
			throw new NullPointerException(error);
		}
		this.columnName=columnBuilder.getColumnName();
		this.type=columnBuilder.getType();
		this.isNull=columnBuilder.getIsNull();
		this.allowedValues=columnBuilder.getAllowedValues();
		this.isUnique=columnBuilder.isUnique();
		this.defaultValue=columnBuilder.getDefaultValue();
	}

	/**
	 * The Class ColumnBuilder.
	 */
	public static class ColumnBuilder {
		
		/** Holds the column name. */
		private String columnName;
		
		/** Holds the column type. */
		private String type;
		
		/**  Holds boolean value, to check whether column can be nullable. */
		private boolean isNull;
		
		/**  Holds allowed values for the column. */
		private String allowedValues;
		
		/** The is unique. */
		private boolean isUnique;
		
		/** The default value. */
		private String defaultValue; 
		
		/**
		 * Gets the column name.
		 *
		 * @return the column name
		 */
		
		public String getColumnName() {
			return columnName;
		}
		
		/**
		 * Sets the column name.
		 *
		 * @param columnName the column name
		 * @return the column builder
		 */
		public ColumnBuilder setColumnName(String columnName) {
			this.columnName = columnName;
			return this;
		}
		
		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		
		/**
		 * Sets the type.
		 *
		 * @param type the type
		 * @return the column builder
		 */
		public ColumnBuilder setType(String type) {
			this.type = type;
			return this;
		}
		
		/**
		 * Gets the checks if is null.
		 *
		 * @return the checks if is null
		 */
		public boolean getIsNull() {
			return isNull;
		}
		
		/**
		 * Sets the is null.
		 *
		 * @param isNull the is null
		 * @return the column builder
		 */
		public ColumnBuilder setIsNull(boolean isNull) {
			this.isNull = isNull;
			return this;
		}
		
		/**
		 * Gets the allowed values.
		 *
		 * @return the allowed values
		 */
		public String getAllowedValues() {
			return allowedValues;
		}
		
		/**
		 * Sets the allowed values.
		 *
		 * @param allowedValues the allowed values
		 * @return the column builder
		 */
		public ColumnBuilder setAllowedValues(String allowedValues) {
			this.allowedValues = allowedValues;
			return this;
		}
		
		/**
		 * Checks if is unique.
		 *
		 * @return true, if is unique
		 */
		public boolean isUnique() {
			return isUnique;
		}

		/**
		 * Sets the unique.
		 *
		 * @param isUnique the new unique
		 */
		public void setUnique(boolean isUnique) {
			this.isUnique = isUnique;
		}

		/**
		 * Gets the default value.
		 *
		 * @return the defaultValue
		 */
		public String getDefaultValue() {
			return defaultValue;
		}

		/**
		 * Sets the default value.
		 *
		 * @param defaultValue the defaultValue to set
		 */
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		/**
		 * Builds the.
		 *
		 * @return the column
		 */
		public Column build() {
			return new Column(this);
		}
	}
}
