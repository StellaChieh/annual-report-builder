package cwb.cmt.view.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import cwb.cmt.summary.main.SummaryApplication;
import cwb.cmt.view.application.Main;
import cwb.cmt.view.utils.OpenFileExplorer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class GSummaryRptViewController implements Initializable{
	
	@FXML
    private TextField cover;

	@FXML
	private TextField example;
	
	@FXML
	private TextField introduction;
	
	@FXML
    private TextField copyright;
	
	private static final String DIALOGUE_TITLE = "小提示";
    private static final String DIALOGUE_CONTENTS_UPLOAD_PDF_ALERT = "請上傳所有PDF檔";
    private static final String DIALOGUE_CONTENTS_FILE_UNVALID = "此檔案不存在: ";
    private static final String DIALOGUE_CONTENTS_RPT_GENERATING = "彙編PDF產製中";
    private static final String DIALOGUE_CONTENTS_RPT_GENERATION_SUCCESS = "彙編PDF已產製完成";
    private static final String DIALOGUE_CONTENTS_RPT_GENERATION_FAIL = "彙編PDF產製失敗";
    private static final String PROP_COVER = "summary.cover";
    private static final String PROP_EXAMPLE = "summary.example";
    private static final String PROP_INTRODUCTION = "summary.introduction";
    private static final String PROP_COPYRIGHT = "summary.copyright";

    private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Parameters params = new Parameters();
    	builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
    				.configure(params.properties().setFileName(Main.globalInterfaceDefaultFilePath));
		try {
			Configuration config = builder.getConfiguration();
			cover.setText(config.getString(PROP_COVER));
			example.setText(config.getString(PROP_EXAMPLE));
			introduction.setText(config.getString(PROP_INTRODUCTION));
			copyright.setText(config.getString(PROP_COPYRIGHT));
		} catch (ConfigurationException e) {
			// TODO
			e.printStackTrace();
		}
	}
    
	@FXML
    void onGenerateSummaryReportBtn(ActionEvent event) {
		
		String coverS = cover.getText().trim();
		String exampleS = example.getText().trim();
		String introductionS = introduction.getText().trim();
		String copyrightS = copyright.getText().trim();
		
		// check if all the pdfs are uploaded
		if(coverS.isEmpty() 
				|| exampleS .isEmpty()
				|| introductionS.isEmpty() 
				|| copyrightS.isEmpty()){
			final Alert alertUploadPdf = new Alert(AlertType.INFORMATION); 
    		alertUploadPdf.setTitle(DIALOGUE_TITLE); 
    		alertUploadPdf.setHeaderText(""); 
    		alertUploadPdf.setContentText(DIALOGUE_CONTENTS_UPLOAD_PDF_ALERT); 
    		//only after the user close the dialogue does the program continue executing
    		alertUploadPdf.showAndWait();
    		return;
		}
		
		
		List<String> unvalidFile = getUnvalidFiles(Arrays.asList(coverS, exampleS, introductionS, copyrightS));
		if(unvalidFile.size() > 0) {
    		StringJoiner msg = new StringJoiner("\n");
    		msg.add(DIALOGUE_CONTENTS_FILE_UNVALID);
    		for(String s : unvalidFile) {
    			msg.add(s);
    		}
    		final Alert alertUploadPdf = new Alert(AlertType.INFORMATION);
    		alertUploadPdf.setTitle(DIALOGUE_TITLE);
    		alertUploadPdf.setHeaderText("");
    		alertUploadPdf.setContentText(msg.toString());
    		//only after the user close the dialogue does the program continue executing
    		alertUploadPdf.showAndWait();
    		return;
    	}
		
		try {
			Configuration config = builder.getConfiguration();
			config.setProperty(PROP_COVER, coverS);
			config.setProperty(PROP_EXAMPLE, exampleS);
			config.setProperty(PROP_INTRODUCTION, introductionS);
			config.setProperty(PROP_COPYRIGHT, copyrightS);
			builder.save();
		} catch (ConfigurationException e) {
			// TODO
			e.printStackTrace();
		}
		
		// show report generating alert
		final Alert notifyRptGenerating = new Alert(AlertType.INFORMATION); // initiate alert dialogue
		notifyRptGenerating.setTitle(DIALOGUE_TITLE);                       // set dialogue title bar 
		notifyRptGenerating.setHeaderText("");                              // set dialogue content title. empty string means no title
		notifyRptGenerating.setContentText(DIALOGUE_CONTENTS_RPT_GENERATING); // set dialogue content 
		notifyRptGenerating.show();
		
		boolean generated = false;
		
		Map<String, String> params = new HashMap<>();
		params.put("cover", coverS);
		params.put("example", exampleS);
		params.put("introduction", introductionS);
		params.put("copyright", copyrightS);
		
		try {
			generated = SummaryApplication.init(Main.globalPropertiesPath)
											.createReport(params);
		} catch (Exception e) {
			notifyRptGenerating.close();
    		final Alert notifyRptDone = new Alert(AlertType.INFORMATION); 
    		notifyRptDone.setTitle(DIALOGUE_TITLE); 
    		notifyRptDone.setHeaderText(""); 
    		notifyRptDone.setContentText(DIALOGUE_CONTENTS_RPT_GENERATION_FAIL); 
    		//only after the user close the dialogue does the program continue executing
    		notifyRptDone.showAndWait();
			e.printStackTrace();
		}
		
		// if report generated done, show notice 
		if(generated == true){
    		notifyRptGenerating.close();
    		final Alert notifyRptDone = new Alert(AlertType.INFORMATION); 
    		notifyRptDone.setTitle(DIALOGUE_TITLE); 
    		notifyRptDone.setHeaderText(""); 
    		notifyRptDone.setContentText(DIALOGUE_CONTENTS_RPT_GENERATION_SUCCESS); 
    		//only after the user close the dialogue does the program continue executing
    		notifyRptDone.showAndWait(); 
    	};
				
	}
	

    @FXML
    void onSummaryCopy(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		cover.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onSummaryExample(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		example.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onSummaryIntroduction(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		introduction.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onSummaryCopyright(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		copyright.setText(file.getAbsolutePath());
    	}
    }
    
    private List<String> getUnvalidFiles(List<String> paths){
    	List<String> list = new ArrayList<>();
    	for(String path :paths) {
    		if(!Files.exists(Paths.get(path))) {
        		list.add(path);
        	}
    	}
    	return list;
    }

}

