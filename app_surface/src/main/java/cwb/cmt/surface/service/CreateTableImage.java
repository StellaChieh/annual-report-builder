package cwb.cmt.surface.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import ChartDirector.BaseChart;

public abstract class CreateTableImage {

	protected String path;
	protected String filename;
	protected final int ChartMarginTop = 45;    
	protected final int CHART_WIDTH = 2100;
	protected final int CHART_HEIGHT = 2970 - ChartMarginTop;
	protected final int TABLE_MARGIN_TOP  = 195;
	
    // Align mode
    protected static final int TABLE_CELL_ALIGNMENT_LEFT_BOTTOM = 1;
    protected static final int TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_BOTTOM = 2;
    protected static final int TABLE_CELL_ALIGNMENT_RIGHT_BOTTOM = 3;
        
    protected static final int TABLE_CELL_ALIGNMENT_LEFT_CENTER_VERTICAL = 4;
    protected static final int TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_VERTICAL = 5;
    protected static final int TABLE_CELL_ALIGNMENT_RIGHT_CENTER_VERTICAL = 6;
    
    protected static final int TABLE_CELL_ALIGNMENT_LEFT_TOP = 7;
    protected static final int TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_TOP = 8;
    protected static final int TABLE_CELL_ALIGNMENT_RIGHT_TOP = 9;
    
    public void createTableImage(Object... objects) {
		// create chart with given data
		BaseChart chart = createChart(objects);
		makeChart(chart, Paths.get(path, filename).toString(), "pdf");
//		makeChart(chart, Paths.get(path, filename).toString(), "svg");
		System.out.println("-----makechart over-----");
	}
	
    protected void makeChart(BaseChart chart, String filename, String extension) {
    	// down size the pdf to normal size
//    	chart.setOutputOptions("width=1654; height = 2339; dpi=200");
    	chart.setOutputOptions("width=595; height = 842; dpi=200; topy=" + 10);
    	chart.makeChart(filename + "." + extension);
	}
    
    //dispatcher
  	protected abstract BaseChart createChart(Object...args);
    /*
     * getter and setter
     */
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
