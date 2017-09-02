package dbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class DatabaseManager {
	private Connection dbConn = null;
	public DatabaseManager () {
		
	}
	Connection conn = null;
	public Connection getConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:D:\\@Development\\EclipseJavaProjects\\sqliteTestApp\\dbase\\theDBase.sqlite");
			JOptionPane.showMessageDialog(null, "Connected!");
			return conn;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: "+ex);
			return null;		
		}		
	}
	
	
	public boolean addNewRecord(){
		dbConn = this.getConnection();
		System.out.println("adding new record");
		return false;
	}
	
	public boolean deleteRecord(String table, String where){
		dbConn = this.getConnection();
		System.out.println("deleting record");
		return false;
	}
	
	public boolean editRecord(String table, String newValue, String where){
		dbConn = this.getConnection();
		System.out.println("editing record");
		return false;
	}
	
	public boolean selectRecord(String table, String column, String where){
		dbConn = this.getConnection();
		
		
		System.out.println("select record");
		return false;
	}
	
	public boolean selectRecordWithSQL(String query) throws SQLException{
		ArrayList<String> resultArray = new ArrayList<String>();
		
		dbConn = this.getConnection();
		PreparedStatement pst = dbConn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();   
		
//		System.out.println("select with SQL record: " + query + " / " + columnsNumber);
//		if(!rs.next())
//			return false;
		
		while(rs.next()) {
			resultArray.add(rs.toString());
			
			for(int i = 1 ; i <= columnsNumber; i++){
				if(rsmd.getColumnLabel(i).compareTo("salt") == 0 || rsmd.getColumnLabel(i).compareTo("password") == 0){
				} else {
			      System.out.print(rsmd.getColumnLabel(i)+" - " + rs.getString(i) + " "); //Print one element of a row
				}
			}

			  System.out.println();//Move to the next line to print the next row.           

		}
//		System.out.println("TRUE!");
		return true;
	}
}
