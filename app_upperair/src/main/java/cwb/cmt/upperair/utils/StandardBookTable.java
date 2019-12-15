package cwb.cmt.upperair.utils;

public enum StandardBookTable {

	TABLE1(false, 37),
	TABLE2(false, 37),
	TABLE3(false, 37),
	TABLE4(false, 37),
	TABLE5(true, 38);
	
	private boolean hasSumRow;
	private int rowCount;
	
	StandardBookTable (boolean hasSumRow, int rowCount){
		this.hasSumRow = hasSumRow;
		this.rowCount = rowCount;
	}
	
	public boolean hasSumRow() {
		return hasSumRow;
	}
	
	public int rowCount() {
		return rowCount;
	}
}
