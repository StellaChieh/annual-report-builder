package cwb.cmt.surface.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import cwb.cmt.surface.dao.StationDao;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.service.CreateCsvForStn;
import cwb.cmt.surface.service.CreateTableImage;
import cwb.cmt.surface.utils.Numbers;
import cwb.cmt.surface.utils.PageEncode;
import cwb.cmt.surface.utils.ParseStnXml;

@Service("processStn")
public class ProcessStn extends Process{
	
 	@Resource(name="stationDaoImpl")
	StationDao dao;
	
	@Resource(name="createTableImageForStn")
	CreateTableImage drawer;
	
	@Resource(name="parseStnXml")
	ParseStnXml parseStnXml;
	
	//create csv
	@Resource(name="createCsvForStn")
	CreateCsvForStn csvWriter;
	
	private static final int STN_COUNT_PER_PAGE = Numbers.STATIONS_PER_PAGE_STNS.getNumber();
	List<List<Station>> stnGroups = new ArrayList<>();
	
	public void run() throws IOException{
		//get stnList
		prepareConfig_Stns();
		drawer.setPath(outputTmpPdfPath);
		
		// divide page: if station count >29, divide it into the next page
		if(stnList.size() <= STN_COUNT_PER_PAGE){
			stnGroups.add(stnList);
		} else {
			int groupCount = (int)Math.ceil(stnList.size()/(double)STN_COUNT_PER_PAGE);
			for(int i=0; i<groupCount; i++){
				if (i == groupCount-1){
					stnGroups.add(stnList.subList(i*STN_COUNT_PER_PAGE, stnList.size()));
				} else {
					stnGroups.add(stnList.subList(i*STN_COUNT_PER_PAGE, (i+1)*STN_COUNT_PER_PAGE));
				}
			}
		}
		//pdf
		for(int i=0; i<stnGroups.size(); i++){
			//set Filename
			drawer.setFilename("1_1" + "_" + "氣象站一覽表"  + "_" + "List of Surface Synoptic Stations");
			boolean isFirstPage = (i == 0) ? true : false;
			boolean isLastPage = (i == stnGroups.size() - 1) ? true : false;
			if (isLastPage) {
				drawer.createTableImage(isFirstPage, isLastPage, stnGroups.get(i));
			}else {
				drawer.createTableImage(isFirstPage, isLastPage, stnGroups.get(i));
			}
			System.out.println("...draw station List completed...");
		} 
	}
	
	
	public void runCsv(String outputCsvFolder) throws IOException{
		//get stnList
		prepareConfig_Stns();
		csvWriter.createCsv(stnList, outputCsvFolder);
		System.out.println("...draw station List csv completed...");
	}
}
