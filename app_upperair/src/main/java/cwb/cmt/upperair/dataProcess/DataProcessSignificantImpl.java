package cwb.cmt.upperair.dataProcess;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cwb.cmt.upperair.dao.SignificantDao;
import cwb.cmt.upperair.model.SignificantData;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.DigitType;
import cwb.cmt.upperair.utils.SignificantBookTable;
import cwb.cmt.upperair.utils.StandardColumn;
import cwb.cmt.upperair.utils.Utility;

@Service
public class DataProcessSignificantImpl implements IDataProcessSignificant {

	@Autowired
	private SignificantDao significantLevelsDao;
	
	private String stno;
	private YearMonth yearMonth;
	private String hour;
	
	List<SignificantData> processResultMap = new ArrayList<>();
	
	private boolean everProcess(String stno, YearMonth yearMonth, String hour) {
		if(this.stno == null
				|| !this.stno.equals(stno)
				|| this.yearMonth.getMonthValue() != yearMonth.getMonthValue()  
				|| !this.hour.equalsIgnoreCase(hour)) {
			this.stno = stno;
			this.yearMonth = yearMonth;
			this.hour = hour;
			processResultMap.clear();
			return false;
		} else {
			return true;
		}
	}
	
	
	private List<SignificantData> queryMonthlyData(String stno, YearMonth yearMonth, String hour) {
		List<SignificantData> queryResult = new ArrayList<>();
		String beginTime = new StringBuilder().append(yearMonth.getYear())
								.append("-").append(String.format("%02d", yearMonth.getMonthValue()))
								.append("-01 00:00:00").toString();

		String endTime = new StringBuilder().append(yearMonth.getYear())
								.append("-").append(String.format("%02d", yearMonth.getMonthValue() + 1))
								.append("-01 00:00:00").toString();
		String tableName = yearMonth.getYear() == LocalDate.now().getYear() ? DbTable.AIRUP.value() : DbTable.HIS_AIRUP.value();
				
		Map<String, String> params = new HashMap<>();
		params.put("table", tableName);
		params.put("stno", stno);
		params.put("hour", hour);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		queryResult.addAll(significantLevelsDao.selectSignificantLevelsData(params));
		
		return queryResult;
	}

