package cwb.cmt.summary.createTableImage;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.summary.model.StnCliEleTable;
import cwb.cmt.summary.model.StnCliEleTable.StnCliEleData;

@Service
public class CreateTableImageForStnCliEle extends CreateTableImage {
	
	final static private int CELL_HORIZONTAL_MARGIN = 7;
	final static private int CELL_VERTICAL_MARGIN = 2;
	final static private double CELL_FONT_SIZE = 6.0;
	final static private int TITLE_FONT_SIZE = 10;
	final static private int X_CONTENT_START_POST = 50;
	final static private int Y_CONTENT_START_POST = 40;

	@SuppressWarnings("unchecked")
	@Override
	protected BaseChart createChart(int pageNumber, Object... args){
		
		List<StnCliEleTable> list = (List<StnCliEleTable>)args[0];
		
		// set license code
		Chart.setLicenseCode(CHARTDIRECTOR_LICENSECODE);
		
		// create chart
		BaseChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
		
		// add station page header
		String pageHeader = list.get(0).getStnNo() + " " + list.get(0).getStnCName() + "  " + list.get(0).getStnEName();
		chart.addText(0, 5  // xPost, yPost
					, StringUtils.center(pageHeader, 140) // use apache-commons-lang 3.6 to centralize
					, CHINESE_FONT ,8); // font, font size
		
		// add table 1
		int yTableEnd = createTable(X_CONTENT_START_POST, Y_CONTENT_START_POST, chart, list.get(0));
		
		
		// add table 2
		if(list.size()>=2){
			yTableEnd = createTable(X_CONTENT_START_POST, yTableEnd+20, chart, list.get(1));
		} 
		
		// add table 3
		if(list.size()>=3){
			createTable(X_CONTENT_START_POST, yTableEnd+20, chart, list.get(2));
		}
		
		// add page number
//		chart.addText(X_PAGE_NUMBER_POST, Y_PAGE_NUMBER_POST, String.valueOf(pageNumber), ENGLISH_FONT ,PAGE_NUMBER_FONT_SIZE);
		
		return chart;
	}
	
	
	private int createTable(int xPost, int yPost, BaseChart chart, StnCliEleTable stnCliEletable){
		
		/*
		 *  retrieve elements
		 */
		List<StnCliEleData> datas = stnCliEletable.getDatas();
		
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
		String cTitle = stnCliEletable.getTableNo() + ".  " +stnCliEletable.getTitleCName();
		String eTitle = stnCliEletable.getTitleEName();
		// make Chinese title and English title both center
		if(cTitle.length() > eTitle.length()){
			int padSpace = (cTitle.trim().length() - eTitle.trim().length())/2;
			eTitle = String.format("%"+padSpace+"s", eTitle);
			eTitle = String.format("%-"+padSpace+"s", eTitle);
		} else {
			int padSpace = (eTitle.trim().length() - cTitle.trim().length())/2;
			cTitle = String.format("%"+padSpace+"s", cTitle);
			cTitle = String.format("%-"+padSpace+"s", cTitle);
		}
		
		TextBox title = chart.addText(0, yTitle  // x position, y position
										, cTitle + "\n" + eTitle
										, CHINESE_FONT 
										, TITLE_FONT_SIZE); // font, font size
		// make title TextBox the chart width, and have text int the center of the TextBox
		title.setWidth(CHART_WIDTH);
		title.setAlignment(5);

		/*
		 *  add table
		 */
		CDMLTable table = chart.addTable(xTable, yTable, 0, 15, 15); // xPosition, yPosition, alignment, columns, rows
		
		/*
		 * set table style and alignment of cell
		 * alignment style:
		 * 4 => The leftmost point on the middle horizontal line
		 * 5 => center alignment
		 * 8 => top center alignment
		 * 9 => top right alignment
		 */
		TextBox cellStyle = table.getStyle();
		cellStyle.setMargin(CELL_HORIZONTAL_MARGIN, CELL_HORIZONTAL_MARGIN, CELL_VERTICAL_MARGIN, CELL_VERTICAL_MARGIN); // (left, right, top, bottom)
		cellStyle.setFontStyle(ENGLISH_FONT);
		cellStyle.setBackground(0xffffff, 0xf0f0f0); // white background color, light grey color for border
		cellStyle.setFontSize(CELL_FONT_SIZE);
		// set cell default top right alignment
		cellStyle.setAlignment(9); 	
		// set "Year" 8 font size and center alignment
		table.getCell(0, 0).setFontSize(7); 
		table.getCell(0, 0).setAlignment(5); 
		
		// set "1", "2"..."12", "MAX" 8 font size and center alignment
		for(int i=1; i<=13; i++){
			table.getCell(i+1, 0).setFontSize(7);
			table.getCell(i+1, 0).setAlignment(5); 
		}
		
		// set 'A.D' row center alignment
		table.getRowStyle(1).setAlignment(5);
		
		// set 'CHINA' column center alignment
		table.getColStyle(1).setAlignment(5);
		
		// fix "MEAN", "MIN" width
		table.getColStyle(14).setWidth(50);
		
		// set "Jan." 8 font size
		for (int i=1; i<=12; i++){
			table.getCell(i+1, 1).setFontSize(7);
		}
		
		// set "2001-2010" column top left alignment
		for (int i=10; i<13; i++){
			table.getCell(0, i+2).setAlignment(7); 
		}
		
		
		
		/*
		 *  content
		 */
		// set first row
		table.setCell(0, 0, 2, 1,String.format("%10s", "Year")); // merge cells for "Year"
		for(int i=1; i<=12; i++){
			table.setCell(i+1, 0, 1, 1, String.format("%10s", i));
		}
		// set second row
		table.setCell(0, 1, 1, 1, "A.D");
		table.setCell(1, 1, 1, 1, "CHINA");
		table.setCell(2, 1, 1, 1, "Jan.");
		table.setCell(3, 1, 1, 1, "Feb.");
		table.setCell(4, 1, 1, 1, "Mar.");
		table.setCell(5, 1, 1, 1, "Apr.");
		table.setCell(6, 1, 1, 1, "May");
		table.setCell(7, 1, 1, 1, "Jun.");
		table.setCell(8, 1, 1, 1, "Jul.");
		table.setCell(9, 1, 1, 1, "Aug.");
		table.setCell(10, 1, 1, 1, "Sep.");
		table.setCell(11, 1, 1, 1, "Oct.");
		table.setCell(12, 1, 1, 1, "Nov.");
		table.setCell(13, 1, 1, 1, "Dec.");
		table.setCell(14, 0, 1, 1, stnCliEletable.getLastColName());   // ex: MEAN
			
		/*
		 *  import data
		 */
		for(int i=0; i<10; i++){
			table.setCell(0, i+2, 1, 1, datas.get(i).getAdYear());     // ex:2001
			table.setCell(1, i+2, 1, 1, datas.get(i).getChinaYear());  // ex:90
			table.setCell(2, i+2, 1, 1, datas.get(i).getDataJan());
			table.setCell(3, i+2, 1, 1, datas.get(i).getDataFeb());
			table.setCell(4, i+2, 1, 1, datas.get(i).getDataMar());
			table.setCell(5, i+2, 1, 1, datas.get(i).getDataApr());
			table.setCell(6, i+2, 1, 1, datas.get(i).getDataMay());
			table.setCell(7, i+2, 1, 1, datas.get(i).getDataJun());
			table.setCell(8, i+2, 1, 1, datas.get(i).getDataJul());
			table.setCell(9, i+2, 1, 1, datas.get(i).getDataAug());
			table.setCell(10, i+2, 1, 1, datas.get(i).getDataSep());
			table.setCell(11, i+2, 1, 1, datas.get(i).getDataOct());
			table.setCell(12, i+2, 1, 1, datas.get(i).getDataNov());
			table.setCell(13, i+2, 1, 1, datas.get(i).getDataDec());
			table.setCell(14, i+2, 1, 1, datas.get(i).getDataSummary());
		}
		
		for (int i=10; i<13; i++){
			table.setCell(0, i+2, 2, 1, datas.get(i).getYearRange());  // ex: 2001-2010
			table.setCell(2, i+2, 1, 1, datas.get(i).getDataJan());
			table.setCell(3, i+2, 1, 1, datas.get(i).getDataFeb());
			table.setCell(4, i+2, 1, 1, datas.get(i).getDataMar());
			table.setCell(5, i+2, 1, 1, datas.get(i).getDataApr());
			table.setCell(6, i+2, 1, 1, datas.get(i).getDataMay());
			table.setCell(7, i+2, 1, 1, datas.get(i).getDataJun());
			table.setCell(8, i+2, 1, 1, datas.get(i).getDataJul());
			table.setCell(9, i+2, 1, 1, datas.get(i).getDataAug());
			table.setCell(10, i+2, 1, 1, datas.get(i).getDataSep());
			table.setCell(11, i+2, 1, 1, datas.get(i).getDataOct());
			table.setCell(12, i+2, 1, 1, datas.get(i).getDataNov());
			table.setCell(13, i+2, 1, 1, datas.get(i).getDataDec());
			table.setCell(14, i+2, 1, 1, datas.get(i).getDataSummary());
		}
		
		int yTableEnd = yTable + table.getHeight();
		return yTableEnd;
	}	
	
	public void createTableImage(int pageNumber, Object... args) {
		super.createTableImage(pageNumber, args);
	}
}
