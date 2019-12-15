package cwb.cmt.surface.pdfProcess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.DocumentException;

import cwb.cmt.surface.pdfProcess.SvgToPdf;

public class MainService {
	
	//only for test
	public static void main(String args[]) {
		try {
			processPdf(
					2016,
					Paths.get("C:\\Users\\YuPing ho\\CMTworkspace\\annual-report\\tmp\\tmp_surface").toString(), 
					"C:\\Users\\YuPing ho\\CMTworkspace\\annual-report\\tmp\\tmp_surface\\resultXXX.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void processPdf(int year, String tmpFolderAbsolutePath, String destFileAbsolutePath) throws IOException, DocumentException {
		
		// 1. turn svg to pdf
//		svgToPdf(tmpFolderAbsolutePath);
		
		// 2. add page number
		System.out.println("start to print page number!");
		printPageNumber(tmpFolderAbsolutePath);
		
		// 3. add outlines and merge pdf
		System.out.println("start to merge pdf!");
		MergePdfWithOutlines m = new MergePdfWithOutlines();
		m.merge(year, tmpFolderAbsolutePath, destFileAbsolutePath);

		System.out.println("report completed!");
		
	}
	private static void svgToPdf(String tmpFolder) throws IOException, DocumentException {
		
		List<File> fileList = null;
		
		// try-with-resource stmt ( The try-with-resources statement ensures that each resource is closed at the end of the statement)
		// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		try (Stream<Path> paths = Files.walk(Paths.get(tmpFolder))) {
			// retrieve svg file
			fileList = paths.filter(f -> FilenameUtils.getExtension(f.getFileName().toString()).equals("svg"))
		    											.map( f -> f.toFile())
		    											.collect(Collectors.toList());
		}
		
		SvgToPdf svg = new SvgToPdf();
		for(int i=0; i<fileList.size(); i++){	
			System.out.println("Start to turn " + fileList.get(i).getName() + " from svg to pdf");
			svg.createPdf(fileList.get(i).getAbsolutePath());
			// delete the svg after turning it to pdf
			Files.deleteIfExists(Paths.get(fileList.get(i).getAbsolutePath()));
			// calls java garbage collection
			System.gc(); 
		}
	}
	
	private static void printPageNumber(String tmpFolder) throws FileNotFoundException, DocumentException, IOException {
		File[] files = Paths.get(tmpFolder).toFile().listFiles();
		for(int i=0; i<files.length; i++) { 			
			AddPageNumber.print(files[i].getAbsolutePath().toString(), getPageNumber(files[i].getName()));
		}
	}
	
	
	// check should print Roman number or number or don't print anythins
	// fileSeq-pageNum_n1-n2-n3#n4_textChinese-textEnglish#pageSeq
	// fileSeq-pageNum_n1-n2#n4_textChinese-textEnglish#pageSeq
	// fileSeq_n1-n2_textChinese-textEnglish#pageSeq
	private static String getPageNumber(String filename) {
		if(CheckPdf.isInterleaf(filename)) { // don't print on interleaf
			return " ";
		} 
		
		int n1 = Integer.parseInt(filename.split("_")[1].split("-")[0]);
		String subFilename = filename.split("_")[1];
		int n2;
		if (subFilename.indexOf("#") < 0 ) { // don't have "#" 
			n2 = Integer.parseInt(subFilename.split("-")[1]);
		} else {
			n2 = Integer.parseInt(subFilename.split("-")[1].split("#")[0]);
		}
		
		if (n1 < 1 || (n1 ==1  && n2 < 3)) {
			return RomanNumber.toRoman(Integer.parseInt(filename.split("_")[0].split("-")[1]));
		} else {
			return filename.split("_")[0].split("-")[1];
		}
	}
}
