package cwb.cmt.surface.model;

import java.util.List;

public class CeColumn {
	
	private String columnName;
	private String stnPrefix;				//測站編號開頭
	private String precision;				//資料四捨五入至第幾位數
	private String statisticType;			//統計型態
	



	public String getStnPrefix() {
		return stnPrefix;
	}



	public void setStnPrefix(String stnPrefix) {
		this.stnPrefix = stnPrefix;
	}



	public String getPrecision() {
		return precision;
	}



	public void setPrecision(String precision) {
		this.precision = precision;
	}



	public String getStatisticType() {
		return statisticType;
	}



	public void setStatisticType(String statisticType) {
		this.statisticType = statisticType;
	}
	
	
	@Override
	public String toString() {
		return "Column [ stnPrefix=" + stnPrefix +
				", precision=" + precision + ", statisticType=" + statisticType +
				"]\n";
	}



	public String getColumnName() {
		return columnName;
	}



	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
