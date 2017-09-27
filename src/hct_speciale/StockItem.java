package hct_speciale;

public class StockItem extends Item{

	private int qnt;
	
	
	public StockItem(String p_stock_number, String p_name, double p_cost, double p_price, int p_qnt) {
		super(p_stock_number, p_name, p_cost, p_price);
		this.qnt = p_qnt;
	}
	public int getQnt() {
		return qnt;
	}
	public void setQnt(int qnt) {
		this.qnt = qnt;
	}
	public void print(){
		System.out.println(getStockNumber()+ " "+this.getName()+" "+this.getCost()+" "+this.getPrice()+" "+this.qnt);
	}
	
	@Override
	public String toString(){
		String toReturn = "";
		int paddLng = getStringLength();
		if(this.getName().contains(this.getHelper().PIRELLI_ST))
			paddLng+=3;
		toReturn = getHelper().paddStringRight(this.getName(), paddLng , getPaddingChar());		
		
		toReturn += this.getCost();
		System.out.println("1: "+paddLng+" / "+toReturn.length()+" / "+this.getName());	
		paddLng = getHelper().getLength(Double.toString(this.getCost()).length(), toReturn.length(), 3, 12, 18);
	
		toReturn = this.getHelper().paddStringRight(toReturn, paddLng, getPaddingChar());

		
		toReturn += this.getPrice();
		paddLng = getHelper().getLength(Double.toString(this.getPrice()).length(), toReturn.length(), 3, 12, 18);
//System.out.println("2: "+paddLng+" / "+toReturn.length()+" / ");
		
		toReturn = this.getHelper().paddStringRight(toReturn, paddLng, getPaddingChar());
		toReturn += this.qnt;
//		
//System.out.println("3: "+paddLng+" / "+toReturn.length()+" / ");
		return toReturn;
	}
}
