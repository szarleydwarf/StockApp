package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import dbase.DatabaseManager;
import utillity.Helper;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;

public class WyswietlMagazyn {

	private JFrame frame;
	private JScrollPane scrollPane ;

	private Helper helper;
	private DatabaseManager DM;	
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WyswietlMagazyn window = new WyswietlMagazyn();
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
	public WyswietlMagazyn() {
		DM = new DatabaseManager();
		helper = new Helper();
		
		initialize();
		populateList();
		
	}

	private void populateList() {
		String query = "SELECT item_name, cost, price,quantity from stock ORDER BY item_name ASC";
		DefaultListModel<String> modelItems = new DefaultListModel<>();
		
		ArrayList<String> listOfItems = DM.selectRecordArrayList(query);
		
		for(int i = 0; i < listOfItems.size(); i++) {
			String tempString = listOfItems.get(i);
			modelItems.addElement(tempString);
//			System.out.println(tempString);
		}
		JList<String> list = new JList<String>();
		scrollPane.setViewportView(list);
		list.setModel(modelItems);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 540, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("MAGAZYN");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		lblTitle.setBounds(141, 11, 120, 24);
		frame.getContentPane().add(lblTitle);
		
		JButton btnNewButton = new JButton("Od\u015Bwie\u017C");
		btnNewButton.setBackground(new Color(255, 255, 153));
		btnNewButton.setForeground(new Color(0, 153, 255));
		btnNewButton.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnNewButton.setBounds(425, 15, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		Border b = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border = BorderFactory.createTitledBorder(b, "WYBRANE");
		
		JLabel label = new JLabel("");
		label.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		label.setBorder(border);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setBounds(10, 89, 370, 461);
		
		label.setVerticalAlignment(SwingConstants.TOP);
		frame.getContentPane().add(label);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 138, 345, 400);
		frame.getContentPane().add(scrollPane);
				
		JLabel lblPrice = new JLabel("Cena");
		lblPrice.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrice.setBounds(254, 117, 50, 19);
		frame.getContentPane().add(lblPrice);
		
		JLabel lblItem = new JLabel("Towar");
		lblItem.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblItem.setHorizontalAlignment(SwingConstants.CENTER);
		lblItem.setBounds(21, 117, 96, 19);
		frame.getContentPane().add(lblItem);
		
		JLabel lblQty = new JLabel("Qty");
		lblQty.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblQty.setHorizontalAlignment(SwingConstants.CENTER);
		lblQty.setBounds(314, 117, 37, 19);
		frame.getContentPane().add(lblQty);
		
		JLabel lblCost = new JLabel("Koszt");
		lblCost.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblCost.setHorizontalAlignment(SwingConstants.CENTER);
		lblCost.setBounds(183, 117, 50, 19);
		frame.getContentPane().add(lblCost);
		
		textField = new JTextField();
		textField.setBounds(21, 46, 195, 24);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnSearch = new JButton("Szukaj");
		btnSearch.setForeground(new Color(255, 255, 204));
		btnSearch.setBackground(new Color(0, 153, 255));
		btnSearch.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnSearch.setBounds(215, 46, 89, 24);
		frame.getContentPane().add(btnSearch);
		
		JButton btnEdit = new JButton("Edytuj zaznaczone");
		btnEdit.setForeground(new Color(255, 255, 255));
		btnEdit.setBackground(new Color(255, 102, 102));
		btnEdit.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnEdit.setBounds(390, 116, 124, 24);
		frame.getContentPane().add(btnEdit);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        }
		    }
		});
	}
}
