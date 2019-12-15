package cwb.cmt.summary.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.createTableImage.CreateTableImageForStnCliEle;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.model.StnCliEleTable;
import cwb.cmt.summary.service.MakeStnCliEleTable;
import cwb.cmt.summary.utils.PageEncode;
import cwb.cmt.summary.utils.RomanNumber;
import windRose.process.WindRoseMain;

@Service
public class ProcessStnCliEle extends Process {
	
	@Autowired
	private MakeStnCliEleTable makeTable;
	
	@Autowired
	private CreateTableImageForStnCliEle tableCreator;
	
	private int mPageStart;
	private int currentPage = mPageStart + 1;
	private static Logger log = Logger.getLogger(ProcessStnCliEle.class);
	
	public void run(){
		
		List<List<ClimaticElement>> cliEleOfPages = distributeCliEle(ceList);
		
		for(int i=0; i<stnList.size(); i++) {
			log.info("Start to draw stnCliEle " + stnList.get(i).getStnEName() + " table.");
			int ceNumOfThisStation = 1;
			for(List<ClimaticElement> ces : cliEleOfPages) {
				drawNewPage(i+1, stnList.get(i), ceNumOfThisStation, ces);
				ceNumOfThisStation += ces.size();
				currentPage++;
			}
			drawWindRose(i+1, stnList.get(i), ceNumOfThisStation++);
		}
	}
	
	private void drawWindRose(int stnNum, Station station, int ceNumOfThisStation) {
		// call windrose jar to draw windrose
		// filename: 10_01_1_00001_1  (PageEncode_測站數_表格代表_目前頁數_風花圖頁數)
		String filename = new StringBuilder().append(PageEncode.TABLE_TWO.getEncode())
											.append("_")
											.append(String.format("%02d", stnNum))
											.append("_1_")
											.append(String.format("%05d", currentPage))
											.toString();
		
		try {
			WindRoseMain.summaryStn(station.getStno(), RomanNumber.toRoman(stnNum), String.valueOf(ceNumOfThisStation), getYearRange(), filename);
		} catch (IOException e) {
			log.error(e.getStackTrace());
		}
		currentPage+=2; // windRose has 2 pages
	}
	
	private void drawNewPage(int stnCount, Station station, int ceStartNumThisPage, List<ClimaticElement> ceList){
		List<StnCliEleTable> tableList = new ArrayList<>();
		for (ClimaticElement ce : ceList) {
			makeTable.setParams(stnCount, station, ceStartNumThisPage++, ce);
			tableList.add(makeTable.makeTableData());   
		}
		
		// filename: 10_01_1_00001_466950_PUNJIYOU  (PageEncode_測站數_表格代表_頁數_測站Id_測站英文名)
		String filename = new StringBuilder().append(PageEncode.TABLE_TWO.getEncode())
											.append("_")
											.append(String.format("%02d", stnCount))
											.append("_1_")
											.append(String.format("%05d", currentPage))
											.append("_")
											.append(station.getStno())
											.append("_")
											.append(station.getStnEName())
											.toString();
		
		tableCreator.setPath(outputTmpPdfPath);
		tableCreator.setFilename(filename);
		tableCreator.createTableImage(currentPage, tableList);
	}
	
	private List<List<ClimaticElement>> distributeCliEle(List<ClimaticElement> list){
		List<ClimaticElement> mayModifiedList = new ArrayList<>(list);
		List<List<ClimaticElement>> result = new ArrayList<>();
		List<ClimaticElement> perPageList = new ArrayList<>();
		while(mayModifiedList.size() > 0 ) {
			// one page may contains max 3 1/2-row tables, or 2 3-row tables. 
			if(perPageList.size()<3 && countRow(perPageList)+mayModifiedList.get(0).getNumOfLinesPerRow()<=6 ) {
				perPageList.add(mayModifiedList.get(0));
				mayModifiedList.remove(0);
			} else {
				result.add(perPageList);
				perPageList = new ArrayList<>();
			}
		}
		if(!perPageList.isEmpty()) {
			result.add(perPageList);
		}
		return result;
	}
	
	private int countRow(List<ClimaticElement> list) {
		return list.stream().mapToInt(ClimaticElement::getNumOfLinesPerRow).sum();
	}
	
	private String getYearRange(){
		return String.valueOf(thisStartYear) + "-" + String.valueOf(thisStartYear+9);
	}

}
