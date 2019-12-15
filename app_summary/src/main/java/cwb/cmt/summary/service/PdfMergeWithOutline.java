package cwb.cmt.summary.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.NumberFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import cwb.cmt.summary.config.Config;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.utils.GetFiles;
import cwb.cmt.summary.utils.PageEncode;

@Service
public class PdfMergeWithOutline {
	
	// outline titles
	private String outputPdfFile;
	private static final String COVER = "氣象報告彙編(臺灣)";
	private static final String EXAMPLE = "凡例";
	private static final String INTRODUCTION = "Introduction";
	private static final String STATIONS = "中央氣象局所屬各氣象站一覽表";
	private static final String CONTENTS = "總目錄";
	private static final String STN_MOVES = "測站資料變遷紀錄一覽表";
	private static final String INTERLEAF_ONE = "第一部 各氣象要素總表";
	private static final String CONTENTS_ONE = "第一部 目錄";
	private static final String INTERLEAF_TWO = "第二部 各測站氣象要素表";
	private static final String CONTENTS_TWO = "第二部 目錄";
	private static final String COPYRIGHT = "預行編目及著作權頁";
	
	private static Logger log = Logger.getLogger(PdfMergeWithOutline.class);
	private int year;
	private List<ClimaticElement> ces;
	private List<Station> stns;
	
	@Autowired
	public PdfMergeWithOutline (Config config, PrepareCes prepareCes, PrepareStations prepareStations) {
		this.year = config.getYear();
		this.outputPdfFile = config.getOutputPdfFile();
		ces = prepareCes.getClimaticElements();
		stns = prepareStations.getStations();
	}
	
	// get climatic element (part one table) tab title
	private int ceCount = 1;
	private String getCeCNameById(String ceId){
		String ceCName = ces.stream().filter(f -> f.getId().equals(ceId))
									.map(f -> f.getChineseName()).findFirst().orElse("風花圖");
		ceCName = String.valueOf(ceCount) + ". " + ceCName;
		ceCount++;
		return ceCName;
	}
	
	// get station (part tow table) tab title
	private String getStnById(String stno){
		Station stn =  stns.stream().filter(f -> f.getStno().equals(stno)).findFirst().get();
		return stn.getStnCName()+ " (" + stn.getStnEName() + " )";
	}
	
	// get cover tab title
	private String getCover(){
		int papaerCount = 7 + (year-2001)/10;
		int chinaYear = year-1911;
		Locale chineseNumbers = new Locale("C@numbers=hant");  // use ICU4J to turn number to Chinese number
		NumberFormat formatter = NumberFormat.getInstance(chineseNumbers);
		return COVER + "第" +  formatter.format(papaerCount) +  "篇民國" + String.valueOf(chinaYear) + "年至民國" + String.valueOf(chinaYear+9)+"年";
	}
	
