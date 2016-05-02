package com.finance.main.java.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.finance.main.java.database.StockDatabaseInterface;
import com.finance.main.java.enums.TextFields;
import com.finance.main.java.stock.Stock;
import com.finance.main.java.util.Localized;
import com.finance.main.java.util.LocalizedStrings;
import com.finance.main.java.util.Utilities;


/**
 * <p>Displays graphical view of one or more stocks.
 * 
 * <p>The chart(s) implements tool-tip generator. Hovering mouse over a data point in the chart
 * will display the stock name, the time and the stock price in the format:
 * 			stockName: (time, price)
 * 
 * 
 * <p>To get the panel containing chart(s),
 * instantiate the class with no parameters, and call getPanel().
 * 
 * <p>This class uses the Builder design pattern. Therefore, to change the panel's width or height
 * or other default parameters, use the following way:
 * 
 * <p><pre>StockChart newChart = new StockChart().setPanelSize(800, 600)
 *                                               .setPanelHeight(400)
 *                                               .setXAxisLabel("Date")
 *                                               .setShowLegend(false); 
 * 
 * <p>To compare two or more stocks on the same panel, calling addDataset(...) will create
 * chart for each stock. For example, to compare Google and Yahoo stocks over May 20, 2015 to 
 * May 30, 2015, use the following:
 * 
 * <p>StockChart newChart = new StockChart();
 * newChart.addDataset("GOOG", "05/20/2015", "05/30/2015");
 * newChart.addDataset("YHOO", "05/20/2015", "05/30/2015");
 * 
 * <p>Calling addDataset() without the dates will return data over a default period of time.
 * 
 * @author MI ONIM
 *
 */
public class StockChart implements Localized
{
	protected ChartPanel chartPanel;
	protected TimeSeriesCollection dataset;
	
			/* Default values for the class */
	
	protected String chartTitle = "";
	protected String xAxisLabel = LocalizedStrings.getLocalString(TextFields.CHART_XAXIS);
	protected String yAxisLabel = LocalizedStrings.getLocalString(TextFields.CHART_YAXIS);
	protected boolean showLegend = true;
	protected boolean showTooltips = true;
	protected boolean generateUrls = false;
	protected int panelWidth = 650;
	protected int panelHeight = 367;
	protected String chartStartDate = "05/20/2015";
	protected String chartEndDate = "08/20/2015";
	
	protected boolean rangeTypeClose = true;     //default type
	protected boolean rangeTypeOpen = false;
	protected boolean rangeTypeAdjClose = false;
	protected boolean rangeTypeVolume = false;
	protected boolean rangeTypeHigh = false;
	protected boolean rangeTypeLow = false;
	
	/* Used to maintain consistent colors and shapes for plots */
	HashMap<String, Paint> plotColors = new HashMap<>();
	HashMap<String, Shape> plotShapes = new HashMap<>();
	
	/**
	 * Default constructor for StockChart. Creates a chart layout in a panel.
	 * Every time a new series is added to the chart, the panel updates automatically.
	 */
	public StockChart()
	{
		dataset = new TimeSeriesCollection();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisLabel, yAxisLabel, dataset, 
		                       showLegend, showTooltips, generateUrls);
		
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		chartPanel.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	/**
	 * Creates a StockChart object, with a settings menu attached.
	 * 
	 * @param settingsMenu Settings menu which can trigger changes to StockChart objects
	 */
	public StockChart(Subject settingsMenu)
	{
		this();
		settingsMenu.attach(this);
	}
	
	/**
	 * Returns the panel containing the chart(s).
	 * Before adding any stock data, returns an empty chart layout. 
	 * 
	 * @return The panel
	 */
	public JPanel getPanel()
	{
		return chartPanel;
	}
	
	/**
	 * Removes all charts from the panel.
	 */
	public void resetPanel()
	{
		dataset.removeAllSeries();
	}
	
