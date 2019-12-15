package cwb.cmt.summary.createTableImage;

import org.springframework.stereotype.Service;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;

@Service
public class CreateTableImageForStnMoves1971_1980_1 extends CreateTableImage {

	private static final String INPUT_YEAR = "1971-1980"; //輸入年份
	
	@Override
	protected BaseChart createChart(int pageNumber, Object... args) {
		Chart.setLicenseCode(CHARTDIRECTOR_LICENSECODE);

		// Create a XYChart object of size 600 x 400 pixels
		XYChart chart = new XYChart(CHART_WIDTH, CHART_HEIGHT);

		// Add a title to the chart using 18 pts Times Bold Italic font
		TextBox title = chart.addText(400, 80, "測 站 資 料 變 遷 紀 錄 一 覽 表" + "\n" + "("+INPUT_YEAR+")", CHINESE_FONT, 14);

		title.setAlignment(Chart.Center);
		title.setBackground(0xffffffff);

		// Create a CDMLTable 參數分別是 x, y, 預設位置(0~12), 欄數, 列數
		CDMLTable table = chart.addTable(60, 120, 0, 20, 31);
		
		//欄位名共10個
		table.setCell(0, 0, 1, 2, "站號\nStation\nNo.");
		table.setCell(2, 0, 1, 2, "站名\nStation\nName");
		table.setCell(4, 0, 1, 2, "年份\nYear");
		table.setCell(6, 0, 1, 2, "北緯\nNorth\nLatitude");
		table.setCell(8, 0, 1, 2, "東經\nEast\nLongitude");
		table.setCell(10, 0, 1, 2, "氣壓計海面\n上高度\nHeight of\nBarometer\nabove Sea\nLevel (m)");
		table.setCell(12, 0, 1, 2, "溫度計地面\n高度\nHeight of\nTherm. above\nGround (m)");
		table.setCell(14, 0, 1, 2, "雨量器口\n地上高度\nHeight of\nRaingauge\nabove Ground\n(m)");
		table.setCell(16, 0, 1, 2, "風速儀地\n上高度\nAnem.\nabove\nGround (m)");
		table.setCell(18, 0, 1, 2, "海拔\nAltitude\n(m)");
		
		//站號共23個
		table.setCell(0, 2, 1, 1, "466950");
		table.setCell(0, 3, 1, 1, "466950");
		table.setCell(0, 4, 1, 1, "466910");
		table.setCell(0, 5, 1, 1, "466910");
		table.setCell(0, 6, 1, 1, "466930");
		table.setCell(0, 7, 1, 1, "466930");
		table.setCell(0, 8, 1, 1, "466900");
		table.setCell(0, 9, 1, 1, "466900");
		table.setCell(0, 10, 1, 1, "466940");
		table.setCell(0, 11, 1, 1, "466940");
		table.setCell(0, 12, 1, 1, "466920");
		table.setCell(0, 13, 1, 1, "467570");
		table.setCell(0, 14, 1, 1, "467570");
		table.setCell(0, 15, 1, 1, "467080");
		table.setCell(0, 16, 1, 1, "467490");
		table.setCell(0, 17, 1, 1, "467770");
		table.setCell(0, 18, 1, 1, "466990");
		table.setCell(0, 19, 1, 1, "467650");
		table.setCell(0, 20, 1, 1, "467350");
		table.setCell(0, 21, 1, 1, "467530");
		table.setCell(0, 22, 1, 1, "467480");
		table.setCell(0, 23, 1, 1, "467550");
		table.setCell(0, 24, 1, 1, "467300");
		table.setCell(0, 25, 1, 1, "467300");
		table.setCell(0, 26, 1, 1, "467610");
		table.setCell(0, 27, 1, 1, "467610");
		table.setCell(0, 28, 1, 1, "467420");
		table.setCell(0, 29, 1, 1, "467410");
		
	
		//站名共23個
		table.setCell(2, 2, 1, 1, "彭佳嶼\nPengchiayu"); //2
		table.setCell(2, 3, 1, 1, "彭佳嶼\nPengchiayu"); //3
		table.setCell(2, 4, 1, 1, "鞍部\nAnpu");
		table.setCell(2, 5, 1, 1, "鞍部\nAnpu");
		table.setCell(2, 6, 1, 1, "竹子湖\nChutzehu"); //6
		table.setCell(2, 7, 1, 1, "竹子湖\nChutzehu"); //7
		table.setCell(2, 8, 1, 1, "淡水\nTanshu");
		table.setCell(2, 9, 1, 1, "淡水\nTanshu");
		table.setCell(2, 10, 1, 1, "基隆\nKeelung"); //10
		table.setCell(2, 11, 1, 1, "基隆\nKeelung"); //11
		table.setCell(2, 12, 1, 1, "臺北\nTaipei");
		table.setCell(2, 13, 1, 1, "新竹\nHsinchu"); //13
		table.setCell(2, 14, 1, 1, "新竹\nHsinchu"); //14
		table.setCell(2, 15, 1, 1, "宜蘭\nIlan");
		table.setCell(2, 16, 1, 1, "臺中\nTaichung"); //16
		table.setCell(2, 17, 1, 1, "梧棲\nWuchi");
		table.setCell(2, 18, 1, 1, "花蓮\nHaulien"); //18
		table.setCell(2, 19, 1, 1, "日月潭\nJiyuehtan");
		table.setCell(2, 20, 1, 1, "澎湖\nPenghu"); //20
		table.setCell(2, 21, 1, 1, "阿里山\nAlishan");
		table.setCell(2, 22, 1, 1, "嘉義\nChiayi"); //22
		table.setCell(2, 23, 1, 1, "玉山\nYushan");
		table.setCell(2, 24, 1, 1, "東吉島\nTungchitao"); //24
		table.setCell(2, 25, 1, 1, "東吉島\nTungchitao"); //25
		table.setCell(2, 26, 1, 1, "新港\nHsinkang");
		table.setCell(2, 27, 1, 1, "新港\nHsinkang");
		table.setCell(2, 28, 1, 1, "永康\nYungkang"); //28
		table.setCell(2, 29, 1, 1, "臺南\nTainan");
		
		
		//年份共23個
		table.setCell(4, 2, 1, 1, "1971-1975");
		table.setCell(4, 3, 1, 1, "1976-1980");
		
		table.setCell(4, 4, 1, 1, "1971-1975");
		table.setCell(4, 5, 1, 1, "1976-1980");
		
		table.setCell(4, 6, 1, 1, "1971-1975");
		table.setCell(4, 7, 1, 1, "1976-1980");

		table.setCell(4, 8, 1, 1, "1971-1975");
		table.setCell(4, 9, 1, 1, "1976-1980");

		table.setCell(4, 10, 1, 1, "1971-1975");
		table.setCell(4, 11, 1, 1, "1976-1980");

		table.setCell(4, 12, 1, 1, "1971-1980");
		
		table.setCell(4, 13, 1, 1, "1971-1979");
		table.setCell(4, 14, 1, 1, "1980");

		table.setCell(4, 15, 1, 1, "1971-1980");		
		table.setCell(4, 16, 1, 1, "1971-1980");
		table.setCell(4, 17, 1, 1, "1976-1980");
		table.setCell(4, 18, 1, 1, "1971-1980");
		table.setCell(4, 19, 1, 1, "1971-1980");
		table.setCell(4, 20, 1, 1, "1971-1980");
		table.setCell(4, 21, 1, 1, "1971-1980");
		table.setCell(4, 22, 1, 1, "1971-1980");
		table.setCell(4, 23, 1, 1, "1971-1980");
		
		table.setCell(4, 24, 1, 1, "1971-1975");
		table.setCell(4, 25, 1, 1, "1976-1980");

		table.setCell(4, 26, 1, 1, "1971-1975");
		table.setCell(4, 27, 1, 1, "1976-1980");

		table.setCell(4, 28, 1, 1, "1971-1974");
		table.setCell(4, 29, 1, 1, "1971-1980");	
		
		
		//北緯共23個
		table.setCell(6, 2, 1, 1, "  25°28＇");	
		table.setCell(6, 3, 1, 1, "  25°38＇");
		table.setCell(6, 4, 1, 1, "  25°11＇");
		table.setCell(6, 5, 1, 1, "  25°11＇");
		table.setCell(6, 6, 1, 1, "  25°10＇");
		table.setCell(6, 7, 1, 1, "  25°10＇");
		table.setCell(6, 8, 1, 1, "  25°10＇");
		table.setCell(6, 9, 1, 1, "  25°10＇");
		table.setCell(6, 10, 1, 1, "  25°08＇");
		table.setCell(6, 11, 1, 1, "  25°08＇");
		table.setCell(6, 12, 1, 1, "  25°02＇");
		table.setCell(6, 13, 1, 1, "  24°48＇");
		table.setCell(6, 14, 1, 1, "  24°48＇");
		table.setCell(6, 15, 1, 1, "  24°46＇");
		table.setCell(6, 16, 1, 1, "  24°09＇");
		table.setCell(6, 17, 1, 1, "  24°15＇");
		table.setCell(6, 18, 1, 1, "  23°58＇");
		table.setCell(6, 19, 1, 1, "  23°53＇");
		table.setCell(6, 20, 1, 1, "  23°32＇");
		table.setCell(6, 21, 1, 1, "  23°31＇");
		table.setCell(6, 22, 1, 1, "  23°30＇");
		table.setCell(6, 23, 1, 1, "  23°29＇");
		table.setCell(6, 24, 1, 1, "  23°16＇");
		table.setCell(6, 25, 1, 1, "  23°16＇");
		table.setCell(6, 26, 1, 1, "  23°06＇");
		table.setCell(6, 27, 1, 1, "  23°06＇");
		table.setCell(6, 28, 1, 1, "  23°01＇");
		table.setCell(6, 29, 1, 1, "  23°00＇");	

		//東經共23個
		table.setCell(8, 2, 1, 1, "  122°04＇");
		table.setCell(8, 3, 1, 1, "  122°04＇");
		table.setCell(8, 4, 1, 1, "  121°31＇");
		table.setCell(8, 5, 1, 1, "  121°31＇");
		table.setCell(8, 6, 1, 1, "  121°32＇");
		table.setCell(8, 7, 1, 1, "  121°32＇");
		table.setCell(8, 8, 1, 1, "  121°26＇");
		table.setCell(8, 9, 1, 1, "  121°26＇");
		table.setCell(8, 10, 1, 1, "  121°45＇");
		table.setCell(8, 11, 1, 1, "  121°45＇");
		table.setCell(8, 12, 1, 1, "  121°31＇");
		table.setCell(8, 13, 1, 1, "  120°58＇");
		table.setCell(8, 14, 1, 1, "  120°58＇");
		table.setCell(8, 15, 1, 1, "  121°45＇");
		table.setCell(8, 16, 1, 1, "  120°41＇");
		table.setCell(8, 17, 1, 1, "  120°31＇");
		table.setCell(8, 18, 1, 1, "  121°37＇");
		table.setCell(8, 19, 1, 1, "  120°51＇");
		table.setCell(8, 20, 1, 1, "  119°33＇");
		table.setCell(8, 21, 1, 1, "  120°48＇");
		table.setCell(8, 22, 1, 1, "  120°25＇");
		table.setCell(8, 23, 1, 1, "  120°57＇");
		table.setCell(8, 24, 1, 1, "  119°40＇");
		table.setCell(8, 25, 1, 1, "  119°40＇");
		table.setCell(8, 26, 1, 1, "  121°22＇");
		table.setCell(8, 27, 1, 1, "  121°22＇");
		table.setCell(8, 28, 1, 1, "  120°13＇");
		table.setCell(8, 29, 1, 1, "  120°13＇");	
		
		
		//氣壓計海面上高度共23個
		table.setCell(10, 2, 1, 1, "101.9");
		table.setCell(10, 3, 1, 1, "101.9");
		table.setCell(10, 4, 1, 1, "836.7");
		table.setCell(10, 5, 1, 1, "836.7");
		table.setCell(10, 6, 1, 1, "605.5");
		table.setCell(10, 7, 1, 1, "602.0");
		table.setCell(10, 8, 1, 1, "20.0");
		table.setCell(10, 9, 1, 1, "23.0");
		table.setCell(10, 10, 1, 1, "4.8");
		table.setCell(10, 11, 1, 1, "32.0");
		table.setCell(10, 12, 1, 1, "9.3");
		table.setCell(10, 13, 1, 1, "34.1");
		table.setCell(10, 14, 1, 1, "34.2");
		table.setCell(10, 15, 1, 1, "8.6");
		table.setCell(10, 16, 1, 1, "84.9");
		table.setCell(10, 17, 1, 1, "13.6");
		table.setCell(10, 18, 1, 1, "19.2");
		table.setCell(10, 19, 1, 1, "1016.9");
		table.setCell(10, 20, 1, 1, "11.0");
		table.setCell(10, 21, 1, 1, "2408.3");
		table.setCell(10, 22, 1, 1, "27.8");
		table.setCell(10, 23, 1, 1, "3850.6");
		table.setCell(10, 24, 1, 1, "45.0");
		table.setCell(10, 25, 1, 1, "45.0");
		table.setCell(10, 26, 1, 1, "37.2");
		table.setCell(10, 27, 1, 1, "37.2");
		table.setCell(10, 28, 1, 1, "12.6");
		table.setCell(10, 29, 1, 1, "14.3");
		
		
		//溫度計地面高度共23個
		table.setCell(12, 2, 1, 1, "1.1");
		table.setCell(12, 3, 1, 1, "1.2");
		table.setCell(12, 4, 1, 1, "1.2");
		table.setCell(12, 5, 1, 1, "1.1");
		table.setCell(12, 6, 1, 1, "1.2");
		table.setCell(12, 7, 1, 1, "2.0");
		table.setCell(12, 8, 1, 1, "1.1");
		table.setCell(12, 9, 1, 1, "1.1");
		table.setCell(12, 10, 1, 1, "1.2");
		table.setCell(12, 11, 1, 1, "1.2");
		table.setCell(12, 12, 1, 1, "1.2");
		table.setCell(12, 13, 1, 1, "1.2");
		table.setCell(12, 14, 1, 1, "1.2");
		table.setCell(12, 15, 1, 1, "1.2");
		table.setCell(12, 16, 1, 1, "1.4");
		table.setCell(12, 17, 1, 1, "1.2");
		table.setCell(12, 18, 1, 1, "1.2");
		table.setCell(12, 19, 1, 1, "1.2");
		table.setCell(12, 20, 1, 1, "1.2");
		table.setCell(12, 21, 1, 1, "1.2");
		table.setCell(12, 22, 1, 1, "1.2");
		table.setCell(12, 23, 1, 1, "1.4");
		table.setCell(12, 24, 1, 1, "1.5");
		table.setCell(12, 25, 1, 1, "1.5");
		table.setCell(12, 26, 1, 1, "1.2");
		table.setCell(12, 27, 1, 1, "1.8");
		table.setCell(12, 28, 1, 1, "1.2");
		table.setCell(12, 29, 1, 1, "1.2");		

		
		//雨量器口地上高度共23個
		table.setCell(14, 2, 1, 1, "0.2");
		table.setCell(14, 3, 1, 1, "0.2");
		table.setCell(14, 4, 1, 1, "0.3");
		table.setCell(14, 5, 1, 1, "0.2");
		table.setCell(14, 6, 1, 1, "0.2");
		table.setCell(14, 7, 1, 1, "0.2");
		table.setCell(14, 8, 1, 1, "0.2");
		table.setCell(14, 9, 1, 1, "0.2");
		table.setCell(14, 10, 1, 1, "0.2");
		table.setCell(14, 11, 1, 1, "0.4");
		table.setCell(14, 12, 1, 1, "0.2");
		table.setCell(14, 13, 1, 1, "0.2");
		table.setCell(14, 14, 1, 1, "0.2");
		table.setCell(14, 15, 1, 1, "0.3");
		table.setCell(14, 16, 1, 1, "0.2");
		table.setCell(14, 17, 1, 1, "0.2");
		table.setCell(14, 18, 1, 1, "0.2");
		table.setCell(14, 19, 1, 1, "0.2");
		table.setCell(14, 20, 1, 1, "0.2");
		table.setCell(14, 21, 1, 1, "0.2");
		table.setCell(14, 22, 1, 1, "0.3");
		table.setCell(14, 23, 1, 1, "0.2");
		table.setCell(14, 24, 1, 1, "0.2");
		table.setCell(14, 25, 1, 1, "0.2");
		table.setCell(14, 26, 1, 1, "0.2");
		table.setCell(14, 27, 1, 1, "1.2");
		table.setCell(14, 28, 1, 1, "0.2");
		table.setCell(14, 29, 1, 1, "0.2");	
		
		
		//風速儀地上高度共23個
		table.setCell(16, 2, 1, 1, "7.2");
		table.setCell(16, 3, 1, 1, "7.2");
		table.setCell(16, 4, 1, 1, "8.3");
		table.setCell(16, 5, 1, 1, "8.3");
		table.setCell(16, 6, 1, 1, "9.0");
		table.setCell(16, 7, 1, 1, "9.0");
		table.setCell(16, 8, 1, 1, "12.2");
		table.setCell(16, 9, 1, 1, "12.2");
		table.setCell(16, 10, 1, 1, "11.0");
		table.setCell(16, 11, 1, 1, "34.6");		
		table.setCell(16, 12, 1, 1, "23.4");
		table.setCell(16, 13, 1, 1, "13.2");
		table.setCell(16, 14, 1, 1, "13.2");
		table.setCell(16, 15, 1, 1, "9.4");
		table.setCell(16, 16, 1, 1, "16.6");
		table.setCell(16, 17, 1, 1, "10.5");
		table.setCell(16, 18, 1, 1, "10.2");
		table.setCell(16, 19, 1, 1, "8.0");
		table.setCell(16, 20, 1, 1, "14.2");
		table.setCell(16, 21, 1, 1, "15.1");
		table.setCell(16, 22, 1, 1, "14.5");
		table.setCell(16, 23, 1, 1, "9.2");
		table.setCell(16, 24, 1, 1, "10.5");
		table.setCell(16, 25, 1, 1, "8.7");
		table.setCell(16, 26, 1, 1, "12.8");
		table.setCell(16, 27, 1, 1, "12.3");
		table.setCell(16, 28, 1, 1, "13.5");
		table.setCell(16, 29, 1, 1, "16.3");		
		
		
		//海拔共23個
		table.setCell(18, 2, 1, 1, "99.0");
		table.setCell(18, 3, 1, 1, "99.0");
		table.setCell(18, 4, 1, 1, "836.2");
		table.setCell(18, 5, 1, 1, "836.2");
		table.setCell(18, 6, 1, 1, "600.0");
		table.setCell(18, 7, 1, 1, "600.0");
		table.setCell(18, 8, 1, 1, "19.0");
		table.setCell(18, 9, 1, 1, "19.0");
		table.setCell(18, 10, 1, 1, "3.4");
		table.setCell(18, 11, 1, 1, "27.4");	
		table.setCell(18, 12, 1, 1, "8.0");
		table.setCell(18, 13, 1, 1, "32.8");
		table.setCell(18, 14, 1, 1, "32.8");
		table.setCell(18, 15, 1, 1, "7.4");
		table.setCell(18, 16, 1, 1, "83.8");
		table.setCell(18, 17, 1, 1, "8.6");
		table.setCell(18, 18, 1, 1, "17.6");
		table.setCell(18, 19, 1, 1, "1014.8");
		table.setCell(18, 20, 1, 1, "9.4");
		table.setCell(18, 21, 1, 1, "2406.1");
		table.setCell(18, 22, 1, 1, "26.8");
		table.setCell(18, 23, 1, 1, "3850.0");
		table.setCell(18, 24, 1, 1, "45.5");
		table.setCell(18, 25, 1, 1, "45.5");
		table.setCell(18, 26, 1, 1, "36.5");
		table.setCell(18, 27, 1, 1, "32.7");		
		table.setCell(18, 28, 1, 1, "11.0");
		table.setCell(18, 29, 1, 1, "12.7");
		
		TextBox cellStyle = table.getStyle();
		cellStyle.setMargin(5, 5, 2, 2); // 調整格子內字到邊框的距離(左,右,上,下)(6, 6, 3, 3)
		cellStyle.setFontStyle(CHINESE_FONT); // 微軟正黑粗體
		cellStyle.setFontSize(8); //字體大小
		
		table.getRowStyle(2).setBackground(0xf3f3f3, 1);
		table.getRowStyle(3).setBackground(0xf3f3f3, 1);
		table.getRowStyle(6).setBackground(0xf3f3f3, 1);
		table.getRowStyle(7).setBackground(0xf3f3f3, 1);
		table.getRowStyle(10).setBackground(0xf3f3f3, 1);
		table.getRowStyle(11).setBackground(0xf3f3f3, 1);
		table.getRowStyle(13).setBackground(0xf3f3f3, 1);
		table.getRowStyle(14).setBackground(0xf3f3f3, 1);
		table.getRowStyle(16).setBackground(0xf3f3f3, 1);
		table.getRowStyle(18).setBackground(0xf3f3f3, 1);
		table.getRowStyle(20).setBackground(0xf3f3f3, 1);
		table.getRowStyle(22).setBackground(0xf3f3f3, 1);
		table.getRowStyle(24).setBackground(0xf3f3f3, 1);
		table.getRowStyle(25).setBackground(0xf3f3f3, 1);
		table.getRowStyle(28).setBackground(0xf3f3f3, 1);
		
		return chart;
	}
	
	public void createTableImage(int pageNumber, Object... args) {
		super.createTableImage(pageNumber, args);
	}

}
