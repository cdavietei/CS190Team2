package com.finance.main.java.database;

import java.sql.*;
import java.io.*;
import com.finance.main.java.enums.Tables;

/**
 * Allows other classes to connected safely to the SQLite Database and execute
 * SQL queries. In order to use, a valid SQL query must be passed to either the
 * {@code executeQuery(String query)} method or the
 * {@code executeUpdate(String query)} method.
 * 
 * All SQL statements must be compatible with SQLite Database.
 *
 * @author Christopher Davie
 * @version 1.0
 */
public class SQLiteConnector
{
	/* Required FilePaths for the database to use */
	public static final String DB_NAME = "financial_workstation.db";
	public static final String RESOURCES = "Resources/";
	public static final String DEFAULT_DB_FILE = RESOURCES + "default.txt";
	

	/* Instance variables for the database connection */
	protected boolean connected = false;
	protected boolean dbExists = false;

	/* The Database connection object */
	protected Connection con;
	
	/**
	 * Default Constructor for the SQLiteConnector
	 * 
	 * @since 1.0
	 */
	public SQLiteConnector()
	{
		/* Checks to see if the database exists */
		dbExists = dbExists();
	}// SQLiteConnector()

	/**
	 * Checks if the database current exists as required by the program
	 * 
	 * @return True if database exists
	 * @since 1.0
	 */
	protected boolean dbExists()
	{
		return tableExists(Tables.CURRENT_STOCKS.toString());
	}// dbExists()

	/**
	 * Connects to the SQLite Database and creates a new one if there isn't one
	 * already.
	 * 
	 * @return True if connection successfully established
	 * @since 1.0
	 */
	protected boolean connect()
	{
		/* Return value */
		boolean retval = false;

		/* Will only open a new connection if one not currently open */
		if (!connected)
		{
			con = null;

			/* Attempts to open a new connection */
			try
			{
				/* Uses the SQLite Java Driver to open a connection */
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection("jdbc:sqlite:" + RESOURCES + DB_NAME);

				connected = true;
				retval = true;

				/* Asserts that the database exists */
				dbExists = dbExists();

				/* Creates the database from default schema */
				if (!dbExists)
					createDatabase();
			} // try
			catch (Exception e)
			{
				System.out.println("connect(): " + e.getLocalizedMessage());
				retval = false;
			} // catch(Exception)
		} // if !connected

		return retval;
	}// connect()

	/**
	 * Executes a SQL query that does have a usable return, such as SELECT
	 * 
	 * @param query The SQL query to be executed on the Database
	 * @return The ResultSet of the SQL query
	 * @since 1.0
	 * @see {@link java.sql.Statement Statement}
	 */
	public ResultSet executeQuery(String query)
	{
		/* Stores variables outside try/catch block to properly close them */
		ResultSet results = null;

		/* Opens a connection if necessary */
		if (!connected)
			connect();

		/* Attempts to execute the given query */
		try
		{
			PreparedStatement stmt = con.prepareStatement(query);

			if (!stmt.isCloseOnCompletion())
				stmt.closeOnCompletion();

			results = stmt.executeQuery();

		}
		catch (SQLException e)
		{
			System.out.println("executeQuery(): " + e.getLocalizedMessage());
		} // catch(SQLException)

		return results;
	}// executeQuery(String)

	/**
	 * Executes a SQL query that doesn't return anything such as INSERT or
	 * UPDATE.
	 * 
	 * @param query The SQL query to be executed
	 * @return True if the query was successfully executed
	 * @since 1.0
	 * @see {@link java.sql.Statement Statement}
	 */
	public boolean executeUpdate(String query)
	{
		boolean retval = false;

		/* Opens a connection if necessary */
		if (!connected)
			connect();

		/* Tries to execute a SQL query */
		try
		{
			PreparedStatement stmt = con.prepareStatement(query);

			int rows = stmt.executeUpdate();

			/* If rows return is greater than 0, execution was successful */
			if (rows > 0)
				retval = true;

			stmt.close();

		} // try
		catch (SQLException e)
		{
			System.out.println("executeUpdate(): " + e.getLocalizedMessage());
			e.printStackTrace();
		} // catch SQLException

		return retval;
	}// executeUpdate(String)
	/**
	 * Creates a new Database if the previous is lost with the schema found in
	 * 'default.txt'
	 * 
	 * @return True if database successfully created
	 * @since 1.0
	 */
	protected boolean createDatabase()
	{
		/* Stores variables outside try/catch block to properly close them */
		BufferedReader reader = null;
		Statement stmt = null;
		boolean retval = false;
		/* Creates a database from default schema file */
		try
		{
			reader = new BufferedReader(new FileReader(new File(DEFAULT_DB_FILE)));

			/* Reads and executes all the statements stored in the file */
			while (reader.ready())
			{
				String sql = reader.readLine();
				stmt = con.createStatement();

				stmt.executeUpdate(sql);
				stmt.close();
			} // while

			retval = true;

			reader.close();

		} // try
		catch (Exception e)
		{
			System.out.println("createDatabase(): " + e.getLocalizedMessage());
		} // catch Exception
		finally
		{
			/* Handles complex objects manually */
			if (reader != null)
				reader = null;

			if (stmt != null)
				stmt = null;
		} // finally

		dbExists = true;

		return retval;
	}// createDatabase()

	/**
	 * Checks if a given table exists in the SQLite Database
	 * 
	 * @param table Name of the table to check
	 * @return True if table exists, False if it does not exists
	 * @since 1.0
	 */
	public boolean tableExists(String table)
	{
		boolean retval = false;

		Statement stmt = null;

		/* Opens a connection if necessary */
		if (!connected)
			connect();

		/* Tries to check if given table exists */
		try
		{
			/* Executes a SQL query */
			stmt = con.createStatement();

			String sql  = String.format("SELECT name FROM sqlite_master WHERE type='table' AND name='%s';", table);

			ResultSet results = stmt.executeQuery(sql);

			/*
			 * If results is automatically closed, nothing was returned from the
			 * SELECT statement
			 */
			if (!results.isClosed() && results.next())
				retval = true;
			results.close();
			stmt.close();
			
		} // try
		catch (SQLException e)
		{
			System.out.println("tableExists():" + e.getLocalizedMessage());
		} // catch SQLException
		finally
		{
			/* Handles complex objects manually */
			if (stmt != null)
				stmt = null;
		} // finally

		return retval;
	}// tableExists(String)

	/**
	 * Safely closes the connection to the database.
	 * 
	 * @since 1.0
	 */
	public void close()
	{
		/* Attempts to safely close connection */
		try
		{
			con.close();
		} // try
		catch (Exception e)
		{
			System.out.println("close():" + e.getLocalizedMessage());
		} // catch Exception
		finally
		{
			/* Handles complex objects manually */
			if (con != null)
				con = null;

			connected = false;
		} // finally
	}// close()
	
	public boolean batchUpdate(String query, String[] values)
	{
		if (!connected)
			connect();
		PreparedStatement stmt = null;
		boolean value = true;
		try
		{
			for(int i=0; i<values.length-1; i+=2)
			{
				stmt = con.prepareStatement(query);
				stmt.setString(1, values[i]);
				stmt.setString(2, values[i+1]);
				int a = stmt.executeUpdate();
				
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("batchUpdate(): "+e.getLocalizedMessage());
		}
		return value;
	}

}// SQliteConnector class
