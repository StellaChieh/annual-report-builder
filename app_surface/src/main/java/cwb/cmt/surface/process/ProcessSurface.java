package cwb.cmt.surface.process;

import java.io.IOException;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("processSurface")
public class ProcessSurface extends Process{
	
	//Time: year
    @Resource(name="year")
    protected int year;
    
	// root path
	@Resource(name="rootPath")
	protected String rootPath;
	
	// tmp folder path
	@Resource(name="outputTmpPdfPath")
	protected String outputTmpPdfPath;
	
	//destination output folder
	@Resource(name="outputPdfFolder")
	protected String outputPdfFolder;
    
    
	@Override
	public void run() throws IOException {
	}
	
	public int getYear() throws IOException {
		return year;
	}
	
	public String getOutputTmpPdfPath() throws IOException {
		return outputTmpPdfPath;
	}
	
	public String getRootPath() throws IOException {
		return rootPath;
	}
	
	public String getOutputPdfFile(){
		String outputPdfPath = Paths.get(outputPdfFolder, year+"氣候資料年報第一部分-地面資料"+".pdf").toString();
		return outputPdfPath;
	}

	//output csv file path
	public String getOutputCsvFolder(){
		return outputPdfFolder;
	}
	
}
