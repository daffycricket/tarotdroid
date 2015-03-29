package org.nla.tarotdroid.lib.helpers;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.OutputKeys;
//import android.os.Environment;

public class DatabaseHelper
{
	private static final String STR_DB = "database";
	private static final String STR_TABLE = "table";
	private static final String STR_ROW = "row";
	private static final String STR_COL = "col";
	private static final String STR_NAME = "name";
	
	private static final String STR_MASTER_QUERY = "SELECT * FROM sqlite_master where type='table'";
	
	private SQLiteDatabase database;
	private Exporter exporter;
	private Importer importer;
	private StringBuilder logBuffer;
	private Map<String, List<String>> databaseSchema;

	public DatabaseHelper(Context context, SQLiteDatabase database)
	{
		this.database = database;
		this.logBuffer = new StringBuilder();
		this.databaseSchema = new HashMap<String, List<String>>();
		this.loadDatabaseSchema();
	}

	public String exportContent() throws ExportException
	{
		this.logBuffer = new StringBuilder();
		try
		{
			this.exporter = new Exporter();
		}
		catch (Exception e) {
			log("Error when creating Exporter [" + e + "]");
			e.printStackTrace();
			throw new ExportException(e);
		}
		
		try
		{
			return this.exporter.exportContent();
		}
		catch (Exception e) {
			log("Error when exporting database content [" + e + "]");
			e.printStackTrace();
			throw new ExportException(e);
		}
	}
	
	public void importContent(String xmlContent) throws ImportException
	{
		this.logBuffer = new StringBuilder();
		try
		{
			this.importer = new Importer(xmlContent);
			this.importer.importContent();
		}
		catch (Exception e) {
			log("Error when importing database [" + e + "]");
			e.printStackTrace();
			throw new ImportException(e);
		}
	}
	
	private void loadDatabaseSchema()
	{
		Cursor cursor = database.rawQuery(STR_MASTER_QUERY, null);
        cursor.moveToFirst();
        while (cursor.getPosition() < cursor.getCount())
        {
        	String tableName = cursor.getString(cursor.getColumnIndex(STR_NAME));
	        if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence"))
	        {
	        	List<String> tableColumns = new ArrayList<String>();
	        	this.databaseSchema.put(tableName, tableColumns);
	        	
	        	Cursor columnCursor = database.rawQuery("PRAGMA table_info(" + tableName + ")", null);
	        	columnCursor.moveToFirst();
	        	while (columnCursor.getPosition() < columnCursor.getCount())
		        {
	        		// [cid, name, type, notnull, dflt_value, pk]
	        		// 1 correspond ? name
	        		tableColumns.add(columnCursor.getString(1));
	        		columnCursor.moveToNext();
		        }
	        	columnCursor.close();
	        }
	        cursor.moveToNext();
        }
        cursor.close();
	}
	
	public String getLogBuffer()
	{
		return this.logBuffer.toString();
	}

	class Exporter
	{
		private Document xmlDoc;
		
		private Element dbElement;

		public Exporter() throws Exception 
		{
			this.xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		}
		
		public String exportContent() throws Exception
		{
			log("Start exporting database [" + database.getPath() + "]");
			
			this.dbElement = this.xmlDoc.createElement(STR_DB);
			this.dbElement.setAttribute(STR_NAME, database.getPath());
			this.xmlDoc.appendChild(this.dbElement);
			
	        Cursor cursor = database.rawQuery(STR_MASTER_QUERY, null);
	        
	        cursor.moveToFirst();
	        while (cursor.getPosition() < cursor.getCount())
	        {
	        	String tableName = cursor.getString(cursor.getColumnIndex(STR_NAME));
		        if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence"))
		        {
			        this.exportTable(tableName);
		        }
		        else
		        {
		        	log("Skipped table [" + tableName  + "]");
		        }
		        cursor.moveToNext();
	        }
	        cursor.close();
			
			log("Done exporting database [" + database.getPath() + "]");
			return transformDocumentToXmlString();
		}
		
		private String transformDocumentToXmlString() throws Exception
		{
			String xmlOutput = null;

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(this.xmlDoc);
			transformer.transform(source, result);
			xmlOutput = result.getWriter().toString();
			return xmlOutput;
		}
		
		private void exportTable(String tableName) throws Exception
		{
			Cursor tableCursor = database.rawQuery("select * from " + tableName, null);
			int numcols = tableCursor.getColumnCount();
			
			StringBuilder rowsColsBuffer = new StringBuilder();
			rowsColsBuffer.append(tableCursor.getCount());
			rowsColsBuffer.append(" rows ");
			rowsColsBuffer.append(numcols);
			rowsColsBuffer.append(" columns ");
			rowsColsBuffer.append(databaseSchema.get(tableName));

			log("Start exporting table [" + tableName + "] " + rowsColsBuffer.toString());

			Element tableElement = this.xmlDoc.createElement(STR_TABLE);
			tableElement.setAttribute(STR_NAME, tableName);
			this.dbElement.appendChild(tableElement);
			
			tableCursor.moveToFirst();
			while(tableCursor.getPosition() < tableCursor.getCount())
			{
				StringBuilder rowBuffer = new StringBuilder();
				Element rowElement = this.xmlDoc.createElement(STR_ROW);
				for(int idx = 0; idx < numcols; idx++)
				{
					String name = tableCursor.getColumnName(idx);
					String val = tableCursor.getString(idx);
					
					rowBuffer.append(name);
					rowBuffer.append("=");
					rowBuffer.append(val);
					if (idx != numcols - 1)
					{
						rowBuffer.append(" ");
					}
					
					Element colElement = this.xmlDoc.createElement(STR_COL);
					colElement.setAttribute(STR_NAME, name);
					colElement.setTextContent(val);
					
					rowElement.appendChild(colElement);
				}
				log("Exported row [" + rowBuffer + "]");
				tableElement.appendChild(rowElement);
				tableCursor.moveToNext();
			}

			tableCursor.close();
			
			log("Done exporting table [" + tableName + "]");
		}
	}

