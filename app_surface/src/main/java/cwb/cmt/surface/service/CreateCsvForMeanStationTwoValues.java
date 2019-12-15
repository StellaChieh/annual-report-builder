package cwb.cmt.surface.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ICsvMapWriter;

import cwb.cmt.surface.model.ClimaticElement;
import cwb.cmt.surface.model.MeanStationValues;
import cwb.cmt.surface.utils.SpecialValue;

@Service("createCsvForMeanStationTwoValues")
public class CreateCsvForMeanStationTwoValues {

    //Time: year
    @Resource(name="year")
    protected int year;
	
    //Time: month
    @Resource(name="month")
    protected int month;
    
    
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
	
	public void createCsv(Object...objects) throws IOException {
		ClimaticElement ceXmlList = (ClimaticElement) objects[0];
		@SuppressWarnings("unchecked")
		Map<String,List<MeanStationValues>> ceMap = 
		(Map<String, List<MeanStationValues>>) objects[1];
		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> annualMap = (Map<String, Map<String, String>>) objects[2];
		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> annualMap2 = (Map<String, Map<String, String>>) objects[3];
		ICsvMapWriter mapWriter = (ICsvMapWriter) objects[4];
		String[] header = (String[]) objects[5];
		@SuppressWarnings("unchecked")
		List<String> stnList = (List<String>) objects[6];
		
		final CellProcessor[] processors = getProcessors();
		Map<String, Object> MeanStationValuesMap = new HashMap<String, Object>();
		//create table for WSWD
		//include special value like X, /, @, -
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.CeUnknown.getNumber(),
				SpecialValue.NewStation.getNumber(), 
				SpecialValue.CancelStation.getNumber(),
				SpecialValue.Trace.getNumber(),
				SpecialValue.SubstituteZero.getNumber()
				);
		List<MeanStationValues> ceList = new ArrayList<>();
        Map<String, List<MeanStationValues>> groupCeMap = new HashMap<>();
        Map<String, List<MeanStationValues>> sortedCeMap = new HashMap<>();
        Map<String, List<MeanStationValues>> groupCeMap2 = new HashMap<>();
        Map<String, List<MeanStationValues>> sortedCeMap2 = new HashMap<>();
        Map<String, String> annual1= new HashMap<>();
        Map<String, String> annual2= new HashMap<>();
    	int countKey = 0;
    	for(String k:ceMap.keySet()) {
    		countKey++;
    		ceList = ceMap.get(k);
    		if(countKey==1) {
    			//group by stno from ceList
        		groupCeMap = ceList.stream()
        	    		.collect(Collectors.groupingBy(MeanStationValues::getStno));
        		sortedCeMap = groupCeMap.entrySet().stream()
                		.sorted(Map.Entry.comparingByKey())
                		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    		}
    		else if (countKey==2){
    			//group by stno from ceList
        		groupCeMap2 = ceList.stream()
        	    		.collect(Collectors.groupingBy(MeanStationValues::getStno));
        		sortedCeMap2 = groupCeMap2.entrySet().stream()
                		.sorted(Map.Entry.comparingByKey())
                		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    		}
    	}
    	int countKey2 = 0;
    	for(String k:annualMap.keySet()) {
    		countKey2++;
    		if(countKey2==1) {
    			annual1 = annualMap.get(k);
    		}
    	}
    	String data1 = null;
    	String data2 = null;
    	String primaryColumn = null;
    	Map<String, List<String>> data1Map = new HashMap<>();
    	Map<String, List<String>> data2Map = new HashMap<>();
    	Map<String, List<String>> data1Maps = new HashMap<>();
    	Map<String, List<String>> data2Maps = new HashMap<>();
    	for(List<MeanStationValues> value:sortedCeMap.values()) {
    		List<String> dataList = new ArrayList<>();
    		//month=11
    		for(int count=0; count<month; count++){
        		data1 = value.get(count).getMonthlyValue();
        		primaryColumn = value.get(0).getStno() + "  " + value.get(0).getStnCName();
        		dataList.add(data1);
    		}
    		data1Map.put(primaryColumn, dataList);
    	}
    	data1Maps = data1Map.entrySet().stream()
    				.sorted(Map.Entry.comparingByKey())
    				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(oldValue, newValue) -> oldValue, LinkedHashMap::new));
    	for(List<MeanStationValues> value:sortedCeMap2.values()) { //size=32
    		List<String> dataList = new ArrayList<>();
    		for(int count=0; count<month; count++){   //month=11
        		data2 = value.get(count).getMonthlyValue();
        		primaryColumn = value.get(0).getStno() + "  " + value.get(0).getStnCName();
        		dataList.add(data2);
    		}
    		data2Map.put(primaryColumn, dataList);
    	}
    	data2Maps = data2Map.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(oldValue, newValue) -> oldValue, LinkedHashMap::new));
    	
    	
    	int row=0;
    	int countStn = 0;
    	for(String stno :data1Maps.keySet()) { 
    		if(row<stnList.size()) { 
    			String[] splitStno = stno.split("\\s+");
    		    MeanStationValuesMap.put(header[0], splitStno[0]);
                MeanStationValuesMap.put(header[1], splitStno[1]);
                MeanStationValuesMap.put(header[2], ceXmlList.getChineseTitle());
                MeanStationValuesMap.put(header[3], (ceXmlList.getEnglishUnit() != null)? ceXmlList.getEnglishUnit() : "");
                MeanStationValuesMap.put(header[4], year);
    			for(int col=0; col<month; col++) { //config.month = 12
                    if(specialValueList.contains(data1Maps.get(stno).get(col))){
                    	if(data1Maps.get(stno).get(col).equals(data2Maps.get(stno).get(col))){
                    		MeanStationValuesMap.put(header[5+col], "   " + data1Maps.get(stno).get(col)+
                            		"\\"+"  ");
                    	}
                    	
                    }else {
                    	MeanStationValuesMap.put(header[5+col], "   " + data1Maps.get(stno).get(col)+
                    			"\\"+data2Maps.get(stno).get(col));
                    }
                }
        	}
    		if(countStn<stnList.size()) {
    			MeanStationValuesMap.put(header[17], "  " +annual1.get(stnList.get(countStn))+
    					"\\"+annualMap2.get(stnList.get(countStn)));
    		}
    		countStn++;
    		row++;
    		// write the customer maps
		    mapWriter.write(MeanStationValuesMap, header, processors);
    	}
	}
}
