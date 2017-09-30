package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;

import dbase.DatabaseManager;
import hct_speciale.Item;
import hct_speciale.StockItem;
import utillity.FinalVariables;
import utillity.Helper;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EdytujTowar {

	private JFrame frame;
	private JTextField tfStockNum;
	private JTextField tfName;
	private JTextField tfCost;
	private JTextField tfPrice;
	private JTextField tfQnt;
	private StockItem item;

//	private StockItem stItem;
	private DatabaseManager dm;
	private FinalVariables fv;
	private Helper helper;
	
	private String stockNum, productName="", cost="", price="", qnt="";
	private double dCost = 0, dPrice = 0;
	private int iQnt = 0;
	private boolean varEmpty = true;
	/**
	 * Launch the application.
	 */
	public static void main(Item i) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EdytujTowar window = new EdytujTowar(i);
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
	public EdytujTowar(Item i) {
		this.item = (StockItem) i;

		this.fv = new FinalVariables();
		helper = new Helper();
		this.dm = new DatabaseManager();

		initialize();
		populateTextFields();
	}

	private void populateTextFields() {
		if(!this.item.getStockNumber().isEmpty())
			this.tfStockNum.setText(this.item.getStockNumber());
		
		if(!this.item.getName().isEmpty())
			this.tfName.setText(this.item.getName());
		
		if(!Double.toString(this.item.getCost()).isEmpty())
			this.tfCost.setText(Double.toString(this.item.getCost()));
		
		if(!Double.toString(this.item.getPrice()).isEmpty())
			this.tfPrice.setText(Double.toString(this.item.getPrice()));
		
		if(!Integer.toString(this.item.getQnt()).isEmpty())
			this.tfQnt.setText(Integer.toString(this.item.getQnt()));
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 579, 193);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            fv.CLOSE_WINDOW, fv.CLOSE_WINDOW, 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		        	MainView.main(null);
		        }
		    }
		});
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Edycja przedmiotu");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblTitle.setBounds(228, 11, 174, 26);
		frame.getContentPane().add(lblTitle);
		
		tfStockNum = new JTextField();
		tfStockNum.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfStockNum.setHorizontalAlignment(SwingConstants.RIGHT);
		tfStockNum.setBounds(10, 68, 86, 24);
		frame.getContentPane().add(tfStockNum);
		tfStockNum.setColumns(10);
		
		JLabel lblStockNum = new JLabel("Numer mag");
		lblStockNum.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblStockNum.setBounds(10, 49, 86, 14);
		frame.getContentPane().add(lblStockNum);
		
		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(106, 68, 234, 24);
		frame.getContentPane().add(tfName);
		
		JLabel lblName = new JLabel("Nazwa");
		lblName.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblName.setBounds(106, 49, 86, 14);
		frame.getContentPane().add(lblName);
		
		tfCost = new JTextField();
		tfCost.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfCost.setHorizontalAlignment(SwingConstants.RIGHT);
		tfCost.setColumns(10);
		tfCost.setBounds(350, 68, 60, 24);
		frame.getContentPane().add(tfCost);
		
		JLabel lblCost = new JLabel("Koszt");
		lblCost.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblCost.setBounds(350, 49, 60, 14);
		frame.getContentPane().add(lblCost);
		
		tfPrice = new JTextField();
		tfPrice.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		tfPrice.setColumns(10);
		tfPrice.setBounds(418, 68, 60, 24);
		frame.getContentPane().add(tfPrice);
		
		JLabel lblPrice = new JLabel("Cena");
		lblPrice.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblPrice.setBounds(418, 49, 60, 14);
		frame.getContentPane().add(lblPrice);
		
		tfQnt = new JTextField();
		tfQnt.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfQnt.setHorizontalAlignment(SwingConstants.RIGHT);
		tfQnt.setColumns(10);
		tfQnt.setBounds(488, 68, 60, 24);
		frame.getContentPane().add(tfQnt);
		
		JLabel lblQnt = new JLabel("Ilo\u015B\u0107");
		lblQnt.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblQnt.setBounds(488, 49, 60, 14);
		frame.getContentPane().add(lblQnt);
		
		JButton btnNewButton = new JButton("Zapisz");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				varEmpty = getVariableForQuery();
				if(!varEmpty)
					zapiszWBazieDanych();
			}
		});
		btnNewButton.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnNewButton.setBounds(459, 120, 89, 23);
		frame.getContentPane().add(btnNewButton);
	}

	protected void zapiszWBazieDanych() {
		String query  = "UPDATE \"stock\" SET item_name='"+this.productName+"', cost='"+this.dCost+"', price='"+this.dPrice+"', quantity='"+this.iQnt+"' WHERE stock_number='"+this.item.getStockNumber()+"'"; 
		System.out.println("Q: "+query);
		boolean saved = dm.editRecord(query);
		if(saved){
			JOptionPane.showMessageDialog(null, "Edycja zakończona pomyślnie");
			this.frame.dispose();
		} else
			JOptionPane.showMessageDialog(null, "Błąd zapisu");

	}
	
	private boolean getVariableForQuery() {
//		this.stockNum = this.tfStockNum.getText();
//		if(this.stockNum.isEmpty()) {
//			JOptionPane.showMessageDialog(null, "Błędny numer magazynowy.");
//			return true;
//		}
		
		this.productName = this.tfName.getText();
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

}
