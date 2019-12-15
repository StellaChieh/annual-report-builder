package cwb.cmt.surface.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.surface.model.Station;

@Service("createTableImageForStn")
public class CreateTableImageForStn extends CreateTableImage{
	
    private final int PageNumberTextMarginBottom = 103 + 7;
    private final int SectionTitleTextSize = 40;
    private final int PageChineseNoteTextSize = 16;
    private final int PageEnglishNoteTextSize = 15;
    private final int PageNumberTextSize = 17;
    private final int TableCellTextSize = 18;
    private final int TableCellHorizontalMargin = 8;
    private final int TableCellVerticalMargin = 6;
    
	// may reference to summary.createTableImageForStns or surface one
	@Override
	protected BaseChart createChart(Object... objects) {
		
		boolean firstPage = (boolean)objects[0];
		boolean lastPage = (boolean)objects[1];
		@SuppressWarnings("unchecked")
		List<Station> stns = (List<Station>) objects[2];
		
		Chart.setLicenseCode("DEVP-2LPQ-D6YH-UTLK-EBC0-3E26");
        
        // Create a XYChart object of size 600 x 400 pixels
        XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
        if (firstPage) {
	        // Add a title to the chart using 18 pts Times Bold Italic font
	        chart.addTitle("1. 1  中 央 氣 象 局 所 屬 各 氣 象 站 一 覽 表" +
	                 "\n" + "List of Stations of Central Weather Bureau",
	                 "kaiu.ttf", SectionTitleTextSize);
        }

        // create a table
        CDMLTable table = createTable(chart, 80, TABLE_MARGIN_TOP, stns);
        
        final int marginLeft = (chart.getWidth() - table.getWidth()) / 2;
        table.setPos(marginLeft, TABLE_MARGIN_TOP);
        

        if (lastPage) {
        	// note (chinese)
            TextBox note_chinese = chart.addText(
            		marginLeft,
            		TABLE_MARGIN_TOP + table.getHeight() + 10,
    	            "註1:括號內數值是地面自動測報系統感應器之離地高度\n" +
    	            "註2:◆表特殊站。板橋為探空站。七股為雷達站。永康站僅有自動測報系統。\n" +
    	            "註3:淡水站自2006年起無人駐站，僅有自動測報系統\n");
            note_chinese.setFontStyle("kaiu.ttf");
            note_chinese.setFontSize(PageChineseNoteTextSize);
            
            // note (english)
            TextBox note_english = chart.addText(
            		marginLeft,
            		note_chinese.getTopY() + note_chinese.getHeight() - 17,
                    "Note1：The numbers in parentheses show the height of automated surface observation system\'s sensor above ground.\n" +
                    "Note2：\"◆\" denotes special stations. Panchiao is a sounding station. Chigu is rader station.\n"+
                    "              Yungkang is equipped with automated surface observation system only.\n" +
                    "Note3：The Danshuei station has became an unmanned automatic station since February 2006.\n");
            note_english.setFontStyle("kaiu.ttf");
            note_english.setFontSize(PageEnglishNoteTextSize);
        }
		// perform chart producing
		return chart;
	}
	
	
	 private CDMLTable createTable(XYChart chart, int posX, int posY, List<Station> stns) {
		 
        // Create a CDMLTable: http://www.advsofteng.com/doc/cdcfdoc/BaseChart.addTable.htm
		//  X-position, y-position, text alignment, rows, columns 
        CDMLTable table = chart.addTable(
    		95, 160, 0,
    		11,
    		100
		);
        
        // Set cell's display attributes
        TextBox cellStyle = table.getStyle();
        cellStyle.setMargin(
    		TableCellHorizontalMargin, // left
    		TableCellHorizontalMargin, // right
    		TableCellVerticalMargin,   // top
    		TableCellVerticalMargin    // bottom
    	);
        cellStyle.setFontStyle("kaiu.ttf");
        cellStyle.setFontSize(TableCellTextSize);

        table.setCell(0, 0, 1, 1, "站號\nStation\nNo.");
        table.setCell(1, 0, 1, 1, "測站名稱\nStation\nName");
        table.setCell(2, 0, 1, 1, "北緯\nNorth\nLatitude");
        table.setCell(3, 0, 1, 1, "東經\nEast\nLongitude");
        table.setCell(4, 0, 1, 1, "氣壓計海面上\n高度\nHeight of\nBarometer\nabove Sea\nLevel (m)");
        table.setCell(5, 0, 1, 1, "溫度計\n地面上高度公尺\nHeight of\nTherm.\nabove\nGround (m)");
        
        table.setCell(6, 0, 1, 1, "雨量器口面\n地上高度\n公尺\nHeight of\nRaingauge\nabove\nGround (m)");
        table.setCell(7, 0, 1, 1, "風速儀\n地上高度\n公尺\nAnem. above\nGround (m)");
        table.setCell(8, 0, 1, 1, "海拔\n公尺\nAltitude\n(m)");
        table.setCell(9, 0, 1, 1, "創立年份\nYear of\nCommen\ncement");
        table.setCell(10, 0, 1, 1, "每日觀測次數\nNo. of\nObs. Per\nDay");
        
        // Put values into all-station Table.
        for (int row = 0; row < stns.size() ; row++) {
        	table.setCell(0, row+1, 1, 1, stns.get(row).getStno());
        	table.setCell(1, row+1, 1, 1, stns.get(row).getStnCName()+"\n"+ stns.get(row).getStnEName());
        	table.setCell(2, row+1, 1, 1, " "+ stns.get(row).getLatitude().trim().replace("N", ""));
        	table.setCell(3, row+1, 1, 1, " "+stns.get(row).getLongitude().trim().replace("E", ""));
			table.setCell(4, row+1, 1, 1, stns.get(row).getHBarometer().trim().replace("m", ""));
			table.setCell(5, row+1, 1, 1, stns.get(row).getHTherm().trim().replace("m", ""));
			table.setCell(6, row+1, 1, 1, stns.get(row).getHRaingauge().trim().replace("m", ""));
			table.setCell(7, row+1, 1, 1, stns.get(row).gethAnem().trim().replace("m", "")); 
			table.setCell(8, row+1, 1, 1, stns.get(row).getAltitude().trim().replace("m", ""));
			table.setCell(9, row+1, 1, 1, stns.get(row).getStnBeginTime().trim().substring(0,4));
			table.setCell(10, row+1, 1, 1, stns.get(row).getManObsNum().trim());
			
        	for (int col=0; col<11; col++) {
            	if (col == 0) {
            		table.getCell(col, row+1).setAlignment(TABLE_CELL_ALIGNMENT_LEFT_TOP);
            	}
            	else if (col == 1) {
            		table.getCell(col, row+1).setAlignment(TABLE_CELL_ALIGNMENT_LEFT_CENTER_VERTICAL);
            	}
            }
        }
    	return table;
	}
	 
	 
	@Override
	public void createTableImage(Object... args){
		super.createTableImage(args);
	}
	 
}
