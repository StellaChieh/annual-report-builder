package cwb.cmt.surface.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cwb.cmt.surface.dao.StationDao;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.utils.ParseStnXml;


@Service("prepareStation")
public class PrepareStation {

	@Resource(name="stationDaoImpl")
	private StationDao dao;
	
	@Resource(name="parseStnXml")
	private ParseStnXml parseStnXml;
	
    @Resource(name="year")
    protected int year;
	
	public List<Station> getStation() {
		// 1. get station from xml
		List<Station> stnsXml = null;
		
		try {
			stnsXml = parseStnXml.getStns();
//			System.out.println("#prepareStation_parseStnXml# " + stnsXml);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		// 2. call dao
		// param: parsed station objects; List<Station>
		List<Station> stnsDao = dao.selectStation(stnsXml);
				
		// 3. merge stnsXml and stnsDao
		List<Station> StnsMergeList = new ArrayList<>();
		
		//filter station not exist in #{year}
		for(Station xml: stnsXml) {
			for(Station dao: stnsDao) {
				if(xml.equals(dao) && "".equals(dao.getStnEndTime())){
					Station s = dao;
					s.setStnCName(xml.getStnCName());
					s.setStnEndTime(dao.getStnEndTime());
					StnsMergeList.add(s);
				}
				else {
					if((xml.equals(dao) && dao.getStnEndTime()==null)
							||(xml.equals(dao) && Integer.valueOf(dao.getStnEndTime().substring(0, 4))>=year))
					{
						Station s = dao;
						s.setStnCName(xml.getStnCName());
						s.setStnEndTime(dao.getStnEndTime());
						StnsMergeList.add(s);
					}
				}
			}
		}
		return StnsMergeList;
	}
	
	
	public List<Station> getOtherStation() {
		Map<String, Integer> params = new HashMap<>();
		params.put("year", year);
		List<Station> otherStnsList = dao.selectOtherStns(params);
		return otherStnsList;
		
	}
}
