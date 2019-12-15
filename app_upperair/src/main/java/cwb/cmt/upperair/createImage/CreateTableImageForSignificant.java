package cwb.cmt.upperair.createImage;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.SignificantBookTable;

@Service  
public class CreateTableImageForSignificant extends CreateTableImage {

	// layout parameters
    private static final int CELL_FONT_SIZE = 12;
    private static final int CELL_HORIZANTOL_MARGIN = 13;
    private static final int CELL_VERTICAL_MARGIN = 3;
    protected static final int TABLE_MARGIN_LEFT  = 120;
    private static final int INTERVAL_BETWEEN_TABLE = 30;
    
        
    
   
    
	@SuppressWarnings("unchecked")
	protected BaseChart createChart(Station station,
									YearMonth yearMonth,
									String hour,
								    Object object,
								    InnerPageOrderInOneSection page) {		
		
		Map<SignificantBookTable, Map<Integer, List<String>>> records = (Map<SignificantBookTable, Map<Integer, List<String>>>)object;
		
		int [] tableBeginDateAry = null;
		Map<Integer, List<String>> table1Data = null;
		Map<Integer, List<String>> table2Data = null;
		Map<Integer, List<String>> table3Data = null;
		switch (page){
			case SIGNIFICANT_PAGE1:
				tableBeginDateAry = new int[] {SignificantBookTable.TABLE1.getBeginDate(), SignificantBookTable.TABLE2.getBeginDate(), SignificantBookTable.TABLE3.getBeginDate()};
				table1Data = records.get(SignificantBookTable.TABLE1);
				table2Data = records.get(SignificantBookTable.TABLE2);
				table3Data = records.get(SignificantBookTable.TABLE3);
				break;
			case SIGNIFICANT_PAGE2:
				tableBeginDateAry = new int[] {SignificantBookTable.TABLE4.getBeginDate(), SignificantBookTable.TABLE5.getBeginDate(), SignificantBookTable.TABLE6.getBeginDate()};
				table1Data = records.get(SignificantBookTable.TABLE4);
				table2Data = records.get(SignificantBookTable.TABLE5);
				table3Data = records.get(SignificantBookTable.TABLE6);
				break;
			case SIGNIFICANT_PAGE3:
				tableBeginDateAry = new int[] {SignificantBookTable.TABLE7.getBeginDate(), SignificantBookTable.TABLE8.getBeginDate()};
				table1Data = records.get(SignificantBookTable.TABLE7);
				table2Data = records.get(SignificantBookTable.TABLE8);
				break;
			default:
				break;
		}
		
		// create a XYChart object
		XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
	
		// set title
		setupTitle(chart, TITLE_FONT_SIZE);
		
		// set title text
		setupTitleText(chart, TABLE_MARGIN_LEFT, TITLE_MARGIN_TOP, station, yearMonth, hour);
		
		// create 1st table
		final int marginTop_table1 = TABLE_MARGIN_TOP; 
		final CDMLTable table1 = createSingleTable(chart, page, TABLE_MARGIN_LEFT, marginTop_table1, 
												   tableBeginDateAry[0], tableBeginDateAry[0]+3, table1Data);
		
		// in page3, only draw table2 if it's days-of-month is grater to 28  
		if(page == InnerPageOrderInOneSection.SIGNIFICANT_PAGE3 && yearMonth.lengthOfMonth() < 29){
			return chart;
		}
		
		// create 2nd table
		final int marginTop_table2 = marginTop_table1 + table1.getHeight() + INTERVAL_BETWEEN_TABLE;
		int table2EndDate = page == InnerPageOrderInOneSection.SIGNIFICANT_PAGE3 ? yearMonth.lengthOfMonth() : tableBeginDateAry[1]+3; 
		final CDMLTable table2 = createSingleTable(chart, page, TABLE_MARGIN_LEFT, marginTop_table2, 
													tableBeginDateAry[1], table2EndDate, table2Data);
		
		// in page3, we don't draw table3
		if(page == InnerPageOrderInOneSection.SIGNIFICANT_PAGE3){
			return chart;
		}
		
		//create 3rd table
		final int marginTop_table3 = marginTop_table2 + table2.getHeight() + INTERVAL_BETWEEN_TABLE;
		final CDMLTable table3 = createSingleTable(chart, page, TABLE_MARGIN_LEFT, marginTop_table3, 
				   								tableBeginDateAry[2], tableBeginDateAry[2]+3, table3Data);

		return chart;
	}
	
