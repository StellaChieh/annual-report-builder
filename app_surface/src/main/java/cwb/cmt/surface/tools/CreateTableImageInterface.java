package cwb.cmt.surface.tools;

public interface CreateTableImageInterface {
	public String getPath();
    
    public <T extends CreateTableImageInterface> T setPath(String path);
    
    public <T extends CreateTableImageInterface> T setLicenseCode(String license);
    
    
    public String getFilename();
    
    public <T extends CreateTableImageInterface> T setFilename(String filename);

	
    public <T extends CreateTableImageInterface> T setYear(int year);
	
	public int getYear();

	
	public int getMaxPageRowCount();
	
	public <T extends CreateTableImageInterface> T setMaxPageRowCount(int maxPageRowCount);	
	
	
	public int getMaxPageLineCount();
	
	public <T extends CreateTableImageInterface> T setMaxPageLineCount(int maxPageLineCount);
}
