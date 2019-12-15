package cwb.cmt.surface.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.CliSum;
import cwb.cmt.surface.model.MeanStationValues;
import cwb.cmt.surface.model.Station;

import cwb.cmt.surface.utils.SpecialValue;

@Service("statisticalData")
public class StatisticalData {
    //Time: year
    @Resource(name="year")
    protected int year;
    
    //Time: month
    @Resource(name="month")
    protected int month;
	
	List<List<String>> sumGroups = new ArrayList<>(); 
	List<String> sumList = new ArrayList<>();
	private static final GregorianCalendar calendar = new GregorianCalendar();
	int count=0;
	 
	 
	//1.4.1
	public List<List<String>> StatisticalRadSun(List<AuxCliSum> resultList, String listPattern) { 
		final int rowCount = resultList.size();
		final int[] daysOfMonth = new int[] {
	        	31, (!calendar.isLeapYear(year))? 28:29, 31, 30, 31, 30,
	        	31, 31, 30, 31, 30, 31};
		List<String> EquipmentMalfunctionSymbol = Arrays.asList(SpecialValue.EquipmentMalfunction.getNumber());
		List<String> UnknownSymbol = Arrays.asList(SpecialValue.Unknown.getNumber());
		int[] validRowCount = new int[12];
		int[] abnormalCount = new int[100];
		float[] sum  = new float[12];
		List<String> sumList = new ArrayList<>();
		List<String> meanList = new ArrayList<>();
		String data = null;
		float dataSum = 0;
		float dataMean = 0;
		int countOfRowcount=0;
		int monthOfYear;
		int rowCountOfMonth;
		
		List<List<String>> statisticGroups = new ArrayList<>();
		for(int col=0; col<month; col++) {
			rowCountOfMonth=0;
			for (int row=0; row<31; row++) {
				if (countOfRowcount<rowCount) {
					monthOfYear = resultList.get(countOfRowcount).getObsTime().getMonthValue();
					data = ((listPattern=="rad")?resultList.get(countOfRowcount).getGlobalRad():
						resultList.get(countOfRowcount).getSunshine());
					if(monthOfYear==col+1) {
						if (EquipmentMalfunctionSymbol.contains(data)) {
							abnormalCount[col]++;
						}
						else if (UnknownSymbol.contains(data)) {
							abnormalCount[col]++;
						}
						else if (!EquipmentMalfunctionSymbol.contains(data) 
								&& !UnknownSymbol.contains(data)) {
							//empty value
							if (Float.isNaN(Float.parseFloat(data))){
								abnormalCount[col]++;
							}
							//abnormal value
							else if (Float.compare(Float.parseFloat(data), 0.0f) < 0) {
								abnormalCount[col]++;
							}
							//normal value
							else{
								validRowCount[col]++;
								rowCountOfMonth++;
								//sum value of per month
								sum[col]+=Float.parseFloat(data);
								if(monthOfYear==6) {
									
								}
								dataSum = new BigDecimal(sum[col]).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
								float mean = (sum[col] / Math.min(rowCountOfMonth, daysOfMonth[col]));
			                    dataMean = new BigDecimal(mean).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
							}
						}
						countOfRowcount+=1;
					}
				}
			}
			sumList.add((validRowCount[col] > 0)
					? String.valueOf(String.format("%6s", ((validRowCount[col] < daysOfMonth[col])? "*":"")
					+String.valueOf(dataSum))): "");
			meanList.add((validRowCount[col] > 0)
                	? String.valueOf(String.format("%6s", ((validRowCount[col] < daysOfMonth[col])? "*":"") +
                      	  String.valueOf(dataMean)))
                      	: "");
		}
		statisticGroups.add(sumList);
		statisticGroups.add(meanList);
		return statisticGroups;
	}
	
