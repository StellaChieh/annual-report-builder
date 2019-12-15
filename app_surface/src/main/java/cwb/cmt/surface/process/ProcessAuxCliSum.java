package cwb.cmt.surface.process;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Service;

import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.CliSum;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.service.CreateCsvForAuxCliSum;
import cwb.cmt.surface.service.CreateCsvForStn;
import cwb.cmt.surface.service.CreateTableImage;
import cwb.cmt.surface.utils.PageEncode;
import cwb.cmt.surface.utils.SpecialValue;
import cwb.cmt.surface.utils.StatisticalData;
import cwb.cmt.surface.utils.NumberConvert;
import cwb.cmt.surface.utils.Numbers;


@Service("processAuxCliSum")
public class ProcessAuxCliSum extends Process {
	//output filename
	private static final String PREFIX_RADSUN = String.valueOf(PageEncode.RADSUN.getFilename());
	private static final String PREFIX_GLOBALRAD = String.valueOf(PageEncode.GLOBALRAD.getFilename());
	private static final String PREFIX_RADMAX = String.valueOf(PageEncode.RADMAX.getFilename());
	private static final int RAD_COUNT_PER_PAGE = Numbers.GLOBALRAD_PER_PAGE_RADS.getNumber();
	private static final int RADHRMAX_COUNT_PER_PAGE = Numbers.RADHRMAX_PER_PAGE_RADS.getNumber();
	
	
	@Resource(name="createTableImageForAuxCliSum")
	CreateTableImage drawer;
	
	@Resource(name="createTableImageForAuxCliSum_GlobalRad")
	CreateTableImage drawer_GlobalRad;
	
	@Resource(name="createTableImageForAuxCliSum_RadHrMax")
	CreateTableImage drawer_RadHrMax;

	@Resource(name="statisticalData")
	StatisticalData statisticalData;

	@Resource(name="processAbstract")
	ProcessAbstract processAbstract;
	
	@Resource(name="createCsvForAuxCliSum_GlobalRad")
	CreateCsvForAuxCliSum radSunWriter;
	
	@Resource(name="createCsvForAuxCliSum_GlobalRad")
	CreateCsvForAuxCliSum globalRadWriter;
	
	@Resource(name="createCsvForAuxCliSum_GlobalRad")
	CreateCsvForAuxCliSum radHrMaxWriter;
	
	//Time: month ; config setting: cmt.month=2
    @Resource(name="month")
    protected int month;
	
