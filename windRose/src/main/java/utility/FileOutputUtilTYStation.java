package utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import ChartDirector.PolarChart;
import entity.Station;

public class FileOutputUtilTYStation {
	
	private String getHeader(Station st,String startDate, String endDate) throws ParseException{
		String[] sDate = startDate.split("-");
		int startYear = Integer.parseInt(sDate[0]);
		
		String[] eDate = endDate.split("-");
		int endYear = Integer.parseInt(eDate[0]);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
	    Date startMonth = sdf.parse(sDate[1]);
	    Date endMonth = sdf.parse(eDate[1]);
	    sdf = new SimpleDateFormat("MMMMM",Locale.US);
		
	    //民國 101 年 7 月  至  民國 101 年 10 月 風花圖 Surface Wind Roses, July, 2012 To October, 2012
		String header1 = "民國 " + (startYear-1911) + "年" + sDate[1] + "月 至民國 "+
						(endYear-1911) + "年" + eDate[1] + "月 風花圖 Surface Wind Roses," +
						sdf.format(startMonth) + ", "+ startYear + 
						" To " + sdf.format(endMonth) + ", "+ endYear;
		
		//466900|淡水|TAMSUI|121.440000|25.165600
		String header2 = st.getStno() + "|" + st.getStnCName().trim() + "|" + st.getStnEName().trim() + 
						"|" + st.getLongitude().trim() + "|" +st.getLatitude().trim();
		return header1 + "\n" + header2;
	}
	
	/**
	 * 輸出txt檔後關閉writer
	 * 	
	 * @param st
	 * @param startDate
	 * @param endDate
	 * @param windRoses
	 * @throws ParseException
	 * @throws IOException
	 */
	public PolarChart txtOutput(Station station,String startDate, String endDate,double[][] windRoses, String stno, Boolean runPerMonth, int month) throws ParseException, IOException{
		
		StringBuilder content = new StringBuilder();
		String header = getHeader(station, startDate, endDate); 
		content.append(header + "|" + windRoses[0][1] + "\n");//header部分串入靜風值
		
		/*測站的中文名稱*/
		String stnCName = station.getStnCName().trim();
		String stnEName = station.getStnEName().trim();

		/*檔名格式: yyyy-mmdd_yyyy-mmdd_stno*/
		//String subFilename = startDate.substring(0,7) + startDate.substring(8,10) + "_" + endDate.substring(0,7) + endDate.substring(8,10) + "_" + stno;
		String subFilename = "WR_A_" + startDate.substring(0,10) + "_" + endDate.substring(0,10) + "_" + stno + "_" + month;
		
		//the data for create rose
		double[] data0 = new double[36];
		double[] data1 = new double[36];
		double[] data2 = new double[36];
		double[] angles = new double[36];
		
	    for(int i = 1; i < windRoses.length; i++) {
	    	content.append(windRoses[i][0] + "|");
	    	content.append(windRoses[i][1] + "|");
	    	content.append(windRoses[i][2] + "|");
	    	content.append(windRoses[i][3] + "\n");
	    	
	    	angles[i-1] = windRoses[i][0];
	    	data0[i-1] = windRoses[i][1];
	    	data1[i-1] = data0[i-1] + windRoses[i][2];
	    	data2[i-1] = data1[i-1] + windRoses[i][3];
	    }
	    
	    ResourceBundle resource = ResourceBundle.getBundle("file_config");
	    File dir = new File(resource.getString("txtPath"));
	    if(!dir.isDirectory()){
	    	//System.out.println("txt dir not exit!");
	    	dir.mkdirs();
	    }
	    FileWriter fileWriter = new FileWriter(resource.getString("txtPath")+subFilename+".txt");
	    fileWriter.write(content.toString());
	    fileWriter.close();
	    
	    //create wind rose chart
	    WindRoseChartTYStation rose = new WindRoseChartTYStation();
	    PolarChart chart = rose.createRose(windRoses[0][1], data0, data1, data2, angles, stno, stnCName, stnEName, startDate, endDate, runPerMonth, month);	   
        
	    //將風花圖測站名稱與檔名 寫入檔尾
	    FileWriter filenameList = new FileWriter(resource.getString("imagePath")+"FileNameList.txt", true);
	    filenameList.write(stno + " " + stnCName + " " + subFilename + "\n");
	    filenameList.close();
	    
	    return chart;
	}
	
}
