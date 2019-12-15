package cwb.cmt.surface.service;

import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.surface.model.AuxCliSum;


@Service("createTableImageForAuxCliSum")
public class CreateTableImageForAuxCliSum extends CreateTableImage {
	
    // Position, margins
    private final int SectionSubTitleMarginTop = 40;
    private final int PageMainTitleMarginTop = 79;
    private final int PageSubTitleMarginTop = 121;
    private final int FooterTextMarginBottom = 172;
    
    private final int PaddingBetweenTopTitleTextAndTable = 5;
    private final int PaddingBetweenFooterTextAndTable = 8;
    private final int TableMarginTop = 200;
    private final int Table2_MarginTop_From_Table1 = 64;
    
    // Text size
    private final int SectionMaintitleTextSize = 27;
    private final int SectionSubTitleTextSize = 20;
    private final int PageMainTitleTextSize = 17;
    private final int PageSubTitleTextSize = 15;
    private final int PageHeaderFooterTextSize = 13;
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
    

//createChart    
    protected BaseChart createChart(Object... objects) {
    	
		boolean firstPage = (boolean)objects[0];
		boolean lastPage = (boolean)objects[1];
		@SuppressWarnings("unchecked")
		String TableCNum = (String)objects[2];
		@SuppressWarnings("unchecked")
		String TableENum = (String)objects[3];
		@SuppressWarnings("unchecked")
		String StnCName = (String)objects[4];
		@SuppressWarnings("unchecked")
		String StnEName = (String)objects[5];
		@SuppressWarnings("unchecked")
		List<AuxCliSum> radList= (List<AuxCliSum>)objects[6];
		String Stno = (String)objects[7];
		@SuppressWarnings("unchecked")
		List<List<String>> radStatistic = (List<List<String>>)objects[8];
		@SuppressWarnings("unchecked")
		List<List<String>> sunStatistic = (List<List<String>>)objects[9];
		//ChartDirector License
		Chart.setLicenseCode("DEVP-2LPQ-D6YH-UTLK-EBC0-3E26");
		// create a XYChart object
        XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
        
        // add a title to the chart
        if (firstPage) {
        	System.out.println("The first page!!!!");
        	chart.addTitle("1.4   輔助氣象綱要表" + "  " + "Auxiliary Table for Climatological Summeries",
        			"kaiu.ttf", SectionMaintitleTextSize);
            // Section subtitle
            TextBox section_subtitle = chart.addText(0, SectionSubTitleMarginTop + 3,
            		"1.4.1 全天空日射量及日照時數" + "  " + "Global Solar Radiation and Duration of Sunshine");
            section_subtitle.setFontStyle("kaiu.ttf");
            section_subtitle.setFontSize(SectionSubTitleTextSize);
            section_subtitle.setPos((CHART_WIDTH - section_subtitle.getWidth())/2, section_subtitle.getTopY());
        }
        
        // Page main title
        TextBox page_main_title = chart.addText(
                0,
                (firstPage)? PageMainTitleMarginTop : PageMainTitleMarginTop-18,
                TableCNum+StnCName.replace("", "  ").trim()	
        );
        page_main_title.setFontStyle("kaiu.ttf");
        page_main_title.setFontSize(PageMainTitleTextSize + 5);
        page_main_title.setPos((CHART_WIDTH - page_main_title.getWidth())/2, page_main_title.getTopY());

        // Page subtitle
        TextBox page_subtitle = chart.addText(
                0,
                (firstPage)? PageSubTitleMarginTop : PageSubTitleMarginTop-18,
                TableENum+StnEName
//                "pageSubtitle"
        );
        page_subtitle.setFontStyle("kaiu.ttf");
        page_subtitle.setFontSize(PageSubTitleTextSize + 2);
        page_subtitle.setPos((CHART_WIDTH - page_subtitle.getWidth())/2, page_subtitle.getTopY());
        
        // create Global Solar Radiation table
        CDMLTable table1 = createTable(
        		chart, 
        		0, 
        		TableMarginTop, 
        		radList, 
        		Stno, 
        		"rad",
        		radStatistic
        		);
        
        // compute adjusted margin left
        final int marginLeft = (chart.getWidth() - table1.getWidth()) / 2;

        // adjust table, align center
        table1.setPos(marginLeft, TableMarginTop);

        // create table1's title
        createTableTopTitles(
        		chart, 
        		table1,
        		marginLeft,
        		TableMarginTop,
	            "全天空日射量 Global Solar Radiation",
	            "年 Year：" + year,
	            "單位：每平方公尺百萬焦耳\n" + "UNIT：MJ/㎡"
        );
        
        // create sunshine table
        CDMLTable table2 = createTable(
        		chart, 
        		marginLeft,
        		TableMarginTop + table1.getHeight() + Table2_MarginTop_From_Table1,
        		radList,
        		Stno,
        		"sunshine",
        		sunStatistic
        );
        
        // create table2's title text
        createTableTopTitles(chart, table2, marginLeft,
            TableMarginTop + table1.getHeight() + Table2_MarginTop_From_Table1,
            "日照時數 Duration of Sunshine",
            "年 Year：" + year,
            "單位：小時\n" + "UNIT：Hour"
        );
        
        // Page footer note left
        TextBox footer_left = chart.addText(
                marginLeft,
                CHART_HEIGHT - FooterTextMarginBottom + PaddingBetweenFooterTextAndTable,
                ""
        );
        footer_left.setFontStyle("kaiu.ttf");
        footer_left.setFontSize(PageHeaderFooterTextSize);
        
        // Page footer note right
        TextBox footer_right = chart.addText(
                marginLeft+ table2.getWidth(),
                CHART_HEIGHT - FooterTextMarginBottom + PaddingBetweenFooterTextAndTable,
                "特殊註記請參考第ii頁說明"
        );
        footer_right.setFontStyle("kaiu.ttf");
        footer_right.setFontSize(PageHeaderFooterTextSize);
        footer_right.setPos(footer_right.getLeftX() - footer_right.getWidth() , footer_right.getTopY());
        return chart;
    }
   

