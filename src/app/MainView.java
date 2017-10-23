package app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dbase.DatabaseManager;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;

public class MainView {

	private JFrame frmHctMagazyn;
	
	private DatabaseManager DM;
	private int invoiceNum;
	private String invoiceFolderPath = "";

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
					window.frmHctMagazyn.setVisible(true);
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
					JOptionPane.showMessageDialog(frmHctMagazyn, "Nie wykonałem kopii bazy danych.");
				}
			} catch (IOException e) {
				this.logger.logError("MAIN VIEW DATABASE BACKUP FAIL "+this.getClass().getName()+"\t"+e.getMessage());
			}
		}
		
		initialize();
	}

	public int getInvoiceNum(){
		return this.invoiceNum;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		if(this.frmHctMagazyn == null)
		frmHctMagazyn = new JFrame();
		frmHctMagazyn.setBackground(new Color(255, 255, 0));
		frmHctMagazyn.getContentPane().setBackground(new Color(255, 51, 0));
		
		frmHctMagazyn.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frmHctMagazyn.setTitle("HCT MAGAZYN");
		frmHctMagazyn.setBounds(100, 100, 650, 280);
		frmHctMagazyn.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmHctMagazyn.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frmHctMagazyn, 
			            fv.CLOSE_WINDOW, fv.CLOSE_WINDOW, 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frmHctMagazyn.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		            SqliteTestAppV.main(null);
		        }
		    }
		});
		frmHctMagazyn.getContentPane().setLayout(null);
		
		JButton stockBtn = new JButton("Wyswietl magazyn");
		stockBtn.setBackground(new Color(135, 206, 235));
		stockBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		stockBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHctMagazyn.dispose();
				WyswietlMagazyn.main(loggerFolderPath);
			}
		});
		stockBtn.setBounds(60, 52, 200, 36);
		frmHctMagazyn.getContentPane().add(stockBtn);
		
		JButton invoiceBtn = new JButton("Wyswietl rachunki");
		invoiceBtn.setBackground(new Color(135, 206, 235));
		invoiceBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		invoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHctMagazyn.dispose();
				WyswietlRachunki.main(loggerFolderPath);
			}
		});
		invoiceBtn.setBounds(208, 171, 200, 36);
		frmHctMagazyn.getContentPane().add(invoiceBtn);


		JButton nowyRachunekBtn = new JButton("Wystaw rachunek");
		nowyRachunekBtn.setBackground(new Color(135, 206, 235));
		nowyRachunekBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyRachunekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmHctMagazyn.dispose();
				WystawRachunek.main(defaultPaths);
			}
		});
		nowyRachunekBtn.setBounds(358, 52, 200, 36);
		frmHctMagazyn.getContentPane().add(nowyRachunekBtn);
		
		JButton nowyTowarBtn = new JButton("Dodaj towar");
		nowyTowarBtn.setBackground(new Color(135, 206, 235));
		nowyTowarBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyTowarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHctMagazyn.dispose();
				DodajTowar.main(loggerFolderPath);
			}
		});
		nowyTowarBtn.setBounds(60, 112, 200, 36);
		frmHctMagazyn.getContentPane().add(nowyTowarBtn);
		
		JButton nowaUslugaBtn = new JButton("Dodaj usluge");
		nowaUslugaBtn.setBackground(new Color(135, 206, 235));
		nowaUslugaBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowaUslugaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmHctMagazyn.dispose();
				DodajUsluge.main(loggerFolderPath);
			}
		});
		nowaUslugaBtn.setBounds(358, 112, 200, 36);
		frmHctMagazyn.getContentPane().add(nowaUslugaBtn);
		
		Icon settingsImg = new ImageIcon("resources/img/cogs.png");
		JButton btnSettings = new JButton("Settings", settingsImg);
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmHctMagazyn.dispose();
				SettingsFrame.main(defaultPaths);
			}
		});
		btnSettings.setBounds(574, 180, 52, 52);
		
		frmHctMagazyn.getContentPane().add(btnSettings);
	}
}
