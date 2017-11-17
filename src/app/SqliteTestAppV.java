package app;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import dbase.DatabaseManager;
import dbase.UserManager;
import utillity.FinalVariables;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SqliteTestAppV {

	private JFrame frame;
	private JTextField username;
	private JPasswordField password;
	private static FinalVariables fv;
	
	private static DatabaseManager DM;
	private static UserManager UM;
	private static String loggerFolderPath;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		fv = new FinalVariables();
		loggerFolderPath = fv.SAVE_FOLDER_DEFAULT_PATH+"\\"+fv.LOGGER_FOLDER_NAME;
		DM = new DatabaseManager(loggerFolderPath);
		UM = new UserManager();
		
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
		this.fv = new FinalVariables();
//		initiateDatbase();

		initialize();
	}

	private void initiateDatbase() {
		String query = "CREATE TABLE IF NOT EXISTS \"users\" (\"nick\" varchar(64) PRIMARY KEY  NOT NULL  DEFAULT (null) ,\"salt\" varchar(32) NOT NULL ,\"password\" varchar(64) NOT NULL, \"first_name\" varchar(32)NOT NULL,\"last_name\" varchar(32)NOT NULL ,\"company_name\" varchar(32)NOT NULL , \"mobile\" varchar(10), \"addres\" varchar(32)NOT NULL, \"town\" varchar(32)NOT NULL,\"county\" varchar(32)NOT NULL,\"country\" varchar(32)NOT NULL,\"eircode\" varchar(7)NOT NULL);"
		+"INSERT INTO \"users\" VALUES('admin','tezrn6H#s7|89jd-M@voxEBI|@£*cW$I','EDORzQox5Kot57a7BlT3jkTiBDXhuFclXYnsBEUtZzlojcBFQj5KV11Zd9qXMUEO','Rad','Cho','HCT','0892422993','Killaneen','Ballinamore','Leitrim','Ireland','N41YK50');"
		+"INSERT INTO \"users\" VALUES('admin2','tezrn6H#s7|89jd-M@voxEBI|@£*cW$I','EDORzQox5Kot57a7BlT3jkTiBDXhuFclXYnsBEUtZzlojcBFQj5KV11Zd9qXMUEO','Marcin','Chrostek','HCT','0892422992','Killaneen','Ballinamore','Leitrim','Ireland','N41YK50');";
		
		DM.createTables(query);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		frame.setTitle("HCT APP");
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBackground(new Color(255, 255, 0));
		frame.setBounds(100, 100, 450, 243);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Logowanie");
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		lblNewLabel.setBounds(161, 11, 113, 44);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNazwaUzytkownika = new JLabel("Nazwa uzytkownika");
		lblNazwaUzytkownika.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		lblNazwaUzytkownika.setBounds(30, 73, 383, 44);
		frame.getContentPane().add(lblNazwaUzytkownika);
		
		JLabel lblHaslo = new JLabel("Haslo");
		lblHaslo.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		lblHaslo.setBounds(30, 116, 384, 44);
		frame.getContentPane().add(lblHaslo);
		
		username = new JTextField();
		username.setBounds(242, 85, 156, 20);
		frame.getContentPane().add(username);
		username.setColumns(10);
		
		password = new JPasswordField();
		password.setBounds(243, 128, 156, 20);
		frame.getContentPane().add(password);
		
		JButton login_btn = new JButton("Zaloguj");
		login_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				zaloguj();
			}
		});
		login_btn.setForeground(Color.YELLOW);
		login_btn.setBackground(new Color(51, 153, 255));
		login_btn.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		login_btn.setBounds(162, 171, 112, 23);
		frame.getContentPane().add(login_btn);
	}
	
	public void zaloguj() {
		try {
			String username_str = username.getText();
			@SuppressWarnings("deprecation")
			String pass = password.getText();
			System.out.println("logowanie "+username_str + " "+pass);

		 	boolean isLogged = UM.login(username_str, pass, loggerFolderPath);
		 	if(isLogged){
		 		frame.setVisible(false);
		 		MainView.main(null);
		 	}
//			conn.close();
		}catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: " + ex);
		}

	}
}
