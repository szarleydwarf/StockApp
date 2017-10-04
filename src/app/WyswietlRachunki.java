package app;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import utillity.FinalVariables;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WyswietlRachunki {

	private JFrame frame;
	private FinalVariables fv;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WyswietlRachunki window = new WyswietlRachunki();
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
	public WyswietlRachunki() {
		this.fv = new FinalVariables();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(240, 230, 140));
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Lista wystawionych rachunkow??");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 24));
		lblNewLabel.setBounds(10, 83, 664, 70);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Powr\u00F3t");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		btnNewButton.setBounds(299, 288, 89, 23);
		frame.getContentPane().add(btnNewButton);
		frame.setBackground(new Color(135, 206, 235));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
		frame.setBounds(100, 100, 700, 361);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
