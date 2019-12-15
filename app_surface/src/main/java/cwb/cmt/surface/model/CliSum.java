package cwb.cmt.surface.model;

import java.time.LocalDateTime;

import javax.annotation.Resource;

public class CliSum {

	private String stno;
	private String stnCName;
	private LocalDateTime obsTime;
	private String stnEndTime;
	private String manObsNum;
	//table1
	//Pressure
	private String stnPres;			//氣壓
	//Air Temperature
	private String txAvg; 			//每月5H_14H_21H平均氣溫
	private String tx;				//平均氣溫
	private String txMaxAvg;			//平均最高氣溫
	private String txMinAvg;			//平均最低氣溫
	private String txMaxAbs;		    //絕對最高氣溫
	private LocalDateTime txMaxAbsTime;	 //絕對最高氣溫月日
	private String txMinAbs;				//絕對最低氣溫
	private LocalDateTime txMinAbsTime;	//絕對最低氣溫月日
	//Relative Humidity
	private String rhAvg;		//每月5H_14H_21H平均相對溼度
	private String rh;				//平均相對濕度
	private String rhMin;			//最小平均相對濕度
	//Sunshine
	private String sunshine;			//日照時數總計
	private String sunshineRate;		//日照時數百分比
	
	private String tx_5H;
	private String tx_14H;
	private String tx_21H;
	private String rh_5H;
	private String rh_14H;
	private String rh_21H;
	
	//table2
	//Wind
	private String ws;//平均風速
	private String wd;//最多風向
	private String wsMax;//最大風速
	private String wdMax;//最大方向
	private LocalDateTime wMaxTime;//最大方向月日
	//Gust
	private String wsGust;//最大陣風風速
	private LocalDateTime wGustTime;//最大陣風風速月日
	//Cloud Amount
	private String cAmtTotalAvg; //每月5H_14H_21H雲量
	private String cAmtMean;//平均雲量
	//Visibility
	private String visbMean;//能見度 (km)
	//Precipitation
	private String precp;//降水量總計
	private String precp1DayMax;//最大一日間降水量
	private LocalDateTime precp1DayMaxTime;//最大一日間降水量月日
	private String precpHourSum; //降水時數
	private String precpDays_01mm;//降水日數總計>=0.1mm
	private String precpDays_1mm;//降水日數總計>=1mm
	private String precpDays_10mm;//降水日數總計>=10mm
	
	private String cAmtTotal5H;
	private String cAmtTotal14H;
	private String cAmtTotal21H;
	//table3
	//Evaporation
	private String evapA;//蒸發量
	//Weather Condition (days)
	private String txMinAbs_LE0;//最低氣溫<0度
	private String txMinAbs_LE10;//最低氣溫<10度
	private String txMinAbs_GE20;//最低氣溫>20度
	private String txMaxAbs_GE25;//最低氣溫>25度
	private String txMaxAbs_GE30;//最低氣溫>30度
	private String txMaxAbs_GE35;//最低氣溫>35度
	private String precpDay;//雨日
	private String snowDay;//雪日(有null時填空白)
	private String fogDay; //霧日(無data)
	private String statO2;//霾日
	private String thunderstormDay;//雷暴日
	private String statF3;//雹日
	private String wsMaxGE10Day; //強風日
	private String statP2;//露日
	private String frostDay;//霜日
	//Sky Condition 
	private String clearSkyDay;//碧
	private String scatteredSkyDay;//疏
	private String brokenSkyDay;//裂
	private String overcastSkyDay;//密
	//Snow Date
	private String statF1_MinTime;//初雪日
	private String statF1_MaxTime;//終雪日
	//Frost Date
	private String statF2_MinTime;//初霜日
	private String statF2_MaxTime;//終霜日
	

	
    //Time: year
    @Resource(name="year")
    protected int year;
    
