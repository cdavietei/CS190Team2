package com.finance.main.java.search;
import javax.swing.JPanel;
import com.finance.main.java.enums.*;
import com.finance.main.java.util.*;
import javax.swing.JTextField;

import com.finance.main.java.util.LocalizedStrings;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;

public class search extends JPanel implements Localized {
	private JTextField textField;
	private static ArrayList<String> stockNames = new ArrayList<String>();
	public JLabel lblSearch;
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
			}
		});
		textField.setBounds(74, 21, 209, 20);
		add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchFunction(textField.getText());
			}
		});
		stockNames.add("GOOG");
		stockNames.add("YHOO");
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\Carl\\workspace\\search2\\source\\5-search-icon.png"));
		btnNewButton.setBounds(293, 21, 20, 20);
		add(btnNewButton);
		
		lblSearch = new JLabel(searchLabel);
		lblSearch.setBounds(26, 24, 46, 14);
		add(lblSearch);
	}
	public String isCompanyName(String userInput){
		return "";
	}
	public boolean isStockName(String userInput){
		return true;
	}
	public boolean searchYQL(String userInput){
		return true;
	}
	public void searchFunction(String userInput){
		JFrame frame = new JFrame();
		JOptionPane mess = new JOptionPane();
		if(!userInput.equals("")){
			String returns = isCompanyName(userInput);
			if(returns.equals("")){
				if(isStockName(userInput)){
					stockNames.add(userInput);
					if(searchYQL(userInput)){
						mess.showMessageDialog(frame,"Search successful!"/*LocalizedStrings.getLocalString(TextFields.SEARCHSUCCESS*/);
					}
					else{
						mess.showMessageDialog(frame, "Search failed!"/*LocalizedStrings.getLocalString(TextFields.SEARCHFAIL*/);
					}
					
				}
				else{
					mess.showMessageDialog(frame, "Invalid input, not a stock or company.");
				}
			}
			else{
				stockNames.add(returns);
				if(searchYQL(returns)){
					mess.showMessageDialog(frame, "Search successful"/*LocalizedStrings.getLocalString(TextFields.SEARCHSUCCESS*/);
				}
				else{
					mess.showMessageDialog(frame, "Search Failed"/*LocalizedStrings.getLocalString(TextFields.SEARCHFAIL*/);
				}
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
