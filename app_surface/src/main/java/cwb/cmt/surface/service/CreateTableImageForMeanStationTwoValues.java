package cwb.cmt.surface.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.surface.model.ClimaticElement;
import cwb.cmt.surface.model.MeanStationValues;
import cwb.cmt.surface.utils.SpecialValue;


@Service("createTableImageForMeanStationTwoValues")
public class CreateTableImageForMeanStationTwoValues extends CreateTableImage{
	
    private final int SectionTitleTextSize = 40;
    private final int PageChineseNoteTextSize = 16;
    private final int PageEnglishNoteTextSize = 15;
    private final int TableCellTextSize = 18;
    private final int TableCellHorizontalMargin = 8;
    private final int TableCellVerticalMargin = 6;
    
    // Layout attributes
    // Width, Height
    private final int ChartMarginTop = 45;
    private final int ChartWidth = 2100;
    private final int ChartHeight = 2970 - ChartMarginTop;
    // Position, margins
    private final int FooterTextMarginBottom = 218;
    private final int PageNumberTextMarginBottom = 103 + 7;
    private final int PageHeaderTextGapAboveTable = 5;
    private final int TableMarginTop = 100;
    private int TableMarginLeft = 80;
    // Text size
    private final int PageTitleTextSize = 21;
    private final int PageHeaderFooterPageNumberTextSize = 16;
    
    // Data
    private String mUnitTextChinses;
    private String mUnitTextEnglish;
    private boolean mShowsPageCenterTitle;
    
    
    // Constants
    private static final String[] TableHeader = {
        "一月\n Jan.", "二月\n Feb.", "三月\n Mar.",
        "四月\n Apr.", "五月\n May", "六月\n Jun.",
        "七月\n Jul.", "八月\n Aug.", "九月\n Sep.",
        "十月\n Oct.", "十一月\n Nov.", "十二月\n Dec.",
        "全年\n Annual"
    };
    
    //Time: year
    @Resource(name="year")
    protected int year;
    
    //Time: month
    @Resource(name="month")
    protected int month;
    
