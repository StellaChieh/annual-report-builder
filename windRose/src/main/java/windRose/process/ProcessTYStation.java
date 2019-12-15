package windRose.process;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import ChartDirector.PolarChart;
import entity.Station;
import service.DataHandlerService;
import utility.FileOutputUtilTYStation;
import utility.WindRoseChartTYStation;
import utility.WindRoseExample;


//大量資料處理可考慮切成多個檔案，以top指令做成類似分頁效果

public class ProcessTYStation {
	private static Logger log = Logger.getLogger(ProcessTYStation.class);
	
	DataHandlerService service;
	boolean runPerMonth = false;
	
	public ProcessTYStation(){
		service = new DataHandlerService();
	}
	
	/**
	 * 取得起始時間
	 * @return 起始日期
	 */
	public Date getStartTime(String StartYear){
		Date startTime = null;
		try {
			startTime = Date.valueOf(StartYear+"-01-01");
		} catch (Exception e) {
			System.out.println("彙編以測站為基準每個月風花圖起始年份錯誤");
		}
		return startTime;
	}
	
	/**
	 * 取得結束時間
	 * @return 結束日期
	 * @throws IOException 
	 */
	public Date getEndTime(String EndYear){
		
		EndYear = String.valueOf((Integer.valueOf(EndYear)+1));
		Date endTime = null;
		try {
			endTime = Date.valueOf(EndYear+"-01-01");
			runPerMonth = false;
		} catch (Exception e) {
			System.out.println("彙編以測站為基準每個月風花圖結束年份錯誤");
		}
		return endTime;
	}
	
	/**
	 * 開啟全新的檔案
	 */
	public void createNewFile() throws IOException{	
	    ResourceBundle resource = ResourceBundle.getBundle("file_config");
	    File dir = new File(resource.getString("txtPath"));
	    if(!dir.isDirectory()){
	    	dir.mkdirs();
	    }
	    FileWriter filenameList = new FileWriter(resource.getString("txtPath")+"FileNameList.txt");
	    filenameList.write("");
	    filenameList.close();
	}
	
	/**
	 * 從資料庫取得測站資料，並輸出txt與image檔
	 */
	public PolarChart createRose(String stno, Date startTime, Date endTime, int month){
		double[][] windRoses;
		Station station;
		PolarChart chart = null;
		try {
			station = service.getStation(stno);		
			windRoses = service.getWindRoses(stno, startTime, endTime, month);
			
			// 輸出檔案至實體位置			
			
			FileOutputUtilTYStation fout = new FileOutputUtilTYStation();
			chart = fout.txtOutput(station, startTime.toString(), endTime.toString(), windRoses, stno, runPerMonth, month);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		
		return chart;
	}
	
	/**
	 * for迴圈  執行單測站或數測站, 單月或數月份
	 */
	public void runStnoAndDate(String stnoStr, ArrayList<Date> DateList, String num_ala, String num, String filename){
		
		PolarChart chart = null;
		PolarChart[] chArray = new PolarChart[12];

		if(stnoStr.equals("all") || stnoStr.equals("All") || stnoStr.equals("ALL")){
			ResourceBundle resource = ResourceBundle.getBundle("file_config");
			String stList = resource.getString("stationList");
			String stArray[] = stList.split(",");
//			Arrays.sort(stArray);
			for(int i = 0; i < DateList.size(); i=i+2){
				for(int j = 0; j < stArray.length; j++) {
					for(int k=1; k<=12; k++){
						chart = createRose(stArray[j], DateList.get(i), DateList.get(i+1), k);
						chArray[k] = chart;
					}
				}
			}

		}
		else {
			String[] stnoList = stnoStr.split(",");
//			Arrays.sort(stnoList);
			for(int i = 0; i < DateList.size(); i=i+2){
				for(int j = 0; j < stnoList.length; j++) {
					for(int k=1; k<=12; k++){
						chart = createRose(stnoList[j], DateList.get(i), DateList.get(i+1), k);
						chArray[k-1] = chart;
					}
				}
			}
		}
		
		/* 產製彙編風花圖 */
		WindRoseExample example = new WindRoseExample(); //產製範例風花圖
		PolarChart exChart = example.createChart();
		
		WindRoseChartTYStation RoseChart = new WindRoseChartTYStation();
		RoseChart.mergeChart(chArray, exChart, num_ala, num, filename);
		log.info("風花圖產製完成");

	}
	
	/**
	 * 手動執行程式主要區塊
	 * @throws IOException 
	 */
	public void run(String Station, String num_ala, String num, String year, String filename) throws IOException {
		
		// Station = 測站ID
		// num_ala = 阿拉伯數字
		// num = 羅馬數字
		// year = 起終年份
		// filename = PDF檔案名稱	

		Date startTime, endTime;
		Date tmpSTime, tmpETime;
		String stnoStr;
		ArrayList<Date> DateList = new ArrayList<Date>();
		
		stnoStr = Station;
		
		startTime = getStartTime(year.substring(0, 4));
		endTime = getEndTime(year.substring(5, 9));

		/*做出每月風花圖的頭尾時間的列表*/
		if(runPerMonth==true){
			Calendar cal = Calendar.getInstance();
			tmpSTime = startTime;
				DateList.add(startTime);
				DateList.add(endTime);
		}
		else{
			DateList.add(startTime);
			DateList.add(endTime);
		}

	    /*開啟全新的空檔*/		
		createNewFile();
		log.info("風花圖產製開始");
		
		/*for迴圈  執行單測站或數測站, 單月或數月份*/
		runStnoAndDate(stnoStr, DateList, num_ala, num, filename);
		
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
