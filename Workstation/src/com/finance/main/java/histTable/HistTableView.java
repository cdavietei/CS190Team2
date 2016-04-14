package com.finance.main.java.histTable;

import javax.swing.JPanel;
import java.io.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JToolBar;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.JScrollPane;

import com.finance.main.java.database.StockDatabaseInterface;
import com.finance.main.java.enums.*;
import com.finance.main.java.search.search;
import com.finance.main.java.stock.Stock;
import com.finance.main.java.util.*;
import javax.swing.JComboBox;
public class HistTableView extends JPanel implements Localized {

	private HistTableSettings settingsFrame = new HistTableSettings();
	private JTable table;
	DefaultTableModel data;
	public JScrollPane scrollpane;
	public ArrayList<Stock> stockArray = new ArrayList<Stock>();
	 private JComboBox comboBox = null;
	/**
	 * Create the panel.
	 */
	Object[][] tableData = new Object[10][2];
	public String[] columnNames = {LocalizedStrings.getLocalString(TextFields.TABLE_SYMBOL),
			LocalizedStrings.getLocalString(TextFields.TABLE_DATE),LocalizedStrings.getLocalString(TextFields.TABLE_HIGH),
			LocalizedStrings.getLocalString(TextFields.TABLE_LOW),LocalizedStrings.getLocalString(TextFields.TABLE_OPEN),
			LocalizedStrings.getLocalString(TextFields.TABLE_CLOSE),LocalizedStrings.getLocalString(TextFields.TABLE_VOLUME),
			LocalizedStrings.getLocalString(TextFields.TABLE_ADJCLOSE)};
	JCheckBox[] checkBoxes={settingsFrame.symbolBox,settingsFrame.dateBox,
			settingsFrame.highBox,settingsFrame.lowBox,settingsFrame.openBox,settingsFrame.closeBox,
			settingsFrame.volumeBox,settingsFrame.adjCloseBox};
	public HistTableView() {
		settingsFrame.setVisible(false);
		setLayout(null);
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
		btnNewButton.setIcon(new ImageIcon("Resources/Images/settingsIcon.gif"));
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
		
		comboBox = new JComboBox();
		ArrayList<String> stockNames = search.getStockNames();
		comboBox.addItem("");
		for(int i = 0; i < search.getStockNames().size();i++)
			comboBox.addItem(stockNames.get(i));
		comboBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e){
				if(e.getStateChange() == ItemEvent.SELECTED){
					if(!comboBox.getSelectedItem().toString().equals(""))
						getStockData(comboBox.getSelectedItem().toString());
				}
			}
		});
		comboBox.addPopupMenuListener(new PopupMenuListener(){

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// TODO Auto-generated method stub
				addItems();
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		comboBox.setBounds(147, 0, 123, 20);
		add(comboBox);

	}
	private void addItems(){
		comboBox.removeAllItems();
		ArrayList<String> stockNames = search.getStockNames();
		comboBox.addItem("");
		for(int i = 0; i < stockNames.size(); i++)
			comboBox.addItem(stockNames.get(i));
	}
	public void getStockData(String stockName){
		StockDatabaseInterface inter = new StockDatabaseInterface();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date1 = new java.util.Date();
		try {
			date1 = formatter.parse("20/03/2016");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.util.Date date2 = new java.util.Date();
		try {
			date2 = formatter.parse("30/03/2016");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			stockArray = inter.getStocks(stockName, new Date(date1.getTime()), new Date(date2.getTime()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.updateTable();
	}
	/**
	 * Updates a table
	 */
	public void updateTable(){
		data = (DefaultTableModel)table.getModel();
		for(int i = data.getRowCount() -1; i >= 0; i--)
			data.removeRow(i);
		for(int i = 0; i < checkBoxes.length; i++){
			if(checkBoxes[i].isSelected()){
				//data.addRow(rows[i]);
			}
		}//for
		int colNeeded = 0;

		int colIndex = 0;
		for(int i = 0; i < checkBoxes.length; i++){
			if(checkBoxes[i].isSelected()){
				colNeeded++;
			}
		}
		String[] columns = new String[colNeeded];
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
				row[index] = stockArray.get(i).getHigh();
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

		}		//data = new DefaultTableModel(rows,columns);
		table.setModel(data);
		settingsFrame.setVisible(false);
		//scrollpane.setSize(table.getSize());
		//scrollpane.setBounds(23,23,table.getWidth(),table.getHeight());

	}
	@Override
	public boolean updateLabels()
	{
		this.columnNames = new String[] {LocalizedStrings.getLocalString(TextFields.TABLE_SYMBOL),
				LocalizedStrings.getLocalString(TextFields.TABLE_DATE),LocalizedStrings.getLocalString(TextFields.TABLE_HIGH),
				LocalizedStrings.getLocalString(TextFields.TABLE_LOW),LocalizedStrings.getLocalString(TextFields.TABLE_OPEN),
				LocalizedStrings.getLocalString(TextFields.TABLE_CLOSE),LocalizedStrings.getLocalString(TextFields.TABLE_VOLUME),
				LocalizedStrings.getLocalString(TextFields.TABLE_ADJCLOSE)};
		this.updateTable();
		return true;
	}
	private void clearTable(){
		data = (DefaultTableModel)table.getModel();
		for(int i = data.getRowCount() -1; i >= 0; i--)
			data.removeRow(i);
		table.setModel(data);
	}
}
