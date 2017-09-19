package utillity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	
	public String paddStringRight(String string2Padd, int stringLength, char paddingChar){
		if(stringLength <= 0)
			return string2Padd;
		
		StringBuilder sb = new StringBuilder(string2Padd);
		stringLength = stringLength - sb.length() - 1;
		while(stringLength-- >= 0){
			sb.append(paddingChar);
		}
		return sb.toString();
		
	}
	
	public String paddStringLeft(String string2Padd, int stringLength, char paddingChar){
		if(stringLength-string2Padd.length() <= 0)
			return string2Padd;
		
		StringBuilder sb = new StringBuilder();
		
		
//		stringLength = sb.length() + string2Padd.length();
		while(sb.length() <= stringLength){
			sb.append(paddingChar);
		}
		sb.append(string2Padd);
		return sb.toString();
		
	}

	
	public String getFormatedDate(){
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df= new SimpleDateFormat("dd-MM-yyyy");
		
		return df.format(today.getTime());

	}
	
	public boolean createFolderIfNotExist (String path) {
//		System.out.println("Folder path: "+path);
		
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdir();
		}
		return false;
	}
}
