package cwb.cmt.summary.dao;

import java.time.Month;
import java.util.List;
import java.util.Map;

import cwb.cmt.summary.model.InitYear;
import cwb.cmt.summary.model.MonthlyData;
import cwb.cmt.summary.model.StnMoves;
import cwb.cmt.summary.model.SummaryModel;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;

public interface DbInteract {

	List<Station> queryStationData(List<Station> xmlStations);

	List<StnMoves> queryStnMoves();

	MonthlyData queryHisCwbmnSingleYearMonthlyData(ClimaticElement ce, String stno, int year, Month month);

	List<MonthlyData> queryHisCwbmnSpanYearsMonthlyData(ClimaticElement ce, String stno, int beginYear, int endYear, Month month);

	InitYear<Map<String, String>, Integer> queryInitYear();

	List<SummaryModel> queryForStnCliEle(String stno, String ceId);

	SummaryModel queryForOneStnCliEle(int no, String stno, String ceId);

	void insertSummaryModel(List<SummaryModel> list);

	void clearSummaryModelTable();

}