package cwb.cmt.surface.service;

import ChartDirector.BaseChart;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import static cwb.cmt.surface.utils.NumberConvert.insertSpaceWithinCharacters;
import static cwb.cmt.surface.utils.NumberConvert.repeatChar;

import java.io.File;

public abstract class CreateTableImageIndexPage extends cwb.cmt.surface.tools.CreateTableImageAbstract  {
	
	 public CreateTableImageIndexPage() {
	        super();
	    }
	    
	    public CreateTableImageIndexPage(String path) {
	        super(path);
	    }
	    
	    public CreateTableImageIndexPage(String path, String filename) {
	        super(path, filename);
	    }
		
//		public boolean getDisplayPageNumber() {
//			return AppConfig.getConfig().getDisplayPageNumber();
//		}
		
		protected BaseChart makeChart(BaseChart chart, String filename, String extension) {
			//if (!AppConfig.getConfig().generatesPdf()) {
			//	return super.makeChart(chart, filename, "bmp");	
			//}
			chart.setOutputOptions("dpi=72; width=595; height=842; topy=" + 10);
//			return super.makeChart(chart, filename, "svg");
			return super.makeChart(chart, filename, "pdf");
		}
	    
		public static class DisplayAttrInterleaf {
		    
		    public int textSize = 90;
	        
		    public int spaces = 2;
		    
		    public DisplayAttrInterleaf() {
		        
		    }
		    
		    public DisplayAttrInterleaf(int textSize, int spaces) {
		        this.textSize = textSize;
		        this.spaces = spaces;
		    }

	        public int getTextSize() {
	            return textSize;
	        }

	        public void setTextSize(int textSize) {
	            this.textSize = textSize;
	        }

	        public int getSpaces() {
	            return spaces;
	        }

	        public void setSpaces(int spaces) {
	            this.spaces = spaces;
	        }
		}
		
		private static final DisplayAttrInterleaf DefaultDisplayAttrInterleaf = new DisplayAttrInterleaf();
		
		public static void createInterleaf(int chapter, String titleChinese, String titleEnglish) {
		    createInterleaf(chapter, titleChinese, titleEnglish, DefaultDisplayAttrInterleaf);
		}
		
	    public static void createInterleaf(int chapter, String titleChinese, String titleEnglish, DisplayAttrInterleaf attr) {
	        
	        Chart.setLicenseCode("DEVP-2LPQ-D6YH-UTLK-EBC0-3E26");
	        
	        // Create a XYChart pro of A4 size
	        XYChart chart = new XYChart(
	                2100,
	                2970
	        );
	        
	        // Add a title to the chart using 18 pts Times Bold Italic font
	        TextBox text_title = chart.addTitle(
	                 chapter + ".",
	                 "kaiu.ttf",
	                 attr.textSize
	        );
	        text_title.setPos(text_title.getLeftX(), (chart.getHeight() - text_title.getHeight())/2 - 290);
	        
	        // Title (chinese)
	        TextBox text_titleChinese = chart.addTitle(
	                insertSpaceWithinCharacters(titleChinese, repeatChar(' ', attr.spaces)),
	                "kaiu.ttf",
	                attr.textSize
	        );
	        text_titleChinese.setPos(text_titleChinese.getLeftX(), text_title.getTopY() + text_title.getHeight() + 35);
	        
	        // Title (english)
	        TextBox text_titleEnglish = chart.addTitle(
	                titleEnglish,
	                "kaiu.ttf",
	                attr.textSize
	        );
	        text_titleEnglish.setPos(text_titleEnglish.getLeftX(), text_titleChinese.getTopY() + text_titleChinese.getHeight() + 35);
	        
	        // perform chart producing
	        String getPdfOutputPath = "C:\\Users\\YuPing ho\\CMTworkspace\\annual-report\\tmp\\tmp_surface";
	        chart.setOutputOptions("dpi=72; width=595; height=842; topy=" + 10);
	        chart.makeChart(getPdfOutputPath + File.separator +
	                        (chapter + "_0" + "_" + titleChinese + "_" + titleEnglish).replace("\n", " ") + "." + "svg");
	    }
}
