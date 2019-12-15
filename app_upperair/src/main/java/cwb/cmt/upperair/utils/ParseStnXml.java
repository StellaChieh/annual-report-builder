package cwb.cmt.upperair.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.model.StationList;

@Service
public class ParseStnXml {

	@Autowired
	private Unmarshaller castorMarshaller;
	
	private static final Logger logger = LogManager.getLogger();
	
	public List<Station> getStns(String stnXmlPath) throws IOException {
		StationList list = null;
		try (FileInputStream is = new FileInputStream(stnXmlPath)){
			logger.info("Parse " + stnXmlPath + ".");
			list = (StationList)castorMarshaller.unmarshal(new StreamSource(is));
			logger.debug("Parsed stations: " + list);
		} 
		return list.getStns();
	}

}
