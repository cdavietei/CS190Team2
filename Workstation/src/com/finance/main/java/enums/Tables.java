package com.finance.main.java.enums;

public enum Tables {
	
	EXCHANGE_INFORMATION("Exchange_Information"),
	CURRENT_STOCKS("Current_Stocks"),
	STOCK_TEMPLATE("Stock_Template"), 
	COMPANIES("Companies");
	
	
	private String value;
	
	private Tables(String value)
	{
		this.value = value;
	}
	
	public String toString()
	{
		return this.value;
	}
}
