package app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dbase.DatabaseManager;
import utillity.FinalVariables;
import utillity.Helper;

public class DodajTowar {

	private JFrame frame;
	private JButton btnZapisz;

	private JTextField textFieldProductName;
	private JTextField tfCost;
	private JTextField tfPrice;
	private JTextField tfQnt;
	private JTextField tfStockNo;
	
	private String stockNum, productName="", cost="", price="", qnt="";
	private boolean varEmpty = true;
	private double dCost = 0, dPrice = 0;
	private int iQnt = 0;

	private Helper helper;
	private DatabaseManager dm = null;
	private FinalVariables fv;
	

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
		this.fv = new FinalVariables();

		String query = "SELECT "+this.fv.STOCK_TABLE_NUMBER+" FROM "+this.fv.STOCK_TABLE+" ORDER BY "+this.fv.STOCK_TABLE_NUMBER+" DESC LIMIT 1";
		ArrayList<String> stNoList = dm.selectRecordArrayList(query);
		
		if(!stNoList.get(0).isEmpty())
			stockNum = stNoList.get(0);
		else
			stockNum = "AAA0000";
		
		helper.getIntFromStNo(stockNum, 'A');
		this.fv = new FinalVariables();
		
		initialize();
	}



	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 556, 328);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            fv.CLOSE_WINDOW, fv.CLOSE_WINDOW, 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        	MainView.main(null);
		        }
		    }
		});
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
	
		btnZapisz.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				varEmpty = getVariableForQuery();
				if(!varEmpty)
					zapiszNowyProdukt();
			}
		});		

	}
	
	private boolean getVariableForQuery() {
		this.productName = this.textFieldProductName.getText();
		if(this.productName.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Wpisz nazwe produktu");
			return true;
		}
		
		this.cost = this.tfCost.getText();
		if(this.dCost == 0){
			this.dCost = this.helper.checkDouble("Wpisz koszt",  "Niepoprawny format kosztu", this.cost);
			return true;
		}
	
		this.price = this.tfPrice.getText();
		if(this.dPrice == 0) {
			this.dPrice = helper.checkDouble("Wpisz cene", "Niepoprawny format ceny", this.price);
			return true;
		}
	
		this.qnt = this.tfQnt.getText();
		if(this.iQnt == 0){
			this.iQnt = this.helper.checkInteger("Wpisz ilość","Niepoprawny format ilosci", this.qnt);
			return true;
		}
	
		return false;	
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
		boolean saved = dm.addNewRecord("INSERT INTO \""+this.fv.STOCK_TABLE+"\"  VALUES ('"+stockNum+"','"+this.productName+"',"+this.dCost+","+this.dPrice+","+this.iQnt+");");
//		System.out.println("zapisuje "+stockNum+"','"+this.productName+"',"+this.dCost+","+this.dPrice+","+this.iQnt);
		if(saved){
			JOptionPane.showMessageDialog(null, "Dodano nowy towar");
			this.frame.dispose();
		} else
			JOptionPane.showMessageDialog(null, "Błąd zapisu");

	}
}
