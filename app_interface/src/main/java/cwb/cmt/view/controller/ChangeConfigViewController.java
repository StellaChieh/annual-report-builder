package cwb.cmt.view.controller;

import org.apache.log4j.Logger;

import cwb.cmt.view.application.Main;
import cwb.cmt.view.utils.FileProcess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChangeConfigViewController {
	
	private String surfaceStn = Main.globalSurfaceStationPath;
	private String surfaceCE = Main.globalSurfaceCePath;
	private String upperairStn = Main.globalUpperairStationPath;
	private String summaryStn = Main.globalSummaryStationPath;
	private String summaryCE = Main.globalSummaryCePath;
	private static Logger logger = Logger.getLogger(ChangeConfigViewController.class);
	
	
	/*
	 * surface
	 */
	@FXML
    void onChangeSurfaceStationBtn(ActionEvent event) {
    	FileProcess.openFile(surfaceStn);
    	logger.info("change surface station config.");
    }
    
    
    @FXML
    void onChangeSurfaceClimaticElementBtn(ActionEvent event) {
    	FileProcess.openFile(surfaceCE);
    	logger.info("change surface climatic element config.");
    }
    
    
    /*
     * upperair
     */
    @FXML
    void onChangeUpperairStationBtn(ActionEvent event) {
    	FileProcess.openFile(upperairStn);
    	logger.info("change upperair station config.");
    }
    
    /*
     * summary
     */
    @FXML
    void onChangeSummaryStationBtn(ActionEvent event) {
    	FileProcess.openFile(summaryStn);
    	logger.info("change summary station config.");
    }
    
    @FXML
    void onChangeSummaryClimaticElementBtn(ActionEvent event) {
    	FileProcess.openFile(summaryCE);
    	logger.info("change summary climatic element config.");
    }
    
}
