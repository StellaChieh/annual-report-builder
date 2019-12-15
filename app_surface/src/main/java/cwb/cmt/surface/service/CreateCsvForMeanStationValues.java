package cwb.cmt.surface.service;

import java.io.IOException;
import java.util.ArrayList;
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

@Service("createCsvForMeanStationValues")
public class CreateCsvForMeanStationValues {

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
		ICsvMapWriter mapWriter = (ICsvMapWriter) objects[3];
		String[] header = (String[]) objects[4];
		
		final CellProcessor[] processors = getProcessors();
		
		Map<String, Object> MeanStationValuesMap = new HashMap<String, Object>();
        List<MeanStationValues> ceList = new ArrayList<>();
        Map<String, List<MeanStationValues>> groupCeMap = new HashMap<>();
        Map<String, List<MeanStationValues>> sortedCeMap = new HashMap<>();
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
        	for(List<MeanStationValues> value : sortedCeMap.values()){
        		if(row<sortedCeMap.size()) {
    	        	int count = 0;
                    MeanStationValuesMap.put(header[0], value.get(0).getStno());
                    MeanStationValuesMap.put(header[1], value.get(0).getStnCName());
                    MeanStationValuesMap.put(header[2], ceXmlList.getChineseTitle());
                    MeanStationValuesMap.put(header[3], (ceXmlList.getEnglishUnit() != null)? ceXmlList.getEnglishUnit() : "");
                    MeanStationValuesMap.put(header[4], year);
                    for(int col=0; col<month; col++) { //config.month = 12
                    	 MeanStationValuesMap.put(header[5+col], value.get(count).getMonthlyValue());
    	                count++;
    	        	}
                    MeanStationValuesMap.put(header[17], "  " +annualMap.get(k).get(value.get(0).getStno()));
        		}
        			row++;
        			// write the customer maps
        		    mapWriter.write(MeanStationValuesMap, header, processors);
            }
        }
	}
}
