package com.bharath.rm.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bharath.rm.common.Utils;

public class CreateTablesDAO {
	
	private static final Logger log = LoggerFactory.getLogger(CreateTablesDAO.class);
	
	private Connection connection=null;
	
	public CreateTablesDAO() throws SQLException, IOException {
		if(connection==null) {
			connection=Utils.getDBConnection();
			connection.setAutoCommit(false);
		}
	}
	
	public void addTablestoDB(String[] tables) throws SQLException, IOException  {
		Statement stmt = connection.createStatement();
		for(String table:tables) {
			stmt.addBatch(table);
		}
		stmt.executeBatch();
	}
	
	public void insertDefaultValuestoTable(List<String> insertQuery) throws SQLException, IOException  {
		Statement stmt = connection.createStatement();
		for(String insert:insertQuery) {
			stmt.addBatch(insert);
		}
		stmt.executeBatch();
	}
	
	public void close() throws SQLException {
		connection.commit();
		connection.close();
	}
	
}
