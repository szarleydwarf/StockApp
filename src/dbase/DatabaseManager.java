package dbase;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import hct_speciale.Item;
import hct_speciale.StockItem;

public class DatabaseManager {
	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	
	public DatabaseManager () {
		
	}
	public Connection connect() {
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
	
	
	public boolean addNewRecord(String query) {// throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(this.conn == null)
			conn = this.connect();
		try {
			conn.createStatement().execute("PRAGMA locking_mode = PENDING");
		} catch (SQLException e) {
			System.out.println("E "+e.getMessage());
		}
		
		try {
			conn.setAutoCommit(false);
			conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
			pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int inserted = pst.executeUpdate();
			
			rs = pst.getGeneratedKeys();
			if(inserted != 1){
				this.conn.rollback();
			} 
			
			conn.commit();
			
			if(inserted != 0)
				return true;

		} catch (SQLException e1) {
			try{
				if(this.conn != null){
					this.conn.rollback();
				}
			} catch ( SQLException e2){
				System.out.println("E2 "+e2.getMessage());
			}
			System.out.println("E1 "+e1.getMessage());
		}	finally {
			try{
	               if (rs != null) {
	                    rs.close();
	                }
	                if (pst != null) {
	                    pst.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
			} catch (Exception e3){
				System.out.println("E3 "+e3.getMessage());
			}
		}
		return false;
	}
	
	public boolean deleteRecord(String query) throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(this.conn == null)
			conn = this.connect();
			try {
				conn.createStatement().execute("PRAGMA locking_mode = PENDING");
			} catch (SQLException e) {
				System.out.println("E "+e.getMessage());
			}
			
			try {
				conn.setAutoCommit(false);
				conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
				pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
				int inserted = pst.executeUpdate();
				
				rs = pst.getGeneratedKeys();
				if(inserted != 1){
					this.conn.rollback();
				} 
				conn.commit();
						
				if(inserted != 0)
					return true;
			
		} catch (SQLException e1) {
			try{
				if(this.conn != null){
					this.conn.rollback();
				}
			} catch ( SQLException e2){
				System.out.println("E2 "+e2.getMessage());
			}
			System.out.println("E1 "+e1.getMessage());
		}	finally {
			try{
	               if (rs != null) {
	                    rs.close();
	                }
	                if (pst != null) {
	                    pst.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
			} catch (Exception e3){
				System.out.println("E3 "+e3.getMessage());
			}
		}
		return false;
	}
	
	public boolean editRecord(String table, String newValue, String where) throws SQLException{
		conn = this.connect();
		System.out.println("editing record");
		return false;
	}
	
	public Item getOneItem(String query){
		conn = this.connect();
		Item item = null;
		String qr = " LIMIT 1";
		if(!query.contains(qr.toLowerCase()) && !query.contains(qr))
			query+=qr;
		
		try {
			PreparedStatement pst = conn.prepareStatement(query);		
			ResultSet rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()){
				item = createItem(rs, columnsNumber);// 
				item.print();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
				rs.close();
				pst.close();
			} catch (Exception e){}
		}
		return item;
	}
	
	private Item createService(ResultSet rs, int columnsNumber) throws NumberFormatException, SQLException {
		String  stNum = "", itName = "";
		double cost = 0, price = 0;
		for(int i = 1 ; i <= columnsNumber; i++){			
			if(!rs.getString(i).isEmpty()) {
				switch(i){
				case 1:
					stNum = rs.getString(i);
					break;
				case 2:
					itName = rs.getString(i);
					break;
				case 3:
					cost = Double.parseDouble(rs.getString(i));
					break;
				case 4:
					price = Double.parseDouble(rs.getString(i));
					break;
				}
			}
		}
		return new Item(stNum, itName, cost, price);
	}
	
	
	public ArrayList<Item> getItemsList(String query){
		conn = this.connect();
		ArrayList<Item> list = new ArrayList<Item>();
		boolean isItem = false;
		if(query.contains("stock")){
			isItem = true;
		} else if(query.contains("services")){
			isItem = false;
		}
//		System.out.println(query);
		try {
			PreparedStatement pst = conn.prepareStatement(query);		
			
			ResultSet rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()){
				Item i = null;
				if(isItem)
					i = createItem(rs, columnsNumber);
				else if(!isItem)
					i = createService(rs, columnsNumber);
//				i.print();
				list.add(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
				rs.close();
				pst.close();
			} catch (Exception e){
				
			}
		}
		return list;
	}
	private StockItem createItem(ResultSet rs, int columnsNumber) throws NumberFormatException, SQLException {
		String  stNum = "", itName = "";
		double cost = 0, price = 0;
		int qnt = 0;
		for(int i = 1 ; i <= columnsNumber; i++){
//			System.out.println(i + " "+ rs.getString(i));
			if(!rs.getString(i).isEmpty()) {
				switch(i){
				case 1:
					stNum = rs.getString(i);
					break;
				case 2:
					itName = rs.getString(i);
					break;
				case 3:
					cost = Double.parseDouble(rs.getString(i));
					break;
				case 4:
					price = Double.parseDouble(rs.getString(i));
					break;
				case 5:
					qnt = Integer.parseInt(rs.getString(i));
					break;
				}
			}
		}
		return new StockItem(stNum, itName, cost, price, qnt);
		}
	
	public int getLastInvoiceNumber(){
		String query = "SELECT invoice_number from invoice_list ORDER BY invoice_number DESC LIMIT 1";
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(this.conn == null)
			conn = this.connect();
			try {
				conn.createStatement().execute("PRAGMA locking_mode = PENDING");
			} catch (SQLException e) {
				System.out.println("E "+e.getMessage());
			}
			
			try {
				conn.setAutoCommit(false);
				conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
				pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
				rs = pst.executeQuery();
				
				while(rs.next()){
					
//					System.out.println("inv db "+rs.getInt("invoice_number"));
					return rs.getInt(1);
				}
			} catch (SQLException e1) {
				try{
					if(this.conn != null){
						this.conn.rollback();
					}
				} catch ( SQLException e2){
					System.out.println("E2 "+e2.getMessage());
				}
				System.out.println("E1 "+e1.getMessage());
			}	finally {
				try{
		               if (rs != null) {
		                    rs.close();
		                }
		                if (pst != null) {
		                    pst.close();
		                }
		                if (conn != null) {
		                    conn.close();
		                }
				} catch (Exception e3){
					System.out.println("E3 "+e3.getMessage());
				}
			}
		return 0;
	}
	
	public ResultSet selectRecord(String query) throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(this.conn == null)
			conn = this.connect();
			try {
				conn.createStatement().execute("PRAGMA locking_mode = PENDING");
			} catch (SQLException e) {
				System.out.println("E "+e.getMessage());
			}
			
			try {
				conn.setAutoCommit(false);
				conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
				pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
				rs = pst.executeQuery();
				
//				rs = pst.getGeneratedKeys();
//				if(rs == null){
//					this.conn.rollback();
//				} 
//				conn.commit();
						
//				if(rs != null)
					return rs;
			
		} catch (SQLException e1) {
			try{
				if(this.conn != null){
					this.conn.rollback();
				}
			} catch ( SQLException e2){
				System.out.println("E2 "+e2.getMessage());
			}
			System.out.println("E1 "+e1.getMessage());
		}	finally {
			try{
//	               if (rs != null) {
//	                    rs.close();
//	                }
	                if (pst != null) {
	                    pst.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
			} catch (Exception e3){
				System.out.println("E3 "+e3.getMessage());
			}
		}
		return null;
	}	
	
	public ArrayList<String> selectRecordArrayList(String query){// throws SQLException{
		conn = this.connect();
		ArrayList<String> resultList = new ArrayList<String>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()){
				for(int i = 1 ; i <= columnsNumber; i++){
//					if(rsmd.getColumnLabel(i).compareTo("service_name") == 0){
						resultList.add(rs.getString(i));
//						System.out.println("selectRecord "+rsmd.getColumnLabel(i)+" - " + rs.getString(i) + " "); //Print one element of a row
//					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
				rs.close();
				pst.close();
			} catch (Exception e){
				
			}
		}
		
		return resultList;
	}
	
