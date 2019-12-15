package service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import utility.SqlSessionFactoryGen;
import dao.WindRoseDao;
import entity.Station;
import entity.WindRose;

public class DataHandlerService {
	private static SqlSessionFactory factory =SqlSessionFactoryGen.getSqlSessionFactory(); 
	
	//全為查詢方法故直接開session給整隻service共用
	private SqlSession sqlSession;
	private WindRoseDao dao;
	
	
	public DataHandlerService() {
		super();
		sqlSession = factory.openSession(false);
		dao = sqlSession.getMapper(WindRoseDao.class);
	}
	
	private double[][] initWinRoses(){
		double[][] windRoses = new double[37][4];
	    for(int i = 0; i < windRoses.length; i++) {
	        windRoses[i][0] = i*10.0;
	        windRoses[i][1] = 0.0;
	        windRoses[i][2] = 0.0;
	        windRoses[i][3] = 0.0;
	    }
	    windRoses[0][1] = 0.0;
		return windRoses;
	}
	
	
	/**
	 * *取得測站資訊
	 * @param stno
	 * @return Station
	 */
	public Station getStation(String stno){
		return dao.getStation(stno);
	}
	
	/**
	 * *取得風花圖參數資訊(計算年報每年月份風花圖 & 彙編10年每測站每月風花圖)
	 * @param stno
	 * @param startDate
	 * @param endDate
	 * @return double[][]
	 */
	public double[][] getWindRoses(String stno,Date startDate, Date endDate, int month){
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("stno", stno);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		condition.put("month", month);
		
		//double staticWind = 0;
		double total = 0.0;
		int totalCount = 0;
		try{		
			totalCount = dao.getTotalCount(condition);//取得總筆數
			//System.out.println("totalCount: " + totalCount);
		} catch (Exception e) {
			System.out.println(e);
			totalCount = 0;
			total = 100;
		}
		List<WindRose> windRoseProps = dao.getWindRoseProps(condition);
		double[][] windRoses = initWinRoses();	//陣列值內容：{方位,第一層,第二層,第三層} 靜風值存放於windRoses[0][1]
		
		for(WindRose wr:windRoseProps){
			if(wr.getWd()!=null){
							
			int wd = wr.getWd().intValue();
			
			if(wd == 0.0 || wd == 999 || wd == -999){//方位0與999.9的特殊處理
				//原靜風取得方式(但加總會有誤差)
//				double temp = wr.getLevel1() + wr.getLevel2() + wr.getLevel3() + wr.getStaticWind();
//				staticWind = staticWind + temp;
				windRoses[0][1] = windRoses[0][1] + (wr.getStaticWind()*100)/totalCount;
				
			}else{
				//staticWind = staticWind + wr.getStaticWind();//靜風值計算
				int i = wd/10;
				
				//小數點後一位四捨五入
				//windRoses[i][1] = windRoses[i][1] + Math.round((wr.getLevel1()*1000)/totalCount)/10.0;
				//windRoses[i][2] = windRoses[i][2] + Math.round((wr.getLevel2()*1000)/totalCount)/10.0;
				//windRoses[i][3] = windRoses[i][3] + Math.round((wr.getLevel3()*1000)/totalCount)/10.0;
				//如果有跨到兩個table(his_cwbhr, cwbhr) 會累加
				windRoses[i][1] = windRoses[i][1] + (wr.getLevel1()*100)/totalCount;
				windRoses[i][2] = windRoses[i][2] + (wr.getLevel2()*100)/totalCount;
				windRoses[i][3] = windRoses[i][3] + (wr.getLevel3()*100)/totalCount;
				}
			}
		}
	    for(int i = 0; i < windRoses.length; i++) {
	    	//小數點後一位四捨五入
	    	windRoses[i][1] = Math.round(windRoses[i][1]*10)/10.0;
	    	windRoses[i][2] = Math.round(windRoses[i][2]*10)/10.0;
	    	windRoses[i][3] = Math.round(windRoses[i][3]*10)/10.0;
	    	total = total + windRoses[i][1] + windRoses[i][2]+ windRoses[i][3];
	    }
		
		if (total > 100.0){
			System.out.println("total wind over 100%: " +  total);
			//total = 100.0;
		}
		
		//windRoses[0][1] = Math.round(staticWind/totalCount*1000)/10.0;	//將靜風值存放於方位0的第一層
		//windRoses[0][1] = Math.round((100 - total)*10)/10.0;
		windRoses[0][1] = Math.round(windRoses[0][1]*10)/10.0;
		
		return windRoses;
	}
	
