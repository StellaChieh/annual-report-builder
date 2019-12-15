package cwb.cmt.summary.createTableImage;

import java.util.List;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.utils.Numbers;

// fix 13 columns and 27 rows 
@Service
public class CreateTableImageForContents extends CreateTableImage{

	final static private int CellHorizontalMargin = 2;
	final static private int CellVerticalMargin = 0;
	final static private double CellFontSize = 7.5;
	final static private double CellChineseFontSize = 8;
	final static private int TitleFontSize = 12;
	final static private int XcontentStartPost = 40;
	final static private int YcontentStartPost = 5;
	
	private static final int NUM_Of_COLUMNS = Numbers.COLUMNS_OF_CONTENTS.getNumber();
	private static final int NUM_OF_ROWS = Numbers.ROWS_OF_CONTENTS.getNumber();
	
	@Override
	protected BaseChart createChart(int pageNumber, Object... args) {
		int ceCountStart = (int)args[0];
		String[] stations = (String[])args[1];
		@SuppressWarnings("unchecked")
		List<ClimaticElement> ces = (List<ClimaticElement>)args[2];
		int[][] pages = (int[][])args[3];
		boolean isBtmPage = (boolean)args[4];
		
		// set license code
		Chart.setLicenseCode(CHARTDIRECTOR_LICENSECODE);
				
		// create chart
		XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
		
		// add table 
		createTable(XcontentStartPost, YcontentStartPost, chart, ceCountStart, stations, ces, pages, isBtmPage);
		
		return chart;
		
	}
	
	private int createTable(int xPost, int yPost, XYChart chart, int ceCountStart
							, String[] stations, List<ClimaticElement> ces, int[][] pages, boolean isBtmPage){
	
		/*
		 *  define relative position of table elements
		 */
		int xTitle = xPost;
		int xTable = xPost;
		
		int yTitle = yPost;
		int yTable = yTitle+20;
		
		/*
		 *  add title
		 */
		chart.addText(xTitle, yTitle  					// x position, y position
				 	  , "總 目 錄  CONTENTS"  				// contents
					  , CHINESE_FONT ,TitleFontSize); // font, font size 
		
		/*
		 *  add table
		 */
		CDMLTable cdmlTable = chart.addTable(xTable, yTable, 0, NUM_Of_COLUMNS, NUM_OF_ROWS); // xPosition, yPosition, alignment to the chart, columns, rows
		
		/*
		 * set table style and alignment of cell
		 * alignment style:
		 * 4 => The leftmost point on the middle horizontal line
		 * 5 => center alignment
		 * 7 => top left
		 * 8 => top center alignment
		 * 9 => top right alignment
		 */
		TextBox cellStyle = cdmlTable.getStyle();
		cellStyle.setMargin2(CellHorizontalMargin, CellHorizontalMargin, CellVerticalMargin, CellVerticalMargin); // left, right, top, bottom
		cellStyle.setFontStyle(CHINESE_FONT);
		cellStyle.setBackground(0xffffff, 0xf0f0f0); // white background color, light grey border color
		cellStyle.setFontSize(CellFontSize);
		// set overall table center alignment 
		cellStyle.setAlignment(5);
		
		// fix the width of the second column
		cdmlTable.getColStyle(1).setWidth(250);
		
		// fix the width of the first row
		for(int i=0; i<NUM_Of_COLUMNS-2; i++){
			cdmlTable.getColStyle(i+2).setWidth(40);
			// set first row top center alignment
			cdmlTable.getCell(i+2, 0).setAlignment(8);
		}
		
		/*
		 *  content
		 */
		// set first row
		cdmlTable.setCell(0, 0, 1, 1, "表\nTable");
		cdmlTable.setCell(1, 0, 1, 1, "   	     	 		                測    站\n"
									+ "  	 		           頁  次          Station\n"
									+ " 項   目        Page\n"
									+ " Element").setAlignment(7);  // top-left alignment
		
		// set first row
		// set "氣象要素總表", "I 彭佳嶼 Pengjiayu"....
		int stationsSize = stations.length;
		for(int i=0; i< NUM_Of_COLUMNS-2 ; i++){
			if(i<stationsSize){
				cdmlTable.setCell(i+2, 0, 1, 1, stations[i]);
			} 
		}
		
		// set second column, ex. "海平面平均氣壓 Mean Pressure at Sea Level"...
		int cesSize = ces.size();
		for(int i=0; i < NUM_OF_ROWS-1; i++){ 
			if(i<cesSize){
				cdmlTable.setCell(0, i+1, 1, 1, String.valueOf(ceCountStart++));
				cdmlTable.setCell(1, i+1, 1, 1, ces.get(i).getChineseName()+"\n"+ces.get(i).getEnglishName());
			} 
		}
		
		if(isBtmPage){
			cdmlTable.setCell(0, cesSize+1, 1, 1, String.valueOf(ceCountStart));
			cdmlTable.setCell(1, cesSize+1, 1, 1, "風花圖"+"\n"+"Wind Rose");
		}
		
		/*
		 *  put page numbers
		 */
		for(int y=0; y<NUM_OF_ROWS-1; y++) {
			for(int x=0; x<NUM_Of_COLUMNS-2; x++){
				if(y < pages.length && x < pages[0].length){
					cdmlTable.setCell(x+2, y+1, 1, 1, String.valueOf(pages[y][x]));
				} 
			}
		}
		
		int yTableEnd = yTable + cdmlTable.getHeight();
		return yTableEnd;
	}	
	
	public void createTableImage(int pageNumber, Object... args) {
		super.createTableImage(pageNumber, args);
	}
	
}
