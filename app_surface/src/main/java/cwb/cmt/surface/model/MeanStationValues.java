package cwb.cmt.surface.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class MeanStationValues {
	
	private String stno;
	private String stnCName;
	private String stnBeginTime;
	private String stnEndTime;
	private String columnId;   			//對應的氣象要素種類
	private String columnName;   		//對應的表格欄位名稱
	private LocalDateTime obsTime;
	private String monthlyValue;
	private String flag;
	private boolean substituteZero;
	private LocalDateTime columnTime;
	private String statisticFunction;
	
	public String getStno() {
		return stno;
	}

	public void setStno(String stno) {
		this.stno = stno;
	}

	public LocalDateTime getObsTime() {
		return obsTime;
	}

	public void setObsTime(LocalDateTime obsTime) {
		this.obsTime = obsTime;
	}


	public String getStnEndTime() {
		return stnEndTime;
	}

	public void setStnEndTime(String stnEndTime) {
		this.stnEndTime = stnEndTime;
	}

	public String getStnCName() {
		return stnCName;
	}

	public void setStnCName(String stnCName) {
		this.stnCName = stnCName;
	}

	public String getStnBeginTime() {
		return stnBeginTime;
	}

	public void setStnBeginTime(String stnBeginTime) {
		this.stnBeginTime = stnBeginTime;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getMonthlyValue() {
		return monthlyValue;
	}


	public void setMonthlyValue(String monthlyValue) {
		this.monthlyValue = monthlyValue;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public boolean isSubstituteZero() {
		return substituteZero;
	}

	public void setSubstituteZero(boolean substituteZero) {
		this.substituteZero = substituteZero;
	}

	public LocalDateTime getColumnTime() {
		return columnTime;
	}

	public void setColumnTime(LocalDateTime columnTime) {
		this.columnTime = columnTime;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	

	public String getStatisticFunction() {
		return statisticFunction;
	}

	public void setStatisticFunction(String statisticFunction) {
		this.statisticFunction = statisticFunction;
	}
	
	@Override
	public String toString() {
		return "MeanStationValues [Stno=" + stno + ", stnCName=" + stnCName + 
				", obsTime=" + obsTime + ", stnBeginTime=" + stnBeginTime +
				", stnEndTime=" + stnEndTime + ", columnId=" + columnId +
				", columnName=" + columnName +
				", monthlyValue=" + monthlyValue +
				", flag=" + flag +
			    ", substituteZero=" + substituteZero +
			    ", columnTime=" + columnTime +
			    ", statisticFunction=" + statisticFunction +
				"]\n";
	}

	
}