    private void setupTitle(BaseChart chart, int fontSize){
		chart.addTitle(
				"\n" +
				"特 性 層 觀 測 紀 錄 表" + "\n" +
				"DATA AT SIGNIFICANT LEVELS",
				"kaiu.ttf", fontSize
		);
    }
    
    private void setupTitleText(BaseChart c, int posX, int posY
			, Station station, YearMonth yearMonth, String hour){
		// unit
		TextBox pageTitleUnit = c.addText(
								posX+1400,
								posY-50,
								"單   位" + " " + "unit" + "\n" +  
								"P:" + "hPa" + ", " +
								"H:" + "gpm" + ", " + 
								"T & Td:" + "\u00B0C" + ", " + 
								"U:" + "%" + ", " + 
								"dd:" + "deg" + ", " +
								"ff:" + "m/sec"
		);
		pageTitleUnit.setFontStyle("kaiu.ttf");
		pageTitleUnit.setFontSize(14);
		
		// station
		TextBox pageTitleStation = c.addText(
								posX,
								posY+10,
								"測   站" + "  " + station.getStnCName() + "\n" + 
								"STATION" + "  " + station.getStnEName());
								pageTitleStation.setFontStyle("kaiu.ttf");
								pageTitleStation.setFontSize(14);	
		
		// year
		TextBox pageTitleYear = c.addText(
								posX+300,
								posY+10,
								"年" + "\n" +  
								"YEAR:" + " " + yearMonth.getYear()
		);
		pageTitleYear.setFontStyle("kaiu.ttf");
		pageTitleYear.setFontSize(14);
		
		// month
		TextBox pageTitleMonth = c.addText(
								posX+430,
								posY+10,
								"月" + "\n" +  
								"MONTH:" + " " + yearMonth.getMonthValue()
		);
		pageTitleMonth.setFontStyle("kaiu.ttf");
		pageTitleMonth.setFontSize(14);
		
		// observation time
		TextBox pageTitleObstime = c.addText(
								posX+680,
								posY+10,
								"觀測時間" + "\n" +  
								"TIME: " + hour + " 00Z(UT)"
		);
		pageTitleObstime.setFontStyle("kaiu.ttf");
		pageTitleObstime.setFontSize(14);
		
		// latitude
		TextBox pageTitleLatitude = c.addText(
								posX+930,
								posY+10,
								"緯度" + "\n" +  
								"LAT: "  + station.getLatitude()
		);
		pageTitleLatitude.setFontStyle("kaiu.ttf");
		pageTitleLatitude.setFontSize(14);
		
		// longitude
		TextBox pageTitleLongitude = c.addText(
								posX+1200,
								posY+10,
								"經度" + "\n" +  
								"LON: " + station.getLongitude()
		);
		pageTitleLongitude.setFontStyle("kaiu.ttf");
		pageTitleLongitude.setFontSize(14);
		
		// barometer height above sea
		TextBox pageTitleBarometerHeightAboveSea = c.addText(
			posX+1490,
			posY+10,
			"氣壓計海拔高度" + "\n" +  
			"BAROMETER ABOVE MSL " + station.getPrintedHBarometer() + " " + "gpm"
		);
		pageTitleBarometerHeightAboveSea.setFontStyle("kaiu.ttf");
		pageTitleBarometerHeightAboveSea.setFontSize(14);
    }
	
	
	/**
	 * @param chart 
	 * @param page page order of inner section
	 * @param posX table x-coordinate
	 * @param posY table y-coordinate
	 * @param beginDate begin date of the records
	 * @param endDate end date of the records
	 * @param sqMap records' sqMap
	 * @return CDMLTable
	 */
	private CDMLTable createSingleTable(XYChart chart, 
										InnerPageOrderInOneSection page,
									    int posX, 
									    int posY,
									    int beginDate,
									    int endDate,
									    Map<Integer, List<String>> sqMap) {
		// create a CDMLTable
		CDMLTable table = chart.addTable(
			posX, posY, 0,
			2 + 6*4,      	// column count
			1+1+36          // row count: day row + item row + data row 
		);
		
		// set display attributes
		TextBox cellStyle = table.getStyle();
		// (left, right, top, bottom)
		cellStyle.setMargin(CELL_HORIZANTOL_MARGIN, CELL_HORIZANTOL_MARGIN
							, CELL_VERTICAL_MARGIN, CELL_VERTICAL_MARGIN); 
		cellStyle.setFontStyle("kaiu.ttf");
		cellStyle.setFontSize(CELL_FONT_SIZE);
		
		// set light gray background row
		table.getRowStyle(11).setBackground(0xf0f0f0, 1);
		table.getRowStyle(21).setBackground(0xf0f0f0, 1);
		table.getRowStyle(31).setBackground(0xf0f0f0, 1);
		
		// date title 
		if(page == InnerPageOrderInOneSection.SIGNIFICANT_PAGE3 && beginDate > 25) {
			if(beginDate == 29 && endDate == 29) {
				table.setCell(1, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
				table.setCell(7, 0, 6, 1, " ");
				table.setCell(13, 0, 6, 1, " ");
			}
			if(beginDate == 29 && endDate == 30) {
				table.setCell(1, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
				table.setCell(7, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
				table.setCell(13, 0, 6, 1, " ");
			}
			if(beginDate == 29 && endDate == 31) {
				table.setCell(1, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
				table.setCell(7, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
				table.setCell(13, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
			}
			table.setCell(19, 0, 6, 1, " "); // merge empty date column
		} else {
			table.setCell(1, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
			table.setCell(7, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
			table.setCell(13, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
			table.setCell(19, 0, 6, 1, "DAY:" + String.valueOf(beginDate++));
		}
		
		// sq title cell 
		table.getCell(0, 1).setMargin(4, 4, 3, 3);
		table.setCell(0, 1, 1, 1, "SQ");
		table.getCell(table.getColCount()-1, 1).setMargin(4, 4, 3, 3);
		table.setCell(table.getColCount()-1, 1, 1, 1, "SQ");
		
		// field title
		for(int i=1; i<=table.getColCount()-2; i++) {
			switch (i%6) {
				case 1:
					table.setCell(i, 1, 1, 1, String.format("%1s", "P"));
					break;
				case 2:
					table.setCell(i, 1, 1, 1, String.format("%1s", "H"));
					break;
				case 3:
					table.setCell(i, 1, 1, 1, String.format("%1s", "T"));
					break;
				case 4:
					table.setCell(i, 1, 1, 1, String.format("%1s", "U"));
					break;
				case 5:
					table.setCell(i, 1, 1, 1, String.format("%1s", "dd"));
					break;
				case 0:
					table.setCell(i, 1, 1, 1, String.format("%1s", "ff"));
					break;	
			} 	
		}
		
		// fill content cell with value
		for (int row = 0; row < table.getRowCount()-2; row++) {
			// first sq column
			table.setCell(0, row+2, 1, 1, String.valueOf(row+1));
			List<String> listOfSameSq = sqMap.get(row+1);
			// field column
			for (int col = 0; col < listOfSameSq.size(); col++) {
				table.setCell(col+1, row+2, 1, 1, String.format("%1$6s", listOfSameSq.get(col)));
			}
			for (int i=listOfSameSq.size()+1; i<table.getColCount()-1; i++) {
				table.setCell(i, row+2, 1, 1, String.format("%1$6s", " "));
			}
			// last sq column
			table.setCell(table.getColCount()-1, row+2, 1, 1, String.valueOf(row+1));
		}
		return table;
	}

}
