package cwb.cmt.summary.service;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.config.Config;
import cwb.cmt.summary.dao.DbInteract;
import cwb.cmt.summary.model.Stations;
import cwb.cmt.summary.model.Stations.Station;

@Service
public class PrepareStations {
	
	private String summaryStationXmlConfigPath;
	private DbInteract dbInteract;
	private List<Station> xmlStations;
	
	@Autowired
	public PrepareStations(Config config, @Qualifier(value="dbInteraction")DbInteract dbInteract) {
		this.summaryStationXmlConfigPath = config.getSummaryStationXmlConfigPath();
		this.dbInteract = dbInteract;
	}
		
	private List<Station> parseXmlStations(){
		if(xmlStations == null) {
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Stations.class);
				Unmarshaller m = jaxbContext.createUnmarshaller();
				xmlStations = ((Stations)(m.unmarshal(new File(summaryStationXmlConfigPath)))).getStation();
				xmlStations = Collections.unmodifiableList(xmlStations);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
		return xmlStations;
	}
	
	public List<Station> getStations(){
		List<Station> xmlStations = parseXmlStations();
		if(!xmlStations.isEmpty()) {
			List<Station> dbStations = dbInteract.queryStationData(xmlStations);
			return mergeStationData(xmlStations, dbStations);
		} else {
			return Collections.emptyList();
		}
	}
	
	// merge xml data with db data, and keep the order of xml list
	private List<Station> mergeStationData(List<Station> xmlStations, List<Station> dbStations){
		for(Station xmlS : xmlStations){
			for (Station dbS : dbStations){
				if (xmlS.getStno().equals(dbS.getStno())){
					xmlS.setStnEName(dbS.getStnEName());
					xmlS.setLongitude(dbS.getLongitude());
					xmlS.setLatitude(dbS.getLatitude());
					xmlS.sethBarometer(dbS.gethBarometer());
					xmlS.sethTherm(dbS.gethTherm());
					xmlS.sethRaingauge(dbS.gethRaingauge());
					xmlS.sethAnem(dbS.gethAnem());
					xmlS.setAltitude(dbS.getAltitude());
					xmlS.setStnBeginTime(dbS.getStnBeginTime());
					xmlS.setManObsNum(dbS.getManObsNum());
					break;
				}
			}
		}
		return Collections.unmodifiableList(xmlStations);
	}
	
}