package com.finance.main.java.stock;

import org.json.JSONObject;

public class CurrentStock extends Stock {
	private double dayLow;
	private double yearHigh;
	private double yearLow;
	private double dayHigh;
	private String symbol;
	private String companyName;
	private String exchange;
	private int avgDaily;
	private double change;
	private double lastTrade;
	private int volume;
	/**
	 * 
	 * @param symbol 
	 * @param yearHigh
	 * @param yearLow 
	 * @param dayHigh 
	 * @param dayLow
	 * @param avgDaily
	 * @param change
	 * @param lastTrade
	 * @param volume
	 * @param exchange
	 */
	public CurrentStock(String symbol,String companyName,double yearHigh, double yearLow,
			double dayHigh, double dayLow, int avgDaily, double change, double lastTrade,
			int volume, String exchange) {
		super(symbol);
		setSymbol(symbol);
		setDayLow(dayLow);
		setDayHigh(dayHigh);
		setAvgDaily(avgDaily);
		setExchange(exchange);
		setVolume(volume);
		setLastTrade(lastTrade);
		setChange(change);
		setYearHigh(yearHigh);
		setYearLow(yearLow);
		setCompanyName(companyName);
	}
	/**
	 * @return
	 */
	public double getDayLow() {
		return dayLow;
	}
	/**
	 * @param dayLow
	 */
	public void setDayLow(double dayLow) {
		this.dayLow = dayLow;
	}
	/**
	 * @return
	 */
	public double getYearHigh() {
		return yearHigh;
	}
	/**
	 * @param yearHigh
	 */
	public void setYearHigh(double yearHigh) {
		this.yearHigh = yearHigh;
	}
	/**
	 * @return
	 */
	public double getYearLow() {
		return yearLow;
	}
	/**
	 * @param yearLow
	 */
	public void setYearLow(double yearLow) {
		this.yearLow = yearLow;
	}
	/**
	 * @return
	 */
	public double getDayHigh() {
		return dayHigh;
	}
	/**
	 * @param dayHigh
	 */
	public void setDayHigh(double dayHigh) {
		this.dayHigh = dayHigh;
	}

	/**
	 * @return 
	 */
	public String getSymbol() {
		return symbol;
	}
	/**
	 * @param symbol
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	/**
	 * @return
	 */
	public String getExchange() {
		return exchange;
	}
	/**
	 * @param exchange
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	/**
	 * @return
	 */
	public int getAvgDaily() {
		return avgDaily;
	}
	/**
	 * @param avgDaily
	 */
	public void setAvgDaily(int avgDaily) {
		this.avgDaily = avgDaily;
	}
	/**
	 * @return
	 */
	public double getChange() {
		return change;
	}
	/**
	 * @param change
	 */
	public void setChange(double change) {
		this.change = change;
	}
	/**
	 * @return
	 */
	public double getLastTrade() {
		return lastTrade;
	}
	/**
	 * @param lastTrade
	 */
	public void setLastTrade(double lastTrade) {
		this.lastTrade = lastTrade;
	}
	/**
	 * @return 
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return 
	 */
	public int getVolume() {
		return volume;
	}
	/**
	 * @param volume
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	/**
	 * makeCurrentStock takes in the json object of the results and parses it for all of the quote values, then
	 * creates a CurrentStock object and returns it.
	 * @param data A json object with the quote data.
	 * @return A CurrentStock object.
	 */
	public static CurrentStock makeCurrentStock(JSONObject data){
		String symbol = data.getString("Symbol");
		String companyName = data.getString("Name");
		double dayHigh = Double.parseDouble(data.getString("DaysHigh"));
		double dayLow = Double.parseDouble(data.getString("DaysLow"));
		double yearHigh = Double.parseDouble(data.getString("YearHigh"));
		double yearLow = Double.parseDouble(data.getString("YearLow"));
		int volume = Integer.parseInt(data.getString("Volume"));
		String exchange = data.getString("StockExchange");
		int avgDaily = Integer.parseInt(data.getString("AverageDailyVolume"));
		double change = Double.parseDouble(data.getString("Change"));
		double lastTrade = Double.parseDouble(data.getString("LastTradePriceOnly"));
		CurrentStock newCurrent = new CurrentStock(symbol, companyName, yearHigh, yearLow, dayHigh, dayLow, avgDaily, change, lastTrade, volume, exchange);
		return newCurrent;
	}
}
