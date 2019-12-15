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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import cwb.cmt.surface.dao.StationDao;
import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.ClimaticElement;
import cwb.cmt.surface.model.MeanStationValues;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.service.CreateCsvForMeanStationOneValueTime;
import cwb.cmt.surface.service.CreateCsvForMeanStationTwoValueTime;
import cwb.cmt.surface.service.CreateCsvForMeanStationTwoValues;
import cwb.cmt.surface.service.CreateCsvForMeanStationValues;
import cwb.cmt.surface.service.CreateTableImage;
import static cwb.cmt.surface.utils.ConvertFileName.encodeFilename;
import cwb.cmt.surface.utils.Numbers;
import cwb.cmt.surface.utils.PageEncode;
import cwb.cmt.surface.utils.ParseCeXml;
import cwb.cmt.surface.utils.ParseStnXml;
import cwb.cmt.surface.utils.SpecialValue;
import cwb.cmt.surface.utils.StatisticalData;

@Service("processCe")
public class ProcessCe extends Process{
	
	private static final String PREFIX = String.valueOf(PageEncode.CE.getFilename());
	private int CE_PER_PAGE = 0;
	
	@Resource(name="createTableImageForMeanStationValues")
	CreateTableImage drawer;
	
	@Resource(name="createTableImageForMeanStationTwoValues")
	CreateTableImage drawer_twoValues;
	
	@Resource(name="createTableImageForMeanStationOneValueTime")
	CreateTableImage drawer_OneValueTime;
	
	@Resource(name="createTableImageForMeanStationTwoValueTime")
	CreateTableImage drawer_twoValueTime;
	
	@Resource(name="parseCeXml")
	ParseCeXml parseCeXml;
	
	@Resource(name="statisticalData")
	StatisticalData statisticalData;
	
	@Resource(name="processAbstract")
	ProcessAbstract processAbstract;
	
	@Resource(name="createCsvForMeanStationValues")
	CreateCsvForMeanStationValues csvWriter;
	
	@Resource(name="createCsvForMeanStationTwoValues")
	CreateCsvForMeanStationTwoValues csvWriter_twoValues;
	
	@Resource(name="createCsvForMeanStationOneValueTime")
	CreateCsvForMeanStationOneValueTime csvWriter_OneValueTime;
	
	@Resource(name="createCsvForMeanStationTwoValueTime")
	CreateCsvForMeanStationTwoValueTime csvWriter_TwoValuesTime;
	
	//Time: month ; config setting: cmt.month=2
    @Resource(name="month")
    protected int month;
    
