package cwb.cmt.upperair.dataProcess;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.SignificantBookTable;

public interface IDataProcessSignificant {
	
	Map<SignificantBookTable, Map<Integer, List<String>>> getPdfMonthlyData(Station station, YearMonth yearMonth, String hour);
	List<List<Object>> getCsvMonthlyData(Station station, YearMonth yearMonth, String hour);
	
}