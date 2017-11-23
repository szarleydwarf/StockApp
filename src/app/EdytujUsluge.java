package app;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import utillity.FinalVariables;
import java.awt.Color;

public class EdytujUsluge {

	private JFrame frame;
	private FinalVariables fv;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EdytujUsluge window = new EdytujUsluge();
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
	public EdytujUsluge() {
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
		frame.setLocation(10, 10);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
