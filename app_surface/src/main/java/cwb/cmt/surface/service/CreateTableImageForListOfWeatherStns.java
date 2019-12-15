package cwb.cmt.surface.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.PlotArea;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.surface.model.Station;

@Service("createTableImageForListOfWeatherStns")
public class CreateTableImageForListOfWeatherStns extends CreateTableImage {

    // Layout attributes
	private final int ChartMarginTop = 12;
	private final int ChartWidth = 2100;
    private final int ChartHeight = 2970;
//    private final int TableMarginTop  = 195;
    private final int PageNumberTextMarginBottom = 103 + 7;
    
    private final int SectionTitleTextSize = 33;
    private final int SectionSubTitleTextSize = 30;
    private final int PageChineseNoteTextSize = 16;
    private final int PageEnglishNoteTextSize = 15;
    private final int PageNumberTextSize = 17;
    private final int TableCellTextSize = 18;
    private final int TableCellHorizontalMargin = 8;
    private final int TableCellVerticalMargin = 6;
    
    protected BaseChart createChart(Object...objects) {
		
    	@SuppressWarnings("unchecked")
		List<Station> stn = (List<Station>) objects[0];   
//    	@SuppressWarnings("unchecked")
//		int pageNumber = (int) objects[1];   
    	
        Chart.setLicenseCode("DEVP-2LPQ-D6YH-UTLK-EBC0-3E26");
        
        // Create a XYChart object of size 600 x 400 pixels
        XYChart chart = new XYChart(ChartWidth, ChartHeight);
        
        // Add a title (chinese)
        TextBox text_title_chinese = chart.addTitle(
        		"2.2  專用氣象觀測站編號一覽表",
                "kaiu.ttf",
                SectionTitleTextSize
        );
        text_title_chinese.setPos(
        		text_title_chinese.getLeftX(),
        		text_title_chinese.getTopY() + ChartMarginTop
		);
        
        // Add a title (english)
        TextBox text_title_english = chart.addTitle(
        		"List of Weather Station Number of Other Organization",
                "kaiu.ttf",
                SectionTitleTextSize
        );
        text_title_english.setPos(
        		text_title_english.getLeftX(),
        		text_title_chinese.getTopY() + text_title_chinese.getHeight() + 15
		);
        
        
        // draw the outline of the area
        PlotArea area = chart.setPlotArea(402 + 50, 165, 1180, 332);
        
        // description
        TextBox text_station_description = chart.addText(
        		area.getLeftX() + 23,
        		area.getTopY()  + 10,
        		"站  號  說  明",
        		"kaiu.ttf",
        		25
        );
        
        // station number
        TextBox text_station_example_stationId = chart.addText(
        		text_station_description.getLeftX() + 50,
        		text_station_description.getTopY() + text_station_description.getHeight() + 10,
        		"5 1 P 1 5 0",
        		"arial.ttc",
        		21
        );
        
        // station name
        TextBox text_station_example_stationName = chart.addText(
        		text_station_example_stationId.getLeftX() + text_station_example_stationId.getWidth() + 36,
        		text_station_example_stationId.getTopY(),
        		"高雄縣 內門",
        		"kaiu.ttf",
        		22
        );
        
        // description for the components of station-information
        chart.addText(
        		text_station_example_stationName.getLeftX() + text_station_example_stationName.getWidth() + 10,
        		text_station_example_stationName.getTopY()  + 42,
        		"鄉鎮名稱" + "\n" +
				"測站所在縣市"  + "\n" +
				"識別碼"  + "\n" +
				"分區編號  15"  + "\n" +
				"區號：P--高雄"  + "\n" +
				"測站類別：0--氣候站; 1--雨量站; 2--農業氣象站"  + "\n" +
				"隸屬機關代號：5--台灣糖業公司"  + "\n",
        		"kaiu.ttf",
        		19
        );
        
        // line 1
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 15,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10,
        		text_station_example_stationId.getLeftX() + 15,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201
		);
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 15,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201,
        		(text_station_example_stationId.getLeftX() + 15) + 345,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201
		);
        
        // line 2
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10,
        		text_station_example_stationId.getLeftX() + 8 + 30,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30
		);
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30,
        		(text_station_example_stationId.getLeftX() + 8 + 30) + 320,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30
		);
        
        // line 3
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10,
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35
		);
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35,
        		(text_station_example_stationId.getLeftX() + 8 + 30 + 23) + 297,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35
		);
        
        // line 4-1
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10,
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35 - 30
		);
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35 - 30,
        		(text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27) + 270,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35 - 30
		);
        
        // line 4-2
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10,
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 30
		);
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 30,
        		(text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23),
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 30
		);
        
        // line 5
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10,
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35 - 30 - 30
		);
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35 - 30 - 30,
        		(text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20) + 227,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35 - 30 - 30
		);
        
        // line 6 (near the chinese name)
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20 + 108,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10,
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20 + 108,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35 - 30 - 30 - 32
		);
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20 + 108,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35 - 30 - 30 - 32,
        		(text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20 + 108) + 119,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30 - 35 - 30 - 30 - 32
		);
        
        // line 7
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20 + 108 + 81,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10,
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20 + 108 + 81,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30-35 - 30 - 30 - 32 - 32
		);
        chart.addLine(
        		text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20 + 108 + 81,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30-35 - 30 - 30 - 32 - 32,
        		(text_station_example_stationId.getLeftX() + 8 + 30 + 23 + 27 + 23 + 20 + 108 + 81) + 38,
        		text_station_example_stationId.getTopY()  + text_station_example_stationId.getHeight() + 10 + 201 - 30-35 - 30 - 30 - 32 - 32
		);
        
        // create a table
        CDMLTable table = createTable(chart,
        		100, 570,
        		stn
		);
        return chart;
    }
    
    
    private CDMLTable createTable(XYChart chart, int posX, int posY, List<Station> stn) {
        // Create a CDMLTable
        CDMLTable table = chart.addTable(
    		posX, posY, 0,
    		15,
    		50
		);
        
        // Set cell's display attributes
        TextBox cellStyle = table.getStyle();
        cellStyle.setMargin(
    		TableCellHorizontalMargin+11, // left
    		TableCellHorizontalMargin+11, // right
    		TableCellVerticalMargin,      // top
    		TableCellVerticalMargin+1    // bottom
    	);
        cellStyle.setFontStyle("mingliu.ttc");
        cellStyle.setFontSize(TableCellTextSize+4);
        cellStyle.setBackground(0x00ffffff, 0xff000000);
        cellStyle.setAlignment(TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_TOP);        
        
        TextBox text_cwb = chart.addText(
	    		posX, posY,
	    		"中央氣象局",
	    		"mingliu.ttc",
	    		26,
	    		0x00ff0000
		);
        text_cwb.setPos(text_cwb.getLeftX()+18, text_cwb.getTopY()-text_cwb.getHeight()-13);
        
        	
    	String lastDistrict=null;
    	boolean caa=false;
    	boolean sameCity = false;
    	
    	int tableColStart=0;
    	int tableCol=0;
    	int tableRow = 0;
    	
    	int index = 0;
    	final int length = stn.size(); //per page of stns=240
    	
    	for (tableColStart=0; tableCol<15; tableCol+=3) {
    		String lastCity = null;
    		for (tableRow=0; tableRow<48; tableRow++) {
    			if (index >= length) {
    				break;
    			}
        		// set flag
        		sameCity = stn.get(index).getCity() != null && stn.get(index).getCity().equals(lastCity);        		
        		
//        		if ("caa".equals(stn.get(index).getClassify())) {
//        			if (!caa) {
//	        			//empty line before caa stations
//	    				table.getCell(tableCol, tableRow).setFontSize(26);
//	    				table.setCell(tableCol, tableRow, 1, 1, "      ");
//	    				tableRow += 1;
//	    				
//	    				table.getCell(tableCol, tableRow).setMargin(
//	         					TableCellHorizontalMargin+12,
//	         			    	TableCellHorizontalMargin+8,
//	         			    	TableCellVerticalMargin+1,
//	         			    	TableCellVerticalMargin+15);
//	    				table.getCell(tableCol, tableRow).setFontSize(26);
//	    				table.getCell(tableCol, tableRow).setFontColor(0x00ff0000);
//	    				table.setCell(tableCol, tableRow, 1, 1, "民航局");
//	    				caa = true;
//        				continue;
//        			}
//        		}
        		
        		// station no
        		table.getCell(tableCol, tableRow).setMargin(
 					TableCellHorizontalMargin+8, // left
 			    	TableCellHorizontalMargin+8, // right
 			    	TableCellVerticalMargin+1,   // top
 			    	TableCellVerticalMargin+1    // bottom
		    	);
 			    table.setCell(tableCol, tableRow, 1, 1, stn.get(index).getStno());
 			    
 			    // city or county
 			    table.getCell(tableCol+1, tableRow).setMargin(
 					TableCellHorizontalMargin+8,     // left
 			    	TableCellHorizontalMargin+8,     // right
 			    	TableCellVerticalMargin+1,       // top
 			    	TableCellVerticalMargin+1        // bottom
		    	);
 			    table.setCell(tableCol+1, tableRow, 1, 1, (sameCity)? "      ":stn.get(index).getCity());
 			    
 			    // district
 			    String district = "      ";
 			    if (stn.get(index).getAddress() != null && !stn.get(index).getAddress().trim().isEmpty()) {
 			    	int begin=-1, end=-1;
 			    	boolean addressContainsCity = stn.get(index).getAddress().startsWith(stn.get(index).getCity());
	 			    begin = (addressContainsCity)
		    		        ? stn.get(index).getCity().length()
		    		        : 0;
	                end=begin+3;
	                district = stn.get(index).getAddress().substring(begin, end);
 			    }
 			    table.getCell(tableCol+2, tableRow).setMargin(
	 			    TableCellHorizontalMargin+8,     // left
	 			    TableCellHorizontalMargin+8+4*5, // right
	 			    TableCellVerticalMargin+1,       // top
	 			    TableCellVerticalMargin+1        // bottom
		        );
 			    table.setCell(tableCol+2, tableRow, 1, 1, district);
 			    
 			    lastCity = stn.get(index).getCity();
 			    lastDistrict = district;
    			index++;
    		}//for row
    	}
    	return table;
	}
    
}
