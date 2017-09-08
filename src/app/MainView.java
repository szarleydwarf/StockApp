package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
//		frmHctMagazyn.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmHctMagazyn.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmHctMagazyn.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frmHctMagazyn, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
//		            System.exit(0);
		        	frmHctMagazyn.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		            SqliteTestAppV.main(null);
		        }
		    }
		});
		frmHctMagazyn.getContentPane().setLayout(null);
		
		JButton stockBtn = new JButton("Wyswietl magazyn");
		stockBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WyswietlMagazyn.main(null);
			}
		});
		stockBtn.setBounds(60, 173, 200, 36);
		frmHctMagazyn.getContentPane().add(stockBtn);
		
		JButton servicesBtn = new JButton("Wyswietl uslugi");
		servicesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WyswietlListeUslug.main(null);
			}
		});
		servicesBtn.setBounds(358, 173, 200, 36);
		frmHctMagazyn.getContentPane().add(servicesBtn);
		
		JButton invoiceBtn = new JButton("Wyswietl rachunki");
		invoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WyswietlRachunki.main(null);
			}
		});
		invoiceBtn.setBounds(358, 47, 200, 36);
		frmHctMagazyn.getContentPane().add(invoiceBtn);
		
		JButton nowyRachunekBtn = new JButton("Wystaw rachunek");
		nowyRachunekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WystawRachunek.main(null);
			}
		});
		nowyRachunekBtn.setBounds(60, 47, 200, 36);
		frmHctMagazyn.getContentPane().add(nowyRachunekBtn);
		
		JButton nowyTowarBtn = new JButton("Dodaj towar");
		nowyTowarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DodajTowar.main(null);
			}
		});
		nowyTowarBtn.setBounds(60, 110, 200, 36);
		frmHctMagazyn.getContentPane().add(nowyTowarBtn);
		
		JButton nowaUslugaBtn = new JButton("Dodaj usluge");
		nowaUslugaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DodajUsluge.main(null);
			}
		});
		nowaUslugaBtn.setBounds(358, 110, 200, 36);
		frmHctMagazyn.getContentPane().add(nowaUslugaBtn);
	}

}