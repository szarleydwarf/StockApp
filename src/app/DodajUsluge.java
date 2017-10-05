package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.SwingConstants;

import dbase.DatabaseManager;
import utillity.FinalVariables;
import utillity.Helper;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;

public class DodajUsluge {

	private JFrame frame;
	private JButton btnZapisz;
	private JTextField tfServiceNum;
	private JTextField tfServiceName;
	private JTextField tfCost;
	private JTextField tfPrice;

	private String serviceNum, serviceName="", cost="", price="";
	private boolean varEmpty = true;
	private double dCost = 0, dPrice = 0;
	private int iQnt = 0;

	private Helper helper;
	private DatabaseManager dm = null;
	private FinalVariables fv;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DodajUsluge window = new DodajUsluge();
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
	public DodajUsluge() {
		helper = new Helper();
		dm = new DatabaseManager();
		this.fv = new FinalVariables();

		String query = "SELECT "+this.fv.SERVICE_TABLE_NUMBER+" FROM "+this.fv.SERVICES_TABLE+" ORDER BY "+this.fv.SERVICE_TABLE_NUMBER+" DESC LIMIT 1";
		ArrayList<String> stNoList = dm.selectRecordArrayList(query);
		
		if(!stNoList.get(0).isEmpty())
			serviceNum = stNoList.get(0);
		else
			serviceNum = "AAS0000";
		
		this.serviceNum = helper.getIntFromStNo(serviceNum, 'S');
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 528, 321);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            fv.CLOSE_WINDOW, fv.CLOSE_WINDOW, 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        	MainView.main(null);
		        }
		    }
		});
		JLabel lblDodajNowUsug = new JLabel("Dodaj now\u0105 us\u0142ug\u0119");
		lblDodajNowUsug.setHorizontalAlignment(SwingConstants.CENTER);
		lblDodajNowUsug.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblDodajNowUsug.setBounds(124, 11, 245, 26);
		frame.getContentPane().add(lblDodajNowUsug);
		
		displayServiceNum();
		
		JLabel lblNazwaUsugi = new JLabel("Nazwa us\u0142ugi");
		lblNazwaUsugi.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblNazwaUsugi.setBounds(10, 100, 124, 26);
		frame.getContentPane().add(lblNazwaUsugi);
		
		tfServiceName = new JTextField();
		tfServiceName.setColumns(10);
		tfServiceName.setBounds(144, 101, 321, 26);
		frame.getContentPane().add(tfServiceName);
		
		JLabel label_3 = new JLabel("Koszt");
		label_3.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		label_3.setBounds(10, 137, 124, 26);
		frame.getContentPane().add(label_3);
		
		tfCost = new JTextField();
		tfCost.setColumns(10);
		tfCost.setBounds(144, 138, 321, 26);
		frame.getContentPane().add(tfCost);
		
		JLabel label_4 = new JLabel("Cena");
		label_4.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		label_4.setBounds(10, 174, 124, 26);
		frame.getContentPane().add(label_4);
		
		tfPrice = new JTextField();
		tfPrice.setColumns(10);
		tfPrice.setBounds(144, 175, 321, 26);
		frame.getContentPane().add(tfPrice);
		
		btnZapisz = new JButton("Zapisz");
		btnZapisz.setForeground(Color.BLUE);
		btnZapisz.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnZapisz.setBackground(new Color(255, 215, 0));
		btnZapisz.setBounds(415, 249, 89, 23);
		frame.getContentPane().add(btnZapisz);
		btnZapisz.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				varEmpty = getVariableForQuery();
				if(!varEmpty)
					zapiszNowyProdukt();
			}

		});	
	}

	private void zapiszNowyProdukt() {
		boolean saved = dm.addNewRecord("INSERT INTO \""+this.fv.SERVICES_TABLE+"\"  VALUES ('"+this.serviceNum+"','"+this.serviceName+"',"+this.dCost+","+this.dPrice+");");
		System.out.println("zapisuje "+serviceNum+"','"+this.serviceName+"',"+this.dCost+","+this.dPrice+","+this.iQnt);
		if(saved){
			JOptionPane.showMessageDialog(null, "Dodano nowy towar");
			this.frame.dispose();
		} else
			JOptionPane.showMessageDialog(null, "Błąd zapisu");
	}

	private boolean getVariableForQuery() {
		this.serviceName = this.tfServiceName.getText();
		System.out.println("get "+serviceName );
		if(this.serviceName.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Wpisz nazwe usługi");
			return true;
		}
		
		this.cost = this.tfCost.getText();
		if(this.dCost == 0){
			this.dCost = this.helper.checkDouble("Wpisz koszt",  "Niepoprawny format kosztu", this.cost);
			return true;
		}
	
		this.price = this.tfPrice.getText();
		if(this.dPrice == 0) {
			this.dPrice = helper.checkDouble("Wpisz cene", "Niepoprawny format ceny", this.price);
			return true;
		}
	
		return false;
	}

	private void displayServiceNum() {
		JLabel label_1 = new JLabel("Numer magazynowy");
		label_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		label_1.setBounds(10, 62, 124, 26);
		frame.getContentPane().add(label_1);
		
		tfServiceNum = new JTextField();
		tfServiceNum.setHorizontalAlignment(SwingConstants.RIGHT);
		tfServiceNum.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		tfServiceNum.setColumns(10);
		tfServiceNum.setBounds(144, 63, 321, 26);
		frame.getContentPane().add(tfServiceNum);
		
		tfServiceNum.setText(this.serviceNum);
	}
}
