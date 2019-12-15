package cwb.cmt.surface.pdfProcess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

public class MergePdfWithOutlines {

	private static final String COVER_C = "年氣候資料年報 第一部分-地面資料 ";
	private static final String COVER_E1 ="CLIMATOLOGICAL DATA ANNUAL REPORT "; 
	private static final String COVER_E2 =" PART I-SURFACE DATA";
	private static final String REFERENCE = "凡例 Reference Notes";
	private static final String DESCRIPTION = "其他說明 Other Description"; 
	
	private static final String CONTENTS = "目錄 Contents";
	private static final String STNS = "1.綜觀氣象站 Surface Synoptic Stations";
	private static final String YEAR_RPT = "2.綜觀及自動觀測站年表 Year Reports of Surface Synoptic and Automatic Weather Stations";
	private static final String WINDROSE = "3.風花圖 Wind Roses";
	private static final String TYPHOON = "4.颱風雨量圖 Precipitation Chart of Typhoon";
	private static final String DAILY = "5.日溫度雨量圖 Daily Temperature and Rainfall Chart";
	private static final String MONTHLY = "6.月季年溫度雨量圖 Monthly, Seasonal, Yearly Temp., Rainfall Chart";
	private static final String COPYRIGHT = "版權頁 Copyright";
	
	private static final String COVER_FN = "Cover";
	private static final String REFERENCE_FN = "Reference Notes";
	private static final String DESCRIPTION_FN = "Other Description";
	private static final String CONTENTS_FN = "Contents#1";
	private static final String COPYRIGHT_FN = "Copyright";
	
	
	private String getCover(int year) {
		StringBuilder sb = new StringBuilder();
		return sb.append(year-1911).append(COVER_C).append(COVER_E1).append(year).append(COVER_E2).toString();
	}
	
	void merge(int year, String srcFolder, String destFilename) throws DocumentException, IOException {
		
		// sort files
		File[] files = GetFiles.getSortedFiles(new File(srcFolder).listFiles());
		
		ArrayList<HashMap<String, Object>> outlines = new ArrayList<HashMap<String, Object>>();
		Document doc = new Document();
	    PdfCopy copy = new PdfCopy(doc, new FileOutputStream(destFilename));
        doc.open();
        
        PdfReader reader = null;
        int page = 1;
        
        for (final File temp : files) {
        	try {
				String fileName = temp.getName();	
				//add outline element
				if(fileName.contains(COVER_FN)){
					
					HashMap<String, Object> map = new HashMap<String, Object>();
			        map.put("Title", getCover(year));
			        map.put("Action", "GoTo");
			        map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);
				
				} else if (fileName.contains(REFERENCE_FN)){
					HashMap<String, Object> map = new HashMap<String, Object>();
			        map.put("Title", REFERENCE);
			        map.put("Action", "GoTo");
			        map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);
		
				} else if (fileName.contains(DESCRIPTION_FN)){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Title", DESCRIPTION);
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);
			    
				} else if (fileName.contains(CONTENTS_FN)){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Title", CONTENTS);
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);    
				
				}  else if (judgeInterleaf(fileName) == 1){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Title", STNS);
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);    
				
				}  else if (judgeInterleaf(fileName) == 2){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Title", YEAR_RPT);
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);    
				
				}  else if (judgeInterleaf(fileName) == 3){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Title", WINDROSE);
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);            
				
				}  else if (judgeInterleaf(fileName) == 4){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Title", TYPHOON);
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);            
				
				}  else if (judgeInterleaf(fileName) == 5){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Title", DAILY);
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);            
				
				} else if (judgeInterleaf(fileName) == 6){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Title", MONTHLY);
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);            
				
				} else if (fileName.contains(COPYRIGHT_FN)){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Title", COPYRIGHT);
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", page));
			        outlines.add(map);            
				} 
			
				FileInputStream is = null;
				try{
					is = new FileInputStream(temp.getPath());
					reader = new PdfReader(is);
					copy.addDocument(reader);
				} finally{
					is.close();  // close fileInputStream
				}
				int pagePerPdf = reader.getNumberOfPages();
				page = page + pagePerPdf;
			} finally{
//				temp.deleteOnExit();  // delete tmp pdf
			}
	    }
        // add the outlines
        copy.setOutlines(outlines);
		// close doc and reader
        doc.close();   
		reader.close(); 
		
	}
	
	// filename: 
	// fileSeq-pageNum_n1-n2-n3#n4_textChinese-textEnglish#pageSeq
	private int judgeInterleaf(String filename) {
		if(CheckPdf.isInterleaf(filename)) {
			return Integer.parseInt(filename.split("-")[0].split("_")[1]);
		} else {
			return 0;
		}
		
	}
	
}
