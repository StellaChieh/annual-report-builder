package cwb.cmt.surface.utils;

import java.util.Arrays;

public class ReportFileInfo {
    
    public final String filename;
    
    public final String ext;
    
    //public int fileSequence;
    
    //public int pageNum;

    
    public final int[] indexes;
    
    public final Integer[] Indexes;
    
    //public int tableNum;
    
    
    public final String textChinese, textEnglish;
    
    public final int pageSequence;
    
    public ReportFileInfo(String filename, int[] indexes, String textChinese, String textEnglish, int pageSequence) {
        this.filename = filename;
        this.ext = (filename != null)? filename.substring(filename.lastIndexOf('.')+1) : null;
        this.indexes = indexes;
        this.Indexes = new Integer[indexes.length];
        for (int i=0; i<indexes.length; i++) {
            Indexes[i] = indexes[i];
        }
        this.textChinese = textChinese;
        this.textEnglish = textEnglish;
        this.pageSequence = pageSequence;
    }
    
    public String toString() {
        return filename + "(" + Arrays.toString(indexes) + ", " + textChinese + ", " + textEnglish + pageSequence + ")" + "\n";
    }
    
    public String filenameWithoutPageSequence() {
        return filename.substring(0, (filename.lastIndexOf('#') != -1)? filename.lastIndexOf('#') : filename.lastIndexOf('.'));
    }
    
    public boolean equalsIgnorePageSequence(ReportFileInfo info) {
        String s1 = filename.substring(0, (filename.lastIndexOf('#') != -1)? filename.lastIndexOf('#') : filename.lastIndexOf('.'));
        String s2 = info.filename.substring(0, (info.filename.lastIndexOf('#') != -1)? info.filename.lastIndexOf('#') : info.filename.lastIndexOf('.'));
        return s1.equals(s2);
    }
    
    public static ReportFileInfo valueOf(String filename) {
        return new ReportFileInfoParser().parse(filename);
    }
    
    public static class ReportFileInfoParser implements cwb.cmt.surface.utils.ReportFileInfoParser {
        public ReportFileInfo parse(String filename) {
            final int pageSequence = ReportFileUtils.getSequence(filename);
            final int[] indexes = ReportFileUtils.get_indexes(filename);
            final String[] text = ReportFileUtils.getText(filename);
            return new ReportFileInfo(filename, indexes, text[0], (text.length > 1)? text[1]:null , pageSequence);
        }
    }
}
