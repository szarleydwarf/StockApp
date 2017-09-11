package utillity;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class StockPrinter implements Printable {
	public void printDoc(){
		
	}

	@Override
	public int print(Graphics graphic, PageFormat pf, int pageNumber) throws PrinterException {
		if(pageNumber > 0) {
			return NO_SUCH_PAGE;
		}
		return PAGE_EXISTS;
	}
}
