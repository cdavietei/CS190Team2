package com.finance.main.java.histTable;

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
public class HistTableView extends JPanel {
	
	private HistTableSettings settingsFrame; //= new HistTableSettings();
	private JTable table;
	DefaultTableModel data;
	public JScrollPane scrollpane;
	/**
	 * Create the panel.
	 */
	Object[][] tableData = new Object[10][2];

	JCheckBox[] checkBoxes={settingsFrame.symbolBox,settingsFrame.dateBox,
			settingsFrame.highBox,settingsFrame.lowBox,settingsFrame.openBox,settingsFrame.closeBox,
			settingsFrame.volumeBox,settingsFrame.adjCloseBox};
	public HistTableView() {
		settingsFrame.setVisible(false);
		setLayout(null);
		LocalizedStrings.setLanguage(Languages.ENGLISH_US);
		LocalizedStrings.update();
		//System.out.println(LocalizedStrings.getLocalString(TextFields.TABLE_AVERAGEDAILY));
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!settingsFrame.isVisible())
					settingsFrame.setVisible(true);
				else
					settingsFrame.setVisible(false);
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
		scrollpane.setBounds(23, 23, 812, 214);
		scrollpane.setBorder(BorderFactory.createEmptyBorder());
		add(scrollpane);
		updateTable();
		table.setEnabled(false);

	}
	
	/**
	 * Updates a table
	 */
	public void updateTable(){	
		Object[][] columns = {{LocalizedStrings.getLocalString(TextFields.TABLE_SYMBOL),null},
			{LocalizedStrings.getLocalString(TextFields.TABLE_DATE),null},
			{LocalizedStrings.getLocalString(TextFields.TABLE_HIGH),null},
			{LocalizedStrings.getLocalString(TextFields.TABLE_LOW), null},
			{LocalizedStrings.getLocalString(TextFields.TABLE_OPEN),null},
			{LocalizedStrings.getLocalString(TextFields.TABLE_CLOSE),null},
			{LocalizedStrings.getLocalString(TextFields.TABLE_VOLUME),null},
			{LocalizedStrings.getLocalString(TextFields.TABLE_ADJCLOSE),null}};
		for(int i = data.getRowCount() -1; i >= 0; i--)
			data.removeRow(i);
		for(int i = 0; i < checkBoxes.length; i++){
			if(checkBoxes[i].isSelected()){
				//data.addRow(rows[i]);
			}
		}//for
<<<<<<< HEAD
		String[] columns = new String[colNeeded];
		int colIndex = 0;
		for(int i = 0; i < checkBoxes.length; i++){
			if(checkBoxes[i].isSelected()){
				columns[colIndex] = columnNames[i];
				colIndex++;
			}
		}//for
		data.setColumnIdentifiers(columns);
		Object[] row;
		for(int i =0; i < stockArray.size(); i++){
			row = new Object[colNeeded];
			int index = 0;
			if(checkBoxes[0].isSelected()){
				row[index] = stockArray.get(i).getSymbol();
				index++;
			}
			if(checkBoxes[1].isSelected()){
				row[index] = stockArray.get(i).getDate();
				index++;
			}
			if(checkBoxes[2].isSelected()){
				row[index] = new Double(stockArray.get(i).getHigh()).toString();
				System.out.println(stockArray.get(i).getHigh());
				index++;
			}
			if(checkBoxes[3].isSelected()){
				row[index] = stockArray.get(i).getLow();
				index++;
			}
			if(checkBoxes[4].isSelected()){
				row[index] = stockArray.get(i).getOpen();
				index++;
			}
			if(checkBoxes[5].isSelected()){
				row[index] = stockArray.get(i).getClose();
				index++;
			}
			if(checkBoxes[6].isSelected()){
				row[index] = stockArray.get(i).getVolume();
				index++;
			}
			if(checkBoxes[7].isSelected()){
				row[index] = stockArray.get(i).getAdjClose();
				index++;
			}
			data.addRow(row);
				
		}
=======
		//data = new DefaultTableModel(rows,columns);
>>>>>>> 953d90bf6fbd8dd189452411fddbbf418491a913
		table.setModel(data);
		settingsFrame.setVisible(false);
		//scrollpane.setSize(table.getSize());
		//scrollpane.setBounds(23,23,table.getWidth(),table.getHeight());

	}
}
