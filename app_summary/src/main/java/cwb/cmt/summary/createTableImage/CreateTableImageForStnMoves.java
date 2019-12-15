package cwb.cmt.summary.createTableImage;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.summary.model.StnMoves;
import cwb.cmt.summary.utils.Numbers;

@Service
public class CreateTableImageForStnMoves extends CreateTableImage {

	@Override
	protected BaseChart createChart(int pageNumber, Object... args) {
		
		boolean firstPage = (boolean)args[0];
		boolean lastPage = (boolean)args[1];
		int beginYear = (int)args[2];
		@SuppressWarnings("unchecked")
		List<StnMoves> list = (List<StnMoves>) args[3];
		
		Chart.setLicenseCode(CHARTDIRECTOR_LICENSECODE);

		XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);
		
		// title
		if(firstPage) { // first page, put title
			TextBox title = chart.addText(400, 80, "測 站 資 料 變 遷 紀 錄 一 覽 表" + "\n" + "("+ beginYear + "-" + String.valueOf(beginYear + 9) + ")", CHINESE_FONT, 14);
			title.setAlignment(Chart.Center);
			title.setBackground(0xffffffff);
		} else { // other page, put "續 Continue"
			TextBox continued = chart.addText(350, 50,"（續 Continue）");
			continued.setFontStyle(CHINESE_FONT);
			continued.setFontSize(9);
		}
		
		int numX = 10;
		int numY = 1 + Numbers.ROWS_PER_PAGE_STNMOVES.getNumber(); // 1 : column title
		
		// Create a CDMLTable 參數分別是 x, y, 預設位置(0~12), 欄數, 列數
		CDMLTable table = chart.addTable(45, 120, 0, numX, numY);
		TextBox style = table.getStyle();
		style.setMargin(0, 0, 2, 2); // 調整格子內字到邊框的距離(左,右,上,下)
		style.setFontStyle(CHINESE_FONT); 
		style.setFontSize(8); 

		
		// add column title
		table.setCell(0, 0, 1, 1, "站號\nStation\nNo.");
		table.setCell(1, 0, 1, 1, "站名\nStation\nName");
		table.setCell(2, 0, 1, 1, "年份\nYear");
		table.setCell(3, 0, 1, 1, "北緯\nNorth\nLatitude");
		table.setCell(4, 0, 1, 1, "東經\nEast\nLongitude");
		table.setCell(5, 0, 1, 1, "氣壓計海面\n上高度\nHeight of\nBarometer\nabove Sea\nLevel (m)");
		table.setCell(6, 0, 1, 1, "溫度計地面\n高度\nHeight of\nTherm. above\nGround (m)");
		table.setCell(7, 0, 1, 1, "雨量器口\n地上高度\nHeight of\nRaingauge\nabove Ground\n(m)");
		table.setCell(8, 0, 1, 1, "風速儀地\n上高度\nAnem.\nabove\nGround (m)");
		table.setCell(9, 0, 1, 1, "海拔\nAltitude\n(m)");

		// set width for each column
		table.getColStyle(0).setWidth(50); // 站號
		table.getColStyle(1).setWidth(90); // 站名
		table.getColStyle(2).setWidth(95); // 年份
		table.getColStyle(3).setWidth(75); // 北緯
		table.getColStyle(4).setWidth(75); // 東經
		table.getColStyle(5).setWidth(75); // 氣壓計海面上高度
		table.getColStyle(6).setWidth(75); // 溫度計地面高度
		table.getColStyle(7).setWidth(75); // 雨量器口地上高度
		table.getColStyle(8).setWidth(70); // 風速儀地上高度
		table.getColStyle(9).setWidth(50); // 海拔

		
		String stno = list.get(0).getStno();
		int count = 0;
		// put data
		for(int j=0; j<list.size(); j++) {
			StnMoves item = list.get(j);
			table.setCell(0, j+1, 1, 1, item.getStno()); // 站號
			table.setCell(1, j+1, 1, 1, item.getStnCName() + "\n" + item.getStnEName()); // 站名
			table.setCell(2, j+1, 1, 1, getDuration(item.getBeginTime(), item.getEndTime())); // 年分
			table.setCell(3, j+1, 1, 1, item.getLatitude().substring(0, item.getLatitude().length()-1)); // 北緯
			table.setCell(4, j+1, 1, 1, item.getLongitude().substring(0, item.getLongitude().length()-1)); // 東經
			table.setCell(5, j+1, 1, 1, item.gethBarometer().substring(0, item.gethBarometer().length()-1)); // 氣壓計海平面上高度
			table.setCell(6, j+1, 1, 1, item.gethTherm().substring(0, item.gethTherm().length()-1)); // 溫度計地面高度
			table.setCell(7, j+1, 1, 1, item.gethRaingauge().substring(0, item.gethRaingauge().length()-1)); // 雨量器口地上高度
			table.setCell(8, j+1, 1, 1, item.gethAnem().substring(0, item.gethAnem().length()-1)); // 風速儀地上高度 
			table.setCell(9, j+1, 1, 1, item.getAltitude().substring(0, item.getAltitude().length()-1)); // 海拔
			
			// same Stno rows have same background color
			if(!stno.equals(item.getStno())) {
				count++;
			}
			
			if(count%2==0) {
				table.getRowStyle(j+1).setBackground(0xf3f3f3, 1); // grey background color
			} 
			
			stno = item.getStno();
		}
		
		if(lastPage) {
			TextBox note = chart.addText(45, table.getHeight()+120+5,   // 45 : offset from the left edge; 120 : offset of this's page's title
					"註1：括號內數值是地面自動測報系統感應器之離地高度"
					+ "\nNote 1：The numbers in parentheses show the height of automated surface observation system's sensor above ground.");
			note.setFontStyle(CHINESE_FONT);
			note.setFontSize(7);
		}
		
		return chart;
	}
	
	private String getDuration(Date beginTime, Date endTime) {
		StringBuilder beginS = new StringBuilder();
		beginS.append(beginTime.toLocalDate().getYear()).append(".").append(beginTime.toLocalDate().getMonthValue());
		StringBuilder endS = new StringBuilder();
		endS.append(endTime.toLocalDate().getYear()).append(".").append(endTime.toLocalDate().getMonthValue());
		return beginS.toString() + "-" + endS.toString();
	}
	
	public void createTableImage(int pageNumber, Object... args) {
		super.createTableImage(pageNumber, args);
	}
	
}