package cwb.cmt.summary.dao;

import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.model.InitYear;
import cwb.cmt.summary.model.MonthlyData;
import cwb.cmt.summary.model.StnMoves;
import cwb.cmt.summary.model.SummaryModel;

@Service
public class DbInteraction implements DbInteract {

	@Autowired
	protected HisCwbmnDao hisCwbmnDao;

	@Autowired
	private InitYearDao initYearDao;

	@Autowired
	private SummaryDao summaryDao;

	@Autowired
	private StnMovesDao stnMovesDao;

	@Autowired
	private StationDao stationDao;

	private static final String DB_STNO = "stno";
	private static final String DB_START_YEAR = "startyear";
	// sql server限制，一次只能儲存最多2100個參數(資料筆數*欄位數)
	// 該資料欄位數18個，一次最多可以存2100/18=116筆資料
	private static final int INSERT_BATCH_NUM = 110;

	private InitYear<Map<String, String>, Integer> initYearMap;
	private List<Station> stations;
	private List<StnMoves> stnMoves;

	@Override
	public List<Station> queryStationData(List<Station> xmlStations) {
		if (stations == null) {
			stations = stationDao.selectStation(xmlStations);
		}
		return stations;
	}

	@Override
	public List<StnMoves> queryStnMoves() {
		if (stnMoves == null) {
			stnMoves = stnMovesDao.selectStnMoves();
		}
		return stnMoves;
	}

	private String makeQueryString(ClimaticElement ce) {
		StringBuilder sb = new StringBuilder();
		if (ce.getNumOfLinesPerRow() >= 1) {
			sb.append(ce.getFirstData().getDbColumn()).append(" AS fv ");
		}
		if (ce.getNumOfLinesPerRow() >= 2) {
			sb.append(", ").append(ce.getTime().get().getDbColumn()).append(" AS t ");
		}
		if (ce.getNumOfLinesPerRow() >= 3) {
			sb.append(", ").append(ce.getSecondData().get().getDbColumn()).append(" AS sv ");
		}
		return sb.toString();
	}

	protected Map<String, Object> makeQueryHisCwbmnParam(ClimaticElement ce, String stno, int beginYear, int endYear,
			Month month) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("stno", stno);
		params.put("beginYear", beginYear);
		params.put("endYear", endYear);
		params.put("selectQuery", makeQueryString(ce));
		params.put("table", ce.getFirstData().getDbTable());
		params.put("month", month.getValue());
		return params;
	}

	@Override
	public MonthlyData queryHisCwbmnSingleYearMonthlyData(ClimaticElement ce, String stno, int year, Month month) {
		MonthlyData m = hisCwbmnDao.querySingleYearMonthlyData(makeQueryHisCwbmnParam(ce, stno, year, year, month));
		m.setBeginYear(year);
		m.setEndYear(year);
		m.setMonth(month);
		return m;
	}

	@Override
	public List<MonthlyData> queryHisCwbmnSpanYearsMonthlyData(ClimaticElement ce, String stno, int beginYear,
			int endYear, Month month) {
		return hisCwbmnDao.querySpanYearsData(makeQueryHisCwbmnParam(ce, stno, beginYear, endYear, month)).stream()
				.map(m -> {
					m.setBeginYear(beginYear);
					m.setEndYear(endYear);
					m.setMonth(month);
					return m;
				}).collect(Collectors.toList());
	}

	@Override
	public InitYear<Map<String, String>, Integer> queryInitYear() {
		if (initYearMap == null) {
			initYearMap = makeInitYearMap(initYearDao.selectInitYear());
		}
		return initYearMap;
	}

	private InitYear<Map<String, String>, Integer> makeInitYearMap(List<Map<String, Object>> list) {

		return list.stream().flatMap((Map<String, Object> e) -> {
			Map<Map<String, String>, Integer> term = new HashMap<>();
			String stno = (String)e.get(DB_STNO);
			Integer stnStartYear = (Integer)e.get(DB_START_YEAR);
			e.entrySet().forEach(map -> {
				if (!map.getKey().equals(DB_STNO) && !map.getKey().equals(DB_START_YEAR)) {
					if (((String)(map.getValue())).equalsIgnoreCase("null")) {
						term.put(Collections.singletonMap(stno, map.getKey()), stnStartYear);
					} else {
						term.put(Collections.singletonMap(stno, map.getKey()), Integer.parseInt((String)map.getValue()));
					}
				}
			});
			return Stream.of(term);
		}).collect(InitYear::new, InitYear::putAll, InitYear::putAll);
	}

	@Override
	public List<SummaryModel> queryForStnCliEle(String stno, String ceId) {
		Map<String, Object> params = new HashMap<>();
		params.put("stno", stno);
		params.put("climaticElement", ceId);
		return summaryDao.selectDbSummary(params);
	}

	@Override
	public SummaryModel queryForOneStnCliEle(int no, String stno, String ceId) {
		Map<String, Object> params = new HashMap<>();
		params.put("no", no);
		params.put("stno", stno);
		params.put("climaticElement", ceId);
		return summaryDao.selectOneDbSummary(params);
	}

	@Override
	public void insertSummaryModel(List<SummaryModel> list) {
		Lists.partition(list, INSERT_BATCH_NUM).stream().forEach(sublist -> summaryDao.insert(sublist));
	}

	@Override
	public void clearSummaryModelTable() {
		summaryDao.clear();
	}

}