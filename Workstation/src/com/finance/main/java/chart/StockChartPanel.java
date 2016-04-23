package com.finance.main.java.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
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
import com.finance.main.java.enums.Languages;
import com.finance.main.java.enums.TextFields;
import com.finance.main.java.stock.Stock;
import com.finance.main.java.util.Localized;
import com.finance.main.java.util.LocalizedStrings;

public class StockChartPanel extends JPanel implements Localized {

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
	
	SettingsFrame settingsFrame = new SettingsFrame();
	
	/**
	 * Default constructor for StockChart. Creates a chart layout in a panel.
	 * Every time a new dataset is added to the chart, the panel updates automatically.
	 */
	public StockChartPanel()
	{
		dataset = new TimeSeriesCollection();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisLabel, yAxisLabel, dataset, 
		                       showLegend, showTooltips, generateUrls);
		
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		//chartPanel.setBorder(new CompoundBorder(new EmptyBorder(10,10,10,10), 
		//BorderFactory.createLineBorder(Color.black)));
		chartPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		settingsFrame.attach(this);
		
		add(chartPanel, BorderLayout.NORTH);
		add(createSettingsButton());
	}
	
	/**
	 * Returns the panel containing the chart(s).
	 * Before adding any stock data, returns a chart layout. 
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
	 * @param stockSymbol
	 * @return
	 * @throws Exception
	 */
	public boolean addSeries(String stockSymbol)
	{
		return addSeries(stockSymbol, chartStartDate, chartEndDate);
	}
	
	/**
	 * Collects historical stock data over given period of time. Adds the stock
	 * data in the chart.
	 * 
	 * @param stockSymbol The symbol of the stock
	 * @param startDate Start date for the search, in mm/dd/yyyy format
	 * @param endDate End date for the search, in mm/dd/yyyy format
	 * @return True if the stock data chart has been created successfully or false otherwise
	 * @throws Exception
	 */
	public boolean addSeries(String stockSymbol, String startDate, String endDate)
	{
		/* overwrites the default date values, so that next stock data chart will have the same
		 * date range as the existing stock chart */
		chartStartDate = startDate;
		chartEndDate = endDate;
		
		ArrayList<Stock> stockArray = null;
		try {
			stockArray = getStockData(stockSymbol, startDate, endDate);
		}
		catch (Exception e) {
			return false;     //stock data could not be retrieved successfully
		}
		
		TimeSeries newSeries = new TimeSeries(stockSymbol);
		
		for (int i = 0; i < stockArray.size(); i++) {
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
		
		return true;
	}
	
	/**
	 * Removes the chart for the given stock.
	 * 
	 * @param stockSymbol Symbol of the stock
	 */
	public void removeSeries(String stockSymbol)
	{
		dataset.removeSeries(dataset.getSeries(stockSymbol));
	}
	
	/**
	 * Renders tool-tip in the chart. Hovering mouse over any data point will display
	 * the stock name, x-Axis value (Time) and the y-Axis value (Stock Price) 
	 * 
	 * @param plot
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
	
	public void updateDataset(String newStartDate, String newEndDate)
	{
		chartStartDate = newStartDate;
		chartEndDate = newEndDate;
		
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) getPlot().getRenderer();
		ArrayList<Paint> plotColors = new ArrayList<>();
		ArrayList<Shape> plotShapes = new ArrayList<>();
		ArrayList<String> currentStocks = currentStocksDisplayed();
		
		for (int i = 0; i < currentStocks.size(); i++) {
			plotColors.add(renderer.getSeriesPaint(i));
			plotShapes.add(renderer.getSeriesShape(i));
			
			removeSeries(currentStocks.get(i));
			addSeries(currentStocks.get(i));
		}
		
		renderer = (XYLineAndShapeRenderer) getPlot().getRenderer();
		for (int i = 0; i < currentStocks.size(); i++) {
			renderer.setSeriesPaint(i, plotColors.get(i));
			renderer.setSeriesShape(i, plotShapes.get(i));
		}
	}
	
	/**
	 * Legend is added to the chart by default. Calling this method will remove them.
	 * 
	 * @return Current StockChartPanel object
	 */
	public StockChartPanel removeLegend()
	{
		this.showLegend = false;
		getChart().removeLegend();
		
		return this;
	}
	
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
	
	public int getNumberOfSeries()
	{
		return dataset.getSeriesCount();
	}
	
	public String getChartTitle()
	{
		return chartTitle;
	}
	
	public StockChartPanel setChartTitle(String chartTitle)
	{
		this.chartTitle = chartTitle;
		getChart().setTitle(chartTitle);
		
		return this;
	}
	
	public String getXAxisLabel()
	{
		return xAxisLabel;
	}
	
	public StockChartPanel setXAxisLabel(String xAxisLabel)
	{
		this.xAxisLabel = xAxisLabel;
		getPlot().getDomainAxis().setLabel(xAxisLabel);
		
		return this;
	}
	
	public String getYAxisLabel()
	{
		return yAxisLabel;
	}
	
	public StockChartPanel setYAxisLabel(String yAxisLabel)
	{
		this.yAxisLabel = yAxisLabel;
		getPlot().getRangeAxis().setLabel(yAxisLabel);
		
		return this;
	}
	
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
	
	public StockChartPanel setPanelSize(int panelWidth, int panelHeight)
	{
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		chartPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		
		return this;
	}
	
	public StockChartPanel setShowTooltips(boolean showTooltips)
	{
		this.showTooltips = showTooltips;
		chartPanel.setDisplayToolTips(showTooltips);
		
		return this;
	}
	
	public int getPanelWidth()
	{
		return panelWidth;
	}
	
	public StockChartPanel setPanelWidth(int panelWidth)
	{
		return setPanelSize(panelWidth, panelHeight);
	}
	
	public int getPanelHeight()
	{
		return panelHeight;
	}
	
	public StockChartPanel setPanelHeight(int panelHeight)
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
	 * Updates all labels with the current language setup
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
	
	public JButton createSettingsButton()
	{
		JButton settings = new JButton("Settings", new ImageIcon("Resources/Images/settingsIcon.gif"));
		settings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				settingsFrame.update();
				settingsFrame.showFrame();
				
			}
		});
		
		return settings;
	}
	
	protected ArrayList<Stock> getStockData(String stockSymbol, String startDate, String endDate) throws Exception
	{
		StockDatabaseInterface stockData = new StockDatabaseInterface();
		
		SimpleDateFormat dateFomatter = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = dateFomatter.parse(startDate);
		Date date2 = dateFomatter.parse(endDate);
		
		ArrayList<Stock> stockArray = null;
		
		try {
			stockArray = stockData.getStocks(stockSymbol, new java.sql.Date(date1.getTime()), 
					new java.sql.Date(date2.getTime()));
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return stockArray;
	}
	
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
	
	public static void main(String[] args)
	{
		LocalizedStrings.language = Languages.ENGLISH_US;
		LocalizedStrings.update();
		
		//SettingsFrame settingsFrame = new SettingsFrame();
		
		/*JButton settings = new JButton("Settings");
		settings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.setVisible(true);
				
			}
		});*/
		
		EventQueue.invokeLater(new Runnable() {
            public void run() {
            	JPanel frame = new JPanel();
				
            	StockChartPanel contentPane = new StockChartPanel();
            	
            	
            	
				frame.add(contentPane.getPanel(), BorderLayout.NORTH);
				frame.add(contentPane.createSettingsButton());
				//settingsFrame.attach(contentPane);
				
				
				try {
					contentPane.addSeries("GOOG");
					contentPane.addSeries("YHOO");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				frame.setVisible(true);
            }
         });
	}

}
