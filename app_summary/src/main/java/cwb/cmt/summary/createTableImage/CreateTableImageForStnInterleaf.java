package cwb.cmt.summary.createTableImage;

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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.utils.RomanNumber;

@Service
public class CreateTableImageForStnInterleaf {

	private String filename;
	
	private String outputTmpPdfPath;
	
	@Resource(name="rootPath")
	private String rootPath;
	
	private String kaiuFilePath;
	@Resource(name="kaiuFilePath")
	public void setKaiuFilePath(String kaiuFilePath){
		this.kaiuFilePath = Paths.get(rootPath, kaiuFilePath).toString();
	}
	
	
	public void createTableImage(int stnCount, Station stn) throws DocumentException, IOException{
		File file = new File(Paths.get(outputTmpPdfPath ,filename).toString()+ ".pdf");
      
		Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        document.setPageSize(PageSize.A4);  
        BaseFont baseFont = BaseFont.createFont(kaiuFilePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED );
        Font titleFont = new Font(baseFont, 22, Font.BOLD);
        
        for (int i=0; i<8; i++){
        	Paragraph pg = new Paragraph(" ", titleFont);
        	document.add(pg);
        }
        
        Paragraph pg = new Paragraph( RomanNumber.toRoman(stnCount)+"\n"
        							+ stn.getStnCName() +"\n"
        							+ stn.getStnEName() +"\n"
        							, titleFont);
        
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
