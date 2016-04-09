package com.finance.main.java.chart;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.text.DecimalFormat;
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
 * This chart implements tool-tip generator. Hovering mouse over a data point in the chart
 * will display the stock name, the time and the stock price in the format:
 * 		stockName: (time, price)
 * 
 * 
 * To get the panel containing chart,
 * instantiate the class with no parameters, and call getPanel().
 * 
 * This class uses the Builder design pattern. Therefore, to change the panel's width or height
 * or other default parameters, use the following:
 * 
 * StockChart newChart = new StockChart().setPanelWidth(700)
 *                                       .setPanelHeight(400)
 *                                       .setXAxisLabel("Date")
 *                                       .setShowLegend(false); 
 * 
 * @author MI ONIM
 *
 */
public class StockChart implements Localized
{
	protected ChartPanel chartPanel;
	protected TimeSeriesCollection dataset;
	
	protected String chartTitle = "";
	protected String xAxisLabel;
	protected String yAxisLabel;
	protected boolean showLegend = true;
	protected boolean showTooltips = true;
	protected boolean generateUrls = false;
	protected int panelWidth = 650;
	protected int panelHeight = 367;
	
	public StockChart()
	{
		updateLabels();
		dataset = new TimeSeriesCollection();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisLabel, yAxisLabel, dataset, 
		                       showLegend, showTooltips, generateUrls);
		
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
	}
	
	/**
	 * 
	 * @return The panel containing the chart
	 */
	public JPanel getPanel()
	{
		return chartPanel;
	}
	
	/**
	 * Collects stock data and add it to the dataset of the panel
	 * 
	 * @param stockSymbol The symbol of the stock
	 * @param startDate Start date for the search, in mm/dd/yyyy format
	 * @param endDate End date for the search, in mm/dd/yyyy format
	 * @throws Exception
	 */
	public void addDataset(String stockSymbol, String startDate, String endDate) throws Exception
	{
		StockDatabaseInterface stockData = new StockDatabaseInterface();
		
		SimpleDateFormat dateFomatter = new SimpleDateFormat("MM/dd/yyyy");
		Date date1 = dateFomatter.parse(startDate);
		Date date2 = dateFomatter.parse(endDate);
		
		ArrayList<Stock> stockArray = stockData.getStocks(stockSymbol, new java.sql.Date(date1.getTime()), 
					new java.sql.Date(date2.getTime()));
		
		TimeSeries newSeries = new TimeSeries(stockSymbol);
		
		for (int i = 0; i < stockArray.size(); i++) {
			Date date = new java.util.Date(stockArray.get(i).getDate().getTime());
			newSeries.add(new Day(date), stockArray.get(i).getClose());
		}
		
		renderTooltip(chartPanel.getChart().getXYPlot());
		
		this.dataset.addSeries(newSeries);
	}
	
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
	
	public String getChartTitle()
	{
		return chartTitle;
	}
	
	public StockChart setChartTitle(String chartTitle)
	{
		this.chartTitle = chartTitle;
		return this;
	}
	
	public String getXAxisLabel()
	{
		return xAxisLabel;
	}
	
	public StockChart setXAxisLabel(String xAxisLabel)
	{
		this.xAxisLabel = xAxisLabel;
		return this;
	}
	
	public String getYAxisLabel()
	{
		return yAxisLabel;
	}
	
	public StockChart setYAxisLabel(String yAxisLabel)
	{
		this.yAxisLabel = yAxisLabel;
		return this;
	}
	
	public StockChart setShowLegend(boolean showLegend)
	{
		this.showLegend = showLegend;
		return this;
	}
	
	public StockChart setShowTooltips(boolean showTooltips)
	{
		this.showTooltips = showTooltips;
		return this;
	}
	
	public int getPanelWidth()
	{
		return panelWidth;
	}
	
	public StockChart setPanelWidth(int panelWidth)
	{
		this.panelWidth = panelWidth;
		return this;
	}
	
	public int getPanelHeight()
	{
		return panelHeight;
	}
	
	public StockChart setPanelHeight(int panelHeight)
	{
		this.panelHeight= panelHeight;
		return this;
	}
	
	@Override
	public boolean updateLabels() {
		LocalizedStrings.setLanguage(Languages.ENGLISH_US);
		
		xAxisLabel = LocalizedStrings.getLocalString(TextFields.CHART_XAXIS);
		yAxisLabel = LocalizedStrings.getLocalString(TextFields.CHART_YAXIS);
		
		return false;
	}
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
            public void run() {
            	JFrame frame = new JFrame();
				//frame.setBounds(100, 100, 800, 600);
            	
            	StockChart contentPane = new StockChart();
				frame.add(contentPane.getPanel());
				
				try {
					contentPane.addDataset("GOOG", "05/20/2014", "12/30/2014");
					contentPane.addDataset("YHOO", "05/20/2014", "12/30/2014");
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
