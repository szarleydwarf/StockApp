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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
import utillity.StockPrinter;

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
	private String carManufacturer, registration, servicePrice ;
	private double productPrice;
	private final String lblTotalSt = "TOTAL";
	private int paddingLength = 22, invoiceNum = 0;
	private double discount = 0;
	private boolean isDiscount = true;
	private boolean printed = false;
	private char ch = ' ';
	private Item item;

	private ArrayList<String> defaultPaths;
	private Map<String, Integer> nameQnt;
	private String stringAddress = "Adres Firmy";
	private String stringComName = "Nazwa firmy";
	private String stringVATReg = "VAT / Tax No.";
	private JTextField tfSearchItem;
	private JTextField tfSearchCar;
	private JTable tableCars;
	private TableRowSorter rowSorterStock,rowSorterCars;
	private JTextField tfCarsSearchBox;
	private JTextField tfSearch;
	private JTextField tfOtherService;
	private JTextField tfRegistration;
	private JTextField tfPriceListed;
	private JTextField tfQntListed;
	private JTextField tfQntOther;
	private JTextField tfPriceOther;
	private JTextField tfCompanyName;
	private JTextField tfCompanyAddress;
	private JTextField tfVATRegNum;
	private JTextField tfDiscountAmount;
	private JLabel lblDiscount;
	private JLabel lblPriceListed;
	private JLabel lblPriceOther;
	private JLabel lblQntListed;
	private JLabel lblQntOther;
	private JLabel lblCarList;
	private JLabel lblStockList;
	private JLabel lblCompanyDetails;
	private JLabel lblChoosenList;
	private JLabel lblNewInvoice;
	private JCheckBox chbTyreShine;
	private JCheckBox chbCaps;
	private JLabel lblCarBrand;
	private ArrayList<Item> wholeStock;
	private JTable tbChoosen;
	private String cenaStr = "Cena";
	protected double sum;
	private TableModel modTBchosen;
	private String markaAuta = "MARKA";
	private String otherString = "other";
	private String defRegistrationString = "wpisz rejestracje", defaultRegistration = "00AA000";
	private boolean[] freebies;
	private JCheckBox chbAirfreshener;
	private JCheckBox chckbxDarmoweMycie;


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
//		if(invoiceNum == 0)
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
		frame.getContentPane().setLayout(null);
				
		lblNewInvoice = new JLabel("Nowy Rachunek");
		lblNewInvoice.setForeground(new Color(51, 51, 51));
		lblNewInvoice.setBackground(new Color(102, 153, 255));
		lblNewInvoice.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewInvoice.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblNewInvoice.setBounds(350, 0, 350, 30);
		frame.getContentPane().add(lblNewInvoice);

	
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setTitle("Nowy Rachunek - HCT");
		frame.setBounds(10, 10, 1121, 606);
		
		tfRegistration = new JTextField();
		tfRegistration.setHorizontalAlignment(SwingConstants.CENTER);
		tfRegistration.setText(defRegistrationString );
		tfRegistration.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfRegistration.setColumns(10);
		tfRegistration.setBounds(438, 91, 160, 30);
		tfRegistration.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfRegistration.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		frame.getContentPane().add(tfRegistration);
		
		lblCarBrand = new JLabel(markaAuta);
		lblCarBrand.setHorizontalAlignment(SwingConstants.CENTER);
		lblCarBrand.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		lblCarBrand.setBounds(438, 41, 160, 44);
		Border b5 = BorderFactory.createLineBorder(Color.yellow);
		TitledBorder border5 = BorderFactory.createTitledBorder(b5, "MARKA");
		lblCarBrand.setBorder(border5);
		frame.getContentPane().add(lblCarBrand);
		
		tfPriceListed = new JTextField();
		tfPriceListed.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfPriceListed.setColumns(10);
		tfPriceListed.setBounds(432, 296, 50, 24);
		frame.getContentPane().add(tfPriceListed);
		
		JButton btnAddListed = new JButton("+");
		btnAddListed.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnAddListed.setBounds(552, 296, 46, 24);
		btnAddListed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addToInvoice();
			}
		});
		frame.getContentPane().add(btnAddListed);
		
		tfQntListed = new JTextField();
		tfQntListed.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfQntListed.setColumns(10);
		tfQntListed.setBounds(492, 296, 50, 24);
		frame.getContentPane().add(tfQntListed);
		
		tfOtherService = new JTextField();
		tfOtherService.setText("wpisz przedmiot");
		tfOtherService.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfOtherService.setColumns(10);
		tfOtherService.setBounds(16, 520, 400, 24);
		tfOtherService.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfOtherService.setText(fv.OTHER_STRING_CHECKUP);
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		frame.getContentPane().add(tfOtherService);
		
		tfQntOther = new JTextField();
		tfQntOther.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfQntOther.setColumns(10);
		tfQntOther.setBounds(486, 520, 50, 24);
		frame.getContentPane().add(tfQntOther);
		
		tfPriceOther = new JTextField();
		tfPriceOther.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfPriceOther.setColumns(10);
		tfPriceOther.setBounds(426, 520, 50, 24);
		frame.getContentPane().add(tfPriceOther);
	
		JButton btnAddOther = new JButton("+");
		btnAddOther.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnAddOther.setBounds(546, 520, 46, 24);
		btnAddOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!tfOtherService.getText().isEmpty() && !tfPriceOther.getText().isEmpty())
					addItemToList(tfOtherService,tfPriceOther,tfQntOther);
				else
					JOptionPane.showMessageDialog(frame, "Opis usługi oraz cena nie mogą być puste.");
			}
		});
		frame.getContentPane().add(btnAddOther);

		JButton btnPrint = new JButton("DRUKUJ");
		btnPrint.setForeground(Color.YELLOW);
		btnPrint.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnPrint.setBackground(new Color(204, 0, 0));
		btnPrint.setBounds(620, 520, 160, 30);
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				printDocument();
			}
		});
		frame.getContentPane().add(btnPrint);
