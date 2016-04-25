package com.finance.main.java.chart;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.finance.main.java.util.Localized;

@SuppressWarnings("serial")
public class StockChartPanel extends JPanel implements Localized
{

	protected StockChart stockChart;
	
	public StockChartPanel()
	{
		SettingsFrame settings = new SettingsFrame();
		stockChart = new StockChart(settings);
		
		GridBagConstraints constraint = new GridBagConstraints();
    	constraint.anchor = GridBagConstraints.NORTHEAST;
    	
    	this.setLayout(new GridBagLayout());
		this.add(stockChart.getPanel());
		this.add(settings.createSettingsButton(), constraint);
	}
	
	public void addSeries(String stockSymbol)
	{
		stockChart.addSeries(stockSymbol);
	}
	
	@Override
	public boolean updateLabels()
	{
		stockChart.updateLabels();
		return true;
	}
}
