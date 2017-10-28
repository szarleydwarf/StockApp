package app;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dbase.DatabaseManager;
import utillity.FinalVariables;
import utillity.Helper;
import utillity.Logger;
import java.awt.Color;

public class SettingsFrame {

	private JFrame frame;
	private JButton btnSaveFolderPath;
	private JLabel lblSaveFolderPath;
	private JFileChooser fc;
	
	private DatabaseManager DM;
	private static FinalVariables fv;
	
	private String folderPath="", printerName, databaseBackupPath = "";
	private String selectedPrinterName;
	protected File current;
	private File defaultFolder, databaseBackupFolder;
	private JList<String> listPrinters;
	private ArrayList<String> m_defaultPaths;
	private JLabel lblPrinterNameDisplay;
	private final String defString = "[DEFAULT]";
	private JButton btnDBBackupPath;
	private JLabel lblDatabaseBackupPath;
	private JFileChooser fcDB;

	protected static String date;
	protected static String loggerFolderPath;
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
//				System.out.print("Settings frame "+loggerFolderPath);
				log = new Logger(loggerFolderPath);
				helper = new Helper();
				date = helper.getFormatedDate();
			try {
					SettingsFrame window = new SettingsFrame(defaultPaths);
					window.frame.setVisible(true);
				} catch (Exception e) {
					log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param defaultPaths 
	 */
	public SettingsFrame(ArrayList<String> defaultPaths) {
		DM = new DatabaseManager(loggerFolderPath);
		
		fc = new JFileChooser();
		fcDB = new JFileChooser();
		
		if(!defaultPaths.isEmpty() && defaultPaths != null && !defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX).isEmpty()){
			folderPath = defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX);
			databaseBackupPath = defaultPaths.get(this.fv.DATABASE_BACKUP_PATH_ROW_ID-1);
		} else {
			m_defaultPaths = DM.getPaths("SELECT "+this.fv.SETTINGS_TABLE_PATH+" FROM "+this.fv.SETTINGS_TABLE);
			if(this.m_defaultPaths != null){
				this.folderPath = this.m_defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX);
				databaseBackupPath = m_defaultPaths.get(this.fv.DATABASE_BACKUP_PATH_ROW_ID-1);
			}else{
				this.folderPath = this.fv.SAVE_FOLDER_DEFAULT_PATH;
				databaseBackupPath = this.fv.DATABASE_BACKUP_DEFAULT_PATH;
			}
		}
		
		if(!defaultPaths.isEmpty() && defaultPaths != null && !defaultPaths.get(this.fv.PRINTER__ARRAYLIST_INDEX).isEmpty())
			this.printerName = defaultPaths.get(this.fv.PRINTER__ARRAYLIST_INDEX);
		else
			this.printerName = this.fv.PRINTER_NAME;
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		defaultFolder = new File(folderPath);
		databaseBackupFolder = new File(databaseBackupPath);
		
		this.helper.createFolderIfNotExist(folderPath);
		fc.setCurrentDirectory(defaultFolder);
		fcDB.setCurrentDirectory(databaseBackupFolder);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(255, 255, 0));
		frame.getContentPane().setBackground(new Color(255, 51, 0));
		frame.setBounds(100, 100, 722, 333);
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
					log.logError(date+" "+this.getClass().getName()+"\t"+e1.getMessage());
				}
			}
		});
		btnSaveFolderPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		btnSaveFolderPath.setBounds(618, 23, 78, 23);
		frame.getContentPane().add(btnSaveFolderPath);
        
        JLabel lblPrinterName = new JLabel("Ustawiona drukarka");
        lblPrinterName.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
        lblPrinterName.setBounds(10, 62, 138, 24);
        frame.getContentPane().add(lblPrinterName);
        
        lblPrinterNameDisplay = new JLabel("");
        lblPrinterNameDisplay.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
        lblPrinterNameDisplay.setBounds(185, 62, 426, 24);
        frame.getContentPane().add(lblPrinterNameDisplay);
        
        lblPrinterNameDisplay.setText(printerName);
        
        JLabel lblDBBackupPath = new JLabel("Kopia zapasowa bazy danych");
        lblDBBackupPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
        lblDBBackupPath.setBounds(10, 210, 174, 24);
        frame.getContentPane().add(lblDBBackupPath);
        
        lblDatabaseBackupPath = new JLabel("");
        lblDatabaseBackupPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
        lblDatabaseBackupPath.setBounds(195, 210, 416, 24);
        frame.getContentPane().add(lblDatabaseBackupPath);
        lblDatabaseBackupPath.setText(databaseBackupPath);
        
        btnDBBackupPath = new JButton("Zmie\u0144");
        btnDBBackupPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
        btnDBBackupPath.setBounds(618, 212, 78, 23);
        btnDBBackupPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent edb) {
				databaseBackup(edb);
			}
		});
        frame.getContentPane().add(btnDBBackupPath);
        
        JButton btnBack = new JButton("Powr\u00F3t");
        btnBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        		MainView.main(null);
        	}
        });
        btnBack.setBounds(618, 260, 78, 23);
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
	
	protected void savePrinterToDatabase() {
		if (JOptionPane.showConfirmDialog(frame, 
	            fv.PRINTER_CHANGE, fv.CLOSE_WINDOW, 
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			
			if(selectedPrinterName.contains(defString)){
				String regex = "\\s*["+defString+"]\\s*";
//				selectedPrinterName = selectedPrinterName.replaceAll(regex, "");
				selectedPrinterName = selectedPrinterName.substring(0, selectedPrinterName.indexOf("["));
				System.out.println("pName "+selectedPrinterName);
			}
			
        	String query = "UPDATE \""+this.fv.SETTINGS_TABLE+"\" SET "+this.fv.SETTINGS_TABLE_PATH+"='"+selectedPrinterName+"' WHERE "+this.fv.ROW_ID+"="+this.fv.PRINTER_DATABASE_ROW_ID+"";
			boolean saved = this.DM.editRecord(query );
			if(saved){
            	JOptionPane.showMessageDialog(null, "Zapisano w bazie danych");
            	printerName = selectedPrinterName;
            	lblPrinterNameDisplay.setText(printerName);
			} else
            	JOptionPane.showMessageDialog(null, "Wystapil blad zapisu w bazie danych");
            
		}else{
			System.out.println("operacja anulowana");
			
		}
		
	}

	private void getPrinterName() {
		this.listPrinters.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if(!listPrinters.isSelectionEmpty()){
//					System.out.println(listPrinters.getSelectedValue());
					selectedPrinterName = listPrinters.getSelectedValue();
				} 
			}
		});
	}

	private void getListOfPrinters() {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService defPrinter = PrintServiceLookup.lookupDefaultPrintService();
        String[] temp = new String[printServices.length];
        int i = 0;
        
        JLabel lblPrinterList = new JLabel("Zainstalowane drukarki");
        lblPrinterList.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
        lblPrinterList.setBounds(10, 97, 155, 24);
        frame.getContentPane().add(lblPrinterList);
        JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(185, 99, 426, 100);
		frame.getContentPane().add(scrollPane);
			
        for (PrintService printer : printServices) {
        	String printerName;
        	if(defPrinter != null && defPrinter.getName().compareTo(printer.getName()) == 0) {
        		printerName = defPrinter.getName()+defString ;
        	}else
        		printerName = printer.getName();
        	
        	temp[i] = printerName;
        	i++;
       
        }
        Arrays.sort(temp);

        listPrinters.setListData(temp);
        scrollPane.setViewportView(listPrinters);
        
        JButton btnSaveDBPath = new JButton("Zmie\u0144");
        btnSaveDBPath.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		savePrinterToDatabase();
        	}
        });
        btnSaveDBPath.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
        btnSaveDBPath.setBounds(618, 99, 78, 23);
        frame.getContentPane().add(btnSaveDBPath);
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

            String query = "UPDATE \""+this.fv.SETTINGS_TABLE+"\" SET "+this.fv.SETTINGS_TABLE_PATH+"='"+newDir.getAbsolutePath()+"' WHERE "+this.fv.ROW_ID+"="+this.fv.DEFAULT_FOLDER_DATABASE_ROW_ID+"";
            boolean updated = this.DM.editRecord(query);
            if(updated)
            	JOptionPane.showMessageDialog(null, "Zapisano w bazie danych");
            else
            	JOptionPane.showMessageDialog(null, "Wystapil blad zapisu w bazie danych");
        }
	}
	
	protected void databaseBackup(ActionEvent edb) {
		if (edb.getSource() == btnDBBackupPath) {	
			int returnVal = fcDB.showSaveDialog(null);
			File newDir = null;
			current = fcDB.getCurrentDirectory().getAbsoluteFile();
		 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	newDir = fcDB.getSelectedFile();
            } else {
            	newDir = current;
            }
        	System.out.println(fcDB.getCurrentDirectory()+" "+newDir.getAbsoluteFile()+" "+current.getAbsoluteFile());
        	fcDB.setCurrentDirectory(newDir.getAbsoluteFile());
            (lblDatabaseBackupPath).setText(""+newDir.getAbsoluteFile());

            String query = "UPDATE \""+this.fv.SETTINGS_TABLE+"\" SET "+this.fv.SETTINGS_TABLE_PATH+"='"+newDir.getAbsolutePath()+"' WHERE "+this.fv.ROW_ID+"="+this.fv.DATABASE_BACKUP_PATH_ROW_ID+"";
            boolean updated = this.DM.editRecord(query);
            if(updated)
            	JOptionPane.showMessageDialog(null, "Zapisano w bazie danych");
            else
            	JOptionPane.showMessageDialog(null, "Wystapil blad zapisu w bazie danych");
        }
	}
}
