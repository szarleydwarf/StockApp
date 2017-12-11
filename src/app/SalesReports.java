package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

import dbase.DatabaseManager;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import utillity.StockPrinter;

import javax.swing.JComboBox;

public class SalesReports {

	private JFrame frame;
	private DatabaseManager DM;
	private HashMap<String, Double> stocksCosts;
	private HashMap<String, Double> servicesCosts;
	protected static String date;
	protected static String loggerFolderPath;
	
	private static FinalVariables fv;
	private static Logger log;
	private static Helper helper;
	private JTable table;
	protected int dayOfReport = 0;
	private ArrayList<String> defaultPaths;
	private StockPrinter stPrinter;

	/**
	 * Launch the application.
	 */
	public static void main(ArrayList<String> defaultPaths) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				fv = new FinalVariables();
				loggerFolderPath = defaultPaths.get(0)+"\\"+fv.LOGGER_FOLDER_NAME;
				log = new Logger(loggerFolderPath);
				helper = new Helper();
				date = helper.getFormatedDate();
				
				try {
					SalesReports window = new SalesReports();
					window.frame.setVisible(true);
				} catch (Exception e) {
					log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SalesReports() {
		this.DM = new DatabaseManager(this.loggerFolderPath);
		stocksCosts = new HashMap<String, Double>();
		servicesCosts = new HashMap<String, Double>();
		
		String q1 = "SELECT "+this.fv.SERVICE_TABLE_NUMBER+","+this.fv.COST+" FROM "+ this.fv.SERVICES_TABLE;
		this.servicesCosts = (HashMap<String, Double>) this.DM.getAllCostsPrices(q1);
//		this.helper.printMap(servicesCosts);
		String q2 = "SELECT "+this.fv.STOCK_TABLE_NUMBER+","+this.fv.COST+" FROM "+ this.fv.STOCK_TABLE;
		this.stocksCosts = (HashMap<String, Double>) this.DM.getAllCostsPrices(q2);

		if(this.defaultPaths == null || this.defaultPaths.isEmpty())
			defaultPaths = DM.getPaths("SELECT "+this.fv.SETTINGS_TABLE_PATH+" FROM "+this.fv.SETTINGS_TABLE);

		stPrinter = new StockPrinter(defaultPaths);

		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		frame.getContentPane().setLayout(null);
		frame.setBounds(100, 100, 748, 490);
		frame.setLocation(10, 10);

		JLabel lblTitle = new JLabel("Raporty sprzeda\u017Cy");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblTitle.setBounds(0, 11, 730, 26);
		frame.getContentPane().add(lblTitle);
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.setBounds(633, 53, 89, 23);
		frame.getContentPane().add(btnBack);
		
		JLabel lblBorder = new JLabel("");
		lblBorder.setHorizontalAlignment(SwingConstants.CENTER);
		lblBorder.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		Border b = BorderFactory.createLineBorder(Color.yellow);
		lblBorder.setBorder(new LineBorder(new Color(255, 255, 0), 2));

		lblBorder.setBounds(42, 50, 560, 20);
		frame.getContentPane().add(lblBorder);

		JButton btnPntDailyRep = new JButton("Drukuj dzienny raport");
		btnPntDailyRep.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnPntDailyRep.setBackground(new Color(135, 206, 235));
		btnPntDailyRep.setBounds(402, 357, 200, 36);
		btnPntDailyRep.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(dayOfReport != 0) {
					stPrinter.printDailyReport(dayOfReport);
				} else{
					int day = helper.getDayOfMonthNum();
					stPrinter.printDailyReport(day);
				}
			}			
		});
		frame.getContentPane().add(btnPntDailyRep);
		
		String[]days = getDaysArray();
		
		JComboBox cbDays = new JComboBox(days);
		cbDays.setBounds(148, 357, 227, 36);
		frame.getContentPane().add(cbDays);
		
		cbDays.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbDays ){
					JComboBox cb = (JComboBox) a.getSource();
					dayOfReport = Integer.parseInt(cb.getSelectedItem().toString());
				}
				
			}
		});
		
		JButton btnPntMnthRep = new JButton("Drukuj miesi\u0119czny raport");
		btnPntMnthRep.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnPntMnthRep.setBackground(new Color(135, 206, 235));
		btnPntMnthRep.setBounds(402, 404, 200, 36);
		frame.getContentPane().add(btnPntMnthRep);
	
		JComboBox cbMonths = new JComboBox(fv.MONTHS_NAMES);
		cbMonths.setBounds(148, 404, 227, 36);
		frame.getContentPane().add(cbMonths);
	      
		populateTable();
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
			            fv.CLOSE_WINDOW, fv.CLOSE_WINDOW, 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		            MainView.main(null);
		        }
		    }
		});
	}

	private String[] getDaysArray() {
		int month = helper.getMonthNum();
		month++;

		switch(month){
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return fv.DAYS_NUM_31;
	
			case 4:
			case 6:
			case 9:
			case 11:
				return fv.DAYS_NUM_30;
	
			case 2:
				boolean isLeap = helper.isLeapYear();
				if(isLeap)
					return fv.DAYS_NUM_29;
				else
					return fv.DAYS_NUM_28;
	
			default:
				return fv.DAYS_NUM_31;
		}
	}

	private void populateTable() {
		DecimalFormat df = new DecimalFormat(this.fv.DECIMAL_FORMAT); 
		String query = "";
		String[][] data = new String [this.fv.MONTHS_2017.length][this.fv.SALES_REPORT_TB_HEADINGS.length];

		for(int j = 0; j < this.fv.MONTHS_2017.length; j++) {
			query = "SELECT "+this.fv.SERVICE_TABLE_NUMBER+","+this.fv.STOCK_TABLE_NUMBER+","+this.fv.TOTAL+" FROM "+this.fv.INVOCE_TABLE+" WHERE "+this.fv.INVOCE_TABLE_DATE+" LIKE '%"+this.fv.MONTHS_2017[j]+"%'";
			ArrayList<String> list = new ArrayList<String>();
			list = this.DM.selectRecordArrayList(query);
			data[j][0] = this.fv.MONTHS_2017[j];
			if(list.isEmpty()){
				data[j][1] = ""+0;
				data[j][2] = ""+0;
				data[j][3] = ""+0;			
			} else {
				double dCost = 0;
				double  incomeSum = 0;
				for(int i = 0; i < list.size(); i++) {
					if(!list.get(i).equals("") && ((i != 2) && (i != 5) && (i != 8) && (i != 11))) {
						String[] tokens = list.get(i).split(",", -1);
						dCost  += sumCosts(tokens);
					}else if(!list.get(i).equals("")){
						incomeSum += Double.parseDouble(list.get(i));		
					}
				}
				data[j][1] = "€ "+ df.format(dCost);
				data[j][2] = "€ "+ df.format(incomeSum);
				double dDiff = incomeSum - dCost;
				data[j][3] = "€ "+ df.format(dDiff);
			}
			
		}
		table = new JTable(data, this.fv.SALES_REPORT_TB_HEADINGS);
		table.setBounds(42, 87, 560, 288);
		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		table.setFillsViewportHeight(true);
		
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.black);
		header.setForeground(Color.yellow);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setLocation(42, 48);
		scrollPane.setSize(560, 287);
		
		frame.getContentPane().add(scrollPane);		
	}

	private double sumCosts(String[] tokens) {
		double sum = 0;
		for (String token : tokens) {
			double d = 0;
			if(this.servicesCosts.containsKey(token)) {
				d = this.servicesCosts.get(token);
//				System.out.println("found service :"+d);
			} else if(this.stocksCosts.containsKey(token)) {
				d = this.stocksCosts.get(token);
//				System.out.println("found stock :"+d);
			}
			sum += d;
		}
//		System.out.println("sum :"+sum);
		return (sum);
	}
}