package cwb.cmt.surface.model;



public class CeTables {
	
	private String tableName;               //表格名稱
	private String columnId;				//氣象要素ID
	private String columnName;				//表格欄位名稱
	private String columnTime;				//觀測時間欄位名稱
	private String stnPrefix;				//測站編號開頭
	private String statisticFunction;			//統計型態
	

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getStnPrefix() {
		return stnPrefix;
	}

	public void setStnPrefix(String stnPrefix) {
		this.stnPrefix = stnPrefix;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getColumnTime() {
		return columnTime;
	}

	public void setColumnTime(String columnTime) {
		this.columnTime = columnTime;
	}

	public String getStatisticFunction() {
		return statisticFunction;
	}

	public void setStatisticFunction(String statisticFunction) {
		this.statisticFunction = statisticFunction;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	
	@Override
	public String toString() {
		return "CeTables [tableName=" + tableName +", columnId="+ columnId +
				", columnName="+columnName +
				", columnTime="+columnTime +
				", stnPrefix=" + stnPrefix +
				", statisticFunction=" + statisticFunction +
				"]\n";
	}
}
