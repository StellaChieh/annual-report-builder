package cwb.cmt.summary.model;

public class SummaryModel {
	
	private int no; // no in db, 1, 2, 3, ... 10 mean single year
					// 10, 11, 12 mean span year    
	private String stno;            
	private String climaticElement;
	private int beginYear;
	private int endYear;

	private String jan;
	private String feb;
	private String mar;
	private String apr;
	private String may;
	private String jun;
	private String jul;
	private String aug;
	private String sep;
	private String oct;
	private String nov;
	private String dec;
	private String summary;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getStno() {
		return stno;
	}
	public void setStno(String stno) {
		this.stno = stno;
	}
	public String getClimaticElement() {
		return climaticElement;
	}
	public void setClimaticElement(String climaticElement) {
		this.climaticElement = climaticElement;
	}
	public int getBeginYear() {
		return beginYear;
	}
	public void setBeginYear(int beginYear) {
		this.beginYear = beginYear;
	}
	public int getEndYear() {
		return endYear;
	}
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
	public String getJan() {
		return jan;
	}
	public void setJan(String jan) {
		this.jan = jan;
	}
	public String getFeb() {
		return feb;
	}
	public void setFeb(String feb) {
		this.feb = feb;
	}
	public String getMar() {
		return mar;
	}
	public void setMar(String mar) {
		this.mar = mar;
	}
	public String getApr() {
		return apr;
	}
	public void setApr(String apr) {
		this.apr = apr;
	}
	public String getMay() {
		return may;
	}
	public void setMay(String may) {
		this.may = may;
	}
	public String getJun() {
		return jun;
	}
	public void setJun(String jun) {
		this.jun = jun;
	}
	public String getJul() {
		return jul;
	}
	public void setJul(String jul) {
		this.jul = jul;
	}
	public String getAug() {
		return aug;
	}
	public void setAug(String aug) {
		this.aug = aug;
	}
	public String getSep() {
		return sep;
	}
	public void setSep(String sep) {
		this.sep = sep;
	}
	public String getOct() {
		return oct;
	}
	public void setOct(String oct) {
		this.oct = oct;
	}
	public String getNov() {
		return nov;
	}
	public void setNov(String nov) {
		this.nov = nov;
	}
	public String getDec() {
		return dec;
	}
	public void setDec(String dec) {
		this.dec = dec;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	@Override
	public String toString() {
		return "SummaryModel [no=" + no + ", stno=" + stno + ", climaticElement=" + climaticElement + ", beginYear="
				+ beginYear + ", endYear=" + endYear + ", jan=" + jan + ", feb=" + feb + ", mar=" + mar + ", apr=" + apr
				+ ", may=" + may + ", jun=" + jun + ", jul=" + jul + ", aug=" + aug + ", sep=" + sep + ", oct=" + oct
				+ ", nov=" + nov + ", dec=" + dec + ", summary=" + summary + "]\n";
	}
	
}
