package utillity;

import javax.swing.DefaultListModel;

public class Helper {
	public double getSum(DefaultListModel md, double discount, boolean applyDiscount){
		double sum = 0;
		if(md.size() > 0){
			for(int i = 0; i < md.size(); i++){
				String tempSt = md.getElementAt(i).toString();
				String subSt = tempSt.substring(tempSt.lastIndexOf("€")+1);
				subSt = subSt.substring(0, subSt.lastIndexOf("x"));
				String quant = tempSt.substring(tempSt.lastIndexOf("x")+1);
				double q = 0;
				if(!quant.isEmpty())
					q = Double.parseDouble(quant);
				if(q == 0){
					q = 1;
				}
				sum += (Double.parseDouble(subSt)*q);	
			}
		}
		
		if(applyDiscount){
			sum -= discount;
		} else if(!applyDiscount){
			sum -= (sum * (discount/100));
		} else {
			sum = sum;
		}
		return sum;
	}
}
