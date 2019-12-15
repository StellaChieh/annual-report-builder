package cwb.cmt.summary.composeData;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.MonthlyData;
import cwb.cmt.summary.model.SummaryModel;

public abstract class ComposeData implements Filter, CalculateSpanYearsData, CalculateSummary, Display, CreateDummy {

	public SummaryModel getSingleYearRowData(String stno, ClimaticElement ce, List<MonthlyData> datas, int year, int no) {
		List<String> all = datas.stream()
									.map(data -> display(ce, data, false))
									.collect(Collectors.toList());
		String summary = display(ce, calculateSummary(ce, datas), true);
		all.add(summary);
		return createSummaryModel(no, stno, ce.getId(), year, year, all);
	}

	public SummaryModel getSpanYearsRowData(String stno, ClimaticElement ce, List<MonthlyData> datas, int beginYear, int endYear, int no) {
		List<MonthlyData> monthlys = new ArrayList<MonthlyData>(); 
		for(int i=1; i<=12; i++) {
			int j = i;
			List<MonthlyData> monthlyData = datas.stream()
													.filter(f -> f.getMonth().equals(Month.of(j)))
													.collect(Collectors.toList());
			monthlys.add(calculateSpanYearsMonthlyData(ce, monthlyData));
		}
		
		monthlys.add(calculateSummary(ce, monthlys.stream().collect(Collectors.toList())));
		
		List<String> all = monthlys.stream()
									.map(f -> display(ce, f, true))
									.collect(Collectors.toList());
		
		return createSummaryModel(no, stno, ce.getId(), beginYear, endYear, all); 
		
	}
	
	private SummaryModel createSummaryModel(int no, String stno, String ceId, int beginYear, int endYear, List<String> rowDatas) {
		SummaryModel s = new SummaryModel();
		s.setNo(no);
		s.setStno(stno);
		s.setClimaticElement(ceId);
		s.setBeginYear(beginYear);
		s.setEndYear(endYear);
		for(int i=0; i<13; i++) {
			String e = rowDatas.get(i);
			switch (i) {
				case 0:
					s.setJan(e);
					break;
				case 1:
					s.setFeb(e);
					break;
				case 2:
					s.setMar(e);
					break;
				case 3:
					s.setApr(e);
					break;
				case 4:
					s.setMay(e);
					break;
				case 5:
					s.setJun(e);
					break;
				case 6:
					s.setJul(e);
					break;
				case 7:
					s.setAug(e);
					break;
				case 8:
					s.setSep(e);
					break;
				case 9:
					s.setOct(e);
					break;
				case 10:
					s.setNov(e);
					break;
				case 11:
					s.setDec(e);
					break;
				default:	
					s.setSummary(e);
			}
		}
		return s;
	}

}