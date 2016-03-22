package com.finance.main.java.yql;

import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.io.*;
import com.finance.main.java.stock.*;
import org.json.*;
public class yqlQuery {
	public static void main(String[] args) throws JSONException, ParseException{
		query("GOOG","2016-03-18","2016-03-29");
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
	String base0 = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22";
	String base1 = "%22%20and%20startDate%20%3D%20%22";
	String base2 ="%22%20and%20endDate%20%3D%20%22";
	String base3 = "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
	String queryStr = base0+stockName.toUpperCase()+base1+startDate+base2+endDate+base3;
	URL fullUrl;
	String stockData = "";
	try {
		fullUrl = new URL(queryStr);
	
	URLConnection connection = fullUrl.openConnection();
	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	String inputLine = "";
	while((inputLine = in.readLine()) !=null){
		stockData+=inputLine;
	}} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//System.out.println(stockData);
	JSONObject jobj = new JSONObject(stockData);
	int count = jobj.getJSONObject("query").getInt("count");
	//System.out.println(count);
	JSONArray quote = jobj.getJSONObject("query").getJSONObject("results").getJSONArray("quote");
	ArrayList<Stock> stocks = new ArrayList<Stock>();
	//System.out.println(stockData);
	for(int i = 0; i < count;  i++){
		String id = quote.getJSONObject(i).getString("Symbol");
		String companyName = "";
		String exchangeName = "";
		double open = quote.getJSONObject(i).getDouble("Open");
		double high = quote.getJSONObject(i).getDouble("High");
		double low = quote.getJSONObject(i).getDouble("Low");
		double close = quote.getJSONObject(i).getDouble("Close");
		double adjClose = quote.getJSONObject(i).getDouble("Adj_Close");
		int volume = quote.getJSONObject(i).getInt("Volume");
		String strDate = quote.getJSONObject(i).getString("Date");
		java.util.Date utildate = new java.util.Date();
		try {
			utildate = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
			System.out.println(utildate);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date date = new Date(utildate.getTime());
		Stock newStock = new Stock(id,companyName,exchangeName,open,high,low,close,adjClose,volume,date);
		stocks.add(newStock);
		//System.out.println(newStock.getDate());
	}
	return stocks;
		

	}
}
