package cwb.cmt.summary.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import cwb.cmt.summary.utils.AddPageNumber;
import cwb.cmt.summary.utils.GetFiles;
import cwb.cmt.summary.utils.PageEncode;
import cwb.cmt.summary.utils.RomanNumber;


@Service
public class PdfAddPageNumber {

	private static Logger log = Logger.getLogger(PdfAddPageNumber.class);
		
	private File[] filterRomanPage(List<File> files){
		Predicate<File> isExample = p -> p.getName().startsWith(String.valueOf(PageEncode.EXAMPLE.getEncode()));
		Predicate<File> isIntroduction = p -> p.getName().startsWith(String.valueOf(PageEncode.INTRODUCTION.getEncode()));
		Predicate<File> isStns = p -> p.getName().startsWith(String.valueOf(PageEncode.STATIONS.getEncode()));
		Predicate<File> isContents = p -> p.getName().startsWith(String.valueOf(PageEncode.CONTENTS.getEncode()));
		Predicate<File> isStnMove = p -> p.getName().startsWith(String.valueOf(PageEncode.STATION_MOVES.getEncode()));
		Predicate<File> fullPredicate = isExample.or(isIntroduction).or(isStns).or(isContents).or(isStnMove);
		List<File> result = files.stream().filter(fullPredicate).collect(Collectors.toList());
		return result.toArray(new File[result.size()]);
	}
	
	private File[] filterPage(List<File> files){
		Predicate<File> isPartOne = p -> p.getName().startsWith(String.valueOf(PageEncode.TABLE_ONE.getEncode()));
		Predicate<File> isPartTwo = p -> p.getName().startsWith(String.valueOf(PageEncode.TABLE_TWO.getEncode()));
		// interleaf encode is the same as part two table, but interleaf don't need to add page number
		Predicate<File> isNotPartTwoStnInterleaf = p -> !FilenameUtils.removeExtension(p.getName()).endsWith(PageEncode.STN_INTERLEAF.getFilename());
		Predicate<File> fullPredicate = isPartOne.or(isPartTwo).and(isNotPartTwoStnInterleaf); 
		List<File> result = files.stream().filter(fullPredicate).collect(Collectors.toList());
		return result.toArray(new File[result.size()]);
	}

	
	public void addPageNumber(String folderContainsPdfs) throws FileNotFoundException, DocumentException, IOException{
		
		File[] pdfs = Paths.get(folderContainsPdfs).toFile().listFiles();
		
		// filter Roman page number pdfs
		log.info("Start to add Roman page number...");
		File[] romanPdfs = filterRomanPage(Arrays.asList(pdfs));
		addPageNumber_inner(folderContainsPdfs, GetFiles.getSortedFiles(romanPdfs), true); // get sorted pdfs files and sent to add page number
		log.info("Add Roman page number done...");
		
		// filter normal page number pdfs
		log.info("Start to add page number...");
		File[] normalPdfs = filterPage(Arrays.asList(pdfs));
		addPageNumber_inner(folderContainsPdfs, GetFiles.getSortedFiles(normalPdfs), false);
		log.info("Add page number done...");
	}
	
	private void addPageNumber_inner(String outputTmpPdfPath, File[] sortedPdfs, boolean isRoman) throws FileNotFoundException, DocumentException, IOException{
		for(int i=0; i<sortedPdfs.length; i++){
			String destFilename = FilenameUtils.removeExtension(sortedPdfs[i].getName()) + "_paged.pdf"; // remove ".pdf" extension and add "_paged"
			String destAbsolutePath = Paths.get(outputTmpPdfPath, destFilename).toString();
        	String pageNumber = (isRoman) ? RomanNumber.toRoman(i+1) : String.valueOf(i+1); // page start from 1 so add 1
			AddPageNumber.add(sortedPdfs[i].getPath(), destAbsolutePath, pageNumber);
			sortedPdfs[i].delete(); // delete the source pdf after it was stamped page number
        }
	}   
	
}
