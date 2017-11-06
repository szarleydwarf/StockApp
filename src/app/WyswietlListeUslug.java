package app;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import java.awt.Color;

public class WyswietlListeUslug {

	private JFrame frame;
	private FinalVariables fv;
	
	protected static String date;
	protected static String loggerFolderPath;
	private static Logger log;
	private static Helper helper;

	/**
	 * Launch the application.
	 */
	public static void main(String p_loggerFolderPath) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				loggerFolderPath = p_loggerFolderPath;
				log = new Logger(loggerFolderPath);
				helper = new Helper();
				date = helper.getFormatedDate();
				try {
					WyswietlListeUslug window = new WyswietlListeUslug();
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
	public WyswietlListeUslug() {
		this.fv = new FinalVariables();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(255, 255, 0));
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
