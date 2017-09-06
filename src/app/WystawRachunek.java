package app;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Toolkit;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JList;
import javax.swing.border.LineBorder;

import dbase.DatabaseManager;

import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollBar;

public class WystawRachunek {

	private JFrame frmNowyRachunek;
	private DatabaseManager DM;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WystawRachunek window = new WystawRachunek();
					window.frmNowyRachunek.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public WystawRachunek() throws SQLException {
		DM = new DatabaseManager();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		frmNowyRachunek = new JFrame();
		
		frmNowyRachunek.setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\@Development\\EclipseJavaProjects\\sqliteTestApp\\StockApp\\resources\\img\\icon_hct.png"));
		frmNowyRachunek.setTitle("Nowy Rachunek - HCT");
		frmNowyRachunek.setBounds(100, 100, 450, 300);
		frmNowyRachunek.getContentPane().setLayout(null);
		
		JLabel lblWystawRachunek = new JLabel("Wystaw rachunek");
		lblWystawRachunek.setBounds(154, 11, 133, 19);
		lblWystawRachunek.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		frmNowyRachunek.getContentPane().add(lblWystawRachunek);
		
		String query = "SELECT * FROM services";
		DefaultListModel<String> model = new DefaultListModel<>();
	
		ArrayList<String> alist = DM.selectRecordArrayList(query);
		
		for(int i = 0; i < alist.size(); i++) {
			String tempString = alist.get(i);
			model.addElement(tempString);
//			System.out.println("ST: "+alist.get(i));
		}
		
				
		
		JLabel lblChoseServiceitem = new JLabel("Chose service/item");
		lblChoseServiceitem.setBounds(23, 40, 109, 36);
		frmNowyRachunek.getContentPane().add(lblChoseServiceitem);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(195, 40, 200, 36);
		frmNowyRachunek.getContentPane().add(scrollPane);
		

		JList list = new JList();
		scrollPane.setViewportView(list);
		list.setModel(model);
		
		
		frmNowyRachunek.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frmNowyRachunek, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frmNowyRachunek.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        }
		    }
		});
	}
}
