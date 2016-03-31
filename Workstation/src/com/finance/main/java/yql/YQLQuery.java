package com.finance.main.java.yql;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import com.finance.main.java.stock.*;
import org.json.*;

public class YQLQuery
{
	public static final String baseUrlStart = "https://query.yahooapis.com/v1/public/yql?q=";
	public static final String baseUrlEnd = "&format=json&env=store://datatables.org/alltableswithkeys";
	
	//the Data-tables YQL looks into
	public static final String yahooFinanceHistoricalData = "yahoo.finance.historicaldata";
	public static final String yahooFinanceQuote = "yahoo.finance.quote";
	
	/* test the query methods here */
	public static void main(String[] args) throws Exception
	{
		//getHistoricalData("GOOG","2016-03-12","2016-03-22");
		//getCurrentData("GOOG");
	}
	
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
		String queryString = queryStringHistoricalData(stockSymbol, startDate, endDate);
		
		//encode queryString into UTF-8 format
		String urlString = baseUrlStart + URLEncoder.encode(queryString, "UTF-8").replace("+", "%20") + baseUrlEnd;
		String urlData = readStreamFromUrl(urlString);
		
		//parse the JSON data returned from the query
		JSONObject queryObject = new JSONObject(urlData);
		int countData = queryObject.getJSONObject("query").getInt("count");
		JSONArray stockData = queryObject.getJSONObject("query").getJSONObject("results").getJSONArray("quote");
		
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		for(int i = 0; i < countData;  i++) {
			JSONObject stockObject = stockData.getJSONObject(i);
			Stock newStock = Stock.makeStock(stockObject);
			stocks.add(newStock);
		} //for
		
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
		String urlString = baseUrlStart + URLEncoder.encode(queryString, "UTF-8").replace("+", "%20") + baseUrlEnd;
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
			} //while
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			input.close();
		} //try-catch-finally
		
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
		String queryString = "SELECT * FROM " + yahooFinanceHistoricalData + " " +
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
		String queryString = "SELECT * FROM " + yahooFinanceQuote + " " +
							 "WHERE symbol = \"" + stockSymbol + "\"";
		
		return queryString;
	}
}
