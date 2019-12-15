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
public class CreateTableImageForPartOneInterleaf {

	private String filename;
	private String outputTmpPdfPath;
	private String kaiuFilePath;
	
	@Autowired
	public CreateTableImageForPartOneInterleaf (Config config) {
		this.kaiuFilePath = config.getKaiuFilePath();
	}
	
	public void createTableImage(int year) throws DocumentException, IOException{
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
        
        String yearRange = String.valueOf(year)+ "-" + String.valueOf(year+9);
        Paragraph pg = new Paragraph( "第一部\n"
        							+ "各 氣 象 要 素 總 表\n"
        							+ "Part One\n"
        							+ "Extremes and mean of monthly values\n"
        							+ "of\n"
        							+ "different meteorological elements\n"
        							+ yearRange, titleFont);
        
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
