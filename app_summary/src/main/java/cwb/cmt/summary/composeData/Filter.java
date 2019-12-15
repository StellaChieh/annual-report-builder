package cwb.cmt.summary.composeData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.MonthlyData;

public interface Filter {

	default List<MonthlyData> filterOutSpecialNumber(ClimaticElement ce, List<MonthlyData> datas) {

		
		List<Double> firstDataSpecialNumbers = ce.getFirstData().getSpecialNumber()
																.stream()
																.map(f -> f.getNumber())
																.collect(Collectors.toList());
		// filter out null firstData and special number
		List<MonthlyData> tmp = datas.stream()
										.filter(data -> data != null)
										.filter(data -> data.getFirstValue().isPresent())
										.filter(data -> !firstDataSpecialNumbers.contains(data.getFirstValue().get()))
										.collect(Collectors.toCollection(ArrayList::new)); 
		
		// filter out null secondData and special number
		if(ce.getSecondData().isPresent()) {
			
			List<Double> secondDataSpecialNumbers = ce.getFirstData().getSpecialNumber()
																	.stream()
																	.map(f -> f.getNumber())
																	.collect(Collectors.toList());
			
			tmp = tmp.stream()
					.filter(data -> data.getSecondValue().isPresent())
					.filter(data -> !secondDataSpecialNumbers.contains(data.getFirstValue().get()))
					.collect(Collectors.toCollection(ArrayList::new));
		}
		
		// filter out null time
		if(ce.getTime().isPresent()) {
			tmp = tmp.stream()
						.filter(data -> data.getTime().isPresent())
						.collect(Collectors.toCollection(ArrayList::new));		
		}
		 			
		return tmp;
	}
}
