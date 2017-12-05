package utillity;

import java.awt.Color;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.printing.PDFPageable;

import dbase.DatabaseManager;


public class StockPrinter  { 
	
	private String savePath = "", accPath = "";
	private String ext = ".pdf", docNameCopy = "AC_";
	private String docPath = "";
	private PDPageContentStream contentStream ;
	private DecimalFormat df;

	private TableModel md;
	private double sum = 0, discount = 0;
	private long timeout = 50000;
	private boolean applyDiscount = false;
	private int itemCount = 0, servCount = 0;
	private int invNo = 1, stringLengthF = 3, stringLengthB = 12;
	private char paddingChar = ' ';
	private String carManufacturer = "OTHER", carRegistration = "00AA0000", invSt = "Invoice", noSt = "No.", date;
	
	private Helper helper;
	private DatabaseManager DM;
	private FinalVariables fv;
	
	private ArrayList<String> stockServicesNumber;
	private boolean jobDone = false;
	private String invoiceFileName;
	private String fileName;
	private String printerName;
	private ArrayList<String> m_defaultPaths;
	private String slash = "\\";
	private boolean[] freebies;
	private JTable table;
	private int rowCount;
	private int colCount;
	private boolean folderExist;
	private boolean accFolderExist;
	private String eightSpaceStr = "        ";
	private Map<String, String> itemCodeName;
	
	protected static String loggerFolderPath;
	private static Logger log;
	
	
	public StockPrinter(ArrayList<String> defaultPaths){
		this.fv = new FinalVariables();
		this.printerName = "";

//		for(int i=0; i<defaultPaths.size();i++)
//		System.out.println("Printer "+defaultPaths.get(i)+" "+defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX));
		
		if(!defaultPaths.isEmpty() && defaultPaths != null && !defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX).isEmpty()){
			this.savePath = defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX)+slash;
			loggerFolderPath = defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX)+slash;//+this.fv.LOGGER_FOLDER_NAME;
		}else{
			m_defaultPaths = DM.getPaths("SELECT "+this.fv.SETTINGS_TABLE_PATH+" FROM "+this.fv.SETTINGS_TABLE);
			if(this.m_defaultPaths != null){
				this.savePath = this.m_defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX)+slash;
				loggerFolderPath = m_defaultPaths.get(this.fv.DEFAULT_FOLDER_ARRAYLIST_INDEX)+slash+this.fv.LOGGER_FOLDER_NAME+slash;
			}else {
				this.savePath = this.fv.SAVE_FOLDER_DEFAULT_PATH+slash;
				loggerFolderPath = slash+this.fv.LOGGER_FOLDER_NAME+slash;
			}
		}
		
		if(!loggerFolderPath.contains(this.fv.LOGGER_FOLDER_NAME))
			loggerFolderPath = slash+this.fv.LOGGER_FOLDER_NAME+slash;
		
		DM = new DatabaseManager(loggerFolderPath);
		helper = new Helper();
		log = new Logger(loggerFolderPath);
		
		if(!defaultPaths.isEmpty() && defaultPaths != null && this.fv.PRINTER__ARRAYLIST_INDEX < defaultPaths.size() && !defaultPaths.get(this.fv.PRINTER__ARRAYLIST_INDEX).isEmpty())
			this.printerName = defaultPaths.get(this.fv.PRINTER__ARRAYLIST_INDEX);
		else
			this.printerName = this.fv.PRINTER_NAME;

		this.date = helper.getFormatedDate();

