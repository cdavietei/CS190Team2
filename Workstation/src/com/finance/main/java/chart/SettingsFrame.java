package com.finance.main.java.chart;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.finance.main.java.enums.TextFields;
import com.finance.main.java.util.Localized;
import com.finance.main.java.util.LocalizedStrings;


/**
 * This is the settings menu for StockChart class. Using the observer pattern,
 * any changes made by users in the settings window sends a notification to the
 * StockChart, which will make the necessary updates.
 * 
 * @author MI ONIM
 *
 */
public class SettingsFrame implements Subject, Localized
{
	/* Constants used for GridBag Constraints */
	public static int ROW_1 = 0;
	public static int ROW_2 = 1;
	public static int ROW_3 = 2;
	public static int ROW_4 = 3;
	public static int ROW_5 = 4;
	public static int ROW_6 = 5;
	public static int COL_1 = 0;
	public static int COL_2 = 1;
	public static int COL_3 = 2;
	public static int COL_4 = 3;
	
	public static int LEFT = 1;
	public static int CENTER = 2;
	public static int RIGHT = 3;
	
	private StockChart observer;
	
	protected JFrame frame = new JFrame("Settings for Stock Chart");
	protected JPanel mainPanel = new JPanel();
	protected JLabel startDate = new JLabel(LocalizedStrings.getLocalString(TextFields.START_DATE)+":");
	protected JLabel endDate = new JLabel(LocalizedStrings.getLocalString(TextFields.END_DATE)+":");
	protected JTextField startDateText = new JTextField(10);
	protected JTextField endDateText = new JTextField(10);
	protected JButton apply = new JButton(LocalizedStrings.getLocalString(TextFields.TABLE_APPLY));
	protected JButton cancel = new JButton(LocalizedStrings.getLocalString(TextFields.TABLE_CANCEL));
	protected ButtonGroup buttonGroup = new ButtonGroup();
	protected JRadioButton open = new JRadioButton(LocalizedStrings.getLocalString(TextFields.TABLE_OPEN));
	protected JRadioButton close = new JRadioButton(LocalizedStrings.getLocalString(TextFields.TABLE_CLOSE));
	protected JRadioButton adjClose = new JRadioButton(LocalizedStrings.getLocalString(TextFields.TABLE_ADJCLOSE));
	protected JRadioButton volume = new JRadioButton(LocalizedStrings.getLocalString(TextFields.TABLE_VOLUME));
	protected JRadioButton high = new JRadioButton(LocalizedStrings.getLocalString(TextFields.TABLE_HIGH));
	protected JRadioButton low = new JRadioButton(LocalizedStrings.getLocalString(TextFields.TABLE_LOW));
	
	protected GridBagLayout layout = new GridBagLayout();
	
	protected String prevStartDate;
	protected String prevEndDate;
	protected String prevRangeType;
	protected ArrayList<Component> tempComponents = new ArrayList<>();
	
	/**
	 * Default Constructor
	 */
	public SettingsFrame()
	{
		initializeComponents();
		configureButtons();
	}
	
	/**
	 * Sets the settings window visible.
	 */
	public void showFrame()
	{
		frame.setVisible(true);
	}
	
	/**
	 * Hides the settings window.
	 */
	public void hideFrame()
	{
		frame.setVisible(false);
	}
	
	/**
	 * Sets up the permanent components in the settings window.
	 */
	protected void initializeComponents()
	{
		hideFrame();
		mainPanel.setLayout(layout);
		
		mainPanel.add(new JLabel("Enter Dates for search (in mm/dd/yyyy):"), buildConstraintsHeader(ROW_1));
		
		mainPanel.add(new JLabel("Start Date:"), buildConstraints(ROW_2, COL_1, CENTER));
		mainPanel.add(startDateText, buildConstraints(ROW_2, COL_2));
		mainPanel.add(new JLabel("End Date:"), buildConstraints(ROW_2, COL_3, CENTER));
		mainPanel.add(endDateText, buildConstraints(ROW_2, COL_4));
		
		addLegendAndResetButton();
		
		mainPanel.add(new JLabel("Select the type of Stock data to display:"), buildConstraintsHeader(ROW_4));
		mainPanel.add(open, buildConstraints(ROW_5, COL_1));
		mainPanel.add(close, buildConstraints(ROW_5, COL_2));
		mainPanel.add(adjClose, buildConstraints(ROW_5, COL_3));
		mainPanel.add(volume, buildConstraints(ROW_6, COL_1));
		mainPanel.add(high, buildConstraints(ROW_6, COL_2));
		mainPanel.add(low, buildConstraints(ROW_6, COL_3));
		
		/* Make the columns equal sized */
		open.setPreferredSize(new Dimension(110,25));
		close.setPreferredSize(new Dimension(110,25));
		adjClose.setPreferredSize(new Dimension(110,25));
		endDateText.setPreferredSize(new Dimension(110,25));
		
		frame.setContentPane(mainPanel);
		frame.pack();
	}
	
