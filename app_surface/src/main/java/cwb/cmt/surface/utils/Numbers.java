package cwb.cmt.surface.utils;


public enum Numbers {
	
	STATIONS_PER_PAGE_STNS(31),			// 各氣象站一覽表，一頁有31個測站
	GLOBALRAD_PER_PAGE_RADS(31),		// 輔助氣象綱要表，全天空日射量，一頁有31個測站
	RADHRMAX_PER_PAGE_RADS(31),			//輔助氣象綱要表，全天空一小時最大日射量，一頁有31個測站
	
	OTHERSTATIONS_PER_PAGE_STNS(240),  //專用氣象觀測站編號一覽表，一頁有240個測站(48*5)
	CLIMATICELEMENT_PER_PAGE(63),      //氣象要素表，一頁有63個測站
	CLIMATICELEMENT2_PER_PAGE(31);    //氣象要素表，一頁有31個測站
	private final int number;
	
	private Numbers(int number){
		this.number = number;
	}
	
	public int getNumber(){
		return this.number;
	}
	
}
