package app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import dbase.DatabaseManager;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import utillity.StockPrinter;
import javax.swing.JLabel;

public class MainView {

	private JFrame frame;
	
	private DatabaseManager DM;
	private int invoiceNum;
	private FinalVariables fv;
	private Logger logger;

	private ArrayList<String> defaultPaths;
	
	private Helper helper;
	
	private String loggerFolderPath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
//					window.frmHctMagazyn.setVisible(true);
//					window.frmHctMagazyn.setExtendedState(JFrame.MAXIMIZED_BOTH); 
//					window.frmHctMagazyn.setUndecorated(true);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainView() {
		this.helper = new Helper();
		this.fv = new FinalVariables();
		this.loggerFolderPath = this.fv.SAVE_FOLDER_DEFAULT_PATH+"\\"+this.fv.LOGGER_FOLDER_NAME;
		this.DM = new DatabaseManager(loggerFolderPath);
		
		defaultPaths = new ArrayList<String>();
		
		if(this.defaultPaths.isEmpty())
			defaultPaths = DM.getPaths("SELECT "+this.fv.SETTINGS_TABLE_PATH+" FROM "+this.fv.SETTINGS_TABLE);
//		for(int i = 0; i < defaultPaths.size(); i++)
//			System.out.println(defaultPaths.get(i));
		if(!loggerFolderPath.equalsIgnoreCase(defaultPaths.get(0)+"\\"+this.fv.LOGGER_FOLDER_NAME))
			loggerFolderPath = defaultPaths.get(0)+"\\"+this.fv.LOGGER_FOLDER_NAME;
		
		this.logger = new Logger(loggerFolderPath);
		
		if(!this.helper.checkDatesOfLastBackup()){
			try {
				String date = this.helper.getFormatedDate();
				String t = this.fv.DATABASE_DEFAULT_PATH.substring(this.fv.DATABASE_DEFAULT_PATH.lastIndexOf('\\')+1);
				String dbbName = date+"_"+t;
				boolean jobDone = this.helper.databaseBackUp(this.fv.DATABASE_DEFAULT_PATH, this.fv.DATABASE_BACKUP_DEFAULT_PATH+"\\"+dbbName);
				if(jobDone){
					String query = "UPDATE \""+fv.SETTINGS_TABLE+"\" SET "+fv.SETTINGS_TABLE_PATH+"='"+date+"'" + " WHERE " + fv.ROW_ID+ "='"+this.fv.SETTINGS_TABLE_LAST_DATABASE_BACKUP+"'";
					this.DM.editRecord(query);
				} else {
					JOptionPane.showMessageDialog(frame, "Nie wykonałem kopii bazy danych.");
				}
			} catch (IOException e) {
				this.logger.logError("MAIN VIEW DATABASE BACKUP FAIL "+this.getClass().getName()+"\t"+e.getMessage());
			}
			printTestPage(false);
		}
		
