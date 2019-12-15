package cwb.cmt.summary.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import cwb.cmt.summary.service.PdfAddPageNumber;
import cwb.cmt.summary.service.PdfCopyOuterFiles;
import cwb.cmt.summary.service.PdfMergeWithOutline;

@Service
public class ProcessPdf extends Process {
	
	@Autowired
	private PdfAddPageNumber pdfAddPageNumber;
	
	@Autowired
	private PdfCopyOuterFiles pdfCopyOuterFiles;
	
	@Autowired
	private PdfMergeWithOutline pdfMergeWithOutline;
	
	private Map<String, String> filenames;
			
	
	public void run() {
				
		// copy outer files to tmp folder
		try {
			pdfCopyOuterFiles.copyFiles(filenames);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// add page number
		try {
			pdfAddPageNumber.addPageNumber(outputTmpPdfPath);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// add outlines and merge pdfs
		try {
			pdfMergeWithOutline.merge(new File(outputTmpPdfPath));
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void cleanTmpFolder(){
		try {
			FileUtils.cleanDirectory(Paths.get(outputTmpPdfPath).toFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
	// setter
	public void setOuterFilesPath(Map<String, String> filenames){
		this.filenames = filenames;		
	}
}