package com.finance.main.java.chart;

public interface Subject
{
	public void attach(StockChart observer);
	public void update();
}
