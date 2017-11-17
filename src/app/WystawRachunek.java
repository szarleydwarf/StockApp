package app;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import dbase.DatabaseManager;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import utillity.StockPrinter;
import javax.swing.ScrollPaneConstants;

public class WystawRachunek {

	private JFrame frame;
	private JTextField tfProdQ;
	private JTextField tfServQ;private JTextField textFieldDiscount;
	private JList<String> listChosen, listItems, listServices;
	private JRadioButton rbPercent, rbMoney;
	private JTextField textFieldRegistration;
	private JLabel lblTotal;
	private JScrollPane scrollPaneItemList;
	private JTextField tfOtherQnt1;
	private JTextField tfOtherPrice1;
	private JTextField tfSearchBox;
	private JTextField tfOther1;
	private JTextField tfServicePrice;
	private JTextField tfItemPrice;
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
	
	private String lblCarManufacturerTxt = "CAR";
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
	private JTextField tfCompanyName;
	private JTextField tfCompanyAddress;
	private JTextField tfVATRegNum;
	private String stringAddress = "Adres Firmy";
	private String stringComName = "Nazwa firmy";
	private String stringVATReg = "VAT / Tax No.";
	private JTextField tfSearchService;
	private JTextField tfSearchCar;
	private JTable tableCars;
	private TableRowSorter rowSorterCars;



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
					window.frame.setVisible(true);
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
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		frame.setBackground(new Color(255, 255, 0));
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setTitle("Nowy Rachunek - HCT");
		frame.setBounds(100, 100, 1170, 606);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPaneChosen = new JScrollPane();
		scrollPaneChosen.setBounds(766, 196, 318, 234);
		frame.setLocation(10, 10);
		frame.getContentPane().add(scrollPaneChosen);
		
		listChosen = new JList<String>();
		listChosen.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		listServices = new JList<String>();
		listServices.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		listItems = new JList<String>();
		listItems.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		model2Add = new DefaultListModel<>();

		scrollPaneChosen.setViewportView(listChosen);
		

		if(this.defaultPaths.size() >= 4){
			String t = this.defaultPaths.get(this.defaultPaths.size()-1);
//System.out.println("Before "+t);
			if(!t.contains(":\\")){
//System.out.println("middle "+t);
				if(t.contains(fv.MAX_SERVIS_QNT))
					t = t.replace(t.substring(t.lastIndexOf("x")+1), "1");;

//System.out.println("after "+t);
				this.model2Add.addElement(t);//this.defaultPaths.get(this.defaultPaths.size()-1));
				this.listChosen.setModel(model2Add);
			}
		}
		
		tfProdQ = new JTextField();
		tfProdQ.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfProdQ.setColumns(10);
		tfProdQ.setBounds(644, 313, 42, 24);
		frame.getContentPane().add(tfProdQ);
		
		tfServQ = new JTextField();
		tfServQ.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfServQ.setColumns(10);
		tfServQ.setBounds(644, 223, 42, 24);
		frame.getContentPane().add(tfServQ);
		
		JLabel labelSaleList = new JLabel("Us\u0142uga / Towar");
		labelSaleList.setHorizontalAlignment(SwingConstants.CENTER);
		labelSaleList.setBounds(766, 174, 96, 19);
		frame.getContentPane().add(labelSaleList);
		
		JLabel lblWystawRachunek = new JLabel("Wybierz us\u0142ugi/produkty");
		lblWystawRachunek.setForeground(Color.DARK_GRAY);
		lblWystawRachunek.setBounds(10, 179, 220, 22);
		lblWystawRachunek.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		frame.getContentPane().add(lblWystawRachunek);
		
		
		JLabel lblWybrane = new JLabel("");
		Border b1 = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border1 = BorderFactory.createTitledBorder(b1, "WYBRANE");
		lblWybrane.setBorder(border1);
		lblWybrane.setVerticalAlignment(SwingConstants.TOP);
		lblWybrane.setBounds(755, 154, 339, 292);
		frame.getContentPane().add(lblWybrane);
		