	//Time: month
    @Resource(name="month")
    protected int month;

    
	// override equals(), so that stations have same stno are the same station
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CliSum) {
			CliSum other = (CliSum)obj;
			if (other.getStno().equals(this.getStno())) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
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
	public void setStnCName(String stnCName) {
		this.stnCName = stnCName;
	}
	public LocalDateTime getObsTime() {
		return obsTime;
	}
	public void setObsTime(LocalDateTime obsTime) {
		this.obsTime = obsTime;
	}
	public String getStnPres() {
		return stnPres;
	}
	public void setStnPres(String stnPres) {
		this.stnPres = stnPres;
	}
	public String getTx() {
		return tx;
	}
	public void setTx(String tx) {
		this.tx = tx;
	}
	public String getTxMaxAbs() {
		return txMaxAbs;
	}
	public void setTxMaxAbs(String txMaxAbs) {
		this.txMaxAbs = txMaxAbs;
	}
	public LocalDateTime getTxMaxAbsTime() {
		return txMaxAbsTime;
	}
	public void setTxMaxAbsTime(LocalDateTime txMaxAbsTime) {
		this.txMaxAbsTime = txMaxAbsTime;
	}
	public String getTxMinAbs() {
		return txMinAbs;
	}
	public void setTxMinAbs(String txMinAbs) {
		this.txMinAbs = txMinAbs;
	}
	public LocalDateTime getTxMinAbsTime() {
		return txMinAbsTime;
	}
	public void setTxMinAbsTime(LocalDateTime txMinAbsTime) {
		this.txMinAbsTime = txMinAbsTime;
	}
	public String getRh() {
		return rh;
	}
	public void setRh(String rh) {
		this.rh = rh;
	}
	public String getRhMin() {
		return rhMin;
	}
	public void setRhMin(String rhMin) {
		this.rhMin = rhMin;
	}
	public String getSunshine() {
		return sunshine;
	}
	public void setSunshine(String sunshine) {
		this.sunshine = sunshine;
	}
	public String getSunshineRate() {
		return sunshineRate;
	}
	public void setSunshineRate(String sunshineRate) {
		this.sunshineRate = sunshineRate;
	}
	public String getTxMaxAvg() {
		return txMaxAvg;
	}
	public void setTxMaxAvg(String txMaxAvg) {
		this.txMaxAvg = txMaxAvg;
	}
	public String getTxMinAvg() {
		return txMinAvg;
	}
	public void setTxMinAvg(String txMinAvg) {
		this.txMinAvg = txMinAvg;
	}
	public String getTxAvg() {
		return txAvg;
	}
	public void setTxAvg(String txAvg) {
		this.txAvg = txAvg;
	}
	public String getRhAvg() {
		return rhAvg;
	}
	public void setRhAvg(String rhAvg) {
		this.rhAvg = rhAvg;
	}
	
	public String getWs() {
		return ws;
	}
	public void setWs(String ws) {
		this.ws = ws;
	}
	public String getWd() {
		return wd;
	}
	public void setWd(String wd) {
		this.wd = wd;
	}
	public String getWsMax() {
		return wsMax;
	}
	public void setWsMax(String wsMax) {
		this.wsMax = wsMax;
	}
	public String getWdMax() {
		return wdMax;
	}
	public void setWdMax(String wdMax) {
		this.wdMax = wdMax;
	}
	public LocalDateTime getwMaxTime() {
		return wMaxTime;
	}
	public void setwMaxTime(LocalDateTime wMaxTime) {
		this.wMaxTime = wMaxTime;
	}
	public String getWsGust() {
		return wsGust;
	}
	public void setWsGust(String wsGust) {
		this.wsGust = wsGust;
	}
	public LocalDateTime getwGustTime() {
		return wGustTime;
	}

	public void setwGustTime(LocalDateTime wGustTime) {
		this.wGustTime = wGustTime;
	}
	public String getcAmtMean() {
		return cAmtMean;
	}
	public void setcAmtMean(String cAmtMean) {
		this.cAmtMean = cAmtMean;
	}
	public String getVisbMean() {
		return visbMean;
	}
	public void setVisbMean(String visbMean) {
		this.visbMean = visbMean;
	}
	public String getPrecp() {
		return precp;
	}
	public void setPrecp(String precp) {
		this.precp = precp;
	}
	
	public String getcAmtTotalAvg() {
		return cAmtTotalAvg;
	}

	public void setcAmtTotalAvg(String cAmtTotalAvg) {
		this.cAmtTotalAvg = cAmtTotalAvg;
	}

	public String getPrecp1DayMax() {
		return precp1DayMax;
	}

	public void setPrecp1DayMax(String precp1DayMax) {
		this.precp1DayMax = precp1DayMax;
	}

	public LocalDateTime getPrecp1DayMaxTime() {
		return precp1DayMaxTime;
	}

	public void setPrecp1DayMaxTime(LocalDateTime precp1DayMaxTime) {
		this.precp1DayMaxTime = precp1DayMaxTime;
	}
	


	public String getPrecpHourSum() {
		return precpHourSum;
	}

	public void setPrecpHourSum(String precpHourSum) {
		this.precpHourSum = precpHourSum;
	}

	public String getEvapA() {
		return evapA;
	}

	public void setEvapA(String evapA) {
		this.evapA = evapA;
	}


	public String getStnEndTime() {
		return stnEndTime;
	}

	public void setStnEndTime(String stnEndTime) {
		this.stnEndTime = stnEndTime;
	}
	
	public String getPrecpDays_01mm() {
		return precpDays_01mm;
	}

	public void setPrecpDays_01mm(String precpDays_01mm) {
		this.precpDays_01mm = precpDays_01mm;
	}

	public String getPrecpDays_1mm() {
		return precpDays_1mm;
	}

	public void setPrecpDays_1mm(String precpDays_1mm) {
		this.precpDays_1mm = precpDays_1mm;
	}

	public String getPrecpDays_10mm() {
		return precpDays_10mm;
	}

	public void setPrecpDays_10mm(String precpDays_10mm) {
		this.precpDays_10mm = precpDays_10mm;
	}
	
	public String getTxMinAbs_LE0() {
		return txMinAbs_LE0;
	}

	public void setTxMinAbs_LE0(String txMinAbs_LE0) {
		this.txMinAbs_LE0 = txMinAbs_LE0;
	}

	public String getTxMinAbs_LE10() {
		return txMinAbs_LE10;
	}

	public void setTxMinAbs_LE10(String txMinAbs_LE10) {
		this.txMinAbs_LE10 = txMinAbs_LE10;
	}

	public String getTxMinAbs_GE20() {
		return txMinAbs_GE20;
	}

	public void setTxMinAbs_GE20(String txMinAbs_GE20) {
		this.txMinAbs_GE20 = txMinAbs_GE20;
	}

	public String getTxMaxAbs_GE25() {
		return txMaxAbs_GE25;
	}

	public void setTxMaxAbs_GE25(String txMinAbs_GE25) {
		this.txMaxAbs_GE25 = txMinAbs_GE25;
	}

	public String getTxMaxAbs_GE30() {
		return txMaxAbs_GE30;
	}

	public void setTxMaxAbs_GE30(String txMinAbs_GE30) {
		this.txMaxAbs_GE30 = txMinAbs_GE30;
	}

	public String getTxMaxAbs_GE35() {
		return txMaxAbs_GE35;
	}

	public void setTxMaxAbs_GE35(String txMinAbs_GE35) {
		this.txMaxAbs_GE35 = txMinAbs_GE35;
	}

	public String getPrecpDay() {
		return precpDay;
	}

	public void setPrecpDay(String precpDay) {
		this.precpDay = precpDay;
	}

	public String getSnowDay() {
		return snowDay;
	}

	public void setSnowDay(String snowDay) {
		this.snowDay = snowDay;
	}

	public String getFogDay() {
		return fogDay;
	}

	public void setFogDay(String fogDay) {
		this.fogDay = fogDay;
	}

	public String getStatO2() {
		return statO2;
	}

	public void setStatO2(String statO2) {
		this.statO2 = statO2;
	}

	public String getThunderstormDay() {
		return thunderstormDay;
	}

	public void setThunderstormDay(String thunderstormDay) {
		this.thunderstormDay = thunderstormDay;
	}

	public String getStatF3() {
		return statF3;
	}

	public void setStatF3(String statF3) {
		this.statF3 = statF3;
	}

	public String getWsMaxGE10Day() {
		return wsMaxGE10Day;
	}

	public void setWsMaxGE10Day(String wsMaxGE10Day) {
		this.wsMaxGE10Day = wsMaxGE10Day;
	}

	public String getStatP2() {
		return statP2;
	}

	public void setStatP2(String statP2) {
		this.statP2 = statP2;
	}

	public String getFrostDay() {
		return frostDay;
	}

	public void setFrostDay(String frostDay) {
		this.frostDay = frostDay;
	}

	public String getClearSkyDay() {
		return clearSkyDay;
	}

	public void setClearSkyDay(String clearSkyDay) {
		this.clearSkyDay = clearSkyDay;
	}

	public String getScatteredSkyDay() {
		return scatteredSkyDay;
	}

	public void setScatteredSkyDay(String scatteredSkyDay) {
		this.scatteredSkyDay = scatteredSkyDay;
	}

	public String getBrokenSkyDay() {
		return brokenSkyDay;
	}

	public void setBrokenSkyDay(String brokenSkyDay) {
		this.brokenSkyDay = brokenSkyDay;
	}

	public String getOvercastSkyDay() {
		return overcastSkyDay;
	}

	public void setOvercastSkyDay(String overcastSkyDay) {
		this.overcastSkyDay = overcastSkyDay;
	}

	public String getStatF1_MinTime() {
		return statF1_MinTime;
	}

	public void setStatF1_MinTime(String statF1_MinTime) {
		this.statF1_MinTime = statF1_MinTime;
	}

	public String getStatF1_MaxTime() {
		return statF1_MaxTime;
	}

	public void setStatF1_MaxTime(String statF1_MaxTime) {
		this.statF1_MaxTime = statF1_MaxTime;
	}

	public String getStatF2_MinTime() {
		return statF2_MinTime;
	}

	public void setStatF2_MinTime(String statF2_MinTime) {
		this.statF2_MinTime = statF2_MinTime;
	}

	public String getStatF2_MaxTime() {
		return statF2_MaxTime;
	}

	public void setStatF2_MaxTime(String statF2_MaxTime) {
		this.statF2_MaxTime = statF2_MaxTime;
	}
	
	public String getManObsNum() {
		return manObsNum;
	}

	public void setManObsNum(String manObsNum) {
		this.manObsNum = manObsNum;
	}
	
	@Override
	public String toString() {
		return "CliSum [stno=" + stno +" stnCName=" + stnCName +" obsTime="+ obsTime + 
				" StnEndTime="+ stnEndTime + " ManObsNum=" + manObsNum+
				" precpDay=" + precpDay + " statO2=" + statO2 + " statF3="+ statF3 +
				" txMinAbs_LE0=" + txMinAbs_LE0 +
				" txMinAbs_LE10=" + txMinAbs_LE10 + " txMinAbs_GE20=" + txMinAbs_GE20 +
				" txMaxAbs_GE25=" + txMaxAbs_GE25 + " txMaxAbs_GE30=" + txMaxAbs_GE30 +
				" txMaxAbs_GE35=" + txMaxAbs_GE35 +
				" EvapA=" + evapA + " snowDay=" + snowDay+
				" fogDay=" + fogDay + " thunderstormDay=" + thunderstormDay+ " wsMaxGE10Day=" + wsMaxGE10Day+
				" frostDay= " + frostDay + " clearSkyDay=" + clearSkyDay + " scatteredSkyDay=" +scatteredSkyDay+
				" brokenSkyDay=" + brokenSkyDay + " overcastSkyDay=" + overcastSkyDay+
				
				" cAmtTotalAvg="+cAmtTotalAvg+" precpHourSum="+precpHourSum +
				" precpDays_01mm=" + precpDays_01mm + " precpDays_1mm=" + precpDays_1mm + 
				" precpDays_10mm=" + precpDays_10mm + " txAvg=" + txAvg + " rhAvg=" + rhAvg +
				" txMaxAvg=" + txMaxAvg + " txMinAvg=" + txMinAvg +	" stnPres="+ stnPres + 
				" txMaxAbs=" + txMaxAbs + " txMaxAbsTime=" + txMaxAbsTime + " txMinAbs=" + txMinAbs +
				" txMinAbsTime=" + txMinAbsTime + " rh=" + rh + " rhMin=" + rhMin +	" sunshine=" + sunshine +
				" sunshine=" + sunshine + " sunshineRate=" + sunshineRate + "]\n";
	}

	public String getTx_5H() {
		return tx_5H;
	}

	public void setTx_5H(String tx_5h) {
		tx_5H = tx_5h;
	}

	public String getTx_14H() {
		return tx_14H;
	}

	public void setTx_14H(String tx_14h) {
		tx_14H = tx_14h;
	}

	public String getTx_21H() {
		return tx_21H;
	}

	public void setTx_21H(String tx_21h) {
		tx_21H = tx_21h;
	}

	public String getRh_5H() {
		return rh_5H;
	}

	public void setRh_5H(String rh_5h) {
		rh_5H = rh_5h;
	}

	public String getRh_14H() {
		return rh_14H;
	}

	public void setRh_14H(String rh_14h) {
		rh_14H = rh_14h;
	}

	public String getRh_21H() {
		return rh_21H;
	}

	public void setRh_21H(String rh_21h) {
		rh_21H = rh_21h;
	}

	public String getcAmtTotal5H() {
		return cAmtTotal5H;
	}

	public void setcAmtTotal5H(String cAmtTotal5H) {
		this.cAmtTotal5H = cAmtTotal5H;
	}

	public String getcAmtTotal14H() {
		return cAmtTotal14H;
	}

	public void setcAmtTotal14H(String cAmtTotal14H) {
		this.cAmtTotal14H = cAmtTotal14H;
	}

	public String getcAmtTotal21H() {
		return cAmtTotal21H;
	}

	public void setcAmtTotal21H(String cAmtTotal21H) {
		this.cAmtTotal21H = cAmtTotal21H;
	}
}
