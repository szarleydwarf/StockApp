package app;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import utillity.FinalVariables;

public class WyswietlListeUslug {

	private JFrame frame;
	private FinalVariables fv;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WyswietlListeUslug window = new WyswietlListeUslug();
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
	public WyswietlListeUslug() {
		this.fv = new FinalVariables();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
