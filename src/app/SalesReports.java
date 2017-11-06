package app;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import dbase.DatabaseManager;
import hct_speciale.Item;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class SalesReports {

	private JFrame frame;
	private JLabel lblMonth;
	private DatabaseManager DM;
	private HashMap<String, Double> stocksCosts;
	private HashMap<String, Double> servicesCosts;
	protected static String date;
	protected static String loggerFolderPath;
	
	private static FinalVariables fv;
	private static Logger log;
	private static Helper helper;

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
//		printMap(servicesCosts);
		System.out.println("\n\n!");
		String q2 = "SELECT "+this.fv.STOCK_TABLE_NUMBER+","+this.fv.COST+" FROM "+ this.fv.STOCK_TABLE;
		this.stocksCosts = (HashMap<String, Double>) this.DM.getAllCostsPrices(q2);
//		printMap(stocksCosts);
	
		
		
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		frame.getContentPane().setLayout(null);
		frame.setBounds(100, 100, 746, 300);

		JLabel lblTitle = new JLabel("Raporty sprzeda\u017Cy");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblTitle.setBounds(0, 11, 730, 26);
		frame.getContentPane().add(lblTitle);
		
		lblMonth = new JLabel("Miesi\u0105c");
		lblMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonth.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblMonth.setBounds(52, 56, 60, 20);
		frame.getContentPane().add(lblMonth);
		
		JLabel lblCosts = new JLabel("Koszty");
		lblCosts.setHorizontalAlignment(SwingConstants.CENTER);
		lblCosts.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblCosts.setBounds(175, 56, 60, 20);
		frame.getContentPane().add(lblCosts);
		
		JLabel lblIncome = new JLabel("Zysk");
		lblIncome.setHorizontalAlignment(SwingConstants.CENTER);
		lblIncome.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblIncome.setBounds(500, 56, 60, 20);
		frame.getContentPane().add(lblIncome);
		
		JLabel lblSale = new JLabel("Sprzeda\u017C");
		lblSale.setHorizontalAlignment(SwingConstants.CENTER);
		lblSale.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblSale.setBounds(333, 56, 60, 20);
		frame.getContentPane().add(lblSale);
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.setBounds(631, 227, 89, 23);
		frame.getContentPane().add(btnBack);
		
		JLabel lblBorder = new JLabel("");
		Border b = BorderFactory.createLineBorder(Color.CYAN);
		lblBorder.setBorder(b);

		lblBorder.setBounds(42, 50, 560, 30);
		frame.getContentPane().add(lblBorder);
		
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

	private void populateTable() {
		String query = "";
		for(int j = this.fv.MONTHS_2017.length-1; j >= 0; j--) {
			query = "SELECT "+this.fv.SERVICE_TABLE_NUMBER+","+this.fv.STOCK_TABLE_NUMBER+","+this.fv.TOTAL+" FROM "+this.fv.INVOCE_TABLE+" WHERE "+this.fv.INVOCE_TABLE_DATE+" LIKE '%"+this.fv.MONTHS_2017[j]+"%'";
			ArrayList<String> list = new ArrayList<String>();
			list = this.DM.selectRecordArrayList(query);
			if(!list.isEmpty()) {
				for(int i = 0; i < list.size(); i++) {
					if(!list.get(i).equals("") && ((i != 2) && (i != 5))){
						String[] tokens = list.get(i).split(",", -1);
						double monthlySum = sumCosts(tokens);
//						if(monthlySum > 0)
						System.out.println(i + " monthlySum " + monthlySum);
					}
				}
			}
		}
	}

	private double sumCosts(String[] tokens) {
		double sum = 0;
		for (String token : tokens) {
			String query = "SELECT "+this.fv.COST+" FROM ";
			if(token.contains("AAS"))
				query += this.fv.SERVICES_TABLE + " WHERE " + this.fv.SERVICE_TABLE_NUMBER;
			else if(token.contains("AAA"))
				query += this.fv.STOCK_TABLE + " WHERE " + this.fv.STOCK_TABLE_NUMBER;
			
			query += " = '"+token+"'";
//			System.out.println("token " + token);//+ "\n"+query+"\n"
			
			double d = 1;
			sum += d;
		}
//		System.out.println("Sum "+sum);
		return sum;
	}
}
