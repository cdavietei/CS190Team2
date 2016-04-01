package com.finance.main.java.yql;

import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import com.finance.main.java.stock.*;
import org.json.*;
public class yqlQuery {
	public static void main(String[] args) throws JSONException, ParseException{
		getQuote("GOOG");
		//query("GOOG","2016-03-12","2016-03-22");
	}
	/**
	 * 
	 * @param stockName The name of the stock, upper or lower case.
	 * @param startDate Start date for the search, in yyyy-mm-dd format.
	 * @param endDate End date of the search, in yyyy-mm-dd format.
	 * @return Returns an ArrayList of stocks.
	 * @throws IOException
	 * @throws JSONException
	 * @throws ParseException
	 */
	public static ArrayList<Stock> query(String stockName,String startDate, String endDate){
		String baseHist = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22";
		String base1 = "%22%20and%20startDate%20%3D%20%22";
		String base2 ="%22%20and%20endDate%20%3D%20%22";
		String base3 = "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
		String queryStr = baseHist+stockName.toUpperCase()+base1+startDate+base2+endDate+base3;
		URL fullUrl;
		String stockData = "";
		try {
			fullUrl = new URL(queryStr);
			URLConnection connection = fullUrl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine = "";
			while((inputLine = in.readLine()) !=null){
				stockData+=inputLine;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(stockData);
		JSONObject jobj = new JSONObject(stockData);
		int count = jobj.getJSONObject("query").getInt("count");
		JSONArray quote = jobj.getJSONObject("query").getJSONObject("results").getJSONArray("quote");
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		for(int i = 0; i < count;  i++){
			JSONObject object = quote.getJSONObject(i);
			Stock newStock = Stock.makeStock(object);
			stocks.add(newStock);
		}
		return stocks;
	}
	/**
	 *  Queries YQL for the quote of the stock and returns an object with all the data.
	 * @param stockName The symbol of the stock.
	 * @return A CurrentStock object created with the given stock quote data.
	 */
	public static CurrentStock getQuote(String stockName){
		String queryStr = "https://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.finance.quote%20WHERE%20symbol%3D%22"
				+stockName
				+"%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
		URL fullUrl;
		String stockInfo = "";
		try{
			fullUrl = new URL(queryStr);
			URLConnection connection = fullUrl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine = "";
			while((inputLine = in.readLine()) != null){
				stockInfo +=inputLine;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		JSONObject jobj = new JSONObject(stockInfo);
		JSONObject stockData = jobj.getJSONObject("query");
		stockData = stockData.getJSONObject("results").getJSONObject("quote");
		return CurrentStock.makeCurrentStock(stockData);

	}

}
