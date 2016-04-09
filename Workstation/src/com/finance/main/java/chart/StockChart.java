package com.finance.main.java.chart;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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


/**
 * Displays graphical view of one or more stocks.
 * 
 * The chart(s) implements tool-tip generator. Hovering mouse over a data point in the chart
 * will display the stock name, the time and the stock price in the format:
 * 		stockName: (time, price)
 * 
 * 
 * To get the panel containing chart(s),
 * instantiate the class with no parameters, and call getPanel().
 * 
 * This class uses the Builder design pattern. Therefore, to change the panel's width or height
 * or other default parameters, use the following:
 * 
 * StockChart newChart = new StockChart().setPanelSize(800, 600)
 *                                       .setPanelHeight(400)
 *                                       .setXAxisLabel("Date")
 *                                       .setShowLegend(false); 
 * 
 * To compare two or more stocks on the same panel, calling addDataset(...) will create
 * chart for each stock. For example, to compare Google and Yahoo stocks over May 20, 2015 to 
 * May 30, 2015, use the following:
 * 
 * StockChart newChart = new StockChart();
 * newChart.addDataset("GOOG", "05/20/2015", "05/30/2015");
 * newChart.addDataset("YHOO", "05/20/2015", "05/30/2015");
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
	
	/**
	 * Default constructor for StockChart. Creates a chart layout in a panel.
	 * Every time a new dataset is added to the chart, the panel updates automatically.
	 */
	public StockChart()
	{
		dataset = new TimeSeriesCollection();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisLabel, yAxisLabel, dataset, 
		                       showLegend, showTooltips, generateUrls);
		
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
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
	 * Collects historical stock data over a default period of time. Adds the stock
	 * data in the dataset of the chart. 
	 * 
	 * @param stockSymbol
	 * @return
	 * @throws Exception
	 */
	public boolean addDataset(String stockSymbol) throws Exception
	{
		return addDataset(stockSymbol, chartStartDate, chartEndDate);
	}
	
	/**
	 * Collects historical stock data over given period of time. Adds the stock
	 * data in the dataset of the chart.
	 * 
	 * @param stockSymbol The symbol of the stock
	 * @param startDate Start date for the search, in mm/dd/yyyy format
	 * @param endDate End date for the search, in mm/dd/yyyy format
	 * @return True if the stock data chart has been created successfully or false otherwise
	 * @throws Exception
	 */
	public boolean addDataset(String stockSymbol, String startDate, String endDate) throws Exception
	{
		/* overwrites the default date values, so that next stock data chart will have the same
		 * date range as the existing stock chart */
		chartStartDate = startDate;
		chartEndDate = endDate;
		
		StockDatabaseInterface stockData = new StockDatabaseInterface();
		
		SimpleDateFormat dateFomatter = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = dateFomatter.parse(startDate);
		Date date2 = dateFomatter.parse(endDate);
		
		ArrayList<Stock> stockArray = null;
		
		try {
			stockArray = stockData.getStocks(stockSymbol, new java.sql.Date(date1.getTime()), 
					new java.sql.Date(date2.getTime()));
		}
		catch (Exception e) {
			return false;     //stock data could not be retrieved successfully
		}
		
		TimeSeries newSeries = new TimeSeries(stockSymbol);
		
		for (int i = 0; i < stockArray.size(); i++) {
			Date date = new java.util.Date(stockArray.get(i).getDate().getTime());
			newSeries.add(new Day(date), stockArray.get(i).getClose());
		}
		
		renderTooltip(getPlot());
		dataset.addSeries(newSeries);
		
		return true;
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
	
	/**
	 * Legend is added to the chart by default. Calling this method will remove them.
	 * 
	 * @return Current StockChart object
	 */
	public StockChart removeLegend()
	{
		this.showLegend = false;
		getChart().removeLegend();
		
		return this;
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
	
	public static void main(String[] args)
	{
		LocalizedStrings.language = Languages.ENGLISH_US;
		LocalizedStrings.update();
		
		EventQueue.invokeLater(new Runnable() {
            public void run() {
            	JFrame frame = new JFrame();
				
            	StockChart contentPane = new StockChart();
				frame.add(contentPane.getPanel());
				
				try {
					contentPane.addDataset("GOOG", "05/20/2015", "08/20/2015");
					//contentPane.addDataset("YHOO", "05/20/2014", "06/20/2014");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocationRelativeTo(null);
            }
         });
	}
}
