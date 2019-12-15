package cwb.cmt.upperair.main;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.process.ProcessContents;
import cwb.cmt.upperair.process.ProcessInterleaf;
import cwb.cmt.upperair.process.ProcessOuterFile;
import cwb.cmt.upperair.process.ProcessPdfs;
import cwb.cmt.upperair.process.ProcessSignificantPdf;
import cwb.cmt.upperair.process.ProcessStandardPdf;
import cwb.cmt.upperair.utils.PdfPageType;
import cwb.cmt.upperair.utils.Utility;

@Service
public class CreatePdf extends CreateOutput {

	@Autowired
	private ProcessOuterFile processOuterFile;
	
	@Autowired
	private ProcessContents processContents;
	
	@Autowired
	private ProcessInterleaf processInterleaf;
	
	@Autowired
	private ProcessStandardPdf processStandard;
	
	@Autowired
	private ProcessSignificantPdf processSignificant;
	
	@Autowired
	private ProcessPdfs processPdfs;
	
	@Autowired
	@Qualifier("outputTmpPdfPath")
	private String tmpFileFolder;

	@Autowired
	@Qualifier("pdfFilename")
	private String outputPdfFile;
	
	// 新細明體字體位置
	@Autowired
	@Qualifier("kaiuFilePath")
	private String kaiuFontFilePath;
	
	private Map<PdfPageType, String> outerFileMap;
	
	private static final Logger logger = LogManager.getLogger();
				
	@Override
	protected boolean prepareResourceFiles() {
		if(!super.prepareResourceFiles()) {
			return false;
		}
		kaiuFontFilePath = makeAbsolutelyPath(rootPath, kaiuFontFilePath);
		tmpFileFolder = makeAbsolutelyPath(rootPath, tmpFileFolder);
		outputPdfFile = makeAbsolutelyPath(outputFolder, formatOutputFilename(outputPdfFile));
		if(!fileExist(kaiuFontFilePath)) {
			return false;
		}
		// if the destination pdf file is opened by other application,
		// our app can not generate new pdf file
		if(fileIsOpened(outputPdfFile)) { 
			return false;
		}
		// delete old pdf file
		deleteOldOutputFile(outputPdfFile);
	
		// create tmp and official output folder
		if(!createFolder(tmpFileFolder)) {
			return false;
		}
		// clean tmp folder
		if(!cleanFolder(tmpFileFolder)) {
			return false;
		}
		return true;
	}
	
	// set init paramter for all the process service
	@Override
	public void initProcessParameters() {
		super.initProcessParameters();
		processOuterFile.setTmpFileFolder(tmpFileFolder);
		processContents.init(tmpFileFolder, YearMonth.of(year, month), kaiuFontFilePath);		
		processInterleaf.init(tmpFileFolder, kaiuFontFilePath);
		processSignificant.setTmpFileFolder(tmpFileFolder);
		processStandard.setTmpFileFolder(tmpFileFolder);
		processPdfs.init(year, outputPdfFile);
	}
	
	@Override
	protected boolean run() {
		
		// 1. move cover page, reference_note page into tmp folder
		// create respectively PageInfo
		String outerFile = "";
		try {
			outerFile = outerFileMap.get(PdfPageType.COVER);
			processOuterFile.splitMultiplePdfsAndPutIntoPdfContainer(PdfPageType.COVER, outerFile);
			outerFile = outerFileMap.get(PdfPageType.REFERENCE_NOTE);
			processOuterFile.splitMultiplePdfsAndPutIntoPdfContainer(PdfPageType.REFERENCE_NOTE, outerFile);
		} catch (IOException e) {
			logger.error("Open " + outerFile + " has IOException." , e);
			return false;
		}
		
		// 2. draw contents, every station makes one page contents
		try {
			for(Station s : stns) {
				processContents.drawSingleContentsPage(s);
			}
		} catch (IOException e) {
			logger.error("Failed to draw Contents. Can not read font file for itext pdf.", e);
			return false;
		} catch (DocumentException e) {
			logger.error("Failed to draw Contents. The itext pdf's font is invalid.", e);
			return false;
		}
			
		for(Station s :stns) {
			// 3. draw interleaf
			try {
				processInterleaf.drawInterleafPage(s);
			} catch (IOException | DocumentException e) {
				logger.error("Failed to draw interleaf.", e);
				return false;			
			}
			// 4. create table image
			for(YearMonth m : Utility.getMonthsUntilNow(year, month)) {
				processStandard.createMonthlyPdfs(s, m, "00");
				processSignificant.createMonthlyPdfs(s, m, "00");
				processStandard.createMonthlyPdfs(s, m, "12");
				processSignificant.createMonthlyPdfs(s, m, "12");
			}
		}
	 
		
		// 5. move copy right page into tmp folder, and create PageInfo
		try {
			processOuterFile.splitMultiplePdfsAndPutIntoPdfContainer(PdfPageType.COPYRIGHT, outerFileMap.get(PdfPageType.COPYRIGHT));
		} catch (IOException e) {
			logger.error("Open " + outerFileMap.get(PdfPageType.COPYRIGHT) + " has IOException." , e);
			return false;
		}
		
		
		// 6. print page number and merge into a whole pdf file
		try {
			processPdfs.run();
		} catch (IOException | DocumentException e) {
			logger.error("Failed add page number or merge pdfs.", e);
			return false;			
		}
		
		logger.info("Upperair Pdf report generated!");
		return true;
	}
	
	public void setOuterFileMap(Map<PdfPageType, String> fileMap) {
		this.outerFileMap = fileMap;
	}
}
