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
	
	private int paddingLength = 30;
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
		frmNowyRachunek.setBounds(100, 100, 763, 426);
		frmNowyRachunek.getContentPane().setLayout(null);
		
		JScrollPane scrollPaneChosen = new JScrollPane();
		scrollPaneChosen.setBounds(450, 60, 225, 178);
		frmNowyRachunek.getContentPane().add(scrollPaneChosen);
		
		JList listChosen = new JList();
		scrollPaneChosen.setViewportView(listChosen);
		
		
		JLabel labelSaleList = new JLabel("Us\u0142uga / Towar");
		labelSaleList.setVerticalAlignment(SwingConstants.TOP);
		labelSaleList.setBounds(450, 40, 96, 19);
		frmNowyRachunek.getContentPane().add(labelSaleList);
		
		JLabel lblWystawRachunek = new JLabel("Wystaw rachunek");
		lblWystawRachunek.setBounds(154, 11, 133, 19);
		lblWystawRachunek.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		frmNowyRachunek.getContentPane().add(lblWystawRachunek);
		
		String query = "SELECT service_name, price FROM services";
		DefaultListModel<String> model = new DefaultListModel<>();
	
		ArrayList<String> listOfServices = DM.selectRecordArrayList(query);
		
		for(int i = 0; i < listOfServices.size(); i+=2) {
			String tempString = listOfServices.get(i);
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
		lblWybrane.setBounds(439, 11, 248, 239);
		frmNowyRachunek.getContentPane().add(lblWybrane);
		
		JButton btnNewButton = new JButton("+");
		btnNewButton.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		DefaultListModel<String> model2Add = new DefaultListModel<>();
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				char ch = '_';
				String element2model = (String) listServices.getSelectedValue();
				
				int index = listOfServices.indexOf(element2model);
				String tempString = listOfServices.get(index+1);
				
				element2model = paddString(element2model, paddingLength, ch);
				
				
				
				element2model += " "+tempString;
				model2Add.addElement(element2model);
				listChosen.setModel(model2Add);
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
		button.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
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
		lblCena.setBounds(632, 40, 43, 19);
		frmNowyRachunek.getContentPane().add(lblCena);
		
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
		

		JButton buttonSubb = new JButton("-");
		buttonSubb.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		buttonSubb.setBounds(687, 32, 50, 24);
		frmNowyRachunek.getContentPane().add(buttonSubb);
		
		
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
	
	public String paddString(String string2Padd, int stringLength, char paddingChar){
		if(stringLength <= 0)
			return string2Padd;
		
		StringBuilder sb = new StringBuilder(string2Padd);
		stringLength = stringLength - sb.length() - 1;
		while(stringLength-- >= 0){
			sb.append(paddingChar);
		}
		return sb.toString();
		
	}
}
