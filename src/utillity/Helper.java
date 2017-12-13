package utillity;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import dbase.DatabaseManager;

public class Helper {
	public final String PIRELLI_ST = "Pirelli";
	private FinalVariables fv;
	private DatabaseManager dm;
	
	public Helper() {
		this.fv = new FinalVariables();
	}
	
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
		
	public double getSumDiscounted(double sum, double discount, boolean applyDiscount){
	
		if(applyDiscount){
			return sum - discount;
		} else if(!applyDiscount){
			return sum - (sum * (discount/100));
		} else {
			return sum;
		}
	}
		
	public int checkInteger(String msg1, String msg2, String number){
		String pattern = this.fv.INTEGER_PATTERN; 

		Pattern patern, dPattern;
		Matcher match, dMatch;
		patern = Pattern.compile(pattern);

		if(number.isEmpty()) {
			JOptionPane.showMessageDialog(null, msg1);
			return 0;
		}else {
			match = patern.matcher(number);
			if(match.find())
				return Integer.parseInt(number);
			else{
				JOptionPane.showMessageDialog(null, msg2);
				return 0;
			}
		}
	}
	
	public double checkDouble(String msg1, String msg2, String number){
		DecimalFormat df;
		df = new DecimalFormat(this.fv.DECIMAL_FORMAT); 

		String decimalPattern = this.fv.DECIMAL_PATTERN;

		Pattern patern, dPattern;
		Matcher match, dMatch;
		dPattern = Pattern.compile(decimalPattern);
		
		if(number.isEmpty()) {
			JOptionPane.showMessageDialog(null, msg1);
			return 0;
		}else {
			dMatch = dPattern.matcher(number);
			if(dMatch.find())
				return Double.parseDouble(df.format(Double.parseDouble(number)));
			else{
				JOptionPane.showMessageDialog(null, msg2);
				return 0;
			}
		}}

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

	public String getFormatedDate(){
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df= new SimpleDateFormat(this.fv.DATE_FORMAT);
		
		return df.format(today.getTime());
	}

