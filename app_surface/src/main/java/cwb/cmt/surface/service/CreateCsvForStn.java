package cwb.cmt.surface.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
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

@Service("createCsvForStn")
public class CreateCsvForStn {
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
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
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
		String outputCsvFolder = (String)objects[1]; 
		 
		final String[] header = new String[] {"站號/StationNo.", "測站中文名/StnCName", "測站英文名/StnEName", "北緯/North Latitude", 
				"東經/East Longitude", "氣壓計海面上高度/Height of Barometer above Sea Level (m)",
				"溫度計地面高度公尺/Height of Therm. above Ground (m)", "雨量器口面地上高度公尺/Height of Raingauge above Ground (m)",
				"風速儀地上高度公尺/Anem. above Ground (m)", "海拔公尺/Altitude(m)", "創立年份/Year of Commencement",
				"每日觀測次數/No. of Obs. Per Day"};
		ICsvMapWriter mapWriter = null;
		try {
			String outputPath = Paths.get(outputCsvFolder, year+"年中央氣象局所屬各氣象站一覽表.csv").toString();
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
			    stationMap.put(header[0], stns.get(row).getStno());
			    stationMap.put(header[1], stns.get(row).getStnCName());
			    stationMap.put(header[2], stns.get(row).getStnEName());
			    stationMap.put(header[3], " "+ stns.get(row).getLatitude().trim().replace("N", ""));
			    stationMap.put(header[4], " "+stns.get(row).getLongitude().trim().replace("E", ""));
			    stationMap.put(header[5], " "+stns.get(row).getHBarometer().trim().replace("m", ""));
			    stationMap.put(header[6], stns.get(row).getHTherm().trim().replace("m", ""));
			    stationMap.put(header[7], stns.get(row).getHRaingauge().trim().replace("m", ""));
			    stationMap.put(header[8], stns.get(row).gethAnem().trim().replace("m", "")); 
			    stationMap.put(header[9], " "+stns.get(row).getAltitude().trim().replace("m", ""));
			    stationMap.put(header[10], " "+stns.get(row).getStnBeginTime().trim().substring(0,4));
			    stationMap.put(header[11], " "+stns.get(row).getManObsNum().trim());
			    
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
