package com.bharath.rm.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.bharath.rm.common.Utils;

/**
 * The Class CreateTablesDAO.
 */
public class CreateTablesDAO {
	
	/** The connection. */
	private Connection connection=null;
	

	/** The Environment variable to read application properties.*/
	@Autowired
	private Environment env;
	
	/**
	 * Instantiates a new creates the tables DAO.
	 *
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public CreateTablesDAO() throws SQLException, IOException {
		if(connection==null) {
			connection=Utils.getDBConnection();
			connection.setAutoCommit(false);
		}
	}
	
	/**
	 * Adds the tables to DB.
	 *
	 * @param tables the tables
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void addTablestoDB(String[] tables) throws SQLException, IOException  {
		Statement stmt = connection.createStatement();
		for(String table:tables) {
			stmt.addBatch(table);
		}
		stmt.executeBatch();
	}
	
	/**
	 * Insert default values to table.
	 *
	 * @param insertQuery the insert query
	 * @throws SQLException the SQL exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void insertDefaultValuestoTable(List<String> insertQuery) throws SQLException, IOException  {
		Statement stmt = connection.createStatement();
		for(String insert:insertQuery) {
			stmt.addBatch(insert);
		}
		stmt.executeBatch();
	}
	
	/**
	 * Close.
	 *
	 * @throws SQLException the SQL exception
	 */
	public void close() throws SQLException {
		connection.commit();
		connection.close();
	}
	
}
