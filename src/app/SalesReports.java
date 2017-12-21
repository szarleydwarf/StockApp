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
import hct_speciale.Invoice;
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
	protected String dayOfReport = "";
	private ArrayList<String> defaultPaths;
	private StockPrinter stPrinter;
	protected String yearOfReport = "";
	protected String monthOfReport = "";
	private HashMap<String, Double> stockPrices;
	private HashMap<String, Double> servicePrices;
	private HashMap<String, String> serviceCodesNames;

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
	
		String q = "SELECT "+this.fv.SERVICE_TABLE_NUMBER+","+this.fv.COST_COL_NAME+" FROM "+ this.fv.SERVICES_TABLE;
		this.servicesCosts = (HashMap<String, Double>) this.DM.getAllCostsPrices(q);

		q = "SELECT "+this.fv.SERVICE_TABLE_NUMBER+","+this.fv.PRICE_COL_NAME+" FROM "+ this.fv.SERVICES_TABLE;
		this.servicePrices = (HashMap<String, Double>) this.DM.getAllCostsPrices(q);
		
		q = "SELECT "+this.fv.SERVICE_TABLE_NUMBER+","+this.fv.SERVICES_TABLE_SERVICE_NAME+" FROM "+ this.fv.SERVICES_TABLE;
		this.serviceCodesNames = (HashMap<String, String>) this.DM.getServiceCodesMap(q);
		
		q = "SELECT "+this.fv.STOCK_TABLE_NUMBER+","+this.fv.COST_COL_NAME+" FROM "+ this.fv.STOCK_TABLE;
		this.stocksCosts = (HashMap<String, Double>) this.DM.getAllCostsPrices(q);

		q = "SELECT "+this.fv.STOCK_TABLE_NUMBER+","+this.fv.PRICE_COL_NAME+" FROM "+ this.fv.STOCK_TABLE;
		this.stockPrices = (HashMap<String, Double>) this.DM.getAllCostsPrices(q);
