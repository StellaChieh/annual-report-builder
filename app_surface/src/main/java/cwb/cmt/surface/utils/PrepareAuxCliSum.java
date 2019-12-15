package cwb.cmt.surface.utils;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cwb.cmt.surface.dao.AuxCliSumDao;
import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.Station;


@Service("prepareAuxCliSum")
public class PrepareAuxCliSum {

	@Resource(name="auxclisumDaoImpl")
	private AuxCliSumDao auxclisumdao;
	
	@Resource(name="parseStnXml")
	private ParseStnXml parseStnXml;
	
    //Time: year
    @Resource(name="year")
    protected int year;
    
	//Time: month
    //config setting:cmt.month=2
    @Resource(name="month")
    protected int month;

    
    //1.4.1 Radiation and SunShine
	public List<AuxCliSum> getRadiation(List<Station> stnList){
		List<AuxCliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "radiation");
		List<AuxCliSum> radDao = auxclisumdao.selectRadSun(params);
		List<AuxCliSum> AuxCliSumMergeList = getMergeList(stnsXml,radDao);
		return AuxCliSumMergeList;
	}
	
	//1.4.2 Global Solar Radiation
	public List<AuxCliSum> getGlobalRad(List<Station> stnList){
		List<AuxCliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "globalRad");
		List<AuxCliSum> radDao = auxclisumdao.selectGlobalRad(params);
		List<AuxCliSum> AuxCliSumMergeList = getMergeList(stnsXml,radDao);
		return AuxCliSumMergeList;
	}
	
	//1.4.3  Global Solar Radi Max.
	public List<AuxCliSum> getRadHrMax(List<Station> stnList){
		List<AuxCliSum> stnsXml = getStnXml(stnList);
		Map<String, Object> params = new HashMap<>();
		params = getParams(stnsXml, "radHrMax");
		List<AuxCliSum> radDao = auxclisumdao.selectRadHrMax(params);
		List<AuxCliSum> AuxCliSumMergeList = getMergeList(stnsXml,radDao);
		return AuxCliSumMergeList;
	}


	
	// 1. get station from xml and filter station
	public List<AuxCliSum> getStnXml(List<Station> stnList){
	    List<AuxCliSum> stnsXml = null;
	    List<AuxCliSum> stnsXml2 = new ArrayList<>();
		try {
			stnsXml = parseStnXml.getStnsAuxCliSum();
			for(Station s: stnList) {
				for(AuxCliSum a: stnsXml) {
					if(s.getStno().equals(a.getStno())) {
						a.setStnEndTime(s.getStnEndTime());
						stnsXml2.add(a);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stnsXml2;
	}
	
	// 2. call dao;param: parsed Radiation objects
	public Map<String, Object> getParams(List<AuxCliSum> stnsXml, String pattern){
		Map<String, Object> params = new HashMap<>();
		if(pattern.equals("globalRad")||pattern.equals("radHrMax") ) {
			params.put("table", (year==Year.now().getValue())?"cwbmn":"his_cwbmn");
		}
		else if(pattern.equals("radiation")) {
			params.put("table", (year==Year.now().getValue())?"cwbdy":"his_cwbdy");
		}
		params.put("year", year);
		params.put("stnList", stnsXml);
		return params;
	}
	
	// 3. merge stnsXml and AuxCliSumDao; put dao value into xml list
	public List<AuxCliSum> getMergeList(List<AuxCliSum> stnsXml, List<AuxCliSum> radDao){
		List<AuxCliSum> AuxCliSumMergeList = new ArrayList<>();
		//add the object month <= month(from user)
		for(AuxCliSum xml: stnsXml) {
			for(AuxCliSum dao: radDao) {
				if(xml.equals(dao) && dao.getObsTime().getMonthValue()<=month) {
					AuxCliSum s = dao;
					s.setStnCName(xml.getStnCName());
					s.setStnEndTime(xml.getStnEndTime());
					AuxCliSumMergeList.add(s);
				}
			}
		}
		return AuxCliSumMergeList;
	}
}
