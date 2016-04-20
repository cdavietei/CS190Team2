package com.finance.main.java.chart;

public interface Subject
{
	public void attach(StockChartPanel observer);
	public void update();
}
