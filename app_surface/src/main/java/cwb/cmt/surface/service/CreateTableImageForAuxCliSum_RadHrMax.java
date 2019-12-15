package cwb.cmt.surface.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Resource;

import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.utils.SpecialValue;


@Service("createTableImageForAuxCliSum_RadHrMax")
public class CreateTableImageForAuxCliSum_RadHrMax extends CreateTableImage {
	// Width, Height
    private final int ChartMarginTop = 45;
    private final int ChartWidth = 2100;
    private final int ChartHeight = 2970 - ChartMarginTop;
    // Position, margins
    private final int SectionSubTitleMarginTop = 40;
    private final int PageMainTitleMarginTop = 79;
    private final int PageSubTitleMarginTop = 121;
    private final int FooterTextMarginBottom = 172;
    private final int PageNumberTextMarginBottom = 66;
    private final int PaddingBetweenTopTitleTextAndTable = 5;
    private final int PaddingBetweenFooterTextAndTable = 8;
    private final int TableMarginTop = 200;
    private final int Table2_MarginTop_From_Table1 = 64;
    private final int TableMarginTop_LastTwoCharts = 100;
    private final int TableMarginTop_AdjustOffset_LastTwoCharts = 32;
    // Text size
    private final int SectionMaintitleTextSize = 27;
    private final int SectionSubTitleTextSize = 20;
    private final int PageMainTitleTextSize = 17;
    private final int PageSubTitleTextSize = 15;
    private final int PageHeaderFooterTextSize = 13;
    private final int PageNumberTextSize = 15;
    private final int TableCellTextSize = 14;
    private final int TableCellHorizontalMargin = 17;
    private final int TableCellVerticalMargin = 8;
    // Row
    private static final int Row_DATA_SUM = 31;
    private static final int Row_DATA_MEAN = 32;
    private static final int Row_STATISTICAL_DATA_START = Row_DATA_SUM;
    // Data
    private static final String[] Statistic;
    static {
        Statistic = new String[] {"月總計", "日平均"};
    }
    //Time: year
    @Resource(name="year")
    protected int year;
    
