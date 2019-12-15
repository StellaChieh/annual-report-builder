package windRose.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

import org.apache.log4j.Logger;

import ChartDirector.PolarChart;
import entity.Station;
import service.DataHandlerService;
import utility.FileOutputUtilTYWeather;
import utility.WindRoseChartTYWeather;
import utility.WindRoseExample_Output;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.Arrays;

//大量資料處理可考慮切成多個檔案，以top指令做成類似分頁效果

public class ProcessTYWeather {
	private static Logger log = Logger.getLogger(ProcessTYWeather.class);

	DataHandlerService service;
	boolean runPerMonth = false;

	public ProcessTYWeather() {
		service = new DataHandlerService();
	}

	/**
	 * 取得起始時間
	 * 
	 * @return 起始日期
	 */
	public Date getStartTime(String StartYear) {

		Date startTime = null;
		try {
			startTime = Date.valueOf(StartYear + "-01-01");
		} catch (Exception e) {
			System.out.println("彙編天氣要素以測站為基準風花圖起始年份錯誤");
		}
		return startTime;
	}

	/**
	 * 取得結束時間
	 * 
	 * @return 結束日期
	 * @throws IOException
	 */
	public Date getEndTime(String EndYear, boolean index) {

		Date endTime = null;
		try {
			if(index){ //Summary
				String EndYearSummary = String.valueOf((Integer.valueOf(EndYear)+1));
				endTime = Date.valueOf(EndYearSummary + "-01-01");				
			}else{ //Surface
				String EndMonthSurface = null;
				String EndYearSurface = EndYear.substring(0, 4);

				if(Integer.valueOf(EndYear.substring(5, EndYear.length()))!=12){
					EndMonthSurface = String.valueOf((Integer.valueOf(EndYear.substring(5, EndYear.length()))+1));
					endTime = Date.valueOf(EndYearSurface + "-" +EndMonthSurface + "-" + "01");
				}else{
					endTime = Date.valueOf(Integer.valueOf(EndYearSurface)+1 + "-01-01");
				}
			}
			runPerMonth = false;
		} catch (Exception e) {
				System.out.println("彙編天氣要素以測站為基準風花圖結束年份錯誤");
		}
		return endTime;
	}

	/**
	 * 開啟全新的檔案
	 */
	public void createNewFile() throws IOException {
		ResourceBundle resource = ResourceBundle.getBundle("file_config");
		File dir = new File(resource.getString("txtPath"));
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		FileWriter filenameList = new FileWriter(resource.getString("txtPath")
				+ "FileNameList.txt");
		filenameList.write("");
		filenameList.close();
	}

	/**
	 * 從資料庫取得測站資料，並輸出txt與image檔
	 * 
	 */
	public PolarChart createRoseTYWeather(String stno, Date startTime, Date endTime) {
		double[][] windRoses;
		Station station;
		PolarChart chart = null;
		try {
			station = service.getStation(stno);
			windRoses = service.getWindRosesTY(stno, startTime, endTime);

			FileOutputUtilTYWeather fout = new FileOutputUtilTYWeather();
			chart = fout.txtOutput(station, startTime.toString(),
					endTime.toString(), windRoses, stno, runPerMonth);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

		return chart;
	}

	/**
	 * for迴圈 執行單測站或數測站, 單月或數月份
	 */
	public void runStnoAndDate(String[] stnoList, ArrayList<Date> DateList, String num, String filename, boolean index) {

		PolarChart chart = null;

		ArrayList<PolarChart> chArray = new ArrayList();

		/* 產製彙編風花圖 */
		if(index){
			WindRoseExample_Output example = new WindRoseExample_Output(); // 產製範例風花圖
			example.createChart(num, filename, index);
		}
		
		WindRoseChartTYWeather RoseChart = new WindRoseChartTYWeather();
		Arrays.sort(stnoList);

		 for(int i = 0; i < DateList.size(); i=i+2){
				 for(int j = 0; j < stnoList.length; j++) {
					 stnoList[j] = stnoList[j].trim();
					 chart = createRoseTYWeather(stnoList[j], DateList.get(i), DateList.get(i+1));
					 chArray.add(chart);
				 }
				 	 RoseChart.mergeChart(chArray, num, filename, index);//分組並產製pdf
				 	 chArray.clear();
			 }
		 
		 log.info("風花圖產製完成");

	}

	/**
	 * 手動執行程式主要區塊
	 * 
	 * @throws IOException
	 */
	public void run(String[] Station, String num, String year, String filename, boolean index) throws IOException {
		Date startTime, endTime;
		Date tmpSTime, tmpETime;
		ArrayList<Date> DateList = new ArrayList<Date>();

		startTime = getStartTime(year.substring(0, 4));

		if(index)
			endTime = getEndTime(year.substring(5, 9), index); //Summary
		else
			endTime = getEndTime(year, index); //Surface
		
		log.info("風花圖產製開始");

		/* 做出每月風花圖的頭尾時間的列表 */
		if (runPerMonth == true) {
			tmpSTime = startTime;
			DateList.add(startTime);
			DateList.add(endTime);
		} else {
			DateList.add(startTime);
			DateList.add(endTime);
		}

		/* 開啟全新的空檔 */
		createNewFile();

		/* for迴圈 執行單測站或數測站, 單月或數月份 */
		runStnoAndDate(Station, DateList, num, filename, index);

		service.closeSession();
		
		//刪除在file_config下的txt所有檔案
		ResourceBundle resource = ResourceBundle.getBundle("file_config");
	    File dir = new File(resource.getString("txtPath"));
	    deleteAll(dir);
	}

	//刪除資料夾內所有檔案
	public void deleteAll(File path) {
        if (!path.exists()) {
            return;
        }
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAll(files[i]);
        }
    }
}
