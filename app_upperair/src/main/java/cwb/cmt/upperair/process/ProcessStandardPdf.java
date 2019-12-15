package cwb.cmt.upperair.process;

import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cwb.cmt.upperair.createImage.CreateTableImage;
import cwb.cmt.upperair.createImage.CreateTableImage.InnerPageOrderInOneSection;
import cwb.cmt.upperair.dataProcess.IDataProcessStandard;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.PageInfo;
import cwb.cmt.upperair.utils.PdfPageType;
import cwb.cmt.upperair.utils.StandardBookTable;

@Service
public class ProcessStandardPdf {

	@Autowired
	private IDataProcessStandard dataProcess;
	
	@Autowired
	private PageInfoManager pageInfoContainer;

	@Autowired
	@Qualifier("createTableImageForStandard")
	private CreateTableImage createTableImage;
	
	private String tmpFileFolder;
	
	private static final Logger logger = LogManager.getLogger();
	
	public void createMonthlyPdfs(Station station, YearMonth yearMonth, String hour) {
		
		Map<StandardBookTable, Map<Integer, List<String>>> sourceDatas = dataProcess.getPdfMonthlyData(station, yearMonth, hour);
		
		// page 1
		PageInfo pageInfo1 = pageInfoContainer.createNewPageInfo(PdfPageType.STANDARD);
		String filename1 = getFilename(station, yearMonth, hour, InnerPageOrderInOneSection.STANDARD_PAGE1);
		pageInfo1.setFilename(filename1);
		pageInfo1.setFilePath(Paths.get(tmpFileFolder, filename1).toString());
		createTableImage.createTableImage(station, yearMonth, hour, sourceDatas, InnerPageOrderInOneSection.STANDARD_PAGE1, pageInfo1);
	
		// page 2
		PageInfo pageInfo2 = pageInfoContainer.createNewPageInfo(PdfPageType.STANDARD);
		String filename2 = getFilename(station, yearMonth, hour, InnerPageOrderInOneSection.STANDARD_PAGE2);
		pageInfo2.setFilename(filename2);
		pageInfo2.setFilePath(Paths.get(tmpFileFolder, filename2).toString());
		createTableImage.createTableImage(station, yearMonth, hour, sourceDatas, InnerPageOrderInOneSection.STANDARD_PAGE2, pageInfo2);
		
		String logMsg = String.format("Create %s %d-%d %s:00 standard level pages."
								, station.getStnEName(), yearMonth.getYear(), yearMonth.getMonthValue(), hour);
		logger.info(logMsg);
	}
	
	private String getFilename(Station station, YearMonth yearMonth, String hour, InnerPageOrderInOneSection innerPageOrder) {
		return String.format("%s測站%d年%d月%s時標準層觀測紀錄表_%d.%s", station.getStnCName(), yearMonth.getYear(), yearMonth.getMonthValue(), hour, innerPageOrder.value(), "pdf");
	}
	
	public void setTmpFileFolder(String tmpFileFolder) {
		this.tmpFileFolder = tmpFileFolder;
	}
	
}