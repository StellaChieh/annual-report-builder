package cwb.cmt.summary.composeData;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import org.springframework.stereotype.Service;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement.Data.SpecialNumber;
import cwb.cmt.summary.model.MonthlyData;

@Service
public class ComposeDualMaxData extends ComposeData {

	@Override
	public MonthlyData calculateSpanYearsMonthlyData(ClimaticElement ce, List<MonthlyData> datas) {
		return calculateStatistics(ce, datas);
	}

	@Override
	public MonthlyData calculateSummary(ClimaticElement ce, List<MonthlyData> datas) {
		return calculateStatistics(ce, datas);
	}

	@Override
	public String display(ClimaticElement ce, MonthlyData data, boolean isStatisticalData) {
		
		List<SpecialNumber> firstSpecialNumbers = ce.getFirstData().getSpecialNumber();
		boolean firstIsDecimalType = ce.getFirstData().isDecimalType();
		boolean firstSubstituteZero = ce.getFirstData().isSubstituteZero();
		String firstValue = displaySingleNumber(data.getFirstValue(), firstIsDecimalType
							, firstSubstituteZero, firstSpecialNumbers, isStatisticalData);

		List<SpecialNumber> secondSpecialNumbers = ce.getSecondData().get().getSpecialNumber();
		boolean secondIsDecimalType = ce.getSecondData().get().isDecimalType();
		boolean secondSubstituteZero = ce.getSecondData().get().isSubstituteZero();
		String secondValue = displaySingleNumber(data.getSecondValue(), secondIsDecimalType
							, secondSubstituteZero, secondSpecialNumbers, isStatisticalData);
		
		String time = data.getTime().isPresent() ? data.getTime().get().toString().split("T")[0] : "#";
		
		if(firstValue.equals("#") && secondValue.equals("#") && time.equals("#")) {
			return "#";
		}
		
		return new StringJoiner("|").add(firstValue).add(time).add(secondValue).toString();
	}

	private String displaySingleNumber(Optional<Double> value, boolean isDecimalType
									, boolean substituteZero, List<SpecialNumber> specialNumbers
									, boolean isStatisticalData) {
		if (!value.isPresent()) {
			return "#";
		}

		// 特殊值
		Map<Double, String> specialNumbersMap = getSpecialNumberMap(specialNumbers);
		if(specialNumbersMap.containsKey(value.get())) {
			return specialNumbersMap.get(value.get());
		}
		
		// 四捨五入到小數點後第一位
		String modifiedNumber = new BigDecimal(value.get())
				.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
			
		// 替換0
		if(substituteZero && modifiedNumber.equals("0.0")){
			return "-";
		}
				
		// 取到整數位
		if( !isDecimalType && !isStatisticalData ){ 
			modifiedNumber = modifiedNumber.substring(0, modifiedNumber.length()-2);
		}
		
		return modifiedNumber;

	}

	private MonthlyData calculateStatistics(ClimaticElement ce, List<MonthlyData> datas) {
		List<MonthlyData> filteredDatas = filterOutSpecialNumber(ce, datas);
		if (filteredDatas.isEmpty()) {
			return new MonthlyData();
		} else {
			return filteredDatas.stream().max(Comparator.comparing(f -> f.getFirstValue().get())).get();
		}
	}

}
