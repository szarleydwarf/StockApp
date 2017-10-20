package app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dbase.DatabaseManager;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import utillity.StockPrinter;

public class WystawRachunek {

	private JFrame frmNowyRachunek;
	private JTextField textFieldProdQ;
	private JTextField textFieldServQ;private JTextField textFieldDiscount;
	private JList<String> listChosen, listItems, listServices;
	private JRadioButton rbPercent, rbMoney;
	private JTextField textFieldRegistration;
	private JLabel lblTotal;
	
	private DefaultListModel<String> model2Add;
	private StockPrinter sPrinter;
	private ButtonGroup radioGroup;
	
	private String carManufacturer, registration, servicePrice, productPrice ;
	private final String lblTotalSt = "TOTAL";
	private int paddingLength = 22, invoiceNum = 0;
	private double discount = 0;
	private boolean applyDiscount = true;
	private boolean printed = false;
	private DecimalFormat df;
	
	private DatabaseManager DM;	
	private static FinalVariables fv;
	private String lblCarManufacturerTxt = "Car manufacturer";
	private ArrayList<String> defaultPaths;
	
	protected static String date;
	protected static String loggerFolderPath;
	private static Logger log;
	private static Helper helper;

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
				date = helper.getFormatedDate();
				try {
					WystawRachunek window = new WystawRachunek(defaultPaths);
					window.frmNowyRachunek.setVisible(true);
				} catch (Exception e) {
					log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param defaultPaths 
	 * @throws SQLException 
	 */
	public WystawRachunek(ArrayList<String> defaultPaths) throws SQLException {
		DM = new DatabaseManager(loggerFolderPath);
		helper = new Helper();
		fv = new FinalVariables();
		//TODO: add check for arraylist null?
		this.defaultPaths = new ArrayList<String>();
		this.defaultPaths = defaultPaths;
		
		invoiceNum = DM.getLastInvoiceNumber();
		if(invoiceNum == 0)
			invoiceNum++;

		try {
			initialize();
		} catch (Exception e) {
			log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {		
		df = new DecimalFormat("#.##"); 
		frmNowyRachunek = new JFrame();
		
		frmNowyRachunek.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frmNowyRachunek.setTitle("Nowy Rachunek - HCT");
		frmNowyRachunek.setBounds(100, 100, 769, 606);
		frmNowyRachunek.getContentPane().setLayout(null);
		
		JScrollPane scrollPaneChosen = new JScrollPane();
		scrollPaneChosen.setBounds(450, 172, 225, 178);
		frmNowyRachunek.getContentPane().add(scrollPaneChosen);
		
		listChosen = new JList<String>();
		listServices = new JList<String>();
		listItems = new JList<String>();
		model2Add = new DefaultListModel<>();

		scrollPaneChosen.setViewportView(listChosen);
		
		textFieldProdQ = new JTextField();
		textFieldProdQ.setColumns(10);
		textFieldProdQ.setBounds(325, 267, 44, 32);
		frmNowyRachunek.getContentPane().add(textFieldProdQ);
		
		textFieldServQ = new JTextField();
		textFieldServQ.setColumns(10);
		textFieldServQ.setBounds(325, 160, 44, 32);
		frmNowyRachunek.getContentPane().add(textFieldServQ);
		
		JLabel labelSaleList = new JLabel("Us\u0142uga / Towar");
		labelSaleList.setHorizontalAlignment(SwingConstants.CENTER);
		labelSaleList.setBounds(450, 152, 96, 19);
		frmNowyRachunek.getContentPane().add(labelSaleList);
		
		JLabel lblWystawRachunek = new JLabel("Wybierz us\u0142ugi/produkty");
		lblWystawRachunek.setBounds(70, 122, 194, 19);
		lblWystawRachunek.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		frmNowyRachunek.getContentPane().add(lblWystawRachunek);
		
		
		JLabel lblWybrane = new JLabel("");
		Border b = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border = BorderFactory.createTitledBorder(b, "WYBRANE");
		lblWybrane.setBorder(border);
		lblWybrane.setVerticalAlignment(SwingConstants.TOP);
		lblWybrane.setBounds(439, 123, 248, 239);
		frmNowyRachunek.getContentPane().add(lblWybrane);

		
		textFieldDiscount = new JTextField();
		textFieldDiscount.setBounds(239, 390, 77, 32);
		frmNowyRachunek.getContentPane().add(textFieldDiscount);
		textFieldDiscount.setColumns(10);
		
		JLabel labelZnizka = new JLabel("Znizka");
		labelZnizka.setBounds(23, 388, 109, 36);
		frmNowyRachunek.getContentPane().add(labelZnizka);
		
		rbPercent = new JRadioButton("%", false);
		rbPercent.setBounds(134, 388, 37, 23);
		frmNowyRachunek.getContentPane().add(rbPercent);
		
		rbMoney = new JRadioButton("\u20AC", true);
		rbMoney.setBounds(173, 388, 37, 23);
		frmNowyRachunek.getContentPane().add(rbMoney);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(rbMoney);
		radioGroup.add(rbPercent);
		
		rbMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyDiscount = true;
			}
		});
		rbPercent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyDiscount = false;
			}
		});
		
		JLabel lblCena = new JLabel("Cena");
		lblCena.setHorizontalAlignment(SwingConstants.CENTER);
		lblCena.setBounds(585, 152, 50, 19);
		frmNowyRachunek.getContentPane().add(lblCena);
				
		JButton btnPrint = new JButton("Drukuj rachunek");
		btnPrint.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				sPrinter = new StockPrinter(defaultPaths);
				try {
					invoiceNum += 1;
					registration = textFieldRegistration.getText();
					if(!lblTotal.getText().equals(lblTotalSt)) {
						printed = sPrinter.printDoc(listChosen, discount, applyDiscount, carManufacturer, registration, invoiceNum);
						if(printed){
							frmNowyRachunek.dispose();
							MainView.main(null);
						}
					} else
						JOptionPane.showMessageDialog(frmNowyRachunek, "Nie przeliczyles wyniku");
				} catch (IOException e) {
					log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
				} catch (Exception e) {
					log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
				}
			}
		});
		btnPrint.setForeground(Color.BLACK);
		btnPrint.setBackground(new Color(178, 34, 34));
		btnPrint.setBounds(439, 457, 248, 32);
		frmNowyRachunek.getContentPane().add(btnPrint);
		

		JButton btnSubb = new JButton("-");
		btnSubb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!listChosen.isSelectionEmpty()) {
					 DefaultListModel md = (DefaultListModel) listChosen.getModel();
					 int selectedIndex = listChosen.getSelectedIndex();
					 if(selectedIndex != -1) {
						 md.remove(selectedIndex);
					 }
				}
			}
		});
		btnSubb.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		btnSubb.setBounds(687, 144, 50, 24);
		frmNowyRachunek.getContentPane().add(btnSubb);
		
		JButton btnClearAll = new JButton("Clear All");
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 DefaultListModel md = (DefaultListModel) listChosen.getModel();
					md.removeAllElements();
			}
		});
		btnClearAll.setBackground(new Color(255, 182, 193));
		btnClearAll.setForeground(Color.RED);
		btnClearAll.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnClearAll.setBounds(628, 373, 109, 24);
		frmNowyRachunek.getContentPane().add(btnClearAll);
		
		JLabel lblQty = new JLabel("Qty");
		lblQty.setHorizontalAlignment(SwingConstants.CENTER);
		lblQty.setBounds(628, 152, 37, 19);
		frmNowyRachunek.getContentPane().add(lblQty);
		
		textFieldRegistration = new JTextField();
		textFieldRegistration.setBounds(548, 72, 127, 28);
		frmNowyRachunek.getContentPane().add(textFieldRegistration);
		textFieldRegistration.setColumns(10);
		
		JLabel lblRegistration = new JLabel("Registration");
		lblRegistration.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblRegistration.setBounds(439, 75, 84, 28);
		frmNowyRachunek.getContentPane().add(lblRegistration);
		
		JLabel labelQuantity = new JLabel("Quantity");
		labelQuantity.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		labelQuantity.setBounds(319, 122, 61, 19);
		frmNowyRachunek.getContentPane().add(labelQuantity);
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmNowyRachunek.dispose();
				MainView.main(null);
			}
		});
		btnBack.setBounds(648, 515, 89, 23);
		frmNowyRachunek.getContentPane().add(btnBack);
		
		populateServices();
		populateItems();
		populateCarList();

		sumCosts();
		
		frmNowyRachunek.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frmNowyRachunek, 
		        		fv.CLOSE_WINDOW, fv.CLOSE_WINDOW,  
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frmNowyRachunek.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        	MainView.main(null);
		        }
		    }
		});
	}
	

	private void sumCosts() {
		JButton btnCalculate = new JButton("Policz = ");
		
		lblTotal = new JLabel(lblTotalSt);
		lblTotal.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotal.setBounds(470, 403, 205, 32);
		frmNowyRachunek.getContentPane().add(lblTotal);

		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double sos = 0;
				if(listChosen.getModel().getSize() != 0){
					DefaultListModel md = (DefaultListModel) listChosen.getModel();
					if(!textFieldDiscount.getText().equals(""))
						discount = Double.parseDouble(textFieldDiscount.getText());
					
					sos = helper.getSum(md, discount, applyDiscount);
				}else{
					sos = 0;
				}
				lblTotal.setText("€ "+df.format(sos));
			}
		});
		btnCalculate.setForeground(new Color(0, 0, 0));
		btnCalculate.setBackground(new Color(220, 20, 60));
		btnCalculate.setBounds(336, 403, 120, 32);
		frmNowyRachunek.getContentPane().add(btnCalculate);
	}

	private void populateCarList() throws Exception {
		String queryCars = "SELECT "+this.fv.MANUFACTURER_TABLE_NAME+" FROM "+this.fv.MANUFACTURER_LIST_TABLE+" ORDER BY "+this.fv.MANUFACTURER_TABLE_NAME+" ASC";
		carManufacturer = "Car manufacturer";
		DefaultListModel<String> modelCars = new DefaultListModel<>();
	
		ArrayList<String> listOfCars = DM.selectRecordArrayList(queryCars);
		
		for(int i = 0; i < listOfCars.size(); i+=2) {
			String tempString = listOfCars.get(i);
			modelCars.addElement(tempString);
//			System.out.println("ST: "+alist.get(i));
		}
		JLabel labelClient = new JLabel("Klient");
		labelClient.setBounds(23, 11, 109, 36);
		frmNowyRachunek.getContentPane().add(labelClient);
		
		JScrollPane scrollPaneCarList = new JScrollPane();
		scrollPaneCarList.setBounds(164, 11, 194, 100);
		frmNowyRachunek.getContentPane().add(scrollPaneCarList);

		JLabel lblCarManufacturer = new JLabel(lblCarManufacturerTxt );
		lblCarManufacturer.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblCarManufacturer.setHorizontalAlignment(SwingConstants.CENTER);
		lblCarManufacturer.setBounds(439, 16, 242, 45);
	
		frmNowyRachunek.getContentPane().add(lblCarManufacturer);
			
		JList listCars = new JList();
		listCars.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listCars.setModel(modelCars);
		scrollPaneCarList.setViewportView(listCars);
		
		listCars.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if(!listCars.isSelectionEmpty()){
					lblCarManufacturer.setText((String) listCars.getSelectedValue());
					carManufacturer = (String) listCars.getSelectedValue();
				} else {
					lblCarManufacturer.setText(lblCarManufacturerTxt);
				}
			}
			
		});		
		

	}

	private void populateItems() throws Exception {
		String queryItems = "SELECT "+this.fv.STOCK_TABLE_ITEM_NAME+", "+this.fv.STOCK_TABLE_PRICE+" FROM "+this.fv.STOCK_TABLE+"";
		DefaultListModel<String> modelItems = new DefaultListModel<>();
	
		ArrayList<String> listOfItems = DM.selectRecordArrayList(queryItems);
		
		for(int i = 0; i < listOfItems.size(); i+=2) {
			String tempString = listOfItems.get(i);
			modelItems.addElement(tempString);
		}
		JLabel lblWybierzPrzedmiot = new JLabel("Wybierz produkt");
		lblWybierzPrzedmiot.setBounds(23, 259, 109, 36);
		frmNowyRachunek.getContentPane().add(lblWybierzPrzedmiot);
		
		JScrollPane scrollPaneItemList = new JScrollPane();
		scrollPaneItemList.setBounds(123, 259, 194, 120);
		frmNowyRachunek.getContentPane().add(scrollPaneItemList);
		
		scrollPaneItemList.setViewportView(listItems);
		listItems.setModel(modelItems);
		
		JButton btnAddItem = new JButton("+");
		btnAddItem.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!listItems.isSelectionEmpty()) {
					char ch = ' ';
					String element2model = (String) listItems.getSelectedValue();
					
					int index = listOfItems.indexOf(element2model);
					String tempString = listOfItems.get(index+1);
					productPrice = tempString;
					element2model = helper.paddStringRight(element2model, paddingLength, ch);
					
					element2model += " €"+tempString;
					
					String productQuantity = textFieldProdQ.getText(); 
					element2model+="x"+productQuantity;
					
					model2Add.addElement(element2model);
					listChosen.setModel(model2Add);}
			}
		});
		btnAddItem.setBounds(379, 264, 50, 24);
		frmNowyRachunek.getContentPane().add(btnAddItem);
	}

	private void populateServices() throws Exception {
		String queryServices = "SELECT "+this.fv.SERVICES_TABLE_SERVICE_NAME+", "+this.fv.STOCK_TABLE_PRICE+" FROM "+this.fv.SERVICES_TABLE+"";
		DefaultListModel<String> model = new DefaultListModel<>();
	
		ArrayList<String> listOfServices = DM.selectRecordArrayList(queryServices);
		
		for(int i = 0; i < listOfServices.size(); i+=2) {
			String tempString = listOfServices.get(i);
			model.addElement(tempString);
		}
				
		JLabel lblChoseServiceitem = new JLabel("Wybierz us\u0142ug\u0119");
		lblChoseServiceitem.setBounds(23, 152, 109, 36);
		frmNowyRachunek.getContentPane().add(lblChoseServiceitem);
		
		JScrollPane scrollPaneServiceList = new JScrollPane();
		scrollPaneServiceList.setBounds(123, 152, 194, 96);
		frmNowyRachunek.getContentPane().add(scrollPaneServiceList);
		
		scrollPaneServiceList.setViewportView(listServices);
		listServices.setModel(model);
		
		JButton btnAddService = new JButton("+");
		btnAddService.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));

		
		btnAddService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!listServices.isSelectionEmpty()) {
					char ch = ' ';
					String element2model = (String) listServices.getSelectedValue();
					
					int index = listOfServices.indexOf(element2model);
					String tempString = listOfServices.get(index+1);
					servicePrice = tempString;
					element2model = helper.paddStringRight(element2model, paddingLength, ch);
					
					element2model += " €"+tempString;
					
					String productQuantity = textFieldServQ.getText(); 
					element2model+="x"+productQuantity;
					
					model2Add.addElement(element2model);
					listChosen.setModel(model2Add);
				}
			}
		});
		btnAddService.setBounds(379, 157, 50, 24);
		frmNowyRachunek.getContentPane().add(btnAddService);
	}
}
