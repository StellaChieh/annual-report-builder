package cwb.cmt.surface.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.Station;

@Service("createCsvForAuxCliSum_GlobalRad")
public class CreateCsvForAuxCliSum {
    @Resource(name="year")
    protected int year;
    @Resource(name="month")
    protected int month;
	protected String path;
	protected String filename;
	//18
	private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] { 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                null, 
                null, 
                null, 
                null, 
                null, 
                null, 
                null,
                null, 
                null, 
                null, 
                null, 
                null, 
                null, 
        };
        return processors;
	}
	//39
	private static CellProcessor[] getProcessorss() {
        final CellProcessor[] processors = new CellProcessor[] { 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                null, 
                null, 
                null, 
                null, 
                null, 
                null,
                null, 
                null, 
                null, 
                null, 
                null, 
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
        };
        return processors;
	}
	
	
	public void createRadSunCsv(Object...objects) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, List<AuxCliSum>> radGroup= (Map<String, List<AuxCliSum>>)objects[0];
		@SuppressWarnings("unchecked")
		List<Station> stn = (List<Station>) objects[1];
		@SuppressWarnings("unchecked")
		Map<String, List<List<String>>> radStatistic = (Map<String, List<List<String>>>)objects[2];
		@SuppressWarnings("unchecked")
		Map<String, List<List<String>>> sunStatistic = (Map<String, List<List<String>>>)objects[3];
		String outputCsvFolder = (String) objects[4];
		final String[] header = new String[] {"測站中文名/StnCName", "測站英文名/StnEName", "氣象要素/Meteorological Element", 
				"單位/Unit", "年份/Year", "月份/Month", "1日/Date", "2日/Date", "3日/Date", "4日/Date", "5日/Date",
				"6日/Date", "7日/Date", "8日/Date", "9日/Date", "10日/Date", "11日/Date", "12日/Date", "13日/Date",
				"14日/Date", "15日/Date", "16日/Date", "17日/Date", "18日/Date", "19日/Date", "20日/Date",
				"21日/Date", "22日/Date", "23日/Date", "24日/Date", "25日/Date", "26日/Date", "27日/Date",
				"28日/Date", "29日/Date", "30日/Date", "31日/Date", "月總計/Total", "日平均/Mean"};
		
		ICsvMapWriter mapWriter = null;
		try {
			String outputPath = Paths.get(outputCsvFolder, year+"年全天空日射量及日照時數.csv").toString();
			FileOutputStream fileStream = new FileOutputStream(new File(outputPath), true);
			OutputStreamWriter fileWriter = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
//			FileWriter fileWriter = new FileWriter(outputPath);
			fileWriter.write('\ufeff');
			mapWriter = new CsvMapWriter(fileWriter,
	                CsvPreference.STANDARD_PREFERENCE);
			final CellProcessor[] processors = getProcessorss();
			// write the header
		    mapWriter.writeHeader(header);
			//monthly data: global rad
		    for(int stno=0; stno<stn.size(); stno++) {
				for(String key:radGroup.keySet()) {
					if(stn.get(stno).getStno().equals(radGroup.get(key).get(0).getStno())) {
						Map<String, Object> radMap  = new HashMap<String, Object>();
						radMap.put(header[0], radGroup.get(key).get(0).getStnCName());
						radMap.put(header[1], stn.get(stno).getStnEName());
						radMap.put(header[2], "全天空日射量");
						radMap.put(header[3], "MJ/㎡");
						radMap.put(header[4], year);
						
						int stnEndMonth = (radGroup.get(key).get(0).getStnEndTime()==null ||
								"".equals(radGroup.get(key).get(0).getStnEndTime()))
								? month : 
									(year == Integer.parseInt(radGroup.get(key).get(0).getStnEndTime().substring(0, 4))?
											Integer.parseInt(radGroup.get(key).get(0).getStnEndTime().substring(5, 7)):month);
						int monthRange = (stnEndMonth < month) ? stnEndMonth : month;
						
						for(int mon=0; mon<monthRange; mon++) {
							List<AuxCliSum> radList = new ArrayList<>();
							int countOfDay = 0;
							for(AuxCliSum rad:radGroup.get(key)) {
								if(key.equals(rad.getStno()) && (mon+1)==rad.getObsTime().getMonthValue()) {
									radList.add(rad);
								}
							}
							countOfDay=radList.size();
							radMap.put(header[5],radList.get(0).getObsTime().getMonthValue());
							for(int i=0; i<countOfDay; i++) {
				    			String data = radList.get(i).getGlobalRad();
				    			radMap.put(header[6+i], data);
				    		}
							if((31-countOfDay)!=0) { //day=28
					    		for(int d=1; d<=(31-countOfDay); d++) {
					    			String data = "";
					    			radMap.put(header[37-d], data);
					    		}
							}
							radMap.put(header[37], radStatistic.get(key).get(0).get(mon));
							
						    radMap.put(header[38], radStatistic.get(key).get(1).get(mon));
						    // write the customer maps
						    mapWriter.write(radMap, header, processors);
						}
					}
				}
		    }
		  //monthly data: sunshine
		    for(int stno=0; stno<stn.size(); stno++) {
				for(String key:radGroup.keySet()) {
					if(stn.get(stno).getStno().equals(radGroup.get(key).get(0).getStno())) {
						Map<String, Object> radMap  = new HashMap<String, Object>();
						radMap.put(header[0], radGroup.get(key).get(0).getStnCName());
						radMap.put(header[1], stn.get(stno).getStnEName());
						radMap.put(header[2], "日照時數");
						radMap.put(header[3], "Hour");
						radMap.put(header[4], year);
						int stnEndMonth = (radGroup.get(key).get(0).getStnEndTime()==null ||
								"".equals(radGroup.get(key).get(0).getStnEndTime()))
								? month : 
									(year == Integer.parseInt(radGroup.get(key).get(0).getStnEndTime().substring(0, 4))?
											Integer.parseInt(radGroup.get(key).get(0).getStnEndTime().substring(5, 7)):month);
						int monthRange = (stnEndMonth < month) ? stnEndMonth : month;
						
						for(int mon=0; mon<monthRange; mon++) {
							List<AuxCliSum> radList = new ArrayList<>();
							int countOfDay = 0;
							for(AuxCliSum rad:radGroup.get(key)) {
								if(key.equals(rad.getStno()) && (mon+1)==rad.getObsTime().getMonthValue()) {
									radList.add(rad);
								}
							}
							countOfDay=radList.size();
							radMap.put(header[5],radList.get(0).getObsTime().getMonthValue());
							for(int i=0; i<countOfDay; i++) {
				    			String data = radList.get(i).getSunshine();
				    			radMap.put(header[6+i], data);
				    		}
							if((31-countOfDay)!=0) { //day=28
					    		for(int d=1; d<=(31-countOfDay); d++) {
					    			String data = "";
					    			radMap.put(header[37-d], data);
					    		}
							}
							radMap.put(header[37], sunStatistic.get(key).get(0).get(mon));
							
						    radMap.put(header[38], sunStatistic.get(key).get(1).get(mon));
						    // write the customer maps
						    mapWriter.write(radMap, header, processors);
						}
					}
				}
		    }
		}
		finally {
            if( mapWriter != null ) {
                    mapWriter.close();
            }
        }
	}
	
	public void createGlobalRadHrCsv(Object...objects) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, List<AuxCliSum>> globalRad = (Map<String, List<AuxCliSum>>) objects[0];
		@SuppressWarnings("unchecked")
		Map<String, String> sumGlobalRad = (Map<String, String>) objects[1];
		@SuppressWarnings("unchecked")
		List<Station> stn = (List<Station>) objects[2];
		@SuppressWarnings("unchecked")
		Map<String, List<AuxCliSum>> radHrMax = (Map<String, List<AuxCliSum>>) objects[3];
		@SuppressWarnings("unchecked")
		Map<String, List<String>> radHrMaxAnnual = (Map<String, List<String>>)objects[4];
		String outputCsvFolder = (String) objects[5];
		
		final String[] header = new String[] {"站號/StationNo.", "測站中文名/StnCName", "氣象要素/Meteorological Element", 
				"單位/Unit", "年份/Year", "一月/Jan.", "二月/Feb.", "三月/Mar.", "四月/Apr.", "五月/May",
				"六月/Jun.", "七月/Jul.", "八月/Aug.", "九月/Sep.", "十月/Oct.", "十一月/Nov.", "十二月/Dec.", "全年/Annual"};
		
		ICsvMapWriter mapWriter = null;
		try {
			String outputPath = Paths.get(outputCsvFolder, year+"年全天空日射量與一小時最大日射量.csv").toString();
			FileOutputStream fileStream = new FileOutputStream(new File(outputPath), true);
			OutputStreamWriter fileWriter = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
//			FileWriter fileWriter = new FileWriter(outputPath);
			fileWriter.write('\ufeff');
			mapWriter = new CsvMapWriter(fileWriter,
	                CsvPreference.STANDARD_PREFERENCE);
			final CellProcessor[] processors = getProcessors();
			// write the header
		    mapWriter.writeHeader(header);
		    //globalrad
		    for(int stno=0; stno<stn.size(); stno++) {
		    	//annual sum: global rad
		    	String sumResult = null;
				for(String key:sumGlobalRad.keySet()) {  
					if (stn.get(stno).getStno().equals(key)) {
						sumResult = sumGlobalRad.get(key);
					}
				}
				//monthly data: global rad
				for(String key:globalRad.keySet()) {
					if(stn.get(stno).getStno().equals(globalRad.get(key).get(0).getStno())) {
						Map<String, Object> globalRadMap  = new HashMap<String, Object>();
						globalRadMap.put(header[0], globalRad.get(key).get(0).getStno());
					    globalRadMap.put(header[1], globalRad.get(key).get(0).getStnCName());
					    globalRadMap.put(header[2], "全天空日射量");
					    globalRadMap.put(header[3], "MJ/㎡");
					    globalRadMap.put(header[4], year);
					    for(int i=0; i<globalRad.get(key).size(); i++) {
					    	String data = globalRad.get(key).get(i).getGlobalRad();
					    	globalRadMap.put(header[5+i], data);
					    }
					    globalRadMap.put(header[17], sumResult);
					    // write the customer maps
					    mapWriter.write(globalRadMap, header, processors);
					}
				}
		    }
			//radhrmax	
		    for(int s=0; s<stn.size(); s++) {
				//annual: Rad Hr Max
		    	List<String> radHrMaxResult = null;
				for(String key:radHrMaxAnnual.keySet()) {  
					if (stn.get(s).getStno().equals(key)) {
						radHrMaxResult = radHrMaxAnnual.get(key);
					}
				}
				//monthly data: Rad Hr Max
				for(String key:radHrMax.keySet()) {
					if(stn.get(s).getStno().equals(radHrMax.get(key).get(0).getStno())) {
						Map<String, Object> radHrMaxMap  = new HashMap<String, Object>();
						radHrMaxMap.put(header[0], radHrMax.get(key).get(0).getStno());
					    radHrMaxMap.put(header[1], radHrMax.get(key).get(0).getStnCName());
					    radHrMaxMap.put(header[2], "全天空一小時最大日射量\\日");
					    radHrMaxMap.put(header[3], "MJ/㎡");
					    radHrMaxMap.put(header[4], year);
					    for(int i=0; i<radHrMax.get(key).size(); i++) {
					    	String data = radHrMax.get(key).get(i).getGlobalRadMax();
					    	String dayOfMonth = (radHrMax.get(key).get(i).getGlobalRadHrMaxTime()!=null)?
					    			String.valueOf(radHrMax.get(key).get(i).getGlobalRadHrMaxTime().getDayOfMonth()):
					    				"";
					    	radHrMaxMap.put(header[5+i], 
					    			data + "\\" + dayOfMonth);
					    }
					    radHrMaxMap.put(header[17], radHrMaxResult.get(0)+"\\"+radHrMaxResult.get(1));
					    // write the customer maps
					    mapWriter.write(radHrMaxMap, header, processors);
					}
				}
				
		    }
		}
		finally {
            if( mapWriter != null ) {
                    mapWriter.close();
            }
        }
	}
	
	
	public void createRadHrMaxCsv(Object...objects) throws IOException {
		
	}
}