	private List<SignificantData> processMonthlyData(Station station, YearMonth yearMonth, String hour){
		if(everProcess(station.getStno(), yearMonth, hour)) {
			return processResultMap;
		}
		
		List<SignificantData> source = queryMonthlyData(station.getStno(), yearMonth, hour);
		
		// sort by timestamp, layer
		source.sort(Comparator.comparing(SignificantData::getTime)
				.thenComparing(SignificantData::getLayer));
				
		// grouping by LocalDate
		Map<LocalDate, List<SignificantData>> dateMap = source.stream().collect(
				 Collectors.groupingBy(f -> f.getTime().toLocalDateTime().toLocalDate()));
 
		for(int date=1; date<=yearMonth.lengthOfMonth(); date++) {
			LocalDate localDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), date);
			// if the records of one day are null, we add fake record list
			dateMap.computeIfAbsent(localDate, k -> {
				LocalDateTime time = LocalDateTime.of(k, LocalTime.of(Integer.valueOf(hour), 0));
				return createFakeListOfCertainStnoAndDate(station.getStno(), time, 36);
			});
			// if the number of records of one LocalDate group didn't reach 36,
			// we add fake record
			int listSize = dateMap.get(localDate).size();
			if(listSize < 36) {
				LocalDateTime time = LocalDateTime.of(localDate, LocalTime.of(Integer.valueOf(hour), 0));
				dateMap.get(localDate).addAll(createFakeListOfCertainStnoAndDate(station.getStno(), time, 36-listSize));
			}
			// set Sq
			for(int j=1; j<=36; j++) {
				dateMap.get(localDate).get(j-1).setSq(j);
			}
		}
			
		// Map<LocalDate, List<SignificantData>> 
		// --> flatMap to --> List<SignificantData> 
		// --> stream --> turn SignificatnData field's value to adjusted value
		processResultMap = dateMap.values().stream().flatMap(singleList -> 
							singleList.stream().map( k -> turnToPrintedvalue(k)))
							.collect(Collectors.toList()); 
		
		return processResultMap;
	}
	
	
	// process records, turn it from List<SginificantData> into Map<Integer, List<String>>
 	// key of the map is sq, value of map is p, h, t, u, dd, ff list
    private Map<Integer, List<String>> dispatchTablelyRecords(List<SignificantData> records
    												, int beginDate, int endDate){	
		Map<Integer, List<SignificantData>> sqMapTemp = records.stream()
						.filter(data -> 
									(beginDate <= data.getTime().toLocalDateTime().getDayOfMonth()) && 
									(data.getTime().toLocalDateTime().getDayOfMonth() <= endDate))
						.collect(Collectors.groupingBy(SignificantData::getSq));

		Map<Integer, List<String>>sqMap = new HashMap<>(); 
		sqMapTemp.forEach((k, v) -> {
			List<String> list = v.stream().sorted(Comparator.comparing(SignificantData::getTime))
								.flatMap(d-> d.turnToFlapList().stream()).collect(Collectors.toList());
			sqMap.put(k, list);
		});						
		return sqMap;
    }
	
	@Override
	public Map<SignificantBookTable, Map<Integer, List<String>>> getPdfMonthlyData(Station station, YearMonth yearMonth, String hour) {
		List<SignificantData> monthlyList = processMonthlyData(station, yearMonth, hour);
		Map<SignificantBookTable, Map<Integer, List<String>>> tableMap = new HashMap<>();
		int beginDate;
		int endDate;
		for(SignificantBookTable table : SignificantBookTable.values()) {
			// if the month has 28 days only, table8 won't show up
			if(yearMonth.lengthOfMonth() < 29 && table == SignificantBookTable.TABLE8) {
				continue;
			}
			beginDate = table.getBeginDate();
			endDate = (table == SignificantBookTable.TABLE8) ? 
						yearMonth.lengthOfMonth() : beginDate+3; 
			tableMap.put(table, dispatchTablelyRecords(monthlyList, beginDate, endDate));
		}
		return tableMap;
	}

	@Override
	public List<List<Object>> getCsvMonthlyData(Station station, YearMonth yearMonth, String hour) {
		List<SignificantData> processResult = processMonthlyData(station, yearMonth, hour);
		processResult.sort(Comparator.comparing(SignificantData::getTime)
					.thenComparing(SignificantData::getSq));
		List<List<Object>> csvResult = new ArrayList<List<Object>>();
		processResult.forEach( e -> {
			List<Object> csvRow = new ArrayList<Object>();
			csvRow.add(station.getStnCName());
			csvRow.add(station.getStnEName());
			csvRow.add(station.getPrintedHBarometer());
			csvRow.add(Utility.getYearFromTimestamp(e.getTime()));
			csvRow.add(Utility.getMonthValueFromTimestamp(e.getTime()));
			csvRow.add(Utility.getDateOfMonthFromTimestamp(e.getTime()));
			csvRow.add(hour + ":00");
			csvRow.add(station.getLatitude());
			csvRow.add(station.getLongitude());
			csvRow.add(e.getSq());
			csvRow.add(e.getPrintedP());
			csvRow.add(e.getPrintedH());
			csvRow.add(e.getPrintedT());
			csvRow.add(e.getPrintedU());
			csvRow.add(e.getPrintedDd());
			csvRow.add(e.getPrintedFf());
			csvResult.add(csvRow);
		});
		
		return csvResult;
	}
	
	
	
	private List<SignificantData> createFakeListOfCertainStnoAndDate(String stno, LocalDateTime time, int number){
		List<SignificantData> list = new ArrayList<>(); 
		for(int i=1; i<=number; i++) { 
			// LocalDate -> LocalDateTime (combines 00:00:00) -> TimeStamp
			SignificantData fakeRecord = new SignificantData(stno, Timestamp.valueOf(time));
			fakeRecord.setP(StandardColumn.P.specialValue());
			fakeRecord.setH(StandardColumn.H.specialValue());
			fakeRecord.setT(StandardColumn.T.specialValue());
			fakeRecord.setU(StandardColumn.U.specialValue());
			fakeRecord.setDd(StandardColumn.dd.specialValue());
			fakeRecord.setFf(StandardColumn.ff.specialValue());
			list.add(fakeRecord);
		}
		return list;
	}
	
	private SignificantData turnToPrintedvalue(SignificantData oData) {
		String printedP = replaceEmptyAndSpecialValueThenTruncated(oData.getP(), StandardColumn.P);
		String printedH = replaceEmptyAndSpecialValueThenTruncated(oData.getH(), StandardColumn.H);
		String printedT = replaceEmptyAndSpecialValueThenTruncated(oData.getT(), StandardColumn.T);							
		String printedU = replaceEmptyAndSpecialValueThenTruncated(oData.getU(), StandardColumn.U);
		String printedDd = replaceEmptyAndSpecialValueThenTruncated(oData.getDd(), StandardColumn.dd);
		String printedFf = replaceEmptyAndSpecialValueThenTruncated(oData.getFf(), StandardColumn.ff);
		
		oData.setPrintedP(printedP);
		oData.setPrintedH(printedH);
		oData.setPrintedT(printedT);
		oData.setPrintedU(printedU);
		oData.setPrintedDd(printedDd);
		oData.setPrintedFf(printedFf);
		
		return oData;
	}
	
	private String replaceEmptyAndSpecialValueThenTruncated(double oValue, StandardColumn column) {
		String digitedValue;
		if(column.digitType() == DigitType.INTEGER) {
			digitedValue = (oValue == column.specialValue())? "-" : String.valueOf((int)oValue);
		} else {
			digitedValue = (oValue == column.specialValue())? "-" : String.valueOf(oValue);
		}
		return digitedValue;
	}
	
}
