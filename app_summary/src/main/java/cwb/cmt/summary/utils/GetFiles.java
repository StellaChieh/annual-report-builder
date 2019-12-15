package cwb.cmt.summary.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.FilenameUtils;

public class GetFiles {

	public static File[] getSortedFiles(File[] files){
		
		// sort file by filename number
        Arrays.sort(files, new Comparator<File>() {	
        	public int compare(File o1, File o2){
        		String[] n1 = FilenameUtils.removeExtension(o1.getName()).split(("_"));
        		String[] n2 = FilenameUtils.removeExtension(o2.getName()).split(("_"));
        		int n = (n1.length >= n2.length) ? n2.length : n1.length; 
        		for(int i=0; i<n; i++){
        			int n1Item = Integer.parseInt(n1[i]);
        			int n2Item = Integer.parseInt(n2[i]);
        			if(n1Item != n2Item){
        				return n1Item - n2Item; 
        			}
        		}
        		return 0;
        	}
        });
        
        return files;
	}
}
