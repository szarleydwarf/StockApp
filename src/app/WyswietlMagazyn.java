package app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import dbase.DatabaseManager;
import hct_speciale.Item;
import hct_speciale.StockItem;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import javax.swing.JComboBox;

public class WyswietlMagazyn {

	private JFrame frame;
	
	private static String loggerFolderPath;
	private static String date;
	
	private static Helper helper;
	private DatabaseManager DM;	
	private JTextField tfSearch;
	private FinalVariables fv;
	private static Logger log;
	private JTextField tfQnt4Invoice;
	private String lblQntLabel = "Dostepnych ";
	protected String lblQntText = "Dostepnych ";
	private JComboBox<String> sortComboBox;
	private String stockSortBy="item_name";
	private String servicesSortBy="service_name";
	
	private JButton btnAddToInvoice;
	private	boolean isItem = false;
	private JButton btnDelete;
	private JButton btnEdit;

	private JTable table;

	private TableRowSorter rowSorter;

	private ArrayList<Item> wholeList;

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
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WyswietlMagazyn() {
		DM = new DatabaseManager(loggerFolderPath);
		this.fv = new FinalVariables();
		DecimalFormat df;
		df = new DecimalFormat(this.fv.DECIMAL_FORMAT); 
	
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(255, 255, 0));
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 804, 600);
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
				helper.toggleJButton(btnAddToInvoice, Color.gray, Color.darkGray, false);
				helper.toggleJButton(btnDelete, Color.gray, Color.darkGray, false);
				helper.toggleJButton(btnEdit, Color.gray, Color.darkGray, false);
				getWholeStock();
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
		
		
		tfSearch = new JTextField();
		tfSearch.setHorizontalAlignment(SwingConstants.CENTER);
		tfSearch.setText(this.fv.SEARCH_TEXT_FIELD_FRAZE );
		tfSearch.setBounds(130, 45, 195, 24);
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
		
		tfSearch.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
		
		btnEdit = new JButton("Edytuj zaznaczone");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editRecordInDatabase();
			}
		});
		btnEdit.setForeground(new Color(255, 255, 255));
		btnEdit.setBackground(Color.GRAY);
		btnEdit.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnEdit.setBounds(624, 44, 124, 24);
		btnEdit.setEnabled(false);
		frame.getContentPane().add(btnEdit);
		
		btnDelete = new JButton("Usuń");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteRocordFromDatabase();
			}
		});
		btnDelete.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		btnDelete.setBounds(625, 490, 71, 20);
		btnDelete.setEnabled(false);
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
		btnBack.setBounds(609, 515, 100, 23);
		frame.getContentPane().add(btnBack);
		
		btnAddToInvoice = new JButton("do Rachunku");
		btnAddToInvoice.setEnabled(false);
		btnAddToInvoice.setBounds(615, 102, 94, 23);
		btnAddToInvoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToInvoice();
			}
		});
		frame.getContentPane().add(btnAddToInvoice);
		
		tfQnt4Invoice = new JTextField();
		tfQnt4Invoice.setBounds(544, 103, 60, 20);
		frame.getContentPane().add(tfQnt4Invoice);
		tfQnt4Invoice.setColumns(10);
		
		JLabel lblQnt2Invoice = new JLabel(lblQntLabel );
		lblQnt2Invoice.setForeground(Color.YELLOW);
		lblQnt2Invoice.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		lblQnt2Invoice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQnt2Invoice.setBounds(240, 106, 300, 14);
		frame.getContentPane().add(lblQnt2Invoice);

		getWholeStock();	
	}

	private ListSelectionListener createTableListener() {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				helper.toggleJButton(btnAddToInvoice, Color.green, Color.darkGray, true);
				helper.toggleJButton(btnDelete, Color.red, Color.gray, true);
				helper.toggleJButton(btnEdit, Color.yellow, Color.gray, true);	
			}
	    };
	    return listener;
	}

	private void getWholeStock() {
		String query = "SELECT * from "+this.fv.STOCK_TABLE+" ORDER BY "+stockSortBy+" ASC";//item_name, cost, price,quantity - this.fv.STOCK_TABLE_ITEM_NAME
		ArrayList<Item> listOfItems = new ArrayList<Item>();
		listOfItems = DM.getItemsList(query);
		
		String queryServices = "SELECT * from "+this.fv.SERVICES_TABLE+" ORDER BY "+servicesSortBy+" ASC";//item_name, cost, price,quantity
		ArrayList<Item> listOfServices = new ArrayList<Item>();
		listOfServices = DM.getItemsList(queryServices);

		int rowNumber = listOfItems.size() + listOfServices.size();

		String[][] data = new String [rowNumber][this.fv.STOCK_TB_HEADINGS.length];
		data = populateDataArray(listOfItems, data, 0, listOfItems.size());
		data = populateDataArray(listOfServices, data, listOfItems.size(), rowNumber);
		wholeList = new ArrayList<Item>();
		wholeList.addAll(listOfItems);
		wholeList.addAll(listOfServices);

		createTable(data);	
	}
	
	private String[][] populateDataArray(ArrayList<Item> list, String[][] data, int startIndex, int rowNumber){
		int j = 0;
		for(int i = startIndex; i < rowNumber; i++) {
			data[i][0] = list.get(j).getName();
			data[i][1] = ""+list.get(j).getCost();
			data[i][2] = ""+list.get(j).getPrice();
			if(list.get(j) instanceof StockItem)
				data[i][3] = ""+((StockItem) list.get(j)).getQnt();
			else
				data[i][3] = ""+0;
			j++;
		}		
		return data;
	}
	
	private void createTable(String[][] data){
		ListSelectionListener listener = createTableListener();
		DefaultTableModel dm = new DefaultTableModel(data, this.fv.STOCK_TB_HEADINGS);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getSelectionModel().addListSelectionListener(listener);
		table.setModel(dm);
		
		table.setBounds(42, 87, 560, 288);
		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(320);
		
		rowSorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rowSorter);
		
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.black);
		header.setForeground(Color.yellow);
		
	      
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(21, 138, 566, 400);
		frame.getContentPane().add(scrollPane);
	}

	private Item getSelectedItem () {
		int row = table.getSelectedRow();

		if(row > -1){
			int j = 0;
			for(Item ih : wholeList){
				if(ih.getName().equals(table.getValueAt(row, 0).toString())){
					if(ih instanceof StockItem)
						return (StockItem)ih;
					else
						return ih;
				}
			}
		}
		return null;
	}
	
	private String selectedItem2String (Item i) {
		String itemString = "";
		itemString = i.getName()+" €"+i.getPrice();
		if(i instanceof StockItem)
			itemString +=  " x"+((StockItem) i).getQnt();
		else{
			if(!itemString.contains("*"))
				itemString += " x"+1;
			else
				itemString+=" x"+fv.MAX_SERVIS_QNT;
		}
		return itemString;
	}
	
	protected void editRecordInDatabase() {
		Item i = getSelectedItem();
		if(i != null){
			frame.dispose();
			EdytujTowar.main(i, this.loggerFolderPath);
		}else
			JOptionPane.showMessageDialog(frame, this.fv.WINDOW_ERROR);

		
	}

	protected void deleteRocordFromDatabase() {
		Item i = getSelectedItem();
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

	protected void addToInvoice() {
		Item i = getSelectedItem();
		String itemForList;
		if(i != null)
			itemForList = this.selectedItem2String(i);
		else
			return;
		
		if(itemForList != ""){
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
		} else {
			if(isItem) {
				if(qntInList == 0){
					JOptionPane.showMessageDialog(this.frame, "Dostępnych "+qntInList+"szt.");
					helper.toggleJButton(btnAddToInvoice, Color.gray, Color.darkGray, false);
					return "";
				}
			}
			str = itemForList;
		}
		return str;
	}

}