package com.bharath.rm.configuration;


import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bharath.rm.common.Utils;
import com.bharath.rm.constants.CreateTablesServiceConstants;
import com.bharath.rm.dao.CreateTablesDAO;
import com.bharath.rm.model.Column;
import com.bharath.rm.model.ForeignKey;
import com.bharath.rm.model.Table;

/**
 * @author bharath
 * @version 1.0
 * Creation time: Jun 10, 2020 12:00:10 AM
 * This class helps to create database table objects that are read from data-dictionary.xml
 */
public final class CreateTableConfig extends DefaultHandler{
	
	/** The logger will be used to log information such as exceptions, info or debug **/
	private static final Logger log = LoggerFactory.getLogger(CreateTableConfig.class);

	/** The data-dictionary.xml is located in the /src/main/resources. */
	private static final String DATA_DICTIONARY="data-dictionary.xml";
	
	/** The tables. */
	final private List<Table> tables=new ArrayList<>();
	
	/** The table builder. */
	private Table.TableBuilder tableBuilder;
	
	/** The columns. */
	private List<Column> columns;
	
	/** The primary keys. */
	private List<String> primaryKeys;
	
	/** The foreign keys. */
	private List<ForeignKey> foreignKeys;
	
	final private List<String> insertDefaultValues=new ArrayList<>();
	
	private StringBuilder insertQuery;
	/**
	 * This method will called when XML elements are opened such as <table>, <column>, etc. Make sure to initialize corresponding objects. 
	 *
	 * @param uri the uri
	 * @param localName the local name
	 * @param qName the q name
	 * @param attributes the attributes
	 * @throws SAXException the SAX exception
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(CreateTablesServiceConstants.TABLE.equals(qName)) {
			log.info("Creating table object for "+attributes.getValue(CreateTablesServiceConstants.NAME));
			tableBuilder=new Table.TableBuilder();
			tableBuilder.setTableName(attributes.getValue(CreateTablesServiceConstants.NAME));
			if(attributes.getValue(CreateTablesServiceConstants.INHERITS)!=null) {
				tableBuilder.setInheritTables(Arrays.asList(attributes.getValue(CreateTablesServiceConstants.INHERITS).split(",")));
			}
		}else if(CreateTablesServiceConstants.COLUMNS.equals(qName)) {
			columns=new ArrayList<>();
			primaryKeys=new ArrayList<>();
			foreignKeys=new ArrayList<>();
		}else if(CreateTablesServiceConstants.COLUMN.equals(qName)) {
			Column.ColumnBuilder builder=new Column.ColumnBuilder().setColumnName(attributes.getValue(CreateTablesServiceConstants.NAME))
			.setType(attributes.getValue(CreateTablesServiceConstants.TYPE))
			.setIsNull(Boolean.parseBoolean(attributes.getValue(CreateTablesServiceConstants.NULL)));
			if(attributes.getValue(CreateTablesServiceConstants.ALLOWEDVALUES)!=null) {
				builder.setAllowedValues(attributes.getValue(CreateTablesServiceConstants.ALLOWEDVALUES));
			}
			if(attributes.getValue(CreateTablesServiceConstants.UNIQUEKEY)!=null) {
				builder.setUnique(Boolean.parseBoolean(attributes.getValue(CreateTablesServiceConstants.UNIQUEKEY)));
			}
			columns.add(builder.build());
			if(attributes.getValue(CreateTablesServiceConstants.PRIMARYKEY)!=null) {
				primaryKeys.add(attributes.getValue(CreateTablesServiceConstants.NAME));
			}
			
			if(attributes.getValue(CreateTablesServiceConstants.FOREIGNKEY)!=null) {
				ForeignKey.ForeignKeyBuilder foreignKeyBuilder=new ForeignKey.ForeignKeyBuilder().setColumnName(attributes.getValue(CreateTablesServiceConstants.NAME))
				.setReferenceTable(attributes.getValue(CreateTablesServiceConstants.REFERENCETABLE))
				.setReferenceColumn(attributes.getValue(CreateTablesServiceConstants.REFERENCECOLUMN));
				if(attributes.getValue(CreateTablesServiceConstants.DELETECASCADE)!=null) {
					foreignKeyBuilder.setDeleteCascade(Boolean.parseBoolean(attributes.getValue(CreateTablesServiceConstants.DELETECASCADE)));
				}
				foreignKeys.add(foreignKeyBuilder.build());
			}
		}else if(CreateTablesServiceConstants.DEFAULTVALUE.equals(qName)) {
			insertQuery=new StringBuilder("INSERT INTO ").append(attributes.getValue(CreateTablesServiceConstants.TABLE));
		}else if(CreateTablesServiceConstants.DEFAULTROW.equals(qName)) {
			int len=attributes.getLength();
			List<String> columns=new ArrayList<>();
			List<String> values=new ArrayList<>();
			for(int i=0;i<len;i++) {
				columns.add(attributes.getQName(i));
				values.add("'"+attributes.getValue(i)+"'");
			}
			StringBuilder row=new StringBuilder(insertQuery);
			row.append("(").append(String.join(",", columns)).append(") VALUES(").append(String.join(",", values)).append(")");
			row.append(" ON CONFLICT DO NOTHING");
			insertDefaultValues.add(row.toString());
		}
	}
	
	/**
	 * This method will be called once when element is parsed. When the table element is full parsed, all the objects belonging to the table will be added.
	 *
	 * @param uri the uri
	 * @param localName the local name
	 * @param qName the q name
	 * @throws SAXException the SAX exception
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(CreateTablesServiceConstants.TABLE.equals(qName)) {
			tableBuilder.setColumns(columns).setForeignKeys(foreignKeys).setPrimaryKey(primaryKeys);
			tables.add(tableBuilder.build());
			log.info("Finised creating table object for "+tableBuilder.getTableName());
		}
	}
	
	/**
	 * This method reads the data-dictionary.xml and passes the input stream to CreateTableServiceHandler which will create table
	 * objects. The XML file is read using SAXparser as it does not load the entire XML file in the memory. 
	 *
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 * @throws SQLException the SQL exception
	 * @throws IOException 
	 */
	public static void init()  {
		try {
			log.info("Updating tables to the database");
			final ClassLoader classLoader = Utils.classloader();
			final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	        try (InputStream inputStream = classLoader.getResourceAsStream(DATA_DICTIONARY)) {
	        	final SAXParser saxParser = saxParserFactory.newSAXParser();
	        	final CreateTableConfig handler=new CreateTableConfig();
	        	saxParser.parse(inputStream, handler);
	        	final List<Table> tables=handler.tables;
	        	final String[] createStatements=getCreateStatementforTables(tables);
	        	CreateTablesDAO dao=new CreateTablesDAO();
	        	dao.addTablestoDB(createStatements);
	        	dao.insertDefaultValuestoTable(handler.insertDefaultValues);
	        	dao.close();
	        	log.info("Finised updating tables to the database");
	        }catch (ParserConfigurationException | SAXException | SQLException | IOException e) {
				log.error("Problem when trying to create tables in the database", e);
			}
		}catch (Exception e) {
			log.error("Exception", e);
		}
	}
	
