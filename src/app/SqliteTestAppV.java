package app;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import dbase.DatabaseManager;
import dbase.UserManager;
import utillity.FinalVariables;

import java.awt.Color;
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

	private JFrame frmHctApp;
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
		try {
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
					window.frmHctApp.setVisible(true);
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHctApp = new JFrame();
		frmHctApp.setTitle("HCT APP");
		frmHctApp.setResizable(false);
		frmHctApp.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frmHctApp.setBackground(Color.RED);
		frmHctApp.setBounds(100, 100, 450, 243);
		frmHctApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmHctApp.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Logowanie");
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		lblNewLabel.setBounds(161, 11, 113, 44);
		frmHctApp.getContentPane().add(lblNewLabel);
		
		JLabel lblNazwaUzytkownika = new JLabel("Nazwa uzytkownika");
		lblNazwaUzytkownika.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		lblNazwaUzytkownika.setBounds(30, 73, 383, 44);
		frmHctApp.getContentPane().add(lblNazwaUzytkownika);
		
		JLabel lblHaslo = new JLabel("Haslo");
		lblHaslo.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		lblHaslo.setBounds(30, 116, 384, 44);
		frmHctApp.getContentPane().add(lblHaslo);
		
		username = new JTextField();
		username.setBounds(242, 85, 156, 20);
		frmHctApp.getContentPane().add(username);
		username.setColumns(10);
		
		password = new JPasswordField();
		password.setBounds(243, 128, 156, 20);
		frmHctApp.getContentPane().add(password);
		
		JButton login_btn = new JButton("Zaloguj");
		login_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				zaloguj();
			}


		});
		login_btn.setForeground(Color.YELLOW);
		login_btn.setBackground(Color.BLUE);
		login_btn.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		login_btn.setBounds(162, 171, 112, 23);
		frmHctApp.getContentPane().add(login_btn);
	}
	
	public void zaloguj() {
		try {
			String username_str = username.getText();
			@SuppressWarnings("deprecation")
			String pass = password.getText();
//			System.out.println("logowanie "+username_str + " "+pass);

		 	boolean isLogged = UM.login(username_str, pass, loggerFolderPath);
		 	if(isLogged){
		 		frmHctApp.setVisible(false);
		 		MainView.main(null);
		 	}
//			conn.close();
		}catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: " + ex);
		}

	}
}
