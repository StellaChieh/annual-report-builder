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
import cwb.cmt.upperair.dataProcess.IDataProcessSignificant;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.PageInfo;
import cwb.cmt.upperair.utils.PdfPageType;
import cwb.cmt.upperair.utils.SignificantBookTable;

@Service
public class ProcessSignificantPdf {

	@Autowired
	private IDataProcessSignificant dataProcess;
	
	@Autowired
	private PageInfoManager pageInfoContainer;
	
	@Autowired
	@Qualifier("createTableImageForSignificant")
	private CreateTableImage createTableImage;
	
	private String tmpFileFolder;
	
	private static final Logger logger = LogManager.getLogger();
		
	public void createMonthlyPdfs(Station station, YearMonth yearMonth, String hour) {

		Map<SignificantBookTable, Map<Integer, List<String>>> sourceDatas = dataProcess.getPdfMonthlyData(station, yearMonth, hour);
		
		// page 1
		PageInfo pageInfo1 = pageInfoContainer.createNewPageInfo(PdfPageType.SIGNIFICANT);
		createSinglePagePdf(station, yearMonth, hour, pageInfo1, sourceDatas, InnerPageOrderInOneSection.SIGNIFICANT_PAGE1);
		
		// page 2
		PageInfo pageInfo2 = pageInfoContainer.createNewPageInfo(PdfPageType.SIGNIFICANT);
		createSinglePagePdf(station, yearMonth, hour, pageInfo2, sourceDatas, InnerPageOrderInOneSection.SIGNIFICANT_PAGE2);
		
		// page 3
		PageInfo pageInfo3 = pageInfoContainer.createNewPageInfo(PdfPageType.SIGNIFICANT);
		createSinglePagePdf(station, yearMonth, hour, pageInfo3, sourceDatas, InnerPageOrderInOneSection.SIGNIFICANT_PAGE3);
		
		String logMsg = String.format("Draw %s %d-%d %s:00 significant level pages."
				, station.getStnEName(), yearMonth.getYear(), yearMonth.getMonthValue(), hour);
		logger.info(logMsg);
	}
	
	private void createSinglePagePdf(Station station, YearMonth yearMonth, String hour
									, PageInfo pageInfo, Map<SignificantBookTable, Map<Integer, List<String>>> datas
									, InnerPageOrderInOneSection innerPageOrder) {
		String fileName = getFilename(station, yearMonth, hour, innerPageOrder);
		pageInfo.setFilename(fileName);
		pageInfo.setFilePath(Paths.get(tmpFileFolder, fileName).toString());
		createTableImage.createTableImage(station, yearMonth, hour, datas, innerPageOrder, pageInfo);
	}
	
	private String getFilename(Station station, YearMonth yearMonth, String hour, InnerPageOrderInOneSection innerPageOrder) {
		return String.format("%s測站%d年%d月%s時特性層觀測紀錄表_%d.%s", station.getStnCName(), yearMonth.getYear(), yearMonth.getMonthValue(), hour, innerPageOrder.value(), "pdf");
	}
	
	public void setTmpFileFolder(String tmpFileFolder) {
		this.tmpFileFolder = tmpFileFolder;
	}
}