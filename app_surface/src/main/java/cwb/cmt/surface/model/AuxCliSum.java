package cwb.cmt.surface.model;

import java.time.LocalDateTime;

import javax.annotation.Resource;

public class AuxCliSum {

	private String stno;
	private String stnCName;
	private LocalDateTime obsTime;
	private String stnEndTime;
	private String globalRad;
	private String globlRad_flag;
	private String sunshine;
	private String globalRadMax;
	private LocalDateTime globalRadHrMaxTime;

	
	
    //Time: year
    @Resource(name="year")
    protected int year;
    
	//Time: month
    @Resource(name="month")
    protected int month;
	
	
	// override equals(), so that stations have same stno are the same station
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AuxCliSum) {
			AuxCliSum other = (AuxCliSum)obj;
			if (other.getStno().equals(this.getStno())) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
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

	public void setStnCName(String stnCName) {
		this.stnCName = stnCName;
	}
	
	public String getGlobalRad() {
		return globalRad;
	}

	public void setGlobalRad(String globalRad) {
		this.globalRad = globalRad;
	}

	public String getSunshine() {
		return sunshine;
	}

	public void setSunshine(String sunshine) {
		this.sunshine = sunshine;
	}

	public LocalDateTime getObsTime() {
		return obsTime;
	}

	public void setObsTime(LocalDateTime obsTime) {
		this.obsTime = obsTime;
	}

    public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}


	public String getGlobalRadMax() {
		return globalRadMax;
	}

	public void setGlobalRadMax(String globalRadMax) {
		this.globalRadMax = globalRadMax;
	}

	public LocalDateTime getGlobalRadHrMaxTime() {
		return globalRadHrMaxTime;
	}

	public void setGlobalRadHrMaxTime(LocalDateTime globalRadHrMaxTime) {
		this.globalRadHrMaxTime = globalRadHrMaxTime;
	}

	public String getStnEndTime() {
		return stnEndTime;
	}

	public void setStnEndTime(String stnEndTime) {
		this.stnEndTime = stnEndTime;
	}

	public String getGloblRad_flag() {
		return globlRad_flag;
	}

	public void setGloblRad_flag(String globlRad_flag) {
		this.globlRad_flag = globlRad_flag;
	}

	@Override
	public String toString() {
		return "AuxCliSum [stno=" + stno +" stnCName=" + stnCName +
				" obsTime="+ obsTime +" StnEndTime=" + stnEndTime + 
				" globalRad="+ globalRad + " sunshine=" + sunshine +
				" globlRad_flag=" + globlRad_flag +" RadMax=" + globalRadMax +
				" RadMaxTime="+ globalRadHrMaxTime + "]\n";
	}
}
