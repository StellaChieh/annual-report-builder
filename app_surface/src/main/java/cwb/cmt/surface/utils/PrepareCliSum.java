package cwb.cmt.surface.utils;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cwb.cmt.surface.dao.CliSumDao;
import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.CliSum;
import cwb.cmt.surface.model.Station;

@Service("prepareCliSum")
public class PrepareCliSum {

	@Resource(name="cliSumDaoImpl")
	private  CliSumDao clisumdao;
	
	@Resource(name="parseStnXml")
	private ParseStnXml parseStnXml;
	
    //Time: year
    @Resource(name="year")
    protected int year;
    
	//Time: month
    //config setting:cmt.month=2
    @Resource(name="month")
    protected int month;
    
   
	public List<CliSum> getMonthData(List<Station> stnList) {
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "monthData");
		List<CliSum> clisumDao = clisumdao.selectMonthData(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getHourAvg(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "hourAvg");
		List<CliSum> clisumDao = clisumdao.selectHourAvg(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getDayAvg(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "dayAvg");
		List<CliSum> clisumDao = clisumdao.selectDayAvg(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getHourSUM(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "hourSum");
		List<CliSum> clisumDao = clisumdao.selectHourSum(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	
	public List<CliSum> getPrecpDays_10mm(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "precpDays_10mm");
		List<CliSum> clisumDao = clisumdao.selectPrecpDays_10mm(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getPrecpDays_1mm(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "precpDays_1mm");
		List<CliSum> clisumDao = clisumdao.selectPrecpDays_1mm(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getPrecpDays_01mm(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "precpDays_01mm");
		List<CliSum> clisumDao = clisumdao.selectPrecpDays_01mm(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getWeatherCondition(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "weatherCondition");
		List<CliSum> clisumDao = clisumdao.selectWeatherCondition(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getTxMinAbs_LE0(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "txMinAbs_LE0");
		List<CliSum> clisumDao = clisumdao.selectTxMinAbs_LE0(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getTxMinAbs_LE10(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "txMinAbs_LE10");
		List<CliSum> clisumDao = clisumdao.selectTxMinAbs_LE10(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getTxMinAbs_GE20(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "txMinAbs_GE20");
		List<CliSum> clisumDao = clisumdao.selectTxMinAbs_GE20(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getTxMaxAbs_GE25(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "txMaxAbs_GE25");
		List<CliSum> clisumDao = clisumdao.selectTxMaxAbs_GE25(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getTxMaxAbs_GE30(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "txMaxAbs_GE30");
		List<CliSum> clisumDao = clisumdao.selectTxMaxAbs_GE30(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getTxMaxAbs_GE35(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "txMaxAbs_GE35");
		List<CliSum> clisumDao = clisumdao.selectTxMaxAbs_GE35(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getPrecpDay(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "precpDay");
		List<CliSum> clisumDao = clisumdao.selectPrecpDay(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getHazeDay(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "hazeDay");
		List<CliSum> clisumDao = clisumdao.selectHazeDay(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getHailDay(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "hailDay");
		List<CliSum> clisumDao = clisumdao.selectHailDay(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getDewDay(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "dewDay");
		List<CliSum> clisumDao = clisumdao.selectDewDay(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getSnowDate(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "snowDate");
		List<CliSum> clisumDao = clisumdao.selectSnowDate(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	public List<CliSum> getFrostDate(List<Station> stnList){
		List<CliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "frostDate");
		List<CliSum> clisumDao = clisumdao.selectFrostDate(params);
		List<CliSum> CliSumMergeList = getMergeList(stnsXml,clisumDao);
		return CliSumMergeList;
	}
	
	// 1. get station from xml and filter station according to StnEndTime
	public List<CliSum> getStnXml(List<Station> stnList){
	    List<CliSum> stnsXml = null;
	    List<CliSum> stnsXml2 = new ArrayList<>();
		try {
			stnsXml = parseStnXml.getStnsCliSum();
			for(Station s: stnList) {
				for(CliSum a: stnsXml) {
					if(s.getStno().equals(a.getStno())) {
						a.setStnEndTime(s.getStnEndTime());
						a.setManObsNum(s.getManObsNum());
						stnsXml2.add(a);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stnsXml2;
	}
	
	// 2. call dao:params
	public Map<String, Object> getParams(List<CliSum> stnsXml, String pattern){
		Map<String, Object> params = new HashMap<>();
		if(pattern.equals("monthData")||pattern.equals("weatherCondition")) {
			params.put("table", (year==Year.now().getValue())?"cwbmn":"his_cwbmn");
		}
		else if(pattern.equals("hourAvg")||pattern.equals("hourSum")) {
			params.put("table", (year==Year.now().getValue())?"cwbhr":"his_cwbhr");
		}
		else if(pattern.equals("dayAvg")||pattern.equals("precpDays_10mm")||
				pattern.equals("precpDays_1mm")||pattern.equals("precpDays_01mm")||
				pattern.equals("txMinAbs_LE0")||pattern.equals("txMinAbs_LE10")||
				pattern.equals("txMinAbs_GE20")||pattern.equals("txMaxAbs_GE25")||
				pattern.equals("txMaxAbs_GE30")||pattern.equals("txMaxAbs_GE35")||
				pattern.equals("precpDay")||pattern.equals("hazeDay")||
				pattern.equals("hailDay")||pattern.equals("dewDay")||
				pattern.equals("snowDate")||pattern.equals("frostDate")) {
			params.put("table", (year==Year.now().getValue())?"cwbdy":"his_cwbdy");
		}
		params.put("year", year);
		params.put("stnList", stnsXml);
		return params;
	}
	
	// 3. merge stnsXml and CliSumDao; put dao value into xml list
	public List<CliSum> getMergeList(List<CliSum> stnsXml, List<CliSum> clisumDao){
		List<CliSum> CliSumMergeList = new ArrayList<>();
		//add the object month <= month(from user)
		for(CliSum xml: stnsXml) {
			for(CliSum dao: clisumDao) {
				if(xml.equals(dao) && dao.getObsTime().getMonthValue()<=month) {
					CliSum s = dao;
					s.setStnCName(xml.getStnCName());
					s.setStnEndTime(xml.getStnEndTime());
					s.setManObsNum(xml.getManObsNum());
					CliSumMergeList.add(s);
				}
			}
		}
		return CliSumMergeList;
	}
	
	
}
