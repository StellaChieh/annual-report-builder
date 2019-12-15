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
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import cwb.cmt.upperair.dataProcess.IDataProcessStandard;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.CsvHeader;

@Service
public class ProcessStandardCsv {

	@Autowired
	IDataProcessStandard standardProcess;
	
	private String standardFilePath;
	private String tropFilePath;
	private String lastFilePath;
	private boolean newStandardFile = true;
	private boolean newTropFile = true;
	private boolean newLastFile = true;
	
	private static final Logger logger = LogManager.getLogger();
	
	public void init(String standardFilePath, String tropFilePath, String lastFilePath) {
		this.standardFilePath = standardFilePath;
		this.tropFilePath = tropFilePath;
		this.lastFilePath = lastFilePath;
		this.newStandardFile = true;
		this.newTropFile = true;
		this.newLastFile = true;
	}
	
	public void writeCsv(Station station, YearMonth yearMonth, String hour) throws IOException {
		List<List<Object>> nlhList = standardProcess.getCsvNlhMonthlyData(station, yearMonth, hour);
		List<List<Object>> standardList = standardProcess.getCsvStandardMonthlyData(station, yearMonth, hour);
		List<List<Object>> tropList = standardProcess.getCsvTropMonthlyData(station, yearMonth, hour);
		List<List<Object>> lastList = standardProcess.getCsvLastMonthlyData(station, yearMonth, hour);
		writeToFile(standardFilePath, nlhList, newStandardFile, getStandardProcessors(), getStandardHeaders());
		newStandardFile = false;
		writeToFile(standardFilePath, standardList, newStandardFile, getStandardProcessors(), getStandardHeaders());
		writeToFile(tropFilePath, tropList, newTropFile, getTropAndLastProcessors(), getTropAndLastHeaders());
		newTropFile = false;
		writeToFile(lastFilePath, lastList, newLastFile, getTropAndLastProcessors(), getTropAndLastHeaders());
		newLastFile = false;
		String logMsg = String.format("Create %s %d-%d %s:00 standard, tropopause, last levels csv files."
							, station.getStnCName(), yearMonth.getYear(), yearMonth.getMonthValue(), hour); 
		logger.info(logMsg);
	}
	
	private static CellProcessor[] getTropAndLastProcessors() {
		final CellProcessor[] processors = new CellProcessor[] {
				new NotNull(), // 測站
				new NotNull(), // sation
				new NotNull(), // 氣壓計海拔高度 
				new NotNull(), // 年
				new NotNull(), // 月
				new Optional(), // 日
				new NotNull(), // 觀測時間
				new NotNull(), // 緯度
				new NotNull(), // 經度
				new NotNull(), // Pressure Level
				new NotNull(), // 資料屬性
				new NotNull(), // P
				new NotNull(), // H
				new NotNull(), // T
				new Optional(), // dd
				new NotNull(), // ff
			};
		return processors;
	}
	
	private static CellProcessor[] getStandardProcessors() {
		final CellProcessor[] processors = new CellProcessor[] {
				new NotNull(), // 測站
				new NotNull(), // sation
				new NotNull(), // 氣壓計海拔高度 
				new NotNull(), // 年
				new NotNull(), // 月
				new Optional(), // 日
				new NotNull(), // 觀測時間
				new NotNull(), // 緯度
				new NotNull(), // 經度
				new Optional(), // synoptic
				new Optional(), // Pressure Level
				new Optional(), // 資料屬性
				new Optional(), // P
				new Optional(), // H
				new Optional(), // T
				new Optional(), // U
				new Optional(), // Td
				new Optional(), // dd
				new Optional(), // ff
			};
		return processors;
	}
	
	static String[] getStandardHeaders() {
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
				CsvHeader.synoptic.getName(),
				CsvHeader.pressureLevel.getName(),
				CsvHeader.attribute.getName(),
				CsvHeader.p.getName(),
				CsvHeader.H.getName(),
				CsvHeader.T.getName(),
				CsvHeader.U.getName(),
				CsvHeader.Td.getName(),
				CsvHeader.dd.getName(),
				CsvHeader.ff.getName()
		};
		return header;
	}
	
	
	static String[] getTropAndLastHeaders() {
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
				CsvHeader.pressureLevel.getName(),
				CsvHeader.attribute.getName(),
				CsvHeader.p.getName(),
				CsvHeader.H.getName(),
				CsvHeader.T.getName(),
				CsvHeader.dd.getName(),
				CsvHeader.ff.getName()
		};
		return header;
	}
	
	public static int getStandardColumnsCount() {
		return getStandardHeaders().length;
	}
	
	public static int getTropAndLastColumnsCount() {
		return getTropAndLastHeaders().length;
	}
	
	private void writeToFile(String filePath, List<List<Object>> source, boolean newFile
			, CellProcessor[] processors, String[] headers) throws IOException {
		try (FileOutputStream fileStream = new FileOutputStream(new File(filePath), true);
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
