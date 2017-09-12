package utillity;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
