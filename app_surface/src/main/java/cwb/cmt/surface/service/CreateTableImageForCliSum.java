package cwb.cmt.surface.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.surface.model.CliSum;
import cwb.cmt.surface.utils.SpecialValue;
import cwb.cmt.surface.utils.StatisticalData;

@Service("createTableImageForCliSum")
public class CreateTableImageForCliSum extends CreateTableImage {
	
    // Layout attributes
    private final int PageNumberTextMarginBottom = 103 + 13;
    private final int SectionTitleTextSize = 23;
    private final int PageMainTitleTextSize = 22;
    private final int PageSubTitleTextSize = 19;
    private final int PageNumberTextSize = 17;
    private final int TableHeaderRowCellTextSize = 14;
    private final int TableCellTextSize = 15;
    private final int TableCellHorizontalMargin = 4;
    private final int TableCellVerticalMargin = 9;
    private static final String[] Timeline = {
			"Jan", "Feb", "Mar", "Apr",
			"May", "Jun", "Jul", "Aug",
			"Sep", "Oct", "Nov", "Dec",
			String.valueOf(Calendar.getInstance().get(Calendar.YEAR))};

	private boolean mStarred;
	
    //Time: year
    @Resource(name="year")
    protected int year;
	
    //Time: year
    @Resource(name="month")
    protected int month;
    
    @Resource(name="statisticalData")
    StatisticalData statisticalData;
    
    public boolean isStarred() {
    	return mStarred;
    }
    
    public void setStar(boolean star) {
    	mStarred = star;
    }
    
    protected BaseChart createChart(Object... objects) {
		boolean firstPage = (boolean)objects[0];
		String TableCNum = (String)objects[1];
		String TableENum = (String)objects[2];
		String StnCName = (String)objects[3];
		String StnEName = (String)objects[4];
		@SuppressWarnings("unchecked")
		List<CliSum> monthDataList = (List<CliSum>) objects[5];
		@SuppressWarnings("unchecked")
		List<CliSum> hourAvgList = (List<CliSum>) objects[6];
		@SuppressWarnings("unchecked")
		List<CliSum> dayAvgList = (List<CliSum>)objects[7];
		@SuppressWarnings("unchecked")
		List<CliSum> hourSumList = (List<CliSum>) objects[8];
		@SuppressWarnings("unchecked")
		List<CliSum> precpDays_10mmList = (List<CliSum>) objects[9];
		@SuppressWarnings("unchecked")
		List<CliSum> precpDays_1mmList = (List<CliSum>) objects[10];
		@SuppressWarnings("unchecked")
		List<CliSum> precpDays_01mmList = (List<CliSum>) objects[11];
		@SuppressWarnings("unchecked")
		List<CliSum> weatherConditionList = (List<CliSum>) objects[12];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_LE0List = (List<CliSum>)objects[13];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_LE10List = (List<CliSum>)objects[14];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_GE20List = (List<CliSum>)objects[15];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_GE25List = (List<CliSum>)objects[16];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_GE30List = (List<CliSum>)objects[17];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_GE35List = (List<CliSum>)objects[18];
		
		@SuppressWarnings("unchecked")
		List<CliSum> precpDayList = (List<CliSum>)objects[19];
		@SuppressWarnings("unchecked")
		List<CliSum> hazeDayList = (List<CliSum>)objects[20];
		@SuppressWarnings("unchecked")
		List<CliSum> hailDayList =(List<CliSum>)objects[21];
		@SuppressWarnings("unchecked")
		List<CliSum> dewDayList = (List<CliSum>)objects[22];
		@SuppressWarnings("unchecked")
		List<CliSum> snowDateList = (List<CliSum>)objects[23];
		@SuppressWarnings("unchecked")
		List<CliSum> frostDateList = (List<CliSum>)objects[24];
		
    	Chart.setLicenseCode("DEVP-2LPQ-D6YH-UTLK-EBC0-3E26");
    	
        // Create a XYChart object
    	XYChart chart = new XYChart(CHART_WIDTH,CHART_HEIGHT);
    	
        // section title
    	if(firstPage) {
	        chart.addTitle(
	            "1.3   基本氣象綱要表" + "  " + "General Table for Climatological Summaries",
	            "kaiu.ttf",
	            SectionTitleTextSize
			);
    	}
        
        // page title
        TextBox page_main_title = chart.addText(
        		CHART_WIDTH/2,
        		62,
        		TableCNum+StnCName.replace("", "  ").trim()	
        );
        page_main_title.setFontStyle("kaiu.ttf");
        page_main_title.setFontSize(PageMainTitleTextSize);
        page_main_title.setPos(CHART_WIDTH/2 - page_main_title.getWidth()/2, page_main_title.getTopY());
        
        // Add star to the title, not used
        if (isStarred()) {
	        TextBox note_page_main_title = chart.addText(
	        		page_main_title.getLeftX() + page_main_title.getWidth() - 20,
	        		page_main_title.getTopY(),
	        		"*"
	        );
	        note_page_main_title.setFontStyle("kaiu.ttf");
	        note_page_main_title.setFontSize(PageMainTitleTextSize);
	        note_page_main_title.setFontColor(0x00ff0000);
        }
        
        // page subtitle
        TextBox page_subtitle = chart.addText(
        		CHART_WIDTH/2,
        		page_main_title.getTopY() + page_main_title.getHeight() + 27,
        		" "+TableENum+StnEName
        );
        page_subtitle.setFontStyle("kaiu.ttf");
        page_subtitle.setFontSize(PageSubTitleTextSize);
        page_subtitle.setPos(CHART_WIDTH/2 - page_subtitle.getWidth()/2, page_subtitle.getTopY());
        
        // Create a CDMLTable
        CDMLTable table = createTable(chart, 80, TABLE_MARGIN_TOP , monthDataList, hourAvgList,
        		dayAvgList, hourSumList, precpDays_10mmList, precpDays_1mmList, precpDays_01mmList,
        		weatherConditionList, TxMinAbs_LE0List, TxMinAbs_LE10List, TxMinAbs_GE20List, TxMinAbs_GE25List,
        		TxMinAbs_GE30List, TxMinAbs_GE35List, precpDayList, hazeDayList, hailDayList, dewDayList,
        		snowDateList, frostDateList);
        return chart;
    }
    
    private CDMLTable createTable(XYChart chart, int posX, int posY, List<CliSum> monthDataList,
    		List<CliSum> hourAvgList, List<CliSum> dayAvgList, List<CliSum> hourSumList,
    		List<CliSum> precpDays_10mmList, List<CliSum> precpDays_1mmList, List<CliSum> precpDays_01mmList,
    		List<CliSum> weatherConditionList, List<CliSum> TxMinAbs_LE0List, List<CliSum> TxMinAbs_LE10List,
    		List<CliSum> TxMinAbs_GE20List, List<CliSum> TxMinAbs_GE25List, List<CliSum> TxMinAbs_GE30List,
    		List<CliSum> TxMinAbs_GE35List, List<CliSum> precpDayList, List<CliSum> hazeDayList,
    		List<CliSum> hailDayList, List<CliSum> dewDayList, List<CliSum> snowDateList,
    		List<CliSum> frostDateList){
    	// Argument: x, y, default column-position, colCount, rowCount 
        CDMLTable table = chart.addTable(
    		posX, posY, 0,
    		24,
    		17+17+16
		);
        
        int lastRowIndex = 0;
        
        lastRowIndex = build1stTable(table, 80, TABLE_MARGIN_TOP, lastRowIndex,
        		monthDataList, hourAvgList, dayAvgList, hourSumList);
        lastRowIndex = build2ndTable(table, 80, TABLE_MARGIN_TOP, lastRowIndex+1, monthDataList,
        		hourAvgList, dayAvgList, hourSumList, precpDays_10mmList, precpDays_1mmList, precpDays_01mmList);        
        lastRowIndex = build3rdTable(table, 80, TABLE_MARGIN_TOP, lastRowIndex+1, monthDataList,
        		hourAvgList, dayAvgList, weatherConditionList, TxMinAbs_LE0List, TxMinAbs_LE10List,
        		TxMinAbs_GE20List, TxMinAbs_GE25List, TxMinAbs_GE30List, TxMinAbs_GE35List, precpDayList,
        		hazeDayList, hailDayList, dewDayList, snowDateList, frostDateList);
        
        // Adjust position
        final int adjustedPosX = (CHART_WIDTH-table.getWidth())/2 - 1;
        table.setPos(adjustedPosX, TABLE_MARGIN_TOP );      
    	return table;
    }
    
    
    private int build1stTable(CDMLTable table,
                               int posX, int posY,
                               int cellRowStartIndex,
                               List<CliSum> monthDataList,
                               List<CliSum> hourAvgList,
                               List<CliSum> dayAvgList,
                               List<CliSum> hourSumList
                               ){
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.CeUnknown.getNumber(),
				SpecialValue.NewStation.getNumber(), 
				SpecialValue.CancelStation.getNumber(),
				SpecialValue.Trace.getNumber(),
				SpecialValue.SubstituteZero.getNumber()
				);
    	
