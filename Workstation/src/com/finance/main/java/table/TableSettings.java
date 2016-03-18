package table;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 357, 276);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		dayHighBox = new JCheckBox("Day High");
		dayHighBox.setSelected(true);
		dayHighBox.setBounds(49, 37, 97, 23);
		contentPane.add(dayHighBox);
		
		dayLowBox = new JCheckBox("Day Low");
		dayLowBox.setSelected(true);
		dayLowBox.setBounds(49, 63, 97, 23);
		contentPane.add(dayLowBox);
		
		nameBox = new JCheckBox("Stock Name");
		nameBox.setSelected(true);
		nameBox.setBounds(49, 89, 97, 23);
		contentPane.add(nameBox);
		
		changeBox = new JCheckBox("Change");
		changeBox.setSelected(true);
		changeBox.setBounds(49, 115, 97, 23);
		contentPane.add(changeBox);
		
		yearHighBox = new JCheckBox("Year High");
		yearHighBox.setSelected(true);
		yearHighBox.setBounds(49, 141, 97, 23);
		contentPane.add(yearHighBox);
		
		yearLowBox = new JCheckBox("Year Low");
		yearLowBox.setSelected(true);
		yearLowBox.setBounds(148, 89, 97, 23);
		contentPane.add(yearLowBox);
		
		lastTradePriceBox = new JCheckBox("Last Trade Price");
		lastTradePriceBox.setSelected(true);
		lastTradePriceBox.setBounds(148, 115, 97, 23);
		contentPane.add(lastTradePriceBox);
		
		volumeBox = new JCheckBox("Volume Traded");
		volumeBox.setSelected(true);
		volumeBox.setBounds(148, 141, 97, 23);
		contentPane.add(volumeBox);
		
		exchangeBox = new JCheckBox("Exchange");
		exchangeBox.setSelected(true);
		exchangeBox.setBounds(146, 37, 97, 23);
		contentPane.add(exchangeBox);
		
		 averageDailyBox = new JCheckBox("Average Daily Price");
		 averageDailyBox.setSelected(true);
		averageDailyBox.setBounds(148, 63, 97, 23);
		contentPane.add(averageDailyBox);
		
		apply = new JButton("Apply");
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dayHigh = dayHighBox.isSelected();
				dayLow = dayLowBox.isSelected();
				yearHigh = yearHighBox.isSelected();
				yearLow = yearLowBox.isSelected();
				change = changeBox.isSelected();
				lastPrice = lastTradePriceBox.isSelected();
				volume = volumeBox.isSelected();
				stockName = nameBox.isSelected();
				exchange = exchangeBox.isSelected();
				avgDaily = averageDailyBox.isSelected();
				TableSettings.this.setVisible(false);
			}
		});
		apply.setBounds(49, 192, 89, 23);
		contentPane.add(apply);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableSettings.this.setVisible(false);
				
			}
		});
		btnCancel.setBounds(171, 192, 89, 23);
		contentPane.add(btnCancel);
	}
}
