package cwb.cmt.summary.createTableImage;

import java.util.List;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.XYChart;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.utils.Numbers;

@Service
public class CreateTableImageForPartOneContents extends CreateTableImage {

	final static private int CELL_VERTICAL_MARGIN = 3;
	final static private double CELL_FONT_SIZE = 10.0;
	final static private int TITLE_FONT_SIZE = 14;
	final static private int X_CONTENT_START_POST = 80;
	final static private int Y_CONTENT_START_POST = 40;

	private static final int ROWS_PER_CONTENTS_PAGE = Numbers.ROWS_OF_PART_ONE_CONTENTS.getNumber();
	
	
	@Override
	protected BaseChart createChart(int pageNumber, Object... args){
		
		@SuppressWarnings("unchecked")
		List<ClimaticElement> ces = (List<ClimaticElement>)args[0];
		
		int ceStart = (int)args[1];
		
		int cePageInterval = (int)args[2];
		
		boolean isLast = (boolean)args[3];
		
		// set license code
		Chart.setLicenseCode(CHARTDIRECTOR_LICENSECODE);
		
		// create chart
		BaseChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
		
		String title = "第一部  目錄  Part One Contents";
		chart.addText(250, 30  // xPost, yPost
					, title
					, CHINESE_FONT ,TITLE_FONT_SIZE); // font, font size
			 
		
		
		// Create a CDMLTable 參數分別是 x, y, 預設位置(0~12), 欄數, 列數
		CDMLTable table = chart.addTable(X_CONTENT_START_POST, Y_CONTENT_START_POST+40, 0, 2, ROWS_PER_CONTENTS_PAGE);
		table.getStyle().setFontSize(CELL_FONT_SIZE);
		table.getStyle().setBackground(0xffffff, 0xf0f0f0);  // white background color, light grey color for border
		table.getStyle().setFontStyle(CHINESE_FONT);
		table.getStyle().setMargin(CELL_VERTICAL_MARGIN);
		table.getColStyle(0).setWidth(600);
		table.getColStyle(0).setAlignment(4); // left alignment
		
		
		int ceCount = ceStart;
        for(int i=0; i<ces.size(); i++){
    		String word = ceCount + ".  "+ ces.get(i).getChineseName() + "  " + ces.get(i).getEnglishName();
        	String page = String.valueOf(ceCount*cePageInterval-(cePageInterval-1));    
        	table.setCell(0, i, 1, 1, word);
        	table.setCell(1, i, 1, 1, page);
        	ceCount++;	
        }
        
        if(isLast){
        	String word = ceCount + ".  "+ "風花圖" + "  " + "Wind Rose";
        	String page = String.valueOf((ceCount-1)*cePageInterval+1);
        	table.setCell(0, ces.size(), 1, 1, word);
        	table.setCell(1, ces.size(), 1, 1, page);
        }
        
		return chart;
	}

	
	public void createTableImage(int pageNumber, Object... args) {
		super.createTableImage(pageNumber, args);
	}
}
