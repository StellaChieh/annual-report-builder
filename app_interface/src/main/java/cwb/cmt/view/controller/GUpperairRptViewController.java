package cwb.cmt.view.controller;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import cwb.cmt.upperair.main.OnCsvProductionListener;
import cwb.cmt.upperair.main.OnPdfProductionListener;
import cwb.cmt.upperair.main.UpperairApplication;
import cwb.cmt.upperair.utils.PdfPageType;
import cwb.cmt.view.application.Main;
import cwb.cmt.view.utils.FileProcess;
import cwb.cmt.view.utils.OpenFileExplorer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class GUpperairRptViewController implements Initializable {
	
	@FXML
    private TextField upperairCoverFilePath;
	
	@FXML
    private TextField upperairReferrenceFilePath;
	
    @FXML
    private TextField upperairCopyrightFilePath;
    
    private static final String DIALOGUE_TITLE = "小提示";
    private static final String DIALOGUE_CONTENTS_UPLOAD_PDF_ALERT = "請上傳所有PDF檔";
    private static final String DIALOGUE_CONTENTS_FILE_UNVALID = "此檔案不存在：";
    private static final String DIALOGUE_CONTENTS_PDF_GENERATING = "高空PDF產製中";
    private static final String DIALOGUE_CONTENTS_PDF_GENERATION_SUCCESS = "高空PDF已產製完成";
    private static final String DIALOGUE_CONTENTS_PDF_GENERATION_FAIL = "高空PDF產製失敗";
    private static final String DIALOGUE_CONTENTS_CSV_GENERATING = "高空CSV產製中";
    private static final String DIALOGUE_CONTENTS_CSV_GENERATION_SUCCESS = "高空CSV已產製完成";
    private static final String DIALOGUE_CONTENTS_CSV_GENERATION_FAIL = "高空CSV產製失敗";
    
    private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
    private static final String PROP_COVER = "upperair.cover";
    private static final String PROP_REFERRENCE = "upperair.referrence";
    private static final String PROP_COPYRIGHT = "upperair.copyright";
    
    private Alert notifyPdfGenerating;
    private Alert notifyCsvGenerating;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		Parameters params = new Parameters();
    	builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
    				.configure(params.properties().setFileName(Main.globalInterfaceDefaultFilePath));
    	try {
			Configuration config = builder.getConfiguration();
			this.upperairCoverFilePath.setText(config.getString(PROP_COVER));
			this.upperairReferrenceFilePath.setText(config.getString(PROP_REFERRENCE));
			this.upperairCopyrightFilePath.setText(config.getString(PROP_COPYRIGHT));
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    @FXML
    void onGenerateUpperairReportBtn(ActionEvent event) {
    	
		final String cover = upperairCoverFilePath.getText().trim();
		final String referrence = upperairReferrenceFilePath.getText().trim();
		final String copyright = upperairCopyrightFilePath.getText().trim();
    	// check if the user upload all needed pdfs
    	if (cover.isEmpty() 
    			|| referrence.isEmpty() 
    			|| copyright.isEmpty()){
    		final Alert alertUploadPdf = new Alert(AlertType.INFORMATION); 
    		alertUploadPdf.setTitle(DIALOGUE_TITLE); 
    		alertUploadPdf.setHeaderText(""); 
    		alertUploadPdf.setContentText(DIALOGUE_CONTENTS_UPLOAD_PDF_ALERT); 
    		//only after the user close the dialogue does the program continue executing
    		alertUploadPdf.showAndWait();
    		return;
    	}
    	
    	// check file exists
    	List<String> unvalidFiles = FileProcess.getUnvalidFiles(Arrays.asList(cover, referrence, copyright));
		if(unvalidFiles.size() > 0) {
			StringJoiner str = new StringJoiner("\n");
			str.add(DIALOGUE_CONTENTS_FILE_UNVALID);
			for(String s : unvalidFiles) {
				str.add(s);
			}
			final Alert alertUploadPdf = new Alert(AlertType.INFORMATION); 
    		alertUploadPdf.setTitle(DIALOGUE_TITLE); 
    		alertUploadPdf.setHeaderText(""); 
    		alertUploadPdf.setContentText(str.toString()); 
    		alertUploadPdf.showAndWait();
    		return;
		}
		
		// write file path to interface config
		try {
			Configuration config = builder.getConfiguration();
			config.setProperty(PROP_COVER, cover);
			config.setProperty(PROP_REFERRENCE, referrence);
			config.setProperty(PROP_COPYRIGHT, copyright);
			builder.save();
		} catch(ConfigurationException cex) {
    	    // loading of the configuration file failed
    		cex.printStackTrace();
    	}
		
		// generate pdf report
		Map<PdfPageType, String> fileMap = new LinkedHashMap<>();
		fileMap.put(PdfPageType.COVER, cover);
		fileMap.put(PdfPageType.REFERENCE_NOTE, referrence);
		fileMap.put(PdfPageType.COPYRIGHT, copyright);
		
		// show report generating alert
		notifyPdfGenerating = new Alert(AlertType.INFORMATION); // initiate alert dialogue
		notifyPdfGenerating.setTitle(DIALOGUE_TITLE);                       // set dialogue title bar 
		notifyPdfGenerating.setHeaderText("");                              // set dialogue content title. empty string means no title
		notifyPdfGenerating.setContentText(DIALOGUE_CONTENTS_PDF_GENERATING);// set dialogue content 
		notifyPdfGenerating.show();
		try {
			UpperairApplication app = new UpperairApplication(Main.globalPropertiesPath);
			app.setOnPdfProductionListener(pdfListener);
			app.createPdfReport(fileMap);															
		} catch (Exception e) {
			e.printStackTrace();
		}	 	
    }
    
    private final OnPdfProductionListener pdfListener = new OnPdfProductionListener() {

		@Override
		public void onPdfProductionComplete(boolean successState) {
			notifyPdfGenerating.close();
    		final Alert notifyRptDone = new Alert(AlertType.INFORMATION);
    		notifyRptDone.setTitle(DIALOGUE_TITLE);
    		notifyRptDone.setHeaderText("");
    		if(successState) {
    			notifyRptDone.setContentText(DIALOGUE_CONTENTS_PDF_GENERATION_SUCCESS);
    		} else {
    			notifyRptDone.setContentText(DIALOGUE_CONTENTS_PDF_GENERATION_FAIL);
    		}
    		notifyRptDone.showAndWait();
		}
    };
    
    
    @FXML
    void onGenerateUpperairCsvBtn(ActionEvent event) {
    	notifyCsvGenerating = new Alert(AlertType.INFORMATION); // initiate alert dialogue
		notifyCsvGenerating.setTitle(DIALOGUE_TITLE);                       // set dialogue title bar 
		notifyCsvGenerating.setHeaderText("");                              // set dialogue content title. empty string means no title
		notifyCsvGenerating.setContentText(DIALOGUE_CONTENTS_CSV_GENERATING);// set dialogue content 
		notifyCsvGenerating.show();
    	try {
			UpperairApplication app = new UpperairApplication(Main.globalPropertiesPath);
			app.setOnCsvProductionListener(csvListener);
			app.createCsv();													
		} catch (Exception e) {
			e.printStackTrace();
		}	 	
    }
    
    private final OnCsvProductionListener csvListener = new OnCsvProductionListener() {

		@Override
		public void onCsvProductionComplete(boolean successState) {
			notifyCsvGenerating.close();
    		final Alert notifyCsvDone = new Alert(AlertType.INFORMATION);
    		notifyCsvDone.setTitle(DIALOGUE_TITLE);
    		notifyCsvDone.setHeaderText("");
    		if(successState) {
    			notifyCsvDone.setContentText(DIALOGUE_CONTENTS_CSV_GENERATION_SUCCESS);
    		} else {
    			notifyCsvDone.setContentText(DIALOGUE_CONTENTS_CSV_GENERATION_FAIL);
    		}
    		notifyCsvDone.showAndWait();
		}
    	
    };
    
    
    @FXML
    void onUpperairCover(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		upperairCoverFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onUpperairReferrence(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		upperairReferrenceFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onUpperairCopyright(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		upperairCopyrightFilePath.setText(file.getAbsolutePath());
    	}
    }    
}
