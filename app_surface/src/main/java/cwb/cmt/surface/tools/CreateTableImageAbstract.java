package cwb.cmt.surface.tools;

import java.io.File;
import java.io.FileNotFoundException;

import cwb.cmt.surface.tools.SpringIocContext;
import cwb.cmt.surface.tools.SpringIocComponent;

import ChartDirector.BaseChart;

@SuppressWarnings({"unused", "unchecked"})
public abstract class CreateTableImageAbstract extends SpringIocComponent implements CreateTableImageInterface
{
    // Align mode
    protected static final int TABLE_CELL_ALIGNMENT_LEFT_BOTTOM = 1;
    protected static final int TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_BOTTOM = 2;
    protected static final int TABLE_CELL_ALIGNMENT_RIGHT_BOTTOM = 3;
        
    protected static final int TABLE_CELL_ALIGNMENT_LEFT_CENTER_VERTICAL = 4;
    protected static final int TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_VERTICAL = 5;
    protected static final int TABLE_CELL_ALIGNMENT_RIGHT_CENTER_VERTICAL = 6;
    
    protected static final int TABLE_CELL_ALIGNMENT_LEFT_TOP = 7;
    protected static final int TABLE_CELL_ALIGNMENT_CENTER_HORIZONTAL_TOP = 8;
    protected static final int TABLE_CELL_ALIGNMENT_RIGHT_TOP = 9;
    
    // Layout dimensions
    private static final int ChartMarginTop = 45;    
    private static final int ChartWidth = 2100;
    private static final int ChartHeight = 2970 - ChartMarginTop;
    private static final int TableMarginTop  = 195;
    private static final int PageNumberTextMarginBottom = 87;
    
    @SuppressWarnings("serial")
    public static class FilenameNotSpecifiedException extends RuntimeException {
        public FilenameNotSpecifiedException(String message) {
            super(message + " " + "Please make sure you have specified path and filename properly.");
        }
    }
    
    /*
     * Authentication
     */
    private String mLicenseCode;
    
    /*
     * Basic information
     */
    private int mYear;
    private int mMaxPageRowCount;
    private int mMaxLineCountPerPage;
    private int mLastDataRow = -1;
    
    private String mPath;
    private String mFilename;
    
    
    public CreateTableImageAbstract() {
        
    }
    
    public CreateTableImageAbstract(String path) {
        this();
        mPath = path;
    }
    
    public CreateTableImageAbstract(String path, String filename) {
        this(path);
        mFilename = filename;
    }
    
    public String getPath() {
        return mPath;
    }
    
    public <T extends CreateTableImageInterface> T setPath(String path) {
        mPath = path;
        return (T) this;
    }
    
    public <T extends CreateTableImageInterface> T setLicenseCode(String license) {
        mLicenseCode = license;
        return (T) this;
    }
    
    public String getFilename() {
        return mFilename;
    }
    
	public <T extends CreateTableImageInterface> T setFilename(String filename) {
        mFilename = filename;
        return (T) this;
    }
    
    public int getYear() {
        return mYear;
    }

	public <T extends CreateTableImageInterface> T setYear(int year) {
        mYear = year;
        return (T) this;
    }
    
	protected <T extends CreateTableImageInterface> T setLastUnprocessedDataRow(int lastDataRow) {
        this.mLastDataRow = lastDataRow;
        return (T) this;
    }
    
    protected int getLastUnprocessedDataRow() {
        return mLastDataRow;
    }
    
    public int getMaxPageRowCount() {
        return mMaxPageRowCount;
    }

	public <T extends CreateTableImageInterface> T setMaxPageRowCount(int maxPageRowCount) {
        if (mMaxPageRowCount != maxPageRowCount) {
            mMaxPageRowCount = maxPageRowCount;
        }
        return (T) this;
    }

    public int getMaxPageLineCount() {
        return mMaxLineCountPerPage;
    }

    public <T extends CreateTableImageInterface> T setMaxPageLineCount(int maxPageLineCount) {
        if (mMaxLineCountPerPage != maxPageLineCount) {
            mMaxLineCountPerPage = maxPageLineCount;
        }
        return (T) this;
    }
    
    //
    // create chart
    //
    
    protected abstract BaseChart createChart(int pageNumber, Object...args);
    
    protected BaseChart makeChart(BaseChart chart, String filename, String extension) {
        chart.makeChart(filename + "." + extension);
        return chart;
    }
    
    //
    // Create table image
    //
    
    protected void checkFileExistence(String parent, String filename) throws FilenameNotSpecifiedException, FileNotFoundException {
        // File existence check
        if ((parent == null || parent.isEmpty())) {
            throw new FilenameNotSpecifiedException("Path not set.");
        }
        else if ((filename == null || filename.isEmpty())) {
            throw new FilenameNotSpecifiedException("Filename not set.");
        }
        else if (!new File(parent, filename).exists()) {
            throw new FilenameNotSpecifiedException("Filename not set.");
        }
    }
    
    protected void checkPdfOutputDirExistence(String path) throws FileNotFoundException {
        checkPdfOutputDirExistence(path, false);
    }
    
    protected void checkPdfOutputDirExistence(String path, boolean createNew) throws FileNotFoundException {
        final File pathDir = new File(path);
        if (pathDir.exists() && !pathDir.isDirectory()) {
            throw new IllegalStateException("The path " + "\'" + path + "\' " + "does not refers to a directory. "
                    + "Please specify a path that does.");
        }
        else if (!pathDir.exists()) {
            if (!createNew) {
                throw new FileNotFoundException("The path " + "\'" + path + "\' does not exist. "
                    + "Please specify an existing one.");
            }
            String[] pathSegments = path.split("\\" + File.separator);
            String dir = "";
            for (int i=0; i<pathSegments.length; i++) {
                File file = new File(dir += pathSegments[i]);
                if (!file.exists()) {
                    file.mkdir();
                }                
                if (i < pathSegments.length) {
                    dir += File.separator;
                }
            }
        }
    }
    
    protected void createTableImage(int pageNumber, Object... args) {
        // File existence check
        final String path = getPath(), filename = getFilename();
        if (path == null || path.isEmpty()) {
            throw new FilenameNotSpecifiedException("Path not set.");
        }
        else if ((filename == null || filename.isEmpty())) {
            throw new FilenameNotSpecifiedException("Filename not set.");
        }
        
        // create chart with the given data
        BaseChart chart = createChart(pageNumber, args);
        
        // create directory
        File pathDir = new File((path.endsWith(File.separator) || path.endsWith("/"))
                                ? path.substring(0, path.length()-1)
                                : path);
        // check path existence
        if (!pathDir.exists()) {
            String[] pathSegments = path.split("\\" + File.separator);
            String dir = "";
            for (int i=0; i<pathSegments.length; i++) {
                final File file = new File(dir += pathSegments[i]);
                if (!file.exists()) {
                    file.mkdir();
                }                
                if (i < pathSegments.length) {
                    dir += File.separator;
                }
            }
        }
        
        // perform chart producing
        makeChart(chart, getPath() + File.separator + getFilename(), "pdf");
    }
}   
