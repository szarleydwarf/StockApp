package app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import dbase.DatabaseManager;

public class DodajTowar {

	private JFrame frame;
	private JButton btnZapisz;

	private DatabaseManager dm = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DodajTowar window = new DodajTowar();
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
	public DodajTowar() {
		dm = new DatabaseManager();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 556, 430);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Dodaj nowy produkt");
		lblTitle.setBounds(151, 0, 245, 26);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		frame.getContentPane().add(lblTitle);
		
		JLabel lblName = new JLabel("Nazwa produktu");
		lblName.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblName.setBounds(36, 100, 124, 26);
		frame.getContentPane().add(lblName);

		zapiszNowyProdukt();

	}
	
	private void zapiszNowyProdukt() {
		btnZapisz = new JButton("Zapisz");
		btnZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dm.addNewRecord("INSERT INTO \"stock\"  VALUES ('AAA0004','Dunlop 225/50 R18',75.86,152.12,4);");
			}

		});
		btnZapisz.setBackground(new Color(255, 215, 0));
		btnZapisz.setForeground(Color.BLUE);
		btnZapisz.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnZapisz.setBounds(441, 357, 89, 23);
		frame.getContentPane().add(btnZapisz);
	}

}
