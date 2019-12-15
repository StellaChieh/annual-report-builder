package cwb.cmt.summary.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.createTableImage.CreateTableImageForContents;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.utils.ArrayProcess;
import cwb.cmt.summary.utils.Numbers;
import cwb.cmt.summary.utils.PageEncode;
import cwb.cmt.summary.utils.RomanNumber;

/*
 * This class is to calculate contents page number.
 */
@Service
public class ProcessContents extends Process {
	
	@Autowired
	private CreateTableImageForContents createTableImageForContents;
	
	private static final int OVERALL_WINDROSE_PER_PAGE = Numbers.WINDROSE_PER_PAGE_PART_ONE.getNumber();  // 第一部分風花圖，一頁幾個測站
	private static final int WINDROSE_PAGE_PER_STATION = Numbers.WINDROSE_PAGE_PER_STATION_PART_TWO.getNumber();  // 第二部分風花圖，一個測站佔幾頁
	private static final int STATIONS_PER_PAGE = Numbers.STATIONS_PER_PAGE_PART_ONE.getNumber();                  // 第一部分，一頁有幾個測站
	private static final int NUM_OF_COLUMNS = Numbers.COLUMNS_OF_CONTENTS.getNumber();                   // 總目錄頁，一頁有幾欄
	private static final int NUM_OF_ROWS = Numbers.ROWS_OF_CONTENTS.getNumber();                         // 總目錄頁，一頁有幾列
	private static final String FILENAME_PREFIX = String.valueOf(PageEncode.CONTENTS.getEncode());
	private static final String FILENAME_POSTFIX = PageEncode.CONTENTS.getFilename();
	
	private static Logger log = Logger.getLogger(ProcessContents.class);
	
	private String[] getFirstRowAry(){
		String[] firstRowAry = new String[stnList.size()+1];  // 1:氣象要素總表
		for(int i=0; i<firstRowAry.length; i++){
			if(i==0){
				firstRowAry[0] = "氣象\n" + "要素\n" + "總表";
			} else {
				firstRowAry[i] = RomanNumber.toRoman(i)+"\n" 		   // V
								+stnList.get(i-1).getStnCName()+"\n"   // 彭佳嶼
								+stnList.get(i-1).getStnEName();       // Pengjiyu
			}
		}
		return firstRowAry;
	}
	
	
	private int getPagePerCe(){
		return (int) Math.ceil(stnList.size() / (double)STATIONS_PER_PAGE);
	}
	
	// calculate how many pages all-station-windrose needs
	private int getPageOfWindRoseOfPartOne(){
		// 1: example page
		return (int) (1 + Math.ceil(stnList.size() / (double)OVERALL_WINDROSE_PER_PAGE)); 
	}
	
	// calculate page interval between climatic element in a station
	private int[] getPageInterval(){
		int[] pageInterval = new int[ceList.size()+1];  // 1 for wind rose
		Queue<ClimaticElement> ceQueue = new LinkedList<ClimaticElement>(ceList);
				
		int tableCount = 0;
		int twoUpRowTableCount = 0;
		int intervalCount = 0;  // record the page interval between different climatic element
		while(!ceQueue.isEmpty()){
			int row = ceQueue.peek().getNumOfLinesPerRow();
			if(row == 3 || row == 2){
				twoUpRowTableCount++; 
			} 
			tableCount++;
			
			if(tableCount ==3 || twoUpRowTableCount ==2){ // a page can contains max 3 2-row-tables or 2 3-row-tables
				pageInterval[intervalCount] = 1;    	  // start a new page, so page interval = 1
				tableCount=0;
				twoUpRowTableCount=0;
			} else {
				pageInterval[intervalCount] = 0;    	  // don't start a new page, so page interval = 0
			}
			
			ceQueue.poll();
			intervalCount++;
		}
		
		pageInterval[pageInterval.length-2] = 1;          // windrose starts a new page
		pageInterval[pageInterval.length-1] = WINDROSE_PAGE_PER_STATION;  // record the last page interval
		
		return pageInterval;
	}
	
