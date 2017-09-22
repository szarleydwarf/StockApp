package app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import dbase.DatabaseManager;
import utillity.Helper;

import javax.swing.JTextField;

public class DodajTowar {

	private JFrame frame;
	private JButton btnZapisz;

	private DatabaseManager dm = null;
	private JTextField textFieldProductName;
	private JTextField tfCost;
	private JTextField tfPrice;
	private JTextField tfQnt;
	private JTextField tfStockNo;
	
	private String stockNum;
	private Helper helper;

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
		helper = new Helper();
		dm = new DatabaseManager();
		
		String query = "SELECT stock_number FROM stock ORDER BY stock_number DESC LIMIT 1";
		ArrayList<String> stNoList = dm.selectRecordArrayList(query);
		
		System.out.println("Array "+stNoList.get(0));
		if(!stNoList.get(0).isEmpty())
			stockNum = stNoList.get(0);
		else
			stockNum = "AAA0000";
		
		getIntFromStNo();
		System.out.println("after "+stockNum);
		
		initialize();
	}

	private void getIntFromStNo() {
		/*				String priceSt = tempSt.substring(tempSt.lastIndexOf("€")+1);
				priceSt = priceSt.substring(0, priceSt.lastIndexOf("x"));
				String quant = tempSt.substring(tempSt.lastIndexOf("x")+1);*/
		String stL = stockNum.substring(0, stockNum.lastIndexOf("A")+1);
		System.out.println("stL "+stL);
		int stN = Integer.parseInt(stockNum.replaceAll("[\\D]", ""));//((int)stockNum.lastIndexOf("A")+1);
		System.out.println("stN "+stN);
		stN++;
		String temp = helper.paddStringLeft(Integer.toString(stN), 2, '0');
		System.out.println("temp "+temp);
		temp = stL + temp;
		System.out.println("temp2 "+temp);
		stockNum = temp;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 556, 328);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JLabel lblTitle = new JLabel("Dodaj nowy produkt");
		lblTitle.setBounds(150, 11, 245, 26);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		frame.getContentPane().add(lblTitle);
	
		displayStockNumber();
		
		JLabel lblName = new JLabel("Nazwa produktu");
		lblName.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblName.setBounds(36, 100, 124, 26);
		frame.getContentPane().add(lblName);
		
		textFieldProductName = new JTextField();
		textFieldProductName.setBounds(170, 101, 321, 26);
		frame.getContentPane().add(textFieldProductName);
		textFieldProductName.setColumns(10);
		
		JLabel lblCost = new JLabel("Koszt");
		lblCost.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblCost.setBounds(36, 137, 124, 26);
		frame.getContentPane().add(lblCost);
		
		tfCost = new JTextField();
		tfCost.setColumns(10);
		tfCost.setBounds(170, 138, 321, 26);
		frame.getContentPane().add(tfCost);
		
		JLabel lblPrice = new JLabel("Cena");
		lblPrice.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblPrice.setBounds(36, 174, 124, 26);
		frame.getContentPane().add(lblPrice);
		
		tfPrice = new JTextField();
		tfPrice.setColumns(10);
		tfPrice.setBounds(170, 175, 321, 26);
		frame.getContentPane().add(tfPrice);
		
		JLabel lblQnt = new JLabel("Ilo\u015B\u0107");
		lblQnt.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblQnt.setBounds(36, 211, 124, 26);
		frame.getContentPane().add(lblQnt);
		
		tfQnt = new JTextField();
		tfQnt.setColumns(10);
		tfQnt.setBounds(170, 212, 321, 26);
		frame.getContentPane().add(tfQnt);
		
		btnZapisz = new JButton("Zapisz");
		btnZapisz.setBackground(new Color(255, 215, 0));
		btnZapisz.setForeground(Color.BLUE);
		btnZapisz.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnZapisz.setBounds(441, 249, 89, 23);
		frame.getContentPane().add(btnZapisz);
	
		zapiszNowyProdukt();

	}
	
	private void displayStockNumber() {
		JLabel lblStockNo = new JLabel("Numer magazynowy");
		lblStockNo.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblStockNo.setBounds(36, 62, 124, 26);
		frame.getContentPane().add(lblStockNo);
		
		tfStockNo = new JTextField();
		tfStockNo.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfStockNo.setHorizontalAlignment(SwingConstants.RIGHT);
		tfStockNo.setColumns(10);
		tfStockNo.setBounds(170, 63, 321, 26);
		frame.getContentPane().add(tfStockNo);
		
		tfStockNo.setText(stockNum);
	}

	private void zapiszNowyProdukt() {
		System.out.println("zapisz nowy");
		
		btnZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean saved = true;// dm.addNewRecord("INSERT INTO \"stock\"  VALUES ('"+stockNum+"','Dunlop 225/50 R18',75.86,152.12,4);");
				System.out.println("zapisuje");
				if(saved)
					JOptionPane.showMessageDialog(null, "Dodano nowy towar");
				else
					JOptionPane.showMessageDialog(null, "Błąd zapisu");
			}
		});		
	}
}
