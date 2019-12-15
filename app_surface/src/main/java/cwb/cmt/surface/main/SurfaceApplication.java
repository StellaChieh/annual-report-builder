package cwb.cmt.surface.main;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.itextpdf.text.pdf.codec.Base64.OutputStream;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.util.SmartPdfSplitter;

import cwb.cmt.surface.pdfProcess.MainService;
import cwb.cmt.surface.process.ProcessAuxCliSum;
import cwb.cmt.surface.process.ProcessCe;
import cwb.cmt.surface.process.ProcessCliSum;
import cwb.cmt.surface.process.ProcessIndexPages;
import cwb.cmt.surface.process.ProcessInterleaf;
import cwb.cmt.surface.process.ProcessListOfWeatherStns;
import cwb.cmt.surface.process.ProcessStn;
import cwb.cmt.surface.process.ProcessSurface;
import cwb.cmt.surface.process.ProcessWindRose;
import cwb.cmt.surface.tools.OnCsvProductionCompleteListener;
import cwb.cmt.surface.tools.OnPdfProductionCompleteListener;
import cwb.cmt.surface.utils.Month;
import cwb.cmt.surface.utils.NumberConvert;
import cwb.cmt.surface.utils.ReportFileInfoNew;
import cwb.cmt.surface.utils.ReportFileUtils;
import static cwb.cmt.surface.utils.NumberConvert.expand2Strings;
import static cwb.cmt.surface.utils.ConvertFileName.encodeFilename;
import cwb.cmt.surface.utils.ReportFileInfoNew.ReportFileInfoParser;
import static cwb.cmt.surface.utils.ReportFileUtils.getSortedFileInformation;
import static cwb.cmt.surface.utils.NumberConvert.expand2StringsExcludingNull;
import static java.lang.String.join;

public class SurfaceApplication {
	private static boolean mRunOnce;
	private static int year;
	private static String outputTmpPdfPath;
	private static String outputPdfFile;
	private static String outputCsvFolder;
	private static String rootPath;
	
	/*
     * The directory for the PDF files that the client provides
     * specify your own.
     */
	//only for test
    private static final String ExternalPdfFilesDir = 
    		"C:\\Users\\YuPing ho\\CMTworkspace\\annual-report\\externalPdfSurface";
    /*
     * The directory for and the configuration file exists
     * specify your own.
     */
	private static ApplicationContext context = null;
	private static Map<String, String> mOrderedOuterPdfFilePath = new LinkedHashMap<String, String>();
	private static final LinkedHashMap<String, Integer> OutputFilePageCount = new LinkedHashMap<String, Integer>();
    private static final String[] Keys = new String[] {
            "coverFilePath",
            "exampleFilePath",
            "explanationFilePath",
            "stnMapFilePath",
            "districtsFilePath",
            "autoStnMapFilePath",
            "typhoonFilePath",
            "tempRainFilePath",
            "monthlyTempRainFilePath",
            "copyrightFilePath",
        };
        
    private static final String[] DestFilenameFormatTempRainfallChartDaily = new String[] {
        "6_%d_民國%d年 1－4月溫度分佈圖",    
        "6_%d_民國%d年 5－8月溫度分佈圖",
        "6_%d_民國%d年 9－12月溫度分佈圖",
        "6_%d_民國%d年季溫度分佈圖",
        "6_%d_民國%d年年溫度分佈圖",
        "6_%d_民國%d年 1－4月雨量分佈圖",    
        "6_%d_民國%d年 5－8月雨量分佈圖",
        "6_%d_民國%d年 9－12月雨量分佈圖",
        "6_%d_民國%d年季雨量分佈圖",
        "6_%d_民國%d年年雨量分佈圖",
    };

	
	private static SurfaceApplication init (String appConfigPath) throws Exception {
		Properties props = new Properties();
		//read key,value matching
		props.load(new FileInputStream(appConfigPath));
		String rootPath = props.getProperty("cmt.root.path").trim();
		String beanPath = props.getProperty("surface.spring.bean.path").trim();
//		System.out.println("Paths.get(rootPath, beanPath).toString()>> "+Paths.get(rootPath, beanPath).toString());
		context = new FileSystemXmlApplicationContext(Paths.get(rootPath, beanPath).toString());
		return new SurfaceApplication();
	}
	
	//config.year
	public void createYear() throws IOException {
		ProcessSurface processSurface = (ProcessSurface)context.getBean("processSurface");
		year = processSurface.getYear();
	}
	//config.rootPath()
	public void createRootPath() throws IOException {
		ProcessSurface processSurface = (ProcessSurface)context.getBean("processSurface");
		rootPath = processSurface.getRootPath();
	}
	//config.outPutTmpPdfPath
	public void createOutPutTmpPdfPath() throws IOException {
		ProcessSurface processSurface = (ProcessSurface)context.getBean("processSurface");
		outputTmpPdfPath = Paths.get(rootPath, processSurface.getOutputTmpPdfPath()).toString();
	}
	//config.outputPdfFile
	public void createOutPutPdfFile() throws IOException {
		ProcessSurface processSurface = (ProcessSurface)context.getBean("processSurface");
		outputPdfFile = processSurface.getOutputPdfFile();
	}
	
