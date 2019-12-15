package cwb.cmt.surface.utils;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;


import static cwb.cmt.surface.utils.ConvertFileName.decodeFilename;

public class ReportFileUtils {
    
    public static ReportFileInfo getSortedFileInformation(String filename, ReportFileInfoParser parser) {
        if (parser == null) {
            throw new NullPointerException("The argument \'parser\' is null. Please specify an acceptable one.");
        }
        return parser.parse(filename);
    }
    
    public static File[] getSortedReportFiles(String dirPath) {
        return getSortedReportFiles(dirPath, new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
    }
    
    public static File[] getSortedReportFiles(String dirPath, FileFilter filter) {
        return getSortedReportFiles(dirPath, filter, new java.util.Comparator<File>() {
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
    }
    
    public static File[] getSortedReportFiles(String dirPath, FileFilter filter, Comparator<File> comparator) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new IllegalStateException("The specified path does not exist in the file system.");
        }
        else if (!dir.isDirectory()) {
            throw new IllegalStateException("The path " + "\'" + dirPath + "\' does not refer to a director. "
                    + "Please specify one that does.");
        }
        
        File[] files = dir.listFiles(filter);
        
        Arrays.sort(files, comparator);
        return files;
    }
    
    public static String getIndexPart(String filename) {
//    	System.out.println("filename>> "+filename);
        int indexOfSharp = filename.lastIndexOf('#');
        int indexOfExtDot = (filename.lastIndexOf('.') != -1)? filename.lastIndexOf('.') : filename.length();
        int indexNonDigit = -1;        
        for (int i=0; i<filename.length(); i++) {
            if (filename.charAt(i) == '_' || (filename.charAt(i) >= '0' && filename.charAt(i) <= '9')) {
                continue;
            }           
            indexNonDigit = i-1;
            break;
        }        
        final String part_index = filename.substring(0, indexNonDigit);
        return part_index;
    }
    
    public static int[] get_indexes(String filename) {       
        final String part_index = getIndexPart(filename);
        final String[] part_indexes = part_index.split("_");
        final int[] indexes = new int[part_indexes.length];
        for (int i=0; i<part_indexes.length; i++) {
            try {
                indexes[i] = Integer.parseInt(part_indexes[i]);
            }
            catch (NumberFormatException e) {
                indexes[i] = -1;
            }
        }
        return indexes;
    }
    
    public static Integer[] get_Indexes(String filename) {       
        final int[] indexes = get_indexes(filename);
        final Integer[] Indexes = new Integer[indexes.length];
        for (int i=0; i<indexes.length; i++) {
            Indexes[i] = Integer.valueOf(indexes[i]);
        }
        return Indexes;
    }
    
    public static String getTextPart(String filename) {
        int indexOfSharp = filename.lastIndexOf('#');
        int indexOfExtDot = (filename.lastIndexOf('.') != -1)? filename.lastIndexOf('.') : filename.length();
        int indexNonDigit = -1;        
        for (int i=0; i<filename.length(); i++) {
            if (filename.charAt(i) == '_' || (filename.charAt(i) >= '0' && filename.charAt(i) <= '9')) {
                continue;
            }
            indexNonDigit = i-1;
            break;
        }        
        String part_text = decodeFilename(filename.substring(indexNonDigit+1, (indexOfSharp != -1)? indexOfSharp : indexOfExtDot));
        return part_text;
    }
    
    public static String[] getText(String filename) {
        String part_text = getTextPart(filename);
        return part_text.split("_");
    }
    
    public static int getSequence(String filename) {
        int indexOfSharp = filename.lastIndexOf('#');
        int indexOfDot = (filename.lastIndexOf('.') != -1)? filename.lastIndexOf('.') : filename.length();
        int indexNonDigit = -1;        
        for (int i=0; i<filename.length(); i++) {
            if (filename.charAt(i) == '_' || (filename.charAt(i) >= '0' && filename.charAt(i) <= '9')) {
                continue;
            }           
            indexNonDigit = i-1;
            break;
        }
        
        String part_text = filename.substring(indexNonDigit+1, (indexOfSharp != -1)? indexOfSharp : indexOfDot);
        String part_seq = filename.substring((indexOfSharp != -1)? indexOfSharp+1 : indexNonDigit+part_text.length()+1, indexOfDot).trim();
        int seq = -1;
        try {
            seq = Integer.parseInt(part_seq);
        }
        catch (NumberFormatException e) {
            seq = -1;
        }
        return seq;
    }
}
