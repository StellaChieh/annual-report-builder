package cwb.cmt.surface.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import cwb.cmt.surface.dao.CliSumDao;
import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.CliSum;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.service.CreateCsvForCliSum;
import cwb.cmt.surface.service.CreateTableImage;
import cwb.cmt.surface.utils.NumberConvert;
import cwb.cmt.surface.utils.Numbers;
import cwb.cmt.surface.utils.PageEncode;
import cwb.cmt.surface.utils.ParseStnXml;
import cwb.cmt.surface.utils.PrepareCliSum;

@Service("processCliSum")
public class ProcessCliSum extends Process {
	
	private static final String PREFIX = String.valueOf(PageEncode.CLISUM.getFilename());
	
	@Resource(name="prepareCliSum")
	PrepareCliSum prepareCliSum;
	
 	@Resource(name="cliSumDaoImpl")
	CliSumDao dao;
	
	@Resource(name="createTableImageForCliSum")
	CreateTableImage drawer;
	
	@Resource(name="parseStnXml")
	ParseStnXml parseStnXml;
	
	@Resource(name="processAbstract")
	ProcessAbstract processAbstract;
	
	@Resource(name="createCsvForCliSum")
	CreateCsvForCliSum csvWriter;
    //Time: year
    @Resource(name="year")
    protected int year;
    
    //Time: year
    @Resource(name="month")
    protected int month;
    
