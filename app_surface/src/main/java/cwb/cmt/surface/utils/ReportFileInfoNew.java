package cwb.cmt.surface.utils;

import static java.lang.Integer.parseInt;
import static cwb.cmt.surface.utils.ConvertFileName.decodeFilename;

import java.util.Arrays;


public class ReportFileInfoNew extends ReportFileInfo {

    public final int fileSequence;
    
    public final int pageNum;
    
    public final int table;
    
    public ReportFileInfoNew(String filename, int fileSequence, int pageNum, int[] indexes, int table, String textChinese, String textEnglish, int pageSequence) {
        super(filename, indexes, textChinese, textEnglish, pageSequence);
        this.fileSequence = fileSequence;
        this.pageNum = pageNum;
        this.table = table;
    }
    
    public String toString() {
        return ((filename != null)? filename : "\"FIXED INDEX ITEM\"") + " ("
             + ((fileSequence != -1)? fileSequence : "(no file sequence)") + ", "
             + ((pageNum != -1)? pageNum : "(no page Number)") + ", "
             + Arrays.toString(indexes) + ((table != -1)? " " + table : " (no table)") + ", "
             + textChinese + ", "
             + ((textEnglish != null)? textEnglish : "")
             + ((pageSequence != -1)?  ", " + pageSequence : "")
             + ")" + "\n";
    }
    
    public String filenameWithoutPageSequence() {
        return filename.substring(0, (filename.lastIndexOf('#') != -1)? filename.lastIndexOf('#') : filename.lastIndexOf('.'));
    }
    
    public boolean equalsIgnorePageSequence(ReportFileInfo info) {
        // indexes, [table], text (Ch,En)
        final ReportFileInfoNew infoNew = (ReportFileInfoNew) info;        
        if (indexes.length != infoNew.indexes.length) {
            return false;
        }
        else if (!Arrays.equals(indexes, infoNew.indexes)) {
            return false;
        }
        else if (table != infoNew.table) {
            return false;
        }        
        
        // check text equlity
        if (textChinese == null) {
            if (info.textChinese != null)
                return false;
        }
        else if (!textChinese.equals(info.textChinese)) {
            return false;
        }
        
        if (textEnglish == null ) {
            if (info.textEnglish != null)
                return false;
        }
        else if (!textEnglish.equals(info.textEnglish)) {
            return false;
        }
        return true;
    }
    
    public static ReportFileInfoNew valueOf(String filename) {
        return (ReportFileInfoNew) new ReportFileInfoParser().parse(filename);
    }
    
    public static class ReportFileInfoParser implements cwb.cmt.surface.utils.ReportFileInfoParser {
        public ReportFileInfoNew parse(String filename) {
            String filenameWithoutExt = filename.substring(0, filename.lastIndexOf('.'));
            String[] parts = filenameWithoutExt.split("_");            
            String fileSequenceStr = parts[0].split("-")[0];
            String pageNumStr = (parts[0].contains("-"))? parts[0].split("-")[1] : null;            
            String[] chapterSection = (parts[1].contains("#"))? parts[1].split("#")[0].split("-") : parts[1].split("-");
            int[] indexes = new int[chapterSection.length];
            for (int i=0; i<chapterSection.length; i++) {
                indexes[i] = parseInt(chapterSection[i]);
            }
            String table = (parts[1].contains("#"))? String.valueOf(parts[1].substring(parts[1].indexOf('#')+1)) : null;
            String text_part = (parts[2].contains("#"))? parts[2].split("#")[0] : parts[2];
            String text_chinese = decodeFilename(text_part.substring(0, (text_part.indexOf('-') != -1)? text_part.indexOf('-') : text_part.length()));
            String text_english = decodeFilename((text_part.indexOf('-') != -1)? text_part.substring(text_part.indexOf('-')+1, text_part.length()) : null);
            String pageSequence = (parts[2].contains("#"))? String.valueOf(parts[2].charAt(parts[2].indexOf('#')+1)) : null;
            return new ReportFileInfoNew(filename,
                                         parseInt(fileSequenceStr),
                                         (pageNumStr != null)? parseInt(pageNumStr) : -1,
                                         indexes, (table != null)? parseInt(table) : -1,
                                         text_chinese, text_english,
                                         (pageSequence != null)? parseInt(pageSequence) : -1);
        }
    }
}
