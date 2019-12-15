package cwb.cmt.surface.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

@Service("createTableImageForFirstInterleaf")
public class CreateTableImageForFirstInterleaf {

	private String filename;
	private String outputTmpPdfPath;
	
	@Resource(name="rootPath")
	private String rootPath;
	
	
	private String kaiuFilePath;
	@Resource(name="kaiuFilePath")
	public void setKaiuFilePath(String kaiuFilePath){
		this.kaiuFilePath = Paths.get(rootPath, kaiuFilePath).toString();
	}
	
	public void createTableImage(Object... objects) throws DocumentException, IOException{
		
		String cnTitle = (String) objects[0];
		String enTitle = (String) objects[1];
		
		File file = new File(Paths.get(outputTmpPdfPath,filename).toString()+ ".pdf");
		Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        document.setPageSize(PageSize.A4);  
        BaseFont baseFont = BaseFont.createFont(kaiuFilePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font cnTitleFont = new Font(baseFont, 20, Font.BOLD);
        Font entitleFont = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        
        for (int i=0; i<9; i++){
        	Paragraph pg = new Paragraph(" ", cnTitleFont);
        	document.add(pg);
        }
        
        Paragraph pg = new Paragraph(" ", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD));
        Paragraph pgChineseTitle = new Paragraph(cnTitle, cnTitleFont);
        Paragraph pgEnglishTitle = new Paragraph(enTitle, entitleFont);
    	pg.setAlignment(Element.ALIGN_CENTER);
    	pgChineseTitle.setAlignment(Element.ALIGN_CENTER);
    	pgEnglishTitle.setAlignment(Element.ALIGN_CENTER);
    	document.add(pgChineseTitle);
    	document.add(pg);
        document.add(pgEnglishTitle);
        document.close();
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setOutputTmpPdfPath(String outputTmpPdfPath) {
		this.outputTmpPdfPath = outputTmpPdfPath;
	}

}