	public void run() throws IOException{
		long begin = System.currentTimeMillis();
		prepareConfig_CliSum();
		drawer.setPath(outputTmpPdfPath);
		//add stns with no count of day
	    List<CliSum> stnsXml = prepareCliSum.getStnXml(stnList);
	    
		for(int i=0, pageNumber=1; i<stnList.size(); i++, pageNumber++) {
			//setting initial page number
			processAbstract.setPageStart(pageNumber);
			System.out.println("draw CliSum Page: "+ (i+1));
			drawer.setFilename(filename(pageNumber, stnList, i));
			//filter clisumList with the same Stno
			List<CliSum> monthData = createResultList(monthDataList, i);
			//Hour AVG
			List<CliSum> hourAvg = createResultList(hourAvgList,i);
			//Day AVG
			List<CliSum> dayAvg = createResultList(dayAvgList,i);
			//hour Sum
			List<CliSum> hourSum = addLossMonth(
					createResultList(addStnCount(stnsXml, hourSumList, "hourSum"), i), "hourSum");
			
			//precpDays_10mmList
			List<CliSum> precpDays_10mm =  addLossMonth(
						createResultList(
								addStnCount(stnsXml, precpDays_10mmList, "precpDays_10mm"), i), "precpDays_10mm");
			//precpDays_1mmList
			List<CliSum> precpDays_1mm = addLossMonth(
						createResultList(
							addStnCount(stnsXml, precpDays_1mmList, "precpDays_1mm"), i), "precpDays_1mm");
			//precpDays_01mmList
			List<CliSum> precpDays_01mm = addLossMonth(
					createResultList(
							addStnCount(stnsXml, precpDays_01mmList, "precpDays_01mm"), i), "precpDays_01mm");
					
			//weatherCondition
			List<CliSum> weatherCondition = createResultList(setWeatherCondition(weatherConditionList), i);
					
			//TxMinAbs
			List<CliSum> TxMinAbs_LE0 =  addLossMonth(
					createResultList(
							addStnCount(stnsXml, txMinAbs_LE0List, "txMinAbs_LE0"), i), "txMinAbs_LE0");
			List<CliSum> TxMinAbs_LE10 = addLossMonth(
					createResultList(
							addStnCount(stnsXml, txMinAbs_LE10List, "txMinAbs_LE10"), i), "txMinAbs_LE10");
			List<CliSum> TxMinAbs_GE20 = addLossMonth(
					createResultList(
							addStnCount(stnsXml, txMinAbs_GE20List, "txMinAbs_GE20"), i), "txMinAbs_GE20");
			List<CliSum> TxMaxAbs_GE25 = addLossMonth(
					createResultList(
							addStnCount(stnsXml, txMaxAbs_GE25List, "txMaxAbs_GE25"), i), "txMaxAbs_GE25");
			List<CliSum> TxMaxAbs_GE30 = addLossMonth(
					createResultList(
							addStnCount(stnsXml, txMaxAbs_GE30List, "txMaxAbs_GE30"), i), "txMaxAbs_GE30");
			List<CliSum> TxMaxAbs_GE35 = addLossMonth(
					createResultList(
							addStnCount(stnsXml, txMaxAbs_GE35List, "txMaxAbs_GE35"), i), "txMaxAbs_GE35");
			
			List<CliSum> precpDay = addLossMonth(
					createResultList(addStnCount(stnsXml, precpDayList, "precpDay"), i), "precpDay");
			
			List<CliSum> hazeDay = addLossMonth(
					createResultList(
							addStnCount(stnsXml, hazeDayList, "hazeDay"), i), "hazeDay");
			List<CliSum> hailDay = addLossMonth(
					createResultList(
							addStnCount(stnsXml, hailDayList, "hailDay"), i), "hailDay");
			List<CliSum> dewDay = addLossMonth(
					createResultList(
							addStnCount(stnsXml, dewDayList, "dewDay"), i), "dewDay");
			List<CliSum> snowDate = addLossMonth(
					createResultList(
							addStnCount(stnsXml, snowDateList, "snowDate"), i), "snowDate");
			List<CliSum> frostDate = addLossMonth(
					createResultList(
							addStnCount(stnsXml, frostDateList, "frostDate"), i), "frostDate");
			
			boolean isFirstPage = (i == 0) ? true : false;
			String TableCNum = "第  " +NumberConvert.digit2ChineseString(i+1)+ "  表   ";
			String TableENum = "  Table " + (i+1) + "  ";
			drawer.createTableImage(isFirstPage, TableCNum, TableENum, stnList.get(i).getStnCName(),
					stnList.get(i).getStnEName(), monthData, hourAvg, dayAvg, hourSum, precpDays_10mm,
					precpDays_1mm, precpDays_01mm, weatherCondition, TxMinAbs_LE0, TxMinAbs_LE10, TxMinAbs_GE20,
					TxMaxAbs_GE25, TxMaxAbs_GE30, TxMaxAbs_GE35, precpDay, hazeDay, hailDay, dewDay, snowDate,
					frostDate, processAbstract.getCurrentPageNumber());
			// update page number
			processAbstract.increasePageNumber();
			System.out.println("...draw completed...");
		}
       
        long over = System.currentTimeMillis();
        System.out.println("使用的時間為： "
            + (over - begin)/1000 + " 秒 " ); 
	}
	
	
	public void runClisumCsv(String outputCsvFolder) throws IOException{
		long begin = System.currentTimeMillis();
		prepareConfig_CliSum();
		
		//add stns with no count of day
	    List<CliSum> stnsXml = prepareCliSum.getStnXml(stnList);
	    ICsvMapWriter mapWriter = null;
		final String[] table1Header = new String[] {"測站中文名/StnCName", "測站英文名/StnEName", 
				"年份/Year", "月份/Month", "氣壓/Station Pressure hPa", "5h氣溫/Air Temperature ℃", "14h/氣溫Air Temperature ℃",
				"21h氣溫/Air Temperature ℃", "平均氣溫/Mean of obs.", "最高平均氣溫/Max.", "最低平均氣溫/Min.", "絕對最高氣溫/Absolute Max.",
				"絕對最高氣溫月日/Date", "絕對最低氣溫/Absolute Min.", "絕對最低氣溫月日/Date", "5h相對濕度/Relative Humidity %",
				"14h相對濕度/Relative Humidity %", "21h相對濕度/Relative Humidity %", "平均相對濕度/Mean",
				"最小相對濕度/Min.", "日照時數總計/Duration of Sunshine Total of hr",
				"日照時數百分/Duration of Sunshine %"}; //0-21
		final String[] table2Header = new String[] {"測站中文名/StnCName", "測站英文名/StnEName", 
				"年份/Year", "月份/Month", "平均風速/Wind Speed m/s", "最多風向/Prev. Dir.", "最大風速 /Maximum",
				"最大風方向/Dir.", "最大風月日/Date", "最大陣風風速/Speed", "最大陣風月日/Date",
				"5h雲量/Cloud Amout 0-10", "14h雲量/Cloud Amout 0-10", "21h雲量/Cloud Amout 0-10", "平均雲量/Mean of obs.",
				"能見度/Visibility km", "降水量總計/Precipitation Total mm", "一日間最大降水量/Max. in 24h",
				"一日間最大降水量月日/Date", "降水時數/Precipitation Duration", "降水日數總計≧0.1mm/No. of Precipitation Days Total ≧0.1mm", 
				"降水日數≧1.0mm/No. of Precipitation Days ≧1.0mm", "降水日數≧10mm No. of Precipitation Days ≧10.0mm"
		       	}; //0-22
		final String[] table3Header = new String[] {"測站中文名/StnCName", "測站英文名/StnEName", 
				"年份/Year", "月份/Month", "蒸發量/Evaporantion mm", "最低氣溫≦ 0°日數/Min. Temp. ≦ 0°","最低氣溫≦ 10°日數/Min. Temp. ≦ 10°",
				"最低氣溫≧ 20°日數/Min. Temp. ≦ 20°", "最高氣溫≧ 25°日數/Max. Temp. ≧ 25°", "最高氣溫≧ 30°日數/Max. Temp. ≧ 30°",
				"最高氣溫≧ 35°日數/Max. Temp. ≧ 35°", "雨日數/Rain", "雪日數/Snow", "霧日數/Fog", "霾日數/Haze", 
				"雷暴日數/Thunderstorm", "雹日數/Hail", "強風日數/wind≧10m/s", "露日數/Dew", "霜日數/Frost",
				"碧日數/＜1", "疏日數/1.0－5.9", "烈日數/6.0－9.0", "密日數/＞9", 
				"初終雪日/Snow Date", "初終霜日/Frost Date"
				};//0-25
		try {
			String outputPath = Paths.get(outputCsvFolder, year+"年基本氣象綱要表.csv").toString();
			FileOutputStream fileStream = new FileOutputStream(new File(outputPath), true);
			OutputStreamWriter fileWriter = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
//			FileWriter fileWriter = new FileWriter(outputPath);
			fileWriter.write('\ufeff');
			mapWriter = new CsvMapWriter(fileWriter,
	                CsvPreference.STANDARD_PREFERENCE);
			for(int head=0; head<3; head++) {
				if(head==0){
					mapWriter.writeHeader(table1Header);
				}else if(head==1) {
					mapWriter.writeHeader(table2Header);
				}
				else if (head==2) {
					mapWriter.writeHeader(table3Header);
				}
				for(int i=0; i<stnList.size(); i++) {
					//filter clisumList with the same Stno
					List<CliSum> monthData = createResultList(monthDataList, i);
					//Hour AVG
					List<CliSum> hourAvg = createResultList(hourAvgList,i);
					//Day AVG
					List<CliSum> dayAvg = createResultList(dayAvgList,i);
					//hour Sum
					List<CliSum> hourSum = addLossMonth(
							createResultList(addStnCount(stnsXml, hourSumList, "hourSum"), i), "hourSum");
					//precpDays_10mmList
					List<CliSum> precpDays_10mm =  addLossMonth(
								createResultList(
										addStnCount(stnsXml, precpDays_10mmList, "precpDays_10mm"), i), "precpDays_10mm");
					//precpDays_1mmList
					List<CliSum> precpDays_1mm = addLossMonth(
								createResultList(
									addStnCount(stnsXml, precpDays_1mmList, "precpDays_1mm"), i), "precpDays_1mm");
					//precpDays_01mmList
					List<CliSum> precpDays_01mm = addLossMonth(
							createResultList(
									addStnCount(stnsXml, precpDays_01mmList, "precpDays_01mm"), i), "precpDays_01mm");
					//weatherCondition
					List<CliSum> weatherCondition = createResultList(setWeatherCondition(weatherConditionList), i);
							
					//TxMinAbs
					List<CliSum> TxMinAbs_LE0 =  addLossMonth(
							createResultList(
									addStnCount(stnsXml, txMinAbs_LE0List, "txMinAbs_LE0"), i), "txMinAbs_LE0");
					List<CliSum> TxMinAbs_LE10 = addLossMonth(
							createResultList(
									addStnCount(stnsXml, txMinAbs_LE10List, "txMinAbs_LE10"), i), "txMinAbs_LE10");
					List<CliSum> TxMinAbs_GE20 = addLossMonth(
							createResultList(
									addStnCount(stnsXml, txMinAbs_GE20List, "txMinAbs_GE20"), i), "txMinAbs_GE20");
					List<CliSum> TxMaxAbs_GE25 = addLossMonth(
							createResultList(
									addStnCount(stnsXml, txMaxAbs_GE25List, "txMaxAbs_GE25"), i), "txMaxAbs_GE25");
					List<CliSum> TxMaxAbs_GE30 = addLossMonth(
							createResultList(
									addStnCount(stnsXml, txMaxAbs_GE30List, "txMaxAbs_GE30"), i), "txMaxAbs_GE30");
					List<CliSum> TxMaxAbs_GE35 = addLossMonth(
							createResultList(
									addStnCount(stnsXml, txMaxAbs_GE35List, "txMaxAbs_GE35"), i), "txMaxAbs_GE35");
					List<CliSum> precpDay = addLossMonth(
							createResultList(addStnCount(stnsXml, precpDayList, "precpDay"), i), "precpDay");
					
					List<CliSum> hazeDay = addLossMonth(
							createResultList(
									addStnCount(stnsXml, hazeDayList, "hazeDay"), i), "hazeDay");
					List<CliSum> hailDay = addLossMonth(
							createResultList(
									addStnCount(stnsXml, hailDayList, "hailDay"), i), "hailDay");
					List<CliSum> dewDay = addLossMonth(
							createResultList(
									addStnCount(stnsXml, dewDayList, "dewDay"), i), "dewDay");
					List<CliSum> snowDate = addLossMonth(
							createResultList(
									addStnCount(stnsXml, snowDateList, "snowDate"), i), "snowDate");
					List<CliSum> frostDate = addLossMonth(
							createResultList(
									addStnCount(stnsXml, frostDateList, "frostDate"), i), "frostDate");
					
					csvWriter.createCsv(stnList.get(i).getStnCName(),
							stnList.get(i).getStnEName(), monthData, hourAvg, dayAvg, hourSum, precpDays_10mm,
							precpDays_1mm, precpDays_01mm, weatherCondition, TxMinAbs_LE0, TxMinAbs_LE10, TxMinAbs_GE20,
							TxMaxAbs_GE25, TxMaxAbs_GE30, TxMaxAbs_GE35, precpDay, hazeDay, hailDay, dewDay, snowDate,
							frostDate, mapWriter, table1Header, table2Header, table3Header, head);
					
					System.out.println("...draw clisum completed...");
				}
			}
	
       
        long over = System.currentTimeMillis();
        System.out.println("使用的時間為： "
            + (over - begin)/1000 + " 秒 " ); 
		}
		finally {
	        if( mapWriter != null ) {
	                mapWriter.close();
	        }
	    }
	}

	
	//select the same stno
	private List<CliSum> createResultList(List<CliSum> clisumList, int i ){
		List<CliSum> resultList = new ArrayList<>();
		for(CliSum r:clisumList) {
			if (stnList.get(i).getStno().equals(r.getStno())) {
				CliSum temp = r;
				resultList.add(temp);
			}
 		}
		return resultList;
	}
	
	
	//calculate 
	private List<CliSum> addLossMonth(List<CliSum> countList, String pattern){
		List<Integer> rawDataMonth = new ArrayList<>();
		List<Integer> fullDataMonth = new ArrayList<>();
		List<Integer> reduceData = new ArrayList<>();
		//cancel station or not
		int stnEndMonth = (countList.get(0).getStnEndTime()==null || "".equals(countList.get(0).getStnEndTime()))
				? month : 
					(year == Integer.parseInt(countList.get(0).getStnEndTime().substring(0, 4))?
							Integer.parseInt(countList.get(0).getStnEndTime().substring(5, 7)):month);
		int rowRange = (stnEndMonth < month) ? stnEndMonth : month;
		
		//control cancel station
		for(int j=1; j<=rowRange; j++) {
			fullDataMonth.add(j);
		}
		for(CliSum s: countList) {
			rawDataMonth.add(s.getObsTime().getMonthValue());
		}
		//if countList obsTime data is not full
		if(rawDataMonth.size() < fullDataMonth.size()) {
			reduceData = fullDataMonth.stream()
					.filter(item -> !rawDataMonth.contains(item)).collect(Collectors.toList());
			for(int q=0; q<reduceData.size(); q++) {
				CliSum t = new CliSum();
				t.setStno(countList.get(q).getStno());
				t.setStnCName(countList.get(q).getStnCName());
			    t.setObsTime(LocalDateTime.of(year, reduceData.get(q), 1, 0, 00, 00, 00));
			    t.setStnEndTime(countList.get(q).getStnEndTime());
			    t.setManObsNum(countList.get(q).getManObsNum());
			    
			    if(pattern == "hourSum") {
			    	t.setPrecpHourSum("0");
			    }
			    else if(pattern == "precpDays_1mm") {
			    	t.setPrecpDays_1mm("0");
			    }
			    else if(pattern == "precpDays_10mm") {
			    	t.setPrecpDays_10mm("0");
			    }
			    else if(pattern == "precpDays_01mm") {
			    	t.setPrecpDays_01mm("0");
			    }
			    else if(pattern == "txMinAbs_LE0") {
			    	t.setTxMinAbs_LE0("0");
			    }
			    else if(pattern == "txMinAbs_LE10") {
			    	t.setTxMinAbs_LE10("0");
			    }
			    else if(pattern == "txMinAbs_GE20") {
			    	t.setTxMinAbs_GE20("0");
			    }
			    else if(pattern == "txMaxAbs_GE25") {
			    	t.setTxMaxAbs_GE25("0");
			    }
			    else if(pattern == "txMaxAbs_GE30") {
			    	t.setTxMaxAbs_GE30("0");
			    }
			    else if(pattern == "txMaxAbs_GE35") {
			    	t.setTxMaxAbs_GE35("0");
			    }
			    else if(pattern == "precpDay") {
			    	t.setPrecpDay("0");
			    }
        	    else if(pattern == "hazeDay") {
        	    	t.setStatO2(((t.getManObsNum().equals("0")) ? null : "0"));
        	    }
        	    else if(pattern == "hailDay") {
        	    	t.setStatF3(((t.getManObsNum().equals("0")) ? null : "0"));
        	    }
        	    else if(pattern == "dewDay") {
        	    	t.setStatP2(((t.getManObsNum().equals("0")) ? null : "0"));
        	    }
        	    else if(pattern == "snowDate") {
        	    	t.setStatF1_MinTime(null);
        	    	t.setStatF1_MaxTime(null);
        	    }
        	    else if(pattern == "frostDate") {
        	    	t.setStatF2_MinTime(null);
        	    	t.setStatF2_MaxTime(null);
        	    }
			    countList.add(t);  
			}
			//countList sort by ObsTime
			Collections.sort(countList, (p1, p2) -> p1.getObsTime().compareTo(p2.getObsTime()));
		}
		return countList; 
	}
	
