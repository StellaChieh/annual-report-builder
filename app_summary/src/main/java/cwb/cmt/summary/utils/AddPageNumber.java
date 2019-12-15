package cwb.cmt.summary.utils;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class AddPageNumber {

	// only for testing
	public static final String SOURCE = "D:\\work\\20170710_cmt年報\\RPT\\summary\\6_0_stationMoves-1971_paged.pdf";
    public static final String DEST = "D:\\work\\20170710_cmt年報\\RPT\\summary\\test-addPageNumber.pdf";
    
    // only for testing
    public static void main(String[] args) throws IOException, DocumentException {
        add(SOURCE, DEST, "X");
    }
    
    public static void add(String sourceAbsolutePath, String destAbsolutePath, String pageNumber) throws IOException, DocumentException {
        // new document
    	Document document = new Document(PageSize.A4);
    	
    	// pdf writer
    	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destAbsolutePath));

    	// open document
    	document.open();
    	
    	// pdf canvas
    	PdfContentByte canvas = writer.getDirectContentUnder();
    	
    	// pdf reader
    	PdfReader reader = new PdfReader(sourceAbsolutePath);
    	PdfImportedPage page = writer.getImportedPage(reader, 1);  // one pdf file only has one page
    	
    	// wrap source pdf in image and put this image on the destination file 
        Image image = Image.getInstance(page);
        image.scaleAbsolute(PageSize.A4);
        image.setAbsolutePosition(0, 0);
        canvas.addImage(image);
        
        // add page number
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER
        		, new Phrase(pageNumber),  297.5f, 15, 0); // number, x, y rotation
        
        // close
        document.close();
        reader.close(); 
    }
    
}
