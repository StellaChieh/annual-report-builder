package cwb.cmt.view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.TextFlow;

public class MyController implements Initializable {
	
	@FXML 
	private TabPane tabPane;
	
	/*
	 *  inject other tab and their controller
	 *  also set their relationship in view.fxml
	 */
	@FXML
	private Tab settingTab;

	@FXML
	private SettingViewController settingViewController;
	
	@FXML
	private Tab gSurfaceRptTab;

	@FXML
	private GSurfaceRptViewController gSurfaceRptViewController;
	
	@FXML
	private Tab gUpperairRptTab;
	
	@FXML
	private GUpperairRptViewController gUpperairRptViewController;
	
	@FXML
	private Tab gSummaryRptTab;
	
	@FXML
	private GSummaryRptViewController gSummaryRptViewController;
	
	@FXML
	private Tab gSubRptTab;
	
	@FXML
	private GSubRptViewController gSubRptViewController;
	
	@FXML
	private Tab changeConfigTab;
	
	@FXML
	private ChangeConfigViewController changeConfigViewController;
	
	
	@FXML
    private TextFlow description;
	
		
	@Override
    public void initialize(URL url, ResourceBundle rb) {
    
	}
	
	
	
	   
}