    //Time: month
    @Resource(name="month")
    protected int month;
    private static final GregorianCalendar calendar = new GregorianCalendar();

    
  //Bookmark: createChartSummery_GlobalRadHrMax()
    public BaseChart createChart(Object... objects) {
		boolean firstPage = (boolean)objects[0];
		boolean lastPage = (boolean)objects[1];
		
		@SuppressWarnings("unchecked")
		Map<String, List<AuxCliSum>> radmaxList = (Map<String, List<AuxCliSum>>) objects[2];
		
		@SuppressWarnings("unchecked")
		List<Station> stn = (List<Station>) objects[3];   
		
		@SuppressWarnings("unchecked")
		Map<String, List<String>> radHrMaxList = (Map<String, List<String>>)objects[4];
		
        Chart.setLicenseCode("DEVP-2LPQ-D6YH-UTLK-EBC0-3E26");    

        // Create a XYChart object of size 600 x 400 pixels
        XYChart chart = new XYChart(
                ChartWidth,
                ChartHeight
        );
        
        // Add a title to the chart
        if (firstPage) {
	        // Page main title
	        TextBox page_main_title = chart.addText(
	                0,
	                PageMainTitleMarginTop/2 - 9 + 4,
	                "1.4.3 全天空一小時最大日射量" + "  " + "Global Solar Radi Max."
	        );
	        page_main_title.setFontStyle("kaiu.ttf");
	        page_main_title.setFontSize(PageMainTitleTextSize + 5);
	        page_main_title.setPos(
	        		(ChartWidth - page_main_title.getWidth())/2,
	        		page_main_title.getTopY()
    		);
        }
        else {
            // Add a title to the chart
            chart.addTitle(
            		"1.4.3 全天空一小時最大日射量" + "  " + "Global Solar Radi Max.",
                    "kaiu.ttf",
                    PageMainTitleTextSize + 4
            );
        }

        CDMLTable table1 = createTable(
                chart,
                37,
                TableMarginTop_LastTwoCharts + TableMarginTop_AdjustOffset_LastTwoCharts,
                "",
                "年 Year：" + year,
                "單位：每平方公尺百萬焦耳\n" + "UNIT：MJ/㎡",
                radmaxList,
                stn,
                radHrMaxList
        );

        // compute adjusted margin left
        final int table_width = table1.getWidth();
        final int marginLeft = (chart.getWidth() - table1.getWidth())/2;

        // Adjust table, align center
        table1.setPos(marginLeft, TableMarginTop_LastTwoCharts + TableMarginTop_AdjustOffset_LastTwoCharts);

        // Page footer note left
        TextBox footer_left = chart.addText(
                marginLeft,
                chart.getHeight() - FooterTextMarginBottom + PaddingBetweenFooterTextAndTable + TableMarginTop_AdjustOffset_LastTwoCharts,
                ""
        );
        footer_left.setAlignment(TABLE_CELL_ALIGNMENT_LEFT_TOP);
        footer_left.setFontStyle("kaiu.ttf");
        footer_left.setFontSize(PageHeaderFooterTextSize);
        
        // Page footer note right
        TextBox footer_right = chart.addText(
                marginLeft,
                chart.getHeight() - FooterTextMarginBottom + PaddingBetweenFooterTextAndTable + TableMarginTop_AdjustOffset_LastTwoCharts,
                "特殊註記請參考第ii頁說明"
        );
        footer_right.setFontStyle("kaiu.ttf");
        footer_right.setFontSize(PageHeaderFooterTextSize);
        footer_right.setPos(
        		footer_right.getLeftX() + table_width - footer_right.getWidth(),
        		footer_right.getTopY()
		);
        return chart;
    }
    
	
	private CDMLTable createTable(XYChart chart, int posX, int posY, 
			String topTitleCenter,
			String topTitleLeft,
            String topTitleRight,
            Map<String, List<AuxCliSum>> radmaxList,
            List<Station> stn,
            Map<String, List<String>> radHrMaxMap){
	
		String data;
		int dayOfMonth = 0;
		//station annual data sum
		float[] sum  = new float[stn.size()];
		List<String> radHrMaxList = new ArrayList<>();
		
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.Unknown.getNumber(),
				SpecialValue.CancelStation.getNumber());
		
		
        // Create a CDMLTable
        CDMLTable table = chart.addTable(
                posX, posY, 0,
                radmaxList.size() + 500,
                radmaxList.size() + 500
        );

        // Set default left/right margins and top/bottom margins.
        TextBox cellStyle = table.getStyle();
        cellStyle.setMargin(
                TableCellHorizontalMargin, // left
                TableCellHorizontalMargin, // right
                TableCellVerticalMargin-2, // top
                TableCellVerticalMargin-2  // bottom
        );
        cellStyle.setFontStyle("kaiu.ttf");
        cellStyle.setFontSize(TableCellTextSize+1);

        // Fill up table's headers
        table.getCell(0, 0).setAlignment(CreateTableImage.TABLE_CELL_ALIGNMENT_LEFT_TOP);
        table.setCell(0, 0, 1, 1, "Station No. _Name\n" +
                                  "站號及測站名稱");
		table.setCell(1, 0, 1, 1, "一月\nJan.");
		table.setCell(2, 0, 1, 1, "二月\nFeb.");
		table.setCell(3, 0, 1, 1, "三月\nMar.");
		table.setCell(4, 0, 1, 1, "四月\nApr.");
		table.setCell(5, 0, 1, 1, "五月\nMay");
		table.setCell(6, 0, 1, 1, "六月\nJun.");
		table.setCell(7, 0, 1, 1, "七月\nJul.");
		table.setCell(8, 0, 1, 1, "八月\nAug.");
		table.setCell(9, 0, 1, 1, "九月\nSep.");
		table.setCell(10, 0, 1, 1, "十月\nOct.");
		table.setCell(11, 0, 1, 1, "十一月\nNov.");
		table.setCell(12, 0, 1, 1, "十二月\nDec.");
		table.setCell(13, 0, 1, 1, "全年\n" + "Annual");
        
