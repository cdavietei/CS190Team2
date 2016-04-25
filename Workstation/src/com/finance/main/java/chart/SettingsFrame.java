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

class SettingsFrame implements Subject, Localized
{
	private StockChart observer;
	protected ArrayList<Component> newComponents = new ArrayList<>();
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
	
	public JButton createSettingsButton()
	{
		JButton settings = new JButton(new ImageIcon("Resources/Images/settingsIcon.gif"));
		settings.setPreferredSize(new Dimension(25, 25));
		
		settings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				showFrame();
				
			}
		});
		
		return settings;
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
	public void attach(StockChart observer)
	{
		this.observer = observer;
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

	@Override
	public boolean updateLabels() {
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
		return false;
	}
}