	//config.outputPdfFile
	public void createOutPutCsvFile() throws IOException {
		ProcessSurface processSurface = (ProcessSurface)context.getBean("processSurface");
		outputCsvFolder = processSurface.getOutputCsvFolder();
	}
	
	//風花圖
	public void createWindRose() throws IOException {
		ProcessWindRose processWindRose = (ProcessWindRose)context.getBean("processWindRose");
		processWindRose.run();
	}
	
	//目錄
    public void makeSurfaceDataIndexPages(String outputTmpPdfPath) throws IOException  {
        ProcessIndexPages processIndexPages = (ProcessIndexPages)context.getBean("processIndexPages");;
        processIndexPages.run(outputTmpPdfPath);
    }
	
	//1.1中央氣象局所屬各氣象站一覽表(List of Stations of Central Weather Bureau)
	public void createStns() throws IOException {
	//General Table for Climatological Summaries
		ProcessStn processStn = (ProcessStn)context.getBean("processStn");
		processStn.run();
		
	}
	
	//1.4輔助氣候綱要表 (Auxiliary Table for Climatological Summaries)
	public void createAuxCliSum() throws IOException{
		ProcessAuxCliSum processAuxCliSum = (ProcessAuxCliSum)context.getBean("processAuxCliSum");
		processAuxCliSum.runRadSun();      //1.4.1
		processAuxCliSum.runGlobalRad();  //1.4.2
		processAuxCliSum.runRadHrMax();  //1.4.3
	}
	
	//2.2專用氣象觀測站編號一覽表 (List of Weather Station Number of Other Organization)
	public void createListOfWeatherStns() throws IOException{
		ProcessListOfWeatherStns processListOfWeatherStns= (ProcessListOfWeatherStns)context.getBean("processListOfWeatherStns");
		processListOfWeatherStns.run();      //2.2

	}
	
	//1.3 基本氣象綱要表(General Table for Climatological Summaries)
	public void createCliSum() throws IOException{
		ProcessCliSum processCliSum= (ProcessCliSum)context.getBean("processCliSum");
		processCliSum.run();      //1.3

	}
	
	//2.3-2.20 氣象要素表(Climatic Element)
	public void createClimaticElement() throws IOException{
		ProcessCe processCe= (ProcessCe)context.getBean("processCe");
		processCe.run();      

	}
	
	// 產製插頁
	public void createInterleaf(int chapter, String titleChinese, String titleEnglish) throws IOException{
		ProcessInterleaf processInterleaf = (ProcessInterleaf) context.getBean("processInterleaf");
		processInterleaf.run(chapter, titleChinese, titleEnglish);
	}
	
	//CSV: 1.1中央氣象局所屬各氣象站一覽表(List of Stations of Central Weather Bureau)
	public void createStnsCsv(String outputCsvFolder) throws IOException {
		ProcessStn processStn = (ProcessStn)context.getBean("processStn");
		processStn.runCsv(outputCsvFolder);
	}
	
	//CSV: 1.4輔助氣候綱要表 (Auxiliary Table for Climatological Summaries)
	public void createAuxCliSumCsv(String outputCsvFolder) throws IOException{
		ProcessAuxCliSum processAuxCliSum = (ProcessAuxCliSum)context.getBean("processAuxCliSum");
		processAuxCliSum.runRadSunCsv(outputCsvFolder);      //1.4.1
		processAuxCliSum.runGlobalRadCsv(outputCsvFolder);  //1.4.2 & 1.4.3
	}
	
	//CSV: 2.2專用氣象觀測站編號一覽表 
	public void createListOfWeatherStnsCsv(String outputCsvFolder) throws IOException{
		ProcessListOfWeatherStns processListOfWeatherStns= (ProcessListOfWeatherStns)context.getBean("processListOfWeatherStns");
		processListOfWeatherStns.runListOfWeatherStnsCsv(outputCsvFolder);      //2.2
	}
	
	//CSV: 2.3-2.20 氣象要素表(Climatic Element)
	public void createCsvClimaticElement(String outputCsvFolder) throws IOException{
		ProcessCe processCe= (ProcessCe)context.getBean("processCe");
		processCe.runClimateElementCsv(outputCsvFolder);       

	}
	
