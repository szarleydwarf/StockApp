package app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import dbase.DatabaseManager;
import hct_speciale.Customer;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class AddNewCustomer {

	private JFrame frame;
	private ArrayList<String> defaultPaths;
	private DatabaseManager DM;
	private ArrayList<Customer> listOfCustomers;
	private static FinalVariables fv;
	private static Logger log;
	private static Helper helper;
	
	protected static String date;
	protected static String loggerFolderPath;
	private JTextField textField;
	private JTextField tfCompanyName;
	private JTextField tfVatTax;
	private JTextField tfAddress;
	protected boolean varEmpty;
	private String[] carsList;
	protected String selectedCar;
	private Map<String, String> carMap;
	protected String index;

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
					AddNewCustomer window = new AddNewCustomer(defaultPaths);
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
	public AddNewCustomer(ArrayList<String> defaultPaths) {
		DM = new DatabaseManager(loggerFolderPath);
		this.defaultPaths = new ArrayList<String>();
		this.defaultPaths = defaultPaths;
		this.carMap = DM.getServiceCodesMap("SELECT "+fv.MANUFACTURER_TABLE_BRAND + ", " + fv.MANUFACTURER_TABLE_CAR_ID + " FROM " + fv.MANUFACTURER_TABLE );//+ " ORDER BY " + fv.MANUFACTURER_TABLE_BRAND + " ASC");
		this.carsList = new String[carMap.size()];	
		Set<String> t = carMap.keySet();
		this.carsList = t.toArray(carsList);
		Arrays.sort(carsList);
//		this.listOfCustomers = new ArrayList<Customer>();

		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 51, 0));	
		frame.setBackground(new Color(255, 255, 0));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 475, 380);
		frame.setLocation(10, 10);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Dodaj Klienta");
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 11, 760, 26);
		frame.getContentPane().add(lblTitle);
		
		JLabel lblMarkaAuta = new JLabel("Marka auta");
		lblMarkaAuta.setBackground(Color.ORANGE);
		lblMarkaAuta.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblMarkaAuta.setBounds(10, 59, 125, 20);
		frame.getContentPane().add(lblMarkaAuta);
		
		JComboBox cbCars = new JComboBox(carsList);
		cbCars.setBounds(156, 48, 250, 36);
		cbCars.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbCars ){
					JComboBox cb = (JComboBox) a.getSource();
					selectedCar = (cb.getSelectedItem().toString());
					index = carMap.get(selectedCar);
					System.out.println("S:" + selectedCar + " " + index);				
				}		
			}
		});

		frame.getContentPane().add(cbCars);

		
		textField = new JTextField();
		textField.setBounds(155, 90, 250, 24);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblRejestracja = new JLabel("Rejestracja");
		lblRejestracja.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblRejestracja.setBackground(Color.ORANGE);
		lblRejestracja.setBounds(10, 90, 125, 20);
		frame.getContentPane().add(lblRejestracja);
		
		JLabel lblFirma = new JLabel("Firma");
		lblFirma.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblFirma.setBackground(Color.ORANGE);
		lblFirma.setBounds(10, 126, 125, 20);
		frame.getContentPane().add(lblFirma);
		
		JCheckBox chbIsCompany = new JCheckBox("");
		chbIsCompany.setBounds(155, 126, 24, 24);
		frame.getContentPane().add(chbIsCompany);
		
		JLabel lblCompanyAddress = new JLabel("Adres");
		lblCompanyAddress.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblCompanyAddress.setBackground(Color.ORANGE);
		lblCompanyAddress.setBounds(10, 244, 125, 20);
		frame.getContentPane().add(lblCompanyAddress);
		
		JLabel lblVatTax = new JLabel("VAT / TAX#");
		lblVatTax.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblVatTax.setBackground(Color.ORANGE);
		lblVatTax.setBounds(10, 208, 125, 20);
		frame.getContentPane().add(lblVatTax);
		
		JLabel lblCompanyName = new JLabel("Nazwa firmy");
		lblCompanyName.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblCompanyName.setBackground(Color.ORANGE);
		lblCompanyName.setBounds(10, 177, 125, 20);
		frame.getContentPane().add(lblCompanyName);
		
		tfCompanyName = new JTextField();
		tfCompanyName.setColumns(10);
		tfCompanyName.setBounds(155, 178, 250, 24);
		frame.getContentPane().add(tfCompanyName);
		
		tfVatTax = new JTextField();
		tfVatTax.setColumns(10);
		tfVatTax.setBounds(155, 209, 250, 24);
		frame.getContentPane().add(tfVatTax);
		
		tfAddress = new JTextField();
		tfAddress.setColumns(10);
		tfAddress.setBounds(155, 245, 250, 24);
		frame.getContentPane().add(tfAddress);
		
		JButton btnZapisz = new JButton("Zapisz");
		btnZapisz.setBackground(new Color(255, 215, 0));
		btnZapisz.setForeground(Color.BLUE);
		btnZapisz.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnZapisz.setBounds(261, 307, 89, 23);
		frame.getContentPane().add(btnZapisz);
	
		btnZapisz.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				varEmpty = getVariableForQuery();
				if(!varEmpty)
					zapiszKlienta();
			}
		});	
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				CustomersWindow.main(defaultPaths);
			}
		});
		btnBack.setBounds(360, 307, 89, 23);
		frame.getContentPane().add(btnBack);
	}

	protected boolean getVariableForQuery() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void zapiszKlienta() {
		// TODO Auto-generated method stub
		
	}
}
