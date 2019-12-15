package cwb.cmt.summary.process;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.createTableImage.CreateTableImageForPartOneContents;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.utils.Numbers;
import cwb.cmt.summary.utils.PageEncode;

@Service
public class ProcessPartOneContents extends Process {

	private static final String FILENAME_PREFIX = String.valueOf(PageEncode.CONTENTS_ONE.getEncode());
	private static final String FILENAME_POSTFIX = PageEncode.CONTENTS_ONE.getFilename();
	
	@Autowired
	private CreateTableImageForPartOneContents imageCreator;
	
	private static final int ROWS_PER_CONTENTS_PAGE = Numbers.ROWS_OF_PART_ONE_CONTENTS.getNumber();
	private static final int STN_COUNT_PER_PAGE = Numbers.STATIONS_PER_PAGE_PART_ONE.getNumber();
	private static Logger log = Logger.getLogger(ProcessPartOneContents.class);
	
	public void run(){
		
		log.info("Start to draw part one contents...");
		
		// 1. calculate a climatic element needs cePage pages
		int cePage = (int) Math.ceil(stnList.size()/(double)STN_COUNT_PER_PAGE); 
		
		List<List<ClimaticElement>> ceGroups = new ArrayList<>();
		// 2. divide climatic element into pages
		if(ceList.size()+1 <= ROWS_PER_CONTENTS_PAGE){  // +1 :wind rose
			ceGroups.add(ceList);
		} else {
			int groupCount = (int)( Math.ceil((ceList.size()+1)/(double)ROWS_PER_CONTENTS_PAGE));
			for(int i=0; i<groupCount; i++){
				if(i == groupCount-1){
					ceGroups.add(ceList.subList(i*ROWS_PER_CONTENTS_PAGE, ceList.size()));
				} else {
					ceGroups.add(ceList.subList(i*ROWS_PER_CONTENTS_PAGE, (i+1)*ROWS_PER_CONTENTS_PAGE));
				}
			}
		}
		
		int ceCount = 1;
		imageCreator.setPath(outputTmpPdfPath);
		// 3. draw contents
		for(int i=0; i<ceGroups.size(); i++){
			log.info("Draw part one contents page " + (i+1));
			String filename = FILENAME_PREFIX + "_" + String.format("%02d", i) + "_" + FILENAME_POSTFIX;   
			imageCreator.setFilename(filename);
			if(i == ceGroups.size()-1){
				// true: the last page has to put windRose
				imageCreator.createTableImage(0, ceGroups.get(i), ceCount, cePage, true); 
			} else {
				imageCreator.createTableImage(0, ceGroups.get(i), ceCount, cePage, false);
			}
			ceCount = (i+1) * ROWS_PER_CONTENTS_PAGE + 1;
		} 
		
		log.info("Draw part one contents completed...");
	}

}
