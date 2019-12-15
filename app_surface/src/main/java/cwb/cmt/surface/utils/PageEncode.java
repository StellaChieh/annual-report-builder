package cwb.cmt.surface.utils;

public enum PageEncode {

	STATIONS(1, "1_1" + "_" + "氣象站一覽表"  + "_" + "List of Surface Synoptic Stations"), 	 								  //1.1氣象站一覽表
	CLISUM(2, "1.3 基本氣象綱要表 "), 									 //1.3基本氣象綱要表
	RADSUN(3, "1.4.1 全天空日射量及日照時數"),                             //1.4.1 全天空日射量及日照時數
	GLOBALRAD(4,"1.4.2 全天空日射量"),                 				    //1.4.2 全天空日射量
	RADMAX(5, "1.4.3 全天空一小時最大日射量"),			                   //1.4.3全天空一小時最大日射量
	OTHERSTATIONS(6, "2.2專用氣象觀測站編號一覽表"),					   //2.2專用氣象觀測站編號一覽表
	CE(7,"2.3 氣象要素一覽表"),
	INTERLEAF(8, "Interleaf");										// 第一部分插頁;
	
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

