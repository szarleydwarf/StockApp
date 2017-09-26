package hct_speciale;

import java.util.ArrayList;

public class ItemList {
	private ArrayList<Item> itemList;
	public ItemList(){
		itemList = new ArrayList<Item>();
	}
	
	public void addItem(Item item) {
		this.itemList.add(item);
	}
	
	public void removeItem(Item item){
		this.itemList.remove(item);
	}
	public void removeItemAtIndex(int index){
		this.itemList.remove(index);
	}
	public void clearList(){
		this.itemList.clear();
	}
	public void getItem(int index){
		this.itemList.get(index);
	}
	public int getSize(){
		return this.itemList.size();
	}
}
