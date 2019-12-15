package cwb.cmt.summary.composeData;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.config.Config;
import cwb.cmt.summary.dao.DbInteract;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.MonthlyData;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.model.SummaryModel;

@Service
@Scope(value="prototype")
public class MakeDataService {

	protected ClimaticElement ce;
	protected Station station;
	protected ComposeData composeDataService;
	protected DbInteract dbInteraction;
	
	@Autowired
	protected Config config;	
	protected String stno;
	protected String ceId;
		
	public void setDbInteraction(DbInteract dbInteraction) {
		this.dbInteraction = dbInteraction;
	}
	
	public void setComposeDataService(ComposeData composeDataService) {
		this.composeDataService = composeDataService;
	}

	public void setClimaticElementNStation(ClimaticElement ce, Station station){
		this.ce = ce;
		this.station = station;
		this.ceId = ce.getId();
		this.stno = station.getStno();
	}
	
	protected boolean isDummyCondition() {
		return !ce.isIsAutoStnMeasure() && station.isAuto();
	}
	
	public List<SummaryModel> createDatas() {
		int initYear = dbInteraction.queryInitYear().get(Collections.singletonMap(stno, ceId));
		int thisYear = config.getYear();
		
		List<SummaryModel> resultList;
		// 此觀測項目在無人測站不觀測 && 此測站是無人測站
		if(isDummyCondition()) {
			resultList = IntStream.range(0, 10)
									.mapToObj(no -> createSingleYearDummySummaryModel(thisYear+no, no+1))
									.collect(Collectors.toList());
			resultList.add(createSpanYearsDummySummaryModel(thisYear, thisYear+9, 11));
			resultList.add(createSpanYearsDummySummaryModel(thisYear-10, thisYear-1, 12));
			resultList.add(createSpanYearsDummySummaryModel(initYear, thisYear+9, 13));
			return resultList;
		} else {
			// single year not yet detected
			resultList = IntStream.range(0 , 10)
									.filter(i -> thisYear+i < initYear)
									.mapToObj(i -> makeRowData(false, thisYear+i, thisYear+i, i+1))
									.collect(Collectors.toList());
			// single year already detected
			resultList.addAll(IntStream.range(0 , 10)
									.filter(i -> thisYear+i >= initYear)
									.mapToObj(i -> makeRowData(true, thisYear+i, thisYear+i, i+1))
									.collect(Collectors.toList()));
			
			boolean isDetected = false;
			int queryBeginYear = Integer.MIN_VALUE;
			
			// thisYear ~ thisYear+9
			queryBeginYear = initYear > thisYear ? initYear : thisYear;
			isDetected = initYear <= thisYear+9 ? true: false;
			resultList.add(makeRowData(isDetected, queryBeginYear, thisYear+9, 11));
			
			// thisYear-10 ~ thisYear-1
			queryBeginYear = initYear > thisYear-10 ? initYear : thisYear-10;
			isDetected = initYear <= thisYear-1 ? true: false;
			resultList.add(makeRowData(isDetected, queryBeginYear, thisYear-1, 12));
			
			// initYear ~ thisYear+9
			isDetected = initYear <= thisYear+9 ? true: false;
			resultList.add(makeRowData(isDetected, initYear, thisYear+9, 13));
			
			return resultList;
		}
	}
	
	protected SummaryModel makeRowData (boolean isDetected, int beginYear, int endYear, int no) {
		if(!isDetected) {
			if(no <= 10) {
				return createSingleYearDummySummaryModel(beginYear, no);
			} else {
				return createSpanYearsDummySummaryModel(beginYear, endYear, no);
			} 
		} else {
			List<MonthlyData> months = new ArrayList<>();
			if(no <= 10) {
				for(int i=1; i<=12; i++) {
					months.add(querySingleYearMonthlyData(beginYear, Month.of(i)));
				}
				return composeSingleYearSummaryModel(months, beginYear, no);
			} else {
				for(int i=1; i<=12; i++) {
					months.addAll(querySpanYearsMonthlyData(beginYear, endYear, Month.of(i)));
				}
				return composeSpanYearsSummaryModel(months, beginYear, endYear,  no);
			}
			
		}
	}
	
	public void insertIntoDb(List<SummaryModel> list) {
		dbInteraction.insertSummaryModel(list);
	}
	
	protected MonthlyData querySingleYearMonthlyData(int year, Month month) {
		return dbInteraction.queryHisCwbmnSingleYearMonthlyData(ce, stno, year, month);
	} 
	
	protected List<MonthlyData> querySpanYearsMonthlyData(int beginYear, int endYear, Month month) {
		return dbInteraction.queryHisCwbmnSpanYearsMonthlyData(ce, stno, beginYear, endYear, month);
	}
	
	protected SummaryModel createSingleYearDummySummaryModel(int year, int no) {
		return composeDataService.createSingleYearDummySummaryModel(stno, ceId, year, no);
	}
	
	protected SummaryModel createSpanYearsDummySummaryModel(int beginYear, int endYear, int no) {
		return composeDataService.createSpanYearsDummySummaryModel(stno, ceId, beginYear, endYear, no);
	}
	
	protected SummaryModel composeSingleYearSummaryModel(List<MonthlyData> months, int year, int no) {
		return composeDataService.getSingleYearRowData(stno, ce, months, year, no);
	}
	
	protected SummaryModel composeSpanYearsSummaryModel(List<MonthlyData> months, int beginYear, int endYear, int no) {
		return composeDataService.getSpanYearsRowData(stno, ce, months, beginYear, endYear,  no);
	}

}
