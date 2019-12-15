package cwb.cmt.surface.pdfProcess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class AddPageNumber {

	private static PdfReader reader = null; // source
	private static PdfStamper stamper = null; // destination

	/*
	static void print(String filename, String pageNumber) throws FileNotFoundException, DocumentException, IOException {

		String destFilename = Paths.get(FilenameUtils.removeExtension(filename)).toString(); // remove ".pdf" extension
		try {
			reader = new PdfReader(new FileInputStream(filename));
			stamper = new PdfStamper(reader, new FileOutputStream(destFilename + "_paged.pdf"));
			PdfContentByte cb = stamper.getOverContent(1); // get page one
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(pageNumber), 297.5f, 28, 0); // number, x, y
																											// rotation
		} finally {
			stamper.close();
			reader.close();
			Files.deleteIfExists(Paths.get(filename)); // delete pdf after it was added page number
		}
	}
	*/

	public static void print(String sourceAbsolutePath, String pageNumber)
											throws IOException, DocumentException {
		
		// destination file name
		String destFilename = Paths.get(FilenameUtils.removeExtension(sourceAbsolutePath)) // remove ".pdf" extension
													.toString()+ "_paged.pdf";  // add "_paged.pdf"
															
		// new document
		Document document = new Document(PageSize.A4);

		// pdf writer
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destFilename));

		// open document
		document.open();

		// pdf canvas
		PdfContentByte canvas = writer.getDirectContentUnder();

		// pdf reader
		PdfReader reader = new PdfReader(sourceAbsolutePath);
		PdfImportedPage page = writer.getImportedPage(reader, 1); // one pdf file only has one page

		// wrap source pdf in image and put this image on the destination file
		Image image = Image.getInstance(page);
		image.scaleAbsolute(PageSize.A4);
		image.setAbsolutePosition(0, 0);
		canvas.addImage(image);

		// add page number
		ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER
									, new Phrase(pageNumber), 297.5f, 15, 0); // number, x, y
	    // rotation

		// close
		document.close();
		reader.close();
		Files.deleteIfExists(Paths.get(sourceAbsolutePath)); // delete pdf after it was added page number
	}
}
