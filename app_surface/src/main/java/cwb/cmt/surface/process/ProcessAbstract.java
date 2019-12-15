package cwb.cmt.surface.process;

import org.springframework.stereotype.Service;

@Service("processAbstract")
public class ProcessAbstract {
	private int mYear;
	
	private int mPageStart;
	
	private int mCurrentPage;
	
	private boolean mDisplaysPageNumber;
	
//	private ImageToPdf mPdfConverter;	
	
	public ProcessAbstract() {
		super();
	}
	
	public ProcessAbstract(int pageStart) {
		super();
		mPageStart = pageStart;
		mCurrentPage = pageStart;
	}

	public ProcessAbstract setYear(int year) {
		mYear = year;
		return this;
	}
	
	public int getYear() {
		return mYear;
	}
	
	public  ProcessAbstract setPageStart(int pageStart) {
		mPageStart = pageStart;
		mCurrentPage = pageStart;
		return this;
	}
	
	public int getPageStart() {
		return mPageStart;
	}
	
	public int increasePageNumber() {
		return ++mCurrentPage;
	}
	
	public int getCurrentPageNumber() {
		return mCurrentPage;
	}

	public void setDisplayPageNumber(boolean display) {
		mDisplaysPageNumber = display;
	}
    
	public boolean getDisplayPageNumber() {
		return mDisplaysPageNumber;
	}

	//
	// component member injectors
	//
	
//	public ProcessAbstract setPdfConverter(ImageToPdf convertor) {
//		this.mPdfConverter = convertor;
//		return this;
//	}
//	
//	public ImageToPdf getPdfConverter() {
//		return mPdfConverter;
//	}
}