	public Map<String, String> selectRecord(String table, String column, Map <String, String> where){// throws SQLException{
		conn = this.connect();
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
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();		
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();   
			
			while(rs.next()) {			
				for(int i = 1 ; i <= columnsNumber; i++){
					if(rsmd.getColumnLabel(i).compareTo("salt") == 0 || rsmd.getColumnLabel(i).compareTo("password") == 0|| rsmd.getColumnLabel(i).compareTo("cost") == 0){
					} else {
						if(i%3==0)
							System.out.println();
						toReturn.put(rsmd.getColumnLabel(i),rs.getString(i));
//						System.out.print("selectRecord "+rsmd.getColumnLabel(i)+" - " + rs.getString(i) + " "); //Print one element of a row
					}
				}
//				System.out.println("\n");//Move to the next line to print the next row.          
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try{
				rs.close();
				pst.close();
			} catch (Exception e){
				
			}
		}
	
		return toReturn;
	}
	
	public boolean selectRecordWithSQL(String query) {//throws SQLException{
		conn = this.connect();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
				rs.close();
				pst.close();
			} catch (Exception e){
				
			}
		}
		return true;
	}


	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{
	               if (rs != null) {
	                    rs.close();
	                }
	                if (pst != null) {
	                    pst.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
			} catch (Exception e3){
				System.out.println("E3 "+e3.getMessage());
			}
		}
	}
	public boolean editRecord(String query) {
		conn = this.connect();
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(this.conn == null)
			conn = this.connect();
		try {
			conn.createStatement().execute("PRAGMA locking_mode = PENDING");
		} catch (SQLException e) {
			System.out.println("E "+e.getMessage());
		}
		
		try {
			conn.setAutoCommit(false);
			conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
			pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int inserted = pst.executeUpdate();
			
			rs = pst.getGeneratedKeys();
			if(inserted != 1){
				this.conn.rollback();
			} 
			
			conn.commit();
			
			if(inserted != 0)
				return true;

		} catch (SQLException e1) {
			try{
				if(this.conn != null){
					this.conn.rollback();
				}
			} catch ( SQLException e2){
				System.out.println("E2 "+e2.getMessage());
			}
			System.out.println("E1 "+e1.getMessage());
		}	finally {
			try{
	               if (rs != null) {
	                    rs.close();
	                }
	                if (pst != null) {
	                    pst.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
			} catch (Exception e3){
				System.out.println("E3 "+e3.getMessage());
			}
		}
		return false;
		}
}
