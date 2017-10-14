package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.SwingConstants;

import dbase.DatabaseManager;
import hct_speciale.Item;
import hct_speciale.StockItem;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;

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
	private Item item;

//	private StockItem stItem;
	private DatabaseManager dm;
	private FinalVariables fv;

	protected static String date;
	protected static String loggerFolderPath;
	private static Logger log;
	private static Helper helper;
	
	private String stockNum, productName="", cost="", price="", qnt="";
	private double dCost = 0, dPrice = 0;
	private int iQnt = 0;
	private boolean varEmpty = true;
	/**
	 * Launch the application.
	 */
	public static void main(Item i, String p_loggerFolderPath) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				loggerFolderPath = p_loggerFolderPath;
				log = new Logger(loggerFolderPath);
				helper = new Helper();
				date = helper.getFormatedDate();
				try {
					EdytujTowar window = new EdytujTowar(i);
					window.frame.setVisible(true);
				} catch (Exception e) {
					log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EdytujTowar(Item i) {
		this.item = (Item) i;

		this.fv = new FinalVariables();
		helper = new Helper();
		this.dm = new DatabaseManager(loggerFolderPath);

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
		
		if(this.item instanceof StockItem) {
			if(!Integer.toString(((StockItem) this.item).getQnt()).isEmpty())
				this.tfQnt.setText(Integer.toString(((StockItem) this.item).getQnt()));
		}
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
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
		
		JButton bgtnSave = new JButton("Zapisz");
		bgtnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				varEmpty = getVariableForQuery();
				if(!varEmpty)
					zapiszWBazieDanych();
			}
		});
		bgtnSave.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		bgtnSave.setBounds(350, 120, 89, 23);
		frame.getContentPane().add(bgtnSave);
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		btnBack.setBounds(459, 120, 89, 23);
		frame.getContentPane().add(btnBack);
	}

	protected void zapiszWBazieDanych() {
		String tableName = "", addToQuery = "", columnName="", colNameToSet="";
		if(this.item instanceof StockItem) {
			tableName = "stock";
			addToQuery = ", quantity='"+this.iQnt+"'";
			columnName = "stock_number";
			colNameToSet = "item_name";
		} else {
			tableName = "services";
			columnName = "service_number";
			colNameToSet = "service_name";
		}
		String query  = "UPDATE \""+tableName+"\" SET "+colNameToSet+"='"+this.productName+"', cost='"+this.dCost+"', price='"+this.dPrice+"'";
		query += (addToQuery);
		query += " WHERE "+columnName+"='"+this.item.getStockNumber()+"'";
		
//		System.out.println("Q: "+query);
		boolean saved =  dm.editRecord(query);
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
		if(!this.cost.isEmpty()){
			this.dCost = this.helper.checkDouble("Wpisz koszt",  "Niepoprawny format kosztu", this.cost);
			if(this.dCost == 0)
				return true;
		}
	
		this.price = this.tfPrice.getText();
		if(!this.price.isEmpty()) {
			this.dPrice = helper.checkDouble("Wpisz cene", "Niepoprawny format ceny", this.price);
			if(this.dPrice == 0)
				return true;
		}
	
		if(this.item instanceof StockItem) {
			this.qnt = this.tfQnt.getText();
			if(!this.qnt.isEmpty()){
				this.iQnt = this.helper.checkInteger("Wpisz ilość","Niepoprawny format ilosci", this.qnt);
				if(this.iQnt == 0)
					return true;
			}
		}
		return false;	
	}

}