	//1.4.2
		public Map<String, String> StatisticalGlobalRad(Map<String, List<AuxCliSum>> countingGroup) {
			String data = null;
			float data_float;
			String dataflag = null;
			List<String> EquipmentMalfunctionList = Arrays.asList(SpecialValue.EquipmentMalfunction.getValue());
			List<String> UnknownList = Arrays.asList(SpecialValue.Unknown.getValue());
			List<String> EquipmentMalfunctionSymbol = Arrays.asList(SpecialValue.EquipmentMalfunction.getNumber());
			List<String> UnknownSymbol = Arrays.asList(SpecialValue.Unknown.getNumber());
			List<String> CancelStationSymbol = Arrays.asList(SpecialValue.CancelStation.getNumber());
			List<String> sumList = new ArrayList<>();
			Map<String, String> sumMap = new HashMap<String,String>();
			
			for(List<AuxCliSum> group:countingGroup.values()) {
				int abnormalCount = 0;
				int flagCount = 0;
				int validRowCount = 0;
				float sum = 0;
				float dataSum = 0;
				String stno = null;
				
				for(int i=0; i<group.size(); i++) {
					stno = group.get(0).getStno();
					if(CancelStationSymbol.contains(group.get(i).getGlobalRad().replaceAll("[*]", ""))||
							EquipmentMalfunctionSymbol.contains(group.get(i).getGlobalRad().replaceAll("[*]", ""))||
							UnknownSymbol.contains(group.get(i).getGlobalRad().replaceAll("[*]", ""))) {
						data = group.get(i).getGlobalRad().replaceAll("[*]", "");
					}
					else {
						data_float = Float.parseFloat(group.get(i).getGlobalRad().replaceAll("[*]", ""));
						data = String.valueOf(new BigDecimal(data_float).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
					}
					
					dataflag = group.get(i).getGloblRad_flag();
					
					if(dataflag.equals("1")) {
						flagCount++;
					}
					
					if(CancelStationSymbol.contains(data)||
							EquipmentMalfunctionSymbol.contains(data)||
							UnknownSymbol.contains(data)) {
						abnormalCount++;
					}
					else if(!CancelStationSymbol.contains(data)&&
							!EquipmentMalfunctionSymbol.contains(data)&&
							!UnknownSymbol.contains(data)) {
						if (Float.isNaN(Float.parseFloat(data))) {
							 abnormalCount++;
						}
						else if (Float.compare(Float.parseFloat(data), 0F) < 0) {
							abnormalCount++;
				            // Handle special values: 
					           if (EquipmentMalfunctionList.contains(data)) {
					        	   abnormalCount++;
					           }
					           else if (UnknownList.contains(data)) {
					        	   abnormalCount++;
					           }
					            // default negative values: 
					           else {
					        	   abnormalCount++;
					           }
						 }
					   else {
				        	// increase counter
				        	validRowCount++;
			        		//sum value of per month
				        	sum+=Float.parseFloat(data);
			        		dataSum = new BigDecimal(sum).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
				        }
					}
				}
				sumMap.put(stno, (validRowCount > 0)
						? String.valueOf(String.format("%6s", (((validRowCount < month)||(flagCount>0))? "*":"")
						+String.valueOf(dataSum))): "");
				
			}
			return sumMap; 
		}
	
	
	
	//1.4.3
	public Map<String, List<String>> StatisticalRadHrMax(
			Map<String, List<AuxCliSum>> countingGroup, List<Station> stnList) {
		
		String data;
		int dayOfMonth = 0;
		Month monthOfYear;
		String annualTime="";
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.Unknown.getNumber(),
				SpecialValue.CancelStation.getNumber());
		ArrayList<Float> dataMergeList = new ArrayList<Float>();
		Map<String, Float> dataMergeMap = new HashMap<>();
		ArrayList<LocalDateTime> timesorted = new ArrayList<LocalDateTime>();
    	List<AuxCliSum> result = new ArrayList<>();
    	Map<String, List<String>> annualMap = new HashMap<>();
    	
    	for(int row=0; row<stnList.size(); row++) {
    		dataMergeList.clear();
        	dataMergeMap.clear();
        	result.clear();
        	int validRowCount = 0;
			for(String key: countingGroup.keySet()) {
				if (stnList.get(row).getStno().equals(key)) {//size=31
					for (int c = 0; c < month; c++) {
						AuxCliSum temp = countingGroup.get(key).get(c);
						result.add(temp);
					}
				}
			}
			
			for(int count =0; count<result.size(); count++) {
				data = result.get(count).getGlobalRadMax();
				if (SpecialValue.CancelStation.getNumber().equals(data)) {
					validRowCount++;
				}
				if (!specialValueList.contains(data)) {
					validRowCount++;
					dataMergeList.add(Float.parseFloat(data));
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					String localTime = df.format(result.get(count).getGlobalRadHrMaxTime());
					dataMergeMap.put(localTime, Float.parseFloat(data));
				}
			}
			//annual max value
			float maxData = dataMergeList.stream().max(Comparator.comparing(n -> n)).get();
		    List<String> collectKey = dataMergeMap.entrySet().stream()
		      		.filter(map -> map.getValue()==maxData)
		      		.map(entry -> entry.getKey() )
		      		.collect(Collectors.toList());
		    
		    List<String> collectValue = dataMergeMap.entrySet().stream()
					.filter(map -> map.getValue()==maxData)
					.map(entry -> "" +entry.getValue())
					.collect(Collectors.toList());
		    
		    //number of max data more than one
		    if (collectKey.size() > 1) { 
		      	timesorted.clear();
		      	for(String s : collectKey) {
		      		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		      		LocalDateTime ldt = LocalDateTime.parse(s,df);
		      		timesorted.add(ldt);
		      	}
		      	LocalDateTime minTime = timesorted.stream().min(Comparator.comparing(n -> n)).get();
		      	dayOfMonth = minTime.getDayOfMonth();
		      	monthOfYear = minTime.getMonth();
		      	String m =monthOfYear.getDisplayName(TextStyle.SHORT,Locale.UK);
		      	annualTime = m + "." + dayOfMonth;
		    }
		    else {
		      	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		  		LocalDateTime ldt = LocalDateTime.parse(collectKey.get(0),df);
		  		dayOfMonth = ldt.getDayOfMonth();
		  		monthOfYear = ldt.getMonth();
		  		String m =monthOfYear.getDisplayName(TextStyle.SHORT,Locale.UK);
		  		annualTime = m + "." + dayOfMonth;
		    }
		    List<String> annualValue = new ArrayList<>();
		    annualValue.add(((validRowCount<result.size())?"*":" ")+collectValue.get(0));
		    annualValue.add(annualTime);
		    String stno = stnList.get(row).getStno();
		    annualMap.put(stno, annualValue);
		    
    	}
    	
		return annualMap;
	}
	
	
	//2.3-2.20
	//sortedMap may have two kinds of columnName, wanna get two kinds of columnName's value
	public Map<String, String> annualStatisticSumAvg(
			Map<String, List<MeanStationValues>> sortedMap, String statisticFunction, String precision){
		//include special value like X, /, @, ~
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.CeUnknown.getNumber(),
				SpecialValue.NewStation.getNumber(), 
				SpecialValue.CancelStation.getNumber());
		//include special Symbol like -, T
		List<String> specialSymbolList = Arrays.asList(
				SpecialValue.Trace.getNumber(),
				SpecialValue.SubstituteZero.getNumber());
		