	//add stns data which not in count result list
		public List<CliSum> addStnCount(List<CliSum> stnsXml, List<CliSum> txMinAbsList, String pattern){
	        // reduce (stnsXml - txMinAbsList)
	        List<CliSum> reduceStn = stnsXml.stream()
	        		.filter(item -> !txMinAbsList.contains(item)).collect(Collectors.toList()); //size=26
	        List<CliSum> resultList = new ArrayList<>();
	        
			for(CliSum rs: reduceStn) {
				//cancel station or not
				int stnEndMonth = (rs.getStnEndTime()==null || "".equals(rs.getStnEndTime()))
						? month : Integer.parseInt(rs.getStnEndTime().substring(5, 7));
				int rowRange = (stnEndMonth < month) ? stnEndMonth : month;
				
				for(int m=1; m<=rowRange; m++) {
		        	CliSum s = new CliSum();
		        	s.setStno(rs.getStno());
		        	s.setStnCName(rs.getStnCName());
	        		s.setObsTime(LocalDateTime.of(year, m, 1, 0, 00, 00, 00));
	        		s.setStnEndTime(rs.getStnEndTime());
	        		s.setManObsNum(rs.getManObsNum());
	        		
	        		if(pattern == "hourSum") {
	        			s.setPrecpHourSum(((s.getManObsNum().equals("0")) ? null : "0"));
	        		}
	        		else if(pattern == "precpDays_1mm") {
				    	s.setPrecpDays_1mm("0");
				    }
				    else if(pattern == "precpDays_10mm") {
				    	s.setPrecpDays_10mm("0");
				    }
				    else if(pattern == "precpDays_01mm") {
				    	s.setPrecpDays_01mm("0");
				    }
				    else if(pattern == "txMinAbs_LE0") {
	        			s.setTxMinAbs_LE0("0");
	        		}
	        		else if (pattern == "txMinAbs_LE10") {
	        			s.setTxMinAbs_LE10("0");
	        		}
	        		else if (pattern == "txMinAbs_GE20") {
	        			s.setTxMinAbs_GE20("0");
	        		}
	        		else if (pattern == "txMaxAbs_GE25") {
	        			s.setTxMaxAbs_GE25("0");
	        		}
	        		else if (pattern == "txMaxAbs_GE30") {
	        			s.setTxMaxAbs_GE30("0");
	        		}
	        		else if (pattern == "txMaxAbs_GE35") {
	        			s.setTxMaxAbs_GE35("0");
	        		}
	        	    else if(pattern == "precpDay") {
				    	s.setPrecpDay("0");
				    }
	        	    else if(pattern == "hazeDay") {
	        	    	s.setStatO2(((s.getManObsNum().equals("0")) ? null : "0"));
	        	    }
	        	    else if(pattern == "hailDay") {
	        	    	s.setStatF3(((s.getManObsNum().equals("0")) ? null : "0"));
	        	    }
	        	    else if(pattern == "dewDay") {
	        	    	s.setStatP2(((s.getManObsNum().equals("0")) ? null : "0"));
	        	    }
	        	    else if(pattern == "snowDate") {
	        	    	s.setStatF1_MinTime(null);
	        	    	s.setStatF1_MaxTime(null);
	        	    }
	        	    else if(pattern == "frostDate") {
	        	    	s.setStatF2_MinTime(null);
	        	    	s.setStatF2_MaxTime(null);
	        	    }
	        		resultList.add(s);
		        }
	        }
	        List<CliSum> newList = Stream.of(resultList, txMinAbsList)
	                .flatMap(x -> x.stream())
	                .collect(Collectors.toList());
	        return newList;
		}
	
	//special weather condition
	private  List<CliSum> setWeatherCondition(List<CliSum> clisumList){
		List<CliSum> resultList = new ArrayList<>();
		for(CliSum r:clisumList) {
			CliSum temp = r;	
			temp.setThunderstormDay((temp.getManObsNum().equals("0")) ? null : temp.getThunderstormDay());
			temp.setFogDay((temp.getManObsNum().equals("0")) ? null : temp.getFogDay());
			resultList.add(temp);
		}
		return resultList;
	}
	
	//setting file name
    private String filename(int page, List<Station> stnList, int index) {
        String englishName = stnList.get(index).getStnEName();
        englishName = (englishName != null && !englishName.trim().isEmpty())
                      ? Character.toUpperCase(englishName.charAt(0)) + englishName.substring(1).toLowerCase()
                      : "";
        return "1_3_" + page + "_" + stnList.get(index).getStnCName() + "_" + englishName;
    }
}