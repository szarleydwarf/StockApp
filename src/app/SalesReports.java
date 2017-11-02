package app;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import javax.swing.JButton;

public class SalesReports {

	private JFrame frame;
	private JLabel lblMonth;
	protected static String date;
	protected static String loggerFolderPath;
	
	private static FinalVariables fv;
	private static Logger log;
	private static Helper helper;

	/**
	 * Launch the application.
	 */
	public static void main(ArrayList<String> defaultPaths) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				fv = new FinalVariables();
				loggerFolderPath = defaultPaths.get(0)+"\\"+fv.LOGGER_FOLDER_NAME;
				log = new Logger(loggerFolderPath);
				helper = new Helper();
				date = helper.getFormatedDate();
				try {
					SalesReports window = new SalesReports();
					window.frame.setVisible(true);
				} catch (Exception e) {
					log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SalesReports() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		frame.getContentPane().setLayout(null);
		frame.setBounds(100, 100, 746, 300);

		JLabel lblTitle = new JLabel("Raporty sprzeda\u017Cy");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblTitle.setBounds(0, 11, 730, 26);
		frame.getContentPane().add(lblTitle);
		
		lblMonth = new JLabel("Miesi\u0105c");
		lblMonth.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonth.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblMonth.setBounds(52, 56, 60, 20);
		frame.getContentPane().add(lblMonth);
		
		JLabel lblCosts = new JLabel("Koszty");
		lblCosts.setHorizontalAlignment(SwingConstants.CENTER);
		lblCosts.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblCosts.setBounds(175, 56, 60, 20);
		frame.getContentPane().add(lblCosts);
		
		JLabel lblIncome = new JLabel("Zysk");
		lblIncome.setHorizontalAlignment(SwingConstants.CENTER);
		lblIncome.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblIncome.setBounds(500, 56, 60, 20);
		frame.getContentPane().add(lblIncome);
		
		JLabel lblSale = new JLabel("Sprzeda\u017C");
		lblSale.setHorizontalAlignment(SwingConstants.CENTER);
		lblSale.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblSale.setBounds(333, 56, 60, 20);
		frame.getContentPane().add(lblSale);
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.setBounds(631, 227, 89, 23);
		frame.getContentPane().add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		
		
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
	}
}
