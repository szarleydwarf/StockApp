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
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import dbase.DatabaseManager;
import utillity.StockPrinter;

public class WystawRachunek {

	private JFrame frmNowyRachunek;
	private DatabaseManager DM;
	private JTextField textFieldDiscount;
	private JList<String> listChosen, listItems, listServices;
	
	private DefaultListModel<String> model2Add;
	
	private ButtonGroup radioGroup;
	private JRadioButton rbPercent, rbMoney;
	
	private String carManufacturer, servicePrice, productPrice ;
	private int paddingLength = 24;
	private double sum = 0;
	private boolean moneyDiscount = true;
	private DecimalFormat df;
	private ArrayList<String>  serviceElements;
	private ArrayList<ArrayList<String>> servicesList;
	
	private StockPrinter sPrinter;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WystawRachunek window = new WystawRachunek();
					window.frmNowyRachunek.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public WystawRachunek() throws SQLException {
		DM = new DatabaseManager();
		try {
			initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		df = new DecimalFormat("#.##"); 
		servicesList = new ArrayList<ArrayList<String>> ();
		serviceElements = new ArrayList<String> ();
		frmNowyRachunek = new JFrame();
		
		frmNowyRachunek.setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\@Development\\EclipseJavaProjects\\sqliteTestApp\\StockApp\\resources\\img\\icon_hct.png"));
		frmNowyRachunek.setTitle("Nowy Rachunek - HCT");
		frmNowyRachunek.setBounds(100, 100, 763, 606);
		frmNowyRachunek.getContentPane().setLayout(null);
		
		JScrollPane scrollPaneChosen = new JScrollPane();
		scrollPaneChosen.setBounds(450, 172, 225, 178);
		frmNowyRachunek.getContentPane().add(scrollPaneChosen);
		
		listChosen = new JList<String>();
		listServices = new JList<String>();
		listItems = new JList<String>();
		model2Add = new DefaultListModel<>();

		scrollPaneChosen.setViewportView(listChosen);
		
		
		JLabel labelSaleList = new JLabel("Us\u0142uga / Towar");
		labelSaleList.setHorizontalAlignment(SwingConstants.CENTER);
		labelSaleList.setBounds(450, 152, 96, 19);
		frmNowyRachunek.getContentPane().add(labelSaleList);
		
		JLabel lblWystawRachunek = new JLabel("Wybierz us\u0142ugi/produkty");
		lblWystawRachunek.setBounds(71, 105, 214, 19);
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
		textFieldDiscount.setBounds(262, 298, 96, 32);
		frmNowyRachunek.getContentPane().add(textFieldDiscount);
		textFieldDiscount.setColumns(10);
		
		JLabel labelZnizka = new JLabel("Znizka");
		labelZnizka.setBounds(23, 298, 109, 36);
		frmNowyRachunek.getContentPane().add(labelZnizka);
		
		rbPercent = new JRadioButton("%", false);
		rbPercent.setBounds(134, 298, 37, 23);
		frmNowyRachunek.getContentPane().add(rbPercent);
		
		rbMoney = new JRadioButton("\u20AC", true);
		rbMoney.setBounds(173, 298, 37, 23);
		frmNowyRachunek.getContentPane().add(rbMoney);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(rbMoney);
		radioGroup.add(rbPercent);
		
		rbMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moneyDiscount = true;
			}
		});
		rbPercent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moneyDiscount = false;
			}
		});
		
		JLabel lblCena = new JLabel("Cena");
		lblCena.setHorizontalAlignment(SwingConstants.CENTER);
		lblCena.setBounds(598, 152, 77, 19);
		frmNowyRachunek.getContentPane().add(lblCena);
				
		JButton btnPrint = new JButton("Drukuj rachunek");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(listChosen.getModel().getSize() != 0 && sum >= 0) {
	//				System.out.println("Sum: "+sum);
					//opis/ilosc/cena/total
					sPrinter = new StockPrinter(servicesList);
					try {
						sPrinter.printDoc();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(frmNowyRachunek, "Nie wybrałeś żadnego towaru/usługi.");
				}
			}
		});
		btnPrint.setForeground(Color.BLACK);
		btnPrint.setBackground(new Color(178, 34, 34));
		btnPrint.setBounds(439, 498, 248, 32);
		frmNowyRachunek.getContentPane().add(btnPrint);
		

		JButton btnSubb = new JButton("-");
		btnSubb.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		btnSubb.setBounds(687, 144, 50, 24);
		frmNowyRachunek.getContentPane().add(btnSubb);
		
		sumCosts();
		populateServices();
		populateItems();
		populateCarList();

		
