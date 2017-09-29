package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import dbase.DatabaseManager;
import hct_speciale.StockItem;

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
	private String valuesToEdit;

	private StockItem item;
	private DatabaseManager dm;
	/**
	 * Launch the application.
	 */
	public static void main(String selected) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EdytujTowar window = new EdytujTowar(selected);
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
	public EdytujTowar(String toEdit) {
		this.valuesToEdit = toEdit;
		getItemFromString();
		
		initialize();
	}

	private void getItemFromString() {
		String name = this.valuesToEdit.substring(0, this.valuesToEdit.indexOf("  "));
		String temp =  this.valuesToEdit.substring(this.valuesToEdit.indexOf("  "),valuesToEdit.indexOf("."));
		System.out.println("1 "+temp);
		temp =  this.valuesToEdit.substring(this.valuesToEdit.indexOf(".")+1,this.valuesToEdit.indexOf("  "));
		System.out.println("2 "+temp);
		
//		double price = Double.parseDouble(temp);
//		System.out.println(name + " "+price);
		
//		item = new StockItem(valuesToEdit, valuesToEdit, 0, 0, 0);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 579, 193);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Edycja przedmiotu");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblTitle.setBounds(228, 11, 174, 26);
		frame.getContentPane().add(lblTitle);
		
		tfStockNum = new JTextField();
		tfStockNum.setBounds(10, 68, 100, 24);
		frame.getContentPane().add(tfStockNum);
		tfStockNum.setColumns(10);
		
		JLabel lblStockNum = new JLabel("Numer mag");
		lblStockNum.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblStockNum.setBounds(10, 49, 86, 14);
		frame.getContentPane().add(lblStockNum);
		
		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(120, 68, 100, 24);
		frame.getContentPane().add(tfName);
		
		JLabel lblName = new JLabel("Nazwa");
		lblName.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblName.setBounds(120, 49, 86, 14);
		frame.getContentPane().add(lblName);
		
		tfCost = new JTextField();
		tfCost.setColumns(10);
		tfCost.setBounds(228, 68, 100, 24);
		frame.getContentPane().add(tfCost);
		
		JLabel lblCost = new JLabel("Koszt");
		lblCost.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblCost.setBounds(228, 49, 86, 14);
		frame.getContentPane().add(lblCost);
		
		tfPrice = new JTextField();
		tfPrice.setColumns(10);
		tfPrice.setBounds(338, 67, 100, 24);
		frame.getContentPane().add(tfPrice);
		
		JLabel lblPrice = new JLabel("Cena");
		lblPrice.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblPrice.setBounds(338, 48, 86, 14);
		frame.getContentPane().add(lblPrice);
		
		tfQnt = new JTextField();
		tfQnt.setColumns(10);
		tfQnt.setBounds(448, 68, 100, 24);
		frame.getContentPane().add(tfQnt);
		
		JLabel lblQnt = new JLabel("Ilo\u015B\u0107");
		lblQnt.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblQnt.setBounds(448, 49, 86, 14);
		frame.getContentPane().add(lblQnt);
		
		JButton btnNewButton = new JButton("Zapisz");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zapiszWBazieDanych();
			}
		});
		btnNewButton.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnNewButton.setBounds(459, 120, 89, 23);
		frame.getContentPane().add(btnNewButton);
	}

	protected void zapiszWBazieDanych() {
		// TODO Auto-generated method stub
		
	}
}