    	TextBox cellStyle = table.getStyle();
        cellStyle.setMargin(
    		TableCellHorizontalMargin, // left
    		TableCellHorizontalMargin, // right
    		TableCellVerticalMargin,   // top
    		TableCellVerticalMargin    // bottom
		);  
        cellStyle.setFontStyle("kaiu.ttf");
        cellStyle.setFontSize(TableCellTextSize);
        
        // set table header row's fontsize
        for (int row=0; row<3; row++) {
        	for (int col=0; col<24; col++) {
        		table.getCell(col, cellRowStartIndex+row)
        			 .setFontSize(TableHeaderRowCellTextSize);
        	}
        }
        
        // month
		table.setCell(0, cellRowStartIndex+0, 1, 3, "月 份\nMonth");
		// pressure
		table.setCell(1, cellRowStartIndex+0, 2, 3, "氣壓\nStation\nPressure\nhPa");
		// air temperature
		table.setCell(3, cellRowStartIndex+0, 11, 1, "氣溫 Air Temperature ℃");
		table.setCell(3, cellRowStartIndex+1, 1, 2, "5h");
		table.setCell(4, cellRowStartIndex+1, 1, 2, "14h");
		table.setCell(5, cellRowStartIndex+1, 1, 2, "21h");
		table.setCell(6, cellRowStartIndex+1, 2, 2, "平均\nMean\nof obs.");
		table.setCell(8, cellRowStartIndex+1, 2, 1, "平均Mean");
		table.setCell(8, cellRowStartIndex+2, 1, 1, "最高\nMax.");
		table.setCell(9, cellRowStartIndex+2, 1, 1, "最低\nMin.");
		table.setCell(10, cellRowStartIndex+1, 4, 1, "絕對 Absolute");
		table.setCell(10, cellRowStartIndex+2, 1, 1, "最高\nMax.");
		table.setCell(11, cellRowStartIndex+2, 1, 1, " 月 日 \nDate");
		table.setCell(12, cellRowStartIndex+2, 1, 1, "最低\nMin.");
		table.setCell(13, cellRowStartIndex+2, 1, 1, " 月 日 \nDate");
		// relative humidity
		table.setCell(14, cellRowStartIndex+0, 6, 2, "相對濕度\nRelative Humidity %");
		table.setCell(14, cellRowStartIndex+2, 2, 1, "5h"); //TODO:
		table.setCell(16, cellRowStartIndex+2, 1, 1, "14h");
		table.setCell(17, cellRowStartIndex+2, 1, 1, "21h");
		table.setCell(18, cellRowStartIndex+2, 1, 1, "平均\nMean");
		table.setCell(19, cellRowStartIndex+2, 1, 1, "最小\nMin.");
		// sunshine
		table.setCell(20, cellRowStartIndex+0, 3, 2, "日照時數\nDuration of Sunshine");
		table.setCell(20, cellRowStartIndex+2, 2, 1, "總  計\nTotal of hr");
		table.setCell(22, cellRowStartIndex+2, 1, 1, "百 分\n%");
		// month
		table.setCell(23, cellRowStartIndex+0, 1, 3, "月 份\nMonth");
		
        // Put the values to the table.
		final int offsetY = 3;
		int count = 0;
		int rowRange = (monthDataList.size() < month)? monthDataList.size() : month;// cancel station=6; general=7
        int countRange = (hourAvgList.size() < (month*3)) ? hourAvgList.size() : (month*3);
        
        CliSum annualCliSum  = new CliSum();
        Float sunshineSum = 0.0f;
        Float stnPresSum =  0.0f;
        Float tx5HSum = 0.0f;
        Float tx14HSum = 0.0f;
        Float tx21HSum = 0.0f;
        Float txSum = 0.0f;
        Float txMaxAvg = 0.0f;
        Float txMinAvg = 0.0f;
        List<String> txMaxAbs = new ArrayList<>();
        List<String> txMinAbs = new ArrayList<>();
        List<String> rhMin = new ArrayList<>();
        Map<Float, LocalDateTime> txMaxAbsTime = new HashMap<>();
        Map<Float, LocalDateTime> txMinAbsTime = new HashMap<>();
        Float rh5HSum = 0.0f;
        Float rh14HSum = 0.0f;
        Float rh21HSum = 0.0f;
        Float rhSum = 0.0f;
        Float sunshineRateSum = 0.0f;
        
