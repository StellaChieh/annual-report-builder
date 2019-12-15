package cwb.cmt.surface.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import cwb.cmt.surface.model.Station;

@Service("createCsvForListOfWeatherStns")
public class CreateCsvForListOfWeatherStns {
	protected String path;
	protected String filename;
	//Time: year
    @Resource(name="year")
    protected int year;
	
	private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] { 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
        };
        return processors;
	}
	
	
	public void createCsv(Object...objects) throws IOException {
		@SuppressWarnings("unchecked")
		List<Station> stns = (List<Station>) objects[0];
		String outputCsvFolder = (String) objects[1];
		 
		final String[] header = new String[] {"隸屬單位/Organization", "站號/StationNo.", 
				"縣市/City", "鄉鎮區/County"};
		
		ICsvMapWriter mapWriter = null;
		try {
			String outputPath = Paths.get(outputCsvFolder, year+"年專用氣象觀測站編號一覽表.csv").toString();
			FileOutputStream fileStream = new FileOutputStream(new File(outputPath), true);
			OutputStreamWriter fileWriter = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
//			FileWriter fileWriter = new FileWriter(outputPath);
			fileWriter.write('\ufeff');
			mapWriter = new CsvMapWriter(fileWriter,
	                CsvPreference.STANDARD_PREFERENCE);
			final CellProcessor[] processors = getProcessors();
			// write the header
		    mapWriter.writeHeader(header);
			for(int row=0; row<stns.size(); row++) {
				Map<String, Object> stationMap = new HashMap<String, Object>();
			    stationMap.put(header[0], stns.get(row).getCharge_ins());
			    stationMap.put(header[1], stns.get(row).getStno());
			    stationMap.put(header[2], stns.get(row).getCity());
			    String district = "      ";
			    if (stns.get(row).getAddress() != null && !stns.get(row).getAddress().trim().isEmpty()) {
					int begin=-1, end=-1;
				    boolean addressContainsCity = stns.get(row).getAddress().startsWith(stns.get(row).getCity());
	 			    begin = (addressContainsCity)
		    		        ? stns.get(row).getCity().length()
		    		        : 0;
	                end=begin+3;
	                district = stns.get(row).getAddress().substring(begin, end);
			    }
			    stationMap.put(header[3], district);
			    // write the customer maps
			    mapWriter.write(stationMap, header, processors);
			}
		}
		finally {
            if( mapWriter != null ) {
                    mapWriter.close();
            }
        }
	}
}
