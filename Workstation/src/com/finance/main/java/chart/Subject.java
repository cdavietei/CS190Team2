package com.finance.main.java.chart;


/**
 * This interface declares the methods to implement the Observer pattern for
 * StockChart class.
 * 
 * @author MI ONIM
 *
 */
public interface Subject
{
	public void attach(StockChart observer);
	public void update();
}
