package app;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import dbase.DatabaseManager;
import hct_speciale.Invoice;
import hct_speciale.Item;
import hct_speciale.StockItem;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;

public class WyswietlRachunki {

	private JFrame frame;
	private JTextField tfSearch;	
	
	private FinalVariables fv;
	private DatabaseManager DM;
	private ArrayList<Invoice> listOfInvoices;
	private JTable table;

	protected static String date;
	protected static String loggerFolderPath;
	private static Logger log;
	private static Helper helper;

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
					WyswietlRachunki window = new WyswietlRachunki();
					window.frame.setVisible(true);
				} catch (Exception e) {
					log.logError(date+"## "+this.getClass().getName()+"\t"+e.getMessage()+"\n\t"+e.toString());
					e.printStackTrace();}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WyswietlRachunki() {
		this.fv = new FinalVariables();
		this.DM = new DatabaseManager(loggerFolderPath);
		
		this.listOfInvoices = new ArrayList<Invoice>();
		
		initialize();
		this.populateList();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		
		frame.setBackground(new Color(255, 255, 0));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(10, 10, 1220, 700);
//		frame.setLocation(10, 10);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Lista wystawionych rachunkow");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 24));
		lblNewLabel.setBounds(10, 11, 724, 43);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		btnBack.setBounds(1105, 627, 89, 23);
		frame.getContentPane().add(btnBack);

		Border b = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border = BorderFactory.createTitledBorder(b, "LISTA WYSTAWIONYCH RACHUNKÓW");
		
		JLabel label = new JLabel("");
		label.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		label.setBorder(border);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setBounds(10, 62, 1106, 554);
		frame.getContentPane().add(label);
		
			
		JButton btnRefresh = new JButton("Odśwież");
		btnRefresh.setForeground(new Color(0, 153, 255));
		btnRefresh.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnRefresh.setBackground(new Color(255, 255, 153));
		btnRefresh.setBounds(23, 80, 89, 23);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				populateList();
			}
		});
		
		frame.getContentPane().add(btnRefresh);
		
		tfSearch = new JTextField();
		tfSearch.setText("wpisz szukaną nazwę");
		tfSearch.setHorizontalAlignment(SwingConstants.CENTER);
		tfSearch.setColumns(10);
		tfSearch.setBounds(121, 79, 288, 24);
		frame.getContentPane().add(tfSearch);
		
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
		btnSearch.setForeground(new Color(255, 255, 204));
		btnSearch.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnSearch.setBackground(new Color(0, 153, 255));
		btnSearch.setBounds(408, 79, 89, 24);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchInDatabase();
			}
		});
		frame.getContentPane().add(btnSearch);
		/*
		JButton btnDelete = new JButton("Usuń");
		btnDelete.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		btnDelete.setBounds(972, 80, 71, 20);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		frame.getContentPane().add(btnDelete);
		*/
		JButton btnPrintOne = new JButton("Drukuj");
		btnPrintOne.setBackground(Color.LIGHT_GRAY);
		btnPrintOne.setForeground(Color.GRAY);
		btnPrintOne.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		btnPrintOne.setBounds(1123, 64, 71, 23);
		btnPrintOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		frame.getContentPane().add(btnPrintOne);
		
		
/*		this.list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if(!list.isSelectionEmpty()){
					helper.toggleJButton(btnDelete, Color.RED, Color.ORANGE, true);
					helper.toggleJButton(btnPrintOne, Color.BLUE, Color.GREEN, true);
					selectedInvoice = listOfInvoices.get(list.getSelectedIndex());
				} else {
					helper.toggleJButton(btnDelete, Color.DARK_GRAY, Color.lightGray, false);
					helper.toggleJButton(btnPrintOne, Color.DARK_GRAY, Color.lightGray, false);
				}
			}
			
		});
		
	*/	
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

	}

	protected void populateList() {
		String query = "SELECT * from "+this.fv.INVOCE_TABLE+" ORDER BY "+this.fv.INVOCE_TABLE_INVOICE_NUMBER+" ASC";//item_name, cost, price,quantity
//		ArrayList<String> list = new ArrayList<String>();
//		list = this.DM.selectRecordArrayList(query);
		listOfInvoices = DM.getInvoiceList(query);

		String[][] data = new String [listOfInvoices.size()][this.fv.INVOICE_REPORT_TB_HEADINGS.length];
		
		for(int i = 0; i < listOfInvoices.size(); i++){
			data[i][0] = ""+listOfInvoices.get(i).getInvoiceNumber();
			data[i][1] = ""+listOfInvoices.get(i).getCustomerName();
			data[i][2] = ""+listOfInvoices.get(i).getItemNumber();
			data[i][3] = ""+listOfInvoices.get(i).getServiceNumber();
			data[i][4] = ""+listOfInvoices.get(i).getTotal();
			data[i][5] = ""+listOfInvoices.get(i).getInvoiceDateString();
			data[i][6] = ""+listOfInvoices.get(i).getFilePathName();
			
		}

		table = new JTable(data, this.fv.INVOICE_REPORT_TB_HEADINGS){
		    @Override
		       public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		           Component component = super.prepareRenderer(renderer, row, column);
		           int rendererWidth = component.getPreferredSize().width;
		           TableColumn tableColumn = getColumnModel().getColumn(column);
		           tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
		           return component;
		        }
		    };
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setBounds(42, 87, 560, 288);
		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		table.setFillsViewportHeight(true);
		
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.black);
		header.setForeground(Color.yellow);
	      
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(23, 114, 1080, 491);
		frame.getContentPane().add(scrollPane);	
	}

	protected void searchInDatabase() {
		String query = "SELECT * FROM "+this.fv.INVOCE_TABLE+"";
		if(!tfSearch.getText().equals(this.fv.SEARCH_TEXT_FIELD_FRAZE))
			query += " WHERE "+this.fv.INVOCE_TABLE_CUSTOMER_NAME+" LIKE '%"+tfSearch.getText()+"%' ORDER BY "+this.fv.INVOCE_TABLE_DATE+" ASC";
	}

	protected void deleteRocordFromDatabase() {
		// TODO Auto-generated method stub
		
	}
}
