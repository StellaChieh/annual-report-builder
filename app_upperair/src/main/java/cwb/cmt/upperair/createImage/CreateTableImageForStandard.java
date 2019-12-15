package cwb.cmt.upperair.createImage;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.StandardColumn;
import cwb.cmt.upperair.utils.StandardGroup;
import cwb.cmt.upperair.utils.StandardBookTable;

@Service
public class CreateTableImageForStandard extends CreateTableImage{

    private static final int TABLE_MARGIN_LEFT = 95;
    private static final int CELL_FONT_SIZE = 12;
	
    
	@SuppressWarnings("incomplete-switch")
	@Override
	protected BaseChart createChart(Station station, YearMonth yearMonth, String time, Object object,
			InnerPageOrderInOneSection pageOrder) {
		
    	BaseChart chart = null;
    	@SuppressWarnings("unchecked")
		Map<StandardBookTable, Map<Integer, List<String>>> tablyMap = (Map<StandardBookTable, Map<Integer, List<String>>>)object; 
		
    	// check page
    	switch(pageOrder) {
    		case STANDARD_PAGE1:
	    		chart = createPage1Chart(station, yearMonth, time, tablyMap); 								  
	    		break;
	    	case STANDARD_PAGE2:
	    		chart = createPage2Chart(station, yearMonth, time, tablyMap);   
	    		break;
    	}
    	
    	return chart;
	}
	
	protected BaseChart createPage1Chart(Station station, YearMonth yearMonth, String hour
							, Map<StandardBookTable, Map<Integer, List<String>>> tablyMap) {

		XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
		
		// title
		chart.addTitle(
				"\n"+
				"標 準 氣 壓 面 觀 測 紀 錄 表" + "\n" +
				"DATA AT STANDARD LEVELS",
				"kaiu.ttf", 21
		);
		
		//  set title text
		setupTitleText(chart, TABLE_MARGIN_LEFT, TITLE_MARGIN_TOP, station, yearMonth, hour);
		
		// table 1
		final int marginTop_table1 = TABLE_MARGIN_TOP;
		final CDMLTable table1 = createSingleTable(chart, StandardBookTable.TABLE1, TABLE_MARGIN_LEFT, marginTop_table1
														, tablyMap.get(StandardBookTable.TABLE1));
		
		// table 2
		final int marginTop_table2 = marginTop_table1 + table1.getHeight() + 35;
		final CDMLTable table2 = createSingleTable(chart, StandardBookTable.TABLE2, TABLE_MARGIN_LEFT, marginTop_table2
														, tablyMap.get(StandardBookTable.TABLE2));
																  
		
		// table 3
		final int marginTop_table3 = marginTop_table2 + table2.getHeight() + 35;
		final CDMLTable table3 = createSingleTable(chart, StandardBookTable.TABLE3, TABLE_MARGIN_LEFT, marginTop_table3
														, tablyMap.get(StandardBookTable.TABLE3));
		
		return chart;
	}
	
	private void setupTitleText(BaseChart c, int posX, int posY, Station station, YearMonth yearMonth, String hour) {
		// unit
		TextBox pageTitleUnit = c.addText(
				posX+1400,
				posY-50,
				"單  位" + " " + "unit" + "\n" +  
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
			posX+20,
			posY+10,
			"測   站" + "  " + station.getStnCName() + "\n" + 
			"STATION" + "  " + station.getStnEName());
		pageTitleStation.setFontStyle("kaiu.ttf");
		pageTitleStation.setFontSize(14);	
		
		// year
		TextBox pageTitleYear = c.addText(
			posX+400,
			posY+10,
			"年" + "\n" +  
			"YEAR:" + " " + yearMonth.getYear()
		);
		pageTitleYear.setFontStyle("kaiu.ttf");
		pageTitleYear.setFontSize(14);
		
		// month
		TextBox pageTitleMonth = c.addText(
			posX+550,
			posY+10,
			"月" + "\n" +  
			"MONTH:" + " " + yearMonth.getMonthValue()
		);
		pageTitleMonth.setFontStyle("kaiu.ttf");
		pageTitleMonth.setFontSize(14);

		// observation time
		TextBox pageTitleObstime = c.addText(
			posX+700,
			posY+10,
			"觀測時間" + "\n" +  
			"TIME: " + hour + " 00Z(UT)"
		);
		pageTitleObstime.setFontStyle("kaiu.ttf");
		pageTitleObstime.setFontSize(14);
		
		// latitude
		TextBox pageTitleLatitude = c.addText(
			posX+1000,
			posY+10,
			"緯度" + "\n" +  
			"LAT: " + station.getLatitude()
		);
		pageTitleLatitude.setFontStyle("kaiu.ttf");
		pageTitleLatitude.setFontSize(14);

		// longitude
		TextBox pageTitleLongitude = c.addText(
			posX+1300,
			posY+10,
			"經度" + "\n" +  
			"LON: " + station.getLongitude()
		);
		pageTitleLongitude.setFontStyle("kaiu.ttf");
		pageTitleLongitude.setFontSize(14);
		
		// barometer height above sea
		TextBox pageTitleBarometerHeightAboveSea = c.addText(
			posX+1580,
			posY+10,
			"氣壓計海拔高度" + "\n" +  
			"BAROMETER ABOVE MSL " + station.getPrintedHBarometer() + " " + "gpm"
		);
		pageTitleBarometerHeightAboveSea.setFontStyle("kaiu.ttf");
		pageTitleBarometerHeightAboveSea.setFontSize(14);
	}
	