	public void run() {
		log.info("Start to draw contents...");
		
		int page = 0;
	
		// prepare the first row (station array)
		String[] firstRowAry = getFirstRowAry();
		
		// get how many pages a climatic element needs in first part of the book
		int pagePerCe = getPagePerCe();
		
		// declare page array
		// ceList.size()+1 to include windRose
		// stnList.size()+1 to include 氣象要素總表
		int[][] pageAry = new int[ceList.size()+1][stnList.size()+1];
		
		// put "氣象要素總表" 頁碼
		for (int y=0; y<pageAry.length; y++){ 
			pageAry[y][0] = 1 + y*pagePerCe;
			if( y == pageAry.length-1){ 
				page = pageAry[y][0];  // record the start page number of all-station-windrose 
			}
		}
		
		// calculate how many pages all-station-windrose needs
		int pageOfWindRoseOfPartOne = getPageOfWindRoseOfPartOne();
	
		// calculate the start page of stnCliEle 
		page += pageOfWindRoseOfPartOne;
		
		// calculate page interval between climatic element in a station
		int[] pageInterval = getPageInterval();
		
		// put page number
		for(int x=1; x<pageAry[0].length; x++){
			for(int y=0; y<pageAry.length; y++){
				if(x==1 && y==0){
					pageAry[y][x] = page;  // the first element
				} else if (y==0){
					pageAry[y][x] = pageAry[pageAry.length-1][x-1] + pageInterval[pageInterval.length-1];  // the last column + pageInterval
				} else {
					pageAry[y][x] = pageAry[y-1][x] + pageInterval[y-1]; // the last element + pageInterval
				}
			}
		}
		
		int xLength = NUM_OF_COLUMNS-2;
		int yLength = NUM_OF_ROWS-1;
		int countX = (int) Math.ceil(firstRowAry.length / (double)xLength);
		int countY = (int) Math.ceil((ceList.size()+1) / (double)yLength);	
		
		// divide the arrays by contents' columns and rows
		// and send to draw the contents
		int pdfPageCount = 1;
		for(int x=0; x<countX; x++){
			for(int y=0; y<countY; y++){
				String[] subStationAry;
				List<ClimaticElement> subCeList;
				int[][] subPage;
				boolean isBtmPage; // creator should put "風花圖" in the bottom page, cause it is not in the ceList
				if( x != countX-1 && y!= countY-1){ 
					subPage = ArrayProcess.slice(pageAry, y*yLength, (y+1)*yLength, x*xLength, (x+1)*xLength);
					subStationAry = Arrays.copyOfRange(firstRowAry, x*xLength, (x+1)*xLength);
					subCeList = new ArrayList<>(ceList.subList(y*yLength, (y+1)*yLength));
					isBtmPage = false;
				} else if (x == countX-1 && y != countY-1){ // reach the end of x
					subPage = ArrayProcess.slice(pageAry, y*yLength, (y+1)*yLength , x*xLength, pageAry[0].length);
					subStationAry = Arrays.copyOfRange(firstRowAry, x*xLength, firstRowAry.length);
					subCeList = new ArrayList<>(ceList.subList(y*yLength, (y+1)*yLength));
					isBtmPage = false;
				} else if (x != countX-1 && y == countY-1){ // reach the end of y
					subPage = ArrayProcess.slice(pageAry, y*yLength, pageAry.length, x*xLength, (x+1)*xLength+1);
					subStationAry = Arrays.copyOfRange(firstRowAry, x*xLength, (x+1)*xLength);
					subCeList = new ArrayList<>(ceList.subList(y*yLength, ceList.size()));
					isBtmPage = true;
				} else { // reach the end of x and y
					subPage = ArrayProcess.slice(pageAry, y*yLength, pageAry.length, x*xLength, pageAry[0].length);
					subStationAry = Arrays.copyOfRange(firstRowAry, x*xLength, firstRowAry.length);
					subCeList = new ArrayList<>(ceList.subList(y*yLength, ceList.size()));
					isBtmPage = true;
				}
				
				log.info("Draw contents page " + pdfPageCount);
				createTableImageForContents.setPath(outputTmpPdfPath);
				createTableImageForContents.setFilename(FILENAME_PREFIX + "_" + pdfPageCount + "_" + FILENAME_POSTFIX);
				// 0:fake current page  || y*yLength+1:ceCountStart 
				createTableImageForContents.createTableImage(0, y*yLength+1, subStationAry, subCeList, subPage, isBtmPage);
				
				pdfPageCount++;
			}
		}
		
		log.info("Draw contents completed...");
	}	
}
