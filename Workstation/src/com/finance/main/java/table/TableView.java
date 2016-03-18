package table;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;

public class TableView extends JPanel {
	String[] columnNames = {"Data Point","Value"};
	private TableSettings settingsFrame = new TableSettings();
	private JTable table;
	DefaultTableModel data;
	/**
	 * Create the panel.
	 */
	Object[][] tableData = new Object[10][2];
	Object[][] rows = {{"Day High",null},{"Day Low",null},{"Year High",null},
			{"Year Low", null},{"Stock Name",null},{"Exchange",null},
			{"Average Daily Price",null},{"Change",null},
			{"Last Price",null},{"Volume",null}};
	JCheckBox[] checkBoxes={settingsFrame.dayHighBox,settingsFrame.dayLowBox,
			settingsFrame.yearHighBox,settingsFrame.yearLowBox,settingsFrame.nameBox,
			settingsFrame.exchangeBox,settingsFrame.averageDailyBox,settingsFrame.changeBox,
			settingsFrame.lastTradePriceBox,settingsFrame.volumeBox};
	public TableView() {
		settingsFrame.setVisible(false);
		setLayout(null);
		
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
				{"Row1","12"},
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
		
		//table.setBounds(86, 67, 217, 160);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setBounds(23, 23, 257, 194);
		add(scrollpane);
		updateTable();

	}
	public void updateTable(){
		data = (DefaultTableModel) table.getModel();
		for(int i = data.getRowCount() -1; i >= 0; i--)
			data.removeRow(i);
		for(int i = 0; i < checkBoxes.length; i++){
			if(checkBoxes[i].isSelected()){
				data.addRow(rows[i]);
			}
		}
		table.setModel(data);;
	}
}
