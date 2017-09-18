package utillity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

public class StockPrinter implements Printable {
	private final String savePath = "D:/@Development/__TEMP/", imagePath = "D:/@Development/EclipseJavaProjects/sqliteTestApp/StockApp/resources/img/Logo HCT 245x84.png";
	private String ext = ".pdf", docNameCopy = "AC_";
	private String docPath = "";
	private PDPageContentStream contentStream ;
	private DecimalFormat df;

	private DefaultListModel md;
	private double sum = 0, discount = 0;
	private boolean applyDiscount = false;
	private int invNo = 1, stringLengthF = 3, stringLengthB = 12;
	private char paddingChar = ' ';
	private String carManufacturer = "NONE", carRegistration = "00AA0000", invSt = "Invoice", noSt = "No.", date;
	
	private Helper helper;
	
	public StockPrinter(){
		helper = new Helper();
	}
	
	public void printDoc(JList<String> list, double discount, boolean applyDiscount, String carManufacturer, String registration, int invoiceNum) throws IOException{
		this.df = new DecimalFormat("#.##"); 
		this.md = (DefaultListModel)list.getModel();
		this.discount = discount;
		this.applyDiscount = applyDiscount;
		this.carManufacturer = carManufacturer;
		if(!registration.isEmpty())
			this.carRegistration = registration;
		this.invNo = invoiceNum;
		
		this.date = helper.getFormatedDate();
//		System.out.println("Printing "+discount+" "+applyDiscount);
		generatePDF();
		createAccountancCopy();
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
		//table of services/products done
	
		contentStream.close();
	
		//TODO:
		// docName change to the date with customer name and number eg today/total
		//add test for folder existance - create folder.
		docPath = savePath+" "+date+" "+invNo+ext;
		customerCopyDoc.save(docPath);
		customerCopyDoc.close();
	}

	private void addLogo(PDDocument customerCopyDoc) throws IOException {
		PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, customerCopyDoc);
		
		contentStream.drawImage(pdImage, 215,  675);
		contentStream.setNonStrokingColor(Color.GRAY);
		contentStream.addRect(15, 420, 580, 50);
		contentStream.fill();

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
		contentStream.setFont(PDType1Font.COURIER, 18);
		
		if(md.size() > 0){
			for(int i = 0; i < md.size(); i++){
				String tempSt = md.getElementAt(i).toString();
				String description = tempSt.substring(0, tempSt.lastIndexOf("€"));
				
				String priceSt = tempSt.substring(tempSt.lastIndexOf("€")+1);
				priceSt = priceSt.substring(0, priceSt.lastIndexOf("x"));
				String quant = tempSt.substring(tempSt.lastIndexOf("x")+1);
				
				if(quant.isEmpty()) {
					quant = "1";
				}

				priceSt = helper.paddStringLeft(priceSt, stringLengthF, paddingChar);
				priceSt = helper.paddStringRight(priceSt, stringLengthB, paddingChar);
				contentStream.showText((i+1)+"   -  "+description+ "  " +priceSt+"  "+quant);
				contentStream.newLine();			
			}
		}		

		contentStream.newLine();	
		contentStream.newLine();	
		sum = helper.getSum(this.md, this.discount, this.applyDiscount);
		contentStream.showText("                         Discount            € "+df.format(this.discount));
		contentStream.newLine();	
		contentStream.showText("                         TOTAL            € "+df.format(this.sum));
		
		
		contentStream.endText();

	}

	private void populateInvNumManufacturer() throws IOException {
		contentStream.beginText();
		contentStream.setLeading(20.5f);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 20);
		
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(140, 520);
		contentStream.showText(invSt +" no."+invNo+" for "+carManufacturer);

		contentStream.endText();
	}

	private void populateInvNumManufacturerRegistration() throws IOException {
		contentStream.beginText();
		contentStream.setLeading(20.5f);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 20);
		
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(120, 520);
		contentStream.showText(invSt +" no."+invNo+" for "+carManufacturer+ " reg "+carRegistration);

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
		contentStream.showText(text + "                                        " + date);
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
		// docName change to the date with customer name and number eg today/total
		//add test for folder existance - create folder.
		docPath = savePath+" "+date+docNameCopy+" "+invNo+ext;
		customerCopyDoc.save(docPath);
		customerCopyDoc.close();
	}

	
	@Override
	public int print(Graphics graphic, PageFormat pf, int pageNumber) throws PrinterException {
		if(pageNumber > 0) {
			return NO_SUCH_PAGE;
		}
		return PAGE_EXISTS;
	}
}