	/**
	 * Collects historical stock data over a default start date and end date. Adds the stock
	 * data in the chart.
	 * 
	 * @param stockSymbol The symbol of the stock
	 * @throws Exception
	 */
	public void addSeries(String stockSymbol)
	{
		storeChartStyle();
		
		stockSymbol = stockSymbol.toUpperCase();
		
		//add the series only if the series is not currently displayed in the chart
		if (dataset.getSeries(stockSymbol) == null) {
			addSeries(stockSymbol, chartStartDate, chartEndDate);
		}		
		
		restoreChartStyle();
	}
	
	/**
	 * Collects historical stock data over given period of time. Adds the stock
	 * data in the chart.
	 * 
	 * @param stockSymbol The symbol of the stock
	 * @param startDate Start date for the search, in mm/dd/yyyy format
	 * @param endDate End date for the search, in mm/dd/yyyy format
	 * @throws Exception
	 */
	public void addSeries(String stockSymbol, String startDate, String endDate)
	{
		ArrayList<Stock> stockArray = null;
		try {
			stockArray = getStockData(stockSymbol, startDate, endDate);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		TimeSeries newSeries = new TimeSeries(stockSymbol);
		
		for (int i = 0; i < stockArray.size(); i++) {
			//identity the type of data requested by user:
			double rangeValue = rangeTypeClose    ? stockArray.get(i).getClose()
			                  : rangeTypeOpen     ? stockArray.get(i).getOpen()
			                  : rangeTypeAdjClose ? stockArray.get(i).getAdjClose()
			                  : rangeTypeVolume   ? stockArray.get(i).getVolume()
			                  : rangeTypeHigh     ? stockArray.get(i).getHigh()
			                  : rangeTypeLow      ? stockArray.get(i).getLow()
			                  : null;
			
			Date date = new java.util.Date(stockArray.get(i).getDate().getTime());
			newSeries.addOrUpdate(new Day(date), rangeValue);
		}
		
		renderTooltip(getPlot());
		dataset.addSeries(newSeries);
		updateYAxisLabel();
	}
	
	/**
	 * Removes the chart corresponding to the given stock.
	 * 
	 * @param stockSymbol Symbol of the stock
	 */
	public void removeSeries(String stockSymbol)
	{
		storeChartStyle();
		
		dataset.removeSeries(dataset.getSeries(stockSymbol));
		
		restoreChartStyle();
	}
	
	/**
	 * Renders tool-tip in the chart. Hovering mouse over any data point will display
	 * the stock name, x-Axis value (Time) and the y-Axis value (Stock Price) 
	 * 
	 * @param plot plot where tool tip will be activated
	 */
	protected void renderTooltip(XYPlot plot)
	{
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
		
        StandardXYToolTipGenerator tooltip = new StandardXYToolTipGenerator(
        		StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,     //"{0}: ({1}, {2})"
				new SimpleDateFormat("yyyy-MMM-dd"),
				new DecimalFormat("###.00"));
        
        renderer.setBaseToolTipGenerator(tooltip);
		
		plot.setRenderer(renderer);
	}
	
	/**
	 * Updates all the charts with new values for start and end dates or type of
	 * stock data requested.
	 * 
	 * @param newStartDate start date for stock data
	 * @param newEndDate end date for stock data
	 */
	public void updateDataset(String newStartDate, String newEndDate)
	{
		/* overwrites the default date values, so that next stock data chart will have the same
		 * date range as the existing stock chart */
		chartStartDate = newStartDate;
		chartEndDate = newEndDate;
		
		ArrayList<String> currentStocks = currentStocksDisplayed();
		
		for (int i = 0; i < currentStocks.size(); i++) {
			removeSeries(currentStocks.get(i));
			addSeries(currentStocks.get(i));
		}
	}
	
	/**
	 * Stores the colors and shapes for all charts.
	 */
	public void storeChartStyle()
	{
		ArrayList<String> currentStocks = currentStocksDisplayed();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) getPlot().getRenderer();
		
		for (int i = 0; i < currentStocks.size(); i++) {
			plotColors.put(currentStocks.get(i), renderer.getSeriesPaint(i));
			plotShapes.put(currentStocks.get(i), renderer.getSeriesShape(i));
		}
	}
	
