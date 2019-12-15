package cwb.cmt.upperair.dataProcess;

public enum DbTable {
	
	HIS_AIRUP("his_airup"),
	HIS_AIRUPCODE("his_airupcode"),
	AIRUP("airup"),
	AIRUPCODE("airupcode");
	
	String tableName;
	
	DbTable(String tableName){
		this.tableName = tableName;
	}
	
	public String value() {
		return this.tableName;
	}
}
