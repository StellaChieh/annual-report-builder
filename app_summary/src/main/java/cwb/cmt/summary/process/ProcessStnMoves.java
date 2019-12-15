package cwb.cmt.summary.process;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves1971_1980_1;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves1971_1980_2;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves1981_1990_1;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves1981_1990_2;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves1981_1990_3;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves1981_1990_4;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves1991_2000_1;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves1991_2000_2;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves2001_2010_1;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnMoves2001_2010_2;
import cwb.cmt.summary.dao.DbInteract;
import cwb.cmt.summary.model.StnMoves;
import cwb.cmt.summary.utils.Numbers;
import cwb.cmt.summary.utils.PageEncode;

@Service
public class ProcessStnMoves extends Process {

	private static final String PREFIX = String.valueOf(PageEncode.STATION_MOVES.getEncode());
	private static final String POSTFIX = PageEncode.STATION_MOVES.getFilename();
	private static Logger log = Logger.getLogger(ProcessStnMoves.class);
			
	@Autowired
	CreateTableImageForStnMoves1971_1980_1 creator1971_1;
	
	@Autowired
	CreateTableImageForStnMoves1971_1980_2 creator1971_2;
	
	@Autowired
	CreateTableImageForStnMoves1981_1990_1 creator1981_1;
	
	@Autowired
	CreateTableImageForStnMoves1981_1990_2 creator1981_2;
	
	@Autowired
	CreateTableImageForStnMoves1981_1990_3 creator1981_3;
	
	@Autowired
	CreateTableImageForStnMoves1981_1990_4 creator1981_4;
	
	@Autowired
	CreateTableImageForStnMoves1991_2000_1 creator1991_1;
	
	@Autowired
	CreateTableImageForStnMoves1991_2000_2 creator1991_2;
	
	@Autowired
	CreateTableImageForStnMoves2001_2010_1 creator2001_1;
	
	@Autowired
	CreateTableImageForStnMoves2001_2010_2 creator2001_2;
	
	@Autowired
	CreateTableImageForStnMoves createTableImageForStnMoves;
	
	@Autowired
	@Qualifier(value="dbInteraction")
	private DbInteract dbInteract;
	
	private int page=0;
	
	
	public void run() {

		log.info("Start to draw station moves...");
		
		drawDefaultStnMoves();
		
		List<StnMoves> list = dbInteract.queryStnMoves();
		
		for(int i=2011; i<=thisStartYear; i=i+10) {
			
			log.info("Draw " + i + "-" + String.valueOf(i+9) + "station moves.");
			
			// divide into each ten years
			final int j = i;
			List<StnMoves> result = list.stream().filter(f->f.getBeginTime().toLocalDate().getYear() >= j 
															&& f.getEndTime().toLocalDate().getYear() <= j+9)
												.collect(Collectors.toList());
			
			// sort by latitude, then beginTime, then endTime
			result.sort((o1, o2) -> {
				int cmp = getLatitudeNumber(o2.getLatitude()) - getLatitudeNumber(o1.getLatitude()); // higher latitude has higher precedence
				if (cmp == 0) {
					cmp = o1.getBeginTime().compareTo(o2.getBeginTime());
				} 
				if (cmp == 0) {
					cmp = o1.getEndTime().compareTo(o2.getEndTime());
				}
				return cmp;
			});
			
			// distribute into pages and draw table
			int rowsPerPage = Numbers.ROWS_PER_PAGE_STNMOVES.getNumber();
			int group = (int)Math.ceil(result.size() / (double)rowsPerPage);
			for(int k=0; k<group; k++) {
				createTableImageForStnMoves.setPath(outputTmpPdfPath);
				createTableImageForStnMoves.setFilename(getFilename(i));
				boolean isFirstPage = (k == 0) ? true : false;
				boolean isLastPage = (k == group - 1) ? true : false;
				if(isLastPage) { 
					createTableImageForStnMoves.createTableImage(0, isFirstPage, isLastPage, i, result.subList(k*rowsPerPage, result.size()));
				} else {
					createTableImageForStnMoves.createTableImage(0, isFirstPage, isLastPage, i, result.subList(k*rowsPerPage, (k+1)*rowsPerPage));
				}
			}
		}
		
		log.info("Draw station moves completed...");
	}
	
		
	private void drawDefaultStnMoves() {
		int targetYear = 0;
		
		// draw 1991-2000 station moves
		targetYear = 1971;
		log.info("Draw " + targetYear + "-" + String.valueOf(targetYear+9) + "station moves.");
		if(thisStartYear>=targetYear){
			creator1971_1.setPath(outputTmpPdfPath);
			creator1971_1.setFilename(getFilename(targetYear));
			creator1971_1.createTableImage(0, (Object[]) null);
			creator1971_2.setPath(outputTmpPdfPath);
			creator1971_2.setFilename(getFilename(targetYear));
			creator1971_2.createTableImage(0, (Object[]) null);
		}
				
		// draw 1981-1990 station moves
		targetYear = 1981;
		log.info("Draw " + targetYear + "-" + String.valueOf(targetYear+9) + "station moves.");
		if(thisStartYear>=targetYear){
			creator1981_1.setPath(outputTmpPdfPath);
			creator1981_1.setFilename(getFilename(targetYear));
			creator1981_1.createTableImage(0, (Object[]) null);
			creator1981_2.setPath(outputTmpPdfPath);
			creator1981_2.setFilename(getFilename(targetYear));
			creator1981_2.createTableImage(0, (Object[]) null);
			creator1981_3.setPath(outputTmpPdfPath);
			creator1981_3.setFilename(getFilename(targetYear));
			creator1981_3.createTableImage(0, (Object[]) null);
			creator1981_4.setPath(outputTmpPdfPath);
			creator1981_4.setFilename(getFilename(targetYear));
			creator1981_4.createTableImage(0, (Object[]) null);
		}
		
		// draw 1991-2000 station moves
		targetYear = 1991;
		log.info("Draw " + targetYear + "-" + String.valueOf(targetYear+9) + "station moves.");
		if(thisStartYear>=targetYear){
			creator1991_1.setPath(outputTmpPdfPath);
			creator1991_1.setFilename(getFilename(targetYear));
			creator1991_1.createTableImage(0, (Object[]) null);
			creator1991_2.setPath(outputTmpPdfPath);
			creator1991_2.setFilename(getFilename(targetYear));
			creator1991_2.createTableImage(0, (Object[]) null);
		}
				

		
		// draw 2001-2010 station moves
		targetYear = 2001;
		log.info("Draw " + targetYear + "-" + String.valueOf(targetYear+9) + "station moves.");
		if(thisStartYear>=targetYear){
			creator2001_1.setPath(outputTmpPdfPath);
			creator2001_1.setFilename(getFilename(targetYear));
			creator2001_1.createTableImage(0, (Object[]) null);
			creator2001_2.setPath(outputTmpPdfPath);
			creator2001_2.setFilename(getFilename(targetYear));
			creator2001_2.createTableImage(0, (Object[]) null);
		} 
		
	}
	
	private String getFilename(int year){
		String filename = PREFIX + "_" + String.valueOf(page) + "_" + POSTFIX + "-" + String.valueOf(year);
		page++;
		return filename;
	}
	
	// latitude: 25°09' 56〞N
	// remove the unit and return number
	private int getLatitudeNumber(String latitude) {
		String num = latitude.substring(0, 2) + latitude.substring(3,5) + latitude.substring(7,9);
		return Integer.parseInt(num);
	}

}
