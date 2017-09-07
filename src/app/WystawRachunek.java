package app;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
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
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import dbase.DatabaseManager;

import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

public class WystawRachunek {

	private JFrame frmNowyRachunek;
	private DatabaseManager DM;
	private JTextField textField;
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
		frmNowyRachunek.setBounds(100, 100, 713, 426);
		frmNowyRachunek.getContentPane().setLayout(null);
		
		JLabel labelSaleList = new JLabel("Us\u0142uga / Towar");
		labelSaleList.setVerticalAlignment(SwingConstants.TOP);
		labelSaleList.setBounds(450, 40, 96, 19);
		frmNowyRachunek.getContentPane().add(labelSaleList);
		
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
		
				
		
		JLabel lblChoseServiceitem = new JLabel("Wybierz us\u0142ug\u0119");
		lblChoseServiceitem.setBounds(23, 40, 109, 36);
		frmNowyRachunek.getContentPane().add(lblChoseServiceitem);
		
		JScrollPane scrollPaneServiceList = new JScrollPane();
		scrollPaneServiceList.setBounds(164, 40, 194, 36);
		frmNowyRachunek.getContentPane().add(scrollPaneServiceList);
		

		JList listServices = new JList();
		scrollPaneServiceList.setViewportView(listServices);
		listServices.setModel(model);
		
		JLabel lblWybrane = new JLabel("");
		Border b = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border = BorderFactory.createTitledBorder(b, "WYBRANE");
		lblWybrane.setBorder(border);
		lblWybrane.setVerticalAlignment(SwingConstants.TOP);
		lblWybrane.setBounds(439, 11, 216, 239);
		frmNowyRachunek.getContentPane().add(lblWybrane);
		
		JButton btnNewButton = new JButton("+");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(379, 46, 50, 24);
		frmNowyRachunek.getContentPane().add(btnNewButton);
		
		JLabel lblWybierzPrzedmiot = new JLabel("Wybierz przedmiot");
		lblWybierzPrzedmiot.setBounds(23, 87, 109, 36);
		frmNowyRachunek.getContentPane().add(lblWybierzPrzedmiot);
		
		JScrollPane scrollPaneItemList = new JScrollPane();
		scrollPaneItemList.setBounds(164, 87, 194, 36);
		frmNowyRachunek.getContentPane().add(scrollPaneItemList);
		
		JList listItems = new JList();
		scrollPaneItemList.setViewportView(listItems);
		
		JButton button = new JButton("+");
		button.setBounds(379, 94, 50, 24);
		frmNowyRachunek.getContentPane().add(button);
		
		textField = new JTextField();
		textField.setBounds(262, 186, 96, 32);
		frmNowyRachunek.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel labelZnizka = new JLabel("Znizka");
		labelZnizka.setBounds(23, 186, 109, 36);
		frmNowyRachunek.getContentPane().add(labelZnizka);
		
		JRadioButton rbPercent = new JRadioButton("%");
		rbPercent.setBounds(134, 186, 37, 23);
		frmNowyRachunek.getContentPane().add(rbPercent);
		
		JRadioButton rbMoney = new JRadioButton("\u20AC");
		rbMoney.setBounds(173, 186, 37, 23);
		frmNowyRachunek.getContentPane().add(rbMoney);
		
		JLabel lblCena = new JLabel("Cena");
		lblCena.setVerticalAlignment(SwingConstants.TOP);
		lblCena.setBounds(599, 40, 43, 19);
		frmNowyRachunek.getContentPane().add(lblCena);
		
		JLabel label = new JLabel("1, ");
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setBounds(450, 70, 192, 169);
		frmNowyRachunek.getContentPane().add(label);
		
		JButton btnNewButton_1 = new JButton("Policz = ");
		btnNewButton_1.setForeground(new Color(0, 0, 0));
		btnNewButton_1.setBackground(new Color(220, 20, 60));
		btnNewButton_1.setBounds(304, 293, 120, 32);
		frmNowyRachunek.getContentPane().add(btnNewButton_1);
		
		JLabel lblTotal = new JLabel("TOTAL");
		lblTotal.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotal.setBounds(450, 293, 205, 32);
		frmNowyRachunek.getContentPane().add(lblTotal);
		
		JLabel labelClient = new JLabel("Klient");
		labelClient.setBounds(23, 139, 109, 36);
		frmNowyRachunek.getContentPane().add(labelClient);
		
		JScrollPane scrollPaneCarList = new JScrollPane();
		scrollPaneCarList.setBounds(164, 134, 194, 36);
		frmNowyRachunek.getContentPane().add(scrollPaneCarList);
		
		JList listCars = new JList();
		scrollPaneCarList.setViewportView(listCars);
		
		JButton btnPrint = new JButton("Drukuj rachunek");
		btnPrint.setForeground(Color.BLACK);
		btnPrint.setBackground(new Color(178, 34, 34));
		btnPrint.setBounds(439, 344, 248, 32);
		frmNowyRachunek.getContentPane().add(btnPrint);
		
		
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
