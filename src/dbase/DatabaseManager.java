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

import hct_speciale.Invoice;
import hct_speciale.Item;
import hct_speciale.StockItem;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;

public class DatabaseManager {
	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private FinalVariables fv;
	private String loggerFolderPath;
	private Logger log;
	private Helper helper;
	private String date;

	public DatabaseManager (String p_loggerFolderPath) {
		this.fv = new FinalVariables();
		loggerFolderPath = p_loggerFolderPath;
		log = new Logger(loggerFolderPath);
		helper = new Helper();
		date = helper.getFormatedDate();
	}
	
	public Connection connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+this.fv.DATABASE_DEFAULT_PATH);
//			JOptionPane.showMessageDialog(null, "Connected!");
			return conn;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: "+ex);
			log.logError(date+" "+this.getClass().getName()+"\t"+ex.getMessage());
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
			log.logError(date+" "+this.getClass().getName()+"\tAdd New Record\t"+e.getMessage());
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
				log.logError(date+" "+this.getClass().getName()+"\tAdd New Record\t"+e2.getMessage());
				System.out.println("E2 "+e2.getMessage());
			}
			System.out.println("E1 "+e1.getMessage());
			log.logError(date+" "+this.getClass().getName()+"\tAdd New Record\t"+e1.getMessage());
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
				log.logError(date+" "+this.getClass().getName()+"\tAdd New Record\t"+e3.getMessage());
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
				System.out.println("Delete Record E "+e.getMessage());
				log.logError(date+" "+this.getClass().getName()+"\tDelete Record\t"+e.getMessage());
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
				System.out.println("Delete Record E2 "+e2.getMessage());
				log.logError(date+" "+this.getClass().getName()+"\tDelete Record\t"+e2.getMessage());
			}
			System.out.println("Delete Record E1 "+e1.getMessage());
			log.logError(date+" "+this.getClass().getName()+"\tDelete Record\t"+e1.getMessage());
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
				System.out.println("Delete Record E3 "+e3.getMessage());
				log.logError(date+" "+this.getClass().getName()+"\tDelete Record\t"+e3.getMessage());
			}
		}
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
//			System.out.println("get one item E "+e.getMessage());
			log.logError(date+" "+this.getClass().getName()+"\tget one item\t"+e.getMessage());
		} finally {
			try{
				rs.close();
				pst.close();
			} catch (Exception e){
				log.logError(date+" "+this.getClass().getName()+"\tget one item\t"+e.getMessage());
			}
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
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<Item> list = new ArrayList<Item>();
		boolean isItem = false;
		if(query.contains(this.fv.STOCK_TABLE)){
			isItem = true;
		} else if(query.contains(this.fv.SERVICES_TABLE)){
			isItem = false;
		}
//		System.out.println(query);
		try {
			pst = conn.prepareStatement(query);		
			
			rs = pst.executeQuery();
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
		} catch (SQLException e1) {
			log.logError(date+" "+this.getClass().getName()+"\tGET ITEMS LIST\tE1 "+e1.getMessage());
		} finally {
			try{
				if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
			} catch (Exception e2){
				log.logError(date+" "+this.getClass().getName()+"\tGET ITEMS LIST\tE2 "+e2.getMessage());
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
	
	public String getOneField(String query) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		if(this.conn == null)
			conn = this.connect();
			try {
				conn.createStatement().execute("PRAGMA locking_mode = PENDING");
			} catch (SQLException e) {
				log.logError(date+" "+this.getClass().getName()+"\tGET PATH\t"+e.getMessage());
			}
			
			try {
				conn.setAutoCommit(false);
				conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
				pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
				rs = pst.executeQuery();
				
				while(rs.next()){
					return rs.getString(1);
				}
			} catch (SQLException e1) {
				try{
					if(this.conn != null){
						this.conn.rollback();
					}
				} catch ( SQLException e2){
					log.logError(date+" "+this.getClass().getName()+"\tGET PATH\t"+e2.getMessage());
				}
				log.logError(date+" "+this.getClass().getName()+"\tGET PATH\t"+e1.getMessage());
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
					log.logError(date+" "+this.getClass().getName()+"\tGET PATH\t"+e3.getMessage());
				}
			}
		return "";
	}
	
	public ArrayList<String> getPaths(String query) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		
		if(this.conn == null)
			conn = this.connect();
			try {
				conn.createStatement().execute("PRAGMA locking_mode = PENDING");
			} catch (SQLException e) {
				log.logError(date+" "+this.getClass().getName()+"\tGET PATHS\t"+e.getMessage());
			}
			
			try {
				conn.setAutoCommit(false);
				conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
				pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
				rs = pst.executeQuery();
				
				while(rs.next()){
					list.add(rs.getString(1));
				}
				return list;
			} catch (SQLException e1) {
				try{
					if(this.conn != null){
						this.conn.rollback();
					}
				} catch ( SQLException e2){
					log.logError(date+" "+this.getClass().getName()+"\tGET PATHS\t"+e2.getMessage());
				}
				log.logError(date+" "+this.getClass().getName()+"\tGET PATHS\t"+e1.getMessage());
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
					log.logError(date+" "+this.getClass().getName()+"\tGET PATHS\t"+e3.getMessage());
				}
			}
		return null;
	}
	
	public int getLastInvoiceNumber(){
		String query = "SELECT "+this.fv.INVOCE_TABLE_INVOICE_NUMBER+" from "+this.fv.INVOCE_TABLE+" ORDER BY "+this.fv.INVOCE_TABLE_INVOICE_NUMBER+" DESC LIMIT 1";
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(this.conn == null)
			conn = this.connect();
			try {
				conn.createStatement().execute("PRAGMA locking_mode = PENDING");
			} catch (SQLException e) {
				log.logError(date+" "+this.getClass().getName()+"\tGET LAST INVOICE NUMBER\t"+e.getMessage());
			}
			
			try {
				conn.setAutoCommit(false);
				conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
				pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
				rs = pst.executeQuery();
				
				while(rs.next()){
					return rs.getInt(1);
				}
			} catch (SQLException e1) {
				try{
					if(this.conn != null){
						this.conn.rollback();
					}
				} catch ( SQLException e2){
					log.logError(date+" "+this.getClass().getName()+"\tGET LAST INVOICE NUMBER\t"+e2.getMessage());
				}
				log.logError(date+" "+this.getClass().getName()+"\tGET LAST INVOICE NUMBER\t"+e1.getMessage());
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
					log.logError(date+" "+this.getClass().getName()+"\tGET LAST INVOICE NUMBER\t"+e3.getMessage());
				}
			}
		return 0;
	}
	

	public Map<String, String> getServiceCodesMap(String q) {
		Map<String, String> toReturn = new HashMap<String, String>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(conn == null)
			conn = this.connect();
		try {
			pst = conn.prepareStatement(q);
			rs = pst.executeQuery();		
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();   
			
			while(rs.next()) {			
				for(int i = 0 ; i <= columnsNumber; i++){
					if(i % 2 != 0){
						toReturn.put(rs.getString(i), rs.getString(i+1));
					}
				}        
			}
		} catch (SQLException e) {
			log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD MAP\t"+e.getMessage());
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(pst != null)
					pst.close();
			} catch (Exception e){
				log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD MAP\t"+e.getMessage());
			}
		}
		return toReturn;
	}

	
	public Map<String, Double> getAllCostsPrices(String query){
		Map<String, Double> toReturn = new HashMap<String, Double>();
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(conn == null)
			conn = this.connect();
		try {
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();		
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();   
			
			while(rs.next()) {			
				for(int i = 0 ; i <= columnsNumber; i++){
					if(i % 2 != 0){
						toReturn.put(rs.getString(i), rs.getDouble(i+1));
//						System.out.println("getAllCostsPrices "+ rs.getString(i) + " "+rs.getDouble(i+1)); 
					}
				}        
			}
		} catch (SQLException e) {
			log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD MAP\t"+e.getMessage());
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(pst != null)
					pst.close();
			} catch (Exception e){
				log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD MAP\t"+e.getMessage());
			}
		}

		return toReturn;
	}
	
	public ResultSet selectRecord(String query) throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(this.conn == null)
			conn = this.connect();
			try {
				conn.createStatement().execute("PRAGMA locking_mode = PENDING");
			} catch (SQLException e) {
				log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORDe \t"+e.getMessage());
			}
			
			try {
				conn.setAutoCommit(false);
				conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
				pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
				return pst.executeQuery();
			
		} catch (SQLException e1) {
			try{
				if(this.conn != null){
					this.conn.rollback();
				}
			} catch ( SQLException e2){
				log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD e2\t"+e2.getMessage());
			}
			log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD e1\t"+e1.getMessage());
		}	finally {
			try{
//                if (pst != null) {
//                    pst.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
			} catch (Exception e3){
				log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD e3\t"+e3.getMessage());
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
			log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD ARRAY LIST\t"+e.getMessage());
		} finally {
			try{
				rs.close();
				pst.close();
			} catch (Exception e){
				log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD ARRAY LIST\t"+e.getMessage());				
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
			log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD MAP\t"+e.getMessage());
		} finally {
			try{
				rs.close();
				pst.close();
			} catch (Exception e){
				log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD MAP\t"+e.getMessage());
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
			log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD WITH SQL\t"+e.getMessage());
		} finally {
			try{
				rs.close();
				pst.close();
			} catch (Exception e){
				log.logError(date+" "+this.getClass().getName()+"\tSELECT RECORD WITH SQL\t"+e.getMessage());
			}
		}
		return true;
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
			log.logError(date+" "+this.getClass().getName()+"\tEDIT RECORD\t"+e.getMessage());
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
				log.logError(date+" "+this.getClass().getName()+"\tEDIT RECORD E2\t"+e2.getMessage());
			}
			log.logError(date+" "+this.getClass().getName()+"\tEDIT RECORD E1\t"+e1.getMessage());
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
				log.logError(date+" "+this.getClass().getName()+"\tEDIT RECORD E3\t"+e3.getMessage());
			}
		}
		return false;
		}
	
	public ArrayList<Invoice> getInvoiceList(String query) {
		conn = this.connect();
		ArrayList<Invoice> list = new ArrayList<Invoice>();

//		System.out.println(query);
		try {
			PreparedStatement pst = conn.prepareStatement(query);		
			
			ResultSet rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()){
				Invoice i = null;
				i = createInvoice(rs, columnsNumber);
//				i.print();
				list.add(i);
			}
		} catch (SQLException e1) {
			log.logError(date+" "+this.getClass().getName()+"\tGET INVOICE LIST E1\t"+e1.getMessage());
		} finally {
			try{
				if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }

			} catch (Exception e2){
				log.logError(date+" "+this.getClass().getName()+"\tGET INVOICE LIST E2\t"+e2.getMessage()+"\n\t"+e2.getLocalizedMessage());
				e2.printStackTrace();
			}
		}
		return list;
	}

	private Invoice createInvoice(ResultSet rs, int columnsNumber) throws NumberFormatException, SQLException {
		String  customer_name = "", service_number = "",item_number = "", invoice_date = "", invoice_path_name = "";
		double total = 0;
		int invoice_number = 0;
		for(int i = 1 ; i <= columnsNumber; i++){
//			System.out.println(i + " "+ rs.getString(i));
			if(!rs.getString(i).isEmpty()) {
				switch(i){
				case 1:
					invoice_number = Integer.parseInt(rs.getString(i));
					break;
				case 2:
					customer_name = rs.getString(i);
					break;
				case 3:
					service_number = rs.getString(i);
					break;
				case 4:
					item_number = rs.getString(i);
					break;
				case 5:
					total = Double.parseDouble(rs.getString(i));
					break;
				case 6:
					invoice_date = rs.getString(i);
					break;
				case 7:
					invoice_path_name = rs.getString(i);
					break;
				}
			}
		}
		return new Invoice(invoice_number, customer_name, service_number, item_number, invoice_date, invoice_path_name, loggerFolderPath, total);
	}
	
	public void createTables(String query) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		if(this.conn == null)
			conn = this.connect();
		try {
			conn.createStatement().execute("PRAGMA locking_mode = PENDING");
		} catch (SQLException e) {
			log.logError(date+" "+this.getClass().getName()+"\tCREATE TABLE\t"+e.getMessage());
		}
		
		try {
			conn.setAutoCommit(false);
			conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
			pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
			rs = pst.executeQuery();
			
			int count = 0;
			while(rs.next()) {
				count++;			
			}
			System.out.println("createTabele "+ count);
		} catch (SQLException e1) {
			try{
				if(this.conn != null){
					this.conn.rollback();
				}
			} catch ( SQLException e2){
				log.logError(date+" "+this.getClass().getName()+"\tGET PATH\t"+e2.getMessage());
			}
			log.logError(date+" "+this.getClass().getName()+"\tGET PATH\t"+e1.getMessage());
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
				log.logError(date+" "+this.getClass().getName()+"\tGET PATH\t"+e3.getMessage());
			}
		}
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			log.logError(date+" "+this.getClass().getName()+"\tCLOSE E\t"+e.getMessage());
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
				log.logError(date+" "+this.getClass().getName()+"\tCLOSE E3\t"+e3.getMessage());
			}
		}
	}
}