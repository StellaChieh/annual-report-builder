package cwb.cmt.surface.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import cwb.cmt.surface.model.CliSum;

@Repository("cliSumDaoImpl")
public class CliSumDaoImpl implements CliSumDao {
	
	@Resource(name="sqlSessionTemplate")
	SqlSessionTemplate session;
	
	
	@Override
	public List<CliSum> selectMonthData(Map<String, Object> monthData) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectMonthData", monthData);
	}
	
	@Override
	public List<CliSum> selectHourAvg(Map<String, Object> HourAvg) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectHourAvg", HourAvg);
	}
	
	@Override
	public List<CliSum> selectDayAvg(Map<String, Object> DayAvg) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectDayAvg", DayAvg);
	}
	
	@Override
	public List<CliSum> selectHourSum(Map<String, Object> HourSum) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectPrecpHourSum", HourSum);
	}
	
	@Override
	public List<CliSum> selectPrecpDays_10mm(Map<String, Object> PrecpDays_10mm) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectPrecpDays_10mm", PrecpDays_10mm);
	}
	
	@Override
	public List<CliSum> selectPrecpDays_1mm(Map<String, Object> PrecpDays_1mm) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectPrecpDays_1mm", PrecpDays_1mm);
	}
	
	@Override
	public List<CliSum> selectPrecpDays_01mm(Map<String, Object> PrecpDays_01mm) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectPrecpDays_01mm", PrecpDays_01mm);
	}
	
	@Override
	public List<CliSum> selectWeatherCondition(Map<String, Object> WeatherCondition) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectWeatherCondition", WeatherCondition);
	}
	
	@Override
	public List<CliSum> selectTxMinAbs_LE0(Map<String, Object> TxMinAbs_LE0) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectTxMinAbs_LE0", TxMinAbs_LE0);
	}
	
	@Override
	public List<CliSum> selectTxMinAbs_LE10(Map<String, Object> TxMinAbs_LE10) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectTxMinAbs_LE10", TxMinAbs_LE10);
	}
	
	@Override
	public List<CliSum> selectTxMinAbs_GE20(Map<String, Object> TxMinAbs_GE20) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectTxMinAbs_GE20", TxMinAbs_GE20);
	}
	
	public List<CliSum> selectTxMaxAbs_GE25(Map<String, Object> TxMaxAbs_GE25) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectTxMaxAbs_GE25", TxMaxAbs_GE25);
	}
	
	@Override
	public List<CliSum> selectTxMaxAbs_GE30(Map<String, Object> TxMaxAbs_GE30) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectTxMaxAbs_GE30", TxMaxAbs_GE30);
	}
	
	@Override
	public List<CliSum> selectTxMaxAbs_GE35(Map<String, Object> TxMaxAbs_GE35) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectTxMaxAbs_GE35", TxMaxAbs_GE35);
	}
	
	@Override
	public List<CliSum> selectPrecpDay(Map<String, Object> PrecpDay) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectPrecpDay", PrecpDay);
	}
	
	@Override
	public List<CliSum> selectHazeDay(Map<String, Object> hazeDay) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectHazeDay", hazeDay);
	}
	
	@Override
	public List<CliSum> selectHailDay(Map<String, Object> hailDay) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectHailDay", hailDay);
	}
	
	@Override
	public List<CliSum> selectDewDay(Map<String, Object> dewDay) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectDewDay", dewDay);
	}
	
	@Override
	public List<CliSum> selectSnowDate(Map<String, Object> snowDate) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectSnowDate", snowDate);
	}
	
	@Override
	public List<CliSum> selectFrostDate(Map<String, Object> frostDate) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.CliSumDao.selectFrostDate", frostDate);
	}
	
}
