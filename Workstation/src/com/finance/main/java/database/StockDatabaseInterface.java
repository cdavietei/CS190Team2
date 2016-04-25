package com.finance.main.java.database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.finance.main.java.stock.Stock;
import com.finance.main.java.util.Utilities;
import com.finance.main.java.enums.*;
import com.finance.main.java.yql.YQLQuery;

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
		String query = String.format("INSERT INTO %s (%s,%s) VALUES ('%s','%s_info')", Tables.CURRENT_STOCKS,
				TableFields.SYMBOL, TableFields.TABLE_NAME, stock, stock);

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
		return String.format(
				"CREATE TABLE %s (%s DATE PRIMARY KEY NOT NULL, %s FLOAT NOT NULL, %s FLOAT NOT NULL, "
						+ "%s FLOAT NOT NULL, %s FLOAT NOT NULL, %s INTEGER NOT NULL, %s FLOAT NOT NULL);",
				tableName, TableFields.DATE, TableFields.OPEN, TableFields.HIGH, TableFields.LOW, TableFields.CLOSE,
				TableFields.VOLUME, TableFields.ADJ_CLOSE);
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
	public ArrayList<Stock> getStocksAsynch(String symbol, Date startDate, Date endDate) throws Exception
	{
		/* Checks to see if the given date ranges are valid */
		if (startDate.compareTo(endDate) > 0)
			throw new Exception("Invalid Date Range");
		else if (!connection.tableExists(symbol + "_info"))
			addNewStock(symbol);

		/* Calculates the difference in days between the dates */
		int diff = Utilities.dateDiff(startDate, endDate);

		/* Creates the ArrayList to store the stocks */
		ArrayList<Stock> stocks = new ArrayList<Stock>(diff);

		if (!dateRangeExists(symbol, startDate, endDate))
			queryYQL(symbol, startDate.toString(), endDate.toString());

		String query = String.format("SELECT * FROM %s_info WHERE Date >= '%s' AND Date <= '%s' ORDER BY %s DESC;", symbol,
				startDate.toString(), endDate.toString(), TableFields.DATE);

		ResultSet results = connection.executeQuery(query);

		while (results.next())
			stocks.add(createStock(symbol, results));

		results.close();

		return stocks;
	}// getStocks(String,Date,Date)
	
	
	public ArrayList<Stock> getStocks(String symbol, Date startDate, Date endDate) throws Exception
	{
		ArrayList<Stock> stocks = getStocksAsynch(symbol, startDate, endDate);
		
		int counter = 0;
		
		
		while((stocks.isEmpty() || stocks.get(0) == null) && counter <3)
		{
			Thread.sleep(1000);
			stocks = getStocksAsynch(symbol, startDate, endDate);
			counter++;
		}
		
		return stocks;
	}

	/**
	 * 
	 * @param symbol
	 * @param start
	 * @param end
	 * @return
	 */
	protected boolean dateRangeExists(String symbol, Date start, Date end)
	{

		String query1 = String.format("SELECT Date FROM %s_info WHERE Date ='%s';", symbol, start.toString());
		String query2 = String.format("SELECT Date FROM %s_info WHERE Date ='%s';", symbol, end.toString());

		ResultSet results1 = connection.executeQuery(query1);
		ResultSet results2 = connection.executeQuery(query2);

		boolean retval = false;

		try
		{
			boolean r1, r2;
			
			r1 = results1.next();
			r2 = results2.next();
			
			//if(r1)
				
			
			
			retval = r1 && r2;

			results1.close();
			results2.close();
		}
		catch (Exception e)
		{
			System.out.println("dateRangeExists()" + e.getLocalizedMessage());
		}
		finally
		{
			if (results1 != null)
				results1 = null;
			if (results2 != null)
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
	 * @throws Exception
	 */
	protected void queryYQL(String stockName, String startDate, String endDate) throws Exception
	{
		System.out.printf("Start: %s End: %s\n", startDate, endDate);
		
		new Thread(new QueryYQLAsynch(stockName, startDate, endDate)).start();;
		
	}

	/**
	 * 
	 * @param stocks
	 */
	public void batchUpdateStock(ArrayList<Stock> stocks)
	{
		for (Stock stock : stocks)
		{

			String query = String.format(
					"INSERT OR IGNORE INTO %s_info (Date, Open, High, Low, Close, Volume, Adj_Close) "
							+ "VALUES ('%s', %f, %f, %f, %f, %d, %f);",
					stock.getSymbol(), stock.getDate().toString(), stock.getOpen(), stock.getHigh(), stock.getLow(),
					stock.getClose(), stock.getVolume(), stock.getAdjClose());

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
			stock.setDate(Utilities.stringToSqlDate(results.getString("Date")));
			stock.setOpen(results.getDouble("Open"));
			stock.setHigh(results.getDouble("High"));
			stock.setLow(results.getDouble("Low"));
			stock.setClose(results.getDouble("Close"));
			stock.setAdjClose(results.getDouble("Adj_Close"));
			stock.setVolume(results.getInt("Volume"));
		}
		catch (SQLException e)
		{
			System.out.println("createStock(): " + e.getLocalizedMessage());
		}

		return stock;

	}// createStock(ResultSet)
	
	public ArrayList<String> getAvailableStocks()
	{
		String query = String.format("SELECT %s FROM %s ORDER BY %s DESC", TableFields.SYMBOL, 
				Tables.CURRENT_STOCKS, TableFields.SYMBOL);
		
		ArrayList<String> stocks = new ArrayList<String>();
		
		
		ResultSet results = connection.executeQuery(query);
		
		try
		{
			while(results.next())
				stocks.add(results.getString(1));
			
			results.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return stocks;
	}

}// StockAdapter class

class QueryYQLAsynch implements Runnable
{

	protected String symbol, startDate, endDate;
	
	
	public QueryYQLAsynch(String symbol, String sDate, String eDate)
	{
		this.symbol = symbol;
		startDate = sDate;
		endDate = eDate;
	}
	
	@Override
	public void run()
	{
		StockDatabaseInterface stockInter = new StockDatabaseInterface();
		
		try
		{
			stockInter.batchUpdateStock(YQLQuery.getHistoricalData(symbol, startDate.toString(), endDate.toString()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