	/**
	 * Returns a GridBagConstraints to be used by GridBagLayout manager. Places the component
	 * in the given position inside the window, and align to the left.
	 * 
	 * @param row The row number to place the component
	 * @param col The column number to place the component
	 * @return GridBagConstraints object
	 */
	protected GridBagConstraints buildConstraints(int row, int col)
	{
		return buildConstraints(row, col, LEFT);
	}
	
	/**
	 * Returns a GridBagConstraints to be used by GridBagLayout manager. Places the component
	 * in the given position inside the window, and align to the given value.
	 * 
	 * @param row The row number to place the component
	 * @param col The column number to place the component
	 * @param orientation The alignment position
	 * @return GridBagConstraints object
	 */
	protected GridBagConstraints buildConstraints(int row, int col, int orientation)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridy = row;
		constraints.gridx = col;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = orientation == LEFT   ? GridBagConstraints.LINE_START
		                   : orientation == CENTER ? GridBagConstraints.CENTER
		                   : orientation == RIGHT  ? GridBagConstraints.LINE_END
		                   : GridBagConstraints.LINE_START;
		
		return constraints;
	}
	
	/**
	 * A specialized GridBagConstraints for the headers inside the Settings window.
	 * The component is placed according the given row. The component takes the full width
	 * of the row, and has a little padding in the top.
	 * 
	 * @param row The row number to place the component
	 * @return GridBagConstraints object
	 */
	protected GridBagConstraints buildConstraintsHeader(int row)
	{
		GridBagConstraints constraints = buildConstraints(row, 0);
		constraints.gridwidth = 4;
		constraints.insets = new Insets(10, 5, 5, 5);
		
		return constraints;
	}
	
	/**
	 * Returns a JButton to open the settings window.
	 * 
	 * @return A Button
	 */
	public JButton createSettingsButton()
	{
		JButton settings = new JButton(new ImageIcon("Resources/Images/settingsIcon.gif"));
		settings.setPreferredSize(new Dimension(25, 25));
		
		settings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				update();
				showFrame();
				
			}
		});
		
		return settings;
	}
	
	/**
	 * Number of rows in the settings window as laid out by the layout manager.
	 * 
	 * @return Number of rows
	 */
	protected int countLayoutRows()
	{
		return layout.getLayoutDimensions()[1].length;
	}
	
	/**
	 * Number of columns in the settings window as laid out by the layout manager.
	 * 
	 * @return Number of columns
	 */
	protected int countLayoutCols()
	{
		return layout.getLayoutDimensions()[0].length;
	}
	
	/**
	 * Adds all the radio buttons to a ButtonGroup, so that only one of them can be selected at a time.
	 */
	protected void configureButtons()
	{
		buttonGroup.add(open);
		buttonGroup.add(close);
		buttonGroup.add(adjClose);
		buttonGroup.add(volume);
		buttonGroup.add(high);
		buttonGroup.add(low);
	}
	
	/**
	 * Returns a string representation of the radio button selected.
	 * 
	 * @return Text of the selected radio button
	 */
	protected String getSelectedButtonText()
	{
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
	
	/**
	 * Adds a button to show/hide legend from the StockChart panel, and another button
	 * to reset the panel with one click.
	 */
	protected void addLegendAndResetButton()
	{
		JButton toggleLegend = new JButton("Hide Legend");
		mainPanel.add(toggleLegend, buildConstraints(ROW_3, COL_2));
		toggleLegend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (observer.isShowLegend()) {
					observer.hideLegend();
					toggleLegend.setText("Show Legend");
				}
				else if (!observer.isShowLegend()) {
					observer.showLegend();
					toggleLegend.setText("Hide Legend");
				}
			}
		});
		
		JButton resetPanel = new JButton("Reset Panel");
		mainPanel.add(resetPanel, buildConstraints(ROW_3, COL_3));
		resetPanel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				observer.resetPanel();
				addCurrentChartLabels();
			}
		});
		
		frame.pack();
	}
	
	/**
	 * Adds an apply button that sends notification to the StockChart object, and another button
	 * that exits from the settings window.
	 */
	protected void addApplyAndCancelButton()
	{
		JButton apply = new JButton("Apply");
		mainPanel.add(apply, buildConstraints(countLayoutRows(), 1, CENTER));
		tempComponents.add(apply);
		apply.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String newStartDate = startDateText.getText();
				String newEndDate = endDateText.getText();
				String newRangeType = getSelectedButtonText();
				
				if (!prevStartDate.equals(newStartDate) || !prevEndDate.equals(newEndDate)
						|| !prevRangeType.equals(newRangeType)) {
					observer.settingsChanged(newStartDate, newEndDate, newRangeType);
				}
				
				frame.setVisible(false);
			}
		});
		
		JButton cancel = new JButton("Cancel");
		mainPanel.add(cancel, buildConstraints(countLayoutRows(), 2, CENTER));
		tempComponents.add(cancel);
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				hideFrame();
			}
		});
		
		frame.pack();
	}
	
	/**
	 * Attaches the StockChart object with this class.
	 */
	@Override
	public void attach(StockChart observer)
	{
		this.observer = observer;
	}
	
	/**
	 * Updates the settings options based on the latest view of the StockChart panel.
	 */
	@Override
	public void update()
	{
		frame.setLocationRelativeTo(observer.getPanel());
		
		prevStartDate = observer.getCurrentStartDate();
		prevEndDate = observer.getCurrentEndDate();
		startDateText.setText(prevStartDate);
		endDateText.setText(prevEndDate);
		
		if (observer.getRangeTypeClose()) {
			close.setSelected(true);
		}
		else if (observer.getRangeTypeAdjClose()) {
			adjClose.setSelected(true);
		}
		else if (observer.getRangeTypeOpen()) {
			open.setSelected(true);
		}
		else if (observer.getRangeTypeVolume()) {
			volume.setSelected(true);
		}
		else if (observer.getRangeTypeHigh()) {
			high.setSelected(true);
		}
		else if (observer.getRangeTypeLow()) {
			low.setSelected(true);
		}
		
		prevRangeType = getSelectedButtonText();
		
		addCurrentChartLabels();
	}
	
	/**
	 * Adds the list of current charts displayed in the StockChart panel and a button
	 * to remove them from the settings window.
	 */
	protected void addCurrentChartLabels()
	{
		removeTemporaryComponents();
		
		if (observer.getNumberOfSeries() > 0) {
			int layoutRows = countLayoutRows();
			
			JLabel header = new JLabel("Current Stocks displayed:");
			tempComponents.add(header);
			mainPanel.add(header, buildConstraintsHeader(layoutRows++));
			
			for (String series : observer.currentStocksDisplayed()) {
				JButton remove = new JButton("Remove");
				remove.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e)
					{
						observer.removeSeries(series);
						addCurrentChartLabels();
					}
				});
				
				JLabel name = new JLabel(series);
				tempComponents.add(name);
				tempComponents.add(remove);
				mainPanel.add(name, buildConstraints(layoutRows, 1, CENTER));
				mainPanel.add(remove, buildConstraints(layoutRows++, 2, CENTER));
			}
			
			frame.pack();
		}
		
		addApplyAndCancelButton();     //add at last
	}
	
	/**
	 * Removes temporary components to avoid duplicate components.
	 */
	protected void removeTemporaryComponents()
	{
		for (Component comp : tempComponents) {
			mainPanel.remove(comp);
			mainPanel.revalidate();
		}
	}
	
	/**
	 * Updates all labels with the current language setup.
	 */
	@Override
	public boolean updateLabels()
	{
		startDate.setText(LocalizedStrings.getLocalString(TextFields.START_DATE)+":");
		endDate.setText(LocalizedStrings.getLocalString(TextFields.END_DATE)+":");
		apply.setText(LocalizedStrings.getLocalString(TextFields.TABLE_APPLY));
		cancel.setText(LocalizedStrings.getLocalString(TextFields.TABLE_CANCEL));
		open.setText(LocalizedStrings.getLocalString(TextFields.TABLE_OPEN));
		close.setText(LocalizedStrings.getLocalString(TextFields.TABLE_CLOSE));
		adjClose.setText(LocalizedStrings.getLocalString(TextFields.TABLE_ADJCLOSE));
		volume.setText(LocalizedStrings.getLocalString(TextFields.TABLE_VOLUME));
		high.setText(LocalizedStrings.getLocalString(TextFields.TABLE_HIGH));
		low.setText(LocalizedStrings.getLocalString(TextFields.TABLE_LOW));
		
		return true;
	}
}
