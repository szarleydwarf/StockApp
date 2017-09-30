package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dbase.DatabaseManager;
import hct_speciale.Item;
import utillity.FinalVariables;

import java.awt.Toolkit;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class MainView {

	private JFrame frmHctMagazyn;
	
	private DatabaseManager DM;
	private int invoiceNum;

	private FinalVariables fv;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
					window.frmHctMagazyn.setVisible(true);
					window.frmHctMagazyn.setExtendedState(JFrame.MAXIMIZED_BOTH); 
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
		DM = new DatabaseManager();
//		ArrayList<Item> list = DM.getItemsList("SELECT * From services ");
//		for(int i = 0; i < list.size();i++)
//			list.get(i).print();
		this.fv = new FinalVariables();
		
		initialize();
	}

	public int getInvoiceNum(){
		return this.invoiceNum;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		if(this.frmHctMagazyn == null)
			frmHctMagazyn = new JFrame();
		frmHctMagazyn.setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\@Development\\EclipseJavaProjects\\sqliteTestApp\\StockApp\\resources\\img\\icon_hct.png"));
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
		stockBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHctMagazyn.dispose();
				WyswietlMagazyn.main(null);
			}
		});
		stockBtn.setBounds(60, 173, 200, 36);
		frmHctMagazyn.getContentPane().add(stockBtn);
		
		JButton servicesBtn = new JButton("Wyswietl uslugi");
		servicesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHctMagazyn.dispose();
				WyswietlListeUslug.main(null);
			}
		});
		servicesBtn.setBounds(358, 173, 200, 36);
		frmHctMagazyn.getContentPane().add(servicesBtn);
		
		JButton invoiceBtn = new JButton("Wyswietl rachunki");
		invoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHctMagazyn.dispose();
				WyswietlRachunki.main(null);
			}
		});
		invoiceBtn.setBounds(358, 47, 200, 36);
		frmHctMagazyn.getContentPane().add(invoiceBtn);
		
		JButton nowyRachunekBtn = new JButton("Wystaw rachunek");
		nowyRachunekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmHctMagazyn.dispose();
				WystawRachunek.main(null);
			}
		});
		nowyRachunekBtn.setBounds(60, 47, 200, 36);
		frmHctMagazyn.getContentPane().add(nowyRachunekBtn);
		
		JButton nowyTowarBtn = new JButton("Dodaj towar");
		nowyTowarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHctMagazyn.dispose();
				DodajTowar.main(null);
			}
		});
		nowyTowarBtn.setBounds(60, 110, 200, 36);
		frmHctMagazyn.getContentPane().add(nowyTowarBtn);
		
		JButton nowaUslugaBtn = new JButton("Dodaj usluge");
		nowaUslugaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmHctMagazyn.dispose();
				DodajUsluge.main(null);
			}
		});
		nowaUslugaBtn.setBounds(358, 110, 200, 36);
		frmHctMagazyn.getContentPane().add(nowaUslugaBtn);
	}

}
