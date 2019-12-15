package cwb.cmt.summary.utils;

public enum PageEncode {

	COVER(1, "cover"), 						//封面
	EXAMPLE(2, "example"), 					//凡例
	INTRODUCTION(3, "introduction"), 		//INTRODUCTION
	STATIONS(4, "stations"), 				// 氣象站一覽表
	CONTENTS(5, "contents"), 				// 總目錄
	STATION_MOVES(6, "stationMoves"), 		// 變遷記錄一覽表
	INTERLEAF_ONE(7, "partOneInterleaf"), 	// 第一部分插頁
	CONTENTS_ONE(8, "partOneContents"), 	// 第一部分目錄
	TABLE_ONE(9, "partOneTable"),  			// 第一部分表格 
	INTERLEAF_TWO(10, "partTwoInterleaf"),  // 第二部分插頁
	CONTENTS_TWO(11, "partTwoContents"),    // 第二部分目錄
	STN_INTERLEAF(12, "stnInterleaf"),      // 第二部分測站插頁
	TABLE_TWO(12, "partTwoTable"),  		// 第二部分表格
	COPYRIGHT(13, "copyright");  			// 版權頁
	
	private final int number;
	private final String filename;
	
	private PageEncode(int number, String fileName){
		this.number = number;
		this.filename = fileName;
	}
	
	public int getEncode(){
		return this.number;
	}
	
	public String getFilename(){
		return this.filename;
	}
	
}
