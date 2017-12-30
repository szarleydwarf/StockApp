package hct_speciale;

import utillity.Helper;

public class Customer {
	private String id, carId, carRegistration, details;
	private boolean isBusiness;
	private int num_visits;
	
	private Helper helper;
	public Customer(String p_id, String p_carId, String p_carRegistration, String p_details, boolean p_isBusiness, int p_num_visits){
		this.id = p_id;
		this.carId = p_carId;
		this.setCarRegistration(p_carRegistration);
		this.details = p_details;
		this.isBusiness = p_isBusiness;
		this.setNum_visits(p_num_visits);
		
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
		System.out.println("["+this.id+" - "+this.carId+" - "+this.carRegistration+" - "+this.details+" - "+this.isBusiness+"]");
	}
	public boolean isBusiness() {
		return isBusiness;
	}
	public void setBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}
	public String getCarRegistration() {
		return carRegistration;
	}
	public void setCarRegistration(String carRegistration) {
		this.carRegistration = carRegistration;
	}
	public int getNumVisits() {
		return num_visits;
	}
	public void setNum_visits(int num_visits) {
		this.num_visits = num_visits;
	}
}
