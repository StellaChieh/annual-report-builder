package cwb.cmt.summary.dao;

import java.util.List;
import java.util.Map;

import cwb.cmt.summary.model.MonthlyData;

public interface HisCwbmnDao {

	MonthlyData querySingleYearMonthlyData(Map<String, Object> params);
	
	List<MonthlyData> querySpanYearsData(Map<String, Object> params);
	
	MonthlyData queryEvapAMax(Map<String, Object> params); 
	
}