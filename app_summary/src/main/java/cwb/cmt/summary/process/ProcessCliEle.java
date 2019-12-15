package cwb.cmt.summary.process;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cwb.cmt.summary.createTableImage.CreateTableImageForCliEle;
import cwb.cmt.summary.model.CliEleTable;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.service.MakeCliEleTable;
import cwb.cmt.summary.utils.Numbers;
import cwb.cmt.summary.utils.PageEncode;
import windRose.process.WindRoseMain;

@Service
public class ProcessCliEle extends Process {
	
	@Autowired
	private CreateTableImageForCliEle tableCreator;
	
	@Autowired
	private MakeCliEleTable putDataService;
		
	private int startPage = 1;
	private int currentPage = startPage;
	
	private static Logger log = Logger.getLogger(ProcessCliEle.class);
	private static final int STN_NUMBER_PER_PAGE = Numbers.STATIONS_PER_PAGE_PART_ONE.getNumber();
	private static final String FILENAME_PREFIX = String.valueOf(PageEncode.TABLE_ONE.getEncode());
	private static final String FILENAME_POSTFIX = PageEncode.TABLE_ONE.getFilename();
				
	public void run() {

		log.info("start to create CliEle table...");

		// if station count > STN_COUNT_PER_PAGE, divide it into more pages
		List<List<Station>> stnGroups = Lists.partition(stnList, STN_NUMBER_PER_PAGE);
		
		// set tmp output pdf file path
		tableCreator.setPath(outputTmpPdfPath);
		
		// record the number of climatic element
		int cliEleCount = 1; 
	
		for (ClimaticElement ce : ceList) {
			
			log.info("start to create CliEle " + ce.getId() + " table.");
			
			for(List<Station> stnGroup : stnGroups){
				putDataService.setParams(cliEleCount, ce, stnGroup, thisStartYear);
				CliEleTable tableData = putDataService.getTableData();
				
				// filename : 9_01_00015_partOneTable-SeaPres (PageEncode_氣象要素數_頁數_partOneTable-氣象要素Id)
				String filename = new StringBuilder().append(FILENAME_PREFIX)
													.append("_")
													.append(String.format("%02d", cliEleCount))
													.append("_")
													.append(String.format("%05d", currentPage))
													.append("_")
													.append(FILENAME_POSTFIX)
													.append("-")
													.append(ce.getId()).toString();
				tableCreator.setFilename(filename);
				tableCreator.createTableImage(currentPage, tableData);
				currentPage++;
			} 
			cliEleCount++;
		}
		
		// call windrose jar to draw windrose
		// filename : 9_01_00015_1 (PageEncode_氣象要素數_頁數_風花圖的頁數)
		String windRoseFilename = new StringBuilder()
								.append(FILENAME_PREFIX)
								.append("_")
								.append(String.format("%02d", cliEleCount))
								.append("_")
								.append(String.format("%05d", currentPage)).toString();
		try {
			WindRoseMain.summary(getStnAry(), String.valueOf(cliEleCount), getYearRange(), windRoseFilename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String[] getStnAry(){
		List<String> stnIds =  stnList.stream().map(f -> f.getStno()).collect(Collectors.toList());
		return stnIds.toArray(new String[stnIds.size()]);
	}
	
	private String getYearRange(){
		return String.valueOf(thisStartYear) + "-" + String.valueOf(thisStartYear+9);
	}
	
}