//		this.savePath;
//		log.logError("log "+this.loggerFolderPath+"\t savepath "+this.savePath);
		this.stockServicesNumber = new ArrayList<String>();
		freebies = new boolean[fv.FREEBIES_ARRAY_SIZE];
		
		folderExist = false;
		accFolderExist = false;
	}
	
	public void printCleanPDF() throws Exception {
		PDDocument empty = new PDDocument();
		PDPage page = new PDPage();
		empty.addPage(page);
		contentStream = new PDPageContentStream(empty, page);
		addLogo(empty);
		fillCompanyDetails();

		contentStream.close();
		String path = savePath+loggerFolderPath+"empty.pdf";
//		log.logError("print empty pdf "+path);
		empty.save(path);
		empty.close();
// TODO
//		this.printPDF(path);
	}
	
	public boolean printDoc(JTable tbChoosen, Map<String, String> itemCodeName, boolean[] freebies, double discount, boolean applyDiscount, String carManufacturer, String registration, int invoiceNum) throws Exception{
		savePath = savePath.concat(this.date);
		if(!folderExist)
			folderExist = helper.createFolderIfNotExist(savePath);

		accPath = savePath+slash + "_accountacy copy"+slash;		
		
		if(!accFolderExist)
			accFolderExist = helper.createFolderIfNotExist(accPath);
		
		this.itemCodeName = itemCodeName;
		
		this.df = new DecimalFormat(this.fv.DECIMAL_FORMAT); 
		this.table = tbChoosen;
		this.md = tbChoosen.getModel();
		this.rowCount = this.md.getRowCount();
		this.colCount = this.md.getColumnCount();

		this.freebies = freebies;
		this.discount = discount;
		this.applyDiscount = applyDiscount;
		this.carManufacturer = carManufacturer;
		if(!registration.isEmpty())
			this.carRegistration = registration;

		this.carRegistration.toUpperCase();

		this.carRegistration = this.carRegistration.toUpperCase();

		this.invNo = invoiceNum;
//		System.out.println("print :" + discount+" "+applyDiscount+" "+carManufacturer+" "+registration+" "+invoiceNum);
		
		generatePDF();

		printPDF(docPath);

		createAccountancCopy();
		
		saveEntryToDatabase();
		return jobDone ;
	}

	public boolean saveDoc(JTable tbChoosen, Map<String, String> itemCodeName, boolean[] freebies, double discount, boolean applyDiscount, String carManufacturer, String registration, int invoiceNum) throws Exception{
		savePath = savePath.concat(this.date);
		if(!folderExist)
			folderExist = helper.createFolderIfNotExist(savePath);

		accPath = savePath+slash + "_accountacy copy"+slash;		
		
		if(!accFolderExist)
			accFolderExist = helper.createFolderIfNotExist(accPath);
		
		this.itemCodeName = itemCodeName;

		this.df = new DecimalFormat(this.fv.DECIMAL_FORMAT); 
		this.table = tbChoosen;
		this.md = tbChoosen.getModel();
		this.rowCount = this.md.getRowCount();
		this.colCount = this.md.getColumnCount();
		
		this.freebies = freebies;
		this.discount = discount;
		this.applyDiscount = applyDiscount;
		this.carManufacturer = carManufacturer;
		if(!registration.isEmpty())
			this.carRegistration = registration;

		this.carRegistration.toUpperCase();
		this.carRegistration = this.carRegistration.toUpperCase();

		this.invNo = invoiceNum;
//		System.out.println("save :" + discount+" "+applyDiscount+" "+carManufacturer+" "+registration+" "+invoiceNum);
		
		generatePDF();

		createAccountancCopy();
		
		saveEntryToDatabase();
		return jobDone ;
	}

	private void saveEntryToDatabase() throws SQLException {
		String servNo = "", itemNo = "";
		int qnt = 0;
		String[] pt = new String[2]; 
		ArrayList<String[]> oldValues = new ArrayList<String[]>();

		if(rowCount > 0) {			
			for(int i = 0; i < this.rowCount; i++){
				pt = new String[2];
				pt[0] = this.itemCodeName.get(this.md.getValueAt(i, 0).toString());
				pt[1] = this.md.getValueAt(i, 2).toString();
				
				if(!oldValues.isEmpty()){
					Iterator<String[]> it = oldValues.iterator();
					while(it.hasNext()){
						String[] s = it.next();
						if(s[0].equals(pt[0])){						
							qnt = Integer.parseInt(pt[1]) + Integer.parseInt(s[1]);
							pt[1] = ""+qnt;	
							it.remove();
						}
					}
				}
				oldValues.add(pt);
			}//end for loop rowcount
		}

		int k = 0;
		if(!oldValues.isEmpty()){
			for(String[] s : oldValues){
				if(s[0].contains(fv.AAA))
					itemNo += s[1]+s[0]+",";
				else if(s[0].contains(fv.AAS))
					servNo += s[1]+s[0]+",";
			}
		}
	
		if(!itemNo.isEmpty())
			itemNo = itemNo.substring(0, itemNo.lastIndexOf(","));
		if(!servNo.isEmpty())
			servNo = servNo.substring(0, servNo.lastIndexOf(","));
		
		int i = 0;
		while(i<timeout){
			i++;
		}
		String query = "INSERT INTO \""+this.fv.INVOCE_TABLE+"\"  VALUES ("+this.invNo+",'"+this.carManufacturer+" / " +this.carRegistration+"','"+servNo+"','"+itemNo +"',"+sum +", '"+this.date+"', '"+this.invoiceFileName    +"');";
		
		boolean succes = DM.addNewRecord(query);
		if(succes){
			JOptionPane.showMessageDialog(null, "Zapisano w bazie danych");
			this.jobDone = true;
		}else{
			JOptionPane.showMessageDialog(null, "Wystapil blad zapisu w bazie danych");
			this.jobDone = false;
		}
	}

	private void generatePDF()  throws IOException{
		PDDocument customerCopyDoc = new PDDocument();
		PDPage page = new PDPage();
		customerCopyDoc.addPage(page);
		contentStream = new PDPageContentStream(customerCopyDoc, page);
		
		addLogo(customerCopyDoc);
		fillCompanyDetails();
		populateInvNumManufacturer();
		populateItemsTable();

		contentStream.close();
		this.fileName =  date+" "+invNo+ext; 
		this.invoiceFileName = date+slash+this.fileName;
		docPath = savePath+slash+this.fileName;
		//TODO
//		log.logError("save path "+savePath +" docpath "+this.docPath+" "+this.docNameCopy+"\t invName "+this.invoiceFileName);

		customerCopyDoc.save(docPath);
		customerCopyDoc.close();
	}

	private void addLogo(PDDocument customerCopyDoc) throws IOException {
		PDImageXObject pdImage = PDImageXObject.createFromFile(this.fv.INVOICE_LOGO_PATH, customerCopyDoc);
		
		contentStream.drawImage(pdImage, 210,  675);
		contentStream.setNonStrokingColor(Color.darkGray);
		contentStream.addRect(15, 420, 580, 50);
		contentStream.fill();
	}

	private void fillCompanyDetails() throws IOException {
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.beginText();
		contentStream.setFont(PDType1Font.COURIER, 18);
		contentStream.newLineAtOffset(25f,  740);
		contentStream.setLeading(20.5f);
		
		String text = "HCT",
				text2 = "Killaneen, Ballinamore",
				text3 = "co.Leitrim",
				text4 = "N41YK50",
				text5 = "089 24 22 993",
				text6 = "hctballinamore@gmail.com",
				text7 = "hct-ireland.business.site",
				text8 = "FB @hct.irl";
		contentStream.showText(text + "                                       " + date);
		contentStream.newLine();
		contentStream.setFont(PDType1Font.COURIER, 12);
		contentStream.showText(text2);
		contentStream.newLine();
		contentStream.showText(text3);
		contentStream.newLine();
		contentStream.showText(text4);
		contentStream.newLine();
		contentStream.showText(text5);
		contentStream.newLine();
		contentStream.showText(text6);
		contentStream.newLine();
		contentStream.showText(text7);
		contentStream.newLine();
		contentStream.showText(text8);
		
		contentStream.endText();
	}
	
	private void populateItemsTable() throws IOException {
		contentStream.beginText();
		contentStream.setNonStrokingColor(Color.WHITE);
		contentStream.setLeading(20.5f);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 20);
		contentStream.newLineAtOffset(25, 440);
		contentStream.showText(noSt+"    Description         Price        Qnt");
		contentStream.newLine();
		contentStream.newLine();
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.setFont(PDType1Font.COURIER, 12);
		if(rowCount > 0) {
			double sum = 0, price = 0;
			int qnt = 0;
			for(int i = 0; i < this.rowCount; i++){
				contentStream.showText(""+i+1);
				qnt = 0;
				for(int j = 0; j < this.colCount; j++){
					contentStream.showText(" - "+this.md.getValueAt(i, j));
					if(j == 1)
						price = Double.parseDouble(this.md.getValueAt(i, j).toString());
					if(j == 2)
						qnt = Integer.parseInt(this.md.getValueAt(i, j).toString());
					
					if(price > 0 && qnt > 0) {
						price = price * qnt;
						sum += price;						
					}
				}
				this.sum = sum;
				contentStream.newLine();
			}
		}
		contentStream.newLine();	
		contentStream.newLine();	
		
		this.sum = this.helper.getSumDiscounted(sum, discount, applyDiscount);

		contentStream.setFont(PDType1Font.COURIER, 18);
		
		char symbol = '€';
		if(!this.applyDiscount)
			symbol = '%';
		
		contentStream.showText("                         Discount          "+symbol+" "+df.format(this.discount));
		contentStream.newLine();	
		contentStream.showText("                         TOTAL            € "+df.format(this.sum));
				
		contentStream.newLine();	
		contentStream.newLine();	
		contentStream.showText(eightSpaceStr + "Free ");
		contentStream.newLine();	
		contentStream.showText(eightSpaceStr );
		for(int i = 0; i < this.freebies.length; i++){
			if(this.freebies[i])
				contentStream.showText(this.fv.FREEBIES_ARRAY[i]);
				
			if(i < this.freebies.length - 1)
				contentStream.showText(", ");
	
			contentStream.newLine();
			contentStream.showText(eightSpaceStr);
		}
			
		contentStream.endText();
	}

	private void populateInvNumManufacturer() throws IOException {
		contentStream.beginText();
		contentStream.setLeading(20.5f);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 16);
		contentStream.setNonStrokingColor(Color.BLACK);
	
		if(!carManufacturer.contains(fv.COMPANY_STRING)){
			contentStream.newLineAtOffset(20, 520);
			contentStream.showText(invSt +" no."+invNo+" for "+carManufacturer);
		} else {
			int yPos = 570;
			String temp = "";
			contentStream.newLineAtOffset(20, yPos);
			temp = carManufacturer.substring(0, carManufacturer.indexOf(fv.COMPANY_STRING));
			contentStream.showText(invSt +" no."+invNo+" for "+temp);
			contentStream.newLine();
			
			temp = carManufacturer.substring(carManufacturer.indexOf(fv.COMPANY_STRING)+2, carManufacturer.lastIndexOf(fv.COMPANY_STRING));
			temp = temp.replaceAll(fv.COMPANY_STRING, ",");
			contentStream.showText(temp);
			contentStream.newLine();
	
			temp = carManufacturer.substring(carManufacturer.lastIndexOf(fv.COMPANY_STRING)+2);
			contentStream.showText(temp);
			contentStream.newLine();

		}
		contentStream.endText();
	}

	private void populateInvNumManufacturerRegistration() throws IOException {
		contentStream.beginText();
		contentStream.setLeading(20.5f);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 16);
		
		contentStream.setNonStrokingColor(Color.BLACK);
		if(!carManufacturer.contains(fv.COMPANY_STRING)){
			contentStream.newLineAtOffset(20, 520);
			contentStream.showText(invSt +" no."+invNo+" for "+carManufacturer + " reg "+carRegistration);
		} else {
			int yPos = 570;
			String temp = "";
			contentStream.newLineAtOffset(20, yPos);
			temp = carManufacturer.substring(0, carManufacturer.indexOf(fv.COMPANY_STRING));
			contentStream.showText(invSt +" no."+invNo+" for "+temp + " reg "+carRegistration);
			contentStream.newLine();
			
			temp = carManufacturer.substring(carManufacturer.indexOf(fv.COMPANY_STRING)+2, carManufacturer.lastIndexOf(fv.COMPANY_STRING));
			temp = temp.replaceAll(fv.COMPANY_STRING, ",");

			contentStream.showText(temp);
			contentStream.newLine();
	
			temp = carManufacturer.substring(carManufacturer.lastIndexOf(fv.COMPANY_STRING)+2);
			contentStream.showText(temp);
			contentStream.newLine();

		}
		contentStream.newLine();
		contentStream.endText();
	}

	private void createAccountancCopy() throws IOException {
		PDDocument customerCopyDoc = new PDDocument();
		PDPage page = new PDPage();
		customerCopyDoc.addPage(page);
		contentStream = new PDPageContentStream(customerCopyDoc, page);
		
		addLogo(customerCopyDoc);
		fillCompanyDetails();
		populateInvNumManufacturerRegistration();
		populateItemsTable();

		contentStream.close();

		accPath = accPath+"\\"+date+"_"+docNameCopy+" "+invNo+ext;
//		log.logError("acc path "+accPath);
		//TODO:
		customerCopyDoc.save(accPath);
		customerCopyDoc.close();
	}

	public void printPDF(String docPath) throws IOException, Exception{
//		System.out.println("printpdf "+docPath);
		PDDocument document = PDDocument.load(new File(docPath));

		if(this.printerName.isEmpty() || this.printerName == "")
        	this.printerName = this.fv.PRINTER_NAME;

        PrintService myPrintService = findPrintService(this.printerName);//this.fv.PRINTER_NAME
        PrintServiceAttributeSet set = myPrintService.getAttributes();
                
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.setPrintService(myPrintService);

        //TODO
        //Uncomment bellow
//        job.print();
        
        document.close();
   }
	
	private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
}