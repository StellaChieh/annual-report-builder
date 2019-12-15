package cwb.cmt.view.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileProcess {
	
	public static boolean openFile(String fileName){	
		if(Files.exists(Paths.get(fileName))){
			try {
				Desktop.getDesktop().open(new File(fileName));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}	
	
	public static List<String> getUnvalidFiles(List<String> paths) {
    	List<String> list = new ArrayList<>();
    	for(String path :paths) {
    		if(path.isEmpty()) {
    			continue;
    		}
    		if(!Files.exists(Paths.get(path))) {
        		list.add(path);
        	}
    	}
    	return list;
    }
	
	public static List<String> getUnvalidDirs(List<String> dirs){
		List<String> unvalids = new ArrayList<>();
		for(String dir : dirs) {
			if(dir.isEmpty()) {
				continue;
			}
			if(!Files.isDirectory(Paths.get(dir))){
				unvalids.add(dir);
			}
		}
		return unvalids;
	}
}
