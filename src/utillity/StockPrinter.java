package utillity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class StockPrinter implements Printable {
	private final String savePath = "D:/@Development/__TEMP/", imagePath = "D:/@Development/EclipseJavaProjects/sqliteTestApp/StockApp/resources/img/Logo HCT 245x84.png";
	private String docName = "lol.pdf";
	private String docPath = "";
	private PDPageContentStream contentStream ;
	private DecimalFormat df;

	private DefaultListModel md;
	private double sum = 0, discount = 0;
	private boolean applyDiscount = false;
	private int invNo = 1;
	private String carManufacturer = "NONE";
	
	private Helper helper;
	
	public StockPrinter(){
		helper = new Helper();
	}
	
	public void printDoc(JList<String> list, double discount, boolean applyDiscount, String carManufacturer, int invoiceNum) throws IOException{
		this.df = new DecimalFormat("#.##"); 
		this.md = (DefaultListModel)list.getModel();
		this.discount = discount;
		this.applyDiscount = applyDiscount;
		this.carManufacturer = carManufacturer;
		this.invNo = invoiceNum;
		
//		System.out.println("Printing "+discount+" "+applyDiscount);
		generatePDF();
	}

	private void generatePDF()  throws IOException{
		PDDocument doc = new PDDocument();
		PDPage page = new PDPage();
		doc.addPage(page);
		contentStream = new PDPageContentStream(doc, page);
		PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
				
		contentStream.drawImage(pdImage, 215,  675);
		contentStream.setNonStrokingColor(Color.GRAY);
		contentStream.addRect(15, 480, 580, 50);
		contentStream.fill();
		
		fillCompanyDetails();

		// table of services/products done
		contentStream.beginText();
		contentStream.setLeading(20.5f);
		contentStream.setFont(PDType1Font.COURIER_BOLD, 20);
		
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.newLineAtOffset(25f, 560);
		contentStream.showText("Invoice no."+invNo+" for "+carManufacturer);
		contentStream.newLine();
		contentStream.newLine();
		contentStream.newLine();
//		
		contentStream.setNonStrokingColor(Color.WHITE);
		contentStream.showText("No.    Description         Price    Quantity");
		contentStream.newLine();
		contentStream.newLine();
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.setFont(PDType1Font.COURIER, 18);
		
		if(md.size() > 0){
			for(int i = 0; i < md.size(); i++){
				String tempSt = md.getElementAt(i).toString();
				contentStream.showText((i+1)+"    -   "+tempSt+"");
				contentStream.newLine();			
			}
		}		

		contentStream.newLine();	
		contentStream.newLine();	
		sum = helper.getSum(this.md, this.discount, this.applyDiscount);
		contentStream.showText("                         TOTAL            € "+df.format(this.sum));
		
		
		contentStream.endText();
		//table of services/products done
	
		contentStream.close();
		
		
		//TODO:
		// docName change to the date with customer name and number eg today/total
		docPath = savePath+docName;
		doc.save(docPath);
		doc.close();
	}

	private void fillCompanyDetails() throws IOException {
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
		
		String date = df.format(today.getTime());

		
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

	@Override
	public int print(Graphics graphic, PageFormat pf, int pageNumber) throws PrinterException {
		if(pageNumber > 0) {
			return NO_SUCH_PAGE;
		}
		return PAGE_EXISTS;
	}
}