	private void createTableTopTitles(XYChart chart,
            CDMLTable table,
            int posX, int posY,
            String topTitleCenter,
            String topTitleLeft,
            String topTitleRight) {
			// Get table's dimensions
			final int table_width = table.getWidth();
			
			// Top title center
			TextBox top_title_center = chart.addText(
			posX,
			posY - PaddingBetweenTopTitleTextAndTable+3,
			topTitleCenter
			);
			top_title_center.setFontStyle("kaiu.ttf");
			top_title_center.setFontSize(PageHeaderFooterTextSize + 5);
			top_title_center.setPos(
			posX + (table_width-top_title_center.getWidth())/2,
			top_title_center.getTopY() - top_title_center.getHeight()
			);
			
			// Top title left
			TextBox top_title_left = chart.addText(
			posX,
			posY - PaddingBetweenTopTitleTextAndTable + 3,
			topTitleLeft
			);
			top_title_left.setFontStyle("kaiu.ttf");
			top_title_left.setFontSize(PageHeaderFooterTextSize + 2);
			top_title_left.setPos(
			top_title_left.getLeftX(),
			top_title_left.getTopY() - top_title_left.getHeight()
			);
			
			// Top title right
			TextBox top_title_right = chart.addText(
			posX,
			posY - PaddingBetweenTopTitleTextAndTable + 3,
			topTitleRight
			);
			top_title_right.setFontStyle("kaiu.ttf");
			top_title_right.setFontSize(PageHeaderFooterTextSize+2);
			top_title_right.setPos(
			posX + table_width - top_title_right.getWidth(),
			top_title_right.getTopY() - top_title_right.getHeight()
			);
}
    
/////////////////////create radiation and sunshine table//////////////////////////
	//createTable:radiation and sunshine
	private CDMLTable createTable(XYChart chart, int posX, int posY, 
			List<AuxCliSum> AuxCliSumList, String Stno, String listPattern,
			List<List<String>> statisticList) {
		
        final int rowCount = AuxCliSumList.size();
        final int[] daysOfMonth = new int[] {
        	31, (!calendar.isLeapYear(year))? 28:29, 31, 30, 31, 30,
        	31, 31, 30, 31, 30, 31
        };
        
        //input table content variable
        String data;
        int monthOfYear;
        int countOfRowcount=0;
		
		// Create a CDMLTable
        CDMLTable table = chart.addTable(
            posX, posY, 0,
            14,
            34
        );

        // Set the default left/right margins and top/bottom margins
        TextBox cellStyle = table.getStyle();
        cellStyle.setMargin(
                TableCellHorizontalMargin, // left
                TableCellHorizontalMargin, // right
                TableCellVerticalMargin,   // top
                TableCellVerticalMargin    // bottom
        );
        cellStyle.setFontStyle("kaiu.ttf");
        cellStyle.setFontSize(TableCellTextSize+1);
        
        // Fill up table's headers
        table.setCell(0, 0, 1, 1, "日\nDate");
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
        table.setCell(13, 0, 1, 1, "日\nDate");        

        
        //config file: cmt.month = 12
        for (int col = 0; col<month; col++) {
	        // table content
	        for (int row=0; row<31+2; row++) {
	        	// primary column
	            table.getCell(col+1, row+1).setAlignment(TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_VERTICAL);
	            table.getCell(col+1, row+1).setMargin(
	                    TableCellHorizontalMargin, // left
	                    TableCellHorizontalMargin, // right
	                    TableCellVerticalMargin,   // top
	                    TableCellVerticalMargin    // bottom
	            );
		        table.getCell(0, row+1).setAlignment(TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_VERTICAL);
		        table.setCell(0, row+1, 1, 1, ((row+1 <= Row_STATISTICAL_DATA_START)
		                                       // 1~31日
		                                       ? String.valueOf(row+1)
		                                       // "月總計", "日平均"
		                                       : Statistic[row - Row_STATISTICAL_DATA_START]
		        ));
		        
                if (row < Row_STATISTICAL_DATA_START) {
                	if (countOfRowcount<rowCount) {
                		monthOfYear = AuxCliSumList.get(countOfRowcount).getObsTime().getMonthValue();
                		//setting value: radiation data
			            if (Stno.equals(AuxCliSumList.get(countOfRowcount).getStno()) 
			            		&& listPattern=="rad" && monthOfYear==col+1){
			            	data = AuxCliSumList.get(countOfRowcount).getGlobalRad();
			            	table.setCell(col+1, row+1, 1, 1, String.format("%5s", data));
		                    countOfRowcount+=1;
			            }
			            
			            //setting value: sunshine data
			            else if (Stno.equals(AuxCliSumList.get(countOfRowcount).getStno()) 
			            		&& listPattern=="sunshine" && monthOfYear==col+1){
			            	data = AuxCliSumList.get(countOfRowcount).getSunshine();
			            	table.setCell(col+1, row+1, 1, 1,  String.format("%5s", data));
		                    countOfRowcount+=1;
			            }
                	}
                }
                
	            // Statistical data sum
                else if (row == Row_DATA_SUM) {
                	table.getCell(col+1, row+1).setAlignment(TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_VERTICAL);
                    table.setCell(col+1, row+1, 1, 1, statisticList.get(0).get(col));
                }
                // Statistical data mean
                else if (row == Row_DATA_MEAN) {
                	table.getCell(col+1, row+1).setAlignment(TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_VERTICAL);
                    table.setCell(col+1, row+1, 1, 1, statisticList.get(1).get(col));
                }
                
		    // The last column
	        table.setCell(13, row+1, 1, 1, ((row+1 <= Row_STATISTICAL_DATA_START)
	                                        // 1~31日
	                                        ? String.valueOf(row+1)
	                                        // "月總計", "日平均"
	                                        : Statistic[row - Row_STATISTICAL_DATA_START]));
	       }
        }
        return table;
	}

	
	@Override
	public void createTableImage(Object... args) {
		super.createTableImage(args);
	}
}
