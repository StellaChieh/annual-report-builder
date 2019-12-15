package cwb.cmt.summary.main;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cwb.cmt.summary.process.ProcessCliEle;
import cwb.cmt.summary.process.ProcessContents;
import cwb.cmt.summary.process.ProcessData;
import cwb.cmt.summary.process.ProcessInterleaf;
import cwb.cmt.summary.process.ProcessPartOneContents;
import cwb.cmt.summary.process.ProcessPartTwoContents;
import cwb.cmt.summary.process.ProcessPdf;
import cwb.cmt.summary.process.ProcessStnCliEle;
import cwb.cmt.summary.process.ProcessStnMoves;
import cwb.cmt.summary.process.ProcessStns;


public class SummaryApplication {

	private static Logger log = Logger.getLogger(SummaryApplication.class);
	private static ApplicationContext context = null;
	
	private ProcessCliEle processCliEle;
	private ProcessContents processContents;
	private ProcessPartOneContents processPartOneContents;
	private ProcessPartTwoContents processPartTwoContents;
	private ProcessData processData;
	private ProcessStnCliEle processStnCliEle;
	private ProcessStns processStns;
	private ProcessInterleaf processInterleaf;
	private ProcessPdf processPdf;
	private ProcessStnMoves processStnMoves;
	
	public static ApplicationContext getApplicationContext() {
		return context;
	}
	
	public static SummaryApplication init(String appConfigPath) throws Exception {
		Properties appProps = new Properties();
		appProps.load(new FileInputStream(appConfigPath));
		String rootPath = appProps.getProperty("cmt.root.path").trim();
		String beanPath = appProps.getProperty("summary.spring.bean.path").trim();
		context = new FileSystemXmlApplicationContext(Paths.get(rootPath, beanPath).toString());
		return new SummaryApplication();
	}		
	
	private void processData(){
		processData = (ProcessData) context.getBean("processData");
		processData.run();
	}
	
	// 測站一覽表
	private void createStns(){
		processStns = (ProcessStns) context.getBean("processStns");
		processStns.run();
	}
	
	// 變遷記錄一覽表
	private void createStnMoves(){
		processStnMoves = (ProcessStnMoves) context.getBean("processStnMoves");
		processStnMoves.run();
	}
	
	// 總目錄
	private void createContents(){
		processContents = (ProcessContents) context.getBean("processContents");
		processContents.run();
	}
	
	// 第一部分目錄
	private void createPartOneContents(){
		processPartOneContents = (ProcessPartOneContents) context.getBean("processPartOneContents");
		processPartOneContents.run();
	}
	
	// 第二部分目錄
	private void createPartTwoContents(){
		processPartTwoContents = (ProcessPartTwoContents) context.getBean("processPartTwoContents");
		processPartTwoContents.run();
	}

	// 第一部分表格
	private void createCliEle(){
		processCliEle = (ProcessCliEle) context.getBean("processCliEle");
		processCliEle.run();
	}
	
	// 第二部分表格
	private void createStnCliEle(){
		processStnCliEle = (ProcessStnCliEle) context.getBean("processStnCliEle");
		processStnCliEle.run();
	}
	
	// 產製插頁
	private void createInterleaf(){
		processInterleaf = (ProcessInterleaf) context.getBean("processInterleaf");
		processInterleaf.run();
	}
		
	private void prepareProcessPdf(){
		processPdf = (ProcessPdf) context.getBean("processPdf");
	}
	
	// 印頁碼、做頁籤、合併
	private void processPdf(Map<String, String> outerFilenames){
		processPdf.setOuterFilesPath(outerFilenames);
		processPdf.run();
	}
	
	// 清除暫存資料夾
	private void cleanTmpFolder(){
		processPdf.cleanTmpFolder();
	}
	
	private void clearSpringContext() {
		((AbstractApplicationContext)context).close();
	}
	
	// 產製整本書
	public boolean createReport(Map<String, String> filePathParams){
		long startTime = System.currentTimeMillis();
		log.info("Start to generate report...");
		prepareProcessPdf();
		cleanTmpFolder();
		createCliEle();
		createStnCliEle();
		createStnMoves();
		createStns();
		createContents();
		createPartOneContents();
		createPartTwoContents();
		createInterleaf();
		processPdf(filePathParams);
		log.info("Generate report completed...");
		long duration = (System.currentTimeMillis()-startTime)/1000;
		log.info("Spend " + duration + " seconds.");
		clearSpringContext();
		return true;
	}
	
	
	// update intermediate db table
	public boolean updateDb(){
		long startTime = System.currentTimeMillis();
		log.info("Start to write to intermediate db...");
		processData();
		log.info("Write to intermediate completed...");
		long duration = (System.currentTimeMillis() - startTime) / 1000;
		log.info("Spend " + duration + " seconds.");
		clearSpringContext();
		return true;
	}
	
	
	/*
	 *  for testing only
	 */
	public static void main(String[] args) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("cover", "D:\\work\\repo\\annual-report-builder\\artifacts\\summary\\封面.pdf");
		params.put("example", "D:\\work\\repo\\annual-report-builder\\artifacts\\summary\\凡例.pdf");
		params.put("introduction", "D:\\work\\repo\\annual-report-builder\\artifacts\\summary\\introduction.pdf");
		params.put("copyright", "D:\\work\\repo\\annual-report-builder\\artifacts\\summary\\版權頁.pdf");
		
		SummaryApplication s = SummaryApplication.init("D:\\work\\repo\\annual-report-builder\\config.properties");
		s.updateDb();
//		s.createReport(params);
		
	}
}
