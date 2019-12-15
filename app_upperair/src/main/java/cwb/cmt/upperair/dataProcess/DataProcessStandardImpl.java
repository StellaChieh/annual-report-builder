package cwb.cmt.upperair.dataProcess;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cwb.cmt.upperair.dao.StandardDao;
import cwb.cmt.upperair.model.StandardData;
import cwb.cmt.upperair.model.StandardNlhData;
import cwb.cmt.upperair.model.StandardParentData;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.process.ProcessStandardCsv;
import cwb.cmt.upperair.utils.StandardAttribute;
import cwb.cmt.upperair.utils.StandardColumn;
import cwb.cmt.upperair.utils.DigitType;
import cwb.cmt.upperair.utils.StandardGroup;
import cwb.cmt.upperair.utils.StandardLevel;
import cwb.cmt.upperair.utils.StandardBookTable;
import cwb.cmt.upperair.utils.Utility;

@Service
public class DataProcessStandardImpl implements IDataProcessStandard{

	@Autowired
	StandardDao dao;
	
	private String stno;
	private YearMonth yearMonth;
	private String hour;
	
	private Map<StandardGroup, List <? extends StandardParentData>> processResultMap = new HashMap<>();
	
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
	
	private Map<StandardLevel, List<? extends StandardParentData>> queryMonthlyData(String stno, YearMonth yearMonth, String hour){
		String beginTime = new StringBuilder().append(yearMonth.getYear())
				.append("-").append(String.format("%02d", yearMonth.getMonthValue()))
				.append("-01 00:00:00").toString();

		String endTime = new StringBuilder().append(yearMonth.getYear())
						.append("-").append(String.format("%02d", yearMonth.getMonthValue() + 1))
						.append("-01 00:00:00").toString();
		String airupTable = yearMonth.getYear() == LocalDate.now().getYear() ? DbTable.AIRUP.value() : DbTable.HIS_AIRUP.value();
		String airupCodeTable = yearMonth.getYear() == LocalDate.now().getYear() ? DbTable.AIRUPCODE.value() : DbTable.HIS_AIRUPCODE.value();
		Map<String, String> params = new HashMap<>();
		params.put("airup_table", airupTable);
		params.put("airupcode_table", airupCodeTable);
		params.put("stno", stno);
		params.put("hour", hour);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		
		HashMap<StandardLevel, List<? extends StandardParentData>> queryResult = new HashMap<>();
		queryResult.put(StandardLevel.NLH, dao.selectNlhData(params));
		queryResult.put(StandardLevel.SURFACE, dao.selectSurfaceData(params));
		queryResult.put(StandardLevel.STANDARD, dao.selectStandardLevelsData(params));
		queryResult.put(StandardLevel.TROPI, dao.selectTropopauseIData(params));
		queryResult.put(StandardLevel.TROPII, dao.selectTropopauseIIData(params));
		queryResult.put(StandardLevel.LAST, dao.selectlLastLevelData(params));
		return queryResult ;
	}
	
