package cwb.cmt.upperair.utils;

public enum SignificantBookTable {
	
	TABLE1(1), 
	TABLE2(5),
	TABLE3(9),
	TABLE4(13),
	TABLE5(17),
	TABLE6(21),
	TABLE7(25),
	TABLE8(29);
	
	int beginDate;
	
	SignificantBookTable(int beginDate){
		this.beginDate = beginDate;
	}
	
	public int getBeginDate() {
		return this.beginDate;
	}
}
