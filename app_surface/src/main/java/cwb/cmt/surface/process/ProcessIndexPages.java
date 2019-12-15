package cwb.cmt.surface.process;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cwb.cmt.surface.service.CreateTableImage;
import cwb.cmt.surface.service.CreateTableImageForFirstInterleaf;
import cwb.cmt.surface.service.CreateTableImageForIndexPages;
import cwb.cmt.surface.utils.ReportFileInfo;
import cwb.cmt.surface.utils.ReportFileInfoNew.ReportFileInfoParser;
import cwb.cmt.surface.utils.ReportFileInfoNew;
import static cwb.cmt.surface.utils.ReportFileUtils.getSortedFileInformation;
import static cwb.cmt.surface.utils.ReportFileUtils.getSortedReportFiles;


@Service("processIndexPages")
public class ProcessIndexPages {
	
	@Resource(name="createTableImageForIndexPages")
	CreateTableImageForIndexPages tableImageCreator;
	
	private static final ReportFileInfoParser DefaultParser = new ReportFileInfoNew.ReportFileInfoParser();
    
	public void run(String getPdfOutputPath) throws IOException{
        // Delete last batch of production of index pages
        File[] files = getSortedReportFiles(getPdfOutputPath, new FileFilter() {
            public boolean accept(File file) {
                boolean accept = file.getName().contains("目錄");
                if (accept) {
                    file.delete();
                }
                return accept;
            }
        });
        
        // Collect all the renamed file items
        files = getSortedReportFiles(getPdfOutputPath);
        
        // iterating through the sorted files, add some fake items at the particular positions
        final ArrayList<ReportFileInfo> data = new ArrayList<ReportFileInfo>();
        boolean generalSummariesItemAdded = false, auxiliarySummariesItemAdded = false;
        for (int i=0; i<files.length; i++) {
            // prepare file-info
            final ReportFileInfoNew info
                    = (ReportFileInfoNew) getSortedFileInformation(files[i].getName(), DefaultParser);
            
            // Add some fixed item
            switch (Arrays.toString(info.indexes)) {
                // general climatological summaries
                case "[1, 3]":
                    if (info.table == 2) {
//                        Log.print();
                    }
                    if (!generalSummariesItemAdded) {
                        data.add(data.size(), new ReportFileInfoNew(null,
                                                                    -1, -1,
                                                                    new int[] {1, 3}, -1,
                                                                    "基本氣象綱要表", "General Tables For Climatological Summaries", -1));
                        generalSummariesItemAdded = true;
                    }
                    break;
                // auxiliary climatological summaries
                case "[1, 4, 1]":
                    if (!auxiliarySummariesItemAdded) {
                        data.add(data.size(), new ReportFileInfoNew(null,
                                                                    -1, -1,
                                                                    new int[] {1, 4}, -1,
                                                                    "輔助氣候綱要表", "Auxiliar Tables For Climatological Summaries", -1));
                        data.add(data.size(), new ReportFileInfoNew(null,
                                                                    -1, -1,
                                                                    new int[] {1, 4, 1}, -1,
                                                                    "全天空日射量及日照時數", "Global Solar Radiation and Duration of Sunshine", -1));
                        auxiliarySummariesItemAdded = true;
                    }
                    break;
                case "[2, 1, 2]":
                    continue;
            }  
            
            if (info.textChinese.contains("封面")) {
                continue;
            }
            
            // ignore items with repeated text
            if (i > 1 && info.equalsIgnorePageSequence(getSortedFileInformation(files[i-1].getName(), DefaultParser))) {
                continue;
            }
            data.add(info);
        }
        
        // Dump
        for (ReportFileInfo info : data)
//            System.out.println("* " + info);
        

        tableImageCreator.setPath(getPdfOutputPath);
        tableImageCreator.setFilename("0-3_目錄-Contents");
        tableImageCreator.createTableImage(data);
    }
	
	
}