	private Map<StandardGroup, List<? extends StandardParentData>> processMonthlyData(Station station, YearMonth yearMonth, String hour){
		if(everProcess(station.getStno(), yearMonth, hour)) {
			return processResultMap;
		}
		
		Map<StandardLevel, List<? extends StandardParentData>> sourceMap = queryMonthlyData(station.getStno(), yearMonth, hour);
		
		// add level, group, attribute field to each StandardData or StandardNlhData
		sourceMap.entrySet().stream().forEach( e -> {
						List<? extends StandardParentData> list = e.getValue();
						list.stream().forEach( i -> {
								i.setAttribute(StandardAttribute.SOURCE);
								addLevelAndGroupField(e.getKey(), i);
						});			
		});
		
		// group by group field
		@SuppressWarnings("unchecked")
		Map<StandardGroup, List<StandardData>> groupMap = sourceMap.entrySet().stream()
				.filter(e -> e.getKey() != StandardLevel.NLH)
				.flatMap(e -> ((List<StandardData>)e.getValue()).stream())
				.collect(Collectors.groupingBy(StandardData::getGroup));		
		
		for(StandardGroup group : StandardGroup.values()) {
			if(group == StandardGroup.NLH) {
				continue;
			}
			// create fake list for that group which has no data
			if(groupMap.get(group) == null) {
				groupMap.put(group, createFakeListForOneGroup(station.getStno(), yearMonth, group));
			}
			// fill the group if record of any day in month is null
			if(groupMap.get(group).size() < yearMonth.lengthOfMonth()) {
				fillGroup(groupMap.get(group), station.getStno(), yearMonth, group);
			}
			// sort by date
			groupMap.get(group).sort(Comparator.comparing(StandardData::getTime));
		}
		
		// calculate each group's statistic value
		groupMap.entrySet().forEach(e -> addStatisticRecord(e.getValue(), e.getKey()));
		
		// turn to printed value (replace special value, truncate to digit scale.)
		groupMap.entrySet().stream()
							.flatMap(e -> e.getValue().stream().map( i -> turnToPrintedValue(i)))
							.collect(Collectors.groupingBy(StandardData::getGroup));
//							.entrySet().forEach(e -> System.out.println(e.getKey()+ ":::" + e.getValue()));
							
		// put Nlh records into groupMap
		processResultMap.put(StandardGroup.NLH, sourceMap.get(StandardLevel.NLH));
		groupMap.entrySet().forEach(e -> processResultMap.put(e.getKey(), e.getValue()));
		
		return processResultMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<StandardBookTable, Map<Integer, List<String>>> getPdfMonthlyData(Station station, 
															YearMonth yearMonth, String hour) {		
		Map<StandardGroup, List<? extends StandardParentData>> groupMap = processMonthlyData(station, yearMonth, hour);
		
		// enlarge every group's list, until to size of 38 (31 days + sum + no + mean + max + max date + min + min date)
		groupMap.entrySet().forEach(e -> {
			List< ? extends StandardParentData> list = e.getValue();
			if(e.getKey() == StandardGroup.NLH) {
				int listSize = list.size();
				if(listSize < 38) { 
					// nlh mark in day row is "-"
					((List<StandardNlhData>)list).addAll(createFakeDashStandardNlhDataList((31 - listSize), "-"));
					listSize = list.size();
					// nlh mark in other row is just " "
					((List<StandardNlhData>)list).addAll(createFakeDashStandardNlhDataList((38 - listSize), " "));
				}
			} else {
				int listSize = list.size();
				if(listSize < 38) {
					((List<StandardData>)list).addAll(yearMonth.lengthOfMonth(), createFakeDashStandardDataList(e.getKey(), 38-listSize));
				}	
			}
		});
				
		Map<StandardBookTable, Map<Integer, List<String>>> tableMap = new HashMap<>();
		tableMap.put(StandardBookTable.TABLE1, getPdfTablyData(groupMap, StandardBookTable.TABLE1));
		tableMap.put(StandardBookTable.TABLE2, getPdfTablyData(groupMap, StandardBookTable.TABLE2));
		tableMap.put(StandardBookTable.TABLE3, getPdfTablyData(groupMap, StandardBookTable.TABLE3));
		tableMap.put(StandardBookTable.TABLE4, getPdfTablyData(groupMap, StandardBookTable.TABLE4));
		tableMap.put(StandardBookTable.TABLE5, getPdfTablyData(groupMap, StandardBookTable.TABLE5));
		
		return tableMap;
	}
	
	private Map<Integer, List<String>> getPdfTablyData(Map<StandardGroup, List<? extends StandardParentData>> groupMap
															, StandardBookTable bookTable) {
		Map<Integer, List<String>> dayMap = new HashMap<>();
		
		for(int i=1; i <= bookTable.rowCount(); i++) {	
			// put nlh data in table1 
			if (bookTable == StandardBookTable.TABLE1) { 
				@SuppressWarnings("unchecked")
				List<StandardNlhData> nlhList = (List<StandardNlhData>)groupMap.get(StandardGroup.NLH);
				dayMap.put(i, new ArrayList<String>(Arrays.asList(nlhList.get(i-1).getNlh())));
			} else { // other table don't have nlh data, so just create empty list in each day
				dayMap.put(i, new ArrayList<String>());
			}		
		} 
			
		// literate dayMap, and put data into dayMap from the table's groups
		dayMap.entrySet().forEach(e -> {
			for(StandardGroup g : StandardGroup.values()) {
				if(g == StandardGroup.NLH) {
					continue;
				}
				if(bookTable == g.getTable()) {
					@SuppressWarnings("unchecked")
					List<StandardData> list = (List<StandardData>)groupMap.get(g);
					if(e.getKey() <= 31) { // source record
						e.getValue().addAll((list.get(e.getKey()-1).printPdfValues()));
					} else {
						if(bookTable.hasSumRow()) { 
							e.getValue().addAll((list.get(e.getKey()-1).printPdfValues()));
						} else { // table which don't have sum row, don't have to pick up sum data
							e.getValue().addAll((list.get(e.getKey()).printPdfValues()));
						}
					}
				}
			}
		});
		
		return dayMap;
	}

	@Override
	public List<List<Object>> getCsvNlhMonthlyData(Station station, YearMonth yearMonth, String hour) {
		Map<StandardGroup, List<? extends StandardParentData>> sourceGroup = processMonthlyData(station, yearMonth, hour);
		
		@SuppressWarnings("unchecked")
		List<StandardNlhData> nlhList = (List<StandardNlhData>)(sourceGroup.get(StandardGroup.NLH));

		return getCsvMonthlyData(station, yearMonth, hour, nlhList);
		
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<List<Object>> getCsvStandardMonthlyData(Station station, YearMonth yearMonth, String hour) {
		Map<StandardGroup, List<? extends StandardParentData>> sourceGroup = processMonthlyData(station, yearMonth, hour);
		Map<StandardGroup, List<StandardData>> standardGroup = new HashMap<StandardGroup, List<StandardData>>();
		for(StandardGroup g : StandardGroup.values()) {
			if (g.getLevel() == StandardLevel.SURFACE
					||g.getLevel() == StandardLevel.STANDARD) {
				standardGroup.put(g, (List<StandardData>)sourceGroup.get(g));
			}
		}
		standardGroup.entrySet().forEach(e -> mergeExtremeAndDateRecord(e.getValue()));
		List<StandardData> standardList = standardGroup.values().stream()
											.flatMap(e-> e.stream())
											.filter(k-> k.getAttribute()!= StandardAttribute.SUM)
											.collect(Collectors.toList());
		
		return getCsvMonthlyData(station, yearMonth, hour, standardList);
	}

	@Override
	public List<List<Object>> getCsvTropMonthlyData(Station station, YearMonth yearMonth, String hour) {
		
		Map<StandardGroup, List<? extends StandardParentData>> sourceGroup = processMonthlyData(station, yearMonth, hour);
		
		@SuppressWarnings("unchecked")
		List<StandardData> tropIList = (List<StandardData>)(sourceGroup.get(StandardGroup.TROPI));
		mergeExtremeAndDateRecord(tropIList);
		
		@SuppressWarnings("unchecked")
		List<StandardData> tropIIList = (List<StandardData>)(sourceGroup.get(StandardGroup.TROPII));
		mergeExtremeAndDateRecord(tropIIList);
		
		List<StandardData> tropList = new ArrayList<>();
		tropList.addAll(tropIList);
		tropList.addAll(tropIIList);
		
		return getCsvMonthlyData(station, yearMonth, hour, tropList);
		
	}
	
	@Override
	public List<List<Object>> getCsvLastMonthlyData(Station station, YearMonth yearMonth, String hour) {
		Map<StandardGroup, List<? extends StandardParentData>> sourceGroup = processMonthlyData(station, yearMonth, hour);
		
		@SuppressWarnings("unchecked")
		List<StandardData> lastList = (List<StandardData>)(sourceGroup.get(StandardGroup.LAST));
		mergeExtremeAndDateRecord(lastList);
	
		return getCsvMonthlyData(station, yearMonth, hour, lastList);
		
	}

	private List<List<Object>> getCsvMonthlyData(Station station, YearMonth yearMonth, String hour
										, List<? extends StandardParentData> source) {
		source.sort(Comparator.comparing(StandardParentData::getLevel)
				.thenComparing(Comparator.comparing(StandardParentData::getGroup))
				.thenComparing(Comparator.comparing(StandardParentData::getAttribute))
				.thenComparing(Comparator.comparing(StandardParentData::getTime)));
		
		List<List<Object>> result = new ArrayList<>();
		for(StandardParentData s : source) {
			List<Object> sList = new ArrayList<>();
			sList.add(station.getStnCName());
			sList.add(station.getStnEName());
			sList.add(station.getPrintedHBarometer());
			sList.add(Utility.getYearFromTimestamp(s.getTime()));
			sList.add(Utility.getMonthValueFromTimestamp(s.getTime()));
			if(s.getAttribute() == StandardAttribute.SOURCE) {
				sList.add(Utility.getDateOfMonthFromTimestamp(s.getTime()));
			} else {
				sList.add(null);
			}
			sList.add(hour + ":00");
			sList.add(station.getLatitude());
			sList.add(station.getLongitude());
			switch (s.getLevel()){
				case NLH:
					generateNlhCsvList((StandardNlhData)s, sList);
					break;
				case SURFACE:
				case STANDARD:	
					generateStandardCsvList((StandardData)s, sList);
					break;
				default:
					generateTropAndLastCsvList((StandardData)s, sList);
					
			}
			result.add(sList);
		}
		return result;
	}
	
	private List<Object> generateNlhCsvList(StandardNlhData s, List<Object> sList) {
		sList.add(s.getNlh());
		sList.add(null); // pressure level
		sList.add(s.getAttribute());
		int lengthGap = ProcessStandardCsv.getStandardColumnsCount()-sList.size();
		for(int i=0; i < lengthGap; i++) {
			sList.add(null);
		}
		return sList;
	}
	
	private List<Object> generateStandardCsvList(StandardData s, List<Object> list) {
		list.add(null); // for nlh synopic 
		list.add(s.getGroup().getGroupNameInBook());
		if(s.getAttribute() == StandardAttribute.SOURCE) {
			list.add(s.getAttribute().name());
		} else {
			if(s.getAttribute() == StandardAttribute.MAX || s.getAttribute() == StandardAttribute.MIN) {
				list.add("STATISTIC-" + s.getAttribute().name() + "\\DATE");
			} else {
				list.add("STATISTIC-" + s.getAttribute().name());
			}
		}
		List<StandardColumn> columns = s.getGroup().getColumns();
		String p = columns.contains(StandardColumn.P)? s.getPrintedP() : null;
		String h = columns.contains(StandardColumn.H)? s.getPrintedH() : null;
		String t = columns.contains(StandardColumn.T)? s.getPrintedT() : null;
		String u = columns.contains(StandardColumn.U)? s.getPrintedU() : null;
		String td = columns.contains(StandardColumn.Td)? s.getPrintedTd() : null;
		String dd = columns.contains(StandardColumn.dd)? s.getPrintedDd() : null;
		String ff = columns.contains(StandardColumn.ff)? s.getPrintedFf() : null;
		list.add(p);
		list.add(h);
		list.add(t);
		list.add(u);
		list.add(td);
		list.add(dd);
		list.add(ff);
		
		return list;
	}
	
	private List<Object> generateTropAndLastCsvList(StandardData s, List<Object> list) {
		list.add(s.getGroup().getGroupNameInBook());
		if(s.getAttribute() == StandardAttribute.SOURCE) {
			list.add(s.getAttribute().name());
		} else {
			if(s.getAttribute() == StandardAttribute.MAX || s.getAttribute() == StandardAttribute.MIN) {
				list.add("STATISTIC-" + s.getAttribute().name() + "\\DATE");
			} else {
				list.add("STATISTIC-" + s.getAttribute().name());
			}
		}
		list.addAll(s.printPdfValues());
		
		return list;
	}

	private void mergeExtremeAndDateRecord(List<StandardData> sourceList){
		StandardData maxRecord = sourceList.stream().filter(e -> e.getAttribute() == StandardAttribute.MAX).findAny().get();
		StandardData maxDateRecord = sourceList.stream().filter(e -> e.getAttribute() == StandardAttribute.MAX_DATE).findAny().get();
		mergeExtremeAndDateRecord(maxRecord, maxDateRecord);
		sourceList.remove(maxDateRecord);
		StandardData minRecord = sourceList.stream().filter(e -> e.getAttribute() == StandardAttribute.MIN).findAny().get();
		StandardData minDateRecord = sourceList.stream().filter(e -> e.getAttribute() == StandardAttribute.MIN_DATE).findAny().get();
		mergeExtremeAndDateRecord(minRecord, minDateRecord);
		sourceList.remove(minDateRecord);
	}

	private StandardData mergeExtremeAndDateRecord(StandardData extreme, StandardData date){
		extreme.setPrintedP(extreme.getPrintedP() + "\\" + date.getPrintedP());
		extreme.setPrintedH(extreme.getPrintedH() + "\\" + date.getPrintedH());
		extreme.setPrintedT(extreme.getPrintedT() + "\\" + date.getPrintedT());
		extreme.setPrintedU(extreme.getPrintedU() + "\\" + date.getPrintedU());
		extreme.setPrintedTd(extreme.getPrintedTd() + "\\" + date.getPrintedTd());
		extreme.setPrintedDd("");
		extreme.setPrintedFf(extreme.getPrintedFf() + "\\" + date.getPrintedFf());
		return extreme;
	}
	
	private void addLevelAndGroupField(StandardLevel level, StandardParentData data) {
		switch(level) {
			case NLH:
				data.setLevel(StandardLevel.NLH);
				data.setGroup(StandardGroup.NLH);
				break;
			case SURFACE:
				data.setLevel(StandardLevel.SURFACE);
				data.setGroup(StandardGroup.SURFACE);
				break;
			case STANDARD:
				data.setLevel(StandardLevel.STANDARD);
				data.setGroup(StandardGroup.LOOKP_FROM_PRESSURE((int)((StandardData)data).getP()));
				break;
			case TROPI:
				data.setLevel(StandardLevel.TROPI);
				data.setGroup(StandardGroup.TROPI);
				break;
			case TROPII:
				data.setLevel(StandardLevel.TROPII);
				data.setGroup(StandardGroup.TROPII);
				break;	
			case LAST:
				data.setLevel(StandardLevel.LAST);
				data.setGroup(StandardGroup.LAST);
				break;	
		}
	}
		
	private List<StandardData> fillGroup(List<StandardData> source, String stno, YearMonth yearMonth, StandardGroup group){
		int[] emptyDate = new int[yearMonth.lengthOfMonth()];
		// we use array index to memorize date, and array value to signal if record of that date is empty or not
		// emtyDate[2] = 1 means that date 3 has record, empDate[2] = 0 means no record
		source.forEach(e -> emptyDate[e.getTime().toLocalDateTime().getDayOfMonth()-1] = 1);
		for(int i=0; i<emptyDate.length; i++) {
			if(emptyDate[i] != 1) { // fill fake record for that date
				source.add(createOneFakeStandardData(stno, LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), i+1)
												, StandardAttribute.SOURCE, group));
			}
		}
		return source;
	}
	
