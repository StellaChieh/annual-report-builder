package cwb.cmt.summary.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.config.Config;
import cwb.cmt.summary.utils.PageEncode;

@Service
public class PdfCopyOuterFiles {

	private static final String COVER_NAME = PageEncode.COVER.getEncode() + "_" + PageEncode.COVER.getFilename() + ".pdf";
	private static final String EXAMPLE_NAME = PageEncode.EXAMPLE.getEncode() + "_" + PageEncode.EXAMPLE.getFilename() + ".pdf";
	private static final String INTRODUCTION_NAME = PageEncode.INTRODUCTION.getEncode() + "_" + PageEncode.INTRODUCTION.getFilename() + ".pdf";
	private static final String COPYRIGHT_NAME = PageEncode.COPYRIGHT.getEncode() + "_" +  PageEncode.COPYRIGHT.getFilename()+ ".pdf"; 
	
	private String outputTmpPdfPath;
	
	@Autowired
	public PdfCopyOuterFiles(Config config) {
		this.outputTmpPdfPath = config.getOutputTmpPdfPath();
	}
	
	public void copyFiles(Map<String, String> files) throws IOException{	
		// get original file path from files hashmap
		// Files.copy(source, target)
		Files.copy(Paths.get(files.get("cover")), Paths.get(outputTmpPdfPath, COVER_NAME), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(Paths.get(files.get("example")), Paths.get(outputTmpPdfPath, EXAMPLE_NAME), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(Paths.get(files.get("introduction")), Paths.get(outputTmpPdfPath, INTRODUCTION_NAME), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(Paths.get(files.get("copyright")), Paths.get(outputTmpPdfPath, COPYRIGHT_NAME), StandardCopyOption.REPLACE_EXISTING);
	}

}
