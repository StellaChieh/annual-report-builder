package cwb.cmt.upperair.process;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import cwb.cmt.upperair.utils.PageInfo;
import cwb.cmt.upperair.utils.PdfPageType;

@Service
public class ProcessOuterFile {

	private String tmpFileFolder;
	
	@Autowired
	private PageInfoManager pageInfoContainer;
	
	private static final Logger logger = LogManager.getLogger();
	
	public void splitMultiplePdfsAndPutIntoPdfContainer(PdfPageType pageType, String inFile) throws IOException {
	
		String inFileName = Paths.get(inFile).getFileName().toString();
		
			PdfReader reader = new PdfReader(inFile);
			int n = reader.getNumberOfPages();
			int i=1;
			while(i<=n) {
				Document document = null;
				PdfCopy writer = null;
				String outFileName = pageType.name() + "_" + i + ".pdf";
				String outFileFullPath = Paths.get(tmpFileFolder, outFileName).toString();
				try {
					document = new Document(reader.getPageSizeWithRotation(1));
					writer = new PdfCopy(document, new FileOutputStream(outFileFullPath));
					document.open();
					PdfImportedPage page = writer.getImportedPage(reader, i);
					writer.addPage(page);
					
					PageInfo pageInfo = pageInfoContainer.createNewPageInfo(pageType);
					pageInfo.setFilename(outFileName);
					pageInfo.setFilePath(outFileFullPath);
					
				} catch (BadPdfFormatException e) {
					logger.error(inFileName + " has bad pdf format so that can't split into single page.", e);
					continue;
				} catch (DocumentException e) {
					logger.error("can't write to " + outFileName, e);
					continue;
				} catch (IOException e ){
					logger.error("can't write to " + outFileName, e);
					continue;
				} finally {
					i++;
					if(document != null) {
						document.close();
					}
					if(writer != null) {
						writer.close();
					}
				} 
			}
			logger.info("Move " + inFile + " into tmp folder.");
	}


	public void setTmpFileFolder(String tmpFileFolder) {
		this.tmpFileFolder = tmpFileFolder;
	}

}
