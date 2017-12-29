package hct_speciale;

import utillity.Helper;

public class Customer {
	private String id, carId, details;
	private boolean isBusiness;
	
	private Helper helper;
	public Customer(String p_id, String p_carId, String p_details, boolean p_isBusiness){
		this.id = p_id;
		this.carId = p_carId;
		this.details = p_details;
		this.isBusiness = p_isBusiness;
		
		this.helper = new Helper();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public void print() {
		System.out.println("["+this.id+" - "+this.carId+" - "+this.details+"]");
	}
	public boolean isBusiness() {
		return isBusiness;
	}
	public void setBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}
}