	public int getDayOfMonthNum(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public int getMonthNum(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}

	public int getYearNum(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public int getYearIndex() {
		String year = ""+getYearNum();
		int index = 0;
		for(int i = 0; i < fv.YEARS_NO_STRING.length; i++){
			if(fv.YEARS_NO_STRING[i].equals(year)){
				return i;
			}
		}
		return index;
	}
	
	public boolean isLeapYear(int year){
        if(year % 4 == 0)
        {
            if( year % 100 == 0)
            {
                if ( year % 400 == 0)
                    return true;
                else
                	return false;
            }
            else
            	return true;
        }
        else {
        	return false;
        }
	}
	
	public boolean isLeapYear(){
		int year = this.getYearNum();
        if(year % 4 == 0)
        {
            if( year % 100 == 0)
            {
                if ( year % 400 == 0)
                    return true;
                else
                	return false;
            }
            else
            	return true;
        }
        else {
        	return false;
        }
	}

	public boolean createFolderIfNotExist (String path) {
//		System.out.println("helper Folder path: "+path);
		
		File dir = new File(path);
		if(!dir.exists()){
			return dir.mkdir();
		}
		return false;
	}
	
	public boolean createFileIfNotExist(String fileName){
//		System.out.println("Filename: "+fileName);
		File file = new File(fileName);
	    if (!file.exists()) {
	    	try {
				return file.createNewFile();
			} catch (IOException e) {
				// TODO Add logger :P
				e.printStackTrace();
			}
	    }
	    return false;
	}

	public String getIntFromStNo(String string, char char2find) {
	
		String stL = string.substring(0, string.lastIndexOf(char2find)+1);
		int stN = Integer.parseInt(string.replaceAll("[\\D]", ""));//((int)stockNum.lastIndexOf("A")+1);
		stN++;
		String temp = paddStringLeft(Integer.toString(stN), 2, '0');
		temp = stL + temp;
		return temp;
	}

	public int getLength(int testLength, int stLength, int min, int addMin, int addMax) {
		if(testLength <= min)
			return stLength+addMax;
		else 
			return stLength+addMin;
	}

	public void toggleJButton(JButton btn_references, Color background_color, Color foreground_color, boolean toggle) {
		btn_references.setForeground(foreground_color);
		btn_references.setBackground(background_color);
		btn_references.setEnabled(toggle);

	}

	public boolean databaseBackUp(String source, String dest) throws IOException{
		try {
//            System.out.println("Reading..." + source+"\n"+dest);
            File sourceFile = new File(source);
            File destinationFile = new File(dest);
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destinationFile);
 
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
            return true;
//            System.out.println("Copied: " + source);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
	}

	public boolean checkDatesOfLastBackup(){
		this.dm = new DatabaseManager(this.fv.LOGGER_FOLDER_NAME);
		String query = "SELECT "+this.fv.SETTINGS_TABLE_PATH+" FROM "+this.fv.SETTINGS_TABLE+" WHERE "+this.fv.ROW_ID+"="+this.fv.SETTINGS_TABLE_LAST_DATABASE_BACKUP+"";
		String today = this.getFormatedDate();
		String lastBackup = this.dm.getOneField(query);

		return this.compareDates(lastBackup, today);
	}
	
	public boolean compareDates(String oldDate, String newDate){
		char find = '-';
		int nDD = Integer.parseInt(newDate.substring(0, newDate.indexOf(find)));
		int nMM = Integer.parseInt(newDate.substring(newDate.indexOf(find)+1, newDate.indexOf(find)+3));
		int nYYYY = Integer.parseInt(newDate.substring(newDate.indexOf(find)+4));

		int oDD = Integer.parseInt(oldDate.substring(0, oldDate.indexOf(find)));
		int oMM = Integer.parseInt(oldDate.substring(oldDate.indexOf(find)+1, oldDate.indexOf(find)+3));
		int oYYYY = Integer.parseInt(oldDate.substring(oldDate.indexOf(find)+4));
		
		if((nYYYY == oYYYY) && (nMM == oMM) && (nDD == oDD)){
			return true;
		}		
		return false;
	}

	public String[] getDaysArray() {
		int month = getMonthNum();
		month++;

		switch(month){
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return fv.DAYS_NUM_31;
	
			case 4:
			case 6:
			case 9:
			case 11:
				return fv.DAYS_NUM_30;
	
			case 2:
				boolean isLeap = isLeapYear();
				if(isLeap)
					return fv.DAYS_NUM_29;
				else
					return fv.DAYS_NUM_28;
	
			default:
				return fv.DAYS_NUM_31;
		}
	}

	
	public double getSumDouble(HashMap<String, Double> map, String[] tokens) {
		double sum = 0;
		for (String token : tokens) {
			int q = Integer.parseInt(token.substring(0, token.indexOf("A")-1));
			token = token.substring(token.indexOf("A"));
			System.out.println("helper q: "+q+" / "+token);
			double d = 0;
			if(map.containsKey(token)) {
				d = map.get(token);
			} 
			sum += d;
		}
		return sum;
	}

	
	public String getSumString(HashMap<String, Double> map, String[] tokens) {
		DecimalFormat df = new DecimalFormat(this.fv.DECIMAL_FORMAT); 
		double sum = 0;
		for (String token : tokens) {
			double d = 0;
			if(map.containsKey(token)) {
				d = map.get(token);
			} 
			sum += d;
		}
		return df.format(sum);
	}
/* Function copied from
	 * https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
	*/
	public static void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	
	public float pt2mmForWeb72dpi(float pt) {
	   return pt2mm(pt,72);
	}
	public float pt2mmForPrint300dpi(float pt) {
	   return pt2mm(pt,300);
	}
	public float pt2mmForPrint600dpi(float pt) {
	   return pt2mm(pt,600);
	}
	public float pt2mm(float pt, float dpi) {
	   return pt * 25.4f / dpi;
	}
}