//		frmNowyRachunek.addWindowListener(new java.awt.event.WindowAdapter() {
//		    @Override
//		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//		        if (JOptionPane.showConfirmDialog(frmNowyRachunek, 
//		            "Are you sure to close this window?", "Really Closing?", 
//		            JOptionPane.YES_NO_OPTION,
//		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
//		        	frmNowyRachunek.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		        }
//		    }
//		});
	}
	
	private void sumCosts() {
		JButton btnCalculate = new JButton("Policz = ");
		
		JLabel lblTotal = new JLabel("TOTAL");
		lblTotal.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotal.setBounds(450, 447, 205, 32);
		frmNowyRachunek.getContentPane().add(lblTotal);
//servicesList, serviceElements
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String description  = "";
				String priceSt  = "";
				String quant  = "";
				if(listChosen.getModel().getSize() != 0){
					double tempNum=0;
					sum = 0;
					DefaultListModel md = (DefaultListModel) listChosen.getModel();
					if(md.size() > 0){
						for(int i = 0; i < md.size(); i++){
							String tempSt = md.getElementAt(i).toString();
<<<<<<< HEAD
							String subSt = tempSt.substring(tempSt.lastIndexOf("€")+1);
							sum += Double.parseDouble(subSt);
							
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
							int index = tempSt.indexOf("_");
							if(index == -1){
								index = tempSt.indexOf("€");
							}
							description = tempSt.substring(0, index);
							
							priceSt = tempSt.substring(tempSt.lastIndexOf("€")+1);
							priceSt = priceSt.substring(0, priceSt.lastIndexOf("x"));
							quant = tempSt.substring(tempSt.lastIndexOf("x")+1);
							double q = 0;
							if(!quant.isEmpty())
								q = Double.parseDouble(quant);
							else
								quant = "1";
							
							if(q == 0){
								q = 1;
							}	
							
							sum += (Double.parseDouble(priceSt)*q);
							serviceElements.add(description);
							serviceElements.add(priceSt);
							serviceElements.add(quant);
=======
							String subSt = tempSt.substring(tempSt.lastIndexOf("€")+1);
							sum += Double.parseDouble(subSt);
							
>>>>>>> parent of 3fa68d2... 14/9/17
=======
							String subSt = tempSt.substring(tempSt.lastIndexOf("€")+1);
							sum += Double.parseDouble(subSt);
							
>>>>>>> parent of 3fa68d2... 14/9/17
=======
							String subSt = tempSt.substring(tempSt.lastIndexOf("€")+1);
							sum += Double.parseDouble(subSt);
							
>>>>>>> parent of 3fa68d2... 14/9/17
>>>>>>> lol
=======
>>>>>>> parent of 3fa68d2... 14/9/17
=======
>>>>>>> parent of 3fa68d2... 14/9/17
=======
>>>>>>> parent of 3fa68d2... 14/9/17
=======
>>>>>>> parent of 3fa68d2... 14/9/17
						}
					}

					serviceElements.add(Double.toString(sum));
					servicesList.add(serviceElements);
					
					if(!textFieldDiscount.getText().equals(""))
						tempNum = Double.parseDouble(textFieldDiscount.getText());
					
					if(moneyDiscount){
						sum -= tempNum;
					} else if(!moneyDiscount){
						sum -= (sum * (tempNum/100));
					} else {
						sum = sum;
					}
					servicesList.add(serviceElements);
					
				}else{
					sum = 0;
				}
				lblTotal.setText("€ "+df.format(sum));
				
				
				for(int i=0;i<servicesList.size();i++){
					servicesList.get(i);
					System.out.println(servicesList.get(i));
				}
			}
		});
		btnCalculate.setForeground(new Color(0, 0, 0));
		btnCalculate.setBackground(new Color(220, 20, 60));
		btnCalculate.setBounds(304, 447, 120, 32);
		frmNowyRachunek.getContentPane().add(btnCalculate);
		
	}

<<<<<<< HEAD
=======
	private void populateCarList() throws Exception {
		String queryCars = "SELECT manufacturer FROM manufacturers ORDER BY manufacturer ASC";
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
		scrollPaneCarList.setBounds(164, 11, 194, 78);
		frmNowyRachunek.getContentPane().add(scrollPaneCarList);

		JLabel lblCarManufacturer = new JLabel("Car manufacturer");
		lblCarManufacturer.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblCarManufacturer.setHorizontalAlignment(SwingConstants.CENTER);
		lblCarManufacturer.setBounds(439, 16, 242, 73);
	
		frmNowyRachunek.getContentPane().add(lblCarManufacturer);
		

				
		JList listCars = new JList();
		listCars.setModel(modelCars);
		scrollPaneCarList.setViewportView(listCars);

		JButton btnAddCustomer = new JButton("+");
		btnAddCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char ch = '_';
				 carManufacturer = (String) listCars.getSelectedValue();
//				System.out.println(carManufacturer);
				lblCarManufacturer.setText(carManufacturer);
				}
		});
		btnAddCustomer.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnAddCustomer.setBounds(379, 16, 50, 24);
//		lblCarManufacturer.setText(carManufacturer);
		frmNowyRachunek.getContentPane().add(btnAddCustomer);
	}

