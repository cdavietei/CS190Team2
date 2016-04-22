package com.finance.main.java.chart;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

class SettingsFrame implements Subject
{
	private StockChartPanel observer;
	protected ArrayList<Component> newComponents = new ArrayList<>();
	protected JFrame frame = new JFrame("Settings for Stock Chart");
	protected JPanel mainPanel = new JPanel();
	protected JLabel startDate = new JLabel("Start Date:");
	protected JLabel endDate = new JLabel("End Date:");
	protected JTextField startDateText = new JTextField(10);
	protected JTextField endDateText = new JTextField(10);
	protected JButton apply = new JButton("Apply");
	protected JButton cancel = new JButton("Cancel");
	protected ButtonGroup buttonGroup = new ButtonGroup();
	protected JRadioButton open = new JRadioButton("Open");
	protected JRadioButton close = new JRadioButton("Close");
	protected JRadioButton adjClose = new JRadioButton("Adjusted Close");
	protected JRadioButton volume = new JRadioButton("Volume");
	protected JRadioButton high = new JRadioButton("High");
	protected JRadioButton low = new JRadioButton("Low");
	
	protected GridBagLayout layout = new GridBagLayout();
	protected GridBagConstraints constraints = new GridBagConstraints();
	
	protected String prevStartDate;
	protected String prevEndDate;
	protected String prevRangeType;
	
	public SettingsFrame()
	{
		initializeComponents();
		configureButtons();
	}
	
	public void showFrame()
	{
		frame.setVisible(true);
	}
	
	protected void initializeComponents()
	{
		frame.setVisible(false);
		mainPanel.setLayout(layout);
		
		mainPanel.add(new JLabel("Enter Dates for stock data (in mm/dd/yyyy):"), buildConstraints(0,0,2));
		//enterDate.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
		//mainPanel.add(buildLabel("<html><hr></html"));
		
		mainPanel.add(startDate, buildConstraints(1,0,1));
		mainPanel.add(startDateText, buildConstraints(1,1,1));
		mainPanel.add(endDate, buildConstraints(1,2,1));
		mainPanel.add(endDateText, buildConstraints(1,3,1));
		
		//mainPanel.add(Box.createRigidArea(new Dimension(20,20)));
		mainPanel.add(new JLabel("Select the type of Stock data to display:"), buildConstraints(2,0,2));
		mainPanel.add(open, buildConstraints(3,0,1));
		mainPanel.add(close, buildConstraints(3,1,1));
		mainPanel.add(adjClose, buildConstraints(3,2,1));
		mainPanel.add(volume, buildConstraints(4,0,1));
		mainPanel.add(high, buildConstraints(4,1,1));
		mainPanel.add(low, buildConstraints(4,2,1));
		
		frame.setContentPane(mainPanel);
		frame.pack();
	}
	
	protected int countLayoutRows()
	{
		return layout.getLayoutDimensions()[1].length;
	}
	
	protected int countLayoutCols()
	{
		return layout.getLayoutDimensions()[0].length;
	}
	
	protected JLabel buildLabel(String labelName)
	{
		return new JLabel(labelName);
	}
	
	protected GridBagConstraints buildConstraints(int row, int col, int rowsToSpan)
	{
		constraints.gridwidth = rowsToSpan;
		constraints.gridy = row;
		constraints.gridx = col;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.EAST;
		
		return constraints;
	}
	
	protected void configureButtons()
	{
		buttonGroup.add(open);
		buttonGroup.add(close);
		buttonGroup.add(adjClose);
		buttonGroup.add(volume);
		buttonGroup.add(high);
		buttonGroup.add(low);
	}
	
	protected String getSelectedButtonText() {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
	
	
	@Override
	public void attach(StockChartPanel stockChartPanel)
	{
		this.observer = stockChartPanel;
	}
	
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
	
	protected void addCurrentChartLabels()
	{
		if(newComponents.size() > 0){
			for(int i =0; i < newComponents.size(); i++)
				mainPanel.remove(newComponents.get(i));
		}
		if (observer.getNumberOfSeries() > 0) {
			newComponents = new ArrayList<>();
			int layoutRows = countLayoutRows();
			
			JLabel header = new JLabel("Current Stocks displayed:");
			newComponents.add(header);
			mainPanel.add(header, buildConstraints(layoutRows++,0,2));
			
			for (String series : observer.currentStocksDisplayed()) {
				JButton remove = new JButton("Remove");
				remove.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						observer.removeSeries(series);
						for (Component comp : newComponents) {
							mainPanel.remove(comp);
							mainPanel.revalidate();
						}
						addCurrentChartLabels();
					}
				});
				
				JLabel name = new JLabel(series);
				newComponents.add(name);
				newComponents.add(remove);
				mainPanel.add(name, buildConstraints(layoutRows, 1, 1));
				mainPanel.add(remove, buildConstraints(layoutRows++, 2, 1));
			}
			
			frame.pack();
		}
		addApplyCancelButtons();  	
	}

	protected void addApplyCancelButtons()
	{
		mainPanel.add(apply, buildConstraints(countLayoutRows(), 1, 1));
		
		apply.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
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
		
		mainPanel.add(cancel, buildConstraints(countLayoutRows(), 2, 1));
		
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});
		
		frame.pack();
		
	}
}
