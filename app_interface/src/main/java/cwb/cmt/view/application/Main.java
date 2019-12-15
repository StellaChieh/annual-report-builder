package cwb.cmt.view.application;
	
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.ex.ConfigurationException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static final boolean runFromBat = true; 
	
	// these file path was compound of [root path + file path in config.property] and become absolute file path
	// , so while the user move the whole folder(report.bat, dependency, config, tmp)
	// , he only needs to change the root path in config.property 
	private static String fxmlPath;       
	private static String cwblogoPath;    
	private static String rootPath;
	
	// config.property absolute file path
	public static String globalUpperairStationPath;
	public static String globalSurfaceStationPath;
	public static String globalSurfaceCePath;
	public static String globalSummaryStationPath;
	public static String globalSummaryCePath;
	public static String globalPropertiesPath;
	public static String globalInterfaceDefaultFilePath;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			TabPane mainPane = (TabPane)FXMLLoader.load(new URL(fxmlPath));
			Scene scene = new Scene(mainPane, 1300, 700);
			primaryStage.getIcons().add(new Image(Files.newInputStream(Paths.get(rootPath, cwblogoPath))));
			primaryStage.setTitle("報表產製程式");
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// args[0] = relative file path of "config.property" (relative to report.bat)
		globalPropertiesPath = (String)args[0];
		
		// load properties file using apache commons configuration2
		org.apache.commons.configuration2.builder.fluent.Parameters params = 
				new org.apache.commons.configuration2.builder.fluent.Parameters();
    	FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
    		new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
    									.configure(params.properties().setFileName(globalPropertiesPath));
		
    	try {
			Configuration config = builder.getConfiguration();
			String projectPath;
			if(runFromBat) {
				// write root path to config.properties file
				projectPath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
										.toURI().getPath()).getParent(); // get parent file(bat folder)
				config.setProperty("cmt.root.path", projectPath);
				builder.save();
			}
			rootPath = config.getString("cmt.root.path").trim();
			// FXMLLoader read URL, so add "file:///" prefix to get view.fxml file path
			fxmlPath = "file:///" + Paths.get(rootPath, config.getString("interface.view.fxml.path").trim()).toString();	
			cwblogoPath = config.getString("interface.view.cwblogo.path");
			
			// set absolute path
			if(runFromBat) {
				globalPropertiesPath = Paths.get(rootPath, args[0]).toString();  // run from bat
			} else {
				globalPropertiesPath = Paths.get(args[0]).toString();  // run from IDE
			}
			globalUpperairStationPath = Paths.get(rootPath, config.getString("upperair.station.path")).toString();
			globalSurfaceStationPath = Paths.get(rootPath, config.getString("surface.station.path")).toString();
			globalSurfaceCePath = Paths.get(rootPath, config.getString("surface.ce.path")).toString();
			globalSummaryStationPath = Paths.get(rootPath, config.getString("summary.station.path")).toString();
			globalSummaryCePath = Paths.get(rootPath, config.getString("summary.ce.path")).toString();
			globalInterfaceDefaultFilePath = Paths.get(rootPath, config.getString("interface.default.pdf.path")).toString();
			
			// create tmp folder for surface, upperair, summary, windrose
			createTmpFolder();
			
			launch(args);
			
		} catch(ConfigurationException cex) {
			cex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void createTmpFolder() {
		String surfaceTmpFolder = Paths.get(rootPath, "tmp", "tmp_surface").toString();
		String upperairTmpFolder = Paths.get(rootPath, "tmp", "tmp_upperair").toString();
		String summaryTmpFolder = Paths.get(rootPath, "tmp", "tmp_summary").toString();
		String windRoseTmpFolder = Paths.get(rootPath, "tmp", "tmp_windRose").toString();
		makeTmpFolder(surfaceTmpFolder);
		makeTmpFolder(upperairTmpFolder);
		makeTmpFolder(summaryTmpFolder);
		makeTmpFolder(windRoseTmpFolder);
	}
	
	private static void makeTmpFolder(String tmpAbsolutePath) {
		File tmp = new File(tmpAbsolutePath);
		if(!tmp.isDirectory()) {
			new File(tmpAbsolutePath).mkdirs();
		}
	}

}