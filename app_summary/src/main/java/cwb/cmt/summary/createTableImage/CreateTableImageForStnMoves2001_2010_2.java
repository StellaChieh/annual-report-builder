package cwb.cmt.summary.createTableImage;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;

@Service
public class CreateTableImageForStnMoves2001_2010_2 extends CreateTableImage {
		
	@Override
	protected BaseChart createChart(int pageNumber, Object... args) {
		Chart.setLicenseCode(CHARTDIRECTOR_LICENSECODE);
		
		XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
		
		// Create a CDMLTable 參數分別是 x, y, 預設位置(0~12), 欄數, 列數
		CDMLTable table = chart.addTable(60, 70, 0, 20, 13);// tableArray.length + 500 580
		
		//setCell(col, row, width, height, text)
		//欄位名共10個
		table.setCell(0, 0, 1, 2, "站號\nStation\nNo.");
		table.setCell(2, 0, 1, 2, "站名\nStation\nName");
		table.setCell(4, 0, 1, 2, "年份\nYear");
		table.setCell(6, 0, 1, 2, "北緯\nNorth\nLatitude");
		table.setCell(8, 0, 1, 2, "東經\nEast\nLongitude");
		table.setCell(10, 0, 1, 2, "氣壓計海面\n上高度\nHeight of\nBarometer\nabove Sea\nLevel (m)");
		table.setCell(12, 0, 1, 2, "溫度計地面\n高度\nHeight of\nTherm. above\nGround (m)");
		table.setCell(14, 0, 1, 2, "雨量器口\n地上高度\nHeight of\nRaingauge\nabove Ground\n(m)");
		table.setCell(16, 0, 1, 2, "風速儀地\n上高度\nAnem.\nabove\nGround\n(m)");
		table.setCell(18, 0, 1, 2, "海拔\nAltitude\n(m)");
		
		TextBox textbox = chart.addText(350, 50,"（續 Continue）");
		textbox.setFontStyle(CHINESE_FONT);
		textbox.setFontSize(9);
		
		
		//站號共5個
		table.setCell(0, 2, 1, 1, "467410");
		table.setCell(0, 3, 1, 1, "467410");
		table.setCell(0, 4, 1, 1, "467410");
		table.setCell(0, 5, 1, 1, "467420");
		table.setCell(0, 6, 1, 1, "467420");
		table.setCell(0, 7, 1, 1, "467420");
		table.setCell(0, 8, 1, 1, "467660");
		table.setCell(0, 9, 1, 1, "467440");
		table.setCell(0, 10, 1, 1, "467540");
		table.setCell(0, 11, 1, 1, "467620");
		table.setCell(0, 12, 1, 1, "467590");
		
		//站名共5個
		table.setCell(2, 2, 1, 1, "臺南\nTainan");
		table.setCell(2, 3, 1, 1, "臺南\nTainan");
		table.setCell(2, 4, 1, 1, "臺南\nTainan");
		table.setCell(2, 5, 1, 1, "永康\nYungkang");	
		table.setCell(2, 6, 1, 1, "永康\nYungkang");	
		table.setCell(2, 7, 1, 1, "永康\nYungkang");	
		table.setCell(2, 8, 1, 1, "臺東\nTaitung");
		table.setCell(2, 9, 1, 1, "高雄\nKaohsiung");
		table.setCell(2, 10, 1, 1, "大武\nTawu");	
		table.setCell(2, 11, 1, 1, "蘭嶼\nLanyu");
		table.setCell(2, 12, 1, 1, "恆春\nHengchun");
		
		//年份共9個
		table.setCell(4, 2, 1, 1, "2002-2003");
		table.setCell(4, 3, 1, 1, "2004-2006");
		table.setCell(4, 4, 1, 1, "2007-2010");
		table.setCell(4, 5, 1, 1, "2002-2005");
		table.setCell(4, 6, 1, 1, "2006");
		table.setCell(4, 7, 1, 1, "2007-2010");
		table.setCell(4, 8, 1, 1, "2001-2010");
		table.setCell(4, 9, 1, 1, "2001-2010");
		table.setCell(4, 10, 1, 1, "2001-2010");
		table.setCell(4, 11, 1, 1, "2001-2010");
		table.setCell(4, 12, 1, 1, "2001-2010");

		
		//北緯共5個
		table.setCell(6, 2, 1, 1, "  23°00＇");
		table.setCell(6, 3, 1, 1, "  23°00＇");
		table.setCell(6, 4, 1, 1, "  23°00＇");
		table.setCell(6, 5, 1, 1, "  23°02＇");	
		table.setCell(6, 6, 1, 1, "  23°02＇");	
		table.setCell(6, 7, 1, 1, "  23°02＇");	
		table.setCell(6, 8, 1, 1, "  22°45＇");
		table.setCell(6, 9, 1, 1, "  22°34＇");
		table.setCell(6, 10, 1, 1, "  22°21＇");
		table.setCell(6, 11, 1, 1, "  22°02＇");
		table.setCell(6, 12, 1, 1, "  22°00＇");
		
		//東經共5個
		table.setCell(8, 2, 1, 1, "  120°12＇");
		table.setCell(8, 3, 1, 1, "  120°12＇");
		table.setCell(8, 4, 1, 1, "  120°12＇");
		table.setCell(8, 5, 1, 1, "  120°14＇");	
		table.setCell(8, 6, 1, 1, "  120°14＇");	
		table.setCell(8, 7, 1, 1, "  120°14＇");	
		table.setCell(8, 8, 1, 1, "  121°09＇");
		table.setCell(8, 9, 1, 1, "  120°18＇");
		table.setCell(8, 10, 1, 1, "  120°54＇");			
		table.setCell(8, 11, 1, 1, "  121°33＇");
		table.setCell(8, 12, 1, 1, "  120°44＇");

		
		//氣壓計海面上高度共5個
		table.setCell(10, 2, 1, 1, "14.7");
		table.setCell(10, 3, 1, 1, "14.7");
		table.setCell(10, 4, 1, 1, "22.1");
		table.setCell(10, 5, 1, 1, "9.6");
		table.setCell(10, 6, 1, 1, "9.6");
		table.setCell(10, 7, 1, 1, "9.6");
		table.setCell(10, 8, 1, 1, "9.7");
		table.setCell(10, 9, 1, 1, "3.1");
		table.setCell(10, 10, 1, 1, "8.9");
		table.setCell(10, 11, 1, 1, "325.1");
		table.setCell(10, 12, 1, 1, "24.1");

		
		//溫度計地面高度共7個
		table.setCell(12, 2, 1, 1, "1.5(1.5)");
		table.setCell(12, 3, 1, 1, "1.5(1.5)");
		table.setCell(12, 4, 1, 1, "1.5(1.5)");
		table.setCell(12, 5, 1, 1, "1.5");
		table.setCell(12, 6, 1, 1, "1.5");
		table.setCell(12, 7, 1, 1, "1.5");
		table.setCell(12, 8, 1, 1, "1.4(1.5)");
		table.setCell(12, 9, 1, 1, "1.2(1.5)");
		table.setCell(12, 10, 1, 1, "1.2(1.5)");
		table.setCell(12, 11, 1, 1, "1.5(1.42)");
		table.setCell(12, 12, 1, 1, "1.5(1.8)");
		
		//雨量器口地上高度共6個
		table.setCell(14, 2, 1, 1, "0.2(0.6)");
		table.setCell(14, 3, 1, 1, "0.2(0.6)");
		table.setCell(14, 4, 1, 1, "(1.5)");
		table.setCell(14, 5, 1, 1, "1.2(0.3)");
		table.setCell(14, 6, 1, 1, "(0.3)");
		table.setCell(14, 7, 1, 1, "(0.5)");
		table.setCell(14, 8, 1, 1, "0.2(0.9)");
		table.setCell(14, 9, 1, 1, "0.2(0.7)");	
		table.setCell(14, 10, 1, 1, "0.2(0.7)");	
		table.setCell(14, 11, 1, 1, "0.2(0.5)");
		table.setCell(14, 12, 1, 1, "0.2(0.8)");
		
		//風速儀地上高度共5個
		table.setCell(16, 2, 1, 1, "36.6");
		table.setCell(16, 3, 1, 1, "36.6");
		table.setCell(16, 4, 1, 1, "53.7");
		table.setCell(16, 5, 1, 1, "37.6");
		table.setCell(16, 6, 1, 1, "37.6");
		table.setCell(16, 7, 1, 1, "37.6");
		table.setCell(16, 8, 1, 1, "11.4");
		table.setCell(16, 9, 1, 1, "14.0");		
		table.setCell(16, 10, 1, 1, "12.7");		
		table.setCell(16, 11, 1, 1, "12.5");	
		table.setCell(16, 12, 1, 1, "14.3");	
		
		//海拔共7個
		table.setCell(18, 2, 1, 1, "13.8");
		table.setCell(18, 3, 1, 1, "40.8");
		table.setCell(18, 4, 1, 1, "40.8");
		table.setCell(18, 5, 1, 1, "8.1");
		table.setCell(18, 6, 1, 1, "8.1");
		table.setCell(18, 7, 1, 1, "8.1");
		table.setCell(18, 8, 1, 1, "9.0");
		table.setCell(18, 9, 1, 1, "2.3");
		table.setCell(18, 10, 1, 1, "8.1");
		table.setCell(18, 11, 1, 1, "324.0");
		table.setCell(18, 12, 1, 1, "22.1");		

		TextBox cellStyle = table.getStyle();
		cellStyle.setMargin(6, 6, 3, 3); // 調整格子內字到邊框的距離(左,右,上,下)
		cellStyle.setFontStyle(CHINESE_FONT); // 微軟正黑粗體
		cellStyle.setFontSize(8);// (28); //字體大小
		
		table.getRowStyle(2).setBackground(0xf3f3f3, 1);
		table.getRowStyle(3).setBackground(0xf3f3f3, 1);
		table.getRowStyle(4).setBackground(0xf3f3f3, 1);
		table.getRowStyle(8).setBackground(0xf3f3f3, 1);
		table.getRowStyle(10).setBackground(0xf3f3f3, 1);
		table.getRowStyle(12).setBackground(0xf3f3f3, 1);

		TextBox note = chart.addText(60, 70+table.getHeight()+5,
				"註1：括號內數值是地面自動測報系統感應器之離地高度"
				+ "\nNote 1：The numbers in parentheses show the height of automated surface observation system's sensor above ground.");
		note.setFontStyle(CHINESE_FONT);
		note.setFontSize(7);
		return chart;
	}
	
	
	public void createTableImage(int pageNumber, Object... args) {
		super.createTableImage(pageNumber, args);
	}

}
