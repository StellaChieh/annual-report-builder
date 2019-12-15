package cwb.cmt.surface.pdfProcess;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class GetFiles {
	private static int getPageSequenceNumber(String fileName) {
		int idxDash = fileName.indexOf('-');
		int idxUnderline = fileName.indexOf('_');
		int sIdx = (idxDash > idxUnderline) ? idxUnderline : idxDash;
		return Integer.parseInt(fileName.substring(0, sIdx));
	}
	
	public static File[] getSortedFiles(File[] files){
		
		// sort file by filename number
        Arrays.sort(files, new Comparator<File>() {	
        	public int compare(File o1, File o2){
        		int n1 = getPageSequenceNumber(o1.getName());
        		int n2 = getPageSequenceNumber(o2.getName());
        		return n1 - n2;
        	}
        });
        
        return files;
	}
}