	public static String[] getCreateStatementforTables(List<Table> tables) {
		String[] statements=new String[tables.size()];
		int c=0;
		for(Table table:tables) {
			StringBuilder builder=new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(table.getTableName()).append("(");
			int colSize=table.getColumns().size();
			for(int i=0;i<colSize;i++) {
				Column column=table.getColumns().get(i);
				builder.append(column.getColumnName()).append(" ").append(column.getType());
				if(!column.isNull()) {
					builder.append(" NOT NULL");
				}
				if(column.getAllowedValues()!=null) {
					String[] values=column.getAllowedValues().split(",");
					ArrayList<String> valueList = new ArrayList<String>();
			        for (String value:values) {
			        	valueList.add("'" + value + "'"); 
			        }
					builder.append(" CHECK (").append(column.getColumnName()).append(" IN (").append(String.join(",", valueList)).append("))");
				}
				if(column.isUnique()) {
					builder.append(" UNIQUE ");
				}
				if(i!=colSize-1) {
					builder.append(",");
				}				
			}
			if(!table.getPrimaryKeys().isEmpty()) {
				builder.append(",PRIMARY KEY(").append(String.join(",", table.getPrimaryKeys())).append(")");
			}
			
			if(!table.getForeignKeys().isEmpty()) {
				builder.append(",");
				int fSize=table.getForeignKeys().size();
				for(int i=0;i<fSize;i++) {
					ForeignKey foreignKey=table.getForeignKeys().get(i);
					builder.append("FOREIGN KEY (").append(foreignKey.getColumnName()).append(")")
					.append(" REFERENCES ").append(foreignKey.getReferenceTable()).append("(").append(foreignKey.getReferenceColumn()).append(")");
					if(foreignKey.isDeleteCascade()) {
						builder.append(" ON DELETE CASCADE");
					}
					if(i!=fSize-1) {
						builder.append(",");
					}
				}
			}
			builder.append(")");
			if(table.getInheritTables()!=null && !table.getInheritTables().isEmpty()) {
				builder.append(" INHERITS(").append(String.join(",", table.getInheritTables())).append(")");
			}
			statements[c]=builder.toString();
			c++;
		}
		return statements;
	}
	
	/*public static String constructInsertQuery(Table table, List<Column> columnNames) {
		List<String> cols=columnNames.stream().map(c->c.getColumnName()).collect(Collectors.toList());
		StringBuilder query = new StringBuilder("INSERT INTO ").append(table.getTableName())
				.append(" (").append(String.join(",", cols)).append(") VALUES (")
				.append(Stream.generate(() -> "?").limit(columnNames.size()).collect(Collectors.joining(","))).append(")");
		return query.toString();
	}
	
	public static String getSelectQuery(Table table, List<Column> columnNames) {
		List<String> cols=columnNames.stream().map(c->c.getColumnName()).collect(Collectors.toList());
		StringBuilder query = new StringBuilder("SELECT ").append(String.join(", ", cols)).append(" FROM ").append(table.getTableName());
		return query.toString();
	}*/
}
