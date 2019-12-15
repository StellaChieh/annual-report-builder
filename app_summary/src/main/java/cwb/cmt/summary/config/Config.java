package cwb.cmt.summary.config;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Resource(name="year")
	private int year;
	
	@Resource(name="rootPath")
	private String rootPath;
	
	private String summaryClimaticElementXmlConfigPath;
	@Resource(name="summaryClimaticElementXmlConfigPath")
	public void setSummaryClimaticElementXmlConfigPath(String summaryClimaticElementXmlConfigPath) throws FileNotFoundException {
		if(Files.exists(Paths.get(rootPath, summaryClimaticElementXmlConfigPath), LinkOption.NOFOLLOW_LINKS)){
			this.summaryClimaticElementXmlConfigPath = Paths.get(rootPath, summaryClimaticElementXmlConfigPath).toString();
		}else {
			throw new FileNotFoundException("summary climatic element xml config path not found.");
		}
	}
	
	private String summaryStationXmlConfigPath;
	@Resource(name="summarystationXmlConfigPath")
	public void setSummaryStationXmlConfigPath(String summaryStationXmlConfigPath) throws FileNotFoundException {
		if(Files.exists(Paths.get(rootPath, summaryStationXmlConfigPath), LinkOption.NOFOLLOW_LINKS)){
			this.summaryStationXmlConfigPath = Paths.get(rootPath, summaryStationXmlConfigPath).toString();
		}else {
			throw new FileNotFoundException("summary station xml config path not found.");
		}
	}
	
	private String outputTmpPdfPath;
	@Resource(name="outputTmpPdfPath")
	public void setOutputTmpPdfPath(String outputTmpPdfPath) throws FileNotFoundException {
		if(Files.exists(Paths.get(rootPath, outputTmpPdfPath), LinkOption.NOFOLLOW_LINKS)){
			this.outputTmpPdfPath = Paths.get(rootPath, outputTmpPdfPath).toString();
		}else {
			throw new FileNotFoundException("output pdf tmp path not found.");
		}
	}
	
	@Resource(name="outputPdfFilename")
	private String outputPdfFile;
	
	@Resource(name="outputFolder")
	private String outputFolder; 
	
	private String kaiuFilePath;
	@Resource(name="kaiuFilePath")
	public void setKaiuFilePath(String kaiuFilePath){
		this.kaiuFilePath = Paths.get(rootPath, kaiuFilePath).toString();
	}	
	public int getYear() {
		return year;
	}
	public String getRootPath() {
		return rootPath;
	}
	public String getSummaryClimaticElementXmlConfigPath() {
		return summaryClimaticElementXmlConfigPath;
	}
	public String getOutputTmpPdfPath() {
		return outputTmpPdfPath;
	}
	public String getOutputPdfFile() {
		return Paths.get(outputFolder, String.format(outputPdfFile, year)).toString();
	}
	public String getOutputFolder() {
		return outputFolder;
	}
	public String getKaiuFilePath() {
		return kaiuFilePath;
	}
	public String getSummaryStationXmlConfigPath() {
		return summaryStationXmlConfigPath;
	}
}