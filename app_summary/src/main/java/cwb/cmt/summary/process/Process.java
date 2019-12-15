package cwb.cmt.summary.process;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import cwb.cmt.summary.config.Config;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.service.PrepareCes;
import cwb.cmt.summary.service.PrepareStations;


public class Process {

	@Autowired
	private PrepareCes prepareCes;
	
	@Autowired
	private PrepareStations prepareStations;
	
	@Autowired
	public Config config;
	
	protected int thisStartYear;
	protected String outputTmpPdfPath;
	protected List<ClimaticElement> ceList;
	protected List<Station> stnList;
	
	
	@PostConstruct // called immediate after bean is initialized 
	protected void prepareConfig(){
		if(thisStartYear == 0) {
			thisStartYear = config.getYear();
		}
		if(outputTmpPdfPath == null) {
			outputTmpPdfPath = config.getOutputTmpPdfPath();
		}
		if(ceList == null) {
			ceList = prepareCes.getClimaticElements();
		}
		if(stnList == null) {
			stnList = prepareStations.getStations();
		}
	}
	
}