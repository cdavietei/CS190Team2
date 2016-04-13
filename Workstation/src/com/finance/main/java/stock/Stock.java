package com.finance.main.java.stock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.finance.main.java.util.Utilities;

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

	public String toString()
	{
		
		String retval = String.format(
				"Symbol: %s Date: %s Open: %.3f High: %.3f Low: %.3f Close: %.3f Volume: %d AdjClose: %.3f",
				getSymbol(), getDate().toString(), getOpen(), getHigh(), getLow(), getClose(), getVolume(),
				getAdjClose());
		return retval;
				
		//return "Symbol: "+getSymbol+" Date: "+getDate();
	}

	/**
	 * 
	 * @param stock
	 * @return
	 */
	public static Stock makeStock(JSONObject stockInfo)
	{
		String id = stockInfo.getString("Symbol");
		String companyName = "";
		String exchangeName = "";
		double open = stockInfo.getDouble("Open");
		double high = stockInfo.getDouble("High");
		double low = stockInfo.getDouble("Low");
		double close = stockInfo.getDouble("Close");
		double adjClose = stockInfo.getDouble("Adj_Close");
		int volume = stockInfo.getInt("Volume");
		String strDate = stockInfo.getString("Date");
		

		Stock newStock = new Stock(id, companyName, exchangeName, open, high, low, close, adjClose, volume,
				Utilities.stringToSqlDate(strDate));
		return newStock;
	}
}
