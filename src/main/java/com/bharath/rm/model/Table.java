package com.bharath.rm.model;

import java.util.List;

/**
 * The Class Table.
 *
 * @author frank
 * 
 * This class contains all necessary attribute to create a table such as table name, columns and constraints such as primary key, foreign key.
 * Builder is used to create table object and to avoid telescope constructors
 */
public class Table {
	
	/** The table name. */
	private String tableName;
	
	/** The columns. */
	private List<Column> columns;
	
	/** The primary keys. */
	private List<String> primaryKeys;
	
	/** The foreign keys. */
	private List<ForeignKey> foreignKeys;
	
	/** The inherit tables. */
	private List<String> inheritTables;
	
	/**
	 * Gets the table name.
	 *
	 * @return the table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Gets the columns.
	 *
	 * @return the columns
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * Gets the primary keys.
	 *
	 * @return the primary keys
	 */
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

	/**
	 * Gets the foreign keys.
	 *
	 * @return the foreign keys
	 */
	public List<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}
	
	/**
	 * Gets the inherit tables.
	 *
	 * @return the inherit tables
	 */
	public List<String> getInheritTables() {
		return inheritTables;
	}

	/**
	 * Instantiates a new table.
	 *
	 * @param tableBuilder the table builder
	 */
	private Table(TableBuilder tableBuilder) {
		String error=null;
		if(tableBuilder.getTableName()==null) {
			error="Table Name not found";
		}else if(tableBuilder.getColumns()==null || tableBuilder.getColumns().isEmpty()) {
			error="There are no columns found for "+tableBuilder.getTableName();
		}
		if(error!=null) {
			throw new NullPointerException(error);
		}
		this.tableName=tableBuilder.getTableName();
		this.columns=tableBuilder.getColumns();
		this.primaryKeys=tableBuilder.getPrimaryKeys();
		this.foreignKeys=tableBuilder.getForeignKeys();
		this.inheritTables=tableBuilder.getInheritTables();
	}
	
	/**
	 * The Class TableBuilder.
	 */
	public static class TableBuilder {
		
		/** Holds the table name. */
		private String tableName;
		
		/** Holds all the columns. */
		private List<Column> columns;
		
		/** Holds all the primary keys, if any. */
		private List<String> primaryKeys;
		
		/** Holds all the foreign keys, if any. */
		private List<ForeignKey> foreignKeys;
		
		/**  Hold all the tables needs to be inherited. */
		private List<String> inheritTables;
		/**
		 * Gets the table name.
		 *
		 * @return the table name
		 */
		public String getTableName() {
			return tableName;
		}
		
		/**
		 * Sets the table name.
		 *
		 * @param tableName the table name
		 * @return the table builder
		 */
		public TableBuilder setTableName(String tableName) {
			this.tableName = tableName;
			return this;
		}
		
		/**
		 * Gets the columns.
		 *
		 * @return the columns
		 */
		public List<Column> getColumns() {
			return columns;
		}
		
		/**
		 * Sets the columns.
		 *
		 * @param columns the columns
		 * @return the table builder
		 */
		public TableBuilder setColumns(List<Column> columns) {
			this.columns = columns;
			return this;
		}
		
		/**
		 * Gets the primary keys.
		 *
		 * @return the primary keys
		 */
		public List<String> getPrimaryKeys() {
			return primaryKeys;
		}
		
		/**
		 * Sets the primary key.
		 *
		 * @param primaryKeys the primary keys
		 * @return the table builder
		 */
		public TableBuilder setPrimaryKey(List<String> primaryKeys) {
			this.primaryKeys = primaryKeys;
			return this;
		}
		
		/**
		 * Gets the foreign keys.
		 *
		 * @return the foreign keys
		 */
		public List<ForeignKey> getForeignKeys() {
			return foreignKeys;
		}
		
		/**
		 * Sets the foreign keys.
		 *
		 * @param foreignKeys the foreign keys
		 * @return the table builder
		 */
		public TableBuilder setForeignKeys(List<ForeignKey> foreignKeys) {
			this.foreignKeys = foreignKeys;
			return this;
		}
		
		/**
		 * Gets the inherit tables.
		 *
		 * @return the inheritTables
		 */
		public List<String> getInheritTables() {
			return inheritTables;
		}

		/**
		 * Sets the inherit tables.
		 *
		 * @param inheritTables the inheritTables to set
		 */
		public void setInheritTables(List<String> inheritTables) {
			this.inheritTables = inheritTables;
		}

		/**
		 * Builds the.
		 *
		 * @return the table
		 */
		public Table build() {
			return new Table(this); 
		}
	}

}
