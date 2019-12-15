package cwb.cmt.upperair.process;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import cwb.cmt.upperair.createImage.CreateInterleafImage;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.PageInfo;
import cwb.cmt.upperair.utils.PdfPageType;

@Service
public class ProcessInterleaf {

	@Autowired
	CreateInterleafImage createInterleaf;
	
	@Autowired
	private PageInfoManager pageInfoContainer;
	
	private String tmpFileFolder;
	
	private String kaiuFontFilePath;
	
	private int stnCount = 1;
	
	private static final Logger logger = LogManager.getLogger();
	
	public void init(String tmpFileFolder, String kaiuFilePath) {
		this.tmpFileFolder = tmpFileFolder;
		this.kaiuFontFilePath = kaiuFilePath;
		this.stnCount = 1;
		createInterleaf.init(kaiuFontFilePath);
	}
	
	public void drawInterleafPage(Station station) throws IOException, DocumentException {
		String filename = station.getStnCName() + "測站插頁.pdf";
		String fileAbsolutePath = Paths.get(tmpFileFolder, filename).toString();
		PageInfo pageInfo = pageInfoContainer.createNewPageInfo(PdfPageType.INTERLEAF);
		pageInfo.setFilename(filename);
		pageInfo.setFilePath(fileAbsolutePath);
		createInterleaf.createInterleaf(stnCount++, station, fileAbsolutePath);
		logger.info("Draw " + station.getStnEName() + " interleaf.");
	}
}
