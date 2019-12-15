package cwb.cmt.summary.composeData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement.Data.SpecialNumber;
import cwb.cmt.summary.model.MonthlyData;

@Service
public class ComposeMeanData extends ComposeData {

	@Override
	public MonthlyData calculateSummary(ClimaticElement ce, List<MonthlyData> datas) {
		List<MonthlyData> filteredDatas = filterOutSpecialNumber(ce, datas); 
		if (filteredDatas.isEmpty()) {
			return new MonthlyData();
		} else {
			double mean = filteredDatas.stream()
								.mapToDouble(f -> f.getFirstValue().get())
								.average()
								.getAsDouble();
			MonthlyData m = new MonthlyData();
			m.setFirstValue(Optional.of(mean));
			return m;
		}
	}

	@Override
	public MonthlyData calculateSpanYearsMonthlyData(ClimaticElement ce, List<MonthlyData> datas) {
		List<MonthlyData> filteredDatas = filterOutSpecialNumber(ce, datas);
		if (filteredDatas.isEmpty()) {
			return new MonthlyData();
		} else {
			double mean = filteredDatas.stream()
								.mapToDouble(f -> f.getFirstValue().get())
								.average()
								.getAsDouble();
			MonthlyData m = new MonthlyData();
			m.setMonth(datas.stream().findFirst().get().getMonth());
			m.setFirstValue(Optional.of(mean));
			return m;
		}
	}

	@Override
	public String display(ClimaticElement ce, MonthlyData data, boolean isStatisticData) {
		
		List<SpecialNumber> specialNumbers = ce.getFirstData().getSpecialNumber();
		boolean isDecimalType = ce.getFirstData().isDecimalType();
		boolean substituteZero = ce.getFirstData().isSubstituteZero();
		
		if(!data.getFirstValue().isPresent()) {
			return "#";
		}
		
		// 特殊值
		double value = data.getFirstValue().get();
		Map<Double, String> specialNumbersMap = getSpecialNumberMap(specialNumbers);
		if(specialNumbersMap.containsKey(value)) {
			return specialNumbersMap.get(value);
		}
		
		// 四捨五入到小數點後第一位
		String modifiedNumber = new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
				
		// 是否要將0替換掉
		if(substituteZero && modifiedNumber.equals("0.0")){
			return "-";
		}
				
		// 取到整數位
		if( !isDecimalType && !isStatisticData ){ 
			modifiedNumber = modifiedNumber.substring(0, modifiedNumber.length()-2);
		}
		
		return modifiedNumber;
	}

}