	/**
	 * *取得風花圖參數資訊(計算彙編10年天氣要素各測站風花圖)
	 * @param stno
	 * @param startDate
	 * @param endDate
	 * @return double[][]
	 */
	public double[][] getWindRosesTY(String stno,Date startDate, Date endDate){
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("stno", stno);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		
		//double staticWind = 0;
		double total = 0.0;
		int totalCount = 0;
		try{		
			totalCount = dao.getTotalCountTY(condition);//取得總筆數
			//System.out.println("totalCount: " + totalCount);
		} catch (Exception e) {
			System.out.println(e);
			totalCount = 0;
			total = 100;
		}
		List<WindRose> windRoseProps = dao.getWindRosePropsTY(condition);
		double[][] windRoses = initWinRoses();	//陣列值內容：{方位,第一層,第二層,第三層} 靜風值存放於windRoses[0][1]
		
		for(WindRose wr:windRoseProps){
			if(wr.getWd()!=null){

			int wd = wr.getWd().intValue();
			
			if(wd == 0.0 || wd == 999 || wd == -999){//方位0與999.9的特殊處理
				//原靜風取得方式(但加總會有誤差)
//				double temp = wr.getLevel1() + wr.getLevel2() + wr.getLevel3() + wr.getStaticWind();
//				staticWind = staticWind + temp;
				windRoses[0][1] = windRoses[0][1] + (wr.getStaticWind()*100)/totalCount;
				
			}else{
				//staticWind = staticWind + wr.getStaticWind();//靜風值計算
				int i = wd/10;
				
				//小數點後一位四捨五入
				//windRoses[i][1] = windRoses[i][1] + Math.round((wr.getLevel1()*1000)/totalCount)/10.0;
				//windRoses[i][2] = windRoses[i][2] + Math.round((wr.getLevel2()*1000)/totalCount)/10.0;
				//windRoses[i][3] = windRoses[i][3] + Math.round((wr.getLevel3()*1000)/totalCount)/10.0;
				//如果有跨到兩個table(his_cwbhr, cwbhr) 會累加
				windRoses[i][1] = windRoses[i][1] + (wr.getLevel1()*100)/totalCount;
				windRoses[i][2] = windRoses[i][2] + (wr.getLevel2()*100)/totalCount;
				windRoses[i][3] = windRoses[i][3] + (wr.getLevel3()*100)/totalCount;
				}
			}
		}
	    for(int i = 0; i < windRoses.length; i++) {
	    	//小數點後一位四捨五入
	    	windRoses[i][1] = Math.round(windRoses[i][1]*10)/10.0;
	    	windRoses[i][2] = Math.round(windRoses[i][2]*10)/10.0;
	    	windRoses[i][3] = Math.round(windRoses[i][3]*10)/10.0;
	    	total = total + windRoses[i][1] + windRoses[i][2]+ windRoses[i][3];
	    }
		
		if (total > 100.0){
			System.out.println("total wind over 100%: " +  total);
			//total = 100.0;
		}
		
		//windRoses[0][1] = Math.round(staticWind/totalCount*1000)/10.0;	//將靜風值存放於方位0的第一層
		//windRoses[0][1] = Math.round((100 - total)*10)/10.0;
		windRoses[0][1] = Math.round(windRoses[0][1]*10)/10.0;
		
		return windRoses;
	}
	
	/**
	 * 供外部關session使用
	 */
	public void closeSession(){
		sqlSession.close();
	}

	
}