    private List<String> getColumnNames(StandardBookTable table){
    	List<StandardGroup> groups = StandardGroup.LOOKUP_FROM_TABLE(table);
    	List<String> columnNames = new ArrayList<>();
    	for(StandardGroup g : groups) {
    		List<StandardColumn> columns = g.getColumns();
    		columns.forEach( e -> columnNames.add(e.getColumnNameInBook()));
    	}
    	return columnNames;
    }
    
    private List<String> getGroupNames(StandardBookTable table){
    	List<StandardGroup> groups = StandardGroup.LOOKUP_FROM_TABLE(table);
    	List<String> groupNames = new ArrayList<>();
    	groups.forEach( g -> groupNames.add(g.getGroupNameInBook()));
    	return groupNames;
    }
	
	private CDMLTable createSingleTable(XYChart chart, StandardBookTable tableType
										, int posX, int posY
										, Map<Integer, List<String>> datas){
	
		List<String> columnNames = getColumnNames(tableType);
		List<String> groupNames = getGroupNames(tableType);
		
		// Adjust the day column space to make all tables the same size
		int cellHorizontalMargin = 0;
		String dayBlankSpace = "";
		switch(tableType){
			case TABLE1:
				dayBlankSpace = "     ";
				cellHorizontalMargin = 18;
				break;
			case TABLE2:
				dayBlankSpace = " ";
				cellHorizontalMargin = 12;
				break;
			case TABLE3:
				dayBlankSpace = " ";
				cellHorizontalMargin = 15;
				break;
			case TABLE4:
				dayBlankSpace = "   ";
				cellHorizontalMargin = 19;
				break;
			case TABLE5: 
				dayBlankSpace = "    ";
				cellHorizontalMargin = 20;
				break;
		}

		// create table
		CDMLTable table = chart.addTable(posX, posY, 0,
			columnNames.size()+2, // column count
			tableType.rowCount()+2 // row count
		);
		
		// Set display attributes
		TextBox cellStyle = table.getStyle();
		cellStyle.setMargin(cellHorizontalMargin, cellHorizontalMargin, 2, 2); // (left, right, top, bottom)
		cellStyle.setFontStyle("kaiu.ttf");
		cellStyle.setFontSize(CELL_FONT_SIZE);
		
		// Set light gray background row
		table.getRowStyle(11).setBackground(0xf0f0f0, 1);
		table.getRowStyle(21).setBackground(0xf0f0f0, 1);
		table.getRowStyle(33).setBackground(0xf0f0f0, 1);
		
		 // Set group title (SYNOPTIC, SURFACE, 1000hPa...)
		switch(tableType){
			case TABLE1:
				table.setCell(1, 0, 1, 1, groupNames.get(0));
				table.setCell(2, 0, 6, 1, groupNames.get(1));
				table.setCell(8, 0, 6, 1, groupNames.get(2));
				table.setCell(14, 0, 6, 1, groupNames.get(3));
				break;
			case TABLE2:	
				table.setCell(1, 0, 6, 1, groupNames.get(0));
				table.setCell(7, 0, 6, 1, groupNames.get(1));
				table.setCell(13, 0, 6, 1, groupNames.get(2));
				table.setCell(19, 0, 6, 1, groupNames.get(3));
				break;
			case TABLE3:
				table.setCell(1, 0, 6, 1, groupNames.get(0));
				table.setCell(7, 0, 4, 1, groupNames.get(1));
				table.setCell(11, 0, 4, 1, groupNames.get(2));
				table.setCell(15, 0, 4, 1, groupNames.get(3));
				table.setCell(19, 0, 4, 1, groupNames.get(4));
				break;
			case TABLE4:
				table.setCell(1, 0, 4, 1, groupNames.get(0));
				table.setCell(5, 0, 4, 1, groupNames.get(1));
				table.setCell(9, 0, 4, 1, groupNames.get(2));
				table.setCell(13, 0, 4, 1, groupNames.get(3));
				table.setCell(17, 0, 4, 1, groupNames.get(4));
				break;
			case TABLE5:
				table.setCell(1, 0, 4, 1, groupNames.get(0));
				table.setCell(5, 0, 5, 1, groupNames.get(1));
				table.setCell(10, 0, 5, 1, groupNames.get(2));
				table.setCell(15, 0, 5, 1, groupNames.get(3));
				break;
		}
			
		// set day title 
		table.getCell(0, 1).setMargin(4, 4, 3, 3);
		table.setCell(0, 1, 1, 1, dayBlankSpace+"DAY"+dayBlankSpace);
		table.getCell(table.getColCount()-1, 1).setMargin(4, 4, 3, 3);
		table.setCell(table.getColCount()-1, 1, 1, 1, dayBlankSpace+"DAY"+dayBlankSpace);
		
		// set column titles
		for(int col=0; col < table.getColCount()-2; col++){
			table.setCell(col+1, 1, 1, 1, columnNames.get(col));
		}

		// set day value
		for (int row = 0; row < table.getRowCount()-2; row++) {
			if(row<=32){
				table.setCell(0, row+2, 1, 1, String.valueOf(row+1));
				table.setCell(table.getColCount()-1, row+2, 1, 1, String.valueOf(row+1));
			}
		}
		
		// set statistic column name
		int indexOfSum = 0;
		if(tableType.hasSumRow()){
			indexOfSum = 1;
			table.setCell(0, 1+31+1, 1, 1, "Sum.");
			table.setCell(table.getColCount()-1, 1+31+1, 1, 1, "Sum.");
		}
		
		table.setCell(0, 1+31+indexOfSum+1, 1, 1, "No.");
		table.setCell(0, 1+31+indexOfSum+2, 1, 1, "Mean.");
		table.setCell(0, 1+31+indexOfSum+3, 1, 1, "Max.");
		table.setCell(0, 1+31+indexOfSum+4, 1, 1, "Date");
		table.setCell(0, 1+31+indexOfSum+5, 1, 1, "Min.");
		table.setCell(0, 1+31+indexOfSum+6, 1, 1, "Date");
		
		table.setCell(table.getColCount()-1, 1+31+indexOfSum+1, 1, 1, "No.");
		table.setCell(table.getColCount()-1, 1+31+indexOfSum+2, 1, 1, "Mean.");
		table.setCell(table.getColCount()-1, 1+31+indexOfSum+3, 1, 1, "Max.");
		table.setCell(table.getColCount()-1, 1+31+indexOfSum+4, 1, 1, "Date");
		table.setCell(table.getColCount()-1, 1+31+indexOfSum+5, 1, 1, "Min.");
		table.setCell(table.getColCount()-1, 1+31+indexOfSum+6, 1, 1, "Date");
		
		// put values to the table
		for (int j=0; j<datas.size(); j++){
			for (int i=0; i<datas.get(j+1).size(); i++){
				table.setCell(1+i, 2+j, 1, 1, String.format("%1$6s", datas.get(j+1).get(i)));
			}
		}
		
		// Adjust position, align the table in the center of the chart
		final int marginLeft = (CHART_WIDTH - table.getWidth()) / 2;
		table.setPos(marginLeft, posY);
				
		return table;
	}
	
	
	protected BaseChart createPage2Chart(Station station, YearMonth yearMonth, String hour
							, Map<StandardBookTable, Map<Integer, List<String>>> tablyMap)  {
		
		XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
		
		// add a title to the chart
		chart.addTitle(
				"\n" +
				"標 準 氣 壓 面 觀 測 紀 錄 表" + "\n" +
				"DATA AT STANDARD LEVELS",
				"kaiu.ttf", 22
		);

		// set 1st part title
		setupTitleText(chart, TABLE_MARGIN_LEFT, TITLE_MARGIN_TOP, station, yearMonth, hour);
		
		// create table
		final int marginTop_table1 = TABLE_MARGIN_TOP;
		final CDMLTable table1 = createSingleTable(chart, StandardBookTable.TABLE4, TABLE_MARGIN_LEFT, marginTop_table1
													, tablyMap.get(StandardBookTable.TABLE4));
		
  		// 2nd part's title
		TextBox titleTropopause = chart.addText(900, 1300, "對 流 層 頂" + "\n" + "TROPOPAUSE"); // x, y, text
		titleTropopause.setFontStyle("kaiu.ttf");
		titleTropopause.setFontSize(22);
		TextBox titleLastLevel= chart.addText(1550, 1300,  "最 終 層" + "\n" + "LAST LEVELS");
		titleLastLevel.setFontStyle("kaiu.ttf");
		titleLastLevel.setFontSize(22);
		
		// 2nd part's secondary title
		TextBox title_I_II = chart.addText(780, 1370, "I                          II");
		title_I_II.setFontStyle("kaiu.ttf");
		title_I_II.setFontSize(22);
		
		// set title text
		setupTitleText(chart, TABLE_MARGIN_LEFT-10, 1380+70, station, yearMonth, hour);
		
		// create table
		final CDMLTable table2 = createSingleTable(chart, StandardBookTable.TABLE5, TABLE_MARGIN_LEFT, 1380+70+60
													, tablyMap.get(StandardBookTable.TABLE5));
		
		return chart;
	}
	
}
