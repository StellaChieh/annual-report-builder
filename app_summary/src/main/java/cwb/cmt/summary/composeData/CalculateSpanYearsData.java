package cwb.cmt.summary.composeData;

import java.util.List;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.MonthlyData;

public interface CalculateSpanYearsData {

	MonthlyData calculateSpanYearsMonthlyData (ClimaticElement ce, List<MonthlyData> datas);
}
