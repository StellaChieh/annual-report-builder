package cwb.cmt.summary.model;

import java.sql.Date;

public class StnMoves {

	private String stno;
	private Date beginTime;
	private Date endTime;
	private String longitude;
	private String latitude;
	private String hBarometer;
	private String hTherm;
	private String hRaingauge;
	private String hAnem;
	private String altitude;
	private String stnCName;
	private String stnEName;
	
	public String getStno() {
		return stno;
	}
	public void setStno(String stno) {
		this.stno = stno;
	}
	
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String gethBarometer() {
		return hBarometer;
	}
	public void sethBarometer(String hBarometer) {
		this.hBarometer = hBarometer;
	}
	public String gethTherm() {
		return hTherm;
	}
	public void sethTherm(String hTherm) {
		this.hTherm = hTherm;
	}
	public String gethRaingauge() {
		return hRaingauge;
	}
	public void sethRaingauge(String hRaingauge) {
		this.hRaingauge = hRaingauge;
	}
	public String gethAnem() {
		return hAnem;
	}
	public void sethAnem(String hAnem) {
		this.hAnem = hAnem;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getStnCName() {
		return stnCName;
	}
	public void setStnCName(String stnCName) {
		this.stnCName = stnCName;
	}
	public String getStnEName() {
		return stnEName;
	}
	public void setStnEName(String stnEName) {
		this.stnEName = stnEName;
	}
	@Override
	public String toString() {
		return "StnMoves [stno=" + stno + ", beginTime=" + beginTime + ", endTime=" + endTime + ", longitude="
				+ longitude + ", latitude=" + latitude + ", hBarometer=" + hBarometer + ", hTherm=" + hTherm
				+ ", hRaingauge=" + hRaingauge + ", hAnem=" + hAnem + ", altitude=" + altitude + ", stnCName="
				+ stnCName + ", stnEName=" + stnEName + "]\n";
	}
}
