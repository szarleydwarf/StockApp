package hct_speciale;

import utillity.Helper;

public class Item {
	
	private String stockNumber;
	private String name;
	private char paddingChar = ' ';
	private double cost;
	private double price;
	private int stringLength = 38;
	
	private Helper helper;
	
	public Item(String p_stock_number, String p_name, double p_cost, double p_price){
		this.stockNumber = p_stock_number;
		this.name = p_name;
		this.cost = p_cost;
		this.price = p_price;
		
		this.helper = new Helper();
	}


	public int getStringLength() {
		return stringLength;
	}

	public Helper getHelper() {
		return helper;
	}

	public String getStockNumber() {
		return stockNumber;
	}


	public void setStockNumber(String stockNumber) {
		this.stockNumber = stockNumber;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getCost() {
		return cost;
	}


	public void setCost(double cost) {
		this.cost = cost;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}
	
	public void print(){
		System.out.println(stockNumber+ " "+this.name+" "+this.cost+" "+this.price);
	}
	
	@Override
	public String toString(){
		String toReturn = "";
		toReturn = helper.paddStringRight(this.name, stringLength/3, paddingChar);
		System.out.println(toReturn);
		toReturn += this.helper.paddStringRight(toReturn, stringLength/2, paddingChar);
		System.out.println(toReturn);
		toReturn = this.name+" "+this.cost+" "+this.price;
		return toReturn;
	}


	public char getPaddingChar() {
		return paddingChar;
	}
}
