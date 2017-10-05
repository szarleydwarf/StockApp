package dbase;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

import utillity.FinalVariables;

public class UserManager {
	private DatabaseManager DM = null;
	private EncryptionClass EC = null;
	private FinalVariables fv = null;
	
	public boolean login(String username, String password) throws SQLException{
		DM = new DatabaseManager();
		EC = new EncryptionClass();
		fv = new FinalVariables();
		String passEncoded = "";
		String salt = "";
		boolean isLogingin = false;
		
		String saltQuery = "Select salt from "+this.fv.USERS_TABLE+" where nick=\""+username+"\"";
		ResultSet rs = DM.selectRecord(saltQuery);
		int i=0;
		while(rs.next()) {
			i++;	
			salt = rs.getString("salt");
		}
//		System.out.println(i);
		
		if(i<=0){
			JOptionPane.showMessageDialog(null, "Podany uzytkownik nie istnieje w bazie danych.");
		}
		
		String pass = salt+password;
		try {
			SecretKey sk = EC.getSKey();
			passEncoded = EC.encrypt(pass, sk);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String query = "select * from "+this.fv.USERS_TABLE+" where nick=\""+username+"\" and password=\""+passEncoded+"\" ";
//		System.out.println(query);
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
//		pst.close();
		return isLogingin;
	}
}