//		this.helper.printMap(serviceCodesNames);

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
				String day = "01", month = "01", year = "2017", dateOfReport = "";
				if(dayOfReport != "") {
					day = dayOfReport;
				} else {
					day = ""+helper.getDayOfMonthNum();
				}
				
				if(monthOfReport != ""){
					month = monthOfReport;
				}else{
					int mn = helper.getMonthNum()+1;
					month = ""+mn;
				}
				
				if(yearOfReport != ""){
					year = yearOfReport;
				}else{
					int y = helper.getYearNum();
					year = ""+y;
				}
				if(day.length() == 1)
					day = "0"+day;
				
				if(month.length() == 1)
					month = "0" + month;
				
				dateOfReport = day+"-"+month+"-"+year;
				
				printDailyReport(dateOfReport);
			}			
		});
		frame.getContentPane().add(btnPntDailyRep);
		
		String[]days = helper.getDaysArray();
		int today = helper.getDayOfMonthNum();

		JComboBox cbDays = new JComboBox(days);
		cbDays.setSelectedIndex(today-1);
		cbDays.setBounds(40, 357, 110, 36);
		frame.getContentPane().add(cbDays);
		
		cbDays.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbDays ){
					JComboBox cb = (JComboBox) a.getSource();
					dayOfReport = (cb.getSelectedItem().toString());
				}		
			}
		});
		
		JButton btnPntMnthRep = new JButton("Drukuj miesi\u0119czny raport");
		btnPntMnthRep.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnPntMnthRep.setBackground(new Color(135, 206, 235));
		btnPntMnthRep.setBounds(402, 404, 200, 36);
		btnPntMnthRep.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String month = "01", year = "2017", dateOfReport = "";
	
				if(monthOfReport != ""){
					month = monthOfReport;
				}else{
					int mn = helper.getMonthNum()+1;
					month = ""+mn;
				}
				
				if(yearOfReport != ""){
					year = yearOfReport;
				}else{
					int y = helper.getYearNum();
					year = ""+y;
				}

				if(month.length() == 1)
					month = "0" + month;
				
				dateOfReport = month+"-"+year;
				
				printMonthlyReport(dateOfReport);
			}			
		});
		frame.getContentPane().add(btnPntMnthRep);
		
		int monthNo = helper.getMonthNum();
	
		JComboBox cbMonths = new JComboBox(fv.MONTHS_NAMES);
		cbMonths.setBounds(160, 404, 110, 36);
		cbMonths.setSelectedIndex(monthNo);
		cbMonths.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonths ){
					JComboBox cb = (JComboBox) a.getSource();
					monthOfReport = (cb.getSelectedItem().toString());
					monthOfReport = helper.getIndexOfMonth(monthOfReport);
				}		
			}
		});

		frame.getContentPane().add(cbMonths);
	      
		JComboBox cbMonthDaily = new JComboBox(fv.MONTHS_NAMES);
		cbMonthDaily.setSelectedIndex(-1);
		cbMonthDaily.setBounds(160, 357, 110, 36);
		cbMonthDaily.setSelectedIndex(monthNo);
		cbMonthDaily.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonthDaily ){
					JComboBox cb = (JComboBox) a.getSource();
					monthOfReport = (cb.getSelectedItem().toString());
					monthOfReport = helper.getIndexOfMonth(monthOfReport);
				}		
			}
		});

		frame.getContentPane().add(cbMonthDaily);
		
		int yearIndex = helper.getYearIndex();
		
		JComboBox cbYearDaily = new JComboBox(fv.YEARS_NO_STRING);
		cbYearDaily.setSelectedIndex(-1);
		cbYearDaily.setBounds(280, 357, 110, 36);
		cbYearDaily.setSelectedIndex(yearIndex);
		cbYearDaily.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYearDaily ){
					JComboBox cb = (JComboBox) a.getSource();
					yearOfReport = (cb.getSelectedItem().toString());
				}		
			}
		});

		frame.getContentPane().add(cbYearDaily);
		
		JComboBox cbYearMonthly = new JComboBox(fv.YEARS_NO_STRING);
		cbYearMonthly.setSelectedIndex(-1);
		cbYearMonthly.setBounds(280, 404, 110, 36);
		cbYearMonthly.setSelectedIndex(yearIndex);
		cbYearMonthly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYearMonthly ){
					JComboBox cb = (JComboBox) a.getSource();
					yearOfReport = (cb.getSelectedItem().toString());
				}		
			}
		});

		frame.getContentPane().add(cbYearMonthly);

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
	}//TODO END OF INITIALIZE

	protected void printMonthlyReport(String dateOfReport) {
		ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
		double[][] toPrint = new double[4][3];

		String query = "SELECT * FROM \"" + fv.INVOCE_TABLE + "\" WHERE \"" + fv.INVOCE_TABLE_DATE + "\" LIKE \"%" + dateOfReport + "%\"";
		invoiceList = DM.getInvoiceList(query);
	
		if(invoiceList != null && !invoiceList.isEmpty()){
			toPrint = populateArrayToPrint(invoiceList);
			boolean jobDone = false;
			try {
				jobDone = stPrinter.printReport(dateOfReport, toPrint);
			} catch (Exception e) {
				String msg =  "Wystapil blad zapisu raportu miesiecznego (" + dateOfReport + ")" + e.getMessage() + " - "+e.getStackTrace();
				JOptionPane.showMessageDialog(frame,msg);
				this.log.logError(msg);
				jobDone = false;
			}
			
			if(jobDone)
				JOptionPane.showMessageDialog(frame, "Raport z miesiaca "+dateOfReport + " wygenerowany pomyslnie.");
			else
				JOptionPane.showMessageDialog(frame, "Raport z miesiaca "+dateOfReport + " nie zostal wygenerowany.");
		}else{
			JOptionPane.showMessageDialog(frame, "Brak wpisów z daty "+dateOfReport);
		}
	}

	protected void printDailyReport(String date) {
		ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
		double[][] toPrint = new double[4][3];

		String query = "SELECT * FROM \"" + fv.INVOCE_TABLE + "\" WHERE \"" + fv.INVOCE_TABLE_DATE + "\"=\"" + date + "\"";
		invoiceList = DM.getInvoiceList(query);

		if(invoiceList != null && !invoiceList.isEmpty()){
			toPrint = populateArrayToPrint(invoiceList);
			boolean jobDone = false;
			try {
				jobDone = stPrinter.printReport(date, toPrint);
			} catch (Exception e) {
				String msg =  "Wystapil blad zapisu raportu dziennego  (" + date + ")" + e.getMessage() + " - "+e.getStackTrace();
				JOptionPane.showMessageDialog(frame,msg);
				this.log.logError(msg);
				jobDone = false;
			}
			
			if(jobDone)
				JOptionPane.showMessageDialog(frame, "Raport z dnia "+date + " wygenerowany pomyslnie.");
			else
				JOptionPane.showMessageDialog(frame, "Raport z dnia "+date + " nie zostal wygenerowany.");
		}else{
			JOptionPane.showMessageDialog(frame, "Brak wpisów z dnia "+date);
		}
	}

	private double[][] populateArrayToPrint(ArrayList<Invoice> invoiceList) {
		double[][] toPrint = new double[5][3];
		String[] itemArray = getItemsArray(invoiceList);
		double sumItemCost = helper.getSumDouble(stocksCosts, itemArray);
		double sumItemPrices = helper.getSumDouble(stockPrices, itemArray);
		double itemProfit = sumItemPrices -  sumItemCost;
		toPrint[0][0] = sumItemPrices;
		toPrint[0][1] = sumItemCost;
		toPrint[0][2] = itemProfit;
		
		String[] carWashArray = getCarWashArray(invoiceList);
		double carWashSumC = helper.getSumDouble(servicesCosts, carWashArray);
		double carWashPrices = helper.getSumDouble(servicePrices, carWashArray);
		double carWashProfit = carWashPrices - carWashSumC;
		toPrint[1][0] = carWashPrices;
		toPrint[1][1] = carWashSumC;
		toPrint[1][2] = carWashProfit;
	
		String[] otherServArray = getOtherServiceArray(invoiceList);
		double otherServSumC = helper.getSumDouble(servicesCosts, otherServArray);
		double otherServPrices = helper.getSumDouble(servicePrices, otherServArray);
		double otherProfit = otherServPrices - otherServSumC;
		toPrint[2][0] = otherServPrices;
		toPrint[2][1] = otherServSumC;
		toPrint[2][2] = otherProfit;
		
		double discounts = getDiscountSum(invoiceList);
		double disCost = 0.00;
//		double sumProfits = itemProfit+ carWashProfit + otherProfit;		
		toPrint[3][0] = discounts;
		toPrint[3][1] = disCost;
		toPrint[3][2] = discounts;

		double sumSales = sumItemPrices + carWashPrices + otherServPrices - discounts;
		double sumCosts = sumItemCost + carWashSumC + otherServSumC;
		double sumProfits = itemProfit+ carWashProfit + otherProfit - discounts;		
		toPrint[4][0] = sumSales;
		toPrint[4][1] = sumCosts;
		toPrint[4][2] = sumProfits;

		return toPrint;
	}

	private double getDiscountSum(ArrayList<Invoice> invoiceList) {
		double sum = 0.00;
		for(Invoice i : invoiceList){
			sum += i.getDiscount();
		}
		return sum;
	}

	private String[] getOtherServiceArray(ArrayList<Invoice> invoiceList) {
		ArrayList<String> temp = new ArrayList<String>();
		temp = getOtherServiceList(temp, invoiceList);
		String[] tArray = new String[temp.size()];
		tArray = temp.toArray(tArray);
		return tArray;
	}

	private String[] getCarWashArray(ArrayList<Invoice> invoiceList) {
		ArrayList<String> temp = new ArrayList<String>();
		temp = getCarWashServiceList(temp, invoiceList);
		String[] tArray = new String[temp.size()];
		tArray = temp.toArray(tArray);
		return tArray;
	}

	private ArrayList<String> getCarWashServiceList(ArrayList<String> temp, ArrayList<Invoice> invoiceList) {
		for(int i = 0; i < invoiceList.size(); i++){
			if(!invoiceList.get(i).getServiceNumber().equals("") && !invoiceList.get(i).getServiceNumber().isEmpty()) {
				String[] tokens = invoiceList.get(i).getServiceNumber().split(",", -1);
				if(tokens.length > 0){
					for(String s : tokens){
						String t = s.substring(s.indexOf("A"));
						if(this.serviceCodesNames.containsKey(t)){
							if(this.serviceCodesNames.get(t).contains("*")){
								temp.add(s);
							}
						}
						
					}
				}
			}
		}
		return temp;
	}

	private ArrayList<String> getOtherServiceList(ArrayList<String> temp, ArrayList<Invoice> invoiceList) {
		for(int i = 0; i < invoiceList.size(); i++){
			if(!invoiceList.get(i).getServiceNumber().equals("") && !invoiceList.get(i).getServiceNumber().isEmpty()) {
				String[] tokens = invoiceList.get(i).getServiceNumber().split(",", -1);
				if(tokens.length > 0){
					for(String s : tokens){
						String t = s.substring(s.indexOf("A"));
						if(this.serviceCodesNames.containsKey(t)){
							if(!this.serviceCodesNames.get(t).contains("*")){
								temp.add(s);
							}
						}
					}
				}
			}
		}
		return temp;
	}

	private String[] getItemsArray(ArrayList<Invoice> invoiceList) {
		ArrayList<String> itemsList = new ArrayList<String>();
		itemsList = getItemList(itemsList, invoiceList);
		String[] itemArray = new String[itemsList.size()];
		return itemsList.toArray(itemArray);	}

