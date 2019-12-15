package cwb.cmt.upperair.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cwb.cmt.upperair.utils.PdfPageType;

public class UpperairApplication {

	private static final Logger logger = LogManager.getLogger();
	private ApplicationContext context = null;
	private OnPdfProductionListener pdfListener;
	private OnCsvProductionListener csvListener;

	public UpperairApplication(String appConfigPath) throws FileNotFoundException, IOException {
		logger.info("init UpperairApplication");
		Properties appProps = new Properties();
		appProps.load(new FileInputStream(appConfigPath));
		String rootPath = appProps.getProperty("cmt.root.path").trim();
		String beanPath = appProps.getProperty("upperair.spring.bean.path").trim();
		context = new FileSystemXmlApplicationContext(Paths.get(rootPath, beanPath).toString());
	}

	public void createPdfReport(Map<PdfPageType, String> fileMap) {
		logger.info("Start to create pdf report.");
		CreatePdf c = (CreatePdf) context.getBean("createPdf");
		c.setOuterFileMap(fileMap);
		boolean createStatus = false;
		try {
			createStatus = c.create();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			closeSpringApplication();
			pdfListener.onPdfProductionComplete(createStatus);
		}
	}

	public void createCsv() {
		logger.info("Start to create csv.");
		CreateCsv c = (CreateCsv) context.getBean("createCsv");
		boolean createStatus = false;
		try {
			createStatus = c.create();
		} catch (Exception e) {
			logger.error(e);
		} finally {
			closeSpringApplication();
			csvListener.onCsvProductionComplete(createStatus);
		}
	}

	private void closeSpringApplication() {
		((AbstractApplicationContext) context).close();
		logger.info("UpperairApplication spring app was closed.");
	}

	/*
	 * for testing only
	 */
	public static void main(String[] args) throws Exception {
		long beginTime = System.currentTimeMillis();
		Map<PdfPageType, String> fileMap = new LinkedHashMap<>();
		fileMap.put(PdfPageType.COVER, "D:\\work\\repo\\annual-report-builder\\artifacts\\upperair\\封面.pdf");
		fileMap.put(PdfPageType.REFERENCE_NOTE, "D:\\work\\repo\\annual-report-builder\\artifacts\\upperair\\凡例.pdf");
		fileMap.put(PdfPageType.COPYRIGHT, "D:\\work\\repo\\annual-report-builder\\artifacts\\upperair\\版權頁.pdf");

		// load properties file using apache commons configuration2
		org.apache.commons.configuration2.builder.fluent.Parameters params = new org.apache.commons.configuration2.builder.fluent.Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
				PropertiesConfiguration.class).configure(
						params.properties().setFileName("D:/work/repo/annual-report-builder/config.properties"));
		
		Configuration config = builder.getConfiguration();
		config.setProperty("cmt.root.path", "D:/work/repo/annual-report-builder");
		for (int year = 2017; year <= 2018; year++) {
			for (int month = 1; month <= 12; month++) {
				config.setProperty("cmt.year", year);
				config.setProperty("cmt.month", month);
//				config.setProperty("upperair.output.folder", "D:/Desktop/report/test/"+ String.format("%2d", month));
				builder.save();
				UpperairApplication application = new UpperairApplication("D:/work/repo/annual-report-builder/config.properties");
//				application.createCsv();
				application.createPdfReport(fileMap);
			}	
		}
		
		// application.createPdfReport(fileMap);
		// application.createCsvTest();
		long endTime = System.currentTimeMillis();
		System.out.println("totla " + (endTime - beginTime) / 1000 + " seconds");
	}

	public void setOnPdfProductionListener(OnPdfProductionListener mListener) {
		this.pdfListener = mListener;
	};

	public void setOnCsvProductionListener(OnCsvProductionListener mListener) {
		this.csvListener = mListener;
	}

}