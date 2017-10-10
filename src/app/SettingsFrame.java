package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import dbase.DatabaseManager;
import utillity.FinalVariables;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class SettingsFrame {

	private JFrame frame;
	JFileChooser fc;
	
	private DatabaseManager DM;
	private FinalVariables fv;
	
	private String folderPath="";
	private JButton btnSaveFolderPath;
	private JLabel lblSaveFolderPath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingsFrame window = new SettingsFrame();
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
	public SettingsFrame() {
		DM = new DatabaseManager();

		this.fv = new FinalVariables();
		
		fc = new JFileChooser();
		
		folderPath = DM.getPath("SELECT path FROM settings WHERE rowid=1");
		System.out.println(folderPath);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 722, 235);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		lblSaveFolderPath = new JLabel("");
		lblSaveFolderPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblSaveFolderPath.setBounds(10, 21, 600, 24);
		lblSaveFolderPath.setText(folderPath);
		frame.getContentPane().add(lblSaveFolderPath);
		
		btnSaveFolderPath = new JButton("...");
		btnSaveFolderPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					performAction(e);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSaveFolderPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnSaveFolderPath.setBounds(618, 23, 78, 23);
		frame.getContentPane().add(btnSaveFolderPath);
	}
	
	private void performAction(ActionEvent e) throws IOException {
		if (e.getSource() == btnSaveFolderPath) {
			 int returnVal = fc.showSaveDialog(null);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                System.out.println(fc.getCurrentDirectory());
	                //This is where a real application would save the file.
//	                (lblSaveFolderPath).setText("Saving: " + file.getName() + ".");
	            } else {
	            	(lblSaveFolderPath).setText("Save command cancelled by user.");
	            }
        }
	}
}
