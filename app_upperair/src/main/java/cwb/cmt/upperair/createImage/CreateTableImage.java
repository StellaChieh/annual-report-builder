package cwb.cmt.upperair.createImage;

import java.nio.file.Paths;
import java.time.YearMonth;

import ChartDirector.BaseChart;
import ChartDirector.Chart;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.PageInfo;

public abstract class CreateTableImage {
	
	protected static final int CHART_MARGIN_TOP = 30;    
	protected static final int CHART_WIDTH = 2100;
	protected static final int CHART_HEIGHT = 2970 - CHART_MARGIN_TOP;
	protected static final int TITLE_MARGIN_TOP = 100;
	protected static final int TITLE_FONT_SIZE = 22;
	protected static final int TABLE_MARGIN_TOP  = 190-30;
	
	
	public enum InnerPageOrderInOneSection{
		STANDARD_PAGE1(1),
		STANDARD_PAGE2(2),
		SIGNIFICANT_PAGE1(1),
		SIGNIFICANT_PAGE2(2),
		SIGNIFICANT_PAGE3(3);
		
		int order;
		
		InnerPageOrderInOneSection(int order){
			this.order = order;
		}
		
		public int value() {
			return this.order;
		}
	}

    public void createTableImage(Station station, YearMonth yearMonth
    							, String time, Object object
    							, InnerPageOrderInOneSection pageOrder
    							, PageInfo pageInfo)  {
        
    	Chart.setLicenseCode("DEVP-2LPQ-D6YH-UTLK-EBC0-3E26");
    	
    	// create chart with given data
        BaseChart chart = createChart(station, yearMonth, time, object, pageOrder);
                
        // perform chart producing
        makeChart(chart, pageInfo);
    }
    
    private BaseChart makeChart(BaseChart chart, PageInfo pageInfo) {
    	// down size the pdf to normal size
    	chart.setOutputOptions("width=1654; height = 2339; dpi=200");
    	chart.makeChart(Paths.get(pageInfo.getFilePath().toString()).toString());
		return chart;
	}
	
	protected abstract BaseChart createChart(Station station, YearMonth yearMonth
											, String time, Object object
											, InnerPageOrderInOneSection pageOrder);

}