		JLabel lblCompanyDet = new JLabel();
		Border b2 = BorderFactory.createLineBorder(Color.yellow);
		TitledBorder border2 = BorderFactory.createTitledBorder(b2, "Firma:");
		lblCompanyDet.setBorder(border2);
		lblCompanyDet.setForeground(Color.DARK_GRAY);
		lblCompanyDet.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblCompanyDet.setBounds(644, 19, 500, 100);
		frame.getContentPane().add(lblCompanyDet);
		
		textFieldDiscount = new JTextField();
		textFieldDiscount.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		textFieldDiscount.setBounds(680, 407, 50, 23);
		frame.getContentPane().add(textFieldDiscount);
		textFieldDiscount.setColumns(10);
		
		JLabel labelZnizka = new JLabel("Znizka");
		labelZnizka.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		labelZnizka.setBounds(641, 385, 50, 23);
		frame.getContentPane().add(labelZnizka);
		
		rbPercent = new JRadioButton("%", false);
		rbPercent.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		rbPercent.setBounds(598, 407, 37, 23);
		frame.getContentPane().add(rbPercent);
		
		rbMoney = new JRadioButton("\u20AC", true);
		rbMoney.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		rbMoney.setBounds(637, 407, 37, 23);
		frame.getContentPane().add(rbMoney);
		
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
		lblCena.setBounds(975, 174, 50, 19);
		frame.getContentPane().add(lblCena);
				
