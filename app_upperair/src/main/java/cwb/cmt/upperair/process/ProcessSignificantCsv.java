package cwb.cmt.upperair.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import cwb.cmt.upperair.dataProcess.IDataProcessSignificant;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.CsvHeader;

@Service
public class ProcessSignificantCsv {

	@Autowired
	IDataProcessSignificant significantProcess;
	
	private String filepath;
	
	private static final Logger logger = LogManager.getLogger();
	
	private boolean newFile = true;
	
	public void init(String fileAbsolutePath) {
		this.filepath = fileAbsolutePath;
		this.newFile = true;
	}
	
	public void writeCsv(Station station, YearMonth yearMonth, String hour) 
			throws IOException {
		List<List<Object>> list = significantProcess.getCsvMonthlyData(station, yearMonth, hour);
		writeToFile(list, newFile);
		// once the new file was created, csv writer will append the next csv in the existing file.
		newFile = false;
		String logMsg = String.format("Create %s %d-%d %s:00 standard, tropopause, last levels csv files."
				, station.getStnCName(), yearMonth.getYear(), yearMonth.getMonthValue(), hour); 
		logger.info(logMsg);
	}
	
	private static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] {
				new NotNull(), // 測站
				new NotNull(), // sation
				new NotNull(), // 氣壓計海拔高度 
				new NotNull(), // 年
				new NotNull(), // 月
				new NotNull(), // 日
				new NotNull(), // 觀測時間
				new NotNull(), // 緯度
				new NotNull(), // 經度
				new NotNull(), // SQ
				new NotNull(), // P
				new NotNull(), // H
				new NotNull(), // T
				new NotNull(), // U
				new NotNull(), // dd
				new NotNull(), // ff
			};
		return processors;
	}
	
	private static String[] getHeaders() {
		final String[] header =  new String[] {
				CsvHeader.stnCName.getName(),
				CsvHeader.stnEName.getName(),
				CsvHeader.hBarom.getName(),
				CsvHeader.year.getName(),
				CsvHeader.month.getName(),
				CsvHeader.date.getName(),
				CsvHeader.hour.getName(),
				CsvHeader.latitude.getName(),
				CsvHeader.logitute.getName(),
				CsvHeader.SQ.getName(),
				CsvHeader.p.getName(),
				CsvHeader.H.getName(),
				CsvHeader.T.getName(),
				CsvHeader.U.getName(),
				CsvHeader.dd.getName(),
				CsvHeader.ff.getName()
		};
		return header;
	}
	
	private void writeToFile(List<List<Object>> source, boolean newFile) throws IOException {
		final CellProcessor[] processors = getProcessors();
		final String[] headers = getHeaders();
		try (FileOutputStream fileStream = new FileOutputStream(new File(filepath), false);
			OutputStreamWriter writer = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
			ICsvListWriter listWriter = new CsvListWriter(writer, CsvPreference.STANDARD_PREFERENCE);) {
			if(newFile) {
				// \ufeff is to used to show Chinese in Excel, otherwise, it would be garbled text.
				// \ufeff is an invalid character
				writer.write("\ufeff");
				listWriter.writeHeader(headers);
			}
			for(List<Object> o : source) {
				listWriter.write(o, processors);
			}
		} 
	}
}
