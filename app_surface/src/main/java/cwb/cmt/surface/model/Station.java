package cwb.cmt.surface.model;

// need rework, attributes don't fit with db and table
public class Station {
	
	private String stno;
	private String stnCName;
	private String stnEName;
	
	private String classify;
	private String city;
	private String address;
	private String charge_ins;
	
	private String latitude;
	private String longitude;
	private String altitude;
	
	private String hBarometer;
	private String hTherm;
	private String hRaingauge;
	private String hAnem;

	private String stnBeginTime;
	private String stnEndTime;
	private String manObsNum;
	
	
	/*
	 * getter and setter
	 */
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
	
	public String getHBarometer() {
		return hBarometer;
	}
	public void setHBarometer(String hBarometer) {
		this.hBarometer = hBarometer;
	}
	public String getHTherm() {
		return hTherm;
	}
	public void setHTherm(String hTherm) {
		this.hTherm = hTherm;
	}
	public String getHRaingauge() {
		return hRaingauge;
	}
	public void setHRaingauge(String hRaingauge) {
		this.hRaingauge = hRaingauge;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getStnBeginTime() {
		return stnBeginTime;
	}
	public void setStnBeginTime(String beginTime) {
		this.stnBeginTime = beginTime;
	}
	public String getStnEndTime() {
		return stnEndTime;
	}
	public void setStnEndTime(String stnEndTime) {
		this.stnEndTime = stnEndTime;
	}
	public String getManObsNum() {
		return manObsNum;
	}
	public void setManObsNum(String manObsNum) {
		this.manObsNum = manObsNum;
	}
	
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCharge_ins() {
		return charge_ins;
	}
	public void setCharge_ins(String charge_ins) {
		this.charge_ins = charge_ins;
	}
	public String gethAnem() {
		return hAnem;
	}
	public void sethAnem(String hAnem) {
		this.hAnem = hAnem;
	}
	
	
	@Override
	public boolean equals(Object obj){
		if (obj instanceof Station){
			Station other = (Station)obj;
			if (other.getStno().equals(this.getStno())){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "Station [stno=" + stno + ", stnCName=" + stnCName + ", stnEName=" + stnEName 
				+", classify=" + classify +", city="+ city + ", charge_ins=" + charge_ins
				+ ", address=" + address + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", hBarometer=" + hBarometer 
				+ ", hTherm=" + hTherm + ", hRaingauge=" + hRaingauge + ", hAnem=" + hAnem 
				+ ", altitude=" + altitude + ", beginTime=" + stnBeginTime +" ,endTime=" + stnEndTime
				+ ", manObsNum=" + manObsNum + "]\n";
	}
	

}