		JButton btnPrint = new JButton("Drukuj rachunek");
		btnPrint.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnPrint.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				printDocument();

			}
		});
		btnPrint.setForeground(Color.BLACK);
		btnPrint.setBackground(new Color(178, 34, 34));
		btnPrint.setBounds(766, 509, 248, 32);
		frame.getContentPane().add(btnPrint);
		

		JButton btnSubb = new JButton("-");
		btnSubb.setForeground(new Color(255, 0, 0));
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
		btnSubb.setBounds(1098, 169, 40, 20);
		frame.getContentPane().add(btnSubb);
		
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
		btnClearAll.setBounds(1035, 130, 109, 24);
		frame.getContentPane().add(btnClearAll);
		
		JLabel lblDescription = new JLabel("Opis usługi/przedmiotu");
		lblDescription.setBounds(123, 507, 127, 14);
		frame.getContentPane().add(lblDescription);
		
		JLabel lblPriceOther = new JLabel("Cena");
		lblPriceOther.setBounds(430, 507, 28, 14);
		frame.getContentPane().add(lblPriceOther);
		
		JLabel lblQnt = new JLabel("Qnt");
		lblQnt.setBounds(480, 507, 28, 14);
		frame.getContentPane().add(lblQnt);
		
		tfServicePrice = new JTextField();
		tfServicePrice.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfServicePrice.setColumns(10);
		tfServicePrice.setBounds(598, 223, 42, 24);
		frame.getContentPane().add(tfServicePrice);
		
		JLabel lblPrice = new JLabel("Cena");
		lblPrice.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblPrice.setBounds(598, 201, 42, 20);
		frame.getContentPane().add(lblPrice);
		
		tfItemPrice = new JTextField();
		tfItemPrice.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfItemPrice.setColumns(10);
		tfItemPrice.setBounds(598, 313, 42, 24);
		frame.getContentPane().add(tfItemPrice);
		
		JLabel lblQty = new JLabel("Qnt");
		lblQty.setHorizontalAlignment(SwingConstants.CENTER);
		lblQty.setBounds(1035, 174, 37, 19);
		frame.getContentPane().add(lblQty);
		
		textFieldRegistration = new JTextField();
		textFieldRegistration.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		textFieldRegistration.setBounds(392, 91, 213, 28);
		frame.getContentPane().add(textFieldRegistration);
		textFieldRegistration.setColumns(10);
		
		JLabel lblWybierzPrzedmiot = new JLabel("Wybierz produkt");
		lblWybierzPrzedmiot.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblWybierzPrzedmiot.setBounds(10, 364, 109, 36);
		frame.getContentPane().add(lblWybierzPrzedmiot);
		
		tfSearchBox = new JTextField();
		tfSearchBox.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfSearchBox.setBounds(123, 345, 436, 20);
		frame.getContentPane().add(tfSearchBox);
		tfSearchBox.setColumns(10);
		
		tfOther1 = new JTextField();
		tfOther1.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfOther1.setText("Inny produkt / usługa");
		tfOther1.setBounds(123, 521, 303, 20);
		frame.getContentPane().add(tfOther1);
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
		tfOtherPrice1.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfOtherPrice1.setColumns(10);
		tfOtherPrice1.setBounds(430, 521, 48, 20);
		frame.getContentPane().add(tfOtherPrice1);
		
		tfOtherQnt1 = new JTextField();
		tfOtherQnt1.setColumns(10);
		tfOtherQnt1.setBounds(480, 521, 28, 20);
		frame.getContentPane().add(tfOtherQnt1);
		
		
		tfCompanyName = new JTextField();
		tfCompanyName.setHorizontalAlignment(SwingConstants.CENTER);
		tfCompanyName.setText(stringComName );
		tfCompanyName.setBackground(Color.LIGHT_GRAY);
		tfCompanyName.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfCompanyName.setBounds(655, 37, 489, 20);
		frame.getContentPane().add(tfCompanyName);
		tfCompanyName.setColumns(10);
		tfCompanyName.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfCompanyName.setText(fv.COMPANY_STRING);
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(tfCompanyName.getText().equals(fv.COMPANY_STRING))
					tfCompanyName.setText(stringComName);
			}
		});
		
		tfCompanyAddress = new JTextField();
		tfCompanyAddress.setHorizontalAlignment(SwingConstants.CENTER);
		tfCompanyAddress.setText(stringAddress );
		tfCompanyAddress.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfCompanyAddress.setColumns(10);
		tfCompanyAddress.setBackground(Color.LIGHT_GRAY);
		tfCompanyAddress.setBounds(655, 64, 489, 20);
		frame.getContentPane().add(tfCompanyAddress);
		tfCompanyAddress.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfCompanyAddress.setText(fv.COMPANY_STRING);
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(tfCompanyAddress.getText().equals(fv.COMPANY_STRING))
					tfCompanyAddress.setText(stringAddress);
			}
		});
		
		
		tfVATRegNum = new JTextField();
		tfVATRegNum.setHorizontalAlignment(SwingConstants.CENTER);
		tfVATRegNum.setText(stringVATReg );
		tfVATRegNum.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfVATRegNum.setColumns(10);
		tfVATRegNum.setBackground(Color.LIGHT_GRAY);
		tfVATRegNum.setBounds(655, 90, 489, 20);
		frame.getContentPane().add(tfVATRegNum);
		tfVATRegNum.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfVATRegNum.setText(fv.COMPANY_STRING);
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if(tfVATRegNum.getText().equals(fv.COMPANY_STRING))
					tfVATRegNum.setText(stringVATReg);
			}
		});


		JButton btnOtherAdd1 = new JButton("+");
		btnOtherAdd1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!tfOther1.getText().isEmpty() && !tfOtherPrice1.getText().isEmpty())
					addItemToList(tfOther1,tfOtherPrice1,tfOtherQnt1);
				else
					JOptionPane.showMessageDialog(frame, "Opis usługi oraz cena nie mogą być puste.");
			}
		});
		btnOtherAdd1.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnOtherAdd1.setBounds(515, 520, 44, 20);
		frame.getContentPane().add(btnOtherAdd1);
		
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
		scrollPaneItemList.setBounds(123, 365, 436, 120);
		frame.getContentPane().add(scrollPaneItemList);

		JLabel lblRegistration = new JLabel("Registration");
		lblRegistration.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblRegistration.setBounds(392, 66, 84, 24);
		frame.getContentPane().add(lblRegistration);
		
		JLabel labelQuantity = new JLabel("Qnt");
		labelQuantity.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		labelQuantity.setBounds(644, 201, 37, 20);
		frame.getContentPane().add(labelQuantity);
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		btnBack.setBounds(1035, 518, 89, 23);
		frame.getContentPane().add(btnBack);
		
		tfSearchService = new JTextField();
		tfSearchService.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfSearchService.setColumns(10);
		tfSearchService.setBounds(123, 213, 436, 20);
		frame.getContentPane().add(tfSearchService);
		
		tfSearchCar = new JTextField();
		tfSearchCar.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		tfSearchCar.setColumns(10);
		tfSearchCar.setBounds(75, 22, 300, 20);
		frame.getContentPane().add(tfSearchCar);
		
		tfSearchCar.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfSearchCar.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		
		tfSearchCar.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tfSearchCar.getText();

                if (text.trim().length() == 0) {
                    rowSorterCars.setRowFilter(null);
                } else {
                    rowSorterCars.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfSearchCar.getText();

                if (text.trim().length() == 0) {
                    rowSorterCars.setRowFilter(null);
                } else {
                    rowSorterCars.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

		JLabel labelClient = new JLabel("Klient");
		labelClient.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		labelClient.setBounds(23, 12, 50, 36);
		frame.getContentPane().add(labelClient);
	
		JLabel lblCarManufacturer = new JLabel(lblCarManufacturerTxt );
		Border b = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border = BorderFactory.createTitledBorder(b, lblCarManufacturerTxt);
		
		lblCarManufacturer.setBorder(border);

		lblCarManufacturer.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblCarManufacturer.setHorizontalAlignment(SwingConstants.CENTER);
		lblCarManufacturer.setBounds(385, 22, 220, 44);
		
	      
		frame.getContentPane().add(lblCarManufacturer);
		
		populateServices();
		populateItems();
		populateCarList();

		sumCosts();
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		        		fv.CLOSE_WINDOW, fv.CLOSE_WINDOW,  
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        	MainView.main(null);
		        } else {}
		    }
		});
	}
	
	private void printDocument(){
		sPrinter = new StockPrinter(defaultPaths);
		try {
			invoiceNum += 1;
			registration = textFieldRegistration.getText();
			if(!lblTotal.getText().equals(lblTotalSt)) {
				if((!tfCompanyName.getText().equals(stringComName) || !this.tfCompanyAddress.getText().equals(stringAddress) || !this.tfVATRegNum.getText().equals(stringVATReg))){
					String str = "";
					if(!tfCompanyName.getText().equals(fv.COMPANY_STRING))
						str += " " + tfCompanyName.getText();
					if(!tfCompanyAddress.getText().equals(fv.COMPANY_STRING))
						str += " " + tfCompanyAddress.getText();
					if(!tfVATRegNum.getText().equals(fv.COMPANY_STRING))
						str += " " + tfVATRegNum.getText();
					carManufacturer+=str;
				}
				printed = sPrinter.printDoc(listChosen, discount, applyDiscount, carManufacturer, registration, invoiceNum);
				if(printed){
					for (String key : nameQnt.keySet()){
						String query = "UPDATE \""+fv.STOCK_TABLE+"\" SET "+fv.STOCK_TABLE_QNT+"='"+nameQnt.get(key)+"'" + " WHERE " + fv.STOCK_TABLE_ITEM_NAME + "='"+key+"'";
//						System.out.println("Query: "+query);
						DM.editRecord(query);
					}
					frame.dispose();
					MainView.main(null);
				}
			} else
				JOptionPane.showMessageDialog(frame, "Nie przeliczyles wyniku");
		} catch (IOException e) {
			log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
		} catch (Exception e) {
			log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
		}
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
		lblTotal.setBounds(873, 460, 205, 32);
		frame.getContentPane().add(lblTotal);

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
		btnCalculate.setBounds(766, 460, 96, 32);
		frame.getContentPane().add(btnCalculate);		
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
					
					if(!tfItemPrice.getText().isEmpty())
						productPrice = tfItemPrice.getText();
					else
						productPrice = tempString;

					element2model = helper.paddStringRight(element2model, paddingLength, ch);
					
					element2model += " €"+productPrice;
					
					String productQuantity = "";
					if(!tfProdQ.getText().isEmpty())
						productQuantity = tfProdQ.getText(); 
					else
						productQuantity = "1";
					
					element2model+="x";
					int qnt = 0, qntForDatabase = 0;
					int qntInList = Integer.parseInt(listOfItems.get(index+2));
					if(!productQuantity.isEmpty())
						qnt = Integer.parseInt(productQuantity);
					else
						qnt = 1;
					
					while(qnt > qntInList){
						JOptionPane.showMessageDialog(frame, "Dostępnych "+qntInList+"szt.");
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
		btnAddItem.setBounds(688, 313, 44, 24);
		frame.getContentPane().add(btnAddItem);
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
		lblChoseServiceitem.setBounds(10, 207, 109, 36);
		frame.getContentPane().add(lblChoseServiceitem);
		
		JScrollPane scrollPaneServiceList = new JScrollPane();
		scrollPaneServiceList.setBounds(123, 235, 436, 96);
		frame.getContentPane().add(scrollPaneServiceList);
		
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
					
					if(!tfServicePrice.getText().isEmpty())
						servicePrice = tfServicePrice.getText();
					else
						servicePrice = tempString;
					
					element2model = helper.paddStringRight(element2model, paddingLength, ch);
					
					element2model += " €"+servicePrice;
					
					String productQuantity = "";
					if(!tfServQ.getText().isEmpty())
						productQuantity = tfServQ.getText(); 
					else
						productQuantity = "1";
					
					element2model+="x"+productQuantity;
					
					model2Add.addElement(element2model);
					listChosen.setModel(model2Add);
				}
			}
		});
		btnAddService.setBounds(688, 223, 44, 24);
		frame.getContentPane().add(btnAddService);
	}

	private void populateCarList() throws Exception {
		String queryCars = "SELECT "+this.fv.MANUFACTURER_TABLE_NAME+" FROM "+this.fv.MANUFACTURER_LIST_TABLE+" ORDER BY "+this.fv.MANUFACTURER_TABLE_NAME+" ASC";
		carManufacturer = "CAR";
	
		ArrayList<String> listOfCars = DM.selectRecordArrayList(queryCars);
		int rowNumber = listOfCars.size();
		String[][] data = new String[rowNumber ][this.fv.CARS_TABLE_HEADER.length]; 
		data = populateDataArrayString(listOfCars, data, rowNumber);

		createTable(data,this.fv.CARS_TABLE_HEADER);
	}

	private void createTable(String[][] data, String[] headings) {
		ListSelectionListener listener = createTableListener();
		DefaultTableModel dm = new DefaultTableModel(data, headings);
		tableCars = new JTable();
		frame.getContentPane().add(tableCars);
		tableCars.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCars.getSelectionModel().addListSelectionListener(listener);
		tableCars.setModel(dm);
		
		tableCars.setBounds(0, 0, 320, 373);
		tableCars.setPreferredScrollableViewportSize(new Dimension(500, 150));
		tableCars.setFillsViewportHeight(true);
		tableCars.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		tableCars.getColumnModel().getColumn(0).setPreferredWidth(299);
		
		rowSorterCars = new TableRowSorter<>(tableCars.getModel());
		tableCars.setRowSorter(rowSorterCars);
		
		JTableHeader header = tableCars.getTableHeader();
		header.setBackground(Color.black);
		header.setForeground(Color.yellow);

		JScrollPane scrollPaneCarList = new JScrollPane(tableCars);
		scrollPaneCarList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneCarList.setBounds(75, 44, 300, 124);
		frame.getContentPane().add(scrollPaneCarList);
	}

	private ListSelectionListener createTableListener() {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
//				helper.toggleJButton(btnAddToInvoice, Color.green, Color.darkGray, true);
			}
	    };
	    return listener;
	  }

	private String[][] populateDataArrayString(ArrayList<String> list, String[][] data, int rowNumber) {
		int j = 0;
		for(int i = 0; i < rowNumber; i++) {
			data[i][0] = list.get(j);
			j++;
		}		
		return data;
	}

}