	class Importer
	{
		private Document xmlDoc;
		
		private Map<String, List<ContentValues>> content;
		
		public Importer(String xml) throws Exception
		{
			this.xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
			this.content = new HashMap<String, List<ContentValues>>();
		}
		
		private void clearTables()
		{
			log("Start deleting database content");
			for (String tableName : databaseSchema.keySet())
			{
				log("Start deleting content for table [" + tableName + "]");
				int deletedRows = database.delete(tableName, "1", null);
				log("Deleted " + deletedRows + " rows on table [" + tableName + "]");
			}
			log("Done deleting database content");
		}
		
		public void importContent() throws ImportException
		{
			log("Start importing content");
			this.loadContentIntoMemory();
			if (this.checkDatabaseCompatibility())
			{
				database.beginTransaction();
				try {
					this.clearTables();
					this.populateDatabase();
					database.setTransactionSuccessful();
				}
				catch (Exception e) {
					database.endTransaction();
					log("Content import stopped unexpectedly, " + e );
					throw new ImportException(e);
				}
				database.endTransaction();
			}
			log("Done importing content");
		}
		
		private boolean checkDatabaseCompatibility()
		{
			log("Start checking database compatibility");
			
			// check there aren't more tables on target device than in xml
			boolean moreTableOnTargetDeviceThanSuppliedInXml = databaseSchema.keySet().size() > this.content.size();
			
			// check at least all tables on device are supplied in xml
			boolean areAllTablesOnTargetDeviceSuppliedInXml = databaseSchema.keySet().containsAll(this.content.keySet());
			
			log("Tables on target device " + databaseSchema.keySet());
			log("Tables to import " + this.content.keySet());
			log("More tables on target device ? " + moreTableOnTargetDeviceThanSuppliedInXml);
			log("All target tables are supplied ? " + areAllTablesOnTargetDeviceSuppliedInXml);
			
			if (moreTableOnTargetDeviceThanSuppliedInXml || !areAllTablesOnTargetDeviceSuppliedInXml)
			{
				log("Done checking database compatibility [KO]");
				return false;
			}
			else
			{
				log("Done checking database compatibility [OK]");
				return true;
			}
		}
		
		private void populateDatabase()
		{
			log("Start populating database");
			for (String tableName : this.content.keySet())
			{
				this.populateTable(tableName);
			}
			log("Done populating database");
		}
		
		private void populateTable(String tableName)
		{
			StringBuilder colsBuffer = new StringBuilder();
			colsBuffer.append(databaseSchema.get(tableName).size());
			colsBuffer.append(" columns ");
			colsBuffer.append(databaseSchema.get(tableName));

			log("Start populating table [" + tableName + "] " + colsBuffer.toString());
			
			for (ContentValues rowValues : this.content.get(tableName))
			{
				try
				{
					log("Inserting into [" + tableName + "] row [" + rowValues + "]");
					database.insertOrThrow(tableName, null, rowValues);
				}
				catch (SQLException e) {
					e.printStackTrace();
					log("Error inserting row [" + rowValues + "] into table [" + tableName + "], " + e );
				}
			}
			log("Done populating table [" + tableName + "]");
		}
		
		private void loadContentIntoMemory()
		{
			log("Start caching content");
			Element databaseElement = xmlDoc.getDocumentElement();
			if (!STR_DB.equals(databaseElement.getNodeName())) 
			{
				throw new IllegalArgumentException("Illegal root name [" + databaseElement.getNodeName() + "]");
			}
			
			NodeList tableNodes = databaseElement.getElementsByTagName(STR_TABLE);
			for (int i = 0; i < tableNodes.getLength(); ++i)
			{
				this.loadTableContentIntoMemory((Element)tableNodes.item(i));
			}
			log("Done caching content");
		}
		
		private void loadTableContentIntoMemory(Element tableElement)
		{
			String tableName = tableElement.getAttribute(STR_NAME);
			log("Start caching content for table [" + tableName + "]");
			
			if (!databaseSchema.containsKey(tableName))
			{
				log("Target database doesn't have table [" + tableName + "] Table skipped");
			}
			else 
			{
				List<ContentValues> tableContentValues = new ArrayList<ContentValues>();
				NodeList rowNodes = tableElement.getElementsByTagName(STR_ROW);
				
				for (int r = 0; r < rowNodes.getLength(); ++r)
				{
					Element rowElement = (Element)rowNodes.item(r);
					NodeList colNodes = rowElement.getElementsByTagName(STR_COL);
					
					ContentValues rowValues = new ContentValues();
					for (int c = 0; c < colNodes.getLength(); ++c)
					{
						Element colElement = (Element)colNodes.item(c);
						String colName = colElement.getAttribute(STR_NAME);
						String colValue = colElement.getTextContent();
						
						if (!databaseSchema.get(tableName).contains(colName))
						{
							log("Target table [" + tableName + "] doesn't have column [" + colName + "] Column skipped");
						}
						else
						{
							rowValues.put(colName, colValue);
						}
					}
					
					tableContentValues.add(rowValues);
				}
				
				this.content.put(tableName, tableContentValues);
			}
			
			log("Done caching content for table [" + tableName + "]");
		}
	}
	
	private void log(String msg)
	{
//		Log.d("DatabaseAssistant", msg);
		this.logBuffer.append(msg);
		this.logBuffer.append("\n");
	}
}