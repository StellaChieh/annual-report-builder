package cwb.cmt.upperair.createImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

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
public class CreateInterleafImage {
	
	private String kaiuFilePath;
	
	public void init(String kaiuFilePath) {
		this.kaiuFilePath = kaiuFilePath;
	}
	
	public void createInterleaf(int stnCount, Station station, String fileAbsolutePath) 
			throws IOException, DocumentException {
		File outfile = new File(Paths.get(fileAbsolutePath).toString());
    	Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outfile));
        document.open();
        document.setPageSize(PageSize.A4);  
        BaseFont baseFont = BaseFont.createFont(kaiuFilePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 30, Font.BOLD); 
        Paragraph no = new Paragraph(String.valueOf(stnCount) + ".", font);
        Paragraph stnC = new Paragraph(station.getStnCName(), font);
        Paragraph stnE = new Paragraph(station.getStnEName(), font);
        no.setAlignment(Element.ALIGN_CENTER);
        stnC.setAlignment(Element.ALIGN_CENTER);
        stnE.setAlignment(Element.ALIGN_CENTER);
        Paragraph blankLine = new Paragraph("  ", font);
    	document.add(blankLine);
        document.add(blankLine);
        document.add(blankLine);
        document.add(blankLine);
        document.add(blankLine);
        document.add(no);
        document.add(stnC);
        document.add(stnE);
        document.close();
	}

}