package com.finance.main.java.yql;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.Date;

import com.finance.main.java.stock.*;
import com.finance.main.java.util.Utilities;

import org.json.*;


/**
 * <p>This class provides functionality to query stock information from Yahoo Finance using
 * Yahoo Query Language. The data is collected through HTTP get method.
 * 
 * <p>This class includes methods to get the current and historical data for any stock.
 * 
 * @author MI ONIM
 *
 */
public class YQLQuery
{
	public static final String BASE_URL_START = "https://query.yahooapis.com/v1/public/yql?q=";
	public static final String BASE_URL_END = "&format=json&env=store://datatables.org/alltableswithkeys";
	
	//the Data-tables YQL looks into
	public static final String YAHOO_FINANCE_HISTORICAL_DATA = "yahoo.finance.historicaldata";
	public static final String YAHOO_FINANCE_QUOTE = "yahoo.finance.quote";
	
	/**
	 * Retrieves historical data of the stock
	 * 
	 * @param stockSymbol The symbol of the stock, case insensitive
	 * @param startDate Start date for the search, in yyyy-mm-dd format
	 * @param endDate End date of the search, in yyyy-mm-dd format
	 * @return return ArrayList of Stock objects
	 * @throws Exception
	 */
	public static ArrayList<Stock> getHistoricalData(String stockSymbol, String startDate, String endDate) throws Exception
	{
		/* YQL fails to return data when the date difference is more than ~350.
		 * As a result, if the difference between start date and end date is more than
		 * 300 days (to be safe), send multiple request to Yahoo Finance with smaller date ranges.
		 */
		Date start = Utilities.stringToSqlDate(startDate);
		Date end = Utilities.stringToSqlDate(endDate);
		int dateDifference = Utilities.dateDiff(start, end);
		int iteration = 1;
		
		if (dateDifference > 300) {
			iteration = (int) Math.ceil((double) dateDifference / 300);
		}
		
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		
		while (iteration-- > 0) {
			end = Utilities.addDaysToSqlDate(start, 300);
			if (end.after(Utilities.stringToSqlDate(endDate))) {
				end = Utilities.stringToSqlDate(endDate);
			}
			
			String queryString = queryStringHistoricalData(stockSymbol, Utilities.sqlDateToString(start),
			                                                            Utilities.sqlDateToString(end));
			
			//encode queryString into UTF-8 format
			String urlString = BASE_URL_START + URLEncoder.encode(queryString, "UTF-8").replace("+", "%20") + BASE_URL_END;
			String urlData = readStreamFromUrl(urlString);
			
			//parse the JSON data returned from the query
			JSONObject queryObject = new JSONObject(urlData);
			int countData = queryObject.getJSONObject("query").getInt("count");
			JSONArray stockData = queryObject.getJSONObject("query").getJSONObject("results").getJSONArray("quote");
			
			for(int i = 0; i < countData;  i++) {
				JSONObject stockObject = stockData.getJSONObject(i);
				Stock newStock = Stock.makeStock(stockObject);
				stocks.add(newStock);
			}
			start = Utilities.addDaysToSqlDate(end, 1);
		}
		
		return stocks;
	}
	
	/**
	 * Retrieves current data of the stock
	 * 
	 * @param stockSymbol The symbol of the stock, case insensitive
	 * @return CurrentStock object
	 * @throws Exception
	 */
	public static CurrentStock getCurrentData(String stockSymbol) throws Exception
	{
		String queryString = queryStringCurrentData(stockSymbol);
		
		//encode queryString into UTF-8 format
		String urlString = BASE_URL_START + URLEncoder.encode(queryString, "UTF-8").replace("+", "%20") + BASE_URL_END;
		String urlData = readStreamFromUrl(urlString);
		
		//parse the JSON data returned from the query
		JSONObject queryObject = new JSONObject(urlData);
		JSONObject stockData = queryObject.getJSONObject("query").getJSONObject("results").getJSONObject("quote");
		
		return CurrentStock.makeCurrentStock(stockData);
	}
	
	/**
	 * Creates a connection to the given URL and reads data from it
	 * 
	 * @param urlString The URL to make the connection
	 * @return Data read from URL connection
	 * @throws IOException
	 */
	private static String readStreamFromUrl(String urlString) throws IOException
	{
		String urlData = "";
		BufferedReader input = null;
		
		try {
			URL url = new URL(urlString);
			input = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine = "";
			while((inputLine = input.readLine()) !=null) {
				urlData += inputLine;
			}
			input.close();
		}
		catch (UnknownHostException e) {
			JFrame frame = new JFrame();
			JOptionPane mess = new JOptionPane();
			mess.showMessageDialog(frame, "Check your internet connection.");
		}
		
		return urlData;
	}
	
	/**
	 * The YQL statement that queries the historical data of a stock
	 * 
	 * @param stockSymbol The symbol of the stock whose data is to be collected
	 * @param startDate Start date for the search
	 * @param endDate End date for the search
	 * @return The query statement that will extract the data
	 */
	private static String queryStringHistoricalData(String stockSymbol, String startDate, String endDate)
	{
		String queryString = "SELECT * FROM " + YAHOO_FINANCE_HISTORICAL_DATA + " " +
		                     "WHERE symbol = \"" + stockSymbol + "\" " +
		                     "AND startDate = \"" + startDate + "\" " +
		                     "AND endDate = \"" + endDate + "\"";
		
		return queryString;
	}
	
	/**
	 * The YQL statement that queries the current data of a stock
	 * 
	 * @param stockSymbol The symbol of the stock whose data is to be collected
	 * @return The query statement that will extract the data
	 */
	private static String queryStringCurrentData(String stockSymbol)
	{
		String queryString = "SELECT * FROM " + YAHOO_FINANCE_QUOTE + " " +
		                     "WHERE symbol = \"" + stockSymbol + "\"";
		
		return queryString;
	}
}
