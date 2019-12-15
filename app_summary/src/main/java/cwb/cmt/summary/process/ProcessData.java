package cwb.cmt.summary.process;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.composeData.MakeDataService;
import cwb.cmt.summary.composeData.MakeDataServiceManager;
import cwb.cmt.summary.dao.DbInteract;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.service.PrepareCes;
import cwb.cmt.summary.service.PrepareStations;
import cwb.cmt.summary.utils.SummaryColumnType;

@Service
public class ProcessData {

	private static Logger log = Logger.getLogger(ProcessData.class);
	
	@Autowired
	private PrepareCes prepareCes;

	@Autowired
	private PrepareStations prepareStations;

	@Autowired
	@Qualifier(value="dbInteraction")
	private DbInteract dbInteract;
	
	// we inject this property in MakeDataServiceManager
	private MakeDataService makeData;
	
	public void run() {
		log.info("Clear summary table.");
		dbInteract.clearSummaryModelTable();
		
		log.info("ProcessData starts to create yearly data.");
		Map<SummaryColumnType, MakeDataService> map = MakeDataServiceManager.getMakeDataService();
		List<Station> stns = prepareStations.getStations();
		List<ClimaticElement> ces = prepareCes.getClimaticElements();
		ces.stream().forEach(ce -> {
			stns.stream().forEach(stn -> {
				log.info(ce.getId() + ":::" + stn.getStno() + ":::" + stn.getStnCName());
				makeData = map.get(SummaryColumnType.lookup(ce.getSummaryColumnType()));
				makeData.setClimaticElementNStation(ce, stn);
				makeData.insertIntoDb(makeData.createDatas());
			});
		});
	}


}
