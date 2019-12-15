package cwb.cmt.upperair.model;

public class Station {
	
	private String stno;
	private String stnCName;
	private String stnEName;
	private String latitude;
	private String longitude;
	private String hBarometer;
	private String printedHBarometer;
	
	public Station() {}
	
	public String gethBarometer() {
		return hBarometer;
	}

	public void sethBarometer(String hBarometer) {
		this.hBarometer = hBarometer;
	}

	public Station(String stno) {
		this.stno = stno;
	}
	
	public String getStno() {
		return stno;
	}
	public void setStno(String stno) {
		this.stno = stno;
	}
	public String getStnCName() {
		return stnCName;
	}
	public void setStnCName(String locationChinese) {
		this.stnCName = locationChinese;
	}
	public String getStnEName() {
		return stnEName;
	}
	public void setStnEName(String locationEnglish) {
		this.stnEName = locationEnglish;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getPrintedHBarometer() {
		setPrintedHBarometer();
		return printedHBarometer;
	}

	private void setPrintedHBarometer() {
		printedHBarometer = this.hBarometer.replace("m", "0");
		this.printedHBarometer = String.valueOf(Double.parseDouble(printedHBarometer));
	}

	@Override
	public String toString() {
		return "Station [stno=" + stno + ", stnCName=" + stnCName + ", stnEName=" + stnEName + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", hBarometer=" + hBarometer + "]\n";
	}

}