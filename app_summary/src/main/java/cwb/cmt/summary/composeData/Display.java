package cwb.cmt.summary.composeData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement.Data.SpecialNumber;
import cwb.cmt.summary.model.MonthlyData;

public interface Display {
	
	default Map<Double, String> getSpecialNumberMap(List<SpecialNumber> specialNumbers) {
		return specialNumbers.stream()
							.collect(Collectors
									.toMap(SpecialNumber::getNumber, SpecialNumber::getSymbol));
	}
	
	
	String display(ClimaticElement ce, MonthlyData data, boolean isStatisticalData);

}