		initialize();
	}

	private void printTestPage(boolean doPrint){
		StockPrinter stPrinter = new StockPrinter(defaultPaths);
		try {
			stPrinter.printCleanPDF(doPrint);
		} catch (Exception e) {
			this.logger.logError("MAIN VIEW EMPTY PDF PRINT FAIL "+this.getClass().getName()+"\t"+e.getMessage());
		}

	}

	public int getInvoiceNum(){
		return this.invoiceNum;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		if(this.frmHctMagazyn == null)
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 51, 0));
//		frame.getContentPane().setBackground(Color.CYAN);
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setTitle("HCT MAGAZYN");
		frame.setBounds(10, 10, 704, 334);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
			            fv.CLOSE_WINDOW, fv.CLOSE_WINDOW, 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		            SqliteTestAppV.main(null);
		        }
		    }
		});
		frame.setLocation(10, 10);
		frame.getContentPane().setLayout(null);
		
		JButton stockBtn = new JButton("Wyswietl magazyn");
		stockBtn.setBackground(new Color(135, 206, 235));
		stockBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		stockBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				WyswietlMagazyn.main(loggerFolderPath);
			}
		});
		stockBtn.setBounds(358, 52, 200, 36);
		frame.getContentPane().add(stockBtn);
		
		JButton invoiceBtn = new JButton("Wyswietl rachunki");
		invoiceBtn.setBackground(new Color(135, 206, 235));
		invoiceBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		invoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				WyswietlRachunki.main(loggerFolderPath);
			}
		});
		invoiceBtn.setBounds(60, 171, 200, 36);
		frame.getContentPane().add(invoiceBtn);


		JButton nowyRachunekBtn = new JButton("Wystaw rachunek");
		nowyRachunekBtn.setBackground(new Color(0, 255, 102));
		nowyRachunekBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyRachunekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				WystawRachunek.main(defaultPaths);
			}
		});
		nowyRachunekBtn.setBounds(60, 52, 200, 36);
		frame.getContentPane().add(nowyRachunekBtn);
		
		JButton nowyTowarBtn = new JButton("Dodaj towar");
		nowyTowarBtn.setBackground(new Color(0, 255, 153));
		nowyTowarBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyTowarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				DodajTowar.main(loggerFolderPath);
			}
		});
		nowyTowarBtn.setBounds(60, 112, 200, 36);
		frame.getContentPane().add(nowyTowarBtn);
		
		JButton nowaUslugaBtn = new JButton("Dodaj usluge");
		nowaUslugaBtn.setBackground(new Color(0, 255, 153));
		nowaUslugaBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowaUslugaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				DodajUsluge.main(loggerFolderPath);
			}
		});
		nowaUslugaBtn.setBounds(358, 112, 200, 36);
		frame.getContentPane().add(nowaUslugaBtn);
		
		Icon settingsImg = new ImageIcon("resources/img/cogs.png");
		JButton btnSettings = new JButton("Settings", settingsImg);
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				SettingsFrame.main(defaultPaths);
			}
		});
		btnSettings.setBounds(606, 52, 52, 52);
		
		frame.getContentPane().add(btnSettings);
		
		JButton btnSalesReports = new JButton("Raporty sprzedaży");
		btnSalesReports.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnSalesReports.setBackground(new Color(135, 206, 235));
		btnSalesReports.setBounds(358, 171, 200, 36);
		btnSalesReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				SalesReports.main(defaultPaths);
			}
		});
		
		frame.getContentPane().add(btnSalesReports);
		
		Icon testPageImg = new ImageIcon("resources/img/testpageico.png");
		JButton btnTestPage = new JButton("Strona testowa", testPageImg);
		btnTestPage.setIconTextGap(-15);
		btnTestPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				printTestPage(true);
			}
		});
		btnTestPage.setBackground(new Color(255, 255, 255));
		btnTestPage.setBounds(606, 121, 52, 52);
		frame.getContentPane().add(btnTestPage);
		
		JButton btnCustomers = new JButton("Klienci");
		btnCustomers.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnCustomers.setBackground(new Color(204, 255, 255));
		btnCustomers.setBounds(60, 236, 200, 36);
		btnCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				CustomersWindow.main(defaultPaths);
			}
		});
		frame.getContentPane().add(btnCustomers);
		
		JButton btnRepakReport = new JButton("Repak");
		btnRepakReport.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnRepakReport.setBackground(new Color(204, 255, 255));
		btnRepakReport.setBounds(358, 236, 200, 36);
		btnRepakReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");
//				frame.dispose();
//				Repak.main(defaultPaths);
			}
		});

		frame.getContentPane().add(btnRepakReport);
		
		Border b = BorderFactory.createLineBorder(Color.black);
		TitledBorder border = BorderFactory.createTitledBorder(b, "");
		JLabel line = new JLabel("");
		line.setBounds(10, 220, 670, 1);
		line.setBorder(border);
		frame.getContentPane().add(line);
	}
}
