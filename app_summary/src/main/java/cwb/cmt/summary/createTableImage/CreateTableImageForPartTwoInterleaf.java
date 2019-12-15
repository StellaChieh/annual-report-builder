package cwb.cmt.summary.createTableImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import cwb.cmt.summary.config.Config;

@Service
public class CreateTableImageForPartTwoInterleaf {

	private String filename;
	
	private String outputTmpPdfPath;
		
	private String kaiuFilePath;
	
	@Autowired
	public CreateTableImageForPartTwoInterleaf (Config config) {
		this.kaiuFilePath = config.getKaiuFilePath();
	}
	
	public void createTableImage() throws DocumentException, IOException{
		File file = new File(Paths.get(outputTmpPdfPath,filename).toString()+ ".pdf");
      
		Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        document.setPageSize(PageSize.A4);  
        BaseFont baseFont = BaseFont.createFont(kaiuFilePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED );
        Font titleFont = new Font(baseFont, 22, Font.BOLD);
        
        for (int i=0; i<5; i++){
        	Paragraph pg = new Paragraph(" ", titleFont);
        	document.add(pg);
        }
        
        Paragraph pg = new Paragraph( "第二部\n"
        							+ "各 測 站 氣 象 要 素 表\n"
        							+ "Part Two\n"
        							+ "Annual monthly mean values and its extremes\n"
        							+ "of\n"
        							+ "different elements of different stations", titleFont);
        
    	pg.setAlignment(Element.ALIGN_CENTER);
        document.add(pg);
        document.close();
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setOutputTmpPdfPath(String outputTmpPdfPath) {
		this.outputTmpPdfPath = outputTmpPdfPath;
	}
}
