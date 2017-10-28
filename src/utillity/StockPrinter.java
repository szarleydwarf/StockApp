package utillity;

import java.awt.Color;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

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

	private DefaultListModel md;
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
		
//		this.savePath;
//		log.logError("log "+this.loggerFolderPath+"\t savepath "+this.savePath);
		this.stockServicesNumber = new ArrayList<String>();
	}
	
	public void printCleanPDF() throws Exception {
		PDDocument empty = new PDDocument();
		PDPage page = new PDPage();
		empty.addPage(page);
		contentStream = new PDPageContentStream(empty, page);
		contentStream.close();
		String path = savePath+loggerFolderPath+"empty.pdf";
//		log.logError("print empty pdf "+path);
		empty.save(path);
		empty.close();
		
		this.printPDF(path);
	}
	
	public boolean printDoc(JList<String> list, double discount, boolean applyDiscount, String carManufacturer, String registration, int invoiceNum) throws Exception{
		this.date = helper.getFormatedDate();
		
		savePath = savePath.concat(this.date);
		helper.createFolderIfNotExist(savePath);
		
		accPath = savePath+slash + "_accountacy copy"+slash;
		helper.createFolderIfNotExist(accPath);

		this.df = new DecimalFormat(this.fv.DECIMAL_FORMAT); 
		this.md = (DefaultListModel)list.getModel();
		this.discount = discount;
		this.applyDiscount = applyDiscount;
		this.carManufacturer = carManufacturer;
		if(!registration.isEmpty())
			this.carRegistration = registration;
		this.invNo = invoiceNum;
		
		generatePDF();

		printPDF(docPath);

		createAccountancCopy();
		saveEntryToDatabase();
		return jobDone ;
	}


	private void saveEntryToDatabase() throws SQLException {
		String servNo = "", itemNo = "";
		for(String s : stockServicesNumber){
			if(s.contains("AAA")){
				itemNo += s+",";
			}else if(s.contains("AAS")){
				servNo += s+",";
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
		this.invoiceFileName = date+"\\"+this.fileName;
		docPath = savePath+slash+this.fileName;
		//TODO
//		log.logError("save path "+savePath +"docpath "+this.docPath+" "+this.docNameCopy+"\t invName "+this.invoiceFileName);
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

	private void populateItemsTable() throws IOException {
		boolean isService = false;
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
		
		if(md.size() > 0){
			for(int i = 0; i < md.size(); i++){
				String tempSt = md.getElementAt(i).toString();
				String description = tempSt.substring(0, tempSt.lastIndexOf("€"));
				
				if(description.contains("Wash"))
					isService=true;
				
				createItemList(description);
				
				String priceSt = tempSt.substring(tempSt.lastIndexOf("€")+1);
				priceSt = priceSt.substring(0, priceSt.lastIndexOf("x"));
				String quant = tempSt.substring(tempSt.lastIndexOf("x")+1);
				
				if(quant.isEmpty()) {
					quant = "1";
				}

				priceSt = helper.paddStringLeft(priceSt, stringLengthF, paddingChar);
				priceSt = helper.paddStringRight(priceSt, stringLengthB, paddingChar);
				contentStream.showText((i+1)+" - "+description+ "  " +priceSt+"  "+quant);
				contentStream.newLine();			
			}
		}		

		contentStream.newLine();	
		contentStream.newLine();	
		sum = helper.getSum(this.md, this.discount, this.applyDiscount);
		contentStream.setFont(PDType1Font.COURIER, 18);
		contentStream.showText("                         Discount          € "+df.format(this.discount));
		contentStream.newLine();	
		contentStream.showText("                         TOTAL            € "+df.format(this.sum));
		
		String freebie = "tire paint, ";
		
		contentStream.newLine();	
		contentStream.newLine();	
		contentStream.showText("           Free ");
		if(isService)
			contentStream.showText(freebie);
		contentStream.showText("air freshener");
			
		contentStream.endText();
	}

	private void createItemList(String description) {
		String des = description.trim();
		if(!des.contains(this.fv.OTHER_STRING_CHECKUP)){
			String query = "SELECT "+this.fv.SERVICE_TABLE_NUMBER+" FROM "+this.fv.SERVICES_TABLE+" WHERE "+this.fv.SERVICES_TABLE_SERVICE_NAME+"=\""+des+"\" union all SELECT "+this.fv.STOCK_TABLE_NUMBER+" FROM "+this.fv.STOCK_TABLE+" WHERE "+this.fv.STOCK_TABLE_ITEM_NAME+"=\""+des+"\"";
			try {
				ArrayList<String> t = DM.selectRecordArrayList(query);
				for(String ts : t){
					if(!stockServicesNumber.contains(ts)){
						this.stockServicesNumber.add(ts);
					} 
				}
			} catch (Exception e) {
				log.logError(date+" "+this.getClass().getName()+"\t"+e.getMessage());
			}
		}
	}

	private void populateInvNumManufacturer() throws IOException {
		contentStream.beginText();
		contentStream.setLeading(20.5f);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 16);
		
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(140, 520);
		contentStream.showText(invSt +" no."+invNo+" for "+carManufacturer);

		contentStream.endText();
	}

	private void populateInvNumManufacturerRegistration() throws IOException {
		contentStream.beginText();
		contentStream.setLeading(20.5f);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 16);
		
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(120, 520);
		contentStream.showText(invSt +" no."+invNo+" for "+carManufacturer);
		contentStream.newLine();
		contentStream.showText( "    reg "+carRegistration);

		contentStream.endText();
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
	
		//TODO:
		//add test for folder existance - create folder.
		accPath = accPath+"\\"+date+"_"+docNameCopy+" "+invNo+ext;
//		log.logError("acc path "+accPath);
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