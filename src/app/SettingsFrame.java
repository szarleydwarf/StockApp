package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import dbase.DatabaseManager;
import utillity.FinalVariables;
import utillity.Helper;

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
	private JButton btnSaveFolderPath;
	private JLabel lblSaveFolderPath;
	private JFileChooser fc;
	
	private DatabaseManager DM;
	private FinalVariables fv;
	private Helper helper;
	
	private String folderPath="";
	protected File current;
	private File defaultFolder;

	/**
	 * Launch the application.
	 */
	public static void main(String invoiceFolderPath) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingsFrame window = new SettingsFrame(invoiceFolderPath);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param invoiceFolderPath 
	 */
	public SettingsFrame(String invoiceFolderPath) {
		DM = new DatabaseManager();
		this.helper = new Helper();
		this.fv = new FinalVariables();
		
		fc = new JFileChooser();
		
		folderPath = invoiceFolderPath;//DM.getPath("SELECT "+this.fv.SETTINGS_TABLE_PATH+" FROM "+this.fv.SETTINGS_TABLE+" WHERE "+this.fv.ROW_ID+"=1");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		defaultFolder = new File(folderPath);
		
		this.helper.createFolderIfNotExist(folderPath);
		fc.setCurrentDirectory(defaultFolder);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 722, 235);
		frame.getContentPane().setLayout(null);
		
		JLabel lblSaveFolderPathInfo = new JLabel("Folder z rachunkami:");
		lblSaveFolderPathInfo.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblSaveFolderPathInfo.setBounds(10, 21, 138, 24);
		frame.getContentPane().add(lblSaveFolderPathInfo);
		
		
		lblSaveFolderPath = new JLabel("");
		lblSaveFolderPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblSaveFolderPath.setBounds(147, 21, 464, 24);
		lblSaveFolderPath.setText(folderPath);
		frame.getContentPane().add(lblSaveFolderPath);
    	
		btnSaveFolderPath = new JButton("Zmie\u0144");
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
		
		JLabel lblDBSavePathInfo = new JLabel("Folder z baz\u0105 danych");
		lblDBSavePathInfo.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblDBSavePathInfo.setBounds(10, 56, 138, 24);
		frame.getContentPane().add(lblDBSavePathInfo);
		
		JLabel lblDBSavePath = new JLabel("<dynamic>");
		lblDBSavePath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblDBSavePath.setBounds(147, 56, 464, 24);
		frame.getContentPane().add(lblDBSavePath);
		
		JButton btnSaveDBPath = new JButton("Zmie\u0144");
		btnSaveDBPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnSaveDBPath.setBounds(618, 58, 78, 23);
		frame.getContentPane().add(btnSaveDBPath);
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
	
	private void performAction(ActionEvent e) throws IOException {
		if (e.getSource() == btnSaveFolderPath) {	
			int returnVal = fc.showSaveDialog(null);
			File newDir = null;
			current = fc.getCurrentDirectory().getAbsoluteFile();
		 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	newDir = fc.getSelectedFile();
            } else {
            	newDir = current;
            }
        	System.out.println(fc.getCurrentDirectory()+" "+newDir.getAbsoluteFile()+" "+current.getAbsoluteFile());
            fc.setCurrentDirectory(newDir.getAbsoluteFile());
            (lblSaveFolderPath).setText(""+newDir.getAbsoluteFile());
        	
            //"UPDATE \""+tableName+"\" SET "+colNameToSet+"='"+this.productName+"', cost='"+this.dCost+"', price='"+this.dPrice+"'";
            String query = "UPDATE \""+this.fv.SETTINGS_TABLE+"\" SET "+this.fv.SETTINGS_TABLE_PATH+"='"+newDir.getAbsolutePath()+"' WHERE "+this.fv.ROW_ID+"=1";
            boolean updated = this.DM.editRecord(query);
            if(updated)
            	System.out.println("SUCCESS");
            else
            	System.out.println("O O");
        }
	}
}
