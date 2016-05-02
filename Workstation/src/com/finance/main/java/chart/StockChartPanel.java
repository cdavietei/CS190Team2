package com.finance.main.java.chart;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.finance.main.java.enums.Languages;
import com.finance.main.java.util.Localized;
import com.finance.main.java.util.LocalizedStrings;


/**
 * <p>This is an adapter class that is used by MainWindow. This class maps methods called from
 * MainWindow to methods defined in StockChart class.
 * 
 * <p>This class is a panel that contains a panel returned by StockChart, and a panel 
 * containing settings icon for the chart.  
 * 
 * @author MI ONIM
 *
 */
@SuppressWarnings("serial")
public class StockChartPanel extends JPanel implements Localized
{
	protected StockChart stockChart;
	SettingsFrame settings;
	
	public StockChartPanel()
	{
		settings = new SettingsFrame();
		stockChart = new StockChart(settings);
		
		GridBagConstraints constraint = new GridBagConstraints();
    	constraint.anchor = GridBagConstraints.NORTHEAST;     //placed in top right corner
    	
    	this.setLayout(new GridBagLayout());
		this.add(stockChart.getPanel());
		this.add(settings.createSettingsButton(), constraint);
	}
	
	public void addSeries(String stockSymbol)
	{
		stockChart.addSeries(stockSymbol);
	}
	
	public void setLange(Languages lang){
		LocalizedStrings.setLanguage(lang);
		updateLabels();
		settings.updateLabels();
	}
	
	@Override
	public boolean updateLabels()
	{
		stockChart.updateLabels();
		return true;
	}
}