>>>>>>> parent of 3fa68d2... 14/9/17
	private void populateItems() throws Exception {
		String queryItems = "SELECT item_name, price FROM stock";
		DefaultListModel<String> modelItems = new DefaultListModel<>();
	
		ArrayList<String> listOfItems = DM.selectRecordArrayList(queryItems);
		
		for(int i = 0; i < listOfItems.size(); i+=2) {
			String tempString = listOfItems.get(i);
			modelItems.addElement(tempString);
//			System.out.println("ST: "+alist.get(i));
		}
		JLabel lblWybierzPrzedmiot = new JLabel("Wybierz produkt");
		lblWybierzPrzedmiot.setBounds(23, 223, 109, 36);
		frmNowyRachunek.getContentPane().add(lblWybierzPrzedmiot);
		
		JScrollPane scrollPaneItemList = new JScrollPane();
		scrollPaneItemList.setBounds(164, 223, 194, 60);
		frmNowyRachunek.getContentPane().add(scrollPaneItemList);
		
		scrollPaneItemList.setViewportView(listItems);
		listItems.setModel(modelItems);
		
		JButton btnAddItem = new JButton("+");
		btnAddItem.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				char ch = '_';
				String element2model = (String) listItems.getSelectedValue();
				
				int index = listOfItems.indexOf(element2model);
				String tempString = listOfItems.get(index+1);
				productPrice = tempString;
				element2model = paddString(element2model, paddingLength, ch);
				
				element2model += " €"+tempString;
				model2Add.addElement(element2model);
				listChosen.setModel(model2Add);}
		});
		btnAddItem.setBounds(379, 228, 50, 24);
		frmNowyRachunek.getContentPane().add(btnAddItem);
	}

	private void populateServices() throws Exception {
		String queryServices = "SELECT service_name, price FROM services";
		DefaultListModel<String> model = new DefaultListModel<>();
	
		ArrayList<String> listOfServices = DM.selectRecordArrayList(queryServices);
		
		for(int i = 0; i < listOfServices.size(); i+=2) {
			String tempString = listOfServices.get(i);
			model.addElement(tempString);
//			System.out.println("ST: "+alist.get(i));
		}
				
		JLabel lblChoseServiceitem = new JLabel("Wybierz us\u0142ug\u0119");
		lblChoseServiceitem.setBounds(23, 152, 109, 36);
		frmNowyRachunek.getContentPane().add(lblChoseServiceitem);
		
		JScrollPane scrollPaneServiceList = new JScrollPane();
		scrollPaneServiceList.setBounds(164, 152, 194, 60);
		frmNowyRachunek.getContentPane().add(scrollPaneServiceList);
		
		scrollPaneServiceList.setViewportView(listServices);
		listServices.setModel(model);
		
		JButton btnAddService = new JButton("+");
		btnAddService.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));

		
		btnAddService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				char ch = '_';
				String element2model = (String) listServices.getSelectedValue();
				
				int index = listOfServices.indexOf(element2model);
				String tempString = listOfServices.get(index+1);
				servicePrice = tempString;
				element2model = paddString(element2model, paddingLength, ch);
				
				element2model += " €"+tempString;
				model2Add.addElement(element2model);
				listChosen.setModel(model2Add);
			}
		});
		btnAddService.setBounds(379, 157, 50, 24);
		frmNowyRachunek.getContentPane().add(btnAddService);
	}
	
	private void populateCarList() throws Exception {
		String queryCars = "SELECT manufacturer FROM manufacturers ORDER BY manufacturer ASC";
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
		scrollPaneCarList.setBounds(164, 11, 194, 78);
		frmNowyRachunek.getContentPane().add(scrollPaneCarList);

		JLabel lblCarManufacturer = new JLabel("Car manufacturer");
		lblCarManufacturer.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblCarManufacturer.setHorizontalAlignment(SwingConstants.CENTER);
		lblCarManufacturer.setBounds(439, 16, 242, 73);
	
		frmNowyRachunek.getContentPane().add(lblCarManufacturer);
		

				
		JList listCars = new JList();
		listCars.setModel(modelCars);
		scrollPaneCarList.setViewportView(listCars);

		JButton btnAddCustomer = new JButton("+");
		btnAddCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char ch = '_';
				 carManufacturer = (String) listCars.getSelectedValue();
//				System.out.println(carManufacturer);
				lblCarManufacturer.setText(carManufacturer);
				}
		});
		btnAddCustomer.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnAddCustomer.setBounds(379, 16, 50, 24);
//		lblCarManufacturer.setText(carManufacturer);
		frmNowyRachunek.getContentPane().add(btnAddCustomer);
		
		
		JLabel labelQuantity = new JLabel("Quantity");
		labelQuantity.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		labelQuantity.setBounds(320, 105, 61, 19);
		frmNowyRachunek.getContentPane().add(labelQuantity);
	}

	public String paddString(String string2Padd, int stringLength, char paddingChar){
		if(stringLength <= 0)
			return string2Padd;
		
		StringBuilder sb = new StringBuilder(string2Padd);
		stringLength = stringLength - sb.length() - 1;
		while(stringLength-- >= 0){
			sb.append(paddingChar);
		}
		return sb.toString();
		
	}
}