    List<String> ce_PerPageList = 
			Arrays.asList("stnPres", "Tx", "Precp", "PrecpDay");
    List<String> ce_PerPageList2 = 
			Arrays.asList("MeanTxMax", "TxMaxAbs", "MeanTxMin", "TxMinAbs", "RH", "RHMin",
					"Precp1DayMax", "WSWD",
					"Evaporation", "MaxWS", "MaxWSGust", "CloudAmountMean", "SunshineDuration",
					"GlobalSolarRadiation");
    int titleIndex = 3;
	public void run() throws IOException{
		prepareConfig_CE();
		drawer.setPath(outputTmpPdfPath);
		drawer_twoValues.setPath(outputTmpPdfPath);
		drawer_OneValueTime.setPath(outputTmpPdfPath);
		drawer_twoValueTime.setPath(outputTmpPdfPath);
		//control climatic element type: including stnPres, tx ...
		//count of climatic element
		for(int i=0; i<ceXmlList.size(); i++) {  
			if(ce_PerPageList.contains(ceXmlList.get(i).getId())) {
				CE_PER_PAGE = Numbers.CLIMATICELEMENT_PER_PAGE.getNumber(); //63
			}
			else {
				CE_PER_PAGE = Numbers.CLIMATICELEMENT2_PER_PAGE.getNumber(); //31
			}
			
			List<String> keyList = new ArrayList<>();
			List<List<String>> ceGroups = new ArrayList<>();
			System.out.println("第幾種氣象要素: "+ (i+1));
			System.out.println("氣象要素類型: " + ceXmlList.get(i).getId());
			System.out.println("是否取代為0: " + ceXmlList.get(i).getSubstituteZero());
			System.out.println("precision: "+ceXmlList.get(i).getPrecision());
			
			List<MeanStationValues> sameColumnId = new ArrayList<>();
			//convert 0 or 0.0 to -
			for(MeanStationValues ce : ceList) {
				if(ceXmlList.get(i).getId().equals(ce.getColumnId())) {
					ce.setSubstituteZero(ceXmlList.get(i).getSubstituteZero());
					sameColumnId.add(ce);
				}
			}
			
			//convert to special value
			List<MeanStationValues> specialValueList = new ArrayList<>();
			List<String> EquipmentMalfunctionList = Arrays.asList(SpecialValue.EquipmentMalfunction.getNumber());
			List<String> CeUnknownList = Arrays.asList(SpecialValue.CeUnknown.getNumber());
			List<String> TraceList = Arrays.asList(SpecialValue.Trace.getNumber());
			List<String> SubstituteZeroList = Arrays.asList(SpecialValue.SubstituteZero.getNumber());
			for(MeanStationValues value : sameColumnId) {
				MeanStationValues v = value;
				v.setMonthlyValue(
						SpecialValue.conversion4(v.getMonthlyValue(),
								v.isSubstituteZero(), v.getFlag(), ceXmlList.get(i).getPrecision()));
				//if monthly value include the following condition, then change columnTime to null.
				if(EquipmentMalfunctionList.contains(v.getMonthlyValue())
						|| CeUnknownList.contains(v.getMonthlyValue())
						|| TraceList.contains(v.getMonthlyValue())
						|| SubstituteZeroList.contains(v.getMonthlyValue()) ) {
					v.setColumnTime(null);
				}
				specialValueList.add(v);
			}
			 Map<String, List<MeanStationValues>> groupColumnNameMaps =
					 specialValueList.stream()
					 .collect(Collectors.groupingBy(MeanStationValues::getColumnName));

			 //one kind of columnName
			 Map<String,Map<String, List<MeanStationValues>>> groupColumnName = new HashMap<>();
			 Map<String,Map<String, List<MeanStationValues>>> groupsColumnName = new HashMap<>();
			 //WS, WD
			 for(String columnName : groupColumnNameMaps.keySet()) {
				 Map<String, List<MeanStationValues>> groupStnoMaps = 
						 getNewORCancelStns(groupColumnNameMaps.get(columnName).stream()
								 .collect(Collectors.groupingBy(MeanStationValues::getStno)));
				 for(String stno:groupStnoMaps.keySet()) {
					 groupStnoMaps.get(stno).sort((p1, p2) -> p1.getObsTime().compareTo(p2.getObsTime()));
				 }
		         Map<String, List<MeanStationValues>> sortedMap = groupStnoMaps.entrySet().stream()
		        		.sorted(Map.Entry.comparingByKey())
		        		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
		                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
				 groupColumnName.put(columnName,  sortedMap);
				 
			 }
			 groupsColumnName = removeAllSpecialValueStns(groupColumnName);
//			 System.out.println("groupsColumnName>> "+groupsColumnName);
/////////////////////////////groupColumnName is correct!!!!///////////////////////////////////////
			 
	        //annual Statistic maps:{WS:{Stno:annual}, WD:{Stno:annual}}
	        String statisticType = null;
	        Map<String, String> annualSumAvgMap = new HashMap<>();
	        Map<String, MeanStationValues> annualMaxMinMap = new HashMap<>();
	        Map<String, MeanStationValues> annualMaxMinMaps1 = new HashMap<>();
	        Map<String, String> annualMaxCountMap = new HashMap<>();
	        Map<String,Map<String, String>> annualSumAvgMaps = new HashMap<>();
	        Map<String,Map<String, MeanStationValues>> annualMaxMinMaps = new HashMap<>();
	        Map<String, List<MeanStationValues>> groupStnoMap = new HashMap<>();
	        Map<String, List<MeanStationValues>> sortedGroupStnoMap = new HashMap<>();
	        List<MeanStationValues> groupColumnNameList = new ArrayList<>();
	        int countColnameStns = 0;
	        //column name = WS, WD
	        for(String columnName: groupsColumnName.keySet()) {
	        	groupColumnNameList.clear();
	        	//the same columnName like WS
	        	for(List<MeanStationValues> columnNameList:groupsColumnName.get(columnName).values()) {
	        			groupColumnNameList.addAll(columnNameList);
	        	}
//	        	System.out.println("groupColumnNameList>> "+groupColumnNameList);
        		groupStnoMap = groupColumnNameList.stream().collect(Collectors.groupingBy(MeanStationValues::getStno));
        		sortedGroupStnoMap = groupStnoMap.entrySet().stream()
		        		.sorted(Map.Entry.comparingByKey())
		        		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
		                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//        		System.out.println("sortedGroupStnoMap>> "+sortedGroupStnoMap);
        		statisticType = groupColumnNameList.get(0).getStatisticFunction();
        		
        		//same columnName with same StatisticFunction so we use get(0)
        		if(statisticType.equals("SUM") || statisticType.equals("AVG")) {
        			annualSumAvgMap = statisticalData.annualStatisticSumAvg(
    	        			sortedGroupStnoMap, statisticType, ceXmlList.get(i).getPrecision());
        			annualSumAvgMaps.put(columnName, annualSumAvgMap);
        		}
        		else if (statisticType.equals("MAX") || statisticType.equals("MIN")) {
        			System.out.println("statisticType>> "+statisticType);
        			annualMaxMinMap = statisticalData.annualStatisticMaxMin(
    	        			sortedGroupStnoMap, statisticType, ceXmlList.get(i).getPrecision());
        			annualMaxMinMaps.put(columnName, annualMaxMinMap);
        		}
        		else if(statisticType.equals("MAXCOUNT")) {
        			System.out.println("statisticType>> "+statisticType);
        			annualMaxCountMap = statisticalData.annualStatisticMaxCount(
    	        			sortedGroupStnoMap, statisticType, ceXmlList.get(i).getPrecision());
        		}
	        }
	        
	        //get a kind of CE's page counts 
	        for(String colname:groupsColumnName.keySet()) {
	        	keyList.clear();
	        	//collect sorted map's key in list
	        	for(String stno:groupsColumnName.get(colname).keySet()) {
	        		keyList.add(stno);
	        	}
	        	countColnameStns = groupsColumnName.get(colname).values().size();
	        }
			
			//there are 63 stations at per page for one type of climatic element
			if(countColnameStns <= CE_PER_PAGE){
				ceGroups.add(keyList);
			} else {
				int groupCount = (int)Math.ceil(countColnameStns/(double)CE_PER_PAGE);
				for(int c=0; c<groupCount; c++){
					if (c == groupCount-1){
						ceGroups.add(keyList.subList(c*CE_PER_PAGE, countColnameStns));
					} else {
						ceGroups.add(keyList.subList(c*CE_PER_PAGE, (c+1)*CE_PER_PAGE));
					}
				}
			}
//			System.out.println("該氣象要素共有幾頁: "+ ceGroups.size());
			String pageTitleIndex = "2_" + String.valueOf(titleIndex) + "_";
			String innerTitleIndex = "2."+ String.valueOf(titleIndex) +" ";
			//draw per page 
			//ceGroup only include station no.
			for(int j=0; j<ceGroups.size(); j++) {
//			for(int j=0; j<1; j++) {
				boolean isFirstPage = (j == 0) ? true : false;
				boolean isLastPage = (j == ceGroups.size() - 1) ? true : false;
				//set Filename
				drawer.setFilename(encodeFilename(getPageFilename(pageTitleIndex, ceXmlList.get(i).getChineseTitle(), 
						ceXmlList.get(i).getEnglishTitle(), (j+1))));
				drawer_twoValues.setFilename(encodeFilename(getPageFilename(pageTitleIndex, ceXmlList.get(i).getChineseTitle(), 
						ceXmlList.get(i).getEnglishTitle(), (j+1))));
				drawer_OneValueTime.setFilename(encodeFilename(getPageFilename(pageTitleIndex, ceXmlList.get(i).getChineseTitle(), 
						ceXmlList.get(i).getEnglishTitle(), (j+1))));
				drawer_twoValueTime.setFilename(encodeFilename(getPageFilename(pageTitleIndex, ceXmlList.get(i).getChineseTitle(), 
						ceXmlList.get(i).getEnglishTitle(), (j+1))));
//				System.out.println("page: " + (j+1) + ", 該頁測站數: "+ceGroups.get(j));
				
				//{stnPres=List<MeanStationValues>}
				Map<String, List<MeanStationValues>> resultMap = new HashMap<>();
				for(String colName:groupsColumnName.keySet()) {
					//important
					List<MeanStationValues> resultList = new ArrayList<>();
					for(String stno: ceGroups.get(j)) {
						if(groupsColumnName.get(colName).get(stno)!=null) {
							resultList.addAll(groupsColumnName.get(colName).get(stno));
						}
					}
					resultMap.put(colName, resultList);
				}
				
				//with one kind of statistic function only include Max and Min
				List<String> ValuesTimeList = 
						Arrays.asList("TxMaxAbs", "TxMinAbs", "RHMin", "Precp1DayMax");
				List<String> twoValuesTimeList = Arrays.asList("MaxWS", "MaxWSGust");
				List<String> twoValuesList = Arrays.asList("WSWD");
				//decide to use which type of table
				if(ceXmlList.get(i).getNumOfTableRow().equals("1")) {
					drawer.createTableImage(ceXmlList.get(i), resultMap, annualSumAvgMaps, ceGroups.get(j), isFirstPage, isLastPage, 
							innerTitleIndex);
					processAbstract.increasePageNumber();
					
				}
				else if (ceXmlList.get(i).getNumOfTableRow().equals("2")) {
					//with one monthlyValue and one columnTime
					if(ValuesTimeList.contains(ceXmlList.get(i).getId())) {
						drawer_OneValueTime.createTableImage(ceXmlList.get(i), resultMap, annualMaxMinMaps, ceGroups.get(j),
								isFirstPage, isLastPage, innerTitleIndex);
						processAbstract.increasePageNumber();
					}
					//two values: ws, wd
					else if (twoValuesList.contains(ceXmlList.get(i).getId())) {
						drawer_twoValues.createTableImage(ceXmlList.get(i), resultMap, annualSumAvgMaps, 
								annualMaxCountMap, ceGroups.get(j), isFirstPage, isLastPage,
								innerTitleIndex);
						processAbstract.increasePageNumber();
					}
					else if(twoValuesTimeList.contains(ceXmlList.get(i).getId())) {
						//get second CE from resultMap
						Map<String, List<MeanStationValues>> map2 = new HashMap<>();
						int countCe = 0;
						for(String ce:annualMaxMinMaps.keySet()) {
							countCe++;
							if(countCe==1) {
								annualMaxMinMaps1 = annualMaxMinMaps.get(ce);
							}
							else if(countCe==2) {
								List<MeanStationValues> resultList2 = resultMap.get(ce);
								map2 = resultList2.stream()
										.collect(Collectors.groupingBy(MeanStationValues::getStno));
								for(String stn01:annualMaxMinMaps1.keySet()) {
									for(MeanStationValues t:map2.get(stn01)) {
										if(annualMaxMinMaps1.get(stn01).getObsTime().isEqual(
												t.getObsTime())) {
											annualMaxMinMaps.get(ce).get(stn01).setMonthlyValue(t.getMonthlyValue());
										}
									}
								}
							}
						}
						drawer_twoValueTime.createTableImage(ceXmlList.get(i), resultMap, annualMaxMinMaps, ceGroups.get(j), 
								isFirstPage, isLastPage, innerTitleIndex);
						processAbstract.increasePageNumber();
					}
				}
			}
			titleIndex++;
			System.out.println("...draw ce station completed...");
		}
	}
	
	
	public void runClimateElementCsv(String outputCsvFolder) throws IOException {
		prepareConfig_CE();
		ICsvMapWriter mapWriter = null;
		final String[] header = new String[] {"站號/StationNo.", "測站名稱/StationName", "氣象要素/Meteorological Element", 
				"單位/Unit", "年份/Year", "一月/Jan.", "二月/Feb.", "三月/Mar.", "四月/Apr.", "五月/May",
				"六月/Jun.", "七月/Jul.", "八月/Aug.", "九月/Sep.", "十月/Oct.", "十一月/Nov.", "十二月/Dec.", "全年/Annual"};
		try {
			String outputPath = Paths.get(outputCsvFolder, year+"年綜觀及自動觀測站年表.csv").toString();
			FileOutputStream fileStream = new FileOutputStream(new File(outputPath), true);
			OutputStreamWriter fileWriter = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
//			FileWriter fileWriter = new FileWriter(outputPath);
			fileWriter.write('\ufeff');
			mapWriter = new CsvMapWriter(fileWriter,
	                CsvPreference.STANDARD_PREFERENCE);
			mapWriter.writeHeader(header);
			//control climatic element type: including stnPres, tx ...
			//count of climatic element
			for(int i=0; i<ceXmlList.size(); i++) {  
				if(ce_PerPageList.contains(ceXmlList.get(i).getId())) {
					CE_PER_PAGE = Numbers.CLIMATICELEMENT_PER_PAGE.getNumber(); //63
				}
				else {
					CE_PER_PAGE = Numbers.CLIMATICELEMENT2_PER_PAGE.getNumber(); //31
				}
				
				List<String> keyList = new ArrayList<>();
				List<List<String>> ceGroups = new ArrayList<>();
				System.out.println("第幾種氣象要素: "+ (i+1));
				System.out.println("氣象要素類型: " + ceXmlList.get(i).getId());
				System.out.println("是否取代為0: " + ceXmlList.get(i).getSubstituteZero());
				System.out.println("precision: "+ceXmlList.get(i).getPrecision());
				
				List<MeanStationValues> sameColumnId = new ArrayList<>();
				//convert 0 or 0.0 to -
				for(MeanStationValues ce : ceList) {
					if(ceXmlList.get(i).getId().equals(ce.getColumnId())) {
						ce.setSubstituteZero(ceXmlList.get(i).getSubstituteZero());
						sameColumnId.add(ce);
					}
				}
				//convert to special value
				List<MeanStationValues> specialValueList = new ArrayList<>();
				List<String> EquipmentMalfunctionList = Arrays.asList(SpecialValue.EquipmentMalfunction.getNumber());
				List<String> CeUnknownList = Arrays.asList(SpecialValue.CeUnknown.getNumber());
				List<String> TraceList = Arrays.asList(SpecialValue.Trace.getNumber());
				List<String> SubstituteZeroList = Arrays.asList(SpecialValue.SubstituteZero.getNumber());
				for(MeanStationValues value : sameColumnId) {
					MeanStationValues v = value;
					v.setMonthlyValue(
							SpecialValue.conversion4(v.getMonthlyValue(),
									v.isSubstituteZero(), v.getFlag(), ceXmlList.get(i).getPrecision()));
					//if monthly value include the following condition, then change columnTime to null.
					if(EquipmentMalfunctionList.contains(v.getMonthlyValue())
							|| CeUnknownList.contains(v.getMonthlyValue())
							|| TraceList.contains(v.getMonthlyValue())
							|| SubstituteZeroList.contains(v.getMonthlyValue()) ) {
						v.setColumnTime(null);
					}
					specialValueList.add(v);
				}
				 Map<String, List<MeanStationValues>> groupColumnNameMaps =
						 specialValueList.stream()
						 .collect(Collectors.groupingBy(MeanStationValues::getColumnName));
	
				 //one kind of columnName
				 Map<String,Map<String, List<MeanStationValues>>> groupColumnName = new HashMap<>();
				 Map<String,Map<String, List<MeanStationValues>>> groupsColumnName = new HashMap<>();
				 //WS, WD
				 for(String columnName : groupColumnNameMaps.keySet()) {
					 Map<String, List<MeanStationValues>> groupStnoMaps = 
							 getNewORCancelStns(groupColumnNameMaps.get(columnName).stream()
									 .collect(Collectors.groupingBy(MeanStationValues::getStno)));
					 for(String stno:groupStnoMaps.keySet()) {
						 groupStnoMaps.get(stno).sort((p1, p2) -> p1.getObsTime().compareTo(p2.getObsTime()));
					 }
			         Map<String, List<MeanStationValues>> sortedMap = groupStnoMaps.entrySet().stream()
			        		.sorted(Map.Entry.comparingByKey())
			        		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
			                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
					 groupColumnName.put(columnName,  sortedMap);
					 
				 }
				 groupsColumnName = removeAllSpecialValueStns(groupColumnName);
	/////////////////////////////groupColumnName is correct!!!!///////////////////////////////////////
				 
		        //annual Statistic maps:{WS:{Stno:annual}, WD:{Stno:annual}}
		        String statisticType = null;
		        Map<String, String> annualSumAvgMap = new HashMap<>();
		        Map<String, MeanStationValues> annualMaxMinMap = new HashMap<>();
		        Map<String, MeanStationValues> annualMaxMinMaps1 = new HashMap<>();
		        Map<String, String> annualMaxCountMap = new HashMap<>();
		        Map<String,Map<String, String>> annualSumAvgMaps = new HashMap<>();
		        Map<String,Map<String, MeanStationValues>> annualMaxMinMaps = new HashMap<>();
		        Map<String, List<MeanStationValues>> groupStnoMap = new HashMap<>();
		        Map<String, List<MeanStationValues>> sortedGroupStnoMap = new HashMap<>();
		        List<MeanStationValues> groupColumnNameList = new ArrayList<>();
		        int countColnameStns = 0;
		        //column name = WS, WD
		        for(String columnName: groupsColumnName.keySet()) {
		        	groupColumnNameList.clear();
		        	//the same columnName like WS
		        	for(List<MeanStationValues> columnNameList:groupsColumnName.get(columnName).values()) {
		        			groupColumnNameList.addAll(columnNameList);
		        	}
	//	        	System.out.println("groupColumnNameList>> "+groupColumnNameList);
	        		groupStnoMap = groupColumnNameList.stream().collect(Collectors.groupingBy(MeanStationValues::getStno));
	        		sortedGroupStnoMap = groupStnoMap.entrySet().stream()
			        		.sorted(Map.Entry.comparingByKey())
			        		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
			                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	//        		System.out.println("sortedGroupStnoMap>> "+sortedGroupStnoMap);
	        		statisticType = groupColumnNameList.get(0).getStatisticFunction();
	        		
	        		//same columnName with same StatisticFunction so we use get(0)
	        		if(statisticType.equals("SUM") || statisticType.equals("AVG")) {
	        			annualSumAvgMap = statisticalData.annualStatisticSumAvg(
	    	        			sortedGroupStnoMap, statisticType, ceXmlList.get(i).getPrecision());
	        			annualSumAvgMaps.put(columnName, annualSumAvgMap);
	        		}
	        		else if (statisticType.equals("MAX") || statisticType.equals("MIN")) {
	        			System.out.println("statisticType>> "+statisticType);
	        			annualMaxMinMap = statisticalData.annualStatisticMaxMin(
	    	        			sortedGroupStnoMap, statisticType, ceXmlList.get(i).getPrecision());
	        			annualMaxMinMaps.put(columnName, annualMaxMinMap);
	        		}
	        		else if(statisticType.equals("MAXCOUNT")) {
	        			System.out.println("statisticType>> "+statisticType);
	        			annualMaxCountMap = statisticalData.annualStatisticMaxCount(
	    	        			sortedGroupStnoMap, statisticType, ceXmlList.get(i).getPrecision());
	        		}
		        }
		        //get a kind of CE's page counts 
		        for(String colname:groupsColumnName.keySet()) {
		        	keyList.clear();
		        	//collect sorted map's key in list
		        	for(String stno:groupsColumnName.get(colname).keySet()) {
		        		keyList.add(stno);
		        	}
		        	countColnameStns = groupsColumnName.get(colname).values().size();
		        }
				//there are 63 stations at per page for one type of climatic element
				if(countColnameStns <= CE_PER_PAGE){
					ceGroups.add(keyList);
				} else {
					int groupCount = (int)Math.ceil(countColnameStns/(double)CE_PER_PAGE);
					for(int c=0; c<groupCount; c++){
						if (c == groupCount-1){
							ceGroups.add(keyList.subList(c*CE_PER_PAGE, countColnameStns));
						} else {
							ceGroups.add(keyList.subList(c*CE_PER_PAGE, (c+1)*CE_PER_PAGE));
						}
					}
				}
				System.out.println("該氣象要素共有幾頁: "+ ceGroups.size());
				//draw per page 
				//ceGroup only include station no.
				for(int j=0; j<ceGroups.size(); j++) {
					//{stnPres=List<MeanStationValues>}
					Map<String, List<MeanStationValues>> resultMap = new HashMap<>();
					for(String colName:groupsColumnName.keySet()) {
						//important
						List<MeanStationValues> resultList = new ArrayList<>();
						for(String stno: ceGroups.get(j)) {
							if(groupsColumnName.get(colName).get(stno)!=null) {
								resultList.addAll(groupsColumnName.get(colName).get(stno));
							}
						}
						resultMap.put(colName, resultList);
					}
					
					//with one kind of statistic function only include Max and Min
					List<String> ValuesTimeList = 
							Arrays.asList("TxMaxAbs", "TxMinAbs", "RHMin", "Precp1DayMax");
					List<String> twoValuesTimeList = Arrays.asList("MaxWS", "MaxWSGust");
					List<String> twoValuesList = Arrays.asList("WSWD");
					//decide to use which type of table
					if(ceXmlList.get(i).getNumOfTableRow().equals("1")) {
						csvWriter.createCsv(ceXmlList.get(i), resultMap, annualSumAvgMaps, mapWriter, header);
					}
					else if (ceXmlList.get(i).getNumOfTableRow().equals("2")) {
						//with one monthlyValue and one columnTime
						if(ValuesTimeList.contains(ceXmlList.get(i).getId())) {
							csvWriter_OneValueTime.createCsv(ceXmlList.get(i), resultMap, annualMaxMinMaps, mapWriter, header, ceGroups.get(j));
						}
						//two values: ws, wd
						else if (twoValuesList.contains(ceXmlList.get(i).getId())) {
							csvWriter_twoValues.createCsv(ceXmlList.get(i), resultMap, annualSumAvgMaps, annualMaxCountMap,
									mapWriter, header, ceGroups.get(j));
						}
						else if(twoValuesTimeList.contains(ceXmlList.get(i).getId())) {
							//get second CE from resultMap
							Map<String, List<MeanStationValues>> map2 = new HashMap<>();
							int countCe = 0;
							for(String ce:annualMaxMinMaps.keySet()) {
								countCe++;
								if(countCe==1) {
									annualMaxMinMaps1 = annualMaxMinMaps.get(ce);
								}
								else if(countCe==2) {
									List<MeanStationValues> resultList2 = resultMap.get(ce);
									map2 = resultList2.stream()
											.collect(Collectors.groupingBy(MeanStationValues::getStno));
									for(String stn01:annualMaxMinMaps1.keySet()) {
										for(MeanStationValues t:map2.get(stn01)) {
											if(annualMaxMinMaps1.get(stn01).getObsTime().isEqual(
													t.getObsTime())) {
												annualMaxMinMaps.get(ce).get(stn01).setMonthlyValue(t.getMonthlyValue());
											}
										}
									}
								}
							}
							csvWriter_TwoValuesTime.createCsv(ceXmlList.get(i), resultMap, annualMaxMinMaps, 
									mapWriter, header, ceGroups.get(j));
						}
					}
				}
				titleIndex++;
				System.out.println("...draw ce station completed...");
			}
		}
		finally {
            if( mapWriter != null ) {
                    mapWriter.close();
            }
        }
	}
	
	
	
