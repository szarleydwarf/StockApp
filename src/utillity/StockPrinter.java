package utillity;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class StockPrinter implements Printable {
	private final String savePath = "D:/@Development/__TEMP/", imagePath = "D:/@Development/EclipseJavaProjects/sqliteTestApp/StockApp/resources/img/Logo HCT 245x84.png";
	private String docName = "lol1.pdf";
	private String docPath = "";
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
	private PDPageContentStream contentStream ;
	
	private ArrayList<ArrayList<String>> servicesList;
	
	public StockPrinter(){
		
	}
=======
>>>>>>> parent of 3fa68d2... 14/9/17
=======
>>>>>>> parent of 3fa68d2... 14/9/17
=======
>>>>>>> parent of 3fa68d2... 14/9/17
	
	public StockPrinter(ArrayList<ArrayList<String>> servicesList) {
		this.servicesList = servicesList;
	}

	public void printDoc() throws IOException{
//		System.out.println("Printing");
		generatePDF();
	}

	private void generatePDF()  throws IOException{
		PDDocument doc = new PDDocument();
		PDPage page = new PDPage();
		doc.addPage(page);
		PDPageContentStream contentStream = new PDPageContentStream(doc, page);
		PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
				
		contentStream.drawImage(pdImage, 215,  675);
		contentStream.setNonStrokingColor(Color.GRAY);
		contentStream.addRect(15, 480, 580, 50);
		contentStream.fill();
		
		fillCompanyDetails();

		// table of services/products done
		contentStream.setNonStrokingColor(Color.WHITE);
		contentStream.beginText();
		contentStream.setFont(PDType1Font.COURIER_BOLD, 20);
		contentStream.newLineAtOffset(25f, 500);
		contentStream.setLeading(20.5f);
		contentStream.showText("No.    Description       Quantity     Price");
		contentStream.newLine();
		contentStream.newLine();
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.setFont(PDType1Font.COURIER, 18);
		
		for(int i = 0; i < this.servicesList.size(); i++){
			ArrayList<String> tList = this.servicesList.get(i);
			contentStream.showText((i+1)+"    ");
			for(int j = 0; j < tList.size(); j++){
				contentStream.showText(tList.get(j)+"    ");
			}
			contentStream.newLine();			
		}
		
		contentStream.showText("              TOTAL                            �7");
		
		
		contentStream.endText();
		//table of services/products done
	
		contentStream.close();
=======
>>>>>>> parent of 3fa68d2... 14/9/17
=======
>>>>>>> parent of 3fa68d2... 14/9/17
=======
>>>>>>> parent of 3fa68d2... 14/9/17
		
		Calendar today = Calendar.getInstance();
		SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
		
		String date = df.format(today.getTime());
		System.out.println(page.getArtBox());
		
		contentStream.drawImage(pdImage, 200,  675);
		contentStream.beginText();
		contentStream.setFont(PDType1Font.COURIER, 18);
		contentStream.newLineAtOffset(7.5f,  740);
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

	
		contentStream.close();
		
		
		//TODO:
		// docName change to the date with customer name and number eg today/total
		docPath = savePath+docName;
		doc.save(docPath);
		doc.close();
	}

	@Override
	public int print(Graphics graphic, PageFormat pf, int pageNumber) throws PrinterException {
		if(pageNumber > 0) {
			return NO_SUCH_PAGE;
		}
		return PAGE_EXISTS;
	}
}
