package com.finance.main.java.enums;

/**
 * 
 * @author Chris
 *
 */
public enum TableFields 
{
	SYMBOL("Symbol"),
	COMPANY_NAME("Company_Name"),
	EXCHANGE_NAME("Exchange_Name"),
	TABLE_NAME("Table_Name"),
	DATE("Date"),
	OPEN("Open"),
	HIGH("High"),
	LOW("Low"),
	CLOSE("Close"),
	VOLUME("Volume"),
	ADJ_CLOSE("Adj_Close");
	
	
	private String value;
	
	/**
	 * 
	 * @param value
	 */
	private TableFields(String value)
	{
		this.value = value;
	}
	
	/**
	 * 
	 */
	public String toString()
	{
		return this.value;
	}
	
	//cache only 30 stocks
}
