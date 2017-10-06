package hct_speciale;

import java.util.Calendar;

import utillity.Helper;

public class Invoice {
	private int invoiceNumber;
	private String customerName;
	private String serviceNumber;
	private String itemNumber;
	private String invoiceDateString;
	private double total;
	
//	private Calendar invoiceDate; 
	private char paddingChar = ' ';
	private int stringLength = 42;
	
	private Helper helper;
	
	public Invoice(int p_invoice_number, String p_customer_name, String p_service_number, String p_item_number, String p_invoice_date, double p_total){
		this.invoiceNumber = p_invoice_number;
		this.customerName = p_customer_name;
		this.serviceNumber = p_service_number;
		this.itemNumber = p_item_number;
		this.invoiceDateString = p_invoice_date;
		this.total = p_total;


		this.helper = new Helper();
	}
	
	@Override
	public String toString(){
		String str = "";
		
		str = this.invoiceNumber+" - "+this.customerName+" - "+this.serviceNumber+" - "+this.itemNumber+" - "+this.total+" - "+this.invoiceDateString;
		return str;
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

	public void print() {
		System.out.println(this.toString());
	}
}
