package app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	private JScrollPane scrollPaneItemList;
	private JTextField tfOtherQnt1;
	private JTextField tfOtherPrice1;
	private JTextField tfSearchBox;
	private JTextField tfOther1;
	private DefaultListModel<String> model2Add;
	
	private StockPrinter sPrinter;
	private ButtonGroup radioGroup;
	private DatabaseManager DM;	
	private static FinalVariables fv;
	private static Logger log;
	private static Helper helper;
	private DecimalFormat df;
	
	protected static String date;
	protected static String loggerFolderPath;
	
	private String lblCarManufacturerTxt = "Car manufacturer";
	private String stockSearchText="";
	private String carManufacturer, registration, servicePrice, productPrice ;
	private final String lblTotalSt = "TOTAL";
	private int paddingLength = 22, invoiceNum = 0;
	private double discount = 0;
	private boolean applyDiscount = true;
	private boolean printed = false;
	private char ch = ' ';

	private ArrayList<String> defaultPaths;
	private Map<String, Integer> nameQnt;


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
		this.nameQnt = new HashMap<String, Integer>();
		
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
		frmNowyRachunek.setBounds(100, 100, 982, 606);
		frmNowyRachunek.getContentPane().setLayout(null);
		
		JScrollPane scrollPaneChosen = new JScrollPane();
		scrollPaneChosen.setBounds(578, 171, 318, 234);
		frmNowyRachunek.getContentPane().add(scrollPaneChosen);
		
		listChosen = new JList<String>();
		listServices = new JList<String>();
		listItems = new JList<String>();
		model2Add = new DefaultListModel<>();

		scrollPaneChosen.setViewportView(listChosen);
		
		textFieldProdQ = new JTextField();
		textFieldProdQ.setColumns(10);
		textFieldProdQ.setBounds(453, 317, 44, 32);
		frmNowyRachunek.getContentPane().add(textFieldProdQ);
		
		textFieldServQ = new JTextField();
		textFieldServQ.setColumns(10);
		textFieldServQ.setBounds(453, 159, 44, 32);
		frmNowyRachunek.getContentPane().add(textFieldServQ);
		
		JLabel labelSaleList = new JLabel("Us\u0142uga / Towar");
		labelSaleList.setHorizontalAlignment(SwingConstants.CENTER);
		labelSaleList.setBounds(578, 151, 96, 19);
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
		lblWybrane.setBounds(567, 122, 339, 292);
		frmNowyRachunek.getContentPane().add(lblWybrane);

		
		textFieldDiscount = new JTextField();
		textFieldDiscount.setBounds(515, 382, 50, 23);
		frmNowyRachunek.getContentPane().add(textFieldDiscount);
		textFieldDiscount.setColumns(10);
		
		JLabel labelZnizka = new JLabel("Znizka");
		labelZnizka.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		labelZnizka.setBounds(476, 360, 50, 23);
		frmNowyRachunek.getContentPane().add(labelZnizka);
		
		rbPercent = new JRadioButton("%", false);
		rbPercent.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		rbPercent.setBounds(433, 382, 37, 23);
		frmNowyRachunek.getContentPane().add(rbPercent);
		
		rbMoney = new JRadioButton("\u20AC", true);
		rbMoney.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		rbMoney.setBounds(472, 382, 37, 23);
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
		lblCena.setBounds(787, 151, 50, 19);
		frmNowyRachunek.getContentPane().add(lblCena);
				
		JButton btnPrint = new JButton("Drukuj rachunek");
		btnPrint.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnPrint.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				sPrinter = new StockPrinter(defaultPaths);
				try {
					invoiceNum += 1;
					registration = textFieldRegistration.getText();
					if(!lblTotal.getText().equals(lblTotalSt)) {
						printed = sPrinter.printDoc(listChosen, discount, applyDiscount, carManufacturer, registration, invoiceNum);
						if(printed){
							for (String key : nameQnt.keySet()){
								String query = "UPDATE \""+fv.STOCK_TABLE+"\" SET "+fv.STOCK_TABLE_QNT+"='"+nameQnt.get(key)+"'" + " WHERE " + fv.STOCK_TABLE_ITEM_NAME + "='"+key+"'";
//								System.out.println("Query: "+query);
								DM.editRecord(query);
							}
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
		btnPrint.setBounds(578, 505, 248, 32);
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
		btnSubb.setBounds(906, 165, 50, 24);
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
		btnClearAll.setBounds(847, 93, 109, 24);
		frmNowyRachunek.getContentPane().add(btnClearAll);
		
		JLabel lblQty = new JLabel("Qty");
		lblQty.setHorizontalAlignment(SwingConstants.CENTER);
		lblQty.setBounds(847, 151, 37, 19);
		frmNowyRachunek.getContentPane().add(lblQty);
		
		textFieldRegistration = new JTextField();
		textFieldRegistration.setBounds(671, 53, 127, 28);
		frmNowyRachunek.getContentPane().add(textFieldRegistration);
		textFieldRegistration.setColumns(10);
		
		JLabel lblWybierzPrzedmiot = new JLabel("Wybierz produkt");
		lblWybierzPrzedmiot.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblWybierzPrzedmiot.setBounds(10, 313, 109, 36);
		frmNowyRachunek.getContentPane().add(lblWybierzPrzedmiot);
		
		tfSearchBox = new JTextField();
		tfSearchBox.setBounds(123, 294, 300, 20);
		frmNowyRachunek.getContentPane().add(tfSearchBox);
		tfSearchBox.setColumns(10);
		
		tfOther1 = new JTextField();
		tfOther1.setText("Inny produkt / usługa");
		tfOther1.setBounds(123, 470, 303, 20);
		frmNowyRachunek.getContentPane().add(tfOther1);
		tfOther1.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfOther1.setText(fv.OTHER_STRING_CHECKUP);
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		tfOther1.setColumns(10);
		
		tfOtherPrice1 = new JTextField();
		tfOtherPrice1.setColumns(10);
		tfOtherPrice1.setBounds(430, 470, 34, 20);
		frmNowyRachunek.getContentPane().add(tfOtherPrice1);
		
		tfOtherQnt1 = new JTextField();
		tfOtherQnt1.setColumns(10);
		tfOtherQnt1.setBounds(466, 470, 34, 20);
		frmNowyRachunek.getContentPane().add(tfOtherQnt1);
		
		JButton btnOtherAdd1 = new JButton("+");
		btnOtherAdd1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!tfOther1.getText().isEmpty() && !tfOtherPrice1.getText().isEmpty())
					addItemToList(tfOther1,tfOtherPrice1,tfOtherQnt1);
				else
					JOptionPane.showMessageDialog(frmNowyRachunek, "Opis usługi oraz cena nie mogą być puste.");
			}
		});
		btnOtherAdd1.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnOtherAdd1.setBounds(500, 470, 50, 20);
		frmNowyRachunek.getContentPane().add(btnOtherAdd1);
		
		tfSearchBox.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
		        try {
					searchInList();
				} catch (Exception e1) {
					log.logError("E1: "+this.getClass().getName()+"\t"+e1.getMessage());
				}
	        }
	      });
		
		scrollPaneItemList = new JScrollPane();
		scrollPaneItemList.setBounds(123, 314, 300, 120);
		frmNowyRachunek.getContentPane().add(scrollPaneItemList);

		JLabel lblRegistration = new JLabel("Registration");
		lblRegistration.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblRegistration.setBounds(671, 14, 84, 28);
		frmNowyRachunek.getContentPane().add(lblRegistration);
		
		JLabel labelQuantity = new JLabel("Quantity");
		labelQuantity.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		labelQuantity.setBounds(453, 122, 61, 19);
		frmNowyRachunek.getContentPane().add(labelQuantity);
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmNowyRachunek.dispose();
				MainView.main(null);
			}
		});
		btnBack.setBounds(847, 514, 89, 23);
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
	

	private void addItemToList(JTextField tfOther, JTextField tfOtherPrice, JTextField tfOtherQnt) {
		String element2model =	tfOther.getText();
		
		element2model = helper.paddStringRight(element2model, paddingLength, ch);
		
		double dPrice = 0;
		while(dPrice == 0.0) {
			dPrice = helper.checkDouble("Wpisz cene", "Niepoprawny format ceny", tfOtherPrice.getText());

			if(dPrice == 0)
				return;
		}

		element2model += " €" + tfOtherPrice.getText();
		element2model += "x" + tfOtherQnt.getText();
		
		model2Add.addElement(element2model);
		listChosen.setModel(model2Add);
	}

	private void sumCosts() {
		JButton btnCalculate = new JButton("Policz = ");
		btnCalculate.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		
		lblTotal = new JLabel(lblTotalSt);
		lblTotal.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotal.setBounds(685, 456, 205, 32);
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
		btnCalculate.setBounds(578, 456, 96, 32);
		frmNowyRachunek.getContentPane().add(btnCalculate);
		
		JLabel lblDescription = new JLabel("Opis usługi/przedmiotu");
		lblDescription.setBounds(123, 456, 127, 14);
		frmNowyRachunek.getContentPane().add(lblDescription);
		
		JLabel lblNewLabel = new JLabel("Cena");
		lblNewLabel.setBounds(430, 456, 28, 14);
		frmNowyRachunek.getContentPane().add(lblNewLabel);
		
		JLabel lblQnt = new JLabel("Qnt");
		lblQnt.setBounds(466, 456, 28, 14);
		frmNowyRachunek.getContentPane().add(lblQnt);
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
		labelClient.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		labelClient.setBounds(71, 11, 61, 36);
		frmNowyRachunek.getContentPane().add(labelClient);
		
		JScrollPane scrollPaneCarList = new JScrollPane();
		scrollPaneCarList.setBounds(164, 11, 194, 100);
		frmNowyRachunek.getContentPane().add(scrollPaneCarList);

		JLabel lblCarManufacturer = new JLabel(lblCarManufacturerTxt );
		lblCarManufacturer.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblCarManufacturer.setHorizontalAlignment(SwingConstants.CENTER);
		lblCarManufacturer.setBounds(388, 40, 242, 45);
	
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
		String queryItems = "SELECT "+this.fv.STOCK_TABLE_ITEM_NAME+", "+this.fv.STOCK_TABLE_PRICE+", "+this.fv.STOCK_TABLE_QNT+" FROM "+this.fv.STOCK_TABLE+"";
		if(!this.stockSearchText.isEmpty()){
			queryItems +=" WHERE "+this.fv.STOCK_TABLE_ITEM_NAME+" LIKE '%"+this.stockSearchText+"%'";
		}
		DefaultListModel<String> modelItems = new DefaultListModel<>();
	
		ArrayList<String> listOfItems = DM.selectRecordArrayList(queryItems);
		
		for(int i = 0; i < listOfItems.size(); i+=3) {
			String tempString = listOfItems.get(i);
			modelItems.addElement(tempString);
		}
		
		scrollPaneItemList.setViewportView(listItems);
		listItems.setModel(modelItems);
		
		JButton btnAddItem = new JButton("+");
		btnAddItem.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!listItems.isSelectionEmpty()) {
					
					String element2model = (String) listItems.getSelectedValue();
					String itemName = element2model;
					
					int index = listOfItems.indexOf(element2model);
					String tempString = listOfItems.get(index+1);
					productPrice = tempString;
					element2model = helper.paddStringRight(element2model, paddingLength, ch);
					
					element2model += " €"+tempString;
					
					String productQuantity = textFieldProdQ.getText(); 
					System.out.println(listOfItems.get(index+2));
					element2model+="x";
					int qnt = 0, qntForDatabase = 0;
					int qntInList = Integer.parseInt(listOfItems.get(index+2));
					if(!productQuantity.isEmpty())
						qnt = Integer.parseInt(productQuantity);
					
					while(qnt > qntInList){
						JOptionPane.showMessageDialog(frmNowyRachunek, "Dostępnych "+qntInList+"szt.");
						if(qnt > qntInList)
							return;
					}
					qntForDatabase = qntInList - qnt;
					nameQnt.put(itemName, qntForDatabase);
					element2model+=productQuantity;

					
					model2Add.addElement(element2model);
					listChosen.setModel(model2Add);
				}
			}
		});
		btnAddItem.setBounds(507, 314, 50, 24);
		frmNowyRachunek.getContentPane().add(btnAddItem);
	}

	protected void searchInList() throws Exception {
		stockSearchText = this.tfSearchBox.getText();
//        System.out.println("Text in method=" + stockSearchText);
        this.populateItems();
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
		lblChoseServiceitem.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblChoseServiceitem.setBounds(10, 151, 109, 36);
		frmNowyRachunek.getContentPane().add(lblChoseServiceitem);
		
		JScrollPane scrollPaneServiceList = new JScrollPane();
		scrollPaneServiceList.setBounds(123, 152, 300, 96);
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
		btnAddService.setBounds(507, 156, 50, 24);
		frmNowyRachunek.getContentPane().add(btnAddService);
	}
}