package cwb.cmt.summary.dao;

import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.MonthlyData;

@Service
public class EvapAMaxDbInteraction extends DbInteraction {

	@Override
	protected Map<String, Object> makeQueryHisCwbmnParam(ClimaticElement ce, String stno, int beginYear, int endYear, Month month){
		HashMap<String, Object> params = new HashMap<>();
		params.put("stno", stno);
		params.put("beginYear", beginYear);
		params.put("endYear", endYear);
		params.put("month", month.getValue());
		return params;
	}
	
	@Override
	public MonthlyData queryHisCwbmnSingleYearMonthlyData(ClimaticElement ce, String stno, int year, Month month) {
		MonthlyData m = hisCwbmnDao.queryEvapAMax(makeQueryHisCwbmnParam(ce, stno, year, year, month));
		if(m == null) {
			m = new MonthlyData();
		}
		m.setBeginYear(year);
		m.setEndYear(year);
		m.setMonth(month);
		return m;
	}

	@Override
	public List<MonthlyData> queryHisCwbmnSpanYearsMonthlyData(ClimaticElement ce, String stno
												, int beginYear, int endYear, Month month) {
		MonthlyData m = hisCwbmnDao.queryEvapAMax(makeQueryHisCwbmnParam(ce, stno, beginYear, endYear, month));
		if(m == null) {
			m = new MonthlyData();
		}
		m.setBeginYear(beginYear);
		m.setEndYear(endYear);
		m.setMonth(month);
		return Collections.singletonList(m);
	}
}
