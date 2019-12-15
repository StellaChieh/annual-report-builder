package cwb.cmt.view.controller;

import java.io.File;
import java.io.IOException;
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

import cwb.cmt.surface.main.SurfaceApplication;
import cwb.cmt.surface.tools.OnCsvProductionCompleteListener;
import cwb.cmt.surface.tools.OnPdfProductionCompleteListener;
import cwb.cmt.view.application.Main;
import cwb.cmt.view.utils.FileProcess;
import cwb.cmt.view.utils.OpenFileExplorer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class GSurfaceRptViewController implements Initializable {

	private static final String DIALOGUE_TITLE = "小提示";
    private static final String DIALOGUE_CONTENTS_UPLOAD_PDF_ALERT = "請上傳所有PDF檔(颱風可不上傳)";
    private static final String DIALOGUE_CONTENTS_FILE_UNVALID = "此檔案不存在：";
    private static final String DIALOGUE_CONTENTS_PDF_GENERATING = "地面PDF產製中";
    private static final String DIALOGUE_CONTENTS_PDF_GENERATION_SUCCESS = "地面PDF已產製完成";
    private static final String DIALOGUE_CONTENTS_PDF_GENERATION_FAIL = "地面PDF產製失敗";
    private static final String DIALOGUE_CONTENTS_CSV_GENERATING = "地面CSV產製中";
    private static final String DIALOGUE_CONTENTS_CSV_GENERATION_SUCCESS = "地面CSV已產製完成";
    private static final String DIALOGUE_CONTENTS_CSV_GENERATION_FAIL = "地面CSV產製失敗";
    
    private static final String PROP_EXAMPLE="surface.example";
    private static final String PROP_STN_MAP ="surface.stnMap";
    private static final String PROP_COVER = "surface.cover";
    private static final String PROP_TYPHOON="surface.typhoon";
    private static final String PROP_MONTHLY_TEMP_RAIN="surface.monthlyTempRain";
    private static final String PROP_TEMP_RAIN="surface.tempRain";
    private static final String PROP_EXPLANATION="surface.explanation";
    private static final String PROP_DISTRICTS="surface.districts";
    private static final String PROP_COPYRIGHT="surface.copyright";
    private static final String PROP_AUTO_STN_MAP="surface.autoStnMap";
    
    FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

    @FXML
    private TextField coverFilePath;   // 封面
    
	@FXML
    private TextField exampleFilePath;  // 凡例
	
    @FXML
    private TextField explanationFilePath;  // 其他說明

    @FXML
    private TextField stnMapFilePath;  // 綜觀氣象站-測站分布圖

    @FXML
    private TextField districtsFilePath;  // 綜觀及自動觀測站年表-台灣行政區域概圖
    
    @FXML
    private TextField autoStnMapFilePath;   // 綜觀及自動觀測站年表-自動雨量及氣象站分布圖

    @FXML
    private TextField typhoonFilePath;  // 颱風雨量圖

    @FXML
    private TextField tempRainFilePath;  //日溫度雨量圖
    
    @FXML
    private TextField monthlyTempRainFilePath;    //月季年溫度雨量圖

    @FXML
    private TextField copyrightFilePath;  //版權頁

    
    private Alert notifyPdf;
    private Alert notifyCsv;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Parameters params = new Parameters();
    	builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
    				.configure(params.properties().setFileName(Main.globalInterfaceDefaultFilePath));
    	try {
			Configuration config = builder.getConfiguration();
			coverFilePath.setText(config.getString(PROP_COVER));
			exampleFilePath.setText(config.getString(PROP_EXAMPLE));
			explanationFilePath.setText(config.getString(PROP_EXPLANATION));
			stnMapFilePath.setText(config.getString(PROP_STN_MAP));
			coverFilePath.setText(config.getString(PROP_COVER));
			districtsFilePath.setText(config.getString(PROP_DISTRICTS));
			autoStnMapFilePath.setText(config.getString(PROP_AUTO_STN_MAP));
			typhoonFilePath.setText(config.getString(PROP_TYPHOON));
			tempRainFilePath.setText(config.getString(PROP_TEMP_RAIN));
			monthlyTempRainFilePath.setText(config.getString(PROP_MONTHLY_TEMP_RAIN));
			copyrightFilePath.setText(config.getString(PROP_COPYRIGHT));
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
    
	
    @FXML
    void onGntRpt(ActionEvent event) throws Exception {

    	String cover = coverFilePath.getText().trim();
    	String example = exampleFilePath.getText().trim();
    	String explanation = explanationFilePath.getText().trim();
    	String stnMap = stnMapFilePath.getText().trim();
    	String districts = districtsFilePath.getText().trim();
    	String autoStnMap = autoStnMapFilePath.getText().trim();
    	String typhoon = typhoonFilePath.getText().trim();
    	String tempRain = tempRainFilePath.getText().trim();
    	String monthlyTempRain = monthlyTempRainFilePath.getText().trim();
    	String copyright = copyrightFilePath.getText().trim();
    	
    	// check if the user upload all needed pdf
    	// typhoon is allowed to be empty
    	if (cover.isEmpty() 
    			|| example.isEmpty() 
    			|| explanation.isEmpty() 
    			|| stnMap.isEmpty()
    			|| districts.isEmpty() 
    			|| autoStnMap.isEmpty()
    			|| tempRain.isEmpty()
    			|| monthlyTempRain.isEmpty() 
    			|| copyright.isEmpty()){
    		final Alert alertUploadPdf = new Alert(AlertType.INFORMATION);
    		alertUploadPdf.setTitle(DIALOGUE_TITLE);
    		alertUploadPdf.setHeaderText("");
    		alertUploadPdf.setContentText(DIALOGUE_CONTENTS_UPLOAD_PDF_ALERT);
    		//only after the user close the dialogue does the program continue executing
    		alertUploadPdf.showAndWait();
    		return;
    	}
    	
    	// check if files exist
    	List<String> unvalidFile = FileProcess.getUnvalidFiles(Arrays.asList(cover, example, explanation
    												,stnMap, districts, autoStnMap, typhoon
    												, tempRain, monthlyTempRain, copyright));
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
    	
    	// write to interface default file path
    	try {
			Configuration config = builder.getConfiguration();
			config.setProperty(PROP_COVER, cover);
			config.setProperty(PROP_EXAMPLE, example);
			config.setProperty(PROP_EXPLANATION, explanation);
			config.setProperty(PROP_STN_MAP, stnMap);
			config.setProperty(PROP_DISTRICTS, districts);
			config.setProperty(PROP_AUTO_STN_MAP, autoStnMap);
			config.setProperty(PROP_TYPHOON, typhoon);
			config.setProperty(PROP_TEMP_RAIN, tempRain);
			config.setProperty(PROP_MONTHLY_TEMP_RAIN, monthlyTempRain);
			config.setProperty(PROP_COPYRIGHT, copyright);
			builder.save();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	// generate report
    	Map<String, String> filePathParams = null;
		filePathParams = new HashMap<>();
		filePathParams.put("coverFilePath", cover);
		filePathParams.put("exampleFilePath", example);
		filePathParams.put("explanationFilePath", explanation);
		filePathParams.put("stnMapFilePath", stnMap);
		filePathParams.put("districtsFilePath", districts);
		filePathParams.put("autoStnMapFilePath", autoStnMap);
		if(!typhoon.isEmpty()) {
			filePathParams.put("typhoonFilePath", typhoon);
		}
		filePathParams.put("tempRainFilePath", tempRain);
		filePathParams.put("monthlyTempRainFilePath", monthlyTempRain);
		filePathParams.put("copyrightFilePath", copyright);
		
	
		notifyPdf = new Alert(AlertType.INFORMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
		notifyPdf.setTitle(DIALOGUE_TITLE); //設定對話框視窗的標題列文字
		notifyPdf.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
		notifyPdf.setContentText(DIALOGUE_CONTENTS_PDF_GENERATING); //設定對話框的訊息文字
		notifyPdf.show();

		try {
			SurfaceApplication.setOnReportProductionCompleteListener(pdfListener);
			SurfaceApplication.createPdfReport(filePathParams, Main.globalPropertiesPath);
		} catch (Exception e) {
			notifyPdf.close();
    		final Alert notifyRptDone = new Alert(AlertType.INFORMATION);
    		notifyRptDone.setTitle(DIALOGUE_TITLE);
    		notifyRptDone.setHeaderText("");
    		notifyRptDone.setContentText(DIALOGUE_CONTENTS_PDF_GENERATION_FAIL);
    		//only after the user close the dialogue does the program continue executing
    		notifyRptDone.showAndWait();
    		e.printStackTrace();
		}
    }

    
    private final OnPdfProductionCompleteListener pdfListener = new OnPdfProductionCompleteListener() {

		@Override
		public void onReportProductionComplete(String name) {
			System.out.println("onReportProductionComplete(), name: " + name);
		}

		@Override
		public void onAllReportProductionComplete() {
			notifyPdf.close();
    		final Alert notifyRptDone = new Alert(AlertType.INFORMATION);
    		notifyRptDone.setTitle(DIALOGUE_TITLE);
    		notifyRptDone.setHeaderText("");
    		notifyRptDone.setContentText(DIALOGUE_CONTENTS_PDF_GENERATION_SUCCESS);
    		//only after the user close the dialogue does the program continue executing
    		notifyRptDone.showAndWait();
		}
    };
    
    
    @FXML
    void onGntCsv(ActionEvent event) throws Exception {
		notifyCsv = new Alert(AlertType.INFORMATION); // 實體化Alert對話框物件，並直接在建構子設定對話框的訊息類型
		notifyCsv.setTitle(DIALOGUE_TITLE); //設定對話框視窗的標題列文字
		notifyCsv.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
		notifyCsv.setContentText(DIALOGUE_CONTENTS_CSV_GENERATING); //設定對話框的訊息文字
		notifyCsv.show();
		try {
			SurfaceApplication.setOnReportProductionCompleteListener(csvListener);
			SurfaceApplication.createCsvReport(Main.globalPropertiesPath);
		} catch (Exception e) {
			notifyCsv.close();
    		final Alert notifyCsvFailed = new Alert(AlertType.INFORMATION);
    		notifyCsvFailed.setTitle(DIALOGUE_TITLE);
    		notifyCsvFailed.setHeaderText("");
    		notifyCsvFailed.setContentText(DIALOGUE_CONTENTS_CSV_GENERATION_FAIL);
    		//only after the user close the dialogue does the program continue executing
    		notifyCsvFailed.showAndWait();
    		e.printStackTrace();
		}
	}
    
    private final OnCsvProductionCompleteListener csvListener = new OnCsvProductionCompleteListener() {

		@Override
		public void onReportProductionComplete(String name) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAllReportProductionComplete() {
			notifyCsv.close();
    		final Alert notifyCsvDone = new Alert(AlertType.INFORMATION);
    		notifyCsvDone.setTitle(DIALOGUE_TITLE);
    		notifyCsvDone.setHeaderText("");
    		notifyCsvDone.setContentText(DIALOGUE_CONTENTS_CSV_GENERATION_SUCCESS);
    		//only after the user close the dialogue does the program continue executing
    		notifyCsvDone.showAndWait();
		}
    	
    };
    
   
    @FXML
    void onTempRain(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		tempRainFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onMonthlyTempRain(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		monthlyTempRainFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onCover(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		coverFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onExample(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		exampleFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onExplanation(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		explanationFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onStnMap(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		stnMapFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onDistricts(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		districtsFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onAutoStnMap(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		autoStnMapFilePath.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void onTyphoon(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		typhoonFilePath.setText(file.getAbsolutePath());
    	}
    }


    @FXML
    void onCopyright(ActionEvent event) {
    	File file = OpenFileExplorer.selectPdf(event);
    	if(file != null){
    		copyrightFilePath.setText(file.getAbsolutePath());
    	}
    }
    
}
