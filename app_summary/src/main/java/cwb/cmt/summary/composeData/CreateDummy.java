package cwb.cmt.summary.composeData;

import cwb.cmt.summary.model.SummaryModel;

public interface CreateDummy {

	default SummaryModel createDummySummaryModel (String stno, String climaticElement
										, int beginYear, int endYear, int no){
		
		SummaryModel s = new SummaryModel();
		s.setNo(no);
		s.setStno(stno);
		s.setClimaticElement(climaticElement);
		s.setBeginYear(beginYear);
		s.setEndYear(endYear);
		s.setJan("#");
		s.setFeb("#");
		s.setMar("#");
		s.setApr("#");
		s.setMay("#");
		s.setJun("#");
		s.setJul("#");
		s.setAug("#");
		s.setSep("#");
		s.setOct("#");
		s.setNov("#");
		s.setDec("#");
		s.setSummary("#");
		return s;
	}
	
	default SummaryModel createSingleYearDummySummaryModel (String stno, String climaticElement
															, int year, int no){
		return createDummySummaryModel (stno, climaticElement, year, year, no);
	}
	
	default SummaryModel createSpanYearsDummySummaryModel (String stno, String climaticElement
													, int beginYear, int endYear, int no){
		return createDummySummaryModel (stno, climaticElement, beginYear, endYear, no);
	}
}
