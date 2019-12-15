package cwb.cmt.summary.createTableImage;

import java.nio.file.Paths;

import ChartDirector.BaseChart;

public abstract class CreateTableImage {
	
	protected String path;
	protected String filename;
	
	final static protected String CHARTDIRECTOR_LICENSECODE = "DEVP-2LPQ-D6YH-UTLK-EBC0-3E26";
	
	final static protected int CHART_WIDTH = 794;
	final static protected int CHART_HEIGHT = 1123;
	
	final static protected String CHINESE_FONT = "kaiu.ttf";
	final static protected String ENGLISH_FONT = "Arial.ttf";
	
	final static protected int X_PAGE_NUMBER_POST = CHART_WIDTH / 2;
	final static protected int Y_PAGE_NUMBER_POST = CHART_HEIGHT - 30;
	final static protected double PAGE_NUMBER_FONT_SIZE = 11.0;
		
    protected void createTableImage(int pageNumber, Object... args)  {
    	// create chart with given data
        BaseChart chart = createChart(pageNumber, args);
                
        // perform chart producing
        makeChart(chart, Paths.get(path, filename).toString(), "pdf");
//      makeChart(chart, Paths.get(path, filename).toString(), "svg");
    }
    
    protected void makeChart(BaseChart chart, String filename, String extension) {
    	// down size the pdf to normal size
    	chart.setOutputOptions("width=595; height = 842");
    	chart.makeChart(filename + "." + extension);
	}
    	
	
	//dispatcher
	protected abstract BaseChart createChart(int pageNumber, Object...args);
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void setPath(String outputTmpPdfPath) {
		this.path = outputTmpPdfPath;
	}
}
