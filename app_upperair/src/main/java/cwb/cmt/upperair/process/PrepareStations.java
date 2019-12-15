package cwb.cmt.upperair.process;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cwb.cmt.upperair.dao.StationDao;
import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.ParseStnXml;

@Service
public class PrepareStations {

	@Autowired
	ParseStnXml parseStnXml;
	
	@Autowired
	StationDao stationDao;
	
	String stnXmlFilePath;
	
	private List<Station> xmlList;
	
	private List<Station> dbList;
	
	private List<Station> resultList;
	
	private static final Logger logger = LogManager.getLogger();
	
	public void init(String xmlAbsolutePath) {
		this.stnXmlFilePath = xmlAbsolutePath;
	}
	
	public List<Station> getStns() throws IOException {
		if (resultList == null) {
			if(xmlList == null) {
				xmlList = parseStnXml.getStns(stnXmlFilePath);
			}
			if(dbList == null) {
				logger.info("Query stations.");
				dbList = stationDao.selectStation(xmlList);
			}
			
			Map<String, Station> dbMap = dbList.stream().collect(
					Collectors.toMap(Station::getStno, station -> station));
			xmlList.forEach( e -> {
				Station dbItem = dbMap.get(e.getStno());
				e.setLatitude(dbItem.getLatitude());
				e.setLongitude(dbItem.getLongitude());
				e.setStnCName(dbItem.getStnCName());
				e.setStnEName(dbItem.getStnEName());
				e.sethBarometer(dbItem.gethBarometer());
			});
			resultList = Collections.unmodifiableList(xmlList);
			logger.info("Stations: " + resultList);
		}	
		return resultList;
	}
}
