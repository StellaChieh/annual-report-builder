package cwb.cmt.summary.utils;

public enum Numbers {
	
	STATIONS_PER_PAGE_STNS(26),    // 測站一覽表，一頁有26個測站
	ROWS_PER_PAGE_STNMOVES(28),     // 測站變遷記錄一覽表，一頁有31列
	
	COLUMNS_OF_CONTENTS(13),       // 總目錄頁，一頁總共有13欄
	ROWS_OF_CONTENTS(45),          // 總目錄頁，一頁總共有45列
	
	ROWS_OF_PART_ONE_CONTENTS(44), // 第一部分目錄，一頁總共有44列
	ROWS_OF_PART_TWO_CONTENTS(35), // 第二部分目錄，一頁總共有35列
	
	STATIONS_PER_PAGE_PART_ONE(26), // 第一部分圖表，一頁紀錄26個測站
	WINDROSE_PER_PAGE_PART_ONE(9),  // 第一部分風花圖，一頁紀錄9個測站
	
	WINDROSE_PAGE_PER_STATION_PART_TWO(2);   // 第二部分風花圖，一個風花圖佔兩頁
	
	private final int number;
	
	private Numbers(int number){
		this.number = number;
	}
	
	public int getNumber(){
		return this.number;
	}
	
}
