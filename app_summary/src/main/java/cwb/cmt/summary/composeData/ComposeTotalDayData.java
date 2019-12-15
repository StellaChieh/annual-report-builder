package cwb.cmt.summary.composeData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement.Data.SpecialNumber;
import cwb.cmt.summary.model.MonthlyData;

/* 
 * 表格以"日數"和"total"標記者，採用此
 * 在config檔裡，則是對應到'totalDay'
 * 
 */
@Service
public class ComposeTotalDayData extends ComposeData {

	/*
	 *  跨年度計算方式：例如：2001-2010者，資料是：1, null, 6, 7, null, 1, null, null, null, 一個特殊值，
	 *	則計算方式為  (1+6+7+1)/(10-1)。 分母僅扣除特殊值個數，不扣除null值個數 
	 */
	@Override
	public MonthlyData calculateSpanYearsMonthlyData(ClimaticElement ce, List<MonthlyData> datas) {
		List<MonthlyData> filteredDatas = filterOutSpecialNumber(ce, datas);
		MonthlyData m = new MonthlyData();
		m.setBeginYear(datas.stream().findAny().get().getBeginYear());
		m.setEndYear(datas.stream().findAny().get().getEndYear());
		if (filteredDatas.isEmpty()) {
			return m;
		} else {
			double total = filteredDatas.stream()
									.filter(f -> f.getFirstValue().isPresent())
									.mapToDouble( f -> f.getFirstValue().get().doubleValue())
									.sum();
			long count = filteredDatas.stream().count();
			m.setFirstValue(Optional.ofNullable(total/count));
			m.setMonth(datas.stream().findAny().get().getMonth());
			return m;
		}
	}

	/*
	 * summary為去除null, 特殊值後的列總和
	 */
	@Override
	public MonthlyData calculateSummary(ClimaticElement ce, List<MonthlyData> datas) {
		List<MonthlyData> filteredDatas = filterOutSpecialNumber(ce, datas)
											.stream()
											.filter(f->f.getFirstValue().isPresent())
											.collect(Collectors.toList());
		
		MonthlyData m = new MonthlyData();
		m.setBeginYear(datas.stream().findAny().get().getBeginYear());
		m.setEndYear(datas.stream().findAny().get().getEndYear());
	
		if (filteredDatas.isEmpty()) {
			return m;
		} else {
			double total = filteredDatas.stream()
									.mapToDouble( f -> f.getFirstValue().get().doubleValue())
									.sum();
			m.setFirstValue(Optional.ofNullable(total));
			return m;
		}
	}

	@Override
	public String display(ClimaticElement ce, MonthlyData data, boolean isStatisticalData) {
		
		List<SpecialNumber> specialNumbers = ce.getFirstData().getSpecialNumber();
		boolean substituteZero = ce.getFirstData().isSubstituteZero();
		
		if(!data.getFirstValue().isPresent()) {
			return "-";
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
		if( data.getBeginYear() == data.getEndYear() ){ 
			modifiedNumber = modifiedNumber.substring(0, modifiedNumber.length()-2);
		}
		
		return modifiedNumber;
	}
	
	@Override
	public List<MonthlyData> filterOutSpecialNumber(ClimaticElement ce, List<MonthlyData> datas) {

		List<Double> firstDataSpecialNumbers = ce.getFirstData().getSpecialNumber()
																.stream()
																.map(f -> f.getNumber())
																.collect(Collectors.toList());
		// filter out special number number only
		// null first data is preserved
		return datas.stream()
					.filter(data -> data != null)
					.filter(data -> !data.getFirstValue().isPresent() 
								|| !firstDataSpecialNumbers.contains(data.getFirstValue().get()))
					.collect(Collectors.toCollection(ArrayList::new)); 
	}

}
