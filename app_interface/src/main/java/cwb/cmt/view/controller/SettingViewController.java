package cwb.cmt.view.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import cwb.cmt.summary.main.SummaryApplication;
import cwb.cmt.view.application.Main;
import cwb.cmt.view.utils.FileProcess;
import cwb.cmt.view.utils.OpenFileExplorer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SettingViewController implements Initializable {

	private static final String DIALOGUE_TITLE = "小提示";
	private static final String DIALOGUE_SUMMARY_DB_WRITING = "彙編寫入中繼資料表中";
	private static final String DIALOGUE_SUMMARY_DB_WRITING_DONE = "彙編寫入中繼資料表已完成";
	private static final String DIALOGUE_CONFIG_UPDATED = "修改成功";
	
	private final static String JDBC = "jdbc:mysql://";
	private final static String DB = "/cmt?zeroDataTimeBehavior=convertToNull&useSSL=false&autoReconnect=true";
	private final static String PROP_URL = "cmt.jdbc.url";
	private final static String PROP_USERNAME = "cmt.jdbc.username";
	private final static String PROP_PASSWORD = "cmt.jdbc.password";
	// private final static String DRIVER = "cmt.jdbc.driver";
	private final static String PROP_YEAR = "cmt.year";
	private final static String PROP_MONTH = "cmt.month";
	private final static String PROP_SUMMARY_YEAR = "cmt.summary.year";
	private final static String PROP_SURFACE_OUTPUT_FOLDER = "surface.output.folder";
	private final static String PROP_UPPERAIR_OUTPUT_FOLDER = "upperair.output.folder";
	private final static String PROP_SUMMARY_OUTPUT_FOLDER = "summary.output.folder";

	private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

	@FXML
	private TextField rptYear;

	@FXML
	private PasswordField password;

	@FXML
	private TextField upperairPath;

	@FXML
	private TextField ip;

	@FXML
	private TextField summaryYear;

	@FXML
	private TextField surfacePath;

	@FXML
	private TextField account;

	@FXML
	private TextField summaryPath;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		int year;
		int month;
		if (YearMonth.now().getMonthValue() == 1) {
			year = YearMonth.now().getYear() - 1;
			month = 12;
		} else {
			year = YearMonth.now().getYear();
			month = YearMonth.now().getMonthValue() - 1;
		}

		int summaryYearConfig;
		if (YearMonth.now().getYear() % 10 == 0) {
			// if this is 2020, we produce 2001-2010 report
			summaryYearConfig = (YearMonth.now().getYear() / 10 - 2) * 10 + 1;
		} else {
			// if this is 2021, we produce 2011-2020 report
			summaryYearConfig = (YearMonth.now().getYear() / 10 - 1) * 10 + 1;
		}

		// load properties file using apache commons configuration2
		Parameters params = new Parameters();
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(params.properties().setFileName(Main.globalPropertiesPath));
		try {
			Configuration config = builder.getConfiguration();
			rptYear.setText(year + "-" + String.format("%02d", month));
			summaryYear.setText(String.valueOf(summaryYearConfig));
			surfacePath.setText(config.getString(PROP_SURFACE_OUTPUT_FOLDER));
			upperairPath.setText(config.getString(PROP_UPPERAIR_OUTPUT_FOLDER));
			summaryPath.setText(config.getString(PROP_SUMMARY_OUTPUT_FOLDER));
			// cmt.jdbc.url=jdbc:mysql://127.0.0.1/cmt?zeroDataTimeBehavior=convertToNull&useSSL=false&autoReconnect=true
			ip.setText(config.getString(PROP_URL).split("//")[1].split("/")[0]);
			account.setText(config.getString(PROP_USERNAME));
			password.setText(config.getString(PROP_PASSWORD));
			// save default this year and default last month into config file
			config.setProperty(PROP_YEAR, year);
			config.setProperty(PROP_MONTH, month);
			config.setProperty(PROP_SUMMARY_YEAR, summaryYearConfig);
			builder.save();

		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<String> unvalidDirs = FileProcess.getUnvalidDirs(
				Arrays.asList(surfacePath.getText().trim(), upperairPath.getText().trim(), summaryPath.getText().trim()));
		if (unvalidDirs.size() > 0) {
			try {
				for(String dir : unvalidDirs) {
		    		if(!Files.isDirectory(Paths.get(dir))) {
		   				Files.createDirectories(Paths.get(dir));
		    		}
		    	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	void onSmyDbBtn(ActionEvent event) {
		// show report generating alert
		final Alert notifyRptGenerating = new Alert(AlertType.INFORMATION); // initiate alert dialogue
		notifyRptGenerating.setTitle(DIALOGUE_TITLE); // set dialogue title bar
		notifyRptGenerating.setHeaderText(""); // set dialogue content title. empty string means no title
		notifyRptGenerating.setContentText(DIALOGUE_SUMMARY_DB_WRITING); // set dialogue content
		notifyRptGenerating.show();

		boolean completed = false;

		try {
			completed = SummaryApplication.init(Main.globalPropertiesPath).updateDb();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// if write to database done, show notice
		if (completed) {
			notifyRptGenerating.close();
			final Alert notifyRptDone = new Alert(AlertType.INFORMATION);
			notifyRptDone.setTitle(DIALOGUE_TITLE);
			notifyRptDone.setHeaderText("");
			notifyRptDone.setContentText(DIALOGUE_SUMMARY_DB_WRITING_DONE);
			// only after the user close the dialogue does the program continue executing
			notifyRptDone.showAndWait();
		}
	}

	@FXML
	void onSave(ActionEvent event) {
		List<String> unvalidDirs = FileProcess.getUnvalidDirs(
				Arrays.asList(surfacePath.getText().trim(), upperairPath.getText().trim()
												, summaryPath.getText().trim()));
		if (unvalidDirs.size() > 0) {
			try {
				for(String dir : unvalidDirs) {
		    		if(!Files.isDirectory(Paths.get(dir))) {
		   				Files.createDirectories(Paths.get(dir));
		    		}
		    	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String time = rptYear.getText().trim();
		String year = time.split("-")[0];
		String month = String.valueOf(Integer.parseInt(time.split("-")[1])); // turn integer first to eliminate "0", for
																				// etc., "01" -> "1"
		String ipS = JDBC + ip.getText().trim() + DB;

		try {
			Configuration config = builder.getConfiguration();
			config.setProperty(PROP_YEAR, year);
			config.setProperty(PROP_MONTH, month);
			config.setProperty(PROP_SUMMARY_YEAR, summaryYear.getText().trim());
			config.setProperty(PROP_SURFACE_OUTPUT_FOLDER, surfacePath.getText().trim());
			config.setProperty(PROP_UPPERAIR_OUTPUT_FOLDER, upperairPath.getText().trim());
			config.setProperty(PROP_SUMMARY_OUTPUT_FOLDER, summaryPath.getText().trim());
			config.setProperty(PROP_URL, ipS);
			config.setProperty(PROP_USERNAME, account.getText().trim());
			config.setProperty(PROP_PASSWORD, password.getText().trim());
			builder.save();
			final Alert updateSuccess = new Alert(AlertType.INFORMATION);
			updateSuccess.setTitle(DIALOGUE_TITLE);
			updateSuccess.setHeaderText("");
			updateSuccess.setContentText(DIALOGUE_CONFIG_UPDATED);
			updateSuccess.show();
		} catch (ConfigurationException cex) {
			// loading of the configuration file failed
			cex.printStackTrace();
		}
	}

	@FXML
	void onSurfacePath(ActionEvent event) {
		File file = OpenFileExplorer.selectFolder(event);
		if (file != null) {
			surfacePath.setText(Paths.get(file.toString()).toString());
		}
	}

	@FXML
	void onUpperairPath(ActionEvent event) {
		File file = OpenFileExplorer.selectFolder(event);
		if (file != null) {
			upperairPath.setText(Paths.get(file.toString()).toString());
		}
	}

	@FXML
	void onSummaryPath(ActionEvent event) {
		File file = OpenFileExplorer.selectFolder(event);
		if (file != null) {
			summaryPath.setText(Paths.get(file.toString()).toString());
		}
	}

}