	private List<StandardData> createFakeListForOneGroup(String stno
							, YearMonth yearMonth, StandardGroup group){
		List<StandardData> list = new ArrayList<>(); 
		for(int i=1; i<=yearMonth.lengthOfMonth(); i++) { 
			StandardData fake = createOneFakeStandardData(stno
					, LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), i)
					, StandardAttribute.SOURCE, group);
			list.add(fake);
		}
		return list;
	}
	
	private List<StandardNlhData> createFakeDashStandardNlhDataList(int number, String s) {
		List<StandardNlhData> list = new ArrayList<>();
		for (int i=0; i<number; i++) {
			StandardNlhData fakeData = new StandardNlhData();
			fakeData.setNlh(s);
			list.add(fakeData);
		}
		return list;
	}
	
	private List<StandardData> createFakeDashStandardDataList(StandardGroup g, int number) {
		List<StandardData> list = new ArrayList<>();
		for(int i=0; i<number; i++) {
			StandardData fakeRecord = new StandardData();
			fakeRecord.setGroup(g);
			fakeRecord.setPrintedP("-");
			fakeRecord.setPrintedH("-");
			fakeRecord.setPrintedT("-");
			fakeRecord.setPrintedU("-");
			fakeRecord.setPrintedTd("-");
			fakeRecord.setPrintedDd("-");
			fakeRecord.setPrintedFf("-");
			list.add(fakeRecord);
		}
		return list;
	}
	
	private StandardData createOneFakeStandardData(String stno, LocalDate date, StandardAttribute attribute, StandardGroup group) {
		StandardData fakeRecord = new StandardData();
		fakeRecord.setStno(stno);
		fakeRecord.setTime(Timestamp.valueOf(date.atStartOfDay()));
		fakeRecord.setLevel(group.getLevel());
		fakeRecord.setGroup(group);
		fakeRecord.setAttribute(attribute);
		fakeRecord.setP(StandardColumn.P.specialValue());
		fakeRecord.setH(StandardColumn.H.specialValue());
		fakeRecord.setT(StandardColumn.T.specialValue());
		fakeRecord.setU(StandardColumn.U.specialValue());
		fakeRecord.setTd(StandardColumn.Td.specialValue());
		fakeRecord.setDd(StandardColumn.dd.specialValue());
		fakeRecord.setFf(StandardColumn.ff.specialValue());
		return fakeRecord;
	}
	
	private void addStatisticRecord(List<StandardData> source, StandardGroup group){
		Map<StandardAttribute, Double> pStat = calculateStatisticForOneColumn(
				source.stream().map(e -> e.getP()).collect(Collectors.toList()), StandardColumn.P);
		Map<StandardAttribute, Double> hStat = calculateStatisticForOneColumn(
				source.stream().map(e -> e.getH()).collect(Collectors.toList()), StandardColumn.H);
		Map<StandardAttribute, Double> tStat = calculateStatisticForOneColumn(
				source.stream().map(e -> e.getT()).collect(Collectors.toList()), StandardColumn.T);
		Map<StandardAttribute, Double> uStat = calculateStatisticForOneColumn(
				source.stream().map(e -> e.getU()).collect(Collectors.toList()), StandardColumn.U);
		Map<StandardAttribute, Double> tdStat = calculateStatisticForOneColumn(
				source.stream().map(e -> e.getTd()).collect(Collectors.toList()), StandardColumn.Td);
//		Map<Attribute, Double> ddStat = getStatisticForOneColumn(
//				source.stream().map(e -> e.getDd()).collect(Collectors.toList()), Column.dd);
		Map<StandardAttribute, Double> ffStat = calculateStatisticForOneColumn(
				source.stream().map(e -> e.getFf()).collect(Collectors.toList()), StandardColumn.ff);
		
		String stno = source.get(0).getStno();
		Timestamp time = source.get(0).getTime();
		
		for(StandardAttribute attr : StandardAttribute.values()) {
			if(attr == StandardAttribute.SOURCE) {
				continue;
			}
			StandardData data = new StandardData();
			data.setStno(stno);
			data.setTime(time);
			data.setLevel(group.getLevel());
			data.setGroup(group);
			data.setAttribute(attr);
			data.setP(pStat.get(attr));
			data.setH(hStat.get(attr));
			data.setT(tStat.get(attr));
			data.setU(uStat.get(attr));
			data.setTd(tdStat.get(attr));
//			data.setDd(ddStat.get(attr));
			data.setFf(ffStat.get(attr));
			source.add(data);
		}
	}
	
	
	private Map<StandardAttribute, Double> calculateStatisticForOneColumn(List<Double> list, StandardColumn column) {
		Map<StandardAttribute, Double> attrMap = new HashMap<>();
		double sValue = column.specialValue();
		DoubleSummaryStatistics stat =  list.stream().filter(e -> e.doubleValue()!=column.specialValue())
										.mapToDouble(x->x).summaryStatistics();
		double count = stat.getCount(); // count (no)
		attrMap.put(StandardAttribute.NO, count);
		if(count == 0.0) {
			attrMap.put(StandardAttribute.SUM, sValue);
			attrMap.put(StandardAttribute.MEAN, sValue);
			attrMap.put(StandardAttribute.MAX, sValue);
			attrMap.put(StandardAttribute.MAX_DATE, sValue);
			attrMap.put(StandardAttribute.MIN, sValue);
			attrMap.put(StandardAttribute.MIN_DATE, sValue);
		} else {
			attrMap.put(StandardAttribute.SUM, new BigDecimal(stat.getSum()).setScale(1, RoundingMode.HALF_UP).doubleValue());
			attrMap.put(StandardAttribute.MEAN, new BigDecimal(stat.getAverage()).setScale(1, RoundingMode.HALF_UP).doubleValue());
			attrMap.put(StandardAttribute.MAX, stat.getMax());
			attrMap.put(StandardAttribute.MAX_DATE, Integer.valueOf(list.indexOf(stat.getMax())).doubleValue()+1);
			attrMap.put(StandardAttribute.MIN, stat.getMin());
			attrMap.put(StandardAttribute.MIN_DATE, Integer.valueOf(list.indexOf(stat.getMin())).doubleValue()+1);
		}
		
		return attrMap;
	}
	
	
	private StandardData turnToPrintedValue(StandardData oData) {
		String printedP = replaceEmptyAndSpecialValueThenTrancate(oData.getP(), StandardColumn.P, oData.getAttribute());
		String printedH = replaceEmptyAndSpecialValueThenTrancate(oData.getH(), StandardColumn.H, oData.getAttribute());
		String printedT = replaceEmptyAndSpecialValueThenTrancate(oData.getT(), StandardColumn.T, oData.getAttribute());							
		String printedU = replaceEmptyAndSpecialValueThenTrancate(oData.getU(), StandardColumn.U, oData.getAttribute());
		String printedTd = replaceEmptyAndSpecialValueThenTrancate(oData.getTd(), StandardColumn.Td, oData.getAttribute());
		String printedDd = replaceEmptyAndSpecialValueThenTrancate(oData.getDd(), StandardColumn.dd, oData.getAttribute());
		String printedFf = replaceEmptyAndSpecialValueThenTrancate(oData.getFf(), StandardColumn.ff, oData.getAttribute());
		
		oData.setPrintedP(printedP);
		oData.setPrintedH(printedH);
		oData.setPrintedT(printedT);
		oData.setPrintedU(printedU);
		oData.setPrintedTd(printedTd);
		oData.setPrintedDd(printedDd);
		oData.setPrintedFf(printedFf);
		
		return oData;
	}
	
	private String replaceEmptyAndSpecialValueThenTrancate(double oValue, StandardColumn column, StandardAttribute attr) {
		if(attr == StandardAttribute.SOURCE) {
			if(column.digitType() == DigitType.INTEGER) {
				return oValue == column.specialValue()? "-" : String.valueOf((int)oValue);
			} else {
				return oValue == column.specialValue()? "-" : String.valueOf(oValue);
			}
		} else {
			if(column == StandardColumn.dd) {
				return  " ";
			}
			if(attr == StandardAttribute.NO || attr == StandardAttribute.MAX_DATE || attr == StandardAttribute.MIN_DATE) {
				return oValue == column.specialValue()? "-" : String.valueOf((int)oValue);
			} else {
				if(column.digitType() == DigitType.INTEGER) {
					return oValue == column.specialValue()? "-" : String.valueOf((int)oValue);
				} else {
					return oValue == column.specialValue()? "-" : String.valueOf(oValue);
				}
			}
		}
	}
}
