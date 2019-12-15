package cwb.cmt.upperair.utils;

public class PageInfo {

	private int pageOrder;
	private PdfPageType pageType;
	private String filename;
	private String printedPageNumber;
	private String filePath;
		
	public PageInfo(int pageOrder, String printedPageNumber, PdfPageType pageType) {
		this.pageOrder = pageOrder;
		this.printedPageNumber = printedPageNumber;
		this.pageType = pageType;
	}
		
	public PdfPageType getPageType() {
		return pageType;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFilename() {
		return filename;
	}

	public int getPageOrder() {
		return pageOrder;
	}

	public String getPrintedPageNumber() {
		return printedPageNumber;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return "PageInfo [pageOrder=" + pageOrder + ", pageType=" + pageType + ", filename=" + filename
				+ ", printedPageNumber=" + printedPageNumber + ", filePath=" + filePath + "]\n";
	}

}