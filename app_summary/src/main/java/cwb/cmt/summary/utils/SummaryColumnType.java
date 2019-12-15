package cwb.cmt.summary.utils;

import java.util.Arrays;
import java.util.Map;

import com.google.common.collect.Maps;

public enum SummaryColumnType {

	MEAN("mean"),
	TOTAL("total"),
	MIN("min"),
	MAX("max"),
	TOTAL_DAY("totalDay"),
	DUAL_MAX("dualMax"),
	EVAPA_MAX("evapAMax"),
	SEA_PRES_MEAN("seaPresMean");
	
	private final String typeName;
	private static Map<String, SummaryColumnType> map 
		= Maps.uniqueIndex(Arrays.asList(SummaryColumnType.values())
							, SummaryColumnType::getTypeName);
	SummaryColumnType(String name){
		this.typeName= name;
	}
	public String getTypeName() {
		return typeName;
	}
	public static SummaryColumnType lookup(String typeName) {
		return map.get(typeName);
	}
}
