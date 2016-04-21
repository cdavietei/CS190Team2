package com.finance.main.java.window;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.finance.main.java.chart.StockChart;
import com.finance.main.java.chart.StockChartPanel;
import com.finance.main.java.enums.Languages;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JToolBar;
import javax.swing.JRadioButtonMenuItem;

public class MainWindow extends JFrame implements ActionListener,Localized {
	
	private volatile int screenX = 0;
	private volatile int screenY = 0;
	private volatile int myX = 0;
	private volatile int myY = 0;
	public search searchPan;
	public Languages currentLang = Languages.ENGLISH_US;
	public ArrayList<JInternalFrame> views = new ArrayList<JInternalFrame>();
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
		JMenu mnNewMenu = new JMenu("Language");
		menuBar.add(mnNewMenu);
		JRadioButtonMenuItem rdbtnmntmEnglish = new JRadioButtonMenuItem("English");
		rdbtnmntmEnglish.setSelected(true);
		rdbtnmntmEnglish.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e){
				stateChange(e);
				
		}
		});

		mnNewMenu.add(rdbtnmntmEnglish);
		JRadioButtonMenuItem rdbtnmntmSpanish = new JRadioButtonMenuItem("Spanish");
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
		
		JButton btnHistoricalTable = new JButton("Historical Table");
		
				btnHistoricalTable.setBounds(10, 11, 105, 23);
				panel_1.add(btnHistoricalTable);
				
				JButton btnGraph = new JButton("Graph");
				btnGraph.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JInternalFrame fr =  new JInternalFrame("Graph View");
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
				btnHistoricalTable.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JInternalFrame fr =  new JInternalFrame("Historical Table"); 
						desktopPane.add(fr);
						HistTableView histTable = new HistTableView();
						histTable.setBounds(110, 130, 105, 70);
						histTable.setVisible(true);
						histTable.setLang(currentLang);
						histTable.updateLabels();
						fr.getContentPane().add(histTable);
						fr.setBounds(110, 130,870,300);
						fr.setResizable(true);
						fr.setVisible(true);
						fr.setClosable(true);
						fr.setBorder(new LineBorder(new Color(0,0,0)));
						views.add(fr);
						}
				});

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
			for(int i = 0; i < views.size();i++){
				if(views.get(i).isVisible()){
					for(int j =0; j < views.get(i).getContentPane().getComponentCount();j++){
						if(views.get(i).getContentPane().getComponent(j).getClass().equals(new HistTableView().getClass())){
							HistTableView vw = (HistTableView)views.get(i).getContentPane().getComponent(j);
							vw.setLang(Languages.ENGLISH_US);
							vw.updateLabels();
							currentLang = Languages.ENGLISH_US;
						}
						else{
							StockChartPanel vw =  (StockChartPanel)views.get(i).getContentPane().getComponent(j);
							vw.updateLabels();
						}
					}
				}
				
			}
		}
		else{		
			for(int i = 0; i < views.size();i++){
			if(views.get(i).isVisible()){
				for(int j =0; j < views.get(i).getContentPane().getComponentCount();j++){
					if(views.get(i).getContentPane().getComponent(j).getClass().equals(new HistTableView().getClass())){
						HistTableView vw = (HistTableView)views.get(i).getContentPane().getComponent(j);
						vw.setLang(Languages.SPANISH);
						currentLang = Languages.SPANISH;
						vw.updateLabels();
					}
					else{
						StockChartPanel vw =  (StockChartPanel)views.get(i).getContentPane().getComponent(j);
						vw.updateLabels();
					}
				}
			}
			
		}
			LocalizedStrings.setLanguage(Languages.SPANISH);
		}
		updateLabels();
	}

	@Override
	public boolean updateLabels(){
		searchPan.updateLabels();
		/*for(int i = 0; i < views.size();i++){
			if(views.get(i).isVisible()){
				for(int j =0; j < views.get(i).getContentPane().getComponentCount();j++){
					if(views.get(i).getContentPane().getComponent(j).getClass().equals(new HistTableView().getClass())){
						HistTableView vw = (HistTableView)views.get(i).getContentPane().getComponent(j);
						vw.setLang(LocalizedStrings.language);
						System.out.println("main " + LocalizedStrings.language);
						vw.updateLabels();
					}
					else{
						StockChartPanel vw =  (StockChartPanel)views.get(i).getContentPane().getComponent(j);
						vw.updateLabels();
					}
				}
			}
			
		}*/
		return false;
	}
}
