import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class search extends JPanel {
	private JTextField textField;
	//LocalizedSrings.lang = Languages.English;

	/**
	 * Create the panel.
	 */
	public search() {
		setLayout(null);
		//LocalizedStrings.update();
		String searchLabel ="search"; //LocalizedStrings.getLocalString(TextFields.SEARCH);
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchFunction(textField.getText());
			}
		});
		textField.setBounds(123, 120, 209, 20);
		add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchFunction(textField.getText());
			}
		});
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\Carl\\workspace\\search2\\source\\5-search-icon.png"));
		btnNewButton.setBounds(342, 120, 20, 20);
		add(btnNewButton);
		
		JLabel lblSearch = new JLabel(searchLabel);
		lblSearch.setBounds(49, 123, 46, 14);
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
}
