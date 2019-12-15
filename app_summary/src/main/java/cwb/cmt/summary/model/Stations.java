package cwb.cmt.summary.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "station"
})
@XmlRootElement(name = "stations")
public class Stations {

    protected List<Stations.Station> station;

    public List<Stations.Station> getStation() {
        if (station == null) {
            station = new ArrayList<Stations.Station>();
        }
        return this.station;
    }
    
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Station {

        @XmlElement(required = true)
        private String stno;  		 // 測站編號，唯一id
        @XmlElement(required = true)
        private String stnCName;     // 測站中文名，由xml設定
        @XmlElement(required = true)
    	private Boolean isAuto;      // 是否為無人測站，由xml設定
        @XmlElement(required = true)
        private Integer autoTime;    // 開始成為無人測站的年份，由xml設定
        
        private String stnEName;     // 測站英文名，由db設定
        private String longitude;    // 東經，由db設定
    	private String latitude;     // 北緯，由db設定
    	private String hBarometer;   // 氣壓計海面上高度公尺，由db設定
    	private String hTherm;       // 溫度計地面高度公尺，由db設定
    	private String hRaingauge;   // 雨量器口面地上高度公尺，由db設定
    	private String hAnem;        // 風速儀地上高度公尺，由db設定
    	private String altitude;     // 海拔公尺，由db設定
    	private String stnBeginTime; // 創立年份，由db設定
    	private String manObsNum;    // 每日觀測次數，由db設定
    	 
    	// get only the numeric part of latitude
    	public int getNumericLatitude(){
    		latitude.replaceAll("'", ""); // replace '
    		latitude.replaceAll("°", ""); // replace °
    		latitude.replaceAll("〞", ""); // replace 〞
    		return Integer.parseInt(latitude);
    	}
    
		/*
         * getter and setter
         */
        public String getStnEName() {
			return stnEName;
		}

		public void setStnEName(String stnEName) {
			this.stnEName = stnEName;
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

		public String getStnBeginTime() {
			return stnBeginTime;
		}

		public void setStnBeginTime(String stnBeginTime) {
			this.stnBeginTime = stnBeginTime;
		}

		public String getManObsNum() {
			return manObsNum;
		}

		public void setManObsNum(String manObsNum) {
			this.manObsNum = manObsNum;
		}

		public Boolean isAuto() {
			return isAuto;
		}

		public void setAuto(Boolean isAuto) {
			this.isAuto = isAuto;
		}
        public String getStno() {
            return stno;
        }

        public void setStno(String value) {
            this.stno = value;
        }
 
        public String getStnCName() {
            return stnCName;
        }
  
        public void setStnCName(String value) {
            this.stnCName = value;
        }
        public Integer getAutoTime() {
            return autoTime;
        }
  
        public void setAutoTime(Integer value) {
            this.autoTime = value;
        }
        
        @Override
    	public String toString() {
    		return "Station [stno=" + stno + ", stnCName=" + stnCName + ", stnEName=" + stnEName + ", longitude="
    				+ longitude + ", latitude=" + latitude + ", hBarometer=" + hBarometer + ", hTherm=" + hTherm
    				+ ", hRaingauge=" + hRaingauge + ", hAnem=" + hAnem + ", altitude=" + altitude + ", stnBeginTime="
    				+ stnBeginTime + ", manObsNum=" + manObsNum + ", isAuto=" + isAuto + ", autoTime=" + autoTime
    				+ "]\n";
    	}

    }

}
