package dbase;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DatabaseManager {
	private Connection dbConn = null;
	public DatabaseManager () {
		
	}
	Connection conn = null;
	public Connection getConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:D:\\@Development\\EclipseJavaProjects\\sqliteTestApp\\StockApp\\dbase\\theDBase.sqlite");
//			JOptionPane.showMessageDialog(null, "Connected!");
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
	
	public ResultSet selectRecord(String query) throws SQLException{
		dbConn = this.getConnection();
		PreparedStatement pst = dbConn.prepareStatement(query);		
		return pst.executeQuery();		
	}
	
	public Map<String, String> selectRecord(String table, String column, Map <String, String> where) throws SQLException{
		dbConn = this.getConnection();
		Map<String,String> toReturn = new HashMap<String, String>();
		String query = "select "+column+" from "+table;
		String queryadd = " WHERE";
		String queryOpp = " AND";

		if(!where.isEmpty()){
			query=query+queryadd;
			for(String key : where.keySet()) {
				query=query + " " + key + "=\""+where.get(key)+"\"";
				query=query+queryOpp;
			}
			query = query.replaceAll(" \\S*$", "");
		}
//		System.out.println("selectRecord query2 "+query);
		PreparedStatement pst = dbConn.prepareStatement(query);
	
		ResultSet rs = pst.executeQuery();		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();   
		
		while(rs.next()) {			
			for(int i = 1 ; i <= columnsNumber; i++){
				if(rsmd.getColumnLabel(i).compareTo("salt") == 0 || rsmd.getColumnLabel(i).compareTo("password") == 0|| rsmd.getColumnLabel(i).compareTo("cost") == 0){
				} else {
					if(i%3==0)
						System.out.println();
					toReturn.put(rsmd.getColumnLabel(i),rs.getString(i));
//					System.out.print("selectRecord "+rsmd.getColumnLabel(i)+" - " + rs.getString(i) + " "); //Print one element of a row
				}
			}
//			System.out.println("\n");//Move to the next line to print the next row.          
		}
		return toReturn;
	}
	
	public boolean selectRecordWithSQL(String query) throws SQLException{
		dbConn = this.getConnection();
		PreparedStatement pst = dbConn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();   
		
		while(rs.next()) {			
			for(int i = 1 ; i <= columnsNumber; i++){
				if(rsmd.getColumnLabel(i).compareTo("salt") == 0 || rsmd.getColumnLabel(i).compareTo("password") == 0){
				} else {
					if(i%3==0)
						System.out.println();
					
					System.out.print(rsmd.getColumnLabel(i)+" - " + rs.getString(i) + " "); //Print one element of a row
				}
			}
			System.out.println("\n");//Move to the next line to print the next row.          
		}
		return true;
	}


	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
