package cwb.cmt.upperair.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.process.PrepareStations;
import cwb.cmt.upperair.utils.Utility;

public abstract class CreateOutput {

	@Autowired
	@Qualifier("rootPath")
	protected String rootPath;
	
	@Autowired
	@Qualifier("outputFolder")
	protected String outputFolder;
	
	@Autowired
	@Qualifier("year")
	protected int year;
	
	@Autowired
	@Qualifier("month")
	protected int month;
	
	@Autowired
	@Qualifier("upperairStationXmlConfigPath")
	protected String stnXmlPath;
	
	@Autowired
	protected PrepareStations prepareStns;
	
	// stns is actually a unmodified list
	protected List<Station> stns = null;
	
	private static final Logger logger = LogManager.getLogger();
	
	protected static boolean createFolder(String path){
		Path folder = Paths.get(path);
		if(!Files.exists(folder)) {
			try {
				Files.createDirectories(folder);
				logger.info(folder.toString() + " doen't exist, create this new directory.");
				return true;
			} catch (IOException e) {
				logger.error("Can not create folder " + path, e);
				return false;
			}
		} 
		return true;
	}
	
	
	protected String makeAbsolutelyPath(String rootPath, String relativePath) {
		return Paths.get(rootPath, relativePath).toString();
	}
	
	protected String formatOutputFilename(String filename) {
		return Paths.get(String.format(filename, year)).toString();
	}
	
	protected boolean cleanFolder(String folder){
		Path pathToBeDeleted = Paths.get(folder);     
		try{
			Files.walk(pathToBeDeleted)
	  		.map(Path::toFile)
	  		.filter(e->!e.getAbsolutePath().equals(pathToBeDeleted.toString()))
	  		.forEach(File::delete);
			logger.info("Clean " + folder + " folder.");
			return true;
		} catch (IOException e) {
			logger.error("Can't clean " + folder + " folder.", e);
			return false;
		}
	}
	
	protected boolean fileExist(String absolutelyFilePath) {
		if(!Files.exists(Paths.get(absolutelyFilePath))) {
			logger.error(absolutelyFilePath + " file doesn't exist.");
			return false;
		}
		return true;
	}
	
	protected boolean fileIsOpened(String absolutelyFilePath) {
		if(Files.exists(Paths.get(absolutelyFilePath)) 
				&& Utility.fileIsOpened(absolutelyFilePath)) {
			logger.error(absolutelyFilePath + " is opened, please close it.");
			return true;
		}
		return false;
	}
	
	protected boolean deleteOldOutputFile(String absoluteFilePath) {	
		if(Files.exists(Paths.get(absoluteFilePath))) {
			try {
				Files.delete(Paths.get(absoluteFilePath));
				logger.info("Delete old " + absoluteFilePath + ".");
			} catch (IOException e) {
				logger.error("Can't delete old " + absoluteFilePath + ".");
				return false;
			}
		}
		return true;
	}
	
	public boolean create() {
		if(!prepareResourceFiles()) {
			return false;
		};
		initProcessParameters();
		// parse station xml and query these station in database
		try {
			stns = prepareStns.getStns();
			if(stns.size() == 0) {
				logger.error("There are no station in xml file.");
				return false;
			}
		} catch (IOException e) {
			logger.error("Can't parse station xml.", e);
			return false;
		}
		return run();
	}
	
	protected boolean prepareResourceFiles() {
		stnXmlPath = makeAbsolutelyPath(rootPath, stnXmlPath);
		outputFolder = Paths.get(outputFolder).toString();
		// check station config xml file exists
		if(!fileExist(stnXmlPath)) {
			return false;
		}
		if(!createFolder(outputFolder)) {
			return false;
		} 
		return true;
	};

	protected void initProcessParameters() {
		prepareStns.init(stnXmlPath);
	};
	
	protected abstract boolean run();
}
