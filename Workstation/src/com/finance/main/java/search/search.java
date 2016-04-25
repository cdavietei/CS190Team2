package com.finance.main.java.search;
import javax.swing.JPanel;

import com.finance.main.java.database.StockDatabaseInterface;
import com.finance.main.java.enums.*;
import com.finance.main.java.util.*;
import javax.swing.JTextField;

import com.finance.main.java.util.LocalizedStrings;
import com.finance.main.java.window.MainWindow;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;

import org.sqlite.core.DB;

import com.finance.main.java.search.*;
public class search extends JPanel implements Localized {
	private JTextField textField;
	public static ArrayList<String> stockNames = new ArrayList<String>();
	public JLabel lblSearch;
	
	protected StockDatabaseInterface stockInter;
	//LocalizedSrings.lang = Languages.English;

	/**
	 * Create the panel.
	 */
	public search() {
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setLayout(null);
		String searchLabel = LocalizedStrings.getLocalString(TextFields.SEARCH)+ ":";
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchFunction(textField.getText());
				textField.setText("");
			}
		});
		
		stockInter = new StockDatabaseInterface();
		
		stockNames = stockInter.getAvailableStocks();
		textField.setBounds(74, 21, 209, 20);
		add(textField);
		textField.setColumns(10);
		
		stockInter = new StockDatabaseInterface();

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchFunction(textField.getText());
			}
		});
		btnNewButton.setIcon(new ImageIcon("Resources/Images/5-search-icon.png"));
		btnNewButton.setBounds(293, 21, 20, 20);
		add(btnNewButton);
		lblSearch = new JLabel(searchLabel);
		lblSearch.setBounds(26, 24, 46, 14);
		add(lblSearch);
	}
	public void searchFunction(String userInput){
		SearchToDB db = new SearchToDB();
		JFrame frame = new JFrame();
		JOptionPane mess = new JOptionPane();
				
		if(!userInput.equals("")){
			String returns = db.isCompanyName(userInput);
			db = new SearchToDB();
			if(returns.equals("")){
				if(db.isStockName(userInput)){
					if(!stockNames.contains(userInput))
						stockNames.add(userInput);
					MainWindow.addToCharts(userInput);
					mess.showMessageDialog(frame, LocalizedStrings.getLocalString(TextFields.SEARCH_SUCCESS));//"Search Successful!"/*LocalizedStrings.getLocalString(TextFields.SEARCHFAIL*/);
					
					try
					{
						stockInter.getStocksAsynch(userInput, 
								new Date(System.currentTimeMillis() - ((long)(20*1000*60*60*24) )), 
										new Date(System.currentTimeMillis()));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else{
					mess.showMessageDialog(frame,LocalizedStrings.getLocalString(TextFields.SEARCH_FAILURE));// "Invalid input, not a stock or company.");
				}
			}
			else{
				if(!stockNames.contains(returns))
					stockNames.add(returns);
				MainWindow.addToCharts(userInput);
						mess.showMessageDialog(frame, LocalizedStrings.getLocalString(TextFields.SEARCH_SUCCESS));//"Search successful"/*LocalizedStrings.getLocalString(TextFields.SEARCHSUCCESS*/);
			}
		}
		else{
			mess.showMessageDialog(frame,"Please input a search term.");
		}
	}
	public static ArrayList<String> getStockNames(){
		return stockNames;
	}
	@Override
	public boolean updateLabels()
	{
		lblSearch.setText(LocalizedStrings.getLocalString(TextFields.SEARCH) + ":");
		
		return true;
	}
}
