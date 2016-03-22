package com.finance.main.java.table;

import javax.swing.JPanel;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import com.finance.main.java.enums.*;
import com.finance.main.java.util.*;
public class TableView extends JPanel {
	LocalizedStrings localStrings = new LocalizedStrings();
	
	private TableSettings settingsFrame = new TableSettings();
	private JTable table;
	DefaultTableModel data;
	public JScrollPane scrollpane;
	/**
	 * Create the panel.
	 */
	Object[][] tableData = new Object[10][2];

	JCheckBox[] checkBoxes={settingsFrame.dayHighBox,settingsFrame.dayLowBox,
			settingsFrame.yearHighBox,settingsFrame.yearLowBox,settingsFrame.nameBox,
			settingsFrame.exchangeBox,settingsFrame.averageDailyBox,settingsFrame.changeBox,
			settingsFrame.lastTradePriceBox,settingsFrame.volumeBox};
	public TableView() {
		settingsFrame.setVisible(false);
		setLayout(null);
		localStrings.setLanguage(Languages.SPANISH);
		localStrings.update();
		System.out.println(localStrings.getLocalString(TextFields.TABLE_AVERAGEDAILY));
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsFrame.setVisible(true);
			}
		});
		settingsFrame.apply.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					updateTable();
			}
		});
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\Carl\\Desktop\\settings1_16x16.gif"));
		btnNewButton.setBounds(280, 0, 23, 23);
		add(btnNewButton);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Row1", "12"},
				{"Row2", "13"},
				{"Row3", "14"},
				{"Row4", "15"},
				{"Row5", "16"},
				{"Row6", "17"},
				{"Row7", "18"},
				{"Row8", "19"},
				{"Row9", "20"},
				{"Ro10", "21"},
			},
			new String[] {
				"Data Point", "Value"
			}
		));
		table.getColumnModel().getColumn(0).setMinWidth(20);
		
		//table.setBounds(86, 67, 217, 160);
		scrollpane = new JScrollPane(table);
		scrollpane.setBounds(23, 23, 257, 194);
		scrollpane.setBorder(BorderFactory.createEmptyBorder());
		add(scrollpane);
		updateTable();
		table.setEnabled(false);

	}
	
	/**
	 * Updates a table
	 */
	public void updateTable(){	
		Object[][] rows = {{localStrings.getLocalString(TextFields.TABLE_DAYHIGH),null},
			{localStrings.getLocalString(TextFields.TABLE_DAYLOW),null},
			{localStrings.getLocalString(TextFields.TABLE_YEARHIGH),null},
			{localStrings.getLocalString(TextFields.TABLE_YEARLOW), null},
			{localStrings.getLocalString(TextFields.TABLE_STOCKNAME),null},
			{localStrings.getLocalString(TextFields.TABLE_EXCHANGE),null},
			{localStrings.getLocalString(TextFields.TABLE_AVERAGEDAILY),null},
			{localStrings.getLocalString(TextFields.TABLE_CHANGE),null},
			{localStrings.getLocalString(TextFields.TABLE_LASTTRADE),null},
			{localStrings.getLocalString(TextFields.TABLE_VOLUME),null}};
		String[] columnNames = {localStrings.getLocalString(TextFields.TABLE_DATAPOINTS),
				localStrings.getLocalString(TextFields.TABLE_VALUE)};
		data = (DefaultTableModel) table.getModel();
		for(int i = data.getRowCount() -1; i >= 0; i--)
			data.removeRow(i);
		for(int i = 0; i < checkBoxes.length; i++){
			if(checkBoxes[i].isSelected()){
				data.addRow(rows[i]);
			}
		}//for
		table.setModel(data);
		settingsFrame.setVisible(false);
		//scrollpane.setSize(table.getSize());
		//scrollpane.setBounds(23,23,table.getWidth(),table.getHeight());

	}
}
