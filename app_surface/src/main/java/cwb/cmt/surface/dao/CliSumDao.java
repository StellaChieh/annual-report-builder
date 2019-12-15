package cwb.cmt.surface.dao;

import java.util.List;
import java.util.Map;
import cwb.cmt.surface.model.CliSum;

public interface CliSumDao {

	List<CliSum> selectMonthData(Map<String, Object> monthData);
	List<CliSum> selectHourAvg(Map<String, Object> hourAvg);
	List<CliSum> selectDayAvg(Map<String, Object> DayAvg);
	List<CliSum> selectHourSum(Map<String, Object> HourSum);
	List<CliSum> selectPrecpDays_10mm(Map<String, Object> PrecpDays_10mm);
	List<CliSum> selectPrecpDays_1mm(Map<String, Object> PrecpDays_1mm);
	List<CliSum> selectPrecpDays_01mm(Map<String, Object> PrecpDays_01mm);
	List<CliSum> selectWeatherCondition(Map<String, Object> WeatherCondition);
	List<CliSum> selectTxMinAbs_LE0(Map<String, Object> TxMinAbs_LE10);
	List<CliSum> selectTxMinAbs_LE10(Map<String, Object> TxMinAbs_LE10);
	List<CliSum> selectTxMinAbs_GE20(Map<String, Object> TxMinAbs_LE20);
	List<CliSum> selectTxMaxAbs_GE25(Map<String, Object> TxMaxAbs_GE25);
	List<CliSum> selectTxMaxAbs_GE30(Map<String, Object> TxMaxAbs_GE30);
	List<CliSum> selectTxMaxAbs_GE35(Map<String, Object> TxMaxAbs_GE35);
	List<CliSum> selectPrecpDay(Map<String, Object> precpDay);
	List<CliSum> selectHazeDay(Map<String, Object> hazeDay);
	List<CliSum> selectHailDay(Map<String, Object> hailDay);
	List<CliSum> selectDewDay(Map<String, Object> hailDay);
	List<CliSum> selectSnowDate(Map<String, Object> snowDateDay);
	List<CliSum> selectFrostDate(Map<String, Object> frostDateDay);
	
}