		for (int row=0; row<12; row++) {
			// month
			table.setCell(0, row+offsetY, 1, 1, Timeline[row]);
			for(int r=0; r<rowRange; r++) { // condition: cancel station (r=rowRange=7, loop end)
				if(count<countRange) {
			    	// pressure
			    	table.setCell(1, cellRowStartIndex+r+offsetY, 2, 1, StringToBigdecimal(monthDataList.get(r).getStnPres()));  // 氣壓
			    	// air temperature
			    	table.setCell(3, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(hourAvgList.get(count).getTxAvg()));  // 氣溫 >> 5h
			    	table.setCell(4, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(hourAvgList.get(count+1).getTxAvg()));  // 氣溫 >> 14h
			    	table.setCell(5, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(hourAvgList.get(count+2).getTxAvg()));  // 氣溫 >> 21h
			    	table.setCell(6, cellRowStartIndex+r+offsetY, 2, 1, StringToBigdecimal(monthDataList.get(r).getTx()));  // 氣溫 >> 平均 Mean of obs.
			    	table.setCell(8, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(dayAvgList.get(r).getTxMaxAvg()));  // 氣溫 >> 平均 Mean >> 最高
			    	table.setCell(9, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(dayAvgList.get(r).getTxMinAvg()));  // 氣溫 >> 平均 Mean >> 最低
			    	table.setCell(10, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getTxMaxAbs())); // 氣溫 >> 絕對 >> 最高
			    	table.setCell(11, cellRowStartIndex+r+offsetY, 1, 1, String.valueOf(monthDataList.get(r).getTxMaxAbsTime().getDayOfMonth())); // 氣溫 >> 絕對 >> 月 日
			    	table.setCell(12, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getTxMinAbs())); // 氣溫 >> 絕對 >> 最低
			    	table.setCell(13, cellRowStartIndex+r+offsetY, 1, 1, String.valueOf(monthDataList.get(r).getTxMinAbsTime().getDayOfMonth())); // 氣溫 >> 絕對 >> 月 日
			    	// relative humidity
			    	table.setCell(14, cellRowStartIndex+r+offsetY, 2, 1, StringToBigdecimal2(hourAvgList.get(count).getRhAvg())); // 相對濕度 >> 5h
			    	table.setCell(16, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal2(hourAvgList.get(count+1).getRhAvg())); // 相對濕度 >> 14h
			    	table.setCell(17, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal2(hourAvgList.get(count+2).getRhAvg())); // 相對濕度 >> 21h
			    	table.setCell(18, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal2(monthDataList.get(r).getRh())); // 相對濕度 >> 平均
			    	table.setCell(19, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal2(monthDataList.get(r).getRhMin())); // 相對濕度 >> 最小
			    	// sunshine
			    	table.setCell(20, cellRowStartIndex+r+offsetY, 2, 1, StringToBigdecimal(monthDataList.get(r).getSunshine())); // 日照時數 >> 總計
			    	table.setCell(22, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getSunshineRate())); // 相對濕度 >> 百分%
			    	//sum sunshine; return float value
			    	stnPresSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getStnPres()));
			    	tx5HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count).getTxAvg()));
			    	tx14HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+1).getTxAvg()));
			    	tx21HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+2).getTxAvg()));
			    	txSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getTx()));
			    	txMaxAvg += statisticalData.Sum(StringToBigdecimal(dayAvgList.get(r).getTxMaxAvg()));
			    	txMinAvg += statisticalData.Sum(StringToBigdecimal(dayAvgList.get(r).getTxMinAvg()));
			    	txMaxAbs.add(StringToBigdecimal(monthDataList.get(r).getTxMaxAbs()));
			    	//getTxMaxAbsTime()
			    	txMaxAbsTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getTxMaxAbs())), monthDataList.get(r).getTxMaxAbsTime());
			    	txMinAbs.add(StringToBigdecimal(monthDataList.get(r).getTxMinAbs()));
			    	txMinAbsTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getTxMinAbs())), monthDataList.get(r).getTxMinAbsTime());
			    	
			    	rh5HSum += statisticalData.Sum(StringToBigdecimal2(hourAvgList.get(count).getRhAvg()));
			    	rh14HSum += statisticalData.Sum(StringToBigdecimal2(hourAvgList.get(count+1).getRhAvg()));
			    	rh21HSum += statisticalData.Sum(StringToBigdecimal2(hourAvgList.get(count+2).getRhAvg()));
			    	rhSum += statisticalData.Sum(StringToBigdecimal2(monthDataList.get(r).getRh()));
			    	rhMin.add(StringToBigdecimal2(monthDataList.get(r).getRhMin()));
			    	sunshineSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getSunshine()));
			    	sunshineRateSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getSunshineRate()));
				}
				count+=3;//calculate 
				//annual 
				annualCliSum.setStnPres(StringToBigdecimal(String.valueOf(stnPresSum/rowRange)));
				annualCliSum.setTx_5H((StringToBigdecimal(String.valueOf(tx5HSum/rowRange))));
				annualCliSum.setTx_14H(StringToBigdecimal(String.valueOf(tx14HSum/rowRange)));
				annualCliSum.setTx_21H(StringToBigdecimal(String.valueOf(tx21HSum/rowRange)));
				annualCliSum.setTx(StringToBigdecimal(String.valueOf(txSum/rowRange)));
				annualCliSum.setTxMaxAvg(StringToBigdecimal(String.valueOf(txMaxAvg/rowRange)));
				annualCliSum.setTxMinAvg(StringToBigdecimal(String.valueOf(txMinAvg/rowRange)));
				annualCliSum.setTxMaxAbs(StringToBigdecimal(String.valueOf(statisticalData.Max(txMaxAbs))));
				
				annualCliSum.setTxMinAbs(StringToBigdecimal(String.valueOf(statisticalData.Min(txMinAbs))));
				
				annualCliSum.setRh_5H((StringToBigdecimal2(String.valueOf(rh5HSum/rowRange))));
				annualCliSum.setRh_14H(StringToBigdecimal2(String.valueOf(rh14HSum/rowRange)));
				annualCliSum.setRh_21H(StringToBigdecimal2(String.valueOf(rh21HSum/rowRange)));
				annualCliSum.setRh(StringToBigdecimal2(String.valueOf(rhSum/rowRange)));
				annualCliSum.setRhMin(StringToBigdecimal2(String.valueOf(statisticalData.Min(rhMin))));
				annualCliSum.setSunshine(StringToBigdecimal(String.valueOf(sunshineSum)));
				annualCliSum.setSunshineRate((StringToBigdecimal(String.valueOf(sunshineRateSum/rowRange))));
			}
			
			if(row>=rowRange) { //rowRange=7 or 6
				//pressure
		    	table.setCell(1, cellRowStartIndex+row+offsetY, 2, 1, "    ");  // 氣壓
		    	// air temperature
		    	table.setCell(3, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 氣溫 >> 5h
		    	table.setCell(4, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 氣溫 >> 14h
		    	table.setCell(5, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 氣溫 >> 21h
		    	table.setCell(6, cellRowStartIndex+row+offsetY, 2, 1, "    ");  // 氣溫 >> 平均 Mean of obs.
		    	table.setCell(8, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 氣溫 >> 平均 Mean >> 最高
		    	table.setCell(9, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 氣溫 >> 平均 Mean >> 最低
		    	table.setCell(10, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 氣溫 >> 絕對 >> 最高
		    	table.setCell(11, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 氣溫 >> 絕對 >> 月 日
		    	table.setCell(12, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 氣溫 >> 絕對 >> 最低
		    	table.setCell(13, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 氣溫 >> 絕對 >> 月 日
		    	// relative humidity
		    	table.setCell(14, cellRowStartIndex+row+offsetY, 2, 1, "    "); // 相對濕度 >> 5h
		    	table.setCell(16, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 相對濕度 >> 14h
		    	table.setCell(17, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 相對濕度 >> 21h
		    	table.setCell(18, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 相對濕度 >> 平均
		    	table.setCell(19, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 相對濕度 >> 最小
		    	// sunshine
		    	table.setCell(20, cellRowStartIndex+row+offsetY, 2, 1, "    "); // 日照時數 >> 總計
		    	table.setCell(22, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 相對濕度 >> 百分%
			}
			// month
	    	table.setCell(23, cellRowStartIndex+row+offsetY, 1, 1, (row+1) + " " + Timeline[row]);
		}
	        
	        // The last row: average
			final int annualAvgRow = cellRowStartIndex+12+offsetY;
	        table.setCell(0, annualAvgRow, 1, 1, String.valueOf(year));
	
			// air temperature
			table.setCell(1, annualAvgRow, 2, 1, annualCliSum.getStnPres());
			// temperature
			table.setCell(3, annualAvgRow, 1, 1, annualCliSum.getTx_5H());	
			table.setCell(4, annualAvgRow, 1, 1, annualCliSum.getTx_14H());	
			table.setCell(5, annualAvgRow, 1, 1, annualCliSum.getTx_21H());	
			table.setCell(6, annualAvgRow, 2, 1, annualCliSum.getTx());	
			table.setCell(8, annualAvgRow, 1, 1, annualCliSum.getTxMaxAvg());	
			table.setCell(9, annualAvgRow, 1, 1, annualCliSum.getTxMinAvg());	
			table.setCell(10, annualAvgRow, 1, 1, annualCliSum.getTxMaxAbs());
			table.setCell(11, annualAvgRow, 1, 1, statisticalData.MaxTime(txMaxAbsTime));	
			table.setCell(12, annualAvgRow, 1, 1, annualCliSum.getTxMinAbs());	
			table.setCell(13, annualAvgRow, 1, 1, statisticalData.MinTime(txMinAbsTime));
			// relative humidity
			table.setCell(14, annualAvgRow, 2, 1, annualCliSum.getRh_5H());	
			table.setCell(16, annualAvgRow, 1, 1, annualCliSum.getRh_14H());	
			table.setCell(17, annualAvgRow, 1, 1, annualCliSum.getRh_21H());	
			table.setCell(18, annualAvgRow, 1, 1, annualCliSum.getRh());	
			table.setCell(19, annualAvgRow, 1, 1, annualCliSum.getRhMin());	
			// sunshine
			table.setCell(20, annualAvgRow, 2, 1, annualCliSum.getSunshine());	
			table.setCell(22, annualAvgRow, 1, 1, annualCliSum.getSunshineRate());	
	
	        table.setCell(23, annualAvgRow, 1, 1, "年 " + year);
			return annualAvgRow;
    }
    
    
	private int build2ndTable(CDMLTable table,
				  int posX, int posY,
				  int cellRowStartIndex,
				  List<CliSum> monthDataList,
				  List<CliSum> hourAvgList,
                  List<CliSum> dayAvgList, 
                  List<CliSum> hourSumList,
                  List<CliSum> precpDays_10mmList,
                  List<CliSum> precpDays_1mmList,
                  List<CliSum> precpDays_01mmList
				  ){
		
		
    	TextBox cellStyle = table.getStyle();
        cellStyle.setMargin(
    		TableCellHorizontalMargin, // left
    		TableCellHorizontalMargin, // right
    		TableCellVerticalMargin,   // top
    		TableCellVerticalMargin    // bottom
		);  
        cellStyle.setFontStyle("kaiu.ttf");
        cellStyle.setFontSize(TableCellTextSize);
        
        // set table header row's fontsize
        for (int row=0; row<3; row++) {
        	for (int col=0; col<24; col++) {
        		table.getCell(col, cellRowStartIndex+row)
        			 .setFontSize(TableHeaderRowCellTextSize);
        	}
        }
		
		// month
		table.setCell(0, cellRowStartIndex+0, 1, 4, "月 份\nMonth");
		// wind
		table.setCell(1, cellRowStartIndex+0, 5, 0, "風 Wind");
		table.setCell(1, cellRowStartIndex+1, 1, 3, "平均風速\nWind\nSpeed\nm/s");
		table.setCell(2, cellRowStartIndex+1, 1, 3, "最多\n風向\nPrev.\nDir.");
		// wind - max
		table.setCell(3, cellRowStartIndex+1, 3, 1, "最大 Maximum");
		table.setCell(3, cellRowStartIndex+2, 1, 2, "速\nSpeed");
		table.setCell(4, cellRowStartIndex+2, 1, 2, "方向\nDir.");
		table.setCell(5, cellRowStartIndex+2, 1, 2, "月 日 \nDate");
		// wind - gust
		table.setCell(6, cellRowStartIndex+0, 2, 1, "最大陣風");
		table.setCell(6, cellRowStartIndex+1, 2, 1, "Gust");
		table.setCell(6, cellRowStartIndex+2, 1, 2, "速\nSpeed");
		table.setCell(7, cellRowStartIndex+2, 1, 2, "月 日 \nDate");
		// cloud amount
		table.setCell(8, cellRowStartIndex+0, 4, 1, "雲量Cloud Amout 0-10");
		table.setCell(8, cellRowStartIndex+1, 1, 3, "5h");
		table.setCell(9, cellRowStartIndex+1, 1, 3, "14h");
		table.setCell(10, cellRowStartIndex+1, 1, 3, "21h");
		table.setCell(11, cellRowStartIndex+1, 1, 3, "平均\nMean\nof\nobs.");
		// visibility
		table.setCell(12, cellRowStartIndex+0, 2, 4, "能見度\nVisibility\nkm");
		// precipitation
		table.setCell(14, cellRowStartIndex+0, 4, 2, "降水量\nPrecipitation mm");
		table.setCell(14, cellRowStartIndex+2, 2, 2, "總計\nTotal");
		table.setCell(16, cellRowStartIndex+2, 2, 1, "最大Max.");
		table.setCell(16, cellRowStartIndex+3, 1, 1, "一日間\nMax.\nin 24h");
		table.setCell(17, cellRowStartIndex+3, 1, 1, "月 日 \nDate");
		// precipitation hour
		table.setCell(18, cellRowStartIndex+0, 2, 4, "降水時數\nPrecipitation\nDuration");
		// precipitation days
		table.setCell(20, cellRowStartIndex+0, 3, 2, "降水日數\nNo. of Precipitation Days"); 
		table.setCell(20, cellRowStartIndex+2, 1, 2, "總計\nTotal\n≧0.1mm");
		table.setCell(21, cellRowStartIndex+2, 1, 2, "\n≧1.0\nmm");
		table.setCell(22, cellRowStartIndex+2, 1, 2, "\n≧10.0\nmm");
		// month
		table.setCell(23, cellRowStartIndex+0, 1, 4, "月 份\nMonth");
		
		int rowRange = (monthDataList.size() < month)? monthDataList.size() : month;// cancel station=6; general=7
        int countRange = (hourAvgList.size() < (month*3)) ? hourAvgList.size() : (month*3);
        
        CliSum annualCliSum  = new CliSum();
        Float ws = 0.0f;
        List<Float> wd = new ArrayList<>();
        Float cAmtTotal5HSum = 0.0f;
        Float cAmtTotal5HSum_flag = 0.0f;
        Float cAmtTotal14HSum = 0.0f;
        Float cAmtTotal14HSum_flag = 0.0f;
        Float cAmtTotal21HSum = 0.0f;
        Float cAmtTotal21HSum_flag = 0.0f;
        Float cAmtMean = 0.0f;
        Float cAmtMean_flag = 0.0f;
        List<String> wsMax = new ArrayList<>();
        Map<Float, Float> wdMax = new HashMap<>();
        List<String> wsGust = new ArrayList<>();
        List<String> precp1DayMax = new ArrayList<>();
        Map<Float, LocalDateTime> wMaxTime = new HashMap<>();
        Map<Float, LocalDateTime> wGustMaxTime = new HashMap<>();
        Map<Float, LocalDateTime> precp1DayMaxTime = new HashMap<>();
        Float visbMean = 0.0f;
        Float visbMean_flag = 0.0f;
        Float precpSum = 0.0f;
        Float precpHourSum = 0.0f;
        Float precpHourSum_flag = 0.0f;
        Float precpDays_01mm= 0.0f;
        Float precpDays_1mm = 0.0f;
        Float precpDays_10mm = 0.0f;
        
		// Put the values to the table.
		final int offsetY = 4;
		int count = 0;
		for (int row=0; row<12; row++) {
			// month
			table.setCell(0, cellRowStartIndex+row+offsetY, 1, 1, Timeline[row]);
			// condition: cancel station 
			for(int r=0; r<rowRange; r++) {
				if(count < countRange) {
					// wind
					table.setCell(1, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getWs()));  // 風 >> 平均風速
					table.setCell(2, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getWd()));  // 風 >> 最多風向
					table.setCell(3, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getWsMax()));  // 風 >> 最大 >> 速
					table.setCell(4, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getWdMax()));  // 風 >> 最大 >> 方向
					table.setCell(5, cellRowStartIndex+r+offsetY, 1, 1, String.valueOf(monthDataList.get(r).getwMaxTime().getDayOfMonth()));  // 風 >> 最大 >> 月 日
					table.setCell(6, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getWsGust()));  // 最大陣風 >> Gust >> 速
					table.setCell(7, cellRowStartIndex+r+offsetY, 1, 1, String.valueOf(monthDataList.get(r).getwGustTime().getDayOfMonth()));  // 最大陣風 >> Gust >> 月日
					// cloud amount
					table.setCell(8, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(hourAvgList.get(count).getcAmtTotalAvg()));  // 雲量 >> 5h
					table.setCell(9, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(hourAvgList.get(count+1).getcAmtTotalAvg()));  // 雲量 >> 14h
					table.setCell(10, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(hourAvgList.get(count+2).getcAmtTotalAvg())); // 雲量 >> 21h
					table.setCell(11, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getcAmtMean())); // 雲量 >> 平均 (Mean of obs.)
					// visibility
					table.setCell(12, cellRowStartIndex+r+offsetY, 2, 1, StringToBigdecimal(monthDataList.get(r).getVisbMean())); // 能見度
					// precipitation
					table.setCell(14, cellRowStartIndex+r+offsetY, 2, 1, StringToBigdecimal(monthDataList.get(r).getPrecp())); // 降水量 >> 總計
					table.setCell(16, cellRowStartIndex+r+offsetY, 1, 1, StringToBigdecimal(monthDataList.get(r).getPrecp1DayMax())); // 降水量 >> 最大 >> 一日間
					table.setCell(17, cellRowStartIndex+r+offsetY, 1, 1, String.valueOf(monthDataList.get(r).getPrecp1DayMaxTime().getDayOfMonth())); // 降水量 >> 最大 >> 月日
					// precipitation hours
					table.setCell(18, cellRowStartIndex+r+offsetY, 2, 1, StringToBigdecimal(hourSumList.get(r).getPrecpHourSum())); // 降水時數
					// precipitation days
					table.setCell(20, cellRowStartIndex+r+offsetY, 1, 1, precpDays_01mmList.get(r).getPrecpDays_01mm()); // 降水日數 >> 總計≧0.1mm
					table.setCell(21, cellRowStartIndex+r+offsetY, 1, 1, precpDays_1mmList.get(r).getPrecpDays_1mm()); // 降水日數 >> ≧1.0mm
					table.setCell(22, cellRowStartIndex+r+offsetY, 1, 1, precpDays_10mmList.get(r).getPrecpDays_10mm()); // 降水日數 >> ≧10.0mm
					
					ws += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getWs()));
					wd.add(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWd())));
					wsMax.add(StringToBigdecimal(monthDataList.get(r).getWsMax()));
					wdMax.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWsMax())), 
							Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWdMax())));
					wMaxTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWsMax())),
							monthDataList.get(r).getwMaxTime());
					wsGust.add(StringToBigdecimal(monthDataList.get(r).getWsGust()));
					wGustMaxTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWsGust())), monthDataList.get(r).getwGustTime());
					
					if(statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count).getcAmtTotalAvg()))<0) {
						cAmtTotal5HSum_flag += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count).getcAmtTotalAvg()));
					}else {
						cAmtTotal5HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count).getcAmtTotalAvg()));
					}	
					
					if(statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+1).getcAmtTotalAvg()))<0) {
						cAmtTotal14HSum_flag += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+1).getcAmtTotalAvg()));
					}else {
						cAmtTotal14HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+1).getcAmtTotalAvg()));
					}
					
					if(statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+2).getcAmtTotalAvg()))<0) {
						cAmtTotal21HSum_flag += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+2).getcAmtTotalAvg()));
					}else {
						cAmtTotal21HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+2).getcAmtTotalAvg()));
					}
					if(statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getcAmtMean()))<0) {
						cAmtMean_flag += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getcAmtMean()));
					}else {
						cAmtMean += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getcAmtMean()));
					}
					
					if(statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getVisbMean()))<0) {
						visbMean_flag += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getVisbMean()));
					}else {
						visbMean += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getVisbMean()));
					}
					
					precpSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getPrecp()));
					precp1DayMax.add(monthDataList.get(r).getPrecp1DayMax());
					precp1DayMaxTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getPrecp1DayMax())),
							monthDataList.get(r).getPrecp1DayMaxTime());
					//sum
					if(statisticalData.Sum(StringToBigdecimal(hourSumList.get(r).getPrecpHourSum()))<0) {
						precpHourSum_flag += statisticalData.Sum(StringToBigdecimal(hourSumList.get(r).getPrecpHourSum()));
					}else {
						precpHourSum += statisticalData.Sum(StringToBigdecimal(hourSumList.get(r).getPrecpHourSum()));
					}
					
					precpDays_01mm += statisticalData.Sum(StringToBigdecimal2(precpDays_01mmList.get(r).getPrecpDays_01mm()));
					precpDays_1mm += statisticalData.Sum(StringToBigdecimal2(precpDays_1mmList.get(r).getPrecpDays_1mm()));
					precpDays_10mm += statisticalData.Sum(StringToBigdecimal2(precpDays_10mmList.get(r).getPrecpDays_10mm()));
				}
				count+=3;
				annualCliSum.setWs(StringToBigdecimal(String.valueOf(ws/rowRange)));
				annualCliSum.setWd(StringToBigdecimal(String.valueOf(statisticalData.MaxCount(wd))));
				
				annualCliSum.setWsMax(StringToBigdecimal(String.valueOf(statisticalData.Max(wsMax))));
				annualCliSum.setWdMax(StringToBigdecimal(statisticalData.MaxWd(wdMax)));
				annualCliSum.setWsGust(StringToBigdecimal(String.valueOf(statisticalData.Max(wsGust))));
				
				annualCliSum.setcAmtTotal5H((cAmtTotal5HSum_flag==-(rowRange))?"    ":
					(StringToBigdecimal(String.valueOf(cAmtTotal5HSum/(rowRange+cAmtTotal5HSum_flag)))));
				annualCliSum.setcAmtTotal14H((cAmtTotal14HSum_flag==-(rowRange))?"    ":
					(StringToBigdecimal(String.valueOf(cAmtTotal14HSum/(rowRange+cAmtTotal14HSum_flag)))));
				annualCliSum.setcAmtTotal21H((cAmtTotal21HSum_flag==-(rowRange))?"    ":
					(StringToBigdecimal(String.valueOf(cAmtTotal21HSum/(rowRange+cAmtTotal21HSum_flag)))));
				annualCliSum.setcAmtMean((cAmtMean_flag==-(rowRange))?"    ":
					(StringToBigdecimal(String.valueOf(cAmtMean/(rowRange+cAmtMean_flag)))));
				annualCliSum.setVisbMean((visbMean_flag==-(rowRange))?"    ":
					(StringToBigdecimal(String.valueOf(visbMean/(rowRange+visbMean_flag)))));
				annualCliSum.setPrecp(StringToBigdecimal(String.valueOf(precpSum)));
				annualCliSum.setPrecp1DayMax(StringToBigdecimal(String.valueOf(statisticalData.Max(precp1DayMax))));
				annualCliSum.setPrecpHourSum((precpHourSum_flag==-(rowRange))?"    ":
					(StringToBigdecimal(String.valueOf(precpHourSum))));
