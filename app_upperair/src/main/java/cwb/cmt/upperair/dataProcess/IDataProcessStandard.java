package cwb.cmt.upperair.dataProcess;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.StandardBookTable;

public interface IDataProcessStandard {

	Map<StandardBookTable, Map<Integer, List<String>>> getPdfMonthlyData(Station station, YearMonth yearMonth, String hour);
	
	List<List<Object>> getCsvNlhMonthlyData(Station station, YearMonth yearMonth, String hour);
	List<List<Object>> getCsvStandardMonthlyData(Station station, YearMonth yearMonth, String hour);
	List<List<Object>> getCsvTropMonthlyData(Station station, YearMonth yearMonth, String hour);
	List<List<Object>> getCsvLastMonthlyData(Station station, YearMonth yearMonth, String hour);
	
}