	//get new stations and cancel stations
	public Map<String, List<MeanStationValues>> getNewORCancelStns(
			Map<String, List<MeanStationValues>> sameColumnNameMap){
		
		//add "@" and "~"
        List<Integer> rawDataMonth = new ArrayList<>();
		List<Integer> fullDataMonth = new ArrayList<>();
		List<Integer> reduceData = new ArrayList<>();
		List<Integer> noDataMonth = new ArrayList<>();
	
		for(int j=1; j<=month; j++) {    //month=11
			fullDataMonth.add(j);
		}
		
        for(List<MeanStationValues> group:sameColumnNameMap.values()) {
        	rawDataMonth.clear();
        	int stnEndTime = 0;
    		int stnBeginTime = 0;
    		List<Integer> beginList = new ArrayList<>();
    		List<Integer> endList = new ArrayList<>();
    		List<Integer> tempList = new ArrayList<>();
    		
        	for(int c=0; c<group.size(); c++) {
        		//calculate count of month
        		rawDataMonth.add(group.get(c).getObsTime().getMonthValue());
        	}
        	
    		//cancel station(in the same station)
    		if(group.get(0).getStnEndTime()!=null && !"".equals(group.get(0).getStnEndTime())) {
    			stnEndTime =  Integer.parseInt(
    					group.get(0).getStnEndTime().substring(5,7).replaceAll("/", ""));
    		}
    		//new station(in the same station)
    		if (group.get(0).getStnBeginTime()!=null && !"".equals(group.get(0).getStnBeginTime())) {
    			if (Integer.parseInt(group.get(0).getStnBeginTime().substring(0, 4)) == year) {
    				stnBeginTime =  Integer.parseInt(
    						group.get(0).getStnBeginTime().substring(5,7).replaceAll("/", ""));
    			}
    		}
        	
        	//get different set
    		if(rawDataMonth.size()<fullDataMonth.size()) {
    			reduceData = fullDataMonth.stream()
    					.filter(item -> !rawDataMonth.contains(item)).collect(Collectors.toList());
    			if (stnEndTime != 0) {
    				for(int data :reduceData) {
    					if(data>stnEndTime) {
    						endList.add(data);
    					}
    				}
    			}
    			if (stnBeginTime != 0) {
    				for(int data :reduceData) {
    					if(data<stnBeginTime) {
    						beginList.add(data);
    					}
    				}
    			}
    			tempList.addAll(endList);
    			tempList.addAll(beginList);
    			//null_data_month = (reduceData - beginList - endList)
    			noDataMonth = reduceData.stream()
    					.filter(item -> !tempList.contains(item)).collect(Collectors.toList());
    			
    			//cancel station
    			for(int q=0; q<endList.size(); q++) {
        			MeanStationValues t = new MeanStationValues();
    				t.setStno(group.get(q).getStno());
    				t.setStnCName(group.get(q).getStnCName());
    				t.setObsTime(LocalDateTime.of(year, endList.get(q), 1, 0, 00, 00, 00));
    				t.setStnBeginTime(group.get(q).getStnBeginTime());
    				t.setStnEndTime(group.get(q).getStnEndTime());
    				t.setMonthlyValue("@");
    				t.setStatisticFunction(group.get(q).getStatisticFunction());
    				t.setColumnName(group.get(q).getColumnName());
    				group.add(t);  
        		}
    			//new station
    			for(int q=0; q<beginList.size(); q++) {
        			MeanStationValues t = new MeanStationValues();
    				t.setStno(group.get(q).getStno());
    				t.setStnCName(group.get(q).getStnCName());
    				t.setObsTime(LocalDateTime.of(year, beginList.get(q), 1, 0, 00, 00, 00));
    				t.setStnBeginTime(group.get(q).getStnBeginTime());
    				t.setStnEndTime(group.get(q).getStnEndTime());
    				t.setMonthlyValue("~");
    				t.setStatisticFunction(group.get(q).getStatisticFunction());
    				t.setColumnName(group.get(q).getColumnName());
    				group.add(t);  
        		}
    			//add no data month with "/"
    			//new station
    			for(int q=0; q<noDataMonth.size(); q++) {
        			MeanStationValues t = new MeanStationValues();
    				t.setStno(group.get(q).getStno());
    				t.setStnCName(group.get(q).getStnCName());
    				t.setObsTime(LocalDateTime.of(year, noDataMonth.get(q), 1, 0, 00, 00, 00));
    				t.setStnBeginTime(group.get(q).getStnBeginTime());
    				t.setStnEndTime(group.get(q).getStnEndTime());
    				t.setMonthlyValue("/");
    				t.setStatisticFunction(group.get(q).getStatisticFunction());
    				t.setColumnName(group.get(q).getColumnName());
    				group.add(t);  
        		}
        		//countList sort by obsTime
    			Collections.sort(group, (p1, p2) -> p1.getObsTime().compareTo(p2.getObsTime()));
    		}
        }
		return sameColumnNameMap;
	}
	
