package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Toolkit;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainView {

	private JFrame frmHctMagazyn;

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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHctMagazyn = new JFrame();
		frmHctMagazyn.setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\@Development\\EclipseJavaProjects\\sqliteTestApp\\StockApp\\resources\\img\\icon_hct.png"));
		frmHctMagazyn.setTitle("HCT MAGAZYN");
		frmHctMagazyn.setBounds(100, 100, 650, 280);
		frmHctMagazyn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmHctMagazyn.getContentPane().setLayout(null);
		
		JButton stockBtn = new JButton("Wyswietl magazyn");
		stockBtn.setBounds(60, 173, 200, 36);
		frmHctMagazyn.getContentPane().add(stockBtn);
		
		JButton servicesBtn = new JButton("Wyswietl uslugi");
		servicesBtn.setBounds(358, 173, 200, 36);
		frmHctMagazyn.getContentPane().add(servicesBtn);
		
		JButton invoiceBtn = new JButton("Wyswietl rachunki");
		invoiceBtn.setBounds(358, 47, 200, 36);
		frmHctMagazyn.getContentPane().add(invoiceBtn);
		
		JButton nowyRachunekBtn = new JButton("Wystaw rachunek");
		nowyRachunekBtn.setBounds(60, 47, 200, 36);
		frmHctMagazyn.getContentPane().add(nowyRachunekBtn);
		
		JButton nowyTowarBtn = new JButton("Dodaj towar");
		nowyTowarBtn.setBounds(60, 110, 200, 36);
		frmHctMagazyn.getContentPane().add(nowyTowarBtn);
		
		JButton nowaUslugaBtn = new JButton("Dodaj usluge");
		nowaUslugaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		nowaUslugaBtn.setBounds(358, 110, 200, 36);
		frmHctMagazyn.getContentPane().add(nowaUslugaBtn);
	}

}
