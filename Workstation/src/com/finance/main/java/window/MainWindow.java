package com.finance.main.java.window;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.finance.main.java.chart.StockChart;
import com.finance.main.java.chart.StockChartPanel;
import com.finance.main.java.histTable.HistTableView;
import com.finance.main.java.search.search;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import java.awt.SystemColor;

public class MainWindow extends JFrame implements ActionListener {
	
	private volatile int screenX = 0;
	private volatile int screenY = 0;
	private volatile int myX = 0;
	private volatile int myY = 0;

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
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.LIGHT_GRAY);
		desktopPane.setBounds(0, 60, 716, 379);
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
						fr.setClosable(true);
						fr.setBorder(new LineBorder(new Color(0,0,0)));
					}
				});
				btnGraph.setBounds(10, 39, 105, 23);
				panel_1.add(btnGraph);
				
				JDesktopPane desktopPane_1 = new JDesktopPane();
				desktopPane_1.setBackground(SystemColor.control);
				desktopPane_1.setBounds(0, 0, 716, 61);
				contentPane.add(desktopPane_1);
				search searchPan = new search();
				desktopPane_1.add(searchPan);
				searchPan.setVisible(true);
				searchPan.setBounds(144, 0, 360, 61);
				btnHistoricalTable.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JInternalFrame fr =  new JInternalFrame("Historical Table"); 
						desktopPane.add(fr);
						HistTableView histTable = new HistTableView();
						histTable.setBounds(110, 130, 105, 70);
						histTable.setVisible(true);
						fr.getContentPane().add(histTable);
						fr.setBounds(110, 130,870,300);
						fr.setResizable(true);
						fr.setVisible(true);
						fr.setClosable(true);
						fr.setBorder(new LineBorder(new Color(0,0,0)));
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
}
