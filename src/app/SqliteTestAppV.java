package app;

import java.awt.EventQueue;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import dbase.DatabaseManager;
import temp.UtillTemp;

public class SqliteTestAppV {

	private JFrame frame;
	private static DatabaseManager DM;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		UtillTemp ut;
		DM = new DatabaseManager();
		try {
//			ut = new UtillTemp();
//			ut.saltPassword("SomePass1234#");

//			DM.selectRecordWithSQL("Select * from users where nick=\"admin\" ");
			Map<String, String> where = new HashMap<String, String>();
			where.put("service_number", "AAS0002");
//			where.put("first_name", "Rad2");
			DM.selectRecord("services", "*", where);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SqliteTestAppV window = new SqliteTestAppV();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SqliteTestAppV() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
