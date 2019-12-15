package cwb.cmt.summary.createTableImage;

import java.util.List;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.summary.model.CliEleTable;
import cwb.cmt.summary.model.CliEleTable.CliEleData;
import cwb.cmt.summary.utils.StringFormat;

@Service
public class CreateTableImageForCliEle extends CreateTableImage {
	
	final static private int CellHorizontalMargin = 5;
	final static private int CellVerticalMargin = 1;
	final static private double CellFontSize = 6.5;
	final static private double CellChineseFontSize = 8;
	final static private int TitleFontSize = 12;
	final static private int XcontentStartPost = 40;
	final static private int YcontentStartPost = 20;
	
	@Override
	protected BaseChart createChart(int pageNumber, Object... args) {
		// set license code
		Chart.setLicenseCode(CHARTDIRECTOR_LICENSECODE);
				
		// create chart
		XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
		
		
		// add table 
		createTable(XcontentStartPost, YcontentStartPost, chart, (CliEleTable)args[0]);
		
		
		// add page number
//		chart.addText(X_PAGE_NUMBER_POST, Y_PAGE_NUMBER_POST, String.valueOf(pageNumber), ENGLISH_FONT ,PAGE_NUMBER_FONT_SIZE);
		
		return chart;
		
	}
	
	private int createTable(int xPost, int yPost, XYChart chart, CliEleTable dataTable){
		
		List<CliEleData> datas = dataTable.getDatas();
		
		/*
		 *  define relative position of table elements
		 */
		int xTitle = 0;
		int xTable = xPost;
		
		int yTitle = yPost;
		int yTable = yTitle+40;
		
		/*
		 *  add title
		 */
		chart.addText(xTitle, yTitle  // x position, y position
						,StringFormat.centerStrings(dataTable.getTableNo()+ ". " +dataTable.getTitleChineseName()
													+"\n" + dataTable.getTitleEnglishName()
													, 96) // one page may contain max 90 English char in such font size and style 
						,CHINESE_FONT ,TitleFontSize); // font, font size 
		
		/*
		 *  add table
		 */
		CDMLTable cdmlTable = chart.addTable(xTable, yTable, 0, 15, datas.size()+2); // xPosition, yPosition, alignment to the chart, columns, rows
		
		
		/*
		 * set table style and alignment of cell
		 * alignment style:
		 * 4 => The leftmost point on the middle horizontal line
		 * 5 => center alignment
		 * 8 => top center alignment
		 * 9 => top right alignment
		 */
		TextBox cellStyle = cdmlTable.getStyle();
		cellStyle.setMargin(CellHorizontalMargin, CellHorizontalMargin, CellVerticalMargin, CellVerticalMargin); // (left, right, top, bottom)
		cellStyle.setFontStyle(ENGLISH_FONT);
		cellStyle.setBackground(0xffffff, 0xf0f0f0); // white background color, light grey border color
		cellStyle.setFontSize(CellFontSize);

		// set overall table top right alignment 
		cellStyle.setAlignment(9); 
		// set "2001-2010" top center alignment
		cdmlTable.getCell(0, 0).setAlignment(8); 
		// set "1", "2"... center alignment
		for(int i=1; i<=12; i++){
			cdmlTable.getCell(i+1, 0).setAlignment(5);
		}
		// set "MAX" center alignment
		cdmlTable.getCell(14, 0).setAlignment(5);
		// set "測站名稱" 標楷體, center alignment
		TextBox sCell = cdmlTable.getCell(0, 1);
		sCell.setFontStyle(CHINESE_FONT); 
		sCell.setFontSize(CellChineseFontSize);
		sCell.setAlignment(5); 
		
		 
		for(int i=0; i<datas.size(); i++){
			// set station Chinese name(彭佳嶼) 標楷體, center alignment
			TextBox cCell = cdmlTable.getCell(0, i+2);
			cCell.setFontStyle(CHINESE_FONT);
			cCell.setFontSize(CellChineseFontSize);
			cCell.setAlignment(4);  
			// set station English name(PENGJIAYU) center alignment
			cdmlTable.getCell(1, i+2).setAlignment(4);
		}
		
		// set "Jan." center alignment
		for (int i=0; i<12; i++){
			cdmlTable.getCell(i+2, 1).setAlignment(5);
			cdmlTable.getCell(i+2, 1).setFontSize(7);
		}
	
		/*
		 *  content
		 */
		//set table first row
		cdmlTable.setCell(0, 0, 2, 1, dataTable.getYearRange()); // merge cells for "2001-2010"
		for(int i=1; i<=12; i++){
			cdmlTable.setCell(i+1, 0, 1, 1, String.format("%10s", i));
		}
		
		// set table second row 
		cdmlTable.setCell(0, 1, 2, 1, "測 站 名 稱");
		cdmlTable.setCell(2, 1, 1, 1, "Jan.");
		cdmlTable.setCell(3, 1, 1, 1, "Feb.");
		cdmlTable.setCell(4, 1, 1, 1, "Mar.");
		cdmlTable.setCell(5, 1, 1, 1, "Apr.");
		cdmlTable.setCell(6, 1, 1, 1, "May");
		cdmlTable.setCell(7, 1, 1, 1, "Jun.");
		cdmlTable.setCell(8, 1, 1, 1, "Jul.");
		cdmlTable.setCell(9, 1, 1, 1, "Aug.");
		cdmlTable.setCell(10, 1, 1, 1, "Sep.");
		cdmlTable.setCell(11, 1, 1, 1, "Oct.");
		cdmlTable.setCell(12, 1, 1, 1, "Nov.");
		cdmlTable.setCell(13, 1, 1, 1, "Dec.");
		cdmlTable.setCell(14, 0, 1, 1, String.format("%11s", dataTable.getLastColName()));
		
		/*
		 *  import data
		 */
		for(int i=0; i<datas.size(); i++){
			cdmlTable.setCell(0, i+2, 1, 1, String.format("%5s", datas.get(i).getStationChineseName()));
			cdmlTable.setCell(1, i+2, 1, 1, String.format("%17s", datas.get(i).getStationEnglishName()));
			cdmlTable.setCell(2, i+2, 1, 1, datas.get(i).getDataJan());
			cdmlTable.setCell(3, i+2, 1, 1, datas.get(i).getDataFeb());
			cdmlTable.setCell(4, i+2, 1, 1, datas.get(i).getDataMar());
			cdmlTable.setCell(5, i+2, 1, 1, datas.get(i).getDataApr());
			cdmlTable.setCell(6, i+2, 1, 1, datas.get(i).getDataMay());
			cdmlTable.setCell(7, i+2, 1, 1, datas.get(i).getDataJun());
			cdmlTable.setCell(8, i+2, 1, 1, datas.get(i).getDataJul());
			cdmlTable.setCell(9, i+2, 1, 1, datas.get(i).getDataAug());
			cdmlTable.setCell(10, i+2, 1, 1, datas.get(i).getDataSep());
			cdmlTable.setCell(11, i+2, 1, 1, datas.get(i).getDataOct());
			cdmlTable.setCell(12, i+2, 1, 1, datas.get(i).getDataNov());
			cdmlTable.setCell(13, i+2, 1, 1, datas.get(i).getDataDec());
			cdmlTable.setCell(14, i+2, 1, 1, datas.get(i).getDataSummary());
		}
		
		int yTableEnd = yTable + cdmlTable.getHeight();
		return yTableEnd;
	}	
	
	public void createTableImage(int pageNumber, Object... args) {
		super.createTableImage(pageNumber, args);
	}
	
}
