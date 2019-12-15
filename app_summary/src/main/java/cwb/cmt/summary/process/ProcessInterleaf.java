package cwb.cmt.summary.process;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import cwb.cmt.summary.createTableImage.CreateTableImageForPartOneInterleaf;
import cwb.cmt.summary.createTableImage.CreateTableImageForPartTwoInterleaf;
import cwb.cmt.summary.createTableImage.CreateTableImageForStnInterleaf;
import cwb.cmt.summary.utils.PageEncode;

@Service
public class ProcessInterleaf extends Process {

	@Autowired
	CreateTableImageForPartOneInterleaf oneImgCreator;
	
	@Autowired
	CreateTableImageForPartTwoInterleaf twoImgCreator;
	
	@Autowired
	CreateTableImageForStnInterleaf stnImgCreator;
	
	private static final String FILENAME_PARTONE = PageEncode.INTERLEAF_ONE.getEncode()+ "_" +PageEncode.INTERLEAF_ONE.getFilename();
	private static final String FILENAME_PARTTWO = PageEncode.INTERLEAF_TWO.getEncode()+ "_" +PageEncode.INTERLEAF_TWO.getFilename();
	private static final String FILENAME_STN_INTERLEAF_PREFIX = String.valueOf(PageEncode.STN_INTERLEAF.getEncode());
	private static final String FILENAME_STN_INTERLEAF_POSTFIX = PageEncode.STN_INTERLEAF.getFilename();
	private static Logger log = Logger.getLogger(ProcessInterleaf.class);
	
	public void run() {
		
		log.info("Start to draw interleaf...");
		
		log.info("Draw part one interleaf");
		oneImgCreator.setOutputTmpPdfPath(outputTmpPdfPath);
		oneImgCreator.setFilename(FILENAME_PARTONE);
		
		log.info("Draw part two interleaf");
		twoImgCreator.setOutputTmpPdfPath(outputTmpPdfPath);
		twoImgCreator.setFilename(FILENAME_PARTTWO);
		
		stnImgCreator.setOutputTmpPdfPath(outputTmpPdfPath);
		
		try {
			// draw part one interleaf
			oneImgCreator.createTableImage(thisStartYear);
			// draw part two interleaf
			twoImgCreator.createTableImage();
			// draw part two table station interleaf
			for(int i=0; i<stnList.size(); i++){
				log.info("Draw " + stnList.get(i).getStnEName() + " interleaf.");
				String filename = FILENAME_STN_INTERLEAF_PREFIX + "_" + String.format("%02d", i+1) +"_0_" + stnList.get(i).getStno()+ "_" + FILENAME_STN_INTERLEAF_POSTFIX;
				stnImgCreator.setFilename(filename);
				stnImgCreator.createTableImage(i+1, stnList.get(i));
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
