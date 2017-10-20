package app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dbase.DatabaseManager;
import hct_speciale.Item;
import hct_speciale.StockItem;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;

public class WyswietlMagazyn {

	private JFrame frame;
	private JScrollPane scrollPane ;
	private JList<String> list ;
	
	private static String loggerFolderPath;
	private static String date;
	
	private static Helper helper;
	private DatabaseManager DM;	
	private JTextField tfSearch;
	private ArrayList<Item> listOfItems, listOfServices;
	private FinalVariables fv;
	private static Logger log;
	/**
	 * Launch the application.
	 */
	public static void main(String p_loggerFolderPath) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				loggerFolderPath = p_loggerFolderPath;
				log = new Logger(loggerFolderPath);
				helper = new Helper();
				date = helper.getFormatedDate();
				try {
					WyswietlMagazyn window = new WyswietlMagazyn();
					window.frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Coś poszło nie tak\n"+e.getMessage());
					log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
//					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WyswietlMagazyn() {
		DM = new DatabaseManager(loggerFolderPath);

		list = new JList<String>();
		this.listOfItems = new ArrayList<Item>();
		this.fv = new FinalVariables();

		initialize();
		populateList();
		
	}

	private void populateList() {
		String query = "SELECT * from "+this.fv.STOCK_TABLE+" ORDER BY "+this.fv.STOCK_TABLE_ITEM_NAME+" ASC";//item_name, cost, price,quantity
		String queryServices = "SELECT * from "+this.fv.SERVICES_TABLE+" ORDER BY "+this.fv.SERVICES_TABLE_SERVICE_NAME+" ASC";//item_name, cost, price,quantity
		DefaultListModel<String> modelItems = new DefaultListModel<>();
		
		listOfItems = DM.getItemsList(query);
		listOfServices = DM.getItemsList(queryServices);
		
		for(int i = 0; i < listOfItems.size(); i++) {
			StockItem item = (StockItem) listOfItems.get(i);
			modelItems.addElement(item.toString());
		}
		for(int i = 0; i < listOfServices.size(); i++) {
			Item item = (Item) listOfServices.get(i);
			modelItems.addElement(item.toString());
		}
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		scrollPane.setViewportView(list);
		list.setModel(modelItems);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 587, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("MAGAZYN");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		lblTitle.setBounds(141, 11, 120, 24);
		frame.getContentPane().add(lblTitle);
		
		JButton btnRefresh = new JButton("Od\u015Bwie\u017C");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				populateList();
			}
		});
		btnRefresh.setBackground(new Color(255, 255, 153));
		btnRefresh.setForeground(new Color(0, 153, 255));
		btnRefresh.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnRefresh.setBounds(10, 46, 89, 23);
		frame.getContentPane().add(btnRefresh);
		
		Border b = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border = BorderFactory.createTitledBorder(b, "LISTA USŁUG I TOWARÓW");
		
		JLabel label = new JLabel("");
		label.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		label.setBorder(border);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setBounds(10, 89, 551, 461);
		
		label.setVerticalAlignment(SwingConstants.TOP);
		frame.getContentPane().add(label);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 138, 421, 400);
		frame.getContentPane().add(scrollPane);
				
		JLabel lblPrice = new JLabel("Cena");
		lblPrice.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrice.setBounds(282, 117, 50, 19);
		frame.getContentPane().add(lblPrice);
		
		JLabel lblItem = new JLabel("Towar");
		lblItem.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblItem.setHorizontalAlignment(SwingConstants.CENTER);
		lblItem.setBounds(21, 117, 96, 19);
		frame.getContentPane().add(lblItem);
		
		JLabel lblQty = new JLabel("Qty");
		lblQty.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblQty.setHorizontalAlignment(SwingConstants.CENTER);
		lblQty.setBounds(378, 117, 37, 19);
		frame.getContentPane().add(lblQty);
		
		JLabel lblCost = new JLabel("Koszt");
		lblCost.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblCost.setHorizontalAlignment(SwingConstants.CENTER);
		lblCost.setBounds(197, 117, 50, 19);
		frame.getContentPane().add(lblCost);
		
		tfSearch = new JTextField();
		tfSearch.setHorizontalAlignment(SwingConstants.CENTER);
		tfSearch.setText(this.fv.SEARCH_TEXT_FIELD_FRAZE );
		tfSearch.setBounds(120, 46, 195, 24);
		frame.getContentPane().add(tfSearch);
		tfSearch.setColumns(10);
		
		tfSearch.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	            tfSearch.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		
		
		JButton btnSearch = new JButton("Szukaj");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchInDatabase();
			}
		});
		btnSearch.setForeground(new Color(255, 255, 204));
		btnSearch.setBackground(new Color(0, 153, 255));
		btnSearch.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnSearch.setBounds(314, 46, 89, 24);
		frame.getContentPane().add(btnSearch);
		
		JButton btnEdit = new JButton("Edytuj zaznaczone");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editRecordInDatabase();
			}
		});
		btnEdit.setForeground(new Color(255, 255, 255));
		btnEdit.setBackground(new Color(255, 215, 0));
		btnEdit.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnEdit.setBounds(413, 45, 124, 24);
		frame.getContentPane().add(btnEdit);
		
		JButton btnDelete = new JButton("Usuń");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteRocordFromDatabase();
			}
		});
		btnDelete.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		btnDelete.setBounds(468, 490, 71, 20);
		frame.getContentPane().add(btnDelete);
		
		this.list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if(!list.isSelectionEmpty()){
					helper.toggleJButton(btnDelete, Color.RED, Color.ORANGE, true);
				} else {
					helper.toggleJButton(btnDelete, Color.DARK_GRAY, Color.lightGray, false);
				}
			}
			
		});
		
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
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		btnBack.setBounds(452, 515, 100, 23);
		frame.getContentPane().add(btnBack);
	}
	
	private void deleteRocordFromDatabase() {
		if(!list.isSelectionEmpty()){
			Item i = getItemFromLists();
			String tableName = "", columnName = "", column2Name = "";
			if(i instanceof StockItem){
				tableName = this.fv.STOCK_TABLE;
				columnName = this.fv.STOCK_TABLE_NUMBER;
				column2Name = this.fv.STOCK_TABLE_ITEM_NAME;
			}else{
				tableName = this.fv.SERVICES_TABLE;
				columnName = this.fv.SERVICE_TABLE_NUMBER;
				column2Name = this.fv.SERVICES_TABLE_SERVICE_NAME;
			}
			
			if(i != null){
				String query = "DELETE FROM '"+tableName+"' WHERE "+columnName+"='"+i.getStockNumber()+"' AND "+column2Name+"='"+i.getName()+"'";
				try {
					boolean success = this.DM.deleteRecord(query);
					if(success)
						JOptionPane.showMessageDialog(null, this.fv.DELETE_SUCCESS);
					else
						JOptionPane.showMessageDialog(null, this.fv.DELETING_ERROR);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, this.fv.DELETING_ERROR);
					log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
				}
				
			}else
				JOptionPane.showMessageDialog(null, this.fv.WINDOW_ERROR);

		}
	}
	
	private void editRecordInDatabase() {
		if(!list.isSelectionEmpty()){
			Item i = getItemFromLists();
			
			if(i != null)
				EdytujTowar.main(i, this.loggerFolderPath);
			else
				JOptionPane.showMessageDialog(null, this.fv.WINDOW_ERROR);

		} else if(list.isSelectionEmpty()){
			JOptionPane.showMessageDialog(null, "Zaznacz przedmiot do edycji");
		}
	}

	private Item getItemFromLists() {
		int listsLength = this.listOfItems.size() + this.listOfServices.size();
		int index = list.getSelectedIndex();

		if(index < this.listOfItems.size())
			return this.listOfItems.get(index);
		else if(index < listsLength){
			index -= this.listOfItems.size();
			return this.listOfServices.get(index);
		}
		return null;
	}

	private void searchInDatabase() {
		//"SELECT "+this.fv.SERVICE_TABLE_NUMBER+" FROM "+this.fv.SERVICES_TABLE+" WHERE "+this.fv.SERVICES_TABLE_SERVICE_NAME+"=\""+des+"\" 
		//union all SELECT "+this.fv.STOCK_TABLE_NUMBER+" FROM "+this.fv.STOCK_TABLE+" WHERE "+this.fv.STOCK_TABLE_ITEM_NAME+"=\""+des+"\""
		String query = "SELECT * FROM "+this.fv.STOCK_TABLE+"";
		if(!tfSearch.getText().equals(this.fv.SEARCH_TEXT_FIELD_FRAZE))
			query += " WHERE "+this.fv.STOCK_TABLE_ITEM_NAME+" LIKE '%"+tfSearch.getText()+"%' ORDER BY "+this.fv.STOCK_TABLE_PRICE+" ASC";
				
		DefaultListModel<String> modelItems = new DefaultListModel<>();
		
		ArrayList<Item> listOfItems = DM.getItemsList(query);
		
		if(listOfItems.size() <= 0){
			query = "SELECT * FROM "+this.fv.SERVICES_TABLE+"";
			if(!tfSearch.getText().equals(this.fv.SEARCH_TEXT_FIELD_FRAZE))
				query += " WHERE "+this.fv.SERVICES_TABLE_SERVICE_NAME+" LIKE '%"+tfSearch.getText()+"%' ORDER BY "+this.fv.STOCK_TABLE_PRICE+" ASC";
			
			listOfItems = DM.getItemsList(query);
		}
		
		for(int i = 0; i < listOfItems.size(); i++) {
			Item item = listOfItems.get(i);
			modelItems.addElement(item.toString());
		}
//		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		scrollPane.setViewportView(list);
		list.setModel(modelItems);
	}
}