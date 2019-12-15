package cwb.cmt.upperair.createImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import cwb.cmt.upperair.model.Station;

@Service
public class CreateContentsImage {

	private String kaiuFilePath;

	private YearMonth yearMonth;

	private int pageNumber = 1;

	private static final String BOOKTITLE = "PART II - UPPER AIR DATA";
	private static final String TITLE = "目    錄        Contents";
	private static final String EXPLINATION = "凡 例      Reference Notes ....................................................... i";
	private static final String JAN00 = "一  月     00世界時      00UT January.............................................";
	private static final String JAN12 = "           12 世界時     12UT ....................................................";
	private static final String FEB00 = "二  月     00 世界時     00UT Febuary.............................................";
	private static final String FEB12 = "           12 世界時     12UT ....................................................";
	private static final String MAR00 = "三  月     00 世界時     00UT March...............................................";
	private static final String MAR12 = "           12 世界時     12UT ....................................................";
	private static final String APR00 = "四  月     00 世界時     00UT April...............................................";
	private static final String APR12 = "           12 世界時     12UT ....................................................";
	private static final String MAY00 = "五  月     00 世界時     00UT May.................................................";
	private static final String MAY12 = "           12 世界時     12UT ....................................................";
	private static final String JUN00 = "六  月     00 世界時     00UT June................................................";
	private static final String JUN12 = "           12 世界時     12UT ....................................................";
	private static final String JUL00 = "七  月     00 世界時     00UT July................................................";
	private static final String JUL12 = "           12 世界時     12UT ....................................................";
	private static final String AUG00 = "八  月     00 世界時     00UT August..............................................";
	private static final String AUG12 = "           12 世界時     12UT ....................................................";
	private static final String SEP00 = "九  月     00 世界時     00UT September...........................................";
	private static final String SEP12 = "           12 世界時     12UT ....................................................";
	private static final String OCT00 = "十  月     00 世界時     00UT October.............................................";
	private static final String OCT12 = "           12 世界時     12UT ....................................................";
	private static final String NOV00 = "十一月     00 世界時     00UT November............................................";
	private static final String NOV12 = "           12 世界時     12UT ....................................................";
	private static final String DEC00 = "十二月     00 世界時     00UT December............................................";
	private static final String DEC12 = "           12 世界時     12UT ....................................................";

	private List<String> getLines() {
		return Arrays.asList(new String[] { JAN00, JAN12, FEB00, FEB12, MAR00, MAR12, APR00, APR12, MAY00, MAY12, JUN00,
				JUN12, JUL00, JUL12, AUG00, AUG12, SEP00, SEP12, OCT00, OCT12, NOV00, NOV12, DEC00, DEC12 });
	}

	public void init(YearMonth yearMonth, String kaiuFilePath) {
		this.yearMonth = yearMonth;
		this.kaiuFilePath = kaiuFilePath;
		this.pageNumber = 1;
	}

	/**
	 * 
	 * @param stnCount
	 * @param station
	 * @param fileAbsolutePath
	 * @throws FileNotFoundException fileAbsolutePath is a directory or it can't be opened
	 * @throws DocumentException font is not valid
	 * @throws IOException can not read font file
	 */
	public void createContents(int stnCount, Station station, String fileAbsolutePath) 
							throws FileNotFoundException, DocumentException, IOException {
		
		File outfile = new File(Paths.get(fileAbsolutePath).toString());
		
		BaseFont baseFont = BaseFont.createFont(kaiuFilePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font titleFont = new Font(baseFont, 14, Font.BOLD);
		Font normalFont = new Font(baseFont, 12, Font.NORMAL);
		
		Document document = new Document();
		try{
			PdfWriter.getInstance(document, new FileOutputStream(outfile));
			document.open();
			document.setPageSize(PageSize.A4);
			try {
				writeToDocument(stnCount, station, document, titleFont, normalFont);
			} catch (DocumentException e) {
				// DocumentException is thrown when document is close, so open it again
				document.open();
				writeToDocument(stnCount, station, document, titleFont, normalFont);
			}
		} catch (DocumentException e) {
			// This exception is thrown is because that document is added PdfWriter more than one time
			// But we won't hapen
		} finally {
			document.close();
		}
	}

	private void writeToDocument(int stnCount, Station station, Document document, Font titleFont, Font normalFont) 
			throws DocumentException {
    	if(stnCount == 1){ 
        	Paragraph bookTitlePg = new Paragraph(BOOKTITLE, titleFont);
        	Paragraph titlePg = new Paragraph(TITLE, titleFont);
        	Paragraph explinationPg = new Paragraph(EXPLINATION, normalFont);
        	bookTitlePg.setAlignment(Element.ALIGN_CENTER);
        	titlePg.setAlignment(Element.ALIGN_CENTER);
        	document.add(bookTitlePg);
            document.add(titlePg);
            document.add(Chunk.NEWLINE); // add blank line
            document.add(Chunk.NEWLINE);
            document.add(explinationPg);
        } else {
        	Paragraph blankLine = new Paragraph("  ");
        	document.add(blankLine);
            document.add(blankLine);
            document.add(blankLine);
            document.add(blankLine);
            document.add(blankLine);
        }
        	
    	// add station name
    	Paragraph stnPg = new Paragraph(stnCount + ".  "+ station.getStnCName() + "  " + station.getStnEName(), normalFont);
    	document.add(stnPg);
        	
    	// add lines
    	List<String> allLines = getLines();
    	for(int i=0; i<2*yearMonth.getMonthValue(); i++) {
    		Paragraph pg = new Paragraph(allLines.get(i) + " " + pageNumber, normalFont);
    		document.add(pg);
    		pageNumber += 5;
    	}
    }
	
}