//				annualCliSum.setPrecpHourSum(StringToBigdecimal(String.valueOf(precpSum)));
				annualCliSum.setPrecpDays_01mm(StringToBigdecimal2(String.valueOf(precpDays_01mm)));
				annualCliSum.setPrecpDays_1mm(StringToBigdecimal2(String.valueOf(precpDays_1mm)));
				annualCliSum.setPrecpDays_10mm(StringToBigdecimal2(String.valueOf(precpDays_10mm)));
				
			}
			if (row>=rowRange) {
				// wind
				table.setCell(1, cellRowStartIndex+row+offsetY, 1, 1,  "    ");  // 風 >> 平均風速
				table.setCell(2, cellRowStartIndex+row+offsetY, 1, 1,  "    ");  // 風 >> 最多風向
				table.setCell(3, cellRowStartIndex+row+offsetY, 1, 1,  "    ");  // 風 >> 最大 >> 速
				table.setCell(4, cellRowStartIndex+row+offsetY, 1, 1,  "    ");  // 風 >> 最大 >> 方向
				table.setCell(5, cellRowStartIndex+row+offsetY, 1, 1,  "    ");  // 風 >> 最大 >> 月 日
				table.setCell(6, cellRowStartIndex+row+offsetY, 1, 1,  "    ");  // 最大陣風 >> Gust >> 速
				table.setCell(7, cellRowStartIndex+row+offsetY, 1, 1,  "    ");  // 最大陣風 >> Gust >> 月日
				// cloud amount
				table.setCell(8, cellRowStartIndex+row+offsetY, 1, 1,  "    ");  // 雲量 >> 5h
				table.setCell(9, cellRowStartIndex+row+offsetY, 1, 1,  "    ");  // 雲量 >> 14h
				table.setCell(10, cellRowStartIndex+row+offsetY, 1, 1,  "    "); // 雲量 >> 21h
				table.setCell(11, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 雲量 >> 平均 (Mean of obs.)
				// visibility
				table.setCell(12, cellRowStartIndex+row+offsetY, 2, 1, "    "); // 能見度
				// precipitation
				table.setCell(14, cellRowStartIndex+row+offsetY, 2, 1,  "    "); // 降水量 >> 總計
				table.setCell(16, cellRowStartIndex+row+offsetY, 1, 1,  "    "); // 降水量 >> 最大 >> 一日間
				table.setCell(17, cellRowStartIndex+row+offsetY, 1, 1,  "    "); // 降水量 >> 最大 >> 月日
				// precipitation hours
				table.setCell(18, cellRowStartIndex+row+offsetY, 2, 1,  "    "); // 降水時數
				// precipitation days
				table.setCell(20, cellRowStartIndex+row+offsetY, 1, 1,  "    "); // 降水日數 >> 總計≧0.1mm
				table.setCell(21, cellRowStartIndex+row+offsetY, 1, 1,  "    "); // 降水日數 >> ≧1.0mm
				table.setCell(22, cellRowStartIndex+row+offsetY, 1, 1,  "    "); // 降水日數 >> ≧10.0mm
			}
			// month
			table.setCell(23, cellRowStartIndex+row+offsetY, 1, 1, (row+1) + " " + Timeline[row]);
		}
		
		// The last row: average
		final int annualAvgRow = cellRowStartIndex+12+offsetY;
		table.setCell(0, annualAvgRow, 1, 1, String.valueOf(year));
		{
			// wind
			table.setCell(1, annualAvgRow, 1, 1, annualCliSum.getWs());
			table.setCell(2, annualAvgRow, 1, 1, annualCliSum.getWd());	
			table.setCell(3, annualAvgRow, 1, 1, annualCliSum.getWsMax());	
			table.setCell(4, annualAvgRow, 1, 1, annualCliSum.getWdMax());
			table.setCell(5, annualAvgRow, 1, 1, statisticalData.MaxTime(wMaxTime));	
			table.setCell(6, annualAvgRow, 1, 1, annualCliSum.getWsGust());
			table.setCell(7, annualAvgRow, 1, 1, statisticalData.MaxTime(wGustMaxTime));
			// cloud amout
			table.setCell(8, annualAvgRow, 1, 1, (annualCliSum.getcAmtTotal5H().equals("0.0"))?"   ":annualCliSum.getcAmtTotal5H());	
			table.setCell(9, annualAvgRow, 1, 1, (annualCliSum.getcAmtTotal14H().equals("0.0"))?"   ":annualCliSum.getcAmtTotal14H());	
			table.setCell(10, annualAvgRow, 1, 1, (annualCliSum.getcAmtTotal21H().equals("0.0"))?"   ":annualCliSum.getcAmtTotal21H());	
			table.setCell(11, annualAvgRow, 1, 1, (annualCliSum.getcAmtMean().equals("0.0"))?"   ":annualCliSum.getcAmtMean());
			// visibility
			table.setCell(12, annualAvgRow, 2, 1, (annualCliSum.getVisbMean().equals("0.0"))?"   ":annualCliSum.getVisbMean());
			// precipitation
			table.setCell(14, annualAvgRow, 2, 1, annualCliSum.getPrecp());	
			table.setCell(16, annualAvgRow, 1, 1, annualCliSum.getPrecp1DayMax());	
			table.setCell(17, annualAvgRow, 1, 1, statisticalData.MaxTime(precp1DayMaxTime));
			// precipitation hours
			table.setCell(18, annualAvgRow, 2, 1, (annualCliSum.getPrecpHourSum().equals("0.0"))?"   ":annualCliSum.getPrecpHourSum());	
			// precipitation days
			table.setCell(20, annualAvgRow, 1, 1, annualCliSum.getPrecpDays_01mm());
			table.setCell(21, annualAvgRow, 1, 1, annualCliSum.getPrecpDays_1mm());	
			table.setCell(22, annualAvgRow, 1, 1, annualCliSum.getPrecpDays_10mm());	
		}
		table.setCell(23, annualAvgRow, 1, 1, "年 " + year);	
		return annualAvgRow;
	}
	
	private int build3rdTable(CDMLTable table,
				  int posX, int posY,
				  int cellRowStartIndex,
				  List<CliSum> monthDataList,
				  List<CliSum> hourAvgList,
                  List<CliSum> dayAvgList,
                  List<CliSum> weatherConditionList,
                  List<CliSum> TxMinAbs_LE0List,
                  List<CliSum> TxMinAbs_LE10List,
				  List<CliSum> TxMinAbs_GE20List,
				  List<CliSum> TxMinAbs_GE25List,
				  List<CliSum> TxMinAbs_GE30List,
				  List<CliSum> TxMinAbs_GE35List,
				  List<CliSum> precpDayList,
				  List<CliSum> hazeDayList,
				  List<CliSum> hailDayList,
				  List<CliSum> dewDayList,
				  List<CliSum> snowDateList,
				  List<CliSum> frostDateList
                  ){
		
		TextBox cellStyle = table.getStyle();
		cellStyle.setMargin(
		TableCellHorizontalMargin, // left
		TableCellHorizontalMargin, // right
		TableCellVerticalMargin,   // top
		TableCellVerticalMargin    // bottom
		);
		cellStyle.setFontStyle("kaiu.ttf");
		cellStyle.setFontSize(TableCellTextSize);
		
		// set table header row's fontsize
		for (int row=0; row<3; row++) {
			for (int col=0; col<24; col++) {
				table.getCell(col, cellRowStartIndex+row).setFontSize(TableHeaderRowCellTextSize);
				if (row==1 && (col >= 8 && col<=20))
					table.getCell(col, cellRowStartIndex+row)
						 .setMargin(TableCellHorizontalMargin, TableCellHorizontalMargin,
							 	TableCellVerticalMargin, TableCellVerticalMargin);
			}
		}
		
		// month
		table.setCell(0, cellRowStartIndex+0, 1, 3, "月 份\nMonth");
		// evaporation
		table.setCell(1, cellRowStartIndex+0, 1, 3, "蒸發量\nEvapora-\ntion\nmm");
		// weather condition
		// - temperature min
		table.setCell(2, cellRowStartIndex+0, 15, 1, "天氣現象（日數）Weather Condition (days)");
		table.setCell(2, cellRowStartIndex+1, 3, 1, "最低氣溫Min. Temp. ℃");
		table.setCell(2, cellRowStartIndex+2, 1, 1, "≦ 0°");
		table.setCell(3, cellRowStartIndex+2, 1, 1, "≦ 10°");
		table.setCell(4, cellRowStartIndex+2, 1, 1, "≧ 20°");
		// - temperature max
		table.setCell(5, cellRowStartIndex+1, 3, 1, "最高氣溫Max. Temp. ℃");
		table.setCell(5, cellRowStartIndex+2, 1, 1, "≧ 25°");
		table.setCell(6, cellRowStartIndex+2, 1, 1, "≧ 30°");
		table.setCell(7, cellRowStartIndex+2, 1, 1, "≧ 35°");
		// 
		table.setCell(8, cellRowStartIndex+1, 1, 2, "雨\nRain\n");
		table.setCell(9, cellRowStartIndex+1, 1, 2, "雪\nSnow\n");
		table.setCell(10, cellRowStartIndex+1, 1, 2, "霧\nFog\n");
		table.setCell(11, cellRowStartIndex+1, 1, 2, "霾\nHaze\n");
		table.setCell(12, cellRowStartIndex+1, 1, 2, "雷暴\nThunder\nstorm");
		table.setCell(13, cellRowStartIndex+1, 1, 2, "雹\nHail\n");
		table.setCell(14, cellRowStartIndex+1, 1, 2, "強風\nwind≧\n10m/s");
		table.setCell(15, cellRowStartIndex+1, 1, 2, "露\nDew\n");
		table.setCell(16, cellRowStartIndex+1, 1, 2, "霜\nFrost\n");
		// sky condition
		table.setCell(17, cellRowStartIndex+0, 4, 1, "天空狀況（日數）Sky (days)\n               Condition");
		table.setCell(17, cellRowStartIndex+1, 1, 2, "碧\n＜1\n");
		table.setCell(18, cellRowStartIndex+1, 1, 2, "疏\n1.0－5.9\n ");
		table.setCell(19, cellRowStartIndex+1, 1, 2, "烈\n6.0－9.0\n ");
		table.setCell(20, cellRowStartIndex+1, 1, 2, "密\n＞9\n");
		// snow date
		table.setCell(21, cellRowStartIndex+0, 1, 1, "初終雪日\nSnow Date");
		table.getCell(21, cellRowStartIndex+1).setFontSize(12.3);
		table.setCell(21, cellRowStartIndex+1, 1, 2, "從－到\nfrom - to\n日     日");
		// frost date
		table.setCell(22, cellRowStartIndex+0, 1, 1, "初終霜日\nFrost Date");
		table.getCell(22, cellRowStartIndex+1).setFontSize(12.3);
		table.setCell(22, cellRowStartIndex+1, 1, 2, "從－到\nfrom - to\n日     日");
		// month
		table.setCell(23, cellRowStartIndex+0, 1, 3, "月 份\nMonth");
		
		int count = 0;
		int rowRange = (monthDataList.size() < month)? monthDataList.size() : month;// cancel station=6; general=7
        int countRange = (monthDataList.size() < month)? monthDataList.size() : month;
        CliSum annualCliSum  = new CliSum();
        Float evapA = 0.0f;
        Float txMinAbs_LE0 = 0.0f;
        Float txMinAbs_LE10 = 0.0f;
        Float txMinAbs_GE20 = 0.0f;
        Float txMaxAbs_GE25 = 0.0f;
        Float txMaxAbs_GE30 = 0.0f;
        Float txMaxAbs_GE35 = 0.0f;
        Float precpDay = 0.0f;
        Float snowDay = 0.0f;
        Float snow_flag = 0.0f;
        Float fogDay = 0.0f;
        Float hazeDay = 0.0f;
        Float thunderstormDay = 0.0f;
        Float hailDay = 0.0f;
        Float wsMaxGE10Day = 0.0f;
        Float dewDay = 0.0f;
        Float frostDay = 0.0f;
        Float frost_flag = 0.0f;
        Float clearSkyDay = 0.0f;
        Float scatteredSkyDay = 0.0f;
        Float brokenSkyDay = 0.0f;
        Float overcastSkyDay = 0.0f;
        
		// Put the values to the table.
		final int offsetY = 3;
		for (int row=0; row<12; row++) {
			// month
			table.setCell(0, cellRowStartIndex+row+offsetY, 1, 1, Timeline[row]);			
			for(int r=0; r<rowRange; r++) {  
				//control sum process
				if(count<countRange) {
					// condition: cancel station 
					// evaporation
					table.setCell(1, cellRowStartIndex+r+offsetY, 1, 1, weatherConditionList.get(r).getEvapA());  // 蒸發量
					// weather condition
					// - temperature min
					table.setCell(2, cellRowStartIndex+r+offsetY, 1, 1, TxMinAbs_LE0List.get(r).getTxMinAbs_LE0());  // 天氣現象 >> 最低氣溫 : <=0
					table.setCell(3, cellRowStartIndex+r+offsetY, 1, 1, TxMinAbs_LE10List.get(r).getTxMinAbs_LE10());  // 天氣現象 >> 最低氣溫 : <=10
					table.setCell(4, cellRowStartIndex+r+offsetY, 1, 1, TxMinAbs_GE20List.get(r).getTxMinAbs_GE20());  // 天氣現象 >> 最低氣溫 : >=20
					// - temperature max
					table.setCell(5, cellRowStartIndex+r+offsetY, 1, 1, TxMinAbs_GE25List.get(r).getTxMaxAbs_GE25());  // 天氣現象 >> 最高氣溫 : >=25
					table.setCell(6, cellRowStartIndex+r+offsetY, 1, 1, TxMinAbs_GE30List.get(r).getTxMaxAbs_GE30());  // 天氣現象 >> 最高氣溫 : >= 30
					table.setCell(7, cellRowStartIndex+r+offsetY, 1, 1, TxMinAbs_GE35List.get(r).getTxMaxAbs_GE35());  // 天氣現象 >> 最高氣溫 : >=35
					table.setCell(8, cellRowStartIndex+r+offsetY, 1, 1, precpDayList.get(r).getPrecpDay());  // 天氣現象 >> 雨
					table.setCell(9, cellRowStartIndex+r+offsetY, 1, 1, weatherConditionList.get(r).getSnowDay());  // 天氣現象 >> 雪
					table.setCell(10, cellRowStartIndex+r+offsetY, 1, 1, weatherConditionList.get(r).getFogDay()); // 天氣現象 >> 霧
					table.setCell(11, cellRowStartIndex+r+offsetY, 1, 1, hazeDayList.get(r).getStatO2()); // 天氣現象 >> 霾
					table.setCell(12, cellRowStartIndex+r+offsetY, 1, 1, nullToEmptyValue(weatherConditionList.get(r).getThunderstormDay())); // 天氣現象 >> 雷暴
					table.setCell(13, cellRowStartIndex+r+offsetY, 1, 1, hailDayList.get(r).getStatF3()); // 天氣現象 >> 雹
					table.setCell(14, cellRowStartIndex+r+offsetY, 1, 1, weatherConditionList.get(r).getWsMaxGE10Day()); // 天氣現象 >> 強風
					table.setCell(15, cellRowStartIndex+r+offsetY, 1, 1, dewDayList.get(r).getStatP2()); // 天氣現象 >> 露
					table.setCell(16, cellRowStartIndex+r+offsetY, 1, 1, weatherConditionList.get(r).getFrostDay()); // 天氣現象 >> 霜
					// sky condition
					table.setCell(17, cellRowStartIndex+r+offsetY, 1, 1, weatherConditionList.get(r).getClearSkyDay()); // 天空狀況 >> 碧
					table.setCell(18, cellRowStartIndex+r+offsetY, 1, 1, weatherConditionList.get(r).getScatteredSkyDay()); // 天空狀況 >> 疏
					table.setCell(19, cellRowStartIndex+r+offsetY, 1, 1, weatherConditionList.get(r).getBrokenSkyDay()); // 天空狀況 >> 裂
					table.setCell(20, cellRowStartIndex+r+offsetY, 1, 1, weatherConditionList.get(r).getOvercastSkyDay()); // 天空狀況 >> 密
					// snow date
					table.setCell(21, cellRowStartIndex+r+offsetY, 1, 1, getDayOfMonth(snowDateList.get(r).getStatF1_MinTime()) +
							"   " + getDayOfMonth(snowDateList.get(r).getStatF1_MaxTime())); // 初終雪日 >> from-to
					// frost date
					table.setCell(22, cellRowStartIndex+r+offsetY, 1, 1, getDayOfMonth(frostDateList.get(r).getStatF2_MinTime()) +
							"   " + getDayOfMonth(frostDateList.get(r).getStatF2_MaxTime())); // 初終雪日 >> from-to
				///////////////////////////////////////
					evapA += statisticalData.CountDaySum(weatherConditionList.get(r).getEvapA());
					txMinAbs_LE0 += statisticalData.Sum(TxMinAbs_LE0List.get(r).getTxMinAbs_LE0());
					txMinAbs_LE10 += statisticalData.Sum(TxMinAbs_LE10List.get(r).getTxMinAbs_LE10());
					txMinAbs_GE20 += statisticalData.Sum(TxMinAbs_GE20List.get(r).getTxMinAbs_GE20());
					txMaxAbs_GE25 += statisticalData.Sum(TxMinAbs_GE25List.get(r).getTxMaxAbs_GE25());
					txMaxAbs_GE30 += statisticalData.Sum(TxMinAbs_GE30List.get(r).getTxMaxAbs_GE30());
					txMaxAbs_GE35 += statisticalData.Sum(TxMinAbs_GE35List.get(r).getTxMaxAbs_GE35());
					precpDay += statisticalData.Sum(precpDayList.get(r).getPrecpDay());
					//day count with empty value
					if(statisticalData.CountDaySum(weatherConditionList.get(r).getSnowDay())<0) {
						snow_flag += statisticalData.CountDaySum(weatherConditionList.get(r).getSnowDay());
					}else {
						snowDay += statisticalData.CountDaySum(weatherConditionList.get(r).getSnowDay());
					}
					
					fogDay += statisticalData.CountDaySum(weatherConditionList.get(r).getFogDay());
					hazeDay += statisticalData.CountDaySum(hazeDayList.get(r).getStatO2());
					thunderstormDay += statisticalData.CountDaySum(nullToEmptyValue(weatherConditionList.get(r).getThunderstormDay()));
					hailDay += statisticalData.CountDaySum(hailDayList.get(r).getStatF3());
					wsMaxGE10Day += statisticalData.Sum(weatherConditionList.get(r).getWsMaxGE10Day());
					dewDay += statisticalData.CountDaySum(dewDayList.get(r).getStatP2());
					if(statisticalData.CountDaySum(weatherConditionList.get(r).getFrostDay())<0) {
						frost_flag += statisticalData.CountDaySum(weatherConditionList.get(r).getFrostDay());
					}else {
						frostDay += statisticalData.CountDaySum(weatherConditionList.get(r).getFrostDay());
					}
					clearSkyDay += statisticalData.CountDaySum(weatherConditionList.get(r).getClearSkyDay());
					scatteredSkyDay += statisticalData.CountDaySum(weatherConditionList.get(r).getScatteredSkyDay());
					brokenSkyDay += statisticalData.CountDaySum(weatherConditionList.get(r).getBrokenSkyDay());
					overcastSkyDay += statisticalData.CountDaySum(weatherConditionList.get(r).getOvercastSkyDay());
				}
				count++;
				annualCliSum.setEvapA(StringToBigdecimal(String.valueOf(evapA)));
				annualCliSum.setTxMinAbs_LE0(StringToBigdecimal2(String.valueOf(txMinAbs_LE0)));
				annualCliSum.setTxMinAbs_LE10(StringToBigdecimal2(String.valueOf(txMinAbs_LE10)));
				annualCliSum.setTxMinAbs_GE20(StringToBigdecimal2(String.valueOf(txMinAbs_GE20)));
				annualCliSum.setTxMaxAbs_GE25(StringToBigdecimal2(String.valueOf(txMaxAbs_GE25)));
				annualCliSum.setTxMaxAbs_GE30(StringToBigdecimal2(String.valueOf(txMaxAbs_GE30)));
				annualCliSum.setTxMaxAbs_GE35(StringToBigdecimal2(String.valueOf(txMaxAbs_GE35)));
				annualCliSum.setPrecpDay(StringToBigdecimal2(String.valueOf(precpDay)));
				annualCliSum.setSnowDay(StringToBigdecimal2(String.valueOf(snowDay)));
				annualCliSum.setFogDay(StringToBigdecimal2(String.valueOf(fogDay)));
				annualCliSum.setStatO2(StringToBigdecimal2(String.valueOf(hazeDay)));
				annualCliSum.setThunderstormDay(StringToBigdecimal2(String.valueOf(thunderstormDay)));
				annualCliSum.setStatF3(StringToBigdecimal2(String.valueOf(hailDay)));
				annualCliSum.setWsMaxGE10Day(StringToBigdecimal2(String.valueOf(wsMaxGE10Day)));
				annualCliSum.setStatP2(StringToBigdecimal2(String.valueOf(dewDay)));
				annualCliSum.setFrostDay(StringToBigdecimal2(String.valueOf(frostDay)));
				annualCliSum.setClearSkyDay(StringToBigdecimal2(String.valueOf(clearSkyDay)));
				annualCliSum.setScatteredSkyDay(StringToBigdecimal2(String.valueOf(scatteredSkyDay)));
				annualCliSum.setBrokenSkyDay(StringToBigdecimal2(String.valueOf(brokenSkyDay)));
				annualCliSum.setOvercastSkyDay(StringToBigdecimal2(String.valueOf(overcastSkyDay)));
			}
			if (row>=rowRange) {
				// evaporation
				table.setCell(1, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 蒸發量
				// weather condition: - temperature min
				table.setCell(2, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 天氣現象 >> 最低氣溫 : <=0
				table.setCell(3, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 天氣現象 >> 最低氣溫 : <=10
				table.setCell(4, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 天氣現象 >> 最低氣溫 : >=20
				// weather condition: - temperature max
				table.setCell(5, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 天氣現象 >> 最高氣溫 : >=25
				table.setCell(6, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 天氣現象 >> 最高氣溫 : >= 30
				table.setCell(7, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 天氣現象 >> 最高氣溫 : >=35
				table.setCell(8, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 天氣現象 >> 雨
				table.setCell(9, cellRowStartIndex+row+offsetY, 1, 1, "    ");  // 天氣現象 >> 雪
				table.setCell(10, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天氣現象 >> 霧
				table.setCell(11, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天氣現象 >> 霾
				table.setCell(12, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天氣現象 >> 雷暴
				table.setCell(13, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天氣現象 >> 雹
				table.setCell(14, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天氣現象 >> 強風
				table.setCell(15, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天氣現象 >> 露
				table.setCell(16, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天氣現象 >> 霜
				// sky condition
				table.setCell(17, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天空狀況 >> 碧
				table.setCell(18, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天空狀況 >> 疏
				table.setCell(19, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天空狀況 >> 裂
				table.setCell(20, cellRowStartIndex+row+offsetY, 1, 1, "    "); // 天空狀況 >> 密
				// snow date
				table.setCell(21, cellRowStartIndex+row+offsetY, 1, 1,  "" + "   " + ""); // 初終雪日 >> from-to
				// frost date
				table.setCell(22, cellRowStartIndex+row+offsetY, 1, 1,  "" + "   " + ""); // 初終雪日 >> from-to
			}
			// month
			table.setCell(23, cellRowStartIndex+row+offsetY, 1, 1, (row+1) + " " + Timeline[row]);
		}
		
		// The last row: average
		final int annualAvgRow = cellRowStartIndex+12+offsetY;
		table.setCell(0, annualAvgRow, 1, 1, String.valueOf(year));
		{
			// evaporation
			table.setCell(1, annualAvgRow, 1, 1, (annualCliSum.getEvapA().equals("0"))?
					(((monthDataList.get(0).getManObsNum().equals("0"))?"   ":"0"))
					:(Float.parseFloat(annualCliSum.getEvapA())<0)?"   ":annualCliSum.getEvapA());
			// weather condition
			// - temperature min
			table.setCell(2, annualAvgRow, 1, 1, annualCliSum.getTxMinAbs_LE0());	
			table.setCell(3, annualAvgRow, 1, 1, annualCliSum.getTxMinAbs_LE10());	
			table.setCell(4, annualAvgRow, 1, 1, annualCliSum.getTxMinAbs_GE20());	
			// - temperature max
			table.setCell(5, annualAvgRow, 1, 1, annualCliSum.getTxMaxAbs_GE25());	
			table.setCell(6, annualAvgRow, 1, 1, annualCliSum.getTxMaxAbs_GE30());	
			table.setCell(7, annualAvgRow, 1, 1, annualCliSum.getTxMaxAbs_GE35());
			table.setCell(8, annualAvgRow, 1, 1, annualCliSum.getPrecpDay());	
			table.setCell(9, annualAvgRow, 1, 1, (snow_flag==-(rowRange))?"   ":annualCliSum.getSnowDay());
			table.setCell(10, annualAvgRow, 1, 1, (annualCliSum.getFogDay().equals("0"))?
					((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
					:(Float.parseFloat(annualCliSum.getFogDay())<0?"   ":annualCliSum.getFogDay()));	
			table.setCell(11, annualAvgRow, 1, 1, (annualCliSum.getStatO2().equals("0"))?
					((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
					:(Float.parseFloat(annualCliSum.getStatO2())<0?"   ":annualCliSum.getStatO2()));
			table.setCell(12, annualAvgRow, 1, 1,  (annualCliSum.getThunderstormDay().equals("0"))?
					((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
					:annualCliSum.getThunderstormDay());
			table.setCell(13, annualAvgRow, 1, 1,  (annualCliSum.getStatF3().equals("0"))?
					((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
					:(Float.parseFloat(annualCliSum.getStatF3())<0)?"   ":annualCliSum.getStatF3());
			table.setCell(14, annualAvgRow, 1, 1, annualCliSum.getWsMaxGE10Day());	
			table.setCell(15, annualAvgRow, 1, 1,  (annualCliSum.getStatP2().equals("0"))?
					((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
					:(Float.parseFloat(annualCliSum.getStatP2())<0)?"   ":annualCliSum.getStatP2());
			table.setCell(16, annualAvgRow, 1, 1, (frost_flag==-(rowRange))?"   ":annualCliSum.getFrostDay());
			// sky condition
			table.setCell(17, annualAvgRow, 1, 1, (annualCliSum.getClearSkyDay().equals("0"))?
					((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
					:(Float.parseFloat(annualCliSum.getClearSkyDay())<0)?"   ":annualCliSum.getClearSkyDay());
			table.setCell(18, annualAvgRow, 1, 1, (annualCliSum.getScatteredSkyDay().equals("0"))?
					((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
					:(Float.parseFloat(annualCliSum.getScatteredSkyDay())<0)?"   ":annualCliSum.getScatteredSkyDay());
			table.setCell(19, annualAvgRow, 1, 1, (annualCliSum.getBrokenSkyDay().equals("0"))?
					((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
					:(Float.parseFloat(annualCliSum.getBrokenSkyDay())<0)?"   ":annualCliSum.getBrokenSkyDay());
			table.setCell(20, annualAvgRow, 1, 1, (annualCliSum.getOvercastSkyDay().equals("0"))?
					((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
					:(Float.parseFloat(annualCliSum.getOvercastSkyDay())<0)?"   ":annualCliSum.getOvercastSkyDay());
			// snow date (from, to)
			table.setCell(21, annualAvgRow, 1, 1, "   ");
			// frost date (from, to)
			table.setCell(22, annualAvgRow, 1, 1, "   ");	
		}
		table.setCell(23, annualAvgRow, 1, 1, "年 " + year);
		
		return annualAvgRow;
	}
   
	
    private String StringToBigdecimal(String value) {
    	String data = (value==null) ? 
    			"    ": new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    	return data;
    }
    
    private String StringToBigdecimal2(String value) {
    	String data = (value==null)? 
    			"    ":new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    	return data;
    }
    
    private String getDayOfMonth(String value) {
    	String data = (value==null)? 
    			"" : value.substring(8, 10);
    	return data;
    }
    private String nullToEmptyValue(String value) {
    	String data = (value==null)? 
    			"" : value;
    	return data;
    }
    
}