	//CSV: 1.3 基本氣象綱要表(General Table for Climatological Summaries)
	public void createCsvCliSum(String outputCsvFolder) throws IOException{
		ProcessCliSum processCliSum= (ProcessCliSum)context.getBean("processCliSum");
		processCliSum.runClisumCsv(outputCsvFolder);      //1.3

	}
	// 產製整本書
	public boolean createReport(Map<String, String> filePathParams) throws IOException{
		long begin = System.currentTimeMillis();
		createYear();
		createRootPath();
		createOutPutTmpPdfPath();
		createOutPutPdfFile();
		// delete all the temp pdf files in the ouput temp directory
		cleanPdfOutputDir(outputTmpPdfPath);
		//perform re-ordering
		orderOuterFilePath(filePathParams);
		
        // 0.
        moveExternalFilesToPdfOutputDir("coverFilePath");
        moveExternalFilesToPdfOutputDir("exampleFilePath");
        moveExternalFilesToPdfOutputDir("explanationFilePath");
        // 1.
        createInterleaf(1, "綜觀氣象站", "Surface Synoptic Stations");
        createStns();
        moveExternalFilesToPdfOutputDir("stnMapFilePath");
        createCliSum(); 
        createAuxCliSum();
     	// 2.
		createInterleaf(2,
		        "綜觀及自動站觀測年表",
		        "Year Reports of Surface Synoptic and\nAutomatic Weather Stations");
	    moveExternalFilesToPdfOutputDir("districtsFilePath");
        moveExternalFilesToPdfOutputDir("autoStnMapFilePath");
        createListOfWeatherStns();
		createClimaticElement();
	    // 3.
		createInterleaf(3, "風花圖", "Wind Roses");
		createWindRose();
        // 4.
		createInterleaf(4, "颱風雨量圖", "Rainfall Chart of Typhoon");
        moveExternalFilesToPdfOutputDir("typhoonFilePath");
        // 5.
		createInterleaf(5, "溫度雨量圖", "Daily Temp. and Rainfall Chart");
        moveExternalFilesToPdfOutputDir("tempRainFilePath");
        // 6.
		createInterleaf(6, "溫度、雨量分佈圖", "Monthly Temperature Chart");
        moveExternalFilesToPdfOutputDir("monthlyTempRainFilePath");
		
        // 7.
        moveExternalFilesToPdfOutputDir("copyrightFilePath");
        
        // Rename to the new naming rule
        renameReportFilesByNewNamingRule(outputTmpPdfPath);
        
		//產目錄
		makeSurfaceDataIndexPages(outputTmpPdfPath);
        // Get the file list of the index pages
        File[] files = new File(outputTmpPdfPath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains("Contents");
            }            
        });
        
        // sort by page sequence
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return f1.getName().substring(f1.getName().lastIndexOf('#')).compareTo(
                       f2.getName().substring(f2.getName().lastIndexOf('#')));
            }            
        });
        // Generate proper page number for the index pages
        for (int i=0; i<files.length; i++) {
            files[i].renameTo(new File(outputTmpPdfPath + File.separator +
                                       String.format("%d-%d_%s", fileSequence_content, pageNum_content, files[i].getName())));
            fileSequence_content++;
            pageNum_content++;
        }
        
        // Re-arrange the page number of the files.
        // rename it to the next page number of the last index page.
        files = new File(outputTmpPdfPath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                ReportFileInfoNew info = (ReportFileInfoNew) getSortedFileInformation(file.getName(), new ReportFileInfoParser());
                if (info.pageNum == -1) {
                    return false;
                }
                int[] indexes = info.indexes;
                boolean ret = (indexes.length == 2 && indexes[0] == 1 && indexes[1] <= 2);
                if (ret) {
                    File dest = new File(file.getParent() + File.separator
                                       + info.fileSequence
                                       + ((pageNum_content != -1)? "-" + pageNum_content : "") + "_"
                                       + join("-", (CharSequence[]) expand2Strings(info.Indexes)) + "_"
                                       + join("-", (CharSequence[]) expand2StringsExcludingNull(info.textChinese, info.textEnglish))
                                       + ((info.pageSequence >= 0)?  "#" + info.pageSequence:"")
                                       + "." + info.ext);
                    file.renameTo(dest);
                    fileSequence_content++;
                    pageNum_content++;
                }
                return ret;
            }            
        });
        //merge pdf
        try {
            MainService.processPdf(
                year,
                outputTmpPdfPath,
                outputPdfFile
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (DocumentException e) {
            e.printStackTrace();
        }
        
        // set complete
        mRunOnce = true;
        if (pdfListener != null)
            pdfListener.onAllReportProductionComplete();
        
        long over = System.currentTimeMillis();
        System.out.println("使用的時間為： "
            + (over - begin)/1000 + " 秒 " ); 
		return true;
	}
	
	public boolean createCsvReport() throws IOException{
		long begin = System.currentTimeMillis();
		createYear();
		createOutPutCsvFile();
		// delete all the csv files in the  ouput directory
		cleanCsvOutputDir(outputCsvFolder);
		createStnsCsv(outputCsvFolder);  //1.1
		createAuxCliSumCsv(outputCsvFolder); //1.4.1-1.4.3
		createListOfWeatherStnsCsv(outputCsvFolder); //2.2
		createCsvClimaticElement(outputCsvFolder); //2.3
		createCsvCliSum(outputCsvFolder); //1.3
		
		 // set complete
        mRunOnce = true;
        if (csvListener != null)
            csvListener.onAllReportProductionComplete();
        
	    long over = System.currentTimeMillis();
        System.out.println("使用的時間為： "
            + (over - begin)/1000 + " 秒 " ); 
		return true;
	}
	
	//only for test
	public static void main(String[] args) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("coverFilePath", ExternalPdfFilesDir("0.封面.pdf"));
		params.put("exampleFilePath", ExternalPdfFilesDir("0.凡例.pdf"));
		params.put("explanationFilePath", ExternalPdfFilesDir("0.其他說明.pdf"));        
        
		params.put("stnMapFilePath", ExternalPdfFilesDir("1.2測站分布圖.pdf"));        
		params.put("districtsFilePath", ExternalPdfFilesDir("2.1.1 臺灣行政區域概圖.pdf"));
        
		params.put("autoStnMapFilePath", ExternalPdfFilesDir("2.1.2.中央氣象局所屬自動雨量及氣象站分布圖.pdf"));        
		params.put("typhoonFilePath", ExternalPdfFilesDir("5.颱風雨量圖集.pdf"));
		params.put("tempRainFilePath", ExternalPdfFilesDir("6.日溫度雨量分佈圖.pdf"));
		params.put("monthlyTempRainFilePath", ExternalPdfFilesDir("7.月季年溫度雨量分佈圖.pdf"));
		params.put("copyrightFilePath", ExternalPdfFilesDir("8.地面版權頁.pdf"));
		
		//test for create pdf report
		SurfaceApplication.init("C:\\Users\\YuPing ho\\CMTworkspace\\annual-report\\config.properties")
							.createReport(params);
//		//test for create csv report
//		SurfaceApplication.init("C:\\Users\\YuPing ho\\CMTworkspace\\annual-report\\config.properties")
//							.createCsvReport();
	}
	
	public static void createPdfReport(Map<String, String> params, String configPath) throws IOException, Exception {
		SurfaceApplication.init(configPath).createReport(params);
	}
	
	public static void createCsvReport(String configPath) throws IOException, Exception {
		SurfaceApplication.init(configPath).createCsvReport();
	}
	
    private static String ExternalPdfFilesDir(String filename) {
        return filename(ExternalPdfFilesDir, filename);
    }
    private static String filename(String parent, String filename) {
        return parent + File.separator + filename;
    }
    
	private static int fileSequence_content = 4;
    private static int pageNum_content = 3;
    
    private static void renameReportFilesByNewNamingRule(String dirPath) {
	    File[] files = ReportFileUtils.getSortedReportFiles(dirPath);  
        boolean isCover = true;
        boolean beginCountPageNum_0 = true;
        boolean beginCountPageNum = false;
        for (int i=0, fileSequence=1, pageNum=1; i<files.length; fileSequence++, i++) {
            String filename = files[i].getName();
            String text_part = ReportFileUtils.getTextPart(filename);
            String index_part = ReportFileUtils.getIndexPart(filename);
            Integer[] indexes = ReportFileUtils.get_Indexes(filename);

            if (indexes.length == 3 && index_part.substring(0, index_part.lastIndexOf('_')).equals("1_3")) {
                if (!beginCountPageNum) {
                    pageNum = 1;
                    beginCountPageNum_0 = false;
                    beginCountPageNum = true;
                }
            }
            
            boolean countPageNum = beginCountPageNum;
            if (isCover || indexes[indexes.length-1] == 0) {
                countPageNum = false;
            }
            
            // determine what page number looks like
            // (arabic or roman number)
            String pageNumStr = ((countPageNum || beginCountPageNum_0 ) && (!isCover)) && indexes[indexes.length-1] != 0
                                ? String.valueOf(pageNum) : null;
//            System.out.println("* " + "pageNum: " + pageNum + ", " + Arrays.toString(indexes));
            
            // accumulate counter
            if ((countPageNum || beginCountPageNum_0) && indexes[indexes.length-1] != 0) pageNum++;
            
            
            // index and table number
            int table = -1;
            if (index_part.substring(0, index_part.lastIndexOf('_')).equals("1_3")) {
                table = indexes[2];
            }
            else if (index_part.substring(0, index_part.lastIndexOf('_')).equals("1_4_1")) {
                table = indexes[3];
            }
            if (table != -1) {
                indexes = Arrays.copyOfRange(indexes, 0, indexes.length-1);
            }
            index_part = String.join("-", (CharSequence[]) expand2Strings(indexes)) + ((table != -1)? "#" + table : "");

            // page
            int pageSequence = ReportFileUtils.getSequence(filename);
            
            // extension filename
            String extFilename = filename.substring(filename.lastIndexOf('.')+1, filename.length());
            File dest = new File(files[i].getParent() + File.separator
                               + fileSequence
                               + ((pageNumStr != null)? "-" + pageNumStr : "") + "_"
                               + index_part.replace('_', '-') + "_"
                               + encodeFilename(text_part).replace('_', '-')
                               + ((pageSequence >= 0)?  "#" + pageSequence:"")
                               + "." + extFilename);
            
            files[i].renameTo(dest);
            
            if (indexes.length == 2 && indexes[0] == 0 && indexes[1] == 2) {
                fileSequence_content = fileSequence+1;
                pageNum_content = pageNum;
                fileSequence += 100;
            }            
            isCover = false;
        }
	}
    
    private static void orderOuterFilePath(Map<String, String> outerPdfFilePath) {
	    // Ordering the entries
        if (outerPdfFilePath.containsKey("coverFilePath"))
            mOrderedOuterPdfFilePath.put("coverFilePath", outerPdfFilePath.get("coverFilePath"));
        if (outerPdfFilePath.containsKey("exampleFilePath"))
            mOrderedOuterPdfFilePath.put("exampleFilePath", outerPdfFilePath.get("exampleFilePath"));
        if (outerPdfFilePath.containsKey("explanationFilePath"))
            mOrderedOuterPdfFilePath.put("explanationFilePath", outerPdfFilePath.get("explanationFilePath"));
        
        if (outerPdfFilePath.containsKey("stnMapFilePath"))
            mOrderedOuterPdfFilePath.put("stnMapFilePath", outerPdfFilePath.get("stnMapFilePath"));
        
        if (outerPdfFilePath.containsKey("districtsFilePath"))
            mOrderedOuterPdfFilePath.put("districtsFilePath", outerPdfFilePath.get("districtsFilePath"));
        if (outerPdfFilePath.containsKey("autoStnMapFilePath"))
            mOrderedOuterPdfFilePath.put("autoStnMapFilePath", outerPdfFilePath.get("autoStnMapFilePath"));
        
        if (outerPdfFilePath.containsKey("typhoonFilePath"))
            mOrderedOuterPdfFilePath.put("typhoonFilePath", outerPdfFilePath.get("typhoonFilePath"));
        if (outerPdfFilePath.containsKey("tempRainFilePath"))
            mOrderedOuterPdfFilePath.put("tempRainFilePath", outerPdfFilePath.get("tempRainFilePath"));
        if (outerPdfFilePath.containsKey("monthlyTempRainFilePath"))
            mOrderedOuterPdfFilePath.put("monthlyTempRainFilePath", outerPdfFilePath.get("monthlyTempRainFilePath"));
        if (outerPdfFilePath.containsKey("copyrightFilePath"))
            mOrderedOuterPdfFilePath.put("copyrightFilePath", outerPdfFilePath.get("copyrightFilePath"));
	}
    //clean pdf output directory
	private static void cleanPdfOutputDir(String getPdfOutputPath) {
	    // clean up files
//	    AppConfig config = AppConfig.getConfig();
        File[] files = new File(getPdfOutputPath).listFiles();
        for (File file : files) {
            try {
                file.delete();
            }
            catch (Exception e) {
                
            }
        }     
	}
	
    //delete csv output directory
	private static void cleanCsvOutputDir(String getPdfOutputPath) {
	    // clean up files
        File[] files = new File(getPdfOutputPath).listFiles();
        String fileYear = String.valueOf(year);
        for (File file : files) {
        	String fileName = file.getName().replace(getPdfOutputPath, "");
            try {
            	if (fileName.endsWith(".csv")&& fileName.startsWith(fileYear)) {
            		 file.delete();
            	}
            }
            catch (Exception e) {
                
            }
        }     
	}
	
    private static void moveExternalFilesToPdfOutputDir(String key) {
        // key existence check
        if (!mOrderedOuterPdfFilePath.containsKey(key)) {
            System.err.println("The corresponding path of the key \'" + key + "\' does not exist.");
            return;
        }
        
//	    // obtain application config instance
//	    AppConfig config = AppConfig.getConfig();
	    
        // iterate through path parameters
	    // existence check
        File file = new File(mOrderedOuterPdfFilePath.get(key));
        if (!file.exists()) {
            System.err.println("[WARNING]: The file \'" + mOrderedOuterPdfFilePath.get(key) + "\' does not exist.");
            return;
        }
        else if (!file.isFile()) {
            System.err.println("[WARNING]: The file \'" + mOrderedOuterPdfFilePath.get(key) + "\' is not a file.");
            return;
        }

        Throwable throwable = null;
        PdfReader reader = null;
        try {
            // open pdf file
            reader = new PdfReader(file.getAbsolutePath());             
            
            OutputFilePageCount.put(key, reader.getNumberOfPages());
            
            // Determine destination filename
            String destFilenameFormat = "";
            switch (key) {
                // 0 封面
                case "coverFilePath":
                    destFilenameFormat = "0_0_封面_Cover";
                    break;
                    
                // 0.1 凡例
                case "exampleFilePath":
                    destFilenameFormat = "0_1_凡例_Reference Notes";
                    break;
                    
                // 0.2 其他說明
                case "explanationFilePath":
                    destFilenameFormat = "0_2_其他說明_Other Description";
                    break;
                    
                // 1.2 測站分佈圖
                case "stnMapFilePath":
                    destFilenameFormat = "1_2_測站分布圖_Geographic Distribution of Surface Synoptic Stations";
                    break;
                    
                // 2.1 台灣行政區域蓋圖
                case "districtsFilePath":
                    destFilenameFormat = "2_1_臺灣行政區概圖_Map of Taiwan Administrative Districts";
                    break;
                    
                // 2.1 (1) ~ 2.1 (3)
                case "autoStnMapFilePath":
                    destFilenameFormat = "2_1_2_中央氣象局所屬自動雨量及氣象站分布圖";
                    break;
                    
                // 4 颱風雨量圖:
                case "typhoonFilePath":
                    destFilenameFormat = "4_%d_民國" + (year - 1911) + "年編號第%d號%s颱風_" + year + "%02d Typhoon %s"; // 序號, 颱風號, 颱風名稱; 颱風號, 颱風英文名稱
                    break;
                    
                // 5 溫度雨量圖 (日)
                case "tempRainFilePath":
                    destFilenameFormat = "5_%d_民國" + (year - 1911) + "年%d月溫度雨量圖_Daily Temp. & Rainfall Chart, %s " + year;  // 序號, 月, 月英文縮寫
                    if (reader.getNumberOfPages() != 12) {
                        // !!??
                    }
                    break;
                    
                // 6 溫度雨量分佈圖 (月)
                case "monthlyTempRainFilePath":
                    if (reader.getNumberOfPages() != 10) {
                        // !!??
                    }               
                    break;
                    
                // 7 版權頁
                case "copyrightFilePath":
                    destFilenameFormat = "7_0_地面版權頁_Copyright";
                    break;
            }
            System.out.println("*** Copying the file " + "\'" + mOrderedOuterPdfFilePath.get(key) + "\' to " + outputTmpPdfPath); 
            
            
            // Splitter to split pdf file of multiple pages
            SmartPdfSplitter splitter = new SmartPdfSplitter(reader);
            
            // typhoon text
            int typhoonNo = 0;
            int typhoonOrdinal = 0;
            String typhoonNoStr = null, typhoonChineseName=null, typhoonEnglishName = null;
            
            // iterate through pages
            int pdfPage_file = 1;
            int pdfPage_specificTyphoon = 1;
            int pdfPage_monthlyTempRain = 1;
            while (splitter.hasMorePages()) {
                System.out.println("pdf page: " + pdfPage_file + ", " + "pdfPageForSpecificTyphoon: " + pdfPage_specificTyphoon);
                
                // producing destination filename
                String destFilename = "";
                switch (key) {
                    // 2.1
                    case "autoStnMapFilePath":
                        destFilename = String.format(destFilenameFormat, pdfPage_file);
                        break; 
                    // 4.x
                    case "typhoonFilePath":
                        // fetch content from the page
                        String content = PdfTextExtractor.getTextFromPage(reader, pdfPage_file);
                        String line = ((content != null)? content.split("\\n")[0] : "").trim();
                        if (line.endsWith("颱風")) {
                            typhoonOrdinal++;
                            pdfPage_specificTyphoon = 1;
                            int begin = line.indexOf('第'), end = line.lastIndexOf('號');
                            typhoonNoStr = line.substring(begin+1, end).trim();
                            typhoonNo = (NumberConvert.isNumeric(typhoonNoStr))? Integer.parseInt(typhoonNoStr) : 0;
                            
                            begin = line.lastIndexOf('號');
                            end = line.indexOf('(');
                            typhoonChineseName = line.substring(begin+1, end).trim();
                            
                            begin = line.indexOf('(');
                            end = line.indexOf(')');
                            typhoonEnglishName = line.substring(begin+1, end).trim();
                        }
                        destFilename = String.format(destFilenameFormat, typhoonOrdinal, typhoonNo, typhoonChineseName, typhoonNo, typhoonEnglishName);
                        break;
                        
                    // 5.x
                    case "tempRainFilePath":
                        destFilename = String.format(destFilenameFormat, pdfPage_file, pdfPage_file, Month.getByIndex(pdfPage_file-1).getSimpleName());
                        break;
                        
                    // 6.x
                    case "monthlyTempRainFilePath":
                    	// fetch content from the page
                        String contents = PdfTextExtractor.getTextFromPage(reader, pdfPage_file);
                        String line_0 = ((contents != null)? contents.split("\\n")[0] : "").trim().replaceAll(" ", "");
                        String line_1 = ((contents != null)? contents.split("\\n")[1] : "").trim().replaceAll(" ", "");
                        String line_2 = ((contents != null)? contents.split("\\n")[2] : "").trim().replaceAll(" ", "");
                        String line_3 = ((contents != null)? contents.split("\\n")[3] : "").trim().replaceAll(" ", "");
//                        System.out.println("::line_0:: "+line_0);
//                        System.out.println("::line_1:: "+line_1);
//                        System.out.println("::line_2:: "+line_2);
//                        System.out.println("::line_3:: "+line_3);
                        
                        if (line_0.endsWith("各月平均溫度")||line_1.endsWith("各月平均溫度")||
                        		line_2.endsWith("各月平均溫度")||line_3.endsWith("各月平均溫度")||
                        		line_0.endsWith("月均溫")||line_1.endsWith("月均溫")||
                        		line_2.endsWith("月均溫")||line_3.endsWith("月均溫")) {
                        	//collect each line from pdf page
                           	List<String> lines = new ArrayList<>();
                        	lines.add(line_0.replaceAll(" ", ""));
                        	lines.add(line_1.replaceAll(" ", ""));
                        	lines.add(line_2.replaceAll(" ", ""));
                        	lines.add(line_3.replaceAll(" ", ""));
                        	//setting pattern 
                        	List<Pattern> patterns_Jan = new ArrayList<>();
                        	patterns_Jan.add(Pattern.compile("\\d{4}年1月~4月各月平均溫度"));
                        	patterns_Jan.add(Pattern.compile("\\d{4}年1月~4月均溫"));
                        	List<Pattern> patterns_May = new ArrayList<>();
                        	patterns_May.add(Pattern.compile("\\d{4}年5月~8月各月平均溫度"));
                        	patterns_May.add(Pattern.compile("\\d{4}年5月~8月均溫"));
                        	List<Pattern> patterns_Sept = new ArrayList<>();
                        	patterns_Sept.add(Pattern.compile("\\d{4}年9月~12月各月平均溫度"));
                        	patterns_Sept.add(Pattern.compile("\\d{4}年9月~12月均溫"));
                        	//pattern match
                        	for (Pattern pattern: patterns_Jan) {
                        		for(String perLine: lines) {
                        			Matcher matcher = pattern.matcher(perLine);
                        			if (matcher.matches()) {
                        		    	destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[0], pdfPage_file, year - 1911);
                        		    }
                        		}
                        	}
                        	for (Pattern pattern: patterns_May) {
                        		for(String perLine: lines) {
                        			Matcher matcher = pattern.matcher(perLine);
                        			if (matcher.matches()) {
                        				destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[1], pdfPage_file, year - 1911);
                        		    }
                        		}
                        	}
                         	for (Pattern pattern: patterns_Sept) {
                        		for(String perLine: lines) {
                        			Matcher matcher = pattern.matcher(perLine);
                        			if (matcher.matches()) {
                        				destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[2], pdfPage_file, year - 1911);
                        		    }
                        		}
                        	}
                        }
                        else if(line_0.endsWith("各季平均溫度")||line_1.endsWith("各季平均溫度")||
                        		line_2.endsWith("各季平均溫度")||line_3.endsWith("各季平均溫度")||
                        		line_0.endsWith("各季均溫")||line_1.endsWith("各季均溫")||
                        		line_2.endsWith("各季均溫")||line_3.endsWith("各季均溫")) {
                        	//collect each line from pdf page
                           	List<String> lines = new ArrayList<>();
                        	lines.add(line_0.replaceAll(" ", ""));
                        	lines.add(line_1.replaceAll(" ", ""));
                        	lines.add(line_2.replaceAll(" ", ""));
                        	lines.add(line_3.replaceAll(" ", ""));
                        	//setting pattern 
                        	List<Pattern> patterns_season = new ArrayList<>();
                        	patterns_season.add(Pattern.compile("\\d{4}年各季平均溫度"));
                        	patterns_season.add(Pattern.compile("\\d{4}年各季均溫"));
                        	//pattern match
                        	for (Pattern pattern: patterns_season) {
                        		for(String perLine: lines) {
                        			Matcher matcher = pattern.matcher(perLine);
                        			if (matcher.matches()) {
                        				destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[3], pdfPage_file, year - 1911);
                        		    }
                        		}
                        	}
                        }
                        else if(line_0.endsWith("年平均溫度")||line_1.endsWith("年平均溫度")||
                        		line_2.endsWith("年平均溫度")||line_3.endsWith("年平均溫度")||
                        		line_0.endsWith("年均溫")||line_1.endsWith("年均溫")||
                        		line_2.endsWith("年均溫")||line_3.endsWith("年均溫")) {
                        	//collect each line from pdf page
                           	List<String> lines = new ArrayList<>();
                        	lines.add(line_0.replaceAll(" ", ""));
                        	lines.add(line_1.replaceAll(" ", ""));
                        	lines.add(line_2.replaceAll(" ", ""));
                        	lines.add(line_3.replaceAll(" ", ""));
                        	//setting pattern 
                        	List<Pattern> patterns_year = new ArrayList<>();
                        	patterns_year.add(Pattern.compile("\\d{4}年平均溫度"));
                        	patterns_year.add(Pattern.compile("\\d{4}年均溫"));
                        	//pattern match
                        	for (Pattern pattern: patterns_year) {
                        		for(String perLine: lines) {
                        			Matcher matcher = pattern.matcher(perLine);
                        			if (matcher.matches()) {
                        				destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[4], pdfPage_file, year - 1911);
                        		    }
                        		}
                        	}
                        }
                        else if(line_0.endsWith("各月累積雨量")||line_1.endsWith("各月累積雨量")||
                        		line_2.endsWith("各月累積雨量")||line_3.endsWith("各月累積雨量")||
                        		line_0.endsWith("月累積雨量")||line_1.endsWith("月累積雨量")||
                        		line_2.endsWith("月累積雨量")||line_3.endsWith("月累積雨量")) {
                        	//collect each line from pdf page
                           	List<String> lines = new ArrayList<>();
                        	lines.add(line_0.replaceAll(" ", ""));
                        	lines.add(line_1.replaceAll(" ", ""));
                        	lines.add(line_2.replaceAll(" ", ""));
                        	lines.add(line_3.replaceAll(" ", ""));
                        	//setting pattern 
                        	List<Pattern> patterns_Jan = new ArrayList<>();
                        	patterns_Jan.add(Pattern.compile("\\d{4}年1月~4月各月累積雨量"));
                        	patterns_Jan.add(Pattern.compile("\\d{4}年1月~4月累積雨量"));
                        	List<Pattern> patterns_May = new ArrayList<>();
                        	patterns_May.add(Pattern.compile("\\d{4}年5月~8月各月累積雨量"));
                        	patterns_May.add(Pattern.compile("\\d{4}年5月~8月累積雨量"));
                        	List<Pattern> patterns_Sept = new ArrayList<>();
                        	patterns_Sept.add(Pattern.compile("\\d{4}年9月~12月各月累積雨量"));
                        	patterns_Sept.add(Pattern.compile("\\d{4}年9月~12月累積雨量"));
                        	//pattern match
                        	for (Pattern pattern: patterns_Jan) {
                        		for(String perLine: lines) {
                        			Matcher matcher = pattern.matcher(perLine);
                        			if (matcher.matches()) {
                        				destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[5], pdfPage_file, year - 1911);
                        		    }
                        		}
                        	}
                        	for (Pattern pattern: patterns_May) {
                        		for(String perLine: lines) {
                        			Matcher matcher = pattern.matcher(perLine);
                        			if (matcher.matches()) {
                        				destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[6], pdfPage_file, year - 1911);
                        		    }
                        		}
                        	}
                         	for (Pattern pattern: patterns_Sept) {
                        		for(String perLine: lines) {
                        			Matcher matcher = pattern.matcher(perLine);
                        			if (matcher.matches()) {
                        				destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[7], pdfPage_file, year - 1911);
                        		    }
                        		}
                        	}
                        }
                        else if(line_0.endsWith("各季累積雨量")||line_1.endsWith("各季累積雨量")||
                        		line_2.endsWith("各季累積雨量")||line_3.endsWith("各季累積雨量")) {
                    		Pattern pattern_season = Pattern.compile("\\d{4}年各季累積雨量");
                    		Matcher matcher_season_0 = pattern_season.matcher(line_0.replaceAll(" ", ""));
                    		Matcher matcher_season_1 = pattern_season.matcher(line_1.replaceAll(" ", ""));
                    		Matcher matcher_season_2 = pattern_season.matcher(line_2.replaceAll(" ", ""));
                    		Matcher matcher_season_3 = pattern_season.matcher(line_3.replaceAll(" ", ""));
                    		if(matcher_season_0.matches()||matcher_season_1.matches()||
                    				matcher_season_2.matches()||matcher_season_3.matches()) {
                    			destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[8], pdfPage_file, year - 1911);
                    		}
                        }
                        else if(line_0.endsWith("年總雨量")||line_1.endsWith("年總雨量")||
                        		line_2.endsWith("年總雨量")||line_3.endsWith("年總雨量")) {
                        	Pattern pattern_year = Pattern.compile("\\d{4}年總雨量");
                    		Matcher matcher_year_0 = pattern_year.matcher(line_0.replaceAll(" ", ""));
                    		Matcher matcher_year_1 = pattern_year.matcher(line_1.replaceAll(" ", ""));
                    		Matcher matcher_year_2 = pattern_year.matcher(line_2.replaceAll(" ", ""));
                    		Matcher matcher_year_3 = pattern_year.matcher(line_3.replaceAll(" ", ""));
                    		if(matcher_year_0.matches()||matcher_year_1.matches()||
                    				matcher_year_2.matches()||matcher_year_3.matches()) {
                    			destFilename = String.format(DestFilenameFormatTempRainfallChartDaily[9], pdfPage_file, year - 1911);
                    		}
                        }
                        break;
                        
                    default:
                    	destFilename = destFilenameFormat;
                        break;
                }
                
                // Add page sequence No.
                switch (key) {
                    case "typhoonFilePath":
                        destFilename += ((reader.getNumberOfPages() > 1)? "#" + pdfPage_specificTyphoon : "");
                        pdfPage_specificTyphoon++;
                        break;
                    case "tempRainFilePath":
                    case "monthlyTempRainFilePath":
                        destFilename += ((reader.getNumberOfPages() > 1)? "#" + pdfPage_monthlyTempRain : "");
                        pdfPage_monthlyTempRain++;
                        break;
                    default:
                        destFilename += ((reader.getNumberOfPages() > 1)? "#" + pdfPage_file : "");
                }
                
                // Start splitting and copying
                splitter.split(new FileOutputStream( outputTmpPdfPath + File.separator + destFilename + ".pdf"), 200000);
                
                // Add page number
                // ...
                
                // update counter
                processedPageCount++;
                pdfPage_file++;
            }
        }
        catch (IOException e) {
            throwable = e;
            System.err.println("[WARNING]: The file \'" + mOrderedOuterPdfFilePath.get(key) + "\'"
                    + " cannot be copied to the destination.");                    
        }
        catch (DocumentException e) {
            throwable = e;
            e.printStackTrace();
        }
        catch (Exception e) {
            throwable = e;
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        }
	}
    private static int processedPageCount = 1;
    private static OnPdfProductionCompleteListener pdfListener;
    private static OnCsvProductionCompleteListener csvListener;
    
    public static void setOnReportProductionCompleteListener(OnPdfProductionCompleteListener listener) {
        pdfListener = listener;
    }
   
    public static void setOnReportProductionCompleteListener(OnCsvProductionCompleteListener listener) {
        csvListener = listener;
    }
    
}