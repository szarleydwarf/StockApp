package app;

import java.awt.Component;
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
import java.util.ArrayList;
import java.util.Arrays;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JList;

public class SettingsFrame {

	private JFrame frame;
	private JButton btnSaveFolderPath;
	private JLabel lblSaveFolderPath;
	private JFileChooser fc;
	
	private DatabaseManager DM;
	private FinalVariables fv;
	private Helper helper;
	
	private String folderPath="", printerName;
	protected File current;
	private File defaultFolder;
	private JList<String> listPrinters;

	/**
	 * Launch the application.
	 */
	public static void main(ArrayList<String> defaultPaths) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingsFrame window = new SettingsFrame(defaultPaths);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param defaultPaths 
	 */
	public SettingsFrame(ArrayList<String> defaultPaths) {
		DM = new DatabaseManager();
		this.helper = new Helper();
		this.fv = new FinalVariables();
		
		fc = new JFileChooser();
		
		if(defaultPaths != null && !defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX).isEmpty())
			folderPath = defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX);
		else
			this.folderPath = this.fv.SAVE_FOLDER_DEFAULT_PATH;
		
		if(defaultPaths != null && !defaultPaths.get(this.fv.PRINTER__ARRAYLIST_INDEX).isEmpty())
			this.printerName = defaultPaths.get(this.fv.PRINTER__ARRAYLIST_INDEX);
		else
			this.printerName = this.fv.PRINTER_NAME;
		
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
		lblSaveFolderPath.setBounds(185, 21, 426, 24);
		lblSaveFolderPath.setText(folderPath);
		frame.getContentPane().add(lblSaveFolderPath);
    	
		listPrinters = new JList<String>();
		listPrinters.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		listPrinters.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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
		
		JLabel lblPrinterList = new JLabel("Zainstalowane drukarki");
		lblPrinterList.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		lblPrinterList.setBounds(10, 56, 155, 24);
		frame.getContentPane().add(lblPrinterList);
		
		JButton btnSaveDBPath = new JButton("Zmie\u0144");
		btnSaveDBPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnSaveDBPath.setBounds(618, 58, 78, 23);
		frame.getContentPane().add(btnSaveDBPath);
		
		JButton btnBack = new JButton("Powr\u00F3t");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainView.main(null);
			}
		});
		btnBack.setBounds(618, 162, 78, 23);
		frame.getContentPane().add(btnBack);

		getPrinterName();
		getListOfPrinters();
		
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
	
	private void getPrinterName() {

	}

	private void getListOfPrinters() {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService defPrinter = PrintServiceLookup.lookupDefaultPrintService();
        String[] temp = new String[printServices.length];
        int i = 0;
        JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(185, 58, 426, 100);
		frame.getContentPane().add(scrollPane);
			
        for (PrintService printer : printServices) {
        	String printerName;
        	if(defPrinter != null && defPrinter.getName().compareTo(printer.getName()) == 0) {
        		printerName = defPrinter.getName()+" [DEFAULT]";
        	}else
        		printerName = printer.getName();
        	
        	temp[i] = printerName;
        	i++;
       
        }
        Arrays.sort(temp);

        listPrinters.setListData(temp);
        scrollPane.setViewportView(listPrinters);
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
