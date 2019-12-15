package utility;

import ChartDirector.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.io.File;

public class WindRoseChartTYWeather {
	public static String stnCHname = null;
	public static String stnENname = null;
	public static String stnID = null;
	public static int stnmonth = 0;

	// Main code for creating charts
	public PolarChart createRoseTYWeather(double CALM, double[] data0,
			double[] data1, double[] data2, double[] angles, String stno,
			String stnCName, String stnEName, String startDate, String endDate,
			Boolean runPerMonth) {
		// 註冊
		Chart.setLicenseCode("DEVP-2LFA-BN3Z-3YPF-D1E4-DE76");

		int centerX = 110;
		int centerY = 150;
		int R = 70;
		int r = 20;

		stnCHname = stnCName; // 測站中文名稱
		stnENname = stnEName; // 測站英文名稱
		stnID = stno; // 測站ID
		// stnmonth = month; //月份

		PolarChart c = new PolarChart(220, 280, 0xffffff, 0x000000, 1);

		c.enableVectorOutput();

		// station name, date
		String textTitle = "";
		String monthRange = null;
		
		String yesterday = getYesterday(endDate);

		String endyear = String
				.valueOf(Integer.valueOf(endDate.substring(0, 4)) - 1); //Summary real year
		String endmonthSurface = String
				.valueOf(Integer.valueOf(yesterday.substring(5, 7))); // Surface real month
		String endyearSurface = String
				.valueOf(Integer.valueOf(yesterday.substring(0, 4))); // Surface real year
				
		if (startDate.substring(0, 4).equals(endyearSurface)) { // Surface 
			//if month==1
			if(Integer.valueOf(endmonthSurface)==1)
				monthRange = "1";
			else
				monthRange = "1-"+endmonthSurface;
			
			textTitle = startDate.substring(0, 4) + "年" + monthRange + "月" + "\n"
					+ stnCName + " " + stno;
		} else { //Summary
			textTitle = startDate.substring(0, 4) + "至" + endyear + "年" + "\n"
					+ stnCName + " " + stno;
		}
		TextBox textSt = c.addText(20, 20, textTitle, "kaiu.ttf", 7);
		textSt.setAlignment(Chart.TopLeft);
		textSt.setBackground(0xffffffff);

		// Set plot area
		c.setPlotArea(centerX, centerY, R, 0xffffff);

		// Set the grid style to circular grid
		// and the grid lines will be at the bottom of the polar plot area
		c.setGridStyle(false, false);
		c.setGridColor(0x709999ff, 2, 0x709999ff, 2);

		// Set angular axis as 0 - 360, with a spoke every 30 units
		String[] angularLabels = new String[12];
		for (int i = 0; i < angularLabels.length; ++i) {
			if (i == 0)
				angularLabels[i] = "360°";
			else
				angularLabels[i] = String.valueOf(i * 30) + "°";
		}
		c.angularAxis().setLinearScale2(0, 360, angularLabels);
		c.angularAxis().setLabelStyle("Arial", 5, 0x000000)
				.setBackground(0xffffffff);

		// remove the line in the center area
		for (int i = 0; i < 360; i = i + 30) {
			int cosX = (int) ((r - 3) * Math.cos(i / 180.0 * Math.PI));
			int sinY = (int) ((r - 3) * Math.sin(i / 180.0 * Math.PI));
			c.addLine(centerX, centerY, centerX + cosX, centerY + sinY,
					0xffffff, 4);
		}

		// Set radial axis as 0 - 40%, with a spoke every 10 units
		c.radialAxis().setMargin(0, r);
		String[] radialLabels = { " ", "10", "20", "30%" };
		c.radialAxis().setLinearScale2(0, 30, radialLabels);
		c.radialAxis().setLabelStyle("", 5, 0x000000).setBackground(0xffffffff);
		c.radialAxis().setColors(0x709999ff);
		c.radialAxis().setLabelGap(1);

		// the CALM
		TextBox textCALM = c.addText(centerX, centerY, "CALM\n" + CALM + "%",
				"Arial Bold", 5);
		textCALM.setAlignment(Chart.Center);
		textCALM.setBackground(0x00ffffff);

		// draw the wind rose
		double maxData = 0.0;
		int maxDataAngles = 0;
		for (int i = 0; i < angles.length; ++i) {
			if (data0[i] > 0)
				c.angularAxis().addZone(angles[i] - 1, angles[i] + 1, 0,
						data0[i], 0x000000, -1);
			if (data1[i] - data0[i] > 0)
				c.angularAxis().addZone(angles[i] - 3, angles[i] + 3, data0[i],
						data1[i], 0x000000, -1);
			if (data2[i] - data1[i] > 0)
				c.angularAxis().addZone(angles[i] - 5, angles[i] + 5, data1[i],
						data2[i], 0x000000, -1);
			if (data2[i] > maxData) {
				maxData = data2[i];
				maxDataAngles = i * 10 + 10;
			}
		}

		// lable the max % of wind
		if (maxData > 0) {
			double[] test0 = { maxData };
			double[] test1 = { maxDataAngles };
			int labelX = 0;
			int labelY = 0;
			PolarLineLayer layer0 = c.addLineLayer(test0);
			layer0.setAngles(test1);
			layer0.setDataLabelFormat("{value}");
			layer0.setDataLabelStyle().setBackground(0xffffffff);
			if (maxDataAngles > 0 && maxDataAngles < 90) {
				labelX = 20;
				labelY = -10;
			} else if (maxDataAngles == 90) {
				labelX = 17;
				labelY = 0;
			} else if (maxDataAngles > 90 && maxDataAngles < 180) {
				labelX = 20;
				labelY = 20;
			} else if (maxDataAngles == 180) {
				labelX = 0;
				labelY = 15;
			} else if (maxDataAngles > 180 && maxDataAngles < 270) {
				labelX = -20;
				labelY = 20;
			} else if (maxDataAngles == 270) {
				labelX = -20;
				labelY = 0;
			} else if (maxDataAngles > 270 && maxDataAngles < 360) {
				labelX = -10;
				labelY = -20;
			} else if (maxDataAngles == 360) {
				labelX = -25;
				labelY = -5;
			}
			layer0.setDataLabelStyle("Arial Bold", 5).setPos(labelX, labelY);
		}

		return c;
	}