	public void merge(final File pdfFolder) throws FileNotFoundException, DocumentException{
		log.info("Start to add outline and merge pdf...");
		
		// record the page
		int page = 1;
		
		// prepare document
		Document doc = new Document();
	    PdfCopy copy = new PdfCopy(doc, new FileOutputStream(outputPdfFile));
        doc.open();
        
        // prepare outline
        ArrayList<HashMap<String, Object>> outlines = new ArrayList<HashMap<String, Object>>();
        ArrayList<HashMap<String, Object>> kids1 = new ArrayList<HashMap<String, Object>>();
        ArrayList<HashMap<String, Object>> kids2 = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> interleafOne = new HashMap<String, Object>();
        HashMap<String, Object> interleafTwo = new HashMap<String, Object>();
        
        // prepare pdf reader
        PdfReader reader = null;
        
        // get sorted files
        File[] files = GetFiles.getSortedFiles(pdfFolder.listFiles()); // get all files in the folder
        
        // record if the first page of that part
        boolean isCover1st = true;
		boolean isExample1st = true;
		boolean isIntroduction1st = true;
		boolean isStations1st = true;
		boolean isContents1st = true;
		boolean isContentsPartOne1st = true;
		boolean isContentsPartTwo1st = true;
		boolean isCopyright1st = true;
        int stnYear = 0;
        String ceIdYear = "default";
        
        for (final File temp : files) {
        	
        	// get filename
        	String filename = temp.getName();
			
        	/*
        	 * add outline (tab)
        	 */
        	// cover
			if(filename.contains(PageEncode.COVER.getFilename()) && isCover1st){
				HashMap<String, Object> cover = new HashMap<String, Object>();
		        cover.put("Title", getCover());
		        cover.put("Action", "GoTo");
		        cover.put("Page", String.format("%d Fit", page));
		        outlines.add(cover);
		        isCover1st = false;
			
			// example (凡例)
			} else if (filename.contains(PageEncode.EXAMPLE.getFilename()) && isExample1st){
				HashMap<String, Object> example = new HashMap<String, Object>();
		        example.put("Title", EXAMPLE);
		        example.put("Action", "GoTo");
		        example.put("Page", String.format("%d Fit", page));
		        outlines.add(example);
		        isExample1st = false;
			
			// introduction 
			} else if (filename.contains(PageEncode.INTRODUCTION.getFilename()) && isIntroduction1st){
				HashMap<String, Object> contents = new HashMap<String, Object>();
				contents.put("Title", INTRODUCTION);
				contents.put("Action", "GoTo");
				contents.put("Page", String.format("%d Fit", page));
		        outlines.add(contents);
		        isIntroduction1st = false;
		    
		    // stations 測站一覽表    
			} else if (filename.contains(PageEncode.STATIONS.getFilename()) && isStations1st){
				HashMap<String, Object> stations = new HashMap<String, Object>();
				stations.put("Title", STATIONS);
				stations.put("Action", "GoTo");
				stations.put("Page", String.format("%d Fit", page));
		        outlines.add(stations);
		        isStations1st = false;
		        
		    // contents 總目錄    
			} else if (filename.contains(PageEncode.CONTENTS.getFilename()) && isContents1st){
				HashMap<String, Object> contents = new HashMap<String, Object>();
				contents.put("Title", CONTENTS);
				contents.put("Action", "GoTo");
				contents.put("Page", String.format("%d Fit", page));
		        outlines.add(contents);
		        isContents1st = false;    
		        
		    // station moves 變遷記錄一覽表    
			} else if (filename.contains(PageEncode.STATION_MOVES.getFilename())){
				String yearStr = FilenameUtils.removeExtension(filename).split("-")[1];
				int year = Integer.parseInt(yearStr.split("_")[0]);
				if(year != stnYear){ // is the first page of station moves
					HashMap<String, Object> stnMoves = new HashMap<String, Object>();
					stnMoves.put("Title", STN_MOVES + "(" + year + "-" + String.valueOf(year+9) + ")");
					stnMoves.put("Action", "GoTo");
					stnMoves.put("Page", String.format("%d Fit", page));
			        outlines.add(stnMoves);
				}
				stnYear = year;
			
			// part one interleaf     
			} else if (filename.contains(PageEncode.INTERLEAF_ONE.getFilename())){
				interleafOne = new HashMap<String, Object>();
				interleafOne.put("Title", INTERLEAF_ONE);
				interleafOne.put("Action", "GoTo");
				interleafOne.put("Page", String.format("%d Fit", page));
				outlines.add(interleafOne);
		        
		    // part one contents     
			} else if (filename.contains(PageEncode.CONTENTS_ONE.getFilename()) && isContentsPartOne1st){
				HashMap<String, Object> contentsOne = new HashMap<String, Object>();
				contentsOne.put("Title", CONTENTS_ONE);
				contentsOne.put("Action", "GoTo");
				contentsOne.put("Page", String.format("%d Fit", page));
				kids1.add(contentsOne); // part of kids1
				isContentsPartOne1st = false;
				
				
		    // part one tables     
			} else if (filename.contains(PageEncode.TABLE_ONE.getFilename())){
				String ceId = (filename.split("-")[1]).split("_")[0];
				if(!ceIdYear.equals(ceId)){
					String ceCName = getCeCNameById(ceId);
					HashMap<String, Object> tableOne = new HashMap<String, Object>();
					tableOne.put("Title", ceCName);
					tableOne.put("Action", "GoTo");
					tableOne.put("Page", String.format("%d Fit", page));
					kids1.add(tableOne); // part of kids1
				}
				ceIdYear = ceId;
				
			// part two interleaf     
			} else if (filename.contains(PageEncode.INTERLEAF_TWO.getFilename())){
				interleafTwo = new HashMap<String, Object>();
				interleafTwo.put("Title", INTERLEAF_TWO);
				interleafTwo.put("Action", "GoTo");
				interleafTwo.put("Page", String.format("%d Fit", page));
				outlines.add(interleafTwo);
		        
		    // part two contents     
			} else if (filename.contains(PageEncode.CONTENTS_TWO.getFilename()) && isContentsPartTwo1st){
				HashMap<String, Object> contentsTwo = new HashMap<String, Object>();
				contentsTwo.put("Title", CONTENTS_TWO);
				contentsTwo.put("Action", "GoTo");
				contentsTwo.put("Page", String.format("%d Fit", page));
				kids2.add(contentsTwo); // part of kids2
				isContentsPartTwo1st = false;
				
			// part two station interleaf
			} else if (filename.contains(PageEncode.STN_INTERLEAF.getFilename())){
				String stno = filename.split("_")[3];
				HashMap<String, Object> stnInterleaf = new HashMap<String, Object>();
				stnInterleaf.put("Title", getStnById(stno));
				stnInterleaf.put("Action", "GoTo");
				stnInterleaf.put("Page", String.format("%d Fit", page));
				kids2.add(stnInterleaf); // part of kids2
				
		    // copyright    
			} else if (filename.contains(PageEncode.COPYRIGHT.getFilename()) && isCopyright1st){
				HashMap<String, Object> copyright = new HashMap<String, Object>();
				copyright.put("Title", COPYRIGHT);
				copyright.put("Action", "GoTo");
				copyright.put("Page", String.format("%d Fit", page));
		        outlines.add(copyright);
		        isCopyright1st = false;      
			}
				
			FileInputStream is = null;
			try{
				is = new FileInputStream(temp.getPath());
				reader = new PdfReader(is);
				copy.addDocument(reader);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					// close fileInputStream
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
			
			int pagePerPdf = reader.getNumberOfPages();
			page = page + pagePerPdf;
//			temp.delete();  // delete tmp pdf

	    }
        
        // add the outlines
        interleafOne.put("Kids", kids1);  // put kids into parent
        interleafTwo.put("Kids", kids2);  // put kids into parent
        copy.setOutlines(outlines);
        
        // close doc and reader
        doc.close();   
		reader.close();
		log.info("Add outline and merge pdf completed...");
	}
}
