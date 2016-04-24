package com.finance.main.java.histTable;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.finance.main.java.enums.Languages;
import com.finance.main.java.enums.TextFields;
import com.finance.main.java.util.Localized;
import com.finance.main.java.util.LocalizedStrings;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class HistTableSettings extends JFrame implements Localized {
	public JButton apply;
	private JPanel contentPane;
	public boolean symbol;
	public boolean open;
	public boolean high;
	public boolean close;
	public boolean date;
	public boolean volume;
	public boolean low;
	public boolean adjClose;
	public JCheckBox symbolBox;
	public JCheckBox openBox;
	public JCheckBox closeBox;
	public JCheckBox highBox;
	public JCheckBox lowBox;
	public JCheckBox volumeBox;
	public JCheckBox adjCloseBox;
	public JCheckBox dateBox;
	public JTextField startDate;
	public JTextField endDate;
	public JButton btnCancel;
	public JLabel lblEndDate;
	public JLabel lblStartDate;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HistTableSettings frame = new HistTableSettings();
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
	public HistTableSettings() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		LocalizedStrings.setLanguage(Languages.ENGLISH_US);
		setBounds(100, 100, 414, 276);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		symbolBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_SYMBOL));
		symbolBox.setSelected(true);
		symbolBox.setBounds(6, 37, 144, 23);
		contentPane.add(symbolBox);
		
		openBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_OPEN));
		openBox.setSelected(true);
		openBox.setBounds(6, 63, 144, 23);
		contentPane.add(openBox);
		
		closeBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_CLOSE));
		closeBox.setSelected(true);
		closeBox.setBounds(6, 89, 144, 23);
		contentPane.add(closeBox);
		
		highBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_HIGH));
		highBox.setSelected(true);
		highBox.setBounds(6, 115, 144, 23);
		contentPane.add(highBox);
		
		lowBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_LOW));
		lowBox.setSelected(true);
		lowBox.setBounds(152, 115, 144, 23);
		contentPane.add(lowBox);
		
		volumeBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_VOLUME));
		volumeBox.setSelected(true);
		volumeBox.setBounds(152, 89, 222, 23);
		contentPane.add(volumeBox);
		
		adjCloseBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_ADJCLOSE));
		adjCloseBox.setSelected(true);
		adjCloseBox.setBounds(152, 37, 224, 23);
		contentPane.add(adjCloseBox);
		
		 dateBox = new JCheckBox(LocalizedStrings.getLocalString(TextFields.TABLE_DATE));
		 dateBox.setSelected(true);
		 dateBox.setBounds(152, 63, 222, 23);
		 contentPane.add(dateBox);
		
		apply = new JButton(LocalizedStrings.getLocalString(TextFields.TABLE_APPLY));
		apply.setBounds(49, 192, 89, 23);
		contentPane.add(apply);
		
		btnCancel = new JButton(LocalizedStrings.getLocalString(TextFields.TABLE_CANCEL));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HistTableSettings.this.setVisible(false);
				
			}
		});
		btnCancel.setBounds(171, 192, 89, 23);
		contentPane.add(btnCancel);
		
		startDate = new JTextField();
		startDate.setText("03/05/2016");
		startDate.setBounds(60, 161, 78, 20);
		contentPane.add(startDate);
		startDate.setColumns(10);
		
		lblStartDate = new JLabel("Start Date:");
		lblStartDate.setBounds(6, 164, 63, 14);
		contentPane.add(lblStartDate);
		
		lblEndDate = new JLabel("End Date:");
		lblEndDate.setBounds(148, 164, 57, 14);
		contentPane.add(lblEndDate);
		
		endDate = new JTextField();
		endDate.setText("04/05/2016");
		endDate.setBounds(199, 161, 86, 20);
		contentPane.add(endDate);
		endDate.setColumns(10);
	}

	@Override
	public boolean updateLabels() {

		btnCancel.setText(LocalizedStrings.getLocalString(TextFields.TABLE_CANCEL));
		apply.setText(LocalizedStrings.getLocalString(TextFields.TABLE_APPLY));
		lblEndDate.setText(LocalizedStrings.getLocalString(TextFields.END_DATE));
		lblStartDate.setText(LocalizedStrings.getLocalString(TextFields.START_DATE));
		symbolBox.setText(LocalizedStrings.getLocalString(TextFields.TABLE_SYMBOL));
		openBox.setText(LocalizedStrings.getLocalString(TextFields.TABLE_OPEN));
		closeBox.setText(LocalizedStrings.getLocalString(TextFields.TABLE_CLOSE));
		highBox.setText(LocalizedStrings.getLocalString(TextFields.TABLE_HIGH));
		lowBox.setText(LocalizedStrings.getLocalString(TextFields.TABLE_LOW));
		volumeBox.setText(LocalizedStrings.getLocalString(TextFields.TABLE_VOLUME));
		adjCloseBox.setText(LocalizedStrings.getLocalString(TextFields.TABLE_ADJCLOSE));
		dateBox.setText(LocalizedStrings.getLocalString(TextFields.TABLE_DATE));
		return false;
	}
	public void setLanguage(Languages lang){
		//LocalizedStrings.setLanguage(lang);
		updateLabels();
	}
}
