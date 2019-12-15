package cwb.cmt.summary.createTableImage;

import java.util.List;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.summary.model.Stations.Station;

@Service
public class CreateTableImageForStns extends CreateTableImage{

	private final int ChartWidth = 794;
	private final int ChartHeight = 1123;
	private final int TableXPosition = 60;
	private final int TableYPosition = 120;
	private final int NumberOfStation = 28;
	private final int NumberOfItem = 11;
	private final int TitleTextSize = 11;
	private final String TextStyle = "kaiu.ttf"; //標楷體

	@Override
	public BaseChart createChart(int pageNumber, Object... args) {
		
		@SuppressWarnings("unchecked")
		List<Station> station = (List<Station>) args[0];
		
		Chart.setLicenseCode(CHARTDIRECTOR_LICENSECODE);

		// Create a XYChart object of size 600 x 400 pixels
		XYChart chart = new XYChart(ChartWidth, ChartHeight);

		TextBox title = chart.addText(400, 80, "中央氣象局所屬各氣象站一覽表" + "\n"
				+ "List of Stations of Central Weather Bureau", TextStyle,
				TitleTextSize);

		title.setAlignment(Chart.Center);
		title.setBackground(0xffffffff);

		// Create a CDMLTable 參數分別是 x, y, 預設位置(0~12), 欄數, 列數
		CDMLTable table = chart.addTable(TableXPosition, TableYPosition, 0, NumberOfItem * 2,
				NumberOfStation);

		// 欄位名共10個
		table.setCell(0, 0, 1, 1, "站號\nStation\nNo.");
		table.setCell(1, 0, 1, 1, "測站名稱\nStation\nName");
		table.setCell(2, 0, 1, 1, "北緯\nNorth Latitude");
		table.setCell(3, 0, 1, 1, "東經\nEast Longitude");
		table.setCell(4, 0, 1, 1,
				"氣壓計\n海面上高度\n公尺\nHeight of\nBarometer above\nSea Level (m)");
		table.setCell(5, 0, 1, 1,
				"溫度計\n地面高度\n公尺\nHeight of\nTherm. above\nGround (m)");

		table.setCell(6, 0, 1, 1,
				"雨量器口\n地上高度\n公尺\nHeight of\nRaingauge\nabove\nGround (m)");
		table.setCell(7, 0, 1, 1, "風速儀\n地上高度\n公尺\nAnem.\nabove\nGround (m)");
		table.setCell(8, 0, 1, 1, "海拔\n公尺\nAltitude\n(m)");
		table.setCell(9, 0, 1, 1, "創立年份\nYear of\nCommen\nCement");
		table.setCell(10, 0, 1, 1, "每日觀測次數\nNo. of\nObs. Per\nDay");

		int count = 1;
		for(Station obj:station){
			count ++;
			table.setCell(0, count, 1, 1, obj.getStno().trim());
			table.setCell(1, count, 1, 1, obj.getStnCName().trim()+"\n"+obj.getStnEName().trim());
			table.setCell(2, count, 1, 1, " "+obj.getLatitude().trim().replace("N", ""));
			table.setCell(3, count, 1, 1, " "+obj.getLongitude().trim().replace("E", ""));
			table.setCell(4, count, 1, 1, obj.gethBarometer().trim().replace("m", ""));
			table.setCell(5, count, 1, 1, obj.gethTherm().trim().replace("m", ""));
			table.setCell(6, count, 1, 1, obj.gethRaingauge().trim().replace("m", ""));
			table.setCell(7, count, 1, 1, obj.gethAnem().trim().replace("m", ""));
			table.setCell(8, count, 1, 1, obj.getAltitude().trim().replace("m", ""));
			table.setCell(9, count, 1, 1, obj.getStnBeginTime().trim().substring(0,4));
			table.setCell(10, count, 1, 1, obj.getManObsNum().trim());
		}
		
		TextBox cellStyle = table.getStyle();
		cellStyle.setMargin(3, 3, 3, 3); // 調整格子內字到邊框的距離(左,右,上,下)
		cellStyle.setFontStyle(TextStyle); 
		cellStyle.setFontSize(7); //字體大小

		TextBox textbox = chart.addText(TableXPosition, TableYPosition+table.getHeight()+5,
				"註1：括號內數值是地面自動測報系統感應器之離地高度"
				+ "\nNote 1：The numbers in parentheses show the height of automated surface observation system's sensor above ground.");
		textbox.setFontStyle(TextStyle);
		textbox.setFontSize(7);
		
		return chart;
	}
	
	@Override
	public void createTableImage(int pageNumber, Object... args) {
		super.createTableImage(pageNumber, args);
	}
}