//TODO	
		JButton btnZapisz = new JButton("ZAPISZ");
		btnZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				savePDFtoHDD();
			}
		});
		btnZapisz.setForeground(new Color(0, 0, 102));
		btnZapisz.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnZapisz.setBackground(new Color(102, 204, 0));
		btnZapisz.setBounds(798, 520, 160, 30);
		frame.getContentPane().add(btnZapisz);

		JButton btnBack = new JButton("Powrót");
		btnBack.setForeground(new Color(0, 0, 0));
		btnBack.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnBack.setBackground(new Color(51, 204, 0));
		btnBack.setBounds(1000, 520, 100, 30);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				MainView.main(null);
			}
		});
		frame.getContentPane().add(btnBack);
		
		lblTotal = new JLabel(lblTotalSt);
		lblTotal.setForeground(new Color(51, 51, 51));
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotal.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblTotal.setBounds(746, 464, 200, 44);
		Border b6 = BorderFactory.createLineBorder(Color.yellow);
		TitledBorder border6 = BorderFactory.createTitledBorder(b6, "TOTAL");
		lblTotal.setBorder(border6);
		frame.getContentPane().add(lblTotal);
		
		tfCompanyName = new JTextField();
		tfCompanyName.setBackground(new Color(204, 204, 204));
		tfCompanyName.setText(this.stringComName);
		tfCompanyName.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfCompanyName.setColumns(10);
		tfCompanyName.setBounds(620, 50, 400, 24);
		frame.getContentPane().add(tfCompanyName);
		
		tfCompanyAddress = new JTextField();
		tfCompanyAddress.setText(this.stringAddress);
		tfCompanyAddress.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfCompanyAddress.setColumns(10);
		tfCompanyAddress.setBackground(new Color(204, 204, 204));
		tfCompanyAddress.setBounds(620, 80, 400, 24);
		frame.getContentPane().add(tfCompanyAddress);
		
		tfVATRegNum = new JTextField();
		tfVATRegNum.setText(this.stringVATReg);
		tfVATRegNum.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfVATRegNum.setColumns(10);
		tfVATRegNum.setBackground(new Color(204, 204, 204));
		tfVATRegNum.setBounds(620, 110, 400, 24);
		frame.getContentPane().add(tfVATRegNum);
		
		JButton btnRemove = new JButton("-");
		btnRemove.setForeground(new Color(204, 255, 0));
		btnRemove.setBackground(new Color(204, 0, 0));
		btnRemove.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnRemove.setBounds(1040, 200, 46, 24);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(item != null && tbChoosen.getSelectedRow() != -1){
					DefaultTableModel model = (DefaultTableModel) tbChoosen.getModel();
					model.removeRow(tbChoosen.getSelectedRow());
				}
			}
		});
		frame.getContentPane().add(btnRemove);
				
		JButton btnClearAll = new JButton("Clear All");
		btnClearAll.setForeground(new Color(204, 255, 0));
		btnClearAll.setBackground(new Color(204, 0, 0));
		btnClearAll.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnClearAll.setBounds(930, 410, 100, 18);
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel model = (DefaultTableModel) tbChoosen.getModel();
				model.setRowCount(0);
			}
		});
		frame.getContentPane().add(btnClearAll);
			
		rbPercent = new JRadioButton("%", false);
		rbPercent.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		rbPercent.setBounds(620, 430, 40, 24);
		frame.getContentPane().add(rbPercent);
		
		rbMoney = new JRadioButton("\u20AC", true);
		rbMoney.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		rbMoney.setBounds(670, 430, 40, 24);
		frame.getContentPane().add(rbMoney);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(rbMoney);
		radioGroup.add(rbPercent);
		
		tfDiscountAmount = new JTextField();
		tfDiscountAmount.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfDiscountAmount.setColumns(10);
		tfDiscountAmount.setBounds(720, 430, 50, 24);
		frame.getContentPane().add(tfDiscountAmount);

		rbMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isDiscount = true;
				double sum = calculateSum();
				sum = applyDiscount(sum);
				lblTotal.setText("€ "+df.format(sum));
			}
		});
		rbPercent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isDiscount = false;
				double sum = calculateSum();
				sum = applyDiscount(sum);
				lblTotal.setText("€ "+df.format(sum));
			}
		});

		lblDiscount = new JLabel("Zniżka");
		lblDiscount.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblDiscount.setBounds(620, 405, 60, 20);
		frame.getContentPane().add(lblDiscount);
		
		lblPriceListed = new JLabel(cenaStr);
		lblPriceListed.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblPriceListed.setBounds(432, 270, 40, 20);
		frame.getContentPane().add(lblPriceListed);
		
		lblPriceOther = new JLabel(cenaStr);
		lblPriceOther.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblPriceOther.setBounds(426, 494, 40, 20);
		frame.getContentPane().add(lblPriceOther);
		
		lblQntListed = new JLabel("Sztuk");
		lblQntListed.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblQntListed.setBounds(492, 270, 44, 20);
		frame.getContentPane().add(lblQntListed);
		
		lblQntOther = new JLabel("Sztuk");
		lblQntOther.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblQntOther.setBounds(486, 494, 44, 20);
		frame.getContentPane().add(lblQntOther);
		
		lblCarList = new JLabel("");
		Border b1 = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border1 = BorderFactory.createTitledBorder(b1, "KLIENT");
		lblCarList.setBorder(border1);
		lblCarList.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblCarList.setBounds(10, 32, 418, 194);
		frame.getContentPane().add(lblCarList);
		
		lblStockList = new JLabel("");
		lblStockList.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblStockList.setBounds(10, 246, 418, 253);
		Border b2 = BorderFactory.createLineBorder(Color.BLUE);
		TitledBorder border2 = BorderFactory.createTitledBorder(b2, "WYBIERZ USŁUGĘ / PRODUKT");
		lblStockList.setBorder(border2);
		frame.getContentPane().add(lblStockList);
		
		lblCompanyDetails = new JLabel("");
		lblCompanyDetails.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblCompanyDetails.setBounds(608, 30, 424, 120);
		Border b3 = BorderFactory.createLineBorder(Color.darkGray);
		TitledBorder border3 = BorderFactory.createTitledBorder(b3, "FIRMA");
		lblCompanyDetails.setBorder(border3);
		frame.getContentPane().add(lblCompanyDetails);
		
		lblChoosenList = new JLabel("");
		lblChoosenList.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		lblChoosenList.setBounds(608, 180, 424, 223);
		Border b4 = BorderFactory.createLineBorder(Color.yellow);
		TitledBorder border4 = BorderFactory.createTitledBorder(b4, "WYBRANE");
		lblChoosenList.setBorder(border4);
		frame.getContentPane().add(lblChoosenList);

		tfCarsSearchBox = new JTextField();
		tfCarsSearchBox.setText("wpisz markę");
		tfCarsSearchBox.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfCarsSearchBox.setBounds(16, 51, 405, 24);
		frame.getContentPane().add(tfCarsSearchBox);
		tfCarsSearchBox.setColumns(10);
		tfCarsSearchBox.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfCarsSearchBox.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		
		
		tfCarsSearchBox.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tfCarsSearchBox.getText();

                if (text.trim().length() == 0) {
                	rowSorterCars.setRowFilter(null);
                } else {
                	rowSorterCars.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfCarsSearchBox.getText();

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
		
		tfSearch = new JTextField();
		tfSearch.setText("wpisz przedmiot");
		tfSearch.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfSearch.setColumns(10);
		tfSearch.setBounds(16, 270, 405, 24);
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
	
		tfSearch.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) {
                    rowSorterStock.setRowFilter(null);
                } else {
                    rowSorterStock.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) {
                    rowSorterStock.setRowFilter(null);
                } else {
                    rowSorterStock.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

		// CHECKBOX - FREEBES SECTION
		JLabel lblFreebies = new JLabel("");
		lblFreebies.setBounds(432, 331, 166, 140);
		Border b7 = BorderFactory.createLineBorder(Color.orange);
		TitledBorder border7 = BorderFactory.createTitledBorder(b7, "PREZENTY");
		lblFreebies.setBorder(border7);
		frame.getContentPane().add(lblFreebies);
		
		chbAirfreshener = new JCheckBox("Odświeżacz");
		chbAirfreshener.setBackground(new Color(255, 51, 51));
		chbAirfreshener.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		chbAirfreshener.setBounds(446, 350, 138, 23);
		frame.getContentPane().add(chbAirfreshener);

		chbTyreShine = new JCheckBox("Połysk do kół");
		chbTyreShine.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		chbTyreShine.setBackground(new Color(255, 51, 51));
		chbTyreShine.setBounds(446, 380, 138, 23);
		frame.getContentPane().add(chbTyreShine);
		
		chbCaps = new JCheckBox("Zestaw nakrętek");
		chbCaps.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		chbCaps.setBackground(new Color(255, 51, 51));
		chbCaps.setBounds(446, 409, 138, 23);
		frame.getContentPane().add(chbCaps);
		
		chckbxDarmoweMycie = new JCheckBox("Darmowe mycie");
		chckbxDarmoweMycie.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		chckbxDarmoweMycie.setBackground(new Color(255, 51, 51));
		chckbxDarmoweMycie.setBounds(446, 440, 138, 23);
		frame.getContentPane().add(chckbxDarmoweMycie);

		// CHECKBOX - FREEBES SECTION END

	
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
		
		createChoosenItemsTable();
		populateStockTable();
		populateCarTable();
	}// END OF INSTANTIATE
	

	private void createChoosenItemsTable() {
		ArrayList<Item>emptyArray = new ArrayList<Item>();
		String[][] data = new String [0][this.fv.STOCK_TB_HEADINGS_NO_COST.length];
		data = populateDataArray(emptyArray, data, 0, 0);

		tbChoosen = new JTable();
		tbChoosen = createTable(data, this.fv.STOCK_TB_HEADINGS_NO_COST, fv.CHOSEN_TB_NAME, 240);
	
		JScrollPane spInvoice = new JScrollPane(tbChoosen);
		spInvoice.setBounds(620, 200, 400, 194);
		frame.getContentPane().add(spInvoice);
		
		modTBchosen = tbChoosen.getModel();
		modTBchosen.addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent arg0) {
				double sum = calculateSum();
				lblTotal.setText("€ "+df.format(sum));
			}			
		});
	}

	private void populateStockTable() {
		String query = "SELECT * from "+this.fv.STOCK_TABLE+" ORDER BY "+fv.STOCK_SORT_BY+" ASC";
		ArrayList<Item> listOfItems = new ArrayList<Item>();
		listOfItems = DM.getItemsList(query);

		String queryServices = "SELECT * from "+this.fv.SERVICES_TABLE+" ORDER BY "+fv.SERVICES_SORT_BY+" ASC";//item_name, cost, price,quantity
		ArrayList<Item> listOfServices = new ArrayList<Item>();
		listOfServices = DM.getItemsList(queryServices);
		
		wholeStock = new ArrayList<Item>();
		wholeStock.addAll(listOfServices);
		wholeStock.addAll(listOfItems);

		int rowNumber = wholeStock.size();

		String[][] data = new String [rowNumber][this.fv.STOCK_TB_HEADINGS_NO_COST.length];
		data = populateDataArray(wholeStock, data, 0, wholeStock.size());

		JTable table = new JTable();
		table = createTable(data, this.fv.STOCK_TB_HEADINGS_NO_COST, fv.STOCK_TB_NAME, 240);
		rowSorterStock = new TableRowSorter<>(table.getModel());
		
		table.setRowSorter(rowSorterStock);
	
		JScrollPane spStockList = new JScrollPane(table);
		spStockList.setBounds(16, 296, 405, 194);
		frame.getContentPane().add(spStockList);
		
	}

	private void populateCarTable() {
		String queryCars = "SELECT "+this.fv.MANUFACTURER_TABLE_NAME+" FROM "+this.fv.MANUFACTURER_LIST_TABLE+" ORDER BY "+this.fv.MANUFACTURER_TABLE_NAME+" ASC";
		ArrayList<String> listOfCars = new ArrayList<String>();
		listOfCars = DM.selectRecordArrayList(queryCars);
	
		String[][] data = new String [listOfCars.size()][this.fv.CARS_TABLE_HEADER.length];
		data = populateDataArrayString(listOfCars, data, listOfCars.size());

		JTable table = new JTable();
		table = createTable(data, this.fv.CARS_TABLE_HEADER, fv.CARS_TB_NAME, 380);
		rowSorterCars = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rowSorterCars);

		JScrollPane spCars = new JScrollPane(table);
		spCars.setSize(405, 140);
		spCars.setLocation(16, 77);
		frame.getContentPane().add(spCars);
	}

	private JTable createTable(String[][] data, String[] headings, String tbName, int firstColumnWidth) {
		DefaultTableModel dm = new DefaultTableModel(data, headings);
		JTable table = new JTable();
		table.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(dm);
		table.setName(tbName);

		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(firstColumnWidth);
		ListSelectionListener listener = null;
		if(tbName == fv.STOCK_TB_NAME)
			listener = createStockTableListener(table);
		else if(tbName == fv.CARS_TB_NAME)
			listener = createCarTableListener(table);
		else if(tbName == fv.CHOSEN_TB_NAME)
			listener = createStockTableListener(table);
		
		table.getSelectionModel().addListSelectionListener(listener);
				
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.black);
		header.setForeground(Color.yellow);
		
		return table;
	}

	private ListSelectionListener createCarTableListener(JTable table) {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
//				helper.toggleJButton(btnAddToInvoice, Color.green, Color.darkGray, true);
				int row = table.getSelectedRow();
				if(row != -1) {
					lblCarBrand.setText(table.getModel().getValueAt(table.convertRowIndexToModel(row), 0).toString());
				} 

			}
	    };
	    return listener;
	
	}
	private ListSelectionListener createStockTableListener(JTable table) {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				item = null;
				int row = table.getSelectedRow();
				if(row != -1) {
					item = getItem(table.getModel().getValueAt(table.convertRowIndexToModel(row), 0).toString());
					if(item != null){
//						System.out.println("Name: "+item.getName());
					}
				}
			}
	    };
	    return listener;
	
	}

	protected Item getItem(String string) {
		Item item = null;
		for(Item i : wholeStock){
			if(i.getName().equals(string))
				item = i;
		}
		return item;
	}

	private String[][] populateDataArray(ArrayList<Item> list, String[][] data, int startIndex, int rowNumber){
		int j = 0;
		for(int i = startIndex; i < rowNumber; i++) {
			data[i][0] = list.get(j).getName();
			data[i][1] = ""+list.get(j).getPrice();
			if(list.get(j) instanceof StockItem)
				data[i][2] = ""+((StockItem) list.get(j)).getQnt();
			else
				data[i][2] = ""+0;
			j++;
		}		
		return data;
	}

	private String[][] populateDataArrayString(ArrayList<String> list, String[][] data, int rowNumber) {
		int j = 0;
		for(int i = 0; i < rowNumber; i++) {
			data[i][0] = list.get(j);
			j++;
		}		
		return data;
	}

	protected void addToInvoice() {
		if(!tfPriceListed.getText().isEmpty())
			productPrice = Double.parseDouble(tfPriceListed.getText());
		else
			productPrice = item.getPrice();
		
		int tfQnt = 0;
		int itemQnt = 0;
		if(item instanceof StockItem)
			itemQnt = ((StockItem) item).getQnt();
		else
			itemQnt = 1;
		
		if(!this.tfQntListed.getText().isEmpty())
			tfQnt = Integer.parseInt(this.tfQntListed.getText());
		else
			tfQnt = 1;
		
		while(tfQnt > itemQnt && (item instanceof StockItem)){
			JOptionPane.showMessageDialog(frame, "Dostępnych "+itemQnt+"szt.");
			if(tfQnt > itemQnt)
				return;
		}
		
		String[] rowData = new String[this.fv.STOCK_TB_HEADINGS_NO_COST.length];

		rowData[0] = item.getName();
		rowData[1] = ""+productPrice;
		rowData[2] = ""+tfQnt;
		
		DefaultTableModel model = (DefaultTableModel) tbChoosen.getModel();
		model.addRow(rowData);
	}

	private void addItemToList(JTextField tfOther, JTextField tfOtherPrice, JTextField tfOtherQnt) {
		String[] rowData = new String[this.fv.STOCK_TB_HEADINGS_NO_COST.length];

		rowData[0] = tfOther.getText();
		rowData[1] = ""+tfOtherPrice.getText();
		rowData[2] = ""+tfOtherQnt.getText();
		
		DefaultTableModel model = (DefaultTableModel) tbChoosen.getModel();
		model.addRow(rowData);
	}

	protected double calculateSum() {
		int rowCount = modTBchosen.getRowCount();
		double sum = 0;
		for(int i = 0; i < rowCount;i++) {
			double price = Double.parseDouble((String) modTBchosen.getValueAt(i, 1));
			int qnt = Integer.parseInt((String)modTBchosen.getValueAt(i, 2));
			price = price * qnt;
			
			sum += price;
		}
		return sum;
	}
	
	protected double applyDiscount(double sum) {
		if(sum > 0 && !tfDiscountAmount.getText().isEmpty()){
			discount = Double.parseDouble(tfDiscountAmount.getText());
			if(!isDiscount){
				return sum - (sum * (discount/100));
			}else if(isDiscount){
				return sum - discount;
			}
		}
		return sum;
	}
