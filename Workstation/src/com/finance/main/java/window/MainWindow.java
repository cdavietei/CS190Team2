package com.finance.main.java.window;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.finance.main.java.chart.StockChart;
import com.finance.main.java.chart.StockChartPanel;
import com.finance.main.java.database.SQLiteConnector;
import com.finance.main.java.enums.Languages;
import com.finance.main.java.enums.TextFields;
import com.finance.main.java.histTable.HistTableView;
import com.finance.main.java.search.search;
import com.finance.main.java.util.*;

import javax.swing.JDesktopPane;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ButtonGroup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.SystemColor;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.JRadioButtonMenuItem;

public class MainWindow extends JFrame implements ActionListener,Localized {
	
	private volatile int screenX = 0;
	private volatile int screenY = 0;
	private volatile int myX = 0;
	private volatile int myY = 0;
	public search searchPan;
	public Languages currentLang = Languages.ENGLISH_US;
	public static ArrayList<JInternalFrame> views = new ArrayList<JInternalFrame>();
	private JPanel contentPane;
	private JButton btnGraph;
	JButton btnHistoricalTable;
	JMenu mnNewMenu;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					MainWindow frame = new MainWindow();
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
	public MainWindow() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 716, 21);
		contentPane.add(menuBar);
		LocalizedStrings.setLanguage(Languages.ENGLISH_US);
		mnNewMenu = new JMenu(LocalizedStrings.getLocalString(TextFields.LANGUAGE));//"Language");
		menuBar.add(mnNewMenu);
		JRadioButtonMenuItem rdbtnmntmEnglish = new JRadioButtonMenuItem(LocalizedStrings.getLocalString(TextFields.ENGLISH));
		rdbtnmntmEnglish.setSelected(true);
		rdbtnmntmEnglish.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e){
				stateChange(e);
		}
		});
		
		mnNewMenu.add(rdbtnmntmEnglish);
		JRadioButtonMenuItem rdbtnmntmSpanish = new JRadioButtonMenuItem(LocalizedStrings.getLocalString(TextFields.SPANISH));
		mnNewMenu.add(rdbtnmntmSpanish);
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnmntmSpanish);
		group.add(rdbtnmntmEnglish);
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.LIGHT_GRAY);
		desktopPane.setBounds(0, 85, 716, 354);
		contentPane.add(desktopPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(595, 0, 121, 71);
		desktopPane.add(panel_1);
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setLayout(null);
		
		btnHistoricalTable = new JButton(LocalizedStrings.getLocalString(TextFields.TABLE));
		btnHistoricalTable.setBounds(10, 11, 105, 23);
		btnHistoricalTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame fr =  new JInternalFrame(LocalizedStrings.getLocalString(TextFields.HISTTABLE));
				desktopPane.add(fr);
				HistTableView histTable = new HistTableView();
				histTable.setBounds(110, 130, 105, 70);
				histTable.setVisible(true);
				histTable.setLang(currentLang);
				histTable.updateLabels();
				fr.getContentPane().add(histTable);
				fr.setBounds(110, 130,870,300);
				fr.setResizable(false);
				fr.setVisible(true);
				fr.setClosable(true);
				fr.setBorder(new LineBorder(new Color(0,0,0)));
				views.add(fr);
				}
		});
		
		panel_1.add(btnHistoricalTable);
		
		btnGraph = new JButton(LocalizedStrings.getLocalString(TextFields.GRAPH));
		btnGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame fr =  new JInternalFrame(LocalizedStrings.getLocalString(TextFields.GRAPHVIEW));
				fr.setBounds(110, 130, 700, 445);                 
				desktopPane.add(fr);
				StockChartPanel stockPan = new StockChartPanel();
				fr.getContentPane().add(stockPan);
				fr.setVisible(true);
				StockChartPanel pan = (StockChartPanel)fr.getContentPane().getComponents()[fr.getContentPane().getComponentCount()-1];
				pan.updateLabels();
				fr.setClosable(true);
				fr.setBorder(new LineBorder(new Color(0,0,0)));
				views.add(fr);
			}
		});
		btnGraph.setBounds(10, 39, 105, 23);
		panel_1.add(btnGraph);
				
		JDesktopPane desktopPane_1 = new JDesktopPane();
		desktopPane_1.setBackground(SystemColor.control);
		desktopPane_1.setBounds(0, 21, 716, 64);
		contentPane.add(desktopPane_1);
		searchPan = new search();
		desktopPane_1.add(searchPan);
		searchPan.updateLabels();
		searchPan.setVisible(true);
		searchPan.setBounds(144, 0, 360, 61);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 732, 478);
		addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent e) {
				Dimension newSize = getSize();
				desktopPane.setSize(newSize);
				desktopPane_1.setSize(newSize);
				Dimension pan = panel_1.getSize();
				Dimension ser = searchPan.getSize();
				searchPan.setBounds(newSize.width/2 - 180,0,ser.width,ser.height);
				panel_1.setBounds(newSize.width-125,0,pan.width,pan.height);
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
			
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void stateChange(ItemEvent e){
		if(e.getStateChange() == ItemEvent.SELECTED){
			LocalizedStrings.setLanguage(Languages.ENGLISH_US);
			LocalizedStrings.update();
			currentLang = Languages.ENGLISH_US;
						
			if(mnNewMenu != null)
				mnNewMenu.setText(LocalizedStrings.getLocalString(TextFields.LANGUAGE));
			
			for(int i = 0; i < views.size();i++){
				if(views.get(i).isVisible()){
					for(int j =0; j < views.get(i).getContentPane().getComponentCount();j++){
						if(views.get(i).getContentPane().getComponent(j).getClass().equals(new HistTableView().getClass())){
							views.get(i).setTitle(LocalizedStrings.getLocalString(TextFields.HISTTABLE));
							HistTableView vw = (HistTableView)views.get(i).getContentPane().getComponent(j);
							vw.setLang(Languages.ENGLISH_US);
							vw.updateLabels();
						}
						else{
							views.get(i).setTitle(LocalizedStrings.getLocalString(TextFields.GRAPHVIEW));
							StockChartPanel vw =  (StockChartPanel)views.get(i).getContentPane().getComponent(j);
							vw.setLange(Languages.ENGLISH_US);
						}
					}
				}
			}
		}
		else{		
			LocalizedStrings.setLanguage(Languages.SPANISH);
			LocalizedStrings.update();
			currentLang = Languages.SPANISH;

			if(mnNewMenu != null)
				mnNewMenu.setText(LocalizedStrings.getLocalString(TextFields.LANGUAGE));
			
			for(int i = 0; i < views.size();i++){
				if(views.get(i).isVisible()){
					for(int j =0; j < views.get(i).getContentPane().getComponentCount();j++){
						if(views.get(i).getContentPane().getComponent(j).getClass().equals(new HistTableView().getClass())){

							views.get(i).setTitle(LocalizedStrings.getLocalString(TextFields.HISTTABLE));
							HistTableView vw = (HistTableView)views.get(i).getContentPane().getComponent(j);
							vw.setLang(Languages.SPANISH);
							vw.updateLabels();
						}//if	
						else{
							views.get(i).setTitle(LocalizedStrings.getLocalString(TextFields.GRAPHVIEW));
							StockChartPanel vw =  (StockChartPanel)views.get(i).getContentPane().getComponent(j);
							vw.setLange(Languages.SPANISH);
						}//else
					}//for
				}//if	
			}//for
		}//else
		updateLabels();
	}
	public static void addToCharts(String stock){
		for(int i = 0; i < views.size(); i++){
			if(views.get(i).isVisible()){
				for(int j =0; j < views.get(i).getContentPane().getComponentCount();j++){
					if(views.get(i).getContentPane().getComponent(j).getClass().equals(new HistTableView().getClass())){
					}
					else{
						StockChartPanel vw =  (StockChartPanel)views.get(i).getContentPane().getComponent(j);
						vw.addSeries(stock);
					}
				}
			}
		}
	}
	@Override
	public boolean updateLabels(){
		searchPan.updateLabels();
		btnHistoricalTable.setText(LocalizedStrings.getLocalString(TextFields.TABLE));
		btnGraph.setText(LocalizedStrings.getLocalString(TextFields.GRAPH));
		return false;
	}
}
