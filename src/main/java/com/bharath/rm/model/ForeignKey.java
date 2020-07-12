package com.bharath.rm.model;

public class ForeignKey {
	
	private String columnName;
	private String referenceTable;
	private String referenceColumn;
	private boolean deleteCascade;
	
	public String getColumnName() {
		return columnName;
	}

	public String getReferenceTable() {
		return referenceTable;
	}

	public String getReferenceColumn() {
		return referenceColumn;
	}

	public boolean isDeleteCascade() {
		return deleteCascade;
	}

	private ForeignKey(ForeignKeyBuilder foreignKeyBuilder) {
		String error=null;
		if(foreignKeyBuilder.getColumnName()==null) {
			error="Column Name not found";
		}else if(foreignKeyBuilder.getReferenceTable()==null) {
			error="The reference table is not associated for the column "+foreignKeyBuilder.getReferenceTable();
		}else if(foreignKeyBuilder.getReferenceColumn()==null) {
			error="The reference column is not associated for the column "+foreignKeyBuilder.getReferenceColumn();
		}
		if(error!=null) {
			throw new NullPointerException(error);
		}
		this.columnName=foreignKeyBuilder.getColumnName();
		this.referenceTable=foreignKeyBuilder.getReferenceTable();
		this.referenceColumn=foreignKeyBuilder.getReferenceColumn();
		this.deleteCascade=foreignKeyBuilder.isDeleteCascade();
	}
	
	public static class ForeignKeyBuilder {
		
		/** Holds the column name. */
		private String columnName;
		
		/** Holds the reference table name. */
		private String referenceTable;
		
		/** Holds the reference table column name. */
		private String referenceColumn;
		
		/** boolean value that dictates whether the child row should be deleted when parent row is deleted */
		private boolean deleteCascade;
		
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
		 * @return the foreign key builder
		 */
		public ForeignKeyBuilder setColumnName(String columnName) {
			this.columnName = columnName;
			return this;
		}
		
		/**
		 * Gets the reference table.
		 *
		 * @return the reference table
		 */
		public String getReferenceTable() {
			return referenceTable;
		}
		
		/**
		 * Sets the reference table.
		 *
		 * @param referenceTable the reference table
		 * @return the foreign key builder
		 */
		public ForeignKeyBuilder setReferenceTable(String referenceTable) {
			this.referenceTable = referenceTable;
			return this;
		}
		
		/**
		 * Gets the reference column.
		 *
		 * @return the reference column
		 */
		public String getReferenceColumn() {
			return referenceColumn;
		}
		
		/**
		 * Sets the reference column.
		 *
		 * @param referenceColumn the reference column
		 * @return the foreign key builder
		 */
		public ForeignKeyBuilder setReferenceColumn(String referenceColumn) {
			this.referenceColumn = referenceColumn;
			return this;
		}
		
		/**
		 * Checks if is delete cascade.
		 *
		 * @return true, if is delete cascade
		 */
		public boolean isDeleteCascade() {
			return deleteCascade;
		}
		
		/**
		 * Sets the delete cascade.
		 *
		 * @param deleteCascade the delete cascade
		 * @return the foreign key builder
		 */
		public ForeignKeyBuilder setDeleteCascade(boolean deleteCascade) {
			this.deleteCascade = deleteCascade;
			return this;
		}
		
		/**
		 * Builds the.
		 *
		 * @return the foreign key
		 */
		public ForeignKey build() {
			return new ForeignKey(this);
		}
	}

	
	@Override
	public String toString() {
		return "ForeignKey [columnName=" + columnName + ", referenceTable=" + referenceTable + ", referenceColumn="
				+ referenceColumn + ", deleteCascade=" + deleteCascade + "]";
	}
}
