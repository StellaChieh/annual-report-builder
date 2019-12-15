package cwb.cmt.surface.service;

import java.io.IOException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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

@Service("createCsvForMeanStationTwoValueTime")
public class CreateCsvForMeanStationTwoValueTime {

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
		Map<String, Map<String, MeanStationValues>> annualMap = (Map<String, Map<String, MeanStationValues>>) objects[2];
		ICsvMapWriter mapWriter = (ICsvMapWriter) objects[3];
		String[] header = (String[]) objects[4];
		@SuppressWarnings("unchecked")
		List<String> stnList = (List<String>) objects[5];
		Map<String, Object> MeanStationValuesMap = new HashMap<String, Object>();
		final CellProcessor[] processors = getProcessors();
		
    	List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.CeUnknown.getNumber(),
				SpecialValue.NewStation.getNumber(), 
				SpecialValue.CancelStation.getNumber(),
				SpecialValue.Trace.getNumber(),
				SpecialValue.SubstituteZero.getNumber()
				);
    	List<MeanStationValues> ceList = new ArrayList<>();
    	List<MeanStationValues> ceList2 = new ArrayList<>();
        Map<String, List<MeanStationValues>> groupCeMap = new HashMap<>();
        Map<String, List<MeanStationValues>> sortedCeMap = new HashMap<>();
        Map<String, List<MeanStationValues>> groupCeMap2 = new HashMap<>();
        Map<String, List<MeanStationValues>> sortedCeMap2 = new HashMap<>();
        Map<String, String> annual1= new HashMap<>();
        Map<String, String> annual2= new HashMap<>();
        //get second ce type from annualMap
        int countKey2 = 0;
        int countKey3 = 0;
        String k2=null;
        String k22=null;
        for(String k:annualMap.keySet()) {
        	countKey2++;
        	if(countKey2==2) {
        		k2=k;
        	}
        }
        for(String k:ceMap.keySet()) {
        	countKey3++;
        	if(countKey3==2) {
        		k22=k;
        		ceList2 = ceMap.get(k22);
        		//group by stno from ceList
        		groupCeMap2 = ceList2.stream()
        	    		.collect(Collectors.groupingBy(MeanStationValues::getStno));
        		sortedCeMap2 = groupCeMap2.entrySet().stream()
                		.sorted(Map.Entry.comparingByKey())
                		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        	}
        }
        
        //ceMap.keyset().size = 2 or 1
        for(String k:ceMap.keySet()) {
    		ceList = ceMap.get(k);
    		//group by stno from ceList
    		groupCeMap = ceList.stream()
    	    		.collect(Collectors.groupingBy(MeanStationValues::getStno));
    		sortedCeMap = groupCeMap.entrySet().stream()
            		.sorted(Map.Entry.comparingByKey())
            		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            //input value to table
        	int row = 0;
        	int countStn = 0;
        	for(List<MeanStationValues> value : sortedCeMap.values()){
        		if(row<sortedCeMap.size()) {//63
    	        	int count = 0;
                    String primaryColumn = value.get(0).getStno() + "  " + value.get(0).getStnCName();
                    String[] splitStno = primaryColumn.split("\\s+");
                    MeanStationValuesMap.put(header[0], splitStno[0]);
                    MeanStationValuesMap.put(header[1], splitStno[1]);
                    MeanStationValuesMap.put(header[2], ceXmlList.getChineseTitle()+"\\日");
                    MeanStationValuesMap.put(header[3], (ceXmlList.getEnglishUnit() != null)? ceXmlList.getEnglishUnit() : "");
                    MeanStationValuesMap.put(header[4], year);
                    for(List<MeanStationValues> value2: sortedCeMap2.values()) {
                    	if(value2.get(0).getStno().equals(value.get(0).getStno())) {
                    		for(int col=0; col<month; col++) { //config.month = 12
                                String dayOfMonth = null;
                                if(value.get(count).getColumnTime()==null) {
                                	dayOfMonth = "";
                                }else {
                                	dayOfMonth = String.valueOf(value.get(count).getColumnTime().getDayOfMonth());
                                }
                                //if two values are equal to the same special symbol
                                if(specialValueList.contains(value.get(count).getMonthlyValue())){
                                	if(value.get(count).getMonthlyValue().equals(value2.get(count).getMonthlyValue())){
                                		MeanStationValuesMap.put(header[5+col], "  "+"  "+value2.get(count).getMonthlyValue()+
                                        		"\\"+dayOfMonth);
                                	}
                                }else {
                                	MeanStationValuesMap.put(header[5+col], value.get(count).getMonthlyValue()+"\\"+
                                    		value2.get(count).getMonthlyValue()+
                                    		"\\"+dayOfMonth);
                                }
            	                count++;
            	        	}
                    	}
                    }
        		}
        		//annual statistic
        		if(countStn<stnList.size()) {
        			if(annualMap.get(k).get(value.get(0).getStno()).getColumnTime()==null) {
        				MeanStationValuesMap.put(header[17], "  " +annualMap.get(k).get(stnList.get(countStn)).getMonthlyValue()+
            					"\\      "+ "  ");
        			}else {
	        			Month monthOfYear = annualMap.get(k).get(value.get(0).getStno()).getColumnTime().getMonth();
	        			String mon =monthOfYear.getDisplayName(TextStyle.SHORT,Locale.UK);
	        			MeanStationValuesMap.put(header[17], "  " +annualMap.get(k).get(stnList.get(countStn)).getMonthlyValue()+
	        					"\\"+annualMap.get(k2).get(stnList.get(countStn)).getMonthlyValue()+
	        					"\\"+
	        					mon + "."+annualMap.get(k).get(stnList.get(countStn)).getColumnTime().getDayOfMonth());
        			}
        		}
        		countStn++;
        		row++;
        		// write the customer maps
    		    mapWriter.write(MeanStationValuesMap, header, processors);
            }
        	break;
        }
		
	}
}
