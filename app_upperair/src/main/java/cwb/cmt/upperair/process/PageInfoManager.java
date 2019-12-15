package cwb.cmt.upperair.process;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import cwb.cmt.upperair.utils.PageInfo;
import cwb.cmt.upperair.utils.PdfPageType;
import cwb.cmt.upperair.utils.RomanNumber;

@Component
public class PageInfoManager {
	
	private List<PageInfo> pageInfos;
	private int accumPageOrder = 1;
	private int accumPrintedRomanNumber = 1;
	private int accumPrintedAbrabicNumber = 1;
		
	private void addOneRomanFooterPage() {
		accumPrintedRomanNumber++;
	}
	
	private void addOneArabicFooterPage() {
		accumPrintedAbrabicNumber++;
	}
	
	public PageInfoManager() {
		if(this.pageInfos == null) {
			this.pageInfos = new ArrayList<>();
		}
	}
	
	public PageInfo createNewPageInfo(PdfPageType pageType) {
		PageInfo pageInfo;
		if(!isEmptyPageNumber(pageType)) {
			if(isRomanPage(pageType)) {
				pageInfo = new PageInfo(accumPageOrder, RomanNumber.toRoman(accumPrintedRomanNumber), pageType);
				addOneRomanFooterPage();
			} else {
				pageInfo = new PageInfo(accumPageOrder, String.valueOf(accumPrintedAbrabicNumber), pageType);
				addOneArabicFooterPage();
			}
		} else {
			pageInfo = new PageInfo(accumPageOrder, " ", pageType);
		}
		
		addOnePage(pageInfo);
		return pageInfo;
	} 
	
	private int addOnePage(PageInfo pageInfo) {
		accumPageOrder++;
		this.pageInfos.add(pageInfo);
		return this.pageInfos.size();
	}
		
	public List<PageInfo> getPageInfos(){
		return this.pageInfos;
	}
	
	private boolean isRomanPage(PdfPageType pageType) {
		if(pageType == PdfPageType.CONTENTS 
				|| pageType == PdfPageType.REFERENCE_NOTE) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isEmptyPageNumber(PdfPageType pageType) {
		if(pageType == PdfPageType.COVER 
				|| pageType == PdfPageType.INTERLEAF
				|| pageType == PdfPageType.COPYRIGHT) {
			return true;
		} else {
			return false;
		}
	}
}