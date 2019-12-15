package cwb.cmt.surface.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cwb.cmt.surface.dao.StationDao;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.service.CreateCsvForListOfWeatherStns;
import cwb.cmt.surface.service.CreateTableImage;
import cwb.cmt.surface.utils.Numbers;
import cwb.cmt.surface.utils.PageEncode;
import cwb.cmt.surface.utils.ParseStnXml;

@Service("processListOfWeatherStns")
public class ProcessListOfWeatherStns extends Process {

	private static final String PREFIX = String.valueOf(PageEncode.OTHERSTATIONS.getFilename());
	private static final String PdfFilename = "2_2"  + "_" + "自動觀測站一覽表" + "_" + "List of Automatic Weather Stations";
	
	@Resource(name="createTableImageForListOfWeatherStns")
	CreateTableImage drawer;	

	@Resource(name="processAbstract")
	ProcessAbstract processAbstract;
	
	@Resource(name="createCsvForListOfWeatherStns")
	CreateCsvForListOfWeatherStns csvWriter;
	
	private static final int OTHERSTN_COUNT_PER_PAGE = Numbers.OTHERSTATIONS_PER_PAGE_STNS.getNumber();
	List<List<Station>> stnGroups = new ArrayList<>();
	
	
	public void run() throws IOException{
		prepareConfig_otherStns();
		drawer.setPath(outputTmpPdfPath);
		
		if(otherStnsList.size() <= OTHERSTN_COUNT_PER_PAGE){
			stnGroups.add(otherStnsList);
		} else {
			int groupCount = (int)Math.ceil(otherStnsList.size()/(double)OTHERSTN_COUNT_PER_PAGE);
			System.out.println("groupCount>> "+ groupCount);
			for(int i=0; i<groupCount; i++){
				if (i == groupCount-1){
					stnGroups.add(otherStnsList.subList(i*OTHERSTN_COUNT_PER_PAGE, otherStnsList.size()));
				} else {
					stnGroups.add(otherStnsList.subList(i*OTHERSTN_COUNT_PER_PAGE, (i+1)*OTHERSTN_COUNT_PER_PAGE));
				}
			}
		}
		
		for(int i=0; i<stnGroups.size(); i++){
			//set Filename
			drawer.setFilename(PdfFilename + "#" + (i+1));
			drawer.createTableImage(stnGroups.get(i), processAbstract.getCurrentPageNumber());
			processAbstract.increasePageNumber();
			System.out.println("...draw station completed...");
		} 
	}


	public void runListOfWeatherStnsCsv(String outputCsvFolder) throws IOException{
		prepareConfig_otherStns();
		csvWriter.createCsv(otherStnsList, outputCsvFolder);
		System.out.println("...draw List of Weather stations completed...");
	}
}