	public void runRadSun(){
		prepareConfig_Rad();
		//get page start
		int pageStart = processAbstract.getPageStart();
		int pageNumber = 0;
		drawer.setPath(outputTmpPdfPath);
		
		for(int i=0; i<stnList.size(); i++, pageNumber++){
			System.out.println("draw AuxCliSum Page: "+ (i+1));
			//set Filename
			drawer.setFilename(filename(pageNumber+1, stnList, i));
			
			boolean isFirstPage = (i == 0) ? true : false;
			boolean isLastPage = (i == i - 1) ? true : false;
			String TableCNum = "第  " +NumberConvert.digit2ChineseString(i+1)+ "  表   ";
			String TableENum = "  Table " + (i+1) + "  ";
			System.out.println("number convert>> "+NumberConvert.digit2ChineseString(i+1));
			
			//filter radList with the same Stno, not convert special value yet
			List<AuxCliSum> result = new ArrayList<>();
			for(AuxCliSum r:radList) {
				if (stnList.get(i).getStno().equals(r.getStno())) {
					AuxCliSum temp = r;
					result.add(temp);
				}
			}
			
			//convert to special value
			List<AuxCliSum> specialValueList = new ArrayList<>();
			for(AuxCliSum rads:result) {
				AuxCliSum t = rads;
				t.setSunshine(SpecialValue.conversion(t.getSunshine()));
				t.setGlobalRad(SpecialValue.conversion(t.getGlobalRad()));
				specialValueList.add(t);
			}
			
			Map<String, List<AuxCliSum>> countingGroup = specialValueList.stream().collect(
	                Collectors.groupingBy(AuxCliSum::getStno, Collectors.toList()));
			for(String key:countingGroup.keySet()) {
				Collections.sort(countingGroup.get(key), (p1, p2) -> p1.getObsTime().compareTo(p2.getObsTime()));
			}
			
			//createTable
			for(String k:countingGroup.keySet()) {
				//statistic sum data and mean data
				List<List<String>> radStatisticList = new ArrayList<>();
				List<List<String>> sunStatisticList = new ArrayList<>();
				radStatisticList = statisticalData.StatisticalRadSun(countingGroup.get(k), "rad");
				sunStatisticList = statisticalData.StatisticalRadSun(countingGroup.get(k), "sunshine");
				if (isLastPage) {
					drawer.createTableImage(isFirstPage, isLastPage, TableCNum, TableENum,
							stnList.get(i).getStnCName(), stnList.get(i).getStnEName(), 
							countingGroup.get(k), stnList.get(i).getStno(), radStatisticList,
							sunStatisticList, (pageStart + pageNumber));
				}else {
					drawer.createTableImage(isFirstPage, isLastPage, TableCNum, TableENum, 
							stnList.get(i).getStnCName(), stnList.get(i).getStnEName(),
							countingGroup.get(k), stnList.get(i).getStno(), radStatisticList,
							sunStatisticList, (pageStart + pageNumber));
				}
			}

			//update page number
			processAbstract.increasePageNumber();
			System.out.println("...draw radiation and sunshine completed...");
		} 
	}

	
	public void runGlobalRad(){
		List<List<Station>> stnGroups = new ArrayList<>();
		prepareConfig_GlobalRad();
		drawer_GlobalRad.setPath(outputTmpPdfPath);
		if(stnList.size() <= RAD_COUNT_PER_PAGE){
			stnGroups.add(stnList);
		} else {
			int groupCount = (int)Math.ceil(stnList.size()/(double)RAD_COUNT_PER_PAGE);
			for(int i=0; i<groupCount; i++){
				if (i == groupCount-1){
					stnGroups.add(stnList.subList(i*RAD_COUNT_PER_PAGE, stnList.size()));
				} else {
					stnGroups.add(stnList.subList(i*RAD_COUNT_PER_PAGE, (i+1)*RAD_COUNT_PER_PAGE));
				}
			}
		}

		//find monthList intersection with globalradList
        Map<String, List<AuxCliSum>> countingGroup = globalradList.stream().collect(
                Collectors.groupingBy(AuxCliSum::getStno, Collectors.toList()));
        //annual sum
        Map<String, String> statisticMap = new HashMap<String,String>();
        statisticMap = statisticalData.StatisticalGlobalRad(countingGroup);
        
        Map<String, List<AuxCliSum>> countingGroup2 = convertSpecialValue(addLossMonth(countingGroup, "globalrad"), "globalrad");
        Map<String, List<AuxCliSum>> countingGroupResult = countingGroup2;
		//stnGroups.size = 2
		for(int i=0; i<stnGroups.size(); i++) {
			System.out.println("draw AuxClliSum Page: "+ (i+1));
			//set Filename
			drawer_GlobalRad.setFilename("1_4_2_全天空日射量年表_Annual Report of Total Global Solar Radiation");
			boolean isFirstPage = (i == 0) ? true : false;
			boolean isLastPage = (i == i - 1) ? true : false;
			System.out.println("number convert>> "+NumberConvert.digit2ChineseString(i+1));
			if (isLastPage) {
				drawer_GlobalRad.createTableImage(isFirstPage, isLastPage, 
						countingGroupResult, stnGroups.get(i), statisticMap, processAbstract.getCurrentPageNumber());
			}else {
				drawer_GlobalRad.createTableImage(isFirstPage, isLastPage, 
						countingGroupResult, stnGroups.get(i), statisticMap, processAbstract.getCurrentPageNumber());
			}
			// update page number
			processAbstract.increasePageNumber();
			System.out.println("...draw completed...");
		}
	}

	
	public void runRadHrMax(){
		List<List<Station>> stnGroups = new ArrayList<>();
		prepareConfig_RadHrMax();
		
		drawer_RadHrMax.setPath(outputTmpPdfPath);
		if(stnList.size() <= RADHRMAX_COUNT_PER_PAGE){
			stnGroups.add(stnList);
		} else {
			int groupCount = (int)Math.ceil(stnList.size()/(double)RADHRMAX_COUNT_PER_PAGE);
			for(int i=0; i<groupCount; i++){
				if (i == groupCount-1){
					stnGroups.add(stnList.subList(i*RADHRMAX_COUNT_PER_PAGE, stnList.size()));
				} else {
					stnGroups.add(stnList.subList(i*RADHRMAX_COUNT_PER_PAGE, (i+1)*RADHRMAX_COUNT_PER_PAGE));
				}
			}
		}
		
		//find monthList intersection with globalradList
        Map<String, List<AuxCliSum>> countingGroup = radmaxList.stream().collect(
                Collectors.groupingBy(AuxCliSum::getStno, Collectors.toList()));
		
        //add "@"
        List<Integer> rawDataMonth = new ArrayList<>();
		List<Integer> fullDataMonth = new ArrayList<>();
		List<Integer> reduceData = new ArrayList<>();
		
		for(int j=1; j<=month; j++) {
			fullDataMonth.add(j);
		}
		
        for(List<AuxCliSum> group:countingGroup.values()) {
        	rawDataMonth.clear();
        	for(int c=0; c<group.size(); c++) {
        		//calculate count of month
        		rawDataMonth.add(group.get(c).getObsTime().getMonthValue());
        	}
    		if(rawDataMonth.size()<fullDataMonth.size()) {
    			reduceData = fullDataMonth.stream()
    					.filter(item -> !rawDataMonth.contains(item)).collect(Collectors.toList());
    			for(int q=0; q<reduceData.size(); q++) {
        			AuxCliSum t = new AuxCliSum();
    				t.setStno(group.get(q).getStno());
    				t.setStnCName(group.get(q).getStnCName());
    				t.setObsTime(LocalDateTime.of(year, reduceData.get(q), 1, 0, 00, 00, 00));
    				t.setStnEndTime(group.get(q).getStnEndTime());
    				t.setGlobalRadMax("@");
    				group.add(t);  
        		}
        		//countList sort by ObsTime
    			Collections.sort(group, (p1, p2) -> p1.getObsTime().compareTo(p2.getObsTime()));
    		}
        }
		
		//convert to special value
        for(List<AuxCliSum> group:countingGroup.values()) {
        	for(int o=0; o<group.size(); o++) {
        		group.get(o).setGlobalRadMax(SpecialValue.conversion3(group.get(o).getGlobalRadMax()));
        	}
        }
        Map<String, List<AuxCliSum>> countingGroup3= countingGroup;
		Map<String, List<String>> radHrMaxStatisticList = 
				statisticalData.StatisticalRadHrMax(countingGroup3, stnList);
		//stnGroups.size = 2
		for(int i=0; i<stnGroups.size(); i++) {
			System.out.println("draw AuxClliSum Page: "+ (i+1));
			//set Filename
			drawer_RadHrMax.setFilename("1_4_3_全天空一小時最大日射量年表_Annual Report of Max. Global Solar Radiation in One Hour");
			boolean isFirstPage = (i == 0) ? true : false;
			boolean isLastPage = (i == i - 1) ? true : false;
			System.out.println("number convert>> "+NumberConvert.digit2ChineseString(i+1));

			if (isLastPage) {
				drawer_RadHrMax.createTableImage(isFirstPage, isLastPage, 
						countingGroup3, stnGroups.get(i), radHrMaxStatisticList, processAbstract.getCurrentPageNumber());
			}else {
				drawer_RadHrMax.createTableImage(isFirstPage, isLastPage, 
						countingGroup3, stnGroups.get(i), radHrMaxStatisticList, processAbstract.getCurrentPageNumber());
			// update current page
			processAbstract.increasePageNumber();
			System.out.println("...draw completed...");
			}
		} 
	}
	
	
	//CSV : 1.4.1 Radiation & sunshine
	public void runRadSunCsv(String outputCsvFolder) throws IOException{
		prepareConfig_Rad();
		Map<String, List<AuxCliSum>> radGroup = radList.stream().collect(
				Collectors.groupingBy(AuxCliSum::getStno, Collectors.toList()));
		Map<String, List<AuxCliSum>> radResult = new HashMap<>();
		Map<String, List<List<String>>> radStatistic = new HashMap<>();
		Map<String, List<List<String>>> sunStatistic = new HashMap<>();
		
		
		for(int i=0; i<stnList.size(); i++){
			//filter radList with the same Stno, not convert special value yet
			List<AuxCliSum> result = new ArrayList<>();
			for(AuxCliSum r:radList) {
				if (stnList.get(i).getStno().equals(r.getStno())) {
					AuxCliSum temp = r;
					result.add(temp);
				}
			}
			//statistic sum data and mean data
			List<List<String>> radStatisticList = new ArrayList<>();
			List<List<String>> sunStatisticList = new ArrayList<>();
			for(String stno:radGroup.keySet()) {
				List<AuxCliSum> specialValueList = new ArrayList<>();
				//convert to special value
				if(stno.equals(result.get(0).getStno())) {
					for(AuxCliSum rads:result) {
						AuxCliSum t = rads;
						t.setSunshine(SpecialValue.conversion(t.getSunshine()));
						t.setGlobalRad(SpecialValue.conversion(t.getGlobalRad()));
						specialValueList.add(t);
					}
					Map<String, List<AuxCliSum>> countingGroup = specialValueList.stream().collect(
			                Collectors.groupingBy(AuxCliSum::getStno, Collectors.toList()));
					for(String key:countingGroup.keySet()) {
						Collections.sort(countingGroup.get(key), (p1, p2) -> p1.getObsTime().compareTo(p2.getObsTime()));
					}
					for(String k:countingGroup.keySet()) {
						radResult.put(stno, countingGroup.get(k));
						radStatisticList = statisticalData.StatisticalRadSun(countingGroup.get(k), "rad");
						sunStatisticList = statisticalData.StatisticalRadSun(countingGroup.get(k), "sunshine");
						radStatistic.put(result.get(0).getStno(), radStatisticList);
						sunStatistic.put(result.get(0).getStno(), sunStatisticList);
					}
				}
			}
		} 
		//generate csv
		radSunWriter.createRadSunCsv(radResult, stnList, radStatistic, sunStatistic, outputCsvFolder);
		System.out.println("...draw radiation & sunshine completed...");
	}
	
	
	//CSV : 1.4.2-1.4.3 globalrad & radhrmax
	public void runGlobalRadCsv(String outputCsvFolder) throws IOException{
		List<List<Station>> stnGroups = new ArrayList<>();
		prepareConfig_GlobalRad();
		prepareConfig_RadHrMax();
		//find monthList intersection with globalradList
        Map<String, List<AuxCliSum>> globalRadGroup = globalradList.stream().collect(
                Collectors.groupingBy(AuxCliSum::getStno, Collectors.toList()));
        Map<String, List<AuxCliSum>> radHrGroup = radmaxList.stream().collect(
                Collectors.groupingBy(AuxCliSum::getStno, Collectors.toList()));
        //annual sum
        Map<String, List<AuxCliSum>> globalRadMap = convertSpecialValue(addLossMonth(globalRadGroup, "globalrad"), "globalrad");
        Map<String, String> globalRadStatistic = new HashMap<String,String>();
        globalRadStatistic = statisticalData.StatisticalGlobalRad(globalRadMap);
        Map<String, List<AuxCliSum>> radHrMap = convertSpecialValue(addLossMonth(radHrGroup, "radhrmax"), "radhrmax");
        Map<String, List<String>> radHrMaxResult = 
				statisticalData.StatisticalRadHrMax(radHrMap, stnList);
        //generate csv
        globalRadWriter.createGlobalRadHrCsv(globalRadMap, globalRadStatistic, stnList, 
        		radHrMap, radHrMaxResult, outputCsvFolder);
		System.out.println("...draw GlobalRad csv & RadHrMax csv completed...");
	}
	
	
	 private String filename(int position, List<Station> stnList, int index) {
        String englishName = stnList.get(index).getStnEName();
        englishName = (englishName != null && !englishName.trim().isEmpty())
                      ? Character.toUpperCase(englishName.charAt(0)) + englishName.substring(1).toLowerCase()
                      : "";
	    return "1_4_1_" + position + "_" + stnList.get(index).getStnCName() + "_" + englishName;
	}
	
	 
	//cancel station
	private Map<String, List<AuxCliSum>> addLossMonth(Map<String, List<AuxCliSum>> countingGroup, String pattern) {
	    //add "@"
        List<Integer> rawDataMonth = new ArrayList<>();
		List<Integer> fullDataMonth = new ArrayList<>();
		List<Integer> reduceData = new ArrayList<>();
		
		for(int j=1; j<=month; j++) {
			fullDataMonth.add(j);
		}
        for(List<AuxCliSum> group:countingGroup.values()) {
        	rawDataMonth.clear();
        	for(int c=0; c<group.size(); c++) {
        		//calculate count of month
        		rawDataMonth.add(group.get(c).getObsTime().getMonthValue());
        	}
    		if(rawDataMonth.size()<fullDataMonth.size()) {
    			reduceData = fullDataMonth.stream()
    					.filter(item -> !rawDataMonth.contains(item)).collect(Collectors.toList());
    			if(pattern=="globalrad") {
    				for(int q=0; q<reduceData.size(); q++) {
            			AuxCliSum t = new AuxCliSum();
        				t.setStno(group.get(q).getStno());
        				t.setStnCName(group.get(q).getStnCName());
        				t.setObsTime(LocalDateTime.of(year, reduceData.get(q), 1, 0, 00, 00, 00));
        				t.setStnEndTime(group.get(q).getStnEndTime());
        				t.setGlobalRad("@");
        				t.setGloblRad_flag("0");
        				group.add(t);  
            		}
    			}
    			else if(pattern=="radhrmax") {
    				for(int q=0; q<reduceData.size(); q++) {
            			AuxCliSum t = new AuxCliSum();
        				t.setStno(group.get(q).getStno());
        				t.setStnCName(group.get(q).getStnCName());
        				t.setObsTime(LocalDateTime.of(year, reduceData.get(q), 1, 0, 00, 00, 00));
        				t.setStnEndTime(group.get(q).getStnEndTime());
        				t.setGlobalRadMax("@");
        				group.add(t);  
            		}
    			}
        		//countList sort by ObsTime
    			Collections.sort(group, (p1, p2) -> p1.getObsTime().compareTo(p2.getObsTime()));
    		}
        } 
        return countingGroup;
	}
	
	
	//special value: globalrad & radhrmax
	private Map<String, List<AuxCliSum>> convertSpecialValue(Map<String, List<AuxCliSum>> countingGroup, String pattern) {
        //convert to special value
		if(pattern=="globalrad") {
			  for(List<AuxCliSum> group:countingGroup.values()) {
		        	for(int o=0; o<group.size(); o++) {
		        		group.get(o).setGlobalRad(SpecialValue.conversion2(group.get(o).getGlobalRad(),
			    				group.get(o).getGloblRad_flag()));
		        	}
		        }
		}
		else if(pattern=="radhrmax") {
		     for(List<AuxCliSum> group:countingGroup.values()) {
		        	for(int o=0; o<group.size(); o++) {
		        		group.get(o).setGlobalRadMax(SpecialValue.conversion3(group.get(o).getGlobalRadMax()));
		        	}
		        }
		}
        return countingGroup;
	}
	 
	@Override
	public void run() throws IOException {
		// TODO Auto-generated method stub
	}
}
