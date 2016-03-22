package com.finance.main.java.stock;

import java.sql.Date;

/**
 * 
 * @author Chris
 *
 */
public class Stock implements Comparable<Stock>
{
	protected String symbol, companyName, exchangeName;
	protected double open, high, low, close, adjClose;
	protected int volume;
	protected Date date;

	/**
	 * 
	 * @param symbol
	 */
	public Stock(String symbol)
	{
		setSymbol(symbol);
	}

	/**
	 * 
	 * @param symbol
	 * @param companyName
	 * @param exchangeName
	 * @param open
	 * @param high
	 * @param low
	 * @param close
	 * @param adjClose
	 * @param volume
	 * @param date
	 */
	public Stock(String symbol, String companyName, String exchangeName, double open, double high, double low,
			double close, double adjClose, int volume, Date date)
	{
		setSymbol(symbol);
		setCompanyName(companyName);
		setExchangeName(exchangeName);
		setOpen(open);
		setHigh(high);
		setLow(low);
		setClose(close);
		setAdjClose(adjClose);
		setVolume(volume);
		setDate(date);

	}

	/**
	 * 
	 * @param symbol
	 */
	public void setSymbol(String symbol)
	{
		this.symbol = symbol.toUpperCase();
	}

	/**
	 * 
	 * @param companyName
	 */
	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	/**
	 * 
	 * @param exchangeName
	 */
	public void setExchangeName(String exchangeName)
	{
		this.exchangeName = exchangeName;
	}

	/**
	 * 
	 * @param open
	 */
	public void setOpen(double open)
	{
		this.open = open;
	}

	/**
	 * 
	 * @param high
	 */
	public void setHigh(double high)
	{
		this.high = high;
	}

	/**
	 * 
	 * @param low
	 */
	public void setLow(double low)
	{
		this.low = low;
	}

	/**
	 * 
	 * @param close
	 */
	public void setClose(double close)
	{
		this.close = close;
	}

	/**
	 * 
	 * @param adjClose
	 */
	public void setAdjClose(double adjClose)
	{
		this.adjClose = adjClose;
	}

	/**
	 * 
	 * @param volume
	 */
	public void setVolume(int volume)
	{
		this.volume = volume;
	}

	/**
	 * 
	 * @param date
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

	/**
	 * 
	 * @return
	 */
	public String getSymbol()
	{
		return symbol;
	}

	/**
	 * 
	 * @return
	 */
	public String getCompanyName()
	{
		return companyName;
	}

	/**
	 * 
	 * @return
	 */
	public String getExchangeName()
	{
		return exchangeName;
	}

	/**
	 * 
	 * @return
	 */
	public double getOpen()
	{
		return open;
	}

	/**
	 * 
	 * @return
	 */
	public double getHigh()
	{
		return high;
	}

	/**
	 * 
	 * @return
	 */
	public double getLow()
	{
		return low;
	}

	/**
	 * 
	 * @return
	 */
	public double getClose()
	{
		return close;
	}

	/**
	 * 
	 * @return
	 */
	public double getAdjClose()
	{
		return adjClose;
	}

	/**
	 * 
	 * @return
	 */
	public int getVolume()
	{
		return volume;
	}

	/**
	 * 
	 * @return
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * 
	 */
	public int compareTo(Stock other)
	{
		return date.compareTo(other.getDate());
	}

}
