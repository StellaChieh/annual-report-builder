package cwb.cmt.summary.composeData;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.springframework.stereotype.Service;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement.Data.SpecialNumber;
import cwb.cmt.summary.model.MonthlyData;

@Service
public class ComposeMinData extends ComposeData {

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
		List<SpecialNumber> specialNumbers = ce.getFirstData().getSpecialNumber();
		boolean isDecimalType = ce.getFirstData().isDecimalType();
		boolean substituteZero = ce.getFirstData().isSubstituteZero();
		
		if( !data.getFirstValue().isPresent() || !data.getTime().isPresent()) {
			return "#";
		}
		
		double value = data.getFirstValue().get();
		String time = data.getTime().get().toString().split("T")[0];
		
		StringJoiner joiner = new StringJoiner("|");
		
		// 特殊值
		Map<Double, String> specialNumbersMap = getSpecialNumberMap(specialNumbers);
		if(specialNumbersMap.containsKey(value)) {
			return specialNumbersMap.get(value);
		}
		
		// 四捨五入到小數點後第一位
		String modifiedNumber = new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
			
		// 替換0
		if(substituteZero && modifiedNumber.equals("0.0")){
			return joiner.add("-").add(time).toString();
		}
				
		// 取到整數位
		if( !isDecimalType && !isStatisticalData ){ 
			modifiedNumber = modifiedNumber.substring(0, modifiedNumber.length()-2);
		}
		
		return joiner.add(modifiedNumber).add(time).toString();
	}
	
	
	private MonthlyData calculateStatistics(ClimaticElement ce, List<MonthlyData> datas) {
		List<MonthlyData> filteredDatas = filterOutSpecialNumber(ce, datas);
		if (filteredDatas.isEmpty()) {
			return new MonthlyData();
		} else {
			return filteredDatas.stream()
								.min(Comparator.comparing(f->f.getFirstValue().get()))
								.get();
		}
	}

}
