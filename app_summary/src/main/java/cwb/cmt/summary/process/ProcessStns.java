package cwb.cmt.summary.process;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cwb.cmt.summary.createTableImage.CreateTableImageForStns;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.utils.Numbers;
import cwb.cmt.summary.utils.PageEncode;

@Service
public class ProcessStns extends Process {

	private static Logger log = Logger.getLogger(ProcessStns.class);
	private static final String FILENAME_PREFIX = String.valueOf(PageEncode.STATIONS.getEncode());
	private static final String FILENAME_POSTFIX = PageEncode.STATIONS.getFilename();

	@Autowired
	CreateTableImageForStns createTableImageForStns;
	
	private static final int STN_COUNT_PER_PAGE = Numbers.STATIONS_PER_PAGE_STNS.getNumber();
	
	public void run() {
		log.info("Start to draw stations...");
		
		createTableImageForStns.setPath(outputTmpPdfPath);
		
		// if station count >26, divide it into more pages
		List<List<Station>> stnGroups = Lists.partition(stnList, STN_COUNT_PER_PAGE);
		
		// draw stations
		for(int i=0; i<stnGroups.size(); i++){
			log.info("Draw stations page " + (i+1));
			String fileName = FILENAME_PREFIX + "_" + String.format("%02d", i+1) + "_" + FILENAME_POSTFIX;   
			createTableImageForStns.setFilename(fileName);
			createTableImageForStns.createTableImage(0, stnGroups.get(i));
		} 
		log.info("Draw stations completed...");
	}

}
