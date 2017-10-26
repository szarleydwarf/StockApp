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
	private JTextField tfQnt4Invoice;
	private String lblQntLabel = "Dostepnych ";
	private int selectedQnt, count = 0;
	protected String lblQntText = "Dostepnych ";
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
		frame.setBackground(new Color(255, 255, 0));
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 799, 600);
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
		label.setBounds(10, 89, 762, 461);
		
		label.setVerticalAlignment(SwingConstants.TOP);
		frame.getContentPane().add(label);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 138, 552, 400);
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
		btnDelete.setBounds(675, 490, 71, 20);
		frame.getContentPane().add(btnDelete);
		
		
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
		btnBack.setBounds(659, 515, 100, 23);
		frame.getContentPane().add(btnBack);
		
		JButton btnAddToInvoice = new JButton("do Rachunku");
		btnAddToInvoice.setEnabled(false);
		btnAddToInvoice.setBounds(665, 102, 94, 23);
		btnAddToInvoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToInvoice();
			}
		});
		frame.getContentPane().add(btnAddToInvoice);
		
		tfQnt4Invoice = new JTextField();
		tfQnt4Invoice.setBounds(594, 103, 60, 20);
		frame.getContentPane().add(tfQnt4Invoice);
		tfQnt4Invoice.setColumns(10);
		
		JLabel lblQnt2Invoice = new JLabel(lblQntLabel );
		lblQnt2Invoice.setForeground(Color.YELLOW);
		lblQnt2Invoice.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		lblQnt2Invoice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQnt2Invoice.setBounds(290, 106, 300, 14);
		frame.getContentPane().add(lblQnt2Invoice);

		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {

				if(!list.isSelectionEmpty()){
					helper.toggleJButton(btnDelete, Color.RED, Color.ORANGE, true);
					helper.toggleJButton(btnAddToInvoice, Color.GREEN, Color.WHITE, true);
					lblQntLabel = lblQntText;
					getSelectedItem();

					if(selectedQnt != 0 && count == 0){
						lblQntLabel = lblQntLabel+" "+ selectedQnt;
						count++;
						lblQnt2Invoice.setText(lblQntLabel);
					}
					count = 0;

				} else {
					helper.toggleJButton(btnDelete, Color.DARK_GRAY, Color.lightGray, false);
					helper.toggleJButton(btnAddToInvoice, Color.LIGHT_GRAY, Color.GRAY, false);
				}
			}			
		});	
	}
	
	protected void addToInvoice() {
		String itemForList = getSelectedItem();
//		System.out.println("item "+itemForList);
		String test="";
		do{
			test = checkQnt(itemForList);
			if(test.isEmpty())
				return;
		}while (test =="");
		itemForList = test;

		ArrayList<String> defaultPaths = new ArrayList<String>();
		defaultPaths = this.DM.getPaths("SELECT "+this.fv.SETTINGS_TABLE_PATH+" FROM "+this.fv.SETTINGS_TABLE);
		defaultPaths.add(itemForList);
		this.frame.dispose();
		
		WystawRachunek.main(defaultPaths);
	}

	private String checkQnt(String itemForList) {
		String str = itemForList.substring(itemForList.lastIndexOf("x")+1);
		int qnt=0;
		int qntInList=0;
		if(!str.isEmpty() && (str.matches("[0-9]+")))
			qntInList = Integer.parseInt(str);
		else
			qntInList = 1;

		if(!tfQnt4Invoice.getText().isEmpty() ){
			qnt = Integer.parseInt(tfQnt4Invoice.getText());

			while(qnt > qntInList ){
				JOptionPane.showMessageDialog(this.frame, "Dostępnych "+qntInList+"szt.");
				if(qnt > qntInList)
					return "";
			}
			str = itemForList.replace(itemForList.substring(itemForList.lastIndexOf("x")+1), tfQnt4Invoice.getText());
		} else
			str = itemForList;
//		System.out.println(str);
		return str;
	}

	private String getSelectedItem() {
		Item i = getItemFromLists();
		String str = "";
		if(i != null){
			str = i.getName()+" €"+i.getPrice();
			if(i instanceof StockItem){
				str += " x"+((StockItem) i).getQnt();
				selectedQnt = ((StockItem) i).getQnt();
			} else {
				if(!str.contains("*"))
					str += " x"+1;
				else
					str+=" x"+this.fv.MAX_SERVIS_QNT;
			}
		}
		return str;
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