        // Put values to the table.
        for (int row = 0; row < stn.size(); row++) {
        	radHrMaxList.clear();
            table.getCell(0, row+1).setAlignment(TABLE_CELL_ALIGNMENT_LEFT_TOP);
            table.setCell(0, row+1, 1, 1, stn.get(row).getStno() + " " + stn.get(row).getStnCName());

        	List<AuxCliSum> result = new ArrayList<>();
			for(String key:radmaxList.keySet()) {  //size=31
				for (int c = 0; c < month; c++) {
					if (stn.get(row).getStno().equals(key)) {
						AuxCliSum temp = radmaxList.get(key).get(c);
						result.add(temp);
					}
				}
			}
			
			for(String key:radHrMaxMap.keySet()) {  //size=31
				if (stn.get(row).getStno().equals(key)) {
					radHrMaxList = radHrMaxMap.get(key);
				}
			}
    		int count =0;
            
            // table content
            for (int col = 0; col < month; col++) {
                table.getCell(col+1, row+1).setAlignment(TABLE_CELL_ALIGNMENT_RIGHT_TOP);
                
                if (count<result.size()) {
                	data = result.get(count).getGlobalRadMax();
                	
                	if (specialValueList.contains(data)) {
                		table.getCell(col+1, row+1).setAlignment(TABLE_CELL_ALIGNMENT_RIGHT_TOP);
                		table.setCell(col+1, row+1, 1, 1, "  "+data);
                	}
                	//normal value
                	else {
                		dayOfMonth = result.get(count).getGlobalRadHrMaxTime().getDayOfMonth();
                        table.setCell(col+1, row+1, 1, 1,
                        		"   " + data + "\n" +
                        	    "   " + dayOfMonth);
                	}
            		
                	count++;
                }
                
            }//for col

//            
            // Print the last column 'annual max value'
            table.getCell(13, row+1).setAlignment(TABLE_CELL_ALIGNMENT_RIGHT_TOP);
            table.setCell(13, row+1, 1, 1,
            		"   " + radHrMaxList.get(0) + "\n" +
    				"   " + radHrMaxList.get(1));
            
        }//for row

        // Table's dimensions
        final int table_width = table.getWidth();
        final int marginLeft = (chart.getWidth() - table.getWidth())/2;
        
        // top title left
        TextBox top_title_left = chart.addText(
                marginLeft,
                posY - PaddingBetweenTopTitleTextAndTable,
                topTitleLeft
        );
        top_title_left.setFontStyle("kaiu.ttf");
        top_title_left.setFontSize(PageHeaderFooterTextSize + 2);
        top_title_left.setPos(
        		top_title_left.getLeftX(),
        		top_title_left.getTopY() - top_title_left.getHeight()
		);

        // top title center
        TextBox top_title_center = chart.addText(
                marginLeft + table_width/2,
                posY - PaddingBetweenTopTitleTextAndTable,
                topTitleCenter
        );
        top_title_center.setFontStyle("kaiu.ttf");
        top_title_center.setFontSize(PageHeaderFooterTextSize + 4);
        top_title_center.setPos(
        		top_title_center.getLeftX() - top_title_center.getWidth()/2,
        		top_title_center.getTopY() - top_title_center.getHeight()
		);
        
        // top title right
        TextBox top_title_right = chart.addText(
                marginLeft + table_width,
                posY - PaddingBetweenTopTitleTextAndTable,
                topTitleRight
        );        
        top_title_right.setFontStyle("kaiu.ttf");
        top_title_right.setFontSize(PageHeaderFooterTextSize + 2);
        top_title_right.setPos(
        		top_title_right.getLeftX() - top_title_right.getWidth(),
        		top_title_right.getTopY() - top_title_right.getHeight()
		);
        return table;
		
	}
	
	
	@Override
	public void createTableImage(Object... args) {
		super.createTableImage(args);
	}

}
