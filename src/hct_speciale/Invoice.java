package hct_speciale;

import java.io.IOException;
import java.util.ArrayList;

import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import utillity.StockPrinter;

public class Invoice {
	private int invoiceNumber;
	private String customerName;
	private String serviceNumber;
	private String itemNumber;
	private String invoiceDateString;
	private String filePathName;
	private double total;
	
//	private Calendar invoiceDate; 
	private char paddingChar = ' ';
	private int stringLength = 42;
	
	private StockPrinter sPrinter;
	private FinalVariables fv;
	private ArrayList<String> defaultPaths = new ArrayList<String>();
	
	protected static String date;
	protected static String loggerFolderPath;
	private static Logger log;
	private static Helper helper;
	
	public Invoice(int p_invoice_number, String p_customer_name, String p_service_number, String p_item_number, String p_invoice_date, String p_file_path_name, String p_loggerFolderPath, double p_total){
		this.invoiceNumber = p_invoice_number;
		this.customerName = p_customer_name;
		this.serviceNumber = p_service_number;
		this.itemNumber = p_item_number;
		this.invoiceDateString = p_invoice_date;
		this.filePathName = p_file_path_name;
		this.total = p_total;
		
		ArrayList<String> defaultPaths = new ArrayList<String>();
		defaultPaths.add(p_loggerFolderPath);
		this.sPrinter = new StockPrinter(defaultPaths);

		this.fv = new FinalVariables();
		loggerFolderPath = p_loggerFolderPath;
		log = new Logger(loggerFolderPath);
		helper = new Helper();
		date = helper.getFormatedDate();
	}
	
	public Invoice() {

	}

	@Override
	public String toString(){
		String str = "";
		
		str = this.invoiceNumber+" - "+this.customerName+" - "+this.serviceNumber+" - "+this.itemNumber+" - "+this.total+" - "+this.invoiceDateString+"; Plik: "+this.filePathName;
		return str;
	}
	
	public void print() {
		String invoicePath = this.fv.SAVE_FOLDER_DEFAULT_PATH+"/"+this.filePathName;
		System.out.println(invoicePath);
		try {
			this.sPrinter.printPDF(invoicePath);
		} catch (IOException e1) {
			log.logError(date+" "+this.getClass().getName()+"\tE1 "+e1.getMessage());
		} catch (Exception e2) {
			log.logError(date+" "+this.getClass().getName()+"\tE2 "+e2.getMessage());
			
		}
	}

	public String getFilePathName() {
		return filePathName;
	}

	public void setFilePathName(String filePathName) {
		this.filePathName = filePathName;
	}

	public int getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(int invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getInvoiceDateString() {
		return invoiceDateString;
	}

	public void setInvoiceDateString(String invoiceDateString) {
		this.invoiceDateString = invoiceDateString;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
}