	public String getYesterday(String endDate){
		Calendar cal = Calendar.getInstance();
		cal.setTime(Date.valueOf(endDate));
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String Yestarday = sdf.format(cal.getTime());
		return Yestarday;
	}
	
	public void mergeChart(ArrayList<PolarChart> chArray, String num,
			String filename, boolean index) {

		int group = 0;

		// 分幾組
		group = chArray.size() / 9;
		// 餘數
		int yu = chArray.size() % 9;
		// 若有餘數則分組數+1
		if (yu > 0)
			group = group + 1;

		for (int i = 0; i < group; i++) { // 每一個group
			List<PolarChart> subGroup = new ArrayList<PolarChart>();
			if (i == group - 1) {
				subGroup = chArray.subList(i * 9, chArray.size());
			} else {
				subGroup = chArray.subList(i * 9, 9 * (i + 1));
			}
			GetPDF(subGroup, i, num, filename, index);
		}
	}

	public void GetPDF(List<PolarChart> subGroup, int count, String num,
			String filename, boolean index) {

		int[] x_position = { 65, 290, 515, 65, 290, 515, 65, 290, 515 };
		int[] y_position = { 110, 110, 110, 420, 420, 420, 730, 730, 730 };

		MultiChart m2 = new MultiChart(794, 1123);

		for (int i = 0; i < subGroup.size(); i++)
			m2.addChart(x_position[i], y_position[i], subGroup.get(i));

		if (index) {

			TextBox title_2 = m2.addText(400, 35, num + "." + " 風 花 圖" + "\n"
					+ " Wind Rose" + "\n", "kaiu.ttf", 10);
			title_2.setAlignment(Chart.Center);
			title_2.setBackground(0xffffffff);

			m2.makeChart("tmp/tmp_summary/" + filename + "_" + (count + 2)
					+ ".pdf"); // 產製彙編風花圖(天氣要素)

		} else {

			TextBox title_2 = m2.addText(400, 35, " 風 花 圖" + "\n"
					+ " Wind Rose" + "\n", "kaiu.ttf", 10);
			title_2.setAlignment(Chart.Center);
			title_2.setBackground(0xffffffff);

			m2.makeChart("tmp/tmp_surface/" + filename + "_" + (count + 2)
					+ ".pdf"); // 產製地面年報風花圖
		}
	}

}
