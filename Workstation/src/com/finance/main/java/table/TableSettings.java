package com.finance.main.java.table;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.finance.main.java.enums.Languages;
import com.finance.main.java.enums.TextFields;
import com.finance.main.java.util.LocalizedStrings;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TableSettings extends JFrame {
	public JButton apply;
	private JPanel contentPane;
	public boolean dayHigh;
	public boolean dayLow;
	public boolean yearHigh;
	public boolean yearLow;
	public boolean stockName;
	public boolean exchange;
	public boolean avgDaily;
	public boolean change;
	public boolean lastPrice;
	public boolean volume;
	public JCheckBox dayHighBox;
	public JCheckBox dayLowBox;
	public JCheckBox nameBox;
	public JCheckBox changeBox;
	public JCheckBox yearHighBox;
	public JCheckBox yearLowBox;
	public JCheckBox lastTradePriceBox;
	public JCheckBox volumeBox;
	public JCheckBox exchangeBox;
	public JCheckBox averageDailyBox;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TableSettings frame = new TableSettings();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TableSettings() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		LocalizedStrings.setLanguage(Languages.ENGLISH_US);
		setBounds(100, 100, 414, 276);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		dayHighBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_DAYHIGH));
		dayHighBox.setSelected(true);
		dayHighBox.setBounds(6, 37, 144, 23);
		contentPane.add(dayHighBox);
		
		dayLowBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_DAYLOW));
		dayLowBox.setSelected(true);
		dayLowBox.setBounds(6, 63, 144, 23);
		contentPane.add(dayLowBox);
		
		nameBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_STOCKNAME));
		nameBox.setSelected(true);
		nameBox.setBounds(6, 89, 144, 23);
		contentPane.add(nameBox);
		
		changeBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_CHANGE));
		changeBox.setSelected(true);
		changeBox.setBounds(6, 115, 144, 23);
		contentPane.add(changeBox);
		
		yearHighBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_YEARHIGH));
		yearHighBox.setSelected(true);
		yearHighBox.setBounds(6, 141, 144, 23);
		contentPane.add(yearHighBox);
		
		yearLowBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_YEARLOW));
		yearLowBox.setSelected(true);
		yearLowBox.setBounds(154, 89, 222, 23);
		contentPane.add(yearLowBox);
		
		lastTradePriceBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_LASTTRADE));
		lastTradePriceBox.setSelected(true);
		lastTradePriceBox.setBounds(154, 115, 222, 23);
		contentPane.add(lastTradePriceBox);
		
		volumeBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_VOLUME));
		volumeBox.setSelected(true);
		volumeBox.setBounds(154, 141, 222, 23);
		contentPane.add(volumeBox);
		
		exchangeBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_EXCHANGE));
		exchangeBox.setSelected(true);
		exchangeBox.setBounds(152, 37, 224, 23);
		contentPane.add(exchangeBox);
		
		 averageDailyBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_AVERAGEDAILY));
		 averageDailyBox.setSelected(true);
		averageDailyBox.setBounds(154, 63, 222, 23);
		contentPane.add(averageDailyBox);
		
		apply = new JButton(LocalizedStrings.getLocalString(TextFields.TABLE_APPLY));
		apply.setBounds(49, 192, 89, 23);
		contentPane.add(apply);
		
		JButton btnCancel = new JButton(LocalizedStrings.getLocalString(TextFields.TABLE_CANCEL));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableSettings.this.setVisible(false);
				
			}
		});
		btnCancel.setBounds(171, 192, 89, 23);
		contentPane.add(btnCancel);
	}
}