	@Override
	protected BaseChart createChart(Object... objects) {
		
		@SuppressWarnings("unchecked")
		ClimaticElement ceXmlList = (ClimaticElement) objects[0];
		@SuppressWarnings("unchecked")
		Map<String,List<MeanStationValues>> ceMap = 
		(Map<String, List<MeanStationValues>>) objects[1];
		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> annualMap = (Map<String, Map<String, String>>) objects[2];
		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> annualMap2 = (Map<String, Map<String, String>>) objects[3];
		@SuppressWarnings("unchecked")
		List<String> stnList = (List<String>) objects[4];
		boolean firstPage = (boolean)objects[5];
		boolean lastPage = (boolean)objects[6];
		String pageTitleIndex = (String) objects[7];
		
        Chart.setLicenseCode("DEVP-2LPQ-D6YH-UTLK-EBC0-3E26");

        // Create a XYChart pro of A4 size
        XYChart chart = new XYChart(
                ChartWidth,
                ChartHeight
        );
        
        // Add a title to the chart using 18 pts Times Bold Italic font
        chart.addTitle(pageTitleIndex + ceXmlList.getChineseTitle() + " " + ceXmlList.getEnglishTitle(),
                         "kaiu.ttf",
                         PageTitleTextSize
                 );
        
        // Create a CDMLTable
        CDMLTable table = createTable(chart,
                TableMarginLeft,
                TableMarginTop,
                ceMap,
                annualMap,
                annualMap2,
                stnList,
                ceXmlList.getId()
        );
        
        // compute the adjusted margins
        final int marginLeft = (ChartWidth - table.getWidth()) / 2;
        final int marginTop  = TableMarginTop;
        
        // update global marginleft
        TableMarginLeft = marginLeft;
        
        // adjust table's position
        table.setPos(marginLeft, marginTop);
        
        // Set header title center
        TextBox title_center = chart.addText(
                marginLeft + table.getWidth()/2,
                marginTop,
                (firstPage)?"":"（續   continued）"
        );
        title_center.setFontStyle("kaiu.ttf");
        title_center.setFontSize(PageHeaderFooterPageNumberTextSize);
        title_center.setPos(
                title_center.getLeftX() - title_center.getWidth()/2,
                title_center.getTopY() - title_center.getHeight() - PageHeaderTextGapAboveTable
        );
        
        // Header title left
        TextBox title_left = chart.addText(
                marginLeft,
                marginTop,
                "年Year:  " + year
        );
        title_left.setFontStyle("kaiu.ttf");
        title_left.setFontSize(PageHeaderFooterPageNumberTextSize);
        title_left.setPos(title_left.getLeftX(), title_left.getTopY() - title_left.getHeight() - PageHeaderTextGapAboveTable);
        
        // Header title right
        TextBox title_right = chart.addText(
                marginLeft + table.getWidth(),
                marginTop,
                "單位："  + ((ceXmlList.getChineseUnit() != null)? ceXmlList.getChineseUnit() : "") + "\n" +
                "UNIT：" + ((ceXmlList.getEnglishUnit() != null)? ceXmlList.getEnglishUnit() : "")
        );
        
        title_right.setFontStyle("kaiu.ttf");
        title_right.setFontSize(PageHeaderFooterPageNumberTextSize);  
        title_right.setPos(
                title_right.getLeftX() - title_right.getWidth(),
                title_right.getTopY() - title_right.getHeight() - PageHeaderTextGapAboveTable
        );
        
        final int marginTop_pageFooter = (marginTop + table.getHeight() >= ChartHeight-FooterTextMarginBottom)
                ? marginTop + table.getHeight()+9
                : ChartHeight-FooterTextMarginBottom;
        
        // Page footer left
        TextBox page_footer_left = chart.addText(
                marginLeft,
                marginTop_pageFooter,
                (lastPage)?"":"（續 to be continued）"
        );
        page_footer_left.setFontStyle("kaiu.ttf");
        page_footer_left.setFontSize(PageHeaderFooterPageNumberTextSize);
        page_footer_left.setPos(
                page_footer_left.getLeftX(),
                page_footer_left.getTopY()
        );
        
        // Page footer right
        TextBox page_footer_right = chart.addText(
                marginLeft + table.getWidth(),
                marginTop_pageFooter,
                "特殊註記請參考第ii頁說明"
        );
        page_footer_right.setFontStyle("kaiu.ttf");
        page_footer_right.setFontSize(PageHeaderFooterPageNumberTextSize);
        page_footer_right.setPos(
                marginLeft + table.getWidth() - page_footer_right.getWidth(),
                page_footer_left.getTopY()
        );
        
		// perform chart producing
		return chart;
	}
	
	
	 private CDMLTable createTable(XYChart chart, int posX, int posY, 
			 Map<String,List<MeanStationValues>> ceMap,
			 Map<String, Map<String, String>> annualMap, Map<String, Map<String, String>> annualMap2,
			 List<String> stnList, String ceXmlId) {   
//		 System.out.println("annualMap>> "+annualMap);
		 String keyname = null;
		 for(String k:ceMap.keySet()) {
			 keyname = k;
		 }
		// Create a CDMLTable
        CDMLTable table = chart.addTable(
                posX, posY, 0,
                12 + 2,
                ceMap.get(keyname).size()+1
        );
        
        // set layout attributes
        TextBox cellStyle = table.getStyle();
        cellStyle.setMargin(
                14, // left
                14, // right
                6,  // top
                6   // bottom
        );
        cellStyle.setFontStyle("kaiu.ttf");
        cellStyle.setFontSize(19);
        //setting table header
    	for(int col=0; col<14; col++) {
    		if (col == 0) {
	            table.getCell(0, 0).setAlignment(TABLE_CELL_ALIGNMENT_LEFT_TOP); // Align left
	            table.setCell(0, 0, 1, 1, "站號及測站名稱\nStation No.  Name");
	        }
    		else {
	            table.getCell(col, 0).setAlignment(TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_BOTTOM); // Align center
	            table.setCell(col, 0, 1, 1, TableHeader[col-1]);
    		}
    	}
    	
    	//create table for WSWD
		//include special value like X, /, @, -
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.CeUnknown.getNumber(),
				SpecialValue.NewStation.getNumber(), 
				SpecialValue.CancelStation.getNumber(),
				SpecialValue.Trace.getNumber(),
				SpecialValue.SubstituteZero.getNumber()
				);
		List<MeanStationValues> ceList = new ArrayList<>();
        Map<String, List<MeanStationValues>> groupCeMap = new HashMap<>();
        Map<String, List<MeanStationValues>> sortedCeMap = new HashMap<>();
        Map<String, List<MeanStationValues>> groupCeMap2 = new HashMap<>();
        Map<String, List<MeanStationValues>> sortedCeMap2 = new HashMap<>();
        Map<String, String> annual1= new HashMap<>();
        Map<String, String> annual2= new HashMap<>();
    	int countKey = 0;
    	for(String k:ceMap.keySet()) {
    		countKey++;
    		ceList = ceMap.get(k);
    		if(countKey==1) {
    			//group by stno from ceList
        		groupCeMap = ceList.stream()
        	    		.collect(Collectors.groupingBy(MeanStationValues::getStno));
        		sortedCeMap = groupCeMap.entrySet().stream()
                		.sorted(Map.Entry.comparingByKey())
                		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    		}
    		else if (countKey==2){
    			//group by stno from ceList
        		groupCeMap2 = ceList.stream()
        	    		.collect(Collectors.groupingBy(MeanStationValues::getStno));
        		sortedCeMap2 = groupCeMap2.entrySet().stream()
                		.sorted(Map.Entry.comparingByKey())
                		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    		}
    	}
    	int countKey2 = 0;
    	for(String k:annualMap.keySet()) {
    		countKey2++;
    		if(countKey2==1) {
    			annual1 = annualMap.get(k);
    		}
    	}
    	String data1 = null;
    	String data2 = null;
    	String primaryColumn = null;
//    	System.out.println("sortedCeMap.values().size()>> "+sortedCeMap.values().size());//size=32
    	Map<String, List<String>> data1Map = new HashMap<>();
    	Map<String, List<String>> data2Map = new HashMap<>();
    	Map<String, List<String>> data1Maps = new HashMap<>();
    	Map<String, List<String>> data2Maps = new HashMap<>();
    	for(List<MeanStationValues> value:sortedCeMap.values()) {
    		List<String> dataList = new ArrayList<>();
    		//month=11
    		for(int count=0; count<month; count++){
        		data1 = value.get(count).getMonthlyValue();
        		primaryColumn = value.get(0).getStno() + "  " + value.get(0).getStnCName();
        		dataList.add(data1);
    		}
    		data1Map.put(primaryColumn, dataList);
    	}
    	data1Maps = data1Map.entrySet().stream()
    				.sorted(Map.Entry.comparingByKey())
    				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(oldValue, newValue) -> oldValue, LinkedHashMap::new));
    	for(List<MeanStationValues> value:sortedCeMap2.values()) { //size=32
    		List<String> dataList = new ArrayList<>();
    		for(int count=0; count<month; count++){   //month=11
        		data2 = value.get(count).getMonthlyValue();
        		primaryColumn = value.get(0).getStno() + "  " + value.get(0).getStnCName();
        		dataList.add(data2);
    		}
    		data2Map.put(primaryColumn, dataList);
    	}
    	data2Maps = data2Map.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(oldValue, newValue) -> oldValue, LinkedHashMap::new));
    	
    	
    	int row=0;
    	int countStn = 0;
    	for(String stno :data1Maps.keySet()) { 
    		if(row<stnList.size()) { 
        		 // primary column
                // Align up
                table.getCell(0, row+1).setAlignment(TABLE_CELL_ALIGNMENT_LEFT_TOP);
                table.setCell(0, row+1, 1, 1, stno);
            	for(int col=0; col<month; col++) { //config.month = 12
	                // align up
                    table.getCell(col+1, row+1).setAlignment(TABLE_CELL_ALIGNMENT_RIGHT_TOP);
                    
                    if(specialValueList.contains(data1Maps.get(stno).get(col))){
                    	if(data1Maps.get(stno).get(col).equals(data2Maps.get(stno).get(col))){
                    		table.setCell(col+1, row+1, 1, 1, "   " + data1Maps.get(stno).get(col)+
                            		"\n"+"  ");
                    	}
                    	
                    }else {
	                    table.setCell(col+1, row+1, 1, 1, "   " + data1Maps.get(stno).get(col)+
	                    		"\n"+data2Maps.get(stno).get(col));
                    }
                }
        	}
    		if(countStn<stnList.size()) {
    			table.setCell(13, row+1, 1, 1, String.format("%8s", annual1.get(stnList.get(countStn)))+
    					"\n"+String.format("%8s", annualMap2.get(stnList.get(countStn))));
    		}
    		countStn++;
    		row++;
    	}
        
    	return table;
	}
	 
	 
	@Override
	public void createTableImage(Object... args) {
		super.createTableImage(args);
	}
	 

}
