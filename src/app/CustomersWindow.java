package app;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;

import dbase.DatabaseManager;
import hct_speciale.Customer;
import hct_speciale.Invoice;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class CustomersWindow {

	private JFrame frame;
	private DatabaseManager DM;
	private ArrayList<String> defaultPaths;
	protected static String date;
	protected static Helper helper;
	protected static Logger log;
	protected static FinalVariables fv;
	protected static String loggerFolderPath;
	private JTextField tfSearch;
	private JTable table;
	private ArrayList<Customer> listOfCustomers;

	/**
	 * Launch the application.
	 */
	public static void main(ArrayList<String> defaultPaths) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				fv = new FinalVariables();
				loggerFolderPath = defaultPaths.get(0)+"\\"+fv.LOGGER_FOLDER_NAME;
				log = new Logger(loggerFolderPath);
				helper = new Helper();
				date = helper.getFormatedDateAndTime();
				try {
					CustomersWindow window = new CustomersWindow(defaultPaths);
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
	public CustomersWindow(ArrayList<String> defaultPaths) {
		DM = new DatabaseManager(loggerFolderPath);
		this.defaultPaths = new ArrayList<String>();
		this.defaultPaths = defaultPaths;
		this.listOfCustomers = new ArrayList<Customer>();

//		customers = DM.getItemsList("SELECT "+fv.STAR + " FROM " +fv.CUSTOMER_TABLE);

	
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
		frame.setBounds(100, 100, 796, 665);
		frame.setLocation(10, 10);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Klienci");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblTitle.setBounds(10, 11, 645, 26);
		frame.getContentPane().add(lblTitle);
		
		JLabel lblSearchFraze = new JLabel("Wpisz szukan\u0105 fraz\u0119");
		lblSearchFraze.setBackground(new Color(255, 255, 204));
		lblSearchFraze.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		lblSearchFraze.setBounds(10, 42, 197, 14);
		frame.getContentPane().add(lblSearchFraze);

		tfSearch = new JTextField();
		tfSearch.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfSearch.setBounds(10, 57, 197, 20);
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

		/*RADIO BUTTONS*/
		JRadioButton rbWhole = new JRadioButton("Wszyscy", true);
		rbWhole.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		rbWhole.setBounds(646, 114, 121, 24);
		frame.getContentPane().add(rbWhole);
		rbWhole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				TODO display whole list
			}
		});
		
		JRadioButton rbIndividual = new JRadioButton("Indywidualni", false);
		rbIndividual.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		rbIndividual.setBounds(646, 141, 121, 24);
		frame.getContentPane().add(rbIndividual);
		rbIndividual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				TODO display only individual customers list
			}
		});
		
		JRadioButton rbBusiness = new JRadioButton("Biznesowi", false);
		rbBusiness.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		rbBusiness.setBounds(646, 168, 121, 24);
		frame.getContentPane().add(rbBusiness);
		rbBusiness.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				TODO display only business customers list

			}
		});
		
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(rbIndividual);
		radioGroup.add(rbWhole);
		radioGroup.add(rbBusiness);
		/*RADIO BUTTONS END*/

		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		btnBack.setBounds(678, 582, 89, 23);
		frame.getContentPane().add(btnBack);

		Border b = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border = BorderFactory.createTitledBorder(b, "LISTA WYSTAWIONYCH RACHUNKÓW");
		
		JLabel label = new JLabel("");
		label.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		label.setBorder(border);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setBounds(10, 85, 633, 531);
		frame.getContentPane().add(label);
		
		JButton btnEdit = new JButton("Edytuj");
		btnEdit.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnEdit.setBackground(new Color(255, 255, 153));
		btnEdit.setBounds(646, 56, 89, 23);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//TODO
			}
		});
		frame.getContentPane().add(btnEdit);
		
		JButton btnNew = new JButton("Dodaj");
		btnNew.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnNew.setBackground(new Color(255, 255, 153));
		btnNew.setBounds(541, 56, 89, 23);
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//TODO
			}
		});

		frame.getContentPane().add(btnNew);
		
		
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

	private void populateList() {
		listOfCustomers = DM.getCustomerList(fv.CUSTOMER_QUERY);

		String[][] data = new String [listOfCustomers.size()][this.fv.CUSTOMER_TB_HEADINGS.length];
		for(int i = 0; i < listOfCustomers.size(); i++){
			data[i][0] = ""+listOfCustomers.get(i).getId();
			data[i][1] = ""+listOfCustomers.get(i).getCarId();
			data[i][2] = ""+listOfCustomers.get(i).getDetails();
			if(listOfCustomers.get(i).isBusiness())
				data[i][3] = "YES";
			else
				data[i][3] = "NO";
		}


/*
		 * Auto column width taken from link below
		 * https://stackoverflow.com/questions/17858132/automatically-adjust-jtable-column-to-fit-content
		 * */
		table = new JTable(data, this.fv.CUSTOMER_TB_HEADINGS){
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
		scrollPane.setBounds(23, 114, 601, 491);
		frame.getContentPane().add(scrollPane);	
	}
}