    private String getPageFilename(String pageTitle, String stnCName, String stnEName, int page) {
        
        return pageTitle
                    + stnCName + "_"
                    + stnEName.replace("  ", "_")
                    + "#" + page;
    }
    
	public Map<String,Map<String, List<MeanStationValues>>> removeAllSpecialValueStns(
			Map<String,Map<String, List<MeanStationValues>>> groupColumnName){
		
		List<String> EquipmentMalfunctionList = Arrays.asList(SpecialValue.EquipmentMalfunction.getNumber());
		List<String> CeUnknownList = Arrays.asList(SpecialValue.CeUnknown.getNumber());
		List<String> TraceList = Arrays.asList(SpecialValue.Trace.getNumber());
		List<String> SubstituteZeroList = Arrays.asList(SpecialValue.SubstituteZero.getNumber());
		List<String> NewStation = Arrays.asList(SpecialValue.NewStation.getNumber());
		List<String> CancelStation = Arrays.asList(SpecialValue.CancelStation.getNumber());
		Map<String,Map<String, List<MeanStationValues>>> groupColumnNameMaps = new HashMap<>();

		for(String columnName:groupColumnName.keySet()) {
			Map<String, List<MeanStationValues>> stngroupMap = new HashMap<>();
			for(String stno:groupColumnName.get(columnName).keySet()) {
				int invalidCount = 0;
				List<MeanStationValues> stnValueList = new ArrayList<>();
				for(MeanStationValues value:groupColumnName.get(columnName).get(stno)) {
					if(EquipmentMalfunctionList.contains(value.getMonthlyValue())
							|| CeUnknownList.contains(value.getMonthlyValue())
							|| TraceList.contains(value.getMonthlyValue())
							|| SubstituteZeroList.contains(value.getMonthlyValue())
							|| NewStation.contains(value.getMonthlyValue())
							|| CancelStation.contains(value.getMonthlyValue())) {
						invalidCount++;
					}
					stnValueList.add(value);
				}
				if(invalidCount < month) {
					stngroupMap.put(stno, stnValueList);
				}
			}
			Map<String, List<MeanStationValues>> stnSortedMap = stngroupMap.entrySet().stream()
	        		.sorted(Map.Entry.comparingByKey())
	        		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			groupColumnNameMaps.put(columnName, stnSortedMap);
		}
		return groupColumnNameMaps;
	}
}
