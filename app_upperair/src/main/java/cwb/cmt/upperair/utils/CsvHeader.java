package cwb.cmt.upperair.utils;

public enum CsvHeader {
	stnCName("測站"),
	stnEName("STATION"),
	hBarom("氣壓計海拔高度 BAROMETER ABOVE MSL(gpm)"),
	year("年 YEAR"),
	month("月 MONTH"), 
	date("日 DATE"),
	hour("觀測時間 HOUR"),
	latitude("緯度 LATITUDE"),
	logitute("經度 LOGITUDE"),
	synoptic("SYNOPTIC NLHMCwwapp"),
	pressureLevel("Pressure Level"),
	attribute("資料屬性 Data Attribute"),
	p("P(hPa)"),
	H("H(gpm)"),
	T("T(\u00B0C)"),
	U("U(%)"),
	Td("Td(\u00B0C)"),
	dd("dd(deg)"),
	ff("ff(m/sec)"),
	SQ("SQ");
	String name;
	CsvHeader(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
