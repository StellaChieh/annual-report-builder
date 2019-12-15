package cwb.cmt.surface.process;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cwb.cmt.surface.utils.Month;
import windRose.process.WindRoseMain;

@Service("processWindRose")
public class ProcessWindRose extends Process{
    //Time: year
    @Resource(name="year")
    protected int year;
    
    //Time: month
    @Resource(name="month")
    protected int months;
    
	private static final String PdfFilenamePrefix = "WindRose";
	
	// business logic
	public void run() throws IOException{
		
		//get stnList
		prepareConfig_Stns();
		
		// call windrose jar to draw windrose
		// filename : 9_01_00015_1 (PageEncode_氣象要素數_頁數_風花圖的頁數)
		List<String> stnsList = new ArrayList<>();
		for(int j=0; j<stnList.size(); j++) {
			stnsList.add(stnList.get(j).getStno());
			
		}
		String [] stationArr = new String[stnsList.size()];
		stationArr = stnsList.toArray(stationArr);
		//Wind Rose files generate at path >> C:\Users\YuPing ho\CMTworkspace\annual-report\app_surface\tmp\tmp_surface
//		WindRoseMain.surface(stationArr, "", String.valueOf(year), PdfFilenamePrefix);
		WindRoseMain.surface(stationArr, "", String.valueOf(year)+"_"+String.format("%02d", months) , PdfFilenamePrefix);
		String getPdfOutputPath = outputTmpPdfPath;
        File[] files = new File(getPdfOutputPath).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().startsWith(PdfFilenamePrefix);
            }
        });
        
        // Sort by creation time
        Arrays.sort(files, new java.util.Comparator<File>() {
            public int compare(File f1, File f2) {
                BasicFileAttributes attr1 = null;
                BasicFileAttributes attr2 = null;                
                try {
                    attr1 = Files.readAttributes(Paths.get(f1.getAbsolutePath()), BasicFileAttributes.class);
                    attr2 = Files.readAttributes(Paths.get(f2.getAbsolutePath()), BasicFileAttributes.class);
                    return attr1.creationTime().compareTo(attr2.creationTime());
                }
                catch (IOException e) {
                    
                }                
                return f1.getName().compareTo(f2.getName());
            }           
        });
        
        int temp = 0;
        for (int i=0; i<files.length; i++) {
            String ext = files[i].getName().substring(files[i].getName().lastIndexOf('.')+1, files[i].getName().length());
            System.out.print("[*] ext: " + ext);
            
            String nameWithoutPrefix = files[i].getName().substring(PdfFilenamePrefix.length(), files[i].getName().lastIndexOf("." + ext));
            int month = (nameWithoutPrefix.indexOf('月') > -1)? Integer.parseInt(nameWithoutPrefix.substring(0, nameWithoutPrefix.indexOf('月'))) : -1;
            int seq = Integer.parseInt(nameWithoutPrefix.substring(nameWithoutPrefix.lastIndexOf('_')+1));
            System.out.println("[*] windrose filename: " + "\'" + files[i].getName() + "\'");
            
            File dest = null;
            if (month == -1) {
                if (seq == 1)
                    dest = new File(getPdfOutputPath + File.separator + "3_1_風花圖範例_Wind Roses Example" + "." + ext);
                else
                    dest = new File(getPdfOutputPath + File.separator + "3_"+(temp+2)+"_民國" + (year - 1911) + "年"
                            + "風花圖_Surface Wind Roses, Annual " + year + "#" + (seq-1) + "." + ext);
            }
            else {
                dest = new File(getPdfOutputPath + File.separator + "3_" + (month+1) + "_民國" + (year - 1911) + "年" + month + "月"
                                + "風花圖_Surface Wind Roses, " + Month.getByIndex(month-1) + " " + year + "#" + (seq-1) + "." + ext);
                temp=month;
            }
            System.out.println("--> rename to " + "\'" + dest + "\'");
            
            files[i].renameTo(dest);
        }
		
	}
}