//TODO
	protected void savePDFtoHDD() {
		sPrinter = new StockPrinter(defaultPaths);
		collectDataForInvoice();
//		for(int i = 0; i <freebies.length;i++)
//		System.out.println("save "+carManufacturer + " " + freebies[i] + discount + " " + isDiscount + " " + registration + " " + invoiceNum);
		try {
			if(!lblTotal.getText().equals(lblTotalSt)){
				boolean saved = sPrinter.saveDoc(tbChoosen, freebies, discount, isDiscount, carManufacturer, registration, invoiceNum);			
			} else
				JOptionPane.showMessageDialog(frame, "Wynik nie został poprawnie policzony.");
		} catch (Exception e) {
			log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
		}
	}

	private void printDocument(){
		sPrinter = new StockPrinter(defaultPaths);
		collectDataForInvoice();
//		System.out.println("print "+carManufacturer + " " + discount + " " + isDiscount + " " +  registration + " " + invoiceNum);
		try {
			if(!lblTotal.getText().equals(lblTotalSt)){
				boolean printed = sPrinter.printDoc(tbChoosen, freebies, discount, isDiscount, carManufacturer, registration, invoiceNum);			
		} else
			JOptionPane.showMessageDialog(frame, "Wynik nie został poprawnie policzony.");
		} catch (Exception e) {
			log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
		}
	}
	private void collectDataForInvoice() {
		registration = tfRegistration.getText();
		if(!lblCarBrand.getText().equals(markaAuta))
			carManufacturer = lblCarBrand.getText();
		else
			carManufacturer = otherString;
		
		if(!tfRegistration.getText().equals(defRegistrationString))
			registration = tfRegistration.getText();
		else registration = defaultRegistration;
		
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
		freebies = new boolean[fv.FREEBIES_ARRAY_SIZE];
		if(chbAirfreshener.isSelected())
			freebies[0] = true;
		else freebies[0] = false;
		
		if(this.chbTyreShine.isSelected())
			freebies[1] = true;
		else freebies[1] = false;
		
		if(this.chbCaps.isSelected())
			freebies[2] = true;
		else freebies[2] = false;
	
		if(this.chbTyreShine.isSelected())
			freebies[3] = true;
		else freebies[3] = false;
	}
}