		Map<String, String> annualStatistic = new HashMap<>();
		if (statisticFunction.equals("SUM")) {
			for(List<MeanStationValues> values : sortedMap.values()) {
				float sum = 0; 
				int flagCount = 0;
				int validRowCount = 0;
				int abnormalCount = 0;	
				int symbolCount = 0;
				String stno = null;
				
				//get one list from sorted Map
				for(MeanStationValues value: values) {
					if (specialValueList.contains(value.getMonthlyValue())) {
						abnormalCount++;
					}
					else if (specialSymbolList.contains(value.getMonthlyValue())) {
						symbolCount++;
					}
					//normal data with * and without *
					else {
						validRowCount++;
						if(value.getFlag()!=null && value.getFlag().equals("1")) {
							flagCount++;
						}
						sum += Float.parseFloat(value.getMonthlyValue().replaceAll("[*]", ""));
					}
					stno = value.getStno();
				}// for value
				if(precision.equals("0")) {
					int dataSum = new BigDecimal(sum).setScale(Integer.valueOf(precision), BigDecimal.ROUND_HALF_UP).intValue();
					annualStatistic.put(stno, (validRowCount > 0)
							? String.valueOf((abnormalCount>0||flagCount>0)?"*":"") + String.valueOf(dataSum)
							:((validRowCount>0)?String.valueOf(dataSum):""));
				}
				else {
					Float dataSum = new BigDecimal(sum).setScale(Integer.valueOf(precision), BigDecimal.ROUND_HALF_UP).floatValue();
					annualStatistic.put(stno, (validRowCount > 0)
							? String.valueOf((abnormalCount>0||flagCount>0)?"*":"") + String.valueOf(dataSum)
							:((validRowCount>0)?String.valueOf(dataSum):""));
					
				}
			}
		}
		//AVG
		else if (statisticFunction.equals("AVG")) {
			for(List<MeanStationValues> values : sortedMap.values()) {
				double avg = 0; 
				float sum = 0;
				int flagCount = 0;
				int validRowCount = 0;
				int abnormalCount = 0;	
				int symbolCount = 0;
				String stno = null;
				
				//get one list from sorted Map
				for(MeanStationValues value: values) {
					if (specialValueList.contains(value.getMonthlyValue())) {
						abnormalCount++;
					}
					else if (specialSymbolList.contains(value.getMonthlyValue())) {
						symbolCount++;
					}
					//normal data with * and without *
					else {
						validRowCount++;
						if(value.getFlag()!=null && value.getFlag().equals("1")) {
							flagCount++;
						}
						sum += Float.parseFloat(value.getMonthlyValue().replaceAll("[*]", ""));
					}
					stno = value.getStno();
				}
				avg = (validRowCount>0)?(sum/validRowCount):0;
				//annual value is integer
				if(precision.equals("0")) {
					int dataAvg = new BigDecimal(avg).setScale(Integer.valueOf(precision), BigDecimal.ROUND_HALF_UP).intValue();
					annualStatistic.put(stno, (validRowCount > 0)
							? String.valueOf((abnormalCount>0||flagCount>0)?"*":"") + String.valueOf(dataAvg)
							:((validRowCount>0)?String.valueOf(dataAvg):""));
				}
				else {
					double dataAvg = new BigDecimal(avg).setScale(Integer.valueOf(precision), BigDecimal.ROUND_HALF_UP).doubleValue();
					annualStatistic.put(stno, (validRowCount > 0)
							? String.valueOf((abnormalCount>0||flagCount>0)?"*":"") + String.valueOf(dataAvg)
							:((validRowCount>0)?String.valueOf(dataAvg):""));
				}
			}
		}
        Map<String, String> sortedAnnualMap = annualStatistic.entrySet().stream()
        		.sorted(Map.Entry.comparingByKey())
        		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		return sortedAnnualMap;
	}
		
	
	
	public Map<String, MeanStationValues> annualStatisticMaxMin(
				Map<String, List<MeanStationValues>> sortedMap, String statisticFunction, String precision){
		
			//include special value like X, /, @, ~
			List<String> specialValueList = Arrays.asList(
					SpecialValue.EquipmentMalfunction.getNumber(), 
					SpecialValue.CeUnknown.getNumber(),
					SpecialValue.NewStation.getNumber(), 
					SpecialValue.CancelStation.getNumber());
			//include special Symbol like -, T
			List<String> specialSymbolList = Arrays.asList(
					SpecialValue.Trace.getNumber(),
					SpecialValue.SubstituteZero.getNumber());
		
			Map<String, MeanStationValues> maxminGroupBYStno =  new HashMap<>();
			Map<String, MeanStationValues> mergeAnnualMap = new HashMap<>();
			for(String stn:sortedMap.keySet()) {
				int abnormalCount = 0;
				int symbolCount = 0;
				int validRowCount = 0;
				int flagCount = 0;
				String stno = null;
				List<MeanStationValues> stnList = sortedMap.get(stn);
				List<MeanStationValues> avoidSpecialValueList = new ArrayList<>();
				Map<String, Integer> flagCountMap = new HashMap<>();
				Map<String, Integer> abnormalSymbolCountMap = new HashMap<>();
				for(MeanStationValues value : stnList) {
					MeanStationValues tmpValue = new MeanStationValues();
					tmpValue.setStno(value.getStno());
					tmpValue.setStnCName(value.getStnCName());
					tmpValue.setObsTime(value.getObsTime());
					tmpValue.setStnBeginTime(value.getStnBeginTime());
					tmpValue.setStnEndTime(value.getStnEndTime());
					tmpValue.setColumnId(value.getColumnId());
					tmpValue.setColumnName(value.getColumnName());
					tmpValue.setMonthlyValue(value.getMonthlyValue());
					tmpValue.setFlag(value.getFlag());
					tmpValue.setSubstituteZero(value.isSubstituteZero());
					tmpValue.setColumnTime(value.getColumnTime());
					tmpValue.setStatisticFunction(value.getStatisticFunction());
					stno = tmpValue.getStno();
					if (specialValueList.contains(tmpValue.getMonthlyValue())) {
						abnormalCount++;
					}
					else if (specialSymbolList.contains(tmpValue.getMonthlyValue())) {
						symbolCount++;
					}
					else {
						validRowCount++;
						if(tmpValue.getFlag()!=null && tmpValue.getFlag().equals("1")) {
							flagCount++;
						}
						tmpValue.setMonthlyValue(tmpValue.getMonthlyValue().replaceAll("[*]", ""));
						avoidSpecialValueList.add(tmpValue);
					}
				}
				flagCountMap.put(stno, flagCount);
//				abnormalSymbolCountMap.put(stno, (abnormalCount+symbolCount));
				abnormalSymbolCountMap.put(stno, (abnormalCount));
			    if (statisticFunction.equals("MAX")) {
				    maxminGroupBYStno = avoidSpecialValueList.stream().collect(
				    	        Collectors.groupingBy(MeanStationValues::getStno,
				    	            Collectors.collectingAndThen(
				    	                Collectors.reducing((MeanStationValues d1, MeanStationValues d2) 
				    	                		-> Float.parseFloat(d1.getMonthlyValue()) > Float.parseFloat(d2.getMonthlyValue()) ? d1 : d2),
				    	                    Optional::get)));
				}
			    else if(statisticFunction.equals("MIN")){
				    maxminGroupBYStno = avoidSpecialValueList.stream().collect(
			    	        Collectors.groupingBy(MeanStationValues::getStno,
			    	            Collectors.collectingAndThen(
			    	                Collectors.reducing((MeanStationValues d1, MeanStationValues d2) 
			    	                		-> Float.parseFloat(d1.getMonthlyValue()) < Float.parseFloat(d2.getMonthlyValue()) ? d1 : d2),
			    	                    Optional::get)));
			    }
			    for(String stnoKey:maxminGroupBYStno.keySet()) {
			    	//if some month have @, annual value add *
			    	if(flagCountMap.get(stnoKey)>0 ||abnormalSymbolCountMap.get(stnoKey)>0) {
			    		MeanStationValues tmpMaxMinAnnualMap = new MeanStationValues();
			    				maxminGroupBYStno.get(stnoKey);
			    		tmpMaxMinAnnualMap.setStno(stnoKey);
			    		tmpMaxMinAnnualMap.setStnCName(maxminGroupBYStno.get(stnoKey).getStnCName());
			    		tmpMaxMinAnnualMap.setObsTime(maxminGroupBYStno.get(stnoKey).getObsTime());
			    		tmpMaxMinAnnualMap.setStnBeginTime(maxminGroupBYStno.get(stnoKey).getStnBeginTime());
			    		tmpMaxMinAnnualMap.setStnEndTime(maxminGroupBYStno.get(stnoKey).getStnEndTime());
			    		tmpMaxMinAnnualMap.setColumnId(maxminGroupBYStno.get(stnoKey).getColumnId());
			    		tmpMaxMinAnnualMap.setColumnName(maxminGroupBYStno.get(stnoKey).getColumnName());
			    		tmpMaxMinAnnualMap.setFlag(maxminGroupBYStno.get(stnoKey).getFlag());
			    		tmpMaxMinAnnualMap.setStatisticFunction(maxminGroupBYStno.get(stnoKey).getStatisticFunction());
			    		tmpMaxMinAnnualMap.setColumnTime(maxminGroupBYStno.get(stnoKey).getColumnTime());
			    		tmpMaxMinAnnualMap.setMonthlyValue(
			    				"*" + maxminGroupBYStno.get(stnoKey).getMonthlyValue());
			    		maxminGroupBYStno.put(stnoKey,tmpMaxMinAnnualMap);
			    	}
			    }
			    //all month values are not only abnormalCount but symbolCount.The both count's sum equal to month 
			    for(MeanStationValues stnoKey:stnList) { //stnList.size =11
			    	MeanStationValues tmpStnoKey = new MeanStationValues();
				    if(abnormalSymbolCountMap.get(stnoKey.getStno())==month) {
			    		tmpStnoKey.setMonthlyValue("");
			    		maxminGroupBYStno.put(stnoKey.getStno(), tmpStnoKey);
				    }
				    break;
			    }
			    mergeAnnualMap.putAll(maxminGroupBYStno);
			}
			return mergeAnnualMap;
	}
	
	
	
	@SuppressWarnings("unused")
	public Map<String, String> annualStatisticMaxCount(
			Map<String, List<MeanStationValues>> sortedMap, String statisticFunction, String precision){
	
		//include special value like X, /, @, ~
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.CeUnknown.getNumber(),
				SpecialValue.NewStation.getNumber(), 
				SpecialValue.CancelStation.getNumber());
		//include special Symbol like -, T
		List<String> specialSymbolList = Arrays.asList(
				SpecialValue.Trace.getNumber(),
				SpecialValue.SubstituteZero.getNumber());
		
		Map<String, String> maxminGroupBYStno =  new HashMap<>();
		Map<String, MeanStationValues> mergeAnnualMap = new HashMap<>();
		Map<String, Map<String, Long>> maxCountGroupBYStno = new HashMap<>();
		for(String stn:sortedMap.keySet()) {
			int abnormalCount = 0;
			int symbolCount = 0;
			int substituteZeroCount = 0;
			int validRowCount = 0;
			int flagCount = 0;
			String stno = null;
			List<MeanStationValues> stnList = sortedMap.get(stn);
			List<MeanStationValues> avoidSpecialValueList = new ArrayList<>();
			Map<String, Integer> flagCountMap = new HashMap<>();
			Map<String, Integer> abnormalSymbolCountMap = new HashMap<>();
			
			for(MeanStationValues value : stnList) {
				MeanStationValues tmpValue = value;
				stno = tmpValue.getStno();
				if (specialValueList.contains(tmpValue.getMonthlyValue())) {
					abnormalCount++;
				}
				else if (specialSymbolList.contains(tmpValue.getMonthlyValue())) {
					symbolCount++;
				}
				else {
					validRowCount++;
					if(tmpValue.getFlag()!=null && tmpValue.getFlag().equals("1")) {
						flagCount++;
					}
					avoidSpecialValueList.add(tmpValue);
				}
			}
			flagCountMap.put(stno, flagCount);
			abnormalSymbolCountMap.put(stno, (abnormalCount+symbolCount));
			if(avoidSpecialValueList.isEmpty()) {
				maxminGroupBYStno.put(stn, "");
			}
			else {
				List<Float> tmpSortedList = new ArrayList<>();
				maxCountGroupBYStno = avoidSpecialValueList.stream().collect(
						Collectors.groupingBy(MeanStationValues::getStno,
								Collectors.groupingBy(MeanStationValues::getMonthlyValue, Collectors.counting())
								));
				//get max count
				//monthlyValue, Count
				for(Map<String, Long> countValue:maxCountGroupBYStno.values()) {
					
					Map<Float, Integer> tmpCount = new HashMap<>();
					for(String data:countValue.keySet()) {
						int count = countValue.get(data).intValue();
						data = data.replaceAll("[*]", "");
						Float dataValue = Float.parseFloat(data);
						tmpCount.put(dataValue, count);
					}
					Entry<Float, Integer> maxCount = tmpCount.entrySet().stream()
							.sorted(Map.Entry.<Float, Integer>comparingByKey().reversed())
							.max(Map.Entry.comparingByValue()).get();
					maxminGroupBYStno.put(stn, String.valueOf(maxCount.getKey()));					
				}
			}
		}
		return maxminGroupBYStno;
}
	
	
	// 1.3 CliSum annual statistical
	public Float Sum(String data) {
		//include special value like X, /, @, ~, -, T
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.CeUnknown.getNumber(),
				SpecialValue.NewStation.getNumber(), 
				SpecialValue.CancelStation.getNumber(),
				SpecialValue.Trace.getNumber(),
				SpecialValue.SubstituteZero.getNumber()
				);
		
		 Float sumValue = 0.0f;
		 int emptyValue = 0;
		 
		
		 if(data != null && !"".equals(data.trim())) {
	    	 if(!specialValueList.contains(data)){
    			 sumValue+= Float.parseFloat(data);
    		 }
    	 }
		 else {
			 emptyValue++;
		 }
		 
		 if(emptyValue>0) {
			 sumValue=-1.0f;
		 }
			return sumValue;
		}
	
	public Float CountDaySum(String data) {
		//include special value like X, /, @, ~, -, T
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.CeUnknown.getNumber(),
				SpecialValue.NewStation.getNumber(), 
				SpecialValue.CancelStation.getNumber(),
				SpecialValue.Trace.getNumber(),
				SpecialValue.SubstituteZero.getNumber()
				);
		 Float sumValue = 0.0f;
		 int emptyValue = 0;
		 
		 if(data == null) {
			 emptyValue++;
		 }
		 else if(data!=null) {
	    	 if(!specialValueList.contains(data)){
	    		 if(data!="") {
	    			 sumValue+= Float.parseFloat(data);
	    		 }
    			 
    		 }
    	 }
		 
		 if(emptyValue>0) {
			 sumValue=-1.0f;
		 }
			return sumValue;
		}
	
	public Float Max(List<String> dataList) {
		String s = dataList.stream()
				.max((e1, e2) -> Float.compare(Float.parseFloat(e1), Float.parseFloat(e2)))
				.get();
		return Float.parseFloat(s);
		
	}

	public Float Min(List<String> dataList) {
		String s = dataList.stream()
				.min((e1, e2) -> Float.compare(Float.parseFloat(e1), Float.parseFloat(e2)))
				.get();
		return Float.parseFloat(s);
		
	}
	
	public String MaxTime(Map<Float, LocalDateTime> dataMap) {
		Map.Entry<Float, LocalDateTime> maxElt = dataMap.entrySet().stream()
                									.max(Map.Entry.comparingByKey()).get();
		String maxtime = String.valueOf(maxElt.getValue().getMonthValue())+
				" "+String.valueOf(maxElt.getValue().getDayOfMonth());
		return maxtime;
		
	}
	//MaxWd
	public String MaxWd(Map<Float, Float> dataMap) {
		Map.Entry<Float, Float> maxElt = dataMap.entrySet().stream()
                									.max(Map.Entry.comparingByKey()).get();
		String maxWd =  String.valueOf(maxElt.getValue());
		return maxWd;
	}
	
	public String MinTime(Map<Float, LocalDateTime> dataMap) {
		Map.Entry<Float, LocalDateTime> minElt = dataMap.entrySet().stream()
                									.min(Map.Entry.comparingByKey())
                										.get();
		String mintime = String.valueOf(minElt.getValue().getMonthValue())+
				" "+String.valueOf(minElt.getValue().getDayOfMonth());
		return mintime;
	}
	
	public String MaxCount(List<Float> dataList) {
	   Map<Float, Long> resultCount =
	            dataList.stream()
	            .collect(Collectors.groupingBy(
	                            Function.identity(), Collectors.counting()));
	   Map<Float, Integer> tmpResult = new HashMap<>();
	   for(Float data: resultCount.keySet()) {
		  int value = resultCount.get(data).intValue();
		  tmpResult.put(data, value);
	   }
	   Entry<Float, Integer> maxEntry = tmpResult.entrySet().stream()
			   .max(Map.Entry.comparingByValue()).get();
	   String maxCount = String.valueOf(maxEntry.getKey());
	   return maxCount;
	}
	
}
