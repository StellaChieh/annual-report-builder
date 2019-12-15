package cwb.cmt.upperair.process;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.YearMonth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import cwb.cmt.upperair.createImage.CreateContentsImage;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.PageInfo;
import cwb.cmt.upperair.utils.PdfPageType;

@Service
public class ProcessContents {

	@Autowired
	CreateContentsImage createContentsImage;
	
	@Autowired
	private PageInfoManager pageInfoContainer;
	
	private String tmpFileFolder;
		
	private String kaiuFontFilePath;
	
	private int stnCount = 1;
	
	private static final Logger logger = LogManager.getLogger();
	
	public void init(String tmpFileFolder, YearMonth yearMonth, String kaiuFilePath) {
		this.tmpFileFolder = tmpFileFolder;
		this.kaiuFontFilePath = kaiuFilePath;
		this.stnCount = 1;
		createContentsImage.init(yearMonth, kaiuFontFilePath);
	}
	
	/**
	 * 
	 * @param station
	 * @throws IOException can not read font file
	 * @throws DocumentException font is not validI
	 */
	public void drawSingleContentsPage(Station station) throws IOException, DocumentException {
		String filename = station.getStnCName() + "測站目錄.pdf";
		String fileAbsolutePath = Paths.get(tmpFileFolder, filename).toString();
		PageInfo pageInfo = pageInfoContainer.createNewPageInfo(PdfPageType.CONTENTS);
		pageInfo.setFilename(filename);
		pageInfo.setFilePath(fileAbsolutePath);
		try {
			createContentsImage.createContents(stnCount, station, fileAbsolutePath);
		} catch (FileNotFoundException e) {
			// we rename the output file and try again
			filename = station.getStnCName() + "測站目錄_1.pdf";
			fileAbsolutePath = Paths.get(tmpFileFolder, filename).toString();
			pageInfo.setFilename(filename);
			pageInfo.setFilePath(fileAbsolutePath);
			createContentsImage.createContents(stnCount, station, fileAbsolutePath);
		}
		stnCount++;
		logger.info("Draw " + station.getStnEName() + " contents.");
	}
}
