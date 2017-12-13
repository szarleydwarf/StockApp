package dbase;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;

public class UserManager {
	private DatabaseManager DM = null;
	private EncryptionClass EC = null;
	private FinalVariables fv = null;
	protected static String date;
	protected static String loggerFolderPath;
	private static Logger log;
	private static Helper helper;
	
	public boolean login(String username, String password, String p_loggerFolderPath) throws SQLException{
		fv = new FinalVariables();
		loggerFolderPath = p_loggerFolderPath;
		log = new Logger(loggerFolderPath);
		helper = new Helper();
		date = helper.getFormatedDate();

		DM = new DatabaseManager(loggerFolderPath);
		EC = new EncryptionClass(loggerFolderPath);

//		this.createNewUser();
		String passEncoded = "";
		String salt = "";
		boolean isLogingin = false;
		
		String saltQuery = "Select \"salt\" from \""+this.fv.USERS_TABLE+"\" where \"nick\"=\""+username+"\"";
		ResultSet rs = DM.selectRecord(saltQuery);
//		System.out.println(saltQuery+"\n"+rs.getString(1));
		
		int i=0;
		while(rs.next()) {
			i++;	
			salt = rs.getString("salt");
//			System.out.println("salt "+salt);
		}
	
		if(i<=0){
			JOptionPane.showMessageDialog(null, "Podany uzytkownik nie istnieje w bazie danych.");
		}
		
		String pass = salt+password;
		try {
			SecretKey sk = EC.getSKey();
			passEncoded = EC.encrypt(pass, sk);
		} catch (Exception e) {
			log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
		}
		
		
		String query = "select * from "+this.fv.USERS_TABLE+" where nick=\""+username+"\" and password=\""+passEncoded+"\" ";
		System.out.println(query);
		ResultSet rsq = DM.selectRecord(query);
		int count = 0;
		while(rsq.next()) {
			count++;			
		}
//		System.out.println("count "+count);
		if(count == 1){
			JOptionPane.showMessageDialog(null, "Loguje ");
			isLogingin = true;
		} else if(count > 1) {
			JOptionPane.showMessageDialog(null, "ten uzytkownik juz istnieje - duplikacja");
		}else {
			JOptionPane.showMessageDialog(null, "Niepoprawna kombinacja nazwy uzytkownika i hasla.");
		}
		rs.close();
		DM.close();
		return isLogingin;
	}
	
	public void createNewUser(){
		String query = "INSERT INTO \"users\" VALUES('admin','tezrn6H#s7|89jd-M@voxEBI|@�*cW$I','EDORzQox5Kot57a7BlT3jkTiBDXhuFclXYnsBEUtZzmiSrabadAdtx8g8W67U9LO','Rad','Cho','HCT','0892422993','Killaneen','Ballinamore','Leitrim','Ireland','N41YK50');";
		System.out.println(query);
		String pass = "Pa55word1!";
		String pass2 = "Alex123!";
		try {
			this.EC.saltPassword(pass2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}