//	private String[] getServArray(ArrayList<Invoice> invoiceList) {
//		ArrayList<String> servicesList = new ArrayList<String>();
//		servicesList = getServiceList(servicesList, invoiceList);
//		String[] servArray = new String[servicesList.size()];
//		return servicesList.toArray(servArray);
//	}

	private ArrayList<String> getItemList(ArrayList<String> itemsList, ArrayList<Invoice> invoiceList) {
		for(int i = 0; i < invoiceList.size(); i++){
			if(!invoiceList.get(i).getItemNumber().equals("") && !invoiceList.get(i).getItemNumber().isEmpty()) {
				String[] tokens = invoiceList.get(i).getItemNumber().split(",", -1);
				if(tokens.length > 0){
					for(String s : tokens){
						itemsList.add(s);
					}
				}
			}
		}
		return itemsList;
	}

	private ArrayList<String> getServiceList(ArrayList<String> servicesList, ArrayList<Invoice> invoiceList) {
		for(int i = 0; i < invoiceList.size(); i++){
			if(!invoiceList.get(i).getServiceNumber().equals("") && !invoiceList.get(i).getServiceNumber().isEmpty()) {
				String[] tokens = invoiceList.get(i).getServiceNumber().split(",", -1);
				if(tokens.length > 0){
					for(String s : tokens){
						servicesList.add(s);
					}
				}
			}
		}
		return servicesList;
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
		scrollPane.setLocation(40, 48);
		scrollPane.setSize(560, 287);
		
		frame.getContentPane().add(scrollPane);		
	}

	private double sumCosts(String[] tokens) {
		double sum = 0;
		for (String token : tokens) {
			double d = 0;
			if(this.servicesCosts.containsKey(token)) {
				d = this.servicesCosts.get(token);
			} else if(this.stocksCosts.containsKey(token)) {
				d = this.stocksCosts.get(token);
			}
			sum += d;
		}
		return (sum);
	}

}