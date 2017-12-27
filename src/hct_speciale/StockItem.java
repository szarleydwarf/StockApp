package hct_speciale;

public class StockItem extends Item{

	private int qnt;
	
	
	public StockItem(String p_stock_number, String p_name, double p_cost, double p_price, int p_qnt) {
		super(p_stock_number, p_name, p_cost, p_price);
		this.qnt = p_qnt;
	}
	public StockItem() {
		super("", "", 0.00, 0.00);
		// TODO Auto-generated constructor stub
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
		toReturn = super.toString();

		if(toReturn.length() < 95)
			paddLng = 95;
		else
			paddLng = toReturn.length()+10;

		toReturn = this.getHelper().paddStringRight(toReturn, paddLng, getPaddingChar());
		toReturn += this.qnt;
		
		return toReturn;
	}
}