	/**
	 * Updates colors and shapes for all charts with their original styles.
	 */
	public void restoreChartStyle()
	{
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) getPlot().getRenderer();
		
		ArrayList<String> currentStocks = currentStocksDisplayed();
		
		for (int i = 0; i < currentStocks.size(); i++) {
			renderer.setSeriesPaint(i, plotColors.get(currentStocks.get(i)));
			renderer.setSeriesShape(i, plotShapes.get(currentStocks.get(i)));
		}
	}
	
	/**
	 * Returns if the panel is showing legends for the charts or not.
	 * @return true if legend is shown, or false otherwise.
	 */
	public boolean isShowLegend()
	{
		return showLegend;
	}
	
	/**
	 * Legend is added to the chart by default. Calling this method will remove them.
	 * 
	 * @return Current StockChart object
	 */
	public StockChart hideLegend()
	{
		getChart().getLegend().setVisible(false);
		showLegend = false;
		
		return this;
	}
	
	/**
	 * Legend is added to the chart.
	 */
	public void showLegend()
	{
		getChart().getLegend().setVisible(true);
		showLegend = true;
	}
	
	/**
	 * Returns names of stocks displayed currently in the panel.
	 * @return ArrayList of stock names
	 */
	protected ArrayList<String> currentStocksDisplayed()
	{
		ArrayList<String> stockNames = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		List<TimeSeries> series = dataset.getSeries();
		
		for (TimeSeries data : series) {
			stockNames.add((String) data.getKey());
		}
		
		return stockNames;
	}
	
	/**
	 * Returns number of charts in the panel.
	 * @return Number of charts
	 */
	public int getNumberOfSeries()
	{
		return dataset.getSeriesCount();
	}
	
	public String getChartTitle()
	{
		return chartTitle;
	}
	
	public StockChart setChartTitle(String chartTitle)
	{
		this.chartTitle = chartTitle;
		getChart().setTitle(chartTitle);
		
		return this;
	}
	
	public String getXAxisLabel()
	{
		return xAxisLabel;
	}
	
	public StockChart setXAxisLabel(String xAxisLabel)
	{
		this.xAxisLabel = xAxisLabel;
		getPlot().getDomainAxis().setLabel(xAxisLabel);
		
		return this;
	}
	
	public String getYAxisLabel()
	{
		return yAxisLabel;
	}
	
	public StockChart setYAxisLabel(String yAxisLabel)
	{
		this.yAxisLabel = yAxisLabel;
		getPlot().getRangeAxis().setLabel(yAxisLabel);
		
		return this;
	}
	
	/**
	 * Updates Y-Axis label with the correct string based on the type of data
	 * displayed in the chart
	 */
	protected void updateYAxisLabel()
	{
		String label = rangeTypeClose    ? "Closing Price"
                     : rangeTypeOpen     ? "Opening Price"
                     : rangeTypeAdjClose ? "Adjusted Closing Price"
                     : rangeTypeVolume   ? "Volume"
                     : rangeTypeHigh     ? "Daily High"
                     : rangeTypeLow      ? "Daily Low"
                     : null;
		
		setYAxisLabel(label);
	}
	
	public Dimension getPanelSize()
	{
		return new Dimension(panelWidth, panelHeight);
	}
	
	public StockChart setPanelSize(int panelWidth, int panelHeight)
	{
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		chartPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		
		return this;
	}
	
	public StockChart setShowTooltips(boolean showTooltips)
	{
		this.showTooltips = showTooltips;
		chartPanel.setDisplayToolTips(showTooltips);
		
		return this;
	}
	
	public int getPanelWidth()
	{
		return panelWidth;
	}
	
	public StockChart setPanelWidth(int panelWidth)
	{
		return setPanelSize(panelWidth, panelHeight);
	}
	
	public int getPanelHeight()
	{
		return panelHeight;
	}
	
	public StockChart setPanelHeight(int panelHeight)
	{
		return setPanelSize(panelWidth, panelHeight);
	}
	
	public String getCurrentStartDate()
	{
		return chartStartDate;
	}
	
	public String getCurrentEndDate()
	{
		return chartEndDate;
	}
	
	public boolean getRangeTypeClose()
	{
		return rangeTypeClose;
	}
	
	public boolean getRangeTypeOpen()
	{
		return rangeTypeOpen;
	}
	
	public boolean getRangeTypeAdjClose()
	{
		return rangeTypeAdjClose;
	}
	
	public boolean getRangeTypeVolume()
	{
		return rangeTypeVolume;
	}
	
	public boolean getRangeTypeHigh()
	{
		return rangeTypeHigh;
	}
	
	public boolean getRangeTypeLow()
	{
		return rangeTypeLow;
	}
	
	/**
	 * Returns a string representation of the type of data displayed in the chart.
	 * @return Range type
	 */
	public String rangeTypeToString()
	{
		return  rangeTypeClose    ? "Close"
              : rangeTypeOpen     ? "Open"
              : rangeTypeAdjClose ? "Adjusted Close"
              : rangeTypeVolume   ? "Volume"
              : rangeTypeHigh     ? "High"
              : rangeTypeLow      ? "Low"
              : null;
	}
	
	protected XYPlot getPlot()
	{
		return getChart().getXYPlot();
	}
	
	protected JFreeChart getChart()
	{
		return chartPanel.getChart();
	}
	
	/**
	 * Updates all labels with the current language setup.
	 */
	@Override
	public boolean updateLabels()
	{
		xAxisLabel = LocalizedStrings.getLocalString(TextFields.CHART_XAXIS);
		yAxisLabel = LocalizedStrings.getLocalString(TextFields.CHART_YAXIS);
		
		setXAxisLabel(xAxisLabel);
		setYAxisLabel(yAxisLabel);
		
		return true;
	}
	
	/**
	 * Returns stock data for a given stock name, and given start and end dates.
	 * 
	 * @param stockSymbol The symbol of the stock
	 * @param startDate Start date for the stock data
	 * @param endDate End date for the stock data
	 * @return ArrayList of Stock objects
	 */
	protected ArrayList<Stock> getStockData(String stockSymbol, String startDate, String endDate)
	{
		StockDatabaseInterface stockData = new StockDatabaseInterface();
		
		ArrayList<Stock> stockArray = null;
		
		try {
			stockArray = stockData.getStocks(stockSymbol, Utilities.stringToSqlDate(startDate, "MM/dd/yyyy"),
			                                              Utilities.stringToSqlDate(endDate, "MM/dd/yyyy"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return stockArray;
	}
	
	/**
	 * Notifies the StockChart object with changes in settings triggered by the settings menu.
	 * 
	 * @param newStartDate start date for stock data
	 * @param newEndDate end date for stock data
	 * @param newRangeType type of stock data
	 */
	public void settingsChanged(String newStartDate, String newEndDate, String newRangeType)
	{
		if (!rangeTypeToString().equals(newRangeType)) {
			setAllRangeTypeFalse();
			
			switch (newRangeType) {
			case "Open":
				rangeTypeOpen = true;
				break;
			case "Close":
				rangeTypeClose = true;
				break;
			case "Adjusted Close":
				rangeTypeAdjClose = true;
				break;
			case "Volume":
				rangeTypeVolume = true;
				break;
			case "High":
				rangeTypeHigh = true;
				break;
			case "Low":
				rangeTypeLow = true;
				break;
			}
		}
		
		updateDataset(newStartDate, newEndDate);
	}
	
	private void setAllRangeTypeFalse()
	{
		rangeTypeClose = false;
		rangeTypeOpen = false;
		rangeTypeAdjClose = false;
	    rangeTypeVolume = false;
	    rangeTypeHigh = false;
	    rangeTypeLow = false;
	}
}
