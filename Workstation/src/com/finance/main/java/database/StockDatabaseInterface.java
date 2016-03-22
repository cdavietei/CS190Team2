package com.finance.main.java.database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.finance.main.java.stock.Stock;
import com.finance.main.java.enums.*;
import com.finance.main.java.yql.yqlQuery;

/**
 * 
 * @author Christopher Davie
 * @version 1.0
 *
 */
public class StockDatabaseInterface
{
	protected SQLiteConnector connection;

	/**
	 * The default constructor for the {@code StockDatabaseInterface} class.
	 */
	public StockDatabaseInterface()
	{
		connection = new SQLiteConnector();
	}// StockDatabaseInterface()

	/**
	 * Adds a new table for the given stock to the SQLite database.
	 * @param stock The Stock symbol to be used for the table name.
	 */
	public void addNewStock(String stock)
	{

		/* Exits function if table already exists */
		if (connection.tableExists(stock + "_info"))
			return;

		/*
		 * Creates and executes the SQL query to insert into the Current_Stocks
		 * table
		 */
		String query = "INSERT INTO " + Tables.CURRENT_STOCKS + " (Symbol,Table_Name) VALUES ('" + stock + "', '"
				+ stock + "_info');";

		connection.executeUpdate(query);

		/* Creates a new table for the given stock to store information in */
		query = createStockTableQuery(stock + "_info");

		connection.executeUpdate(query);

		connection.close();
	}// addNewStock(String)

	/**
	 * Creates a SQL query to create a new table for the given stock
	 * @param tableName The desired table name for the Stock
	 * @return The SQL query to create a new table
	 */
	protected String createStockTableQuery(String tableName)
	{
		return "CREATE TABLE " + tableName + " (" + TableFields.DATE + " DATE PRIMARY KEY NOT NULL, " + TableFields.OPEN
				+ " FLOAT NOT NULL, " + TableFields.HIGH + " FLOAT NOT NULL, " + TableFields.LOW + " FLOAT NOT NULL, "
				+ TableFields.CLOSE + " FLOAT NOT NULL, " + TableFields.VOLUME + " INTEGER NOT NULL, "
				+ TableFields.ADJ_CLOSE + " FLOAT NOT NULL);";
	}// createStockTableQuery()

	/**
	 * Gets all the stock information from the SQLite database related to the
	 * given symbol and date range.
	 * @param symbol The symbol of the stock.
	 * @param startDate The inclusive starting date of the given range to gather
	 *            information.
	 * @param endDate The inclusive ending date of the given range to gather
	 *            information.
	 * @return An ArrayList of stocks in order of their given dates.
	 * @throws Exception Throws an Exception if the starting date is greater
	 *             than the ending date.
	 */
	public ArrayList<Stock> getStocks(String symbol, Date startDate, Date endDate) throws Exception
	{
		/* Checks to see if the given date ranges are valid */
		if (startDate.compareTo(endDate) > 0)
			throw new Exception("Invalid Date Range");
		else if (connection.tableExists(symbol + "_info"))
			return null;

		/* Calculates the difference in days between the dates */
		int diff = dateDiff(startDate, endDate);

		/* Creates the ArrayList to store the stocks */
		ArrayList<Stock> stocks = new ArrayList<Stock>(diff);

		if (!dateRangeExists(symbol, startDate, endDate))
			queryYQL(symbol,"start", "end");

		/* Queries the database for the stock information */
		// TODO Make this more efficient by using two date clauses in the
		// where
		
		String query = "SELECT * FROM "+symbol+"_info WHERE Date >= '"+startDate.toString() +"' AND Date <= '"+endDate.toString()+"';";
		ResultSet results = connection.executeQuery(query);
		
		while(results.next())
			stocks.add(createStock(symbol, results));
		/*
		for (int i = 0; i < diff; i++)
		{

			Date date = new Date(startDate.getTime() + i * 1000 * 60 * 60 * 24);
			String query = "SELECT * FROM " + symbol + "_info WHERE Date = " + date.toString() + ";";

			ResultSet results = connection.executeQuery(query);

			while (results.next())
				stocks.add(createStock(results));
		}
		*/
		results.close();

		return stocks;
	}// getStocks(String,Date,Date)

	/**
	 * 
	 * @param symbol 
	 * @param start
	 * @param end
	 * @return
	 */
	protected boolean dateRangeExists(String symbol, Date start, Date end)
	{
		String query1 = "SELECT Open FROM " + symbol + "_info WHERE Date ='" + start.toString() + "';";
		String query2 = "SELECT Open FROM " + symbol + "_info WHERE Date ='" + end.toString() + "';";

		ResultSet results1 = connection.executeQuery(query1);
		ResultSet results2 = connection.executeQuery(query2);

		boolean retval = false;

		try
		{
			retval = !results1.isClosed() && !results2.isClosed();
			
			results1.close();
			results2.close();
		}
		catch (Exception e)
		{
			System.out.println(e.getLocalizedMessage());
		}
		finally
		{
			if(results1 != null)
				results1 = null;
			if(results2 != null)
				results2 = null;
		}

		return retval;
	}
	/**
	 * 
	 * @param stockName The name of the stock.
	 * @param startDate The starting date for the search (yyyy-mm-dd)
	 * @param endDate The ending date for the search (yyyy-mm-dd)
	 * @return An Array/List of Stocks, one for each date in the date range.
	 */
	protected boolean queryYQL(String stockName, String startDate, String endDate)
	{
		ArrayList<Stock> stockList = yqlQuery.query(stockName,startDate,endDate);
		return false;
	}

	/**
	 * 
	 * @param stocks
	 */
	public void batchUpdateStock(ArrayList<Stock> stocks)
	{
		for (Stock stock : stocks)
		{
			String query = "INSERT INTO " + stock.getSymbol()
					+ "_info (Symbol, Date, Open, High, Low, Close, Volume, Adj_Close)" + "VALUES ('"
					+ stock.getSymbol() + "', '" + stock.getDate().toString() + "'," + stock.getOpen() + ","
					+ stock.getHigh() + "," + stock.getLow() + "," + stock.getClose() + "," + stock.getVolume() + ","
					+ stock.getAdjClose() + ");";

			connection.executeUpdate(query);
		}
		connection.close();
	}

	/**
	 * 
	 * @param results
	 * @return
	 */
	protected Stock createStock(String symbol, ResultSet results)
	{
		Stock stock = null;

		try
		{
			stock = new Stock(symbol);

			stock.setDate(results.getDate("Date"));
			stock.setOpen(results.getDouble("Open"));
			stock.setHigh(results.getDouble("High"));
			stock.setLow(results.getDouble("Low"));
			stock.setClose(results.getDouble("Close"));
			stock.setAdjClose(results.getDouble("Adj_Close"));
			stock.setVolume(results.getInt("Volume"));
		}
		catch (SQLException e)
		{
			System.out.println(e.getLocalizedMessage());
		}

		return stock;

	}// createStock(ResultSet)

	/**
	 * Returns the differences between two dates in terms of days
	 * @param start The starting date of the given range
	 * @param end The end date of the given range
	 * @return The number of days difference between the two days
	 */
	protected int dateDiff(Date start, Date end)
	{
		return (int) ((end.getTime() - start.getTime()) / 1000 * 60 * 60 * 24);
	}// dateDiff(Date,Date)

}// StockAdapter class
