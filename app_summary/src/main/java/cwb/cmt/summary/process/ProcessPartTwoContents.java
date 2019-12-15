package cwb.cmt.summary.process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cwb.cmt.summary.createTableImage.CreateTableImageForPartTwoContents;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.utils.Numbers;
import cwb.cmt.summary.utils.PageEncode;

@Service
public class ProcessPartTwoContents extends Process{

	private static final String FILENAME_PREFIX = String.valueOf(PageEncode.CONTENTS_TWO.getEncode());
	private static final String FILENAME_POSTFIX = PageEncode.CONTENTS_TWO.getFilename();
	
	@Autowired
	private CreateTableImageForPartTwoContents imageCreator;
	
	private static final int ROWS_PER_CONTENTS_PAGE = Numbers.ROWS_OF_PART_TWO_CONTENTS.getNumber();
	private static final int WINDROSE_PAGE_PER_STATION = Numbers.WINDROSE_PAGE_PER_STATION_PART_TWO.getNumber();
	private static final int STATIONS_PER_PAGE_PART_ONE = Numbers.STATIONS_PER_PAGE_PART_ONE.getNumber();  // part one 一頁天氣要素有幾個測站
	private static final int WINDROSE_PER_PAGE_PART_ONE = Numbers.WINDROSE_PER_PAGE_PART_ONE.getNumber();  // part one 一頁風花圖有幾個測站
	private static Logger log = Logger.getLogger(ProcessPartTwoContents.class);
	
	public void run(){
		
		log.info("start to draw part two contents...");
		
		// 0. calculate how many pages part one needs
		int pagePerCe = (int) Math.ceil(stnList.size()/(double)STATIONS_PER_PAGE_PART_ONE);
		int pageWindRose = 1 + (int) Math.ceil(stnList.size()/(double)WINDROSE_PER_PAGE_PART_ONE); // 1:example
		int partOnePages = pagePerCe*ceList.size() + pageWindRose;
		
		// 1. calculate how many pages a station needs
		Queue<ClimaticElement> ceQueue = new LinkedList<>(ceList);
		int tableCount = 0;
		int twoUpRowTableCount = 0;
		int pageInterval=0;
		List<ClimaticElement> ceList = new ArrayList<>();
		while(!ceQueue.isEmpty()){
			ClimaticElement ce = ceQueue.peek();
			int row = ce.getNumOfLinesPerRow();
			if(row == 3 || row == 2){
				twoUpRowTableCount++; 
			} 
			tableCount++;
			ceList.add(ce);
			if(tableCount == 3 || twoUpRowTableCount == 2){ // a page can contains max 3 2-row-tables or 2 3-row-tables
				pageInterval++;  // start a new page
				tableCount=0;
				twoUpRowTableCount=0;
				ceList = new ArrayList<>();
			} 
			ceQueue.poll();
		}
		if(!ceList.isEmpty()){
			pageInterval++;
		}
		pageInterval += WINDROSE_PAGE_PER_STATION; // add wind rose page
		
	
		// 2. divide stations into pages
		List<List<Station>> stnGroups = Lists.partition(stnList, ROWS_PER_CONTENTS_PAGE);
		

		// 3. draw part two contents
		int stnCount = 1;
		imageCreator.setPath(outputTmpPdfPath);
		for(int i=0; i<stnGroups.size(); i++){
			log.info("Draw part two contents page " + (i+1));
			String filename = FILENAME_PREFIX + "_" + String.format("%02d", i) + "_" + FILENAME_POSTFIX;   
			imageCreator.setFilename(filename);
			if(i == stnGroups.size()-1){
				// 0:face pageNumber, partOnePages, List<Station>, stnStart, stnPageInterval
				imageCreator.createTableImage(0, partOnePages, stnGroups.get(i), stnCount, pageInterval); 
			} else {
				imageCreator.createTableImage(0, partOnePages, stnGroups.get(i), stnCount, pageInterval);
			}
			stnCount = (i+1) * ROWS_PER_CONTENTS_PAGE + 1;
		} 
		
		log.info("Draw part two contents completed...");
	}

}
