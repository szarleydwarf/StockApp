package app;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import dbase.DatabaseManager;
import hct_speciale.Item;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;

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
	private JTable table;

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
		
		String q2 = "SELECT "+this.fv.STOCK_TABLE_NUMBER+","+this.fv.COST+" FROM "+ this.fv.STOCK_TABLE;
		this.stocksCosts = (HashMap<String, Double>) this.DM.getAllCostsPrices(q2);
		
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		frame.getContentPane().setLayout(null);
		frame.setBounds(100, 100, 748, 426);

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
		btnBack.setBounds(633, 53, 89, 23);
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
		DecimalFormat df;
		df = new DecimalFormat(this.fv.DECIMAL_FORMAT); 
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
				data[j][1] = "� "+ df.format(dCost);
				data[j][2] = "� "+ df.format(incomeSum);
				double dDiff = incomeSum - dCost;
				data[j][3] = "� "+ df.format(dDiff);
			}
			
		}
		table = new JTable(data, this.fv.SALES_REPORT_TB_HEADINGS);
		table.setBounds(42, 87, 560, 288);
		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setLocation(42, 95);
		scrollPane.setSize(560, 240);
		
		frame.getContentPane().add(scrollPane);
		
	}

	private double sumCosts(String[] tokens) {
		double sum = 0;
		for (String token : tokens) {
			double d = 0;
			if(this.servicesCosts.containsKey(token))
				d = this.servicesCosts.get(token);
			else if(this.stocksCosts.containsKey(token))
				d = this.stocksCosts.get(token);
			
			sum += d;
		}
		return (sum);
	}
}