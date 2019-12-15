package cwb.cmt.summary.model;

import java.util.ArrayList;
import java.util.List;

public class StnCliEleTable {
	
	private String stno;               // station ID
	private String stnNo;              // 第幾個station
	private String stnCName;           // station Chinese name
	private String stnEName;           // station English name
	private String tableNo;            // ex: 1 (第一個天氣要素之意=第1個table)
	private String titleCName;         // 表格標題中文名 ex: 海平面平均氣壓
	private String titleEName;         // 表格標題英文名 ex: Mean Pressure at Sea Level (hPa)
	private String lastColName;        // ex: MEAN
	private List<StnCliEleData> datas = new ArrayList<>(); // 此表格包含的數字
	
	public static class StnCliEleData {
		
		private boolean isPerYearData;  // show if the data is per year data or span years data
		private String dataType;        
		
		private String chinaYear;       // ex: 2001
		private String adYear;          // ex: 90
		private String yearRange;       // ex: 2001 - 2010
		
		private String dataJan;         
		private String dataFeb;
		private String dataMar;
		private String dataApr;
		private String dataMay;
		private String dataJun;
		private String dataJul;
		private String dataAug;
		private String dataSep;
		private String dataOct;
		private String dataNov;
		private String dataDec;
		
		private String dataSummary;     

		public boolean isPerYearData() {
			return isPerYearData;
		}

		public void setPerYearData(boolean isPerYearData) {
			this.isPerYearData = isPerYearData;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getChinaYear() {
			return chinaYear;
		}

		public void setChinaYear(String chinaYear) {
			this.chinaYear = chinaYear;
		}

		public String getAdYear() {
			return adYear;
		}

		public void setAdYear(String adYear) {
			this.adYear = adYear;
		}

		public String getYearRange() {
			return yearRange;
		}

		public void setYearRange(String yearRange) {
			this.yearRange = yearRange;
		}

		public String getDataJan() {
			return dataJan;
		}

		public void setDataJan(String dataJan) {
			this.dataJan = dataJan;
		}

		public String getDataFeb() {
			return dataFeb;
		}

		public void setDataFeb(String dataFeb) {
			this.dataFeb = dataFeb;
		}

		public String getDataMar() {
			return dataMar;
		}

		public void setDataMar(String dataMar) {
			this.dataMar = dataMar;
		}

		public String getDataApr() {
			return dataApr;
		}

		public void setDataApr(String dataApr) {
			this.dataApr = dataApr;
		}

		public String getDataMay() {
			return dataMay;
		}

		public void setDataMay(String dataMay) {
			this.dataMay = dataMay;
		}

		public String getDataJun() {
			return dataJun;
		}

		public void setDataJun(String dataJun) {
			this.dataJun = dataJun;
		}

		public String getDataJul() {
			return dataJul;
		}

		public void setDataJul(String dataJul) {
			this.dataJul = dataJul;
		}

		public String getDataAug() {
			return dataAug;
		}

		public void setDataAug(String dataAug) {
			this.dataAug = dataAug;
		}

		public String getDataSep() {
			return dataSep;
		}

		public void setDataSep(String dataSep) {
			this.dataSep = dataSep;
		}

		public String getDataOct() {
			return dataOct;
		}

		public void setDataOct(String dataOct) {
			this.dataOct = dataOct;
		}

		public String getDataNov() {
			return dataNov;
		}

		public void setDataNov(String dataNov) {
			this.dataNov = dataNov;
		}

		public String getDataDec() {
			return dataDec;
		}

		public void setDataDec(String dataDec) {
			this.dataDec = dataDec;
		}

		public String getDataSummary() {
			return dataSummary;
		}

		public void setDataSummary(String dataSummary) {
			this.dataSummary = dataSummary;
		}

		@Override
		public String toString() {
			return "StnCliEleData [isPerYearData=" + isPerYearData + ", dataType=" + dataType + ", chinaYear=" + chinaYear
					+ ", adYear=" + adYear + ", yearRange=" + yearRange + ", dataJan=" + dataJan + ", dataFeb=" + dataFeb
					+ ", dataMar=" + dataMar + ", dataApr=" + dataApr + ", dataMay=" + dataMay + ", dataJun=" + dataJun
					+ ", dataJul=" + dataJul + ", dataAug=" + dataAug + ", dataSep=" + dataSep + ", dataOct=" + dataOct
					+ ", dataNov=" + dataNov + ", dataDec=" + dataDec + ", dataSummary=" + dataSummary + "]\n";
		}
		
	}
	
	
	public String getTableNo() {
		return tableNo;
	}
	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}
	public String getTitleCName() {
		return titleCName;
	}
	public void setTitleCName(String titleChineseName) {
		this.titleCName = titleChineseName;
	}
	public String getTitleEName() {
		return titleEName;
	}
	public void setTitleEName(String titleEnglishName) {
		this.titleEName = titleEnglishName;
	}
	public String getLastColName() {
		return lastColName;
	}
	public void setLastColName(String lastColName) {
		this.lastColName = lastColName;
	}
	public String getStno() {
		return stno;
	}
	public void setStno(String stno) {
		this.stno = stno;
	}
	public String getStnNo() {
		return stnNo;
	}
	public void setStnNo(String stnNo) {
		this.stnNo = stnNo;
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
	public List<StnCliEleData> getDatas() {
		return datas;
	}
	public void setDatas(List<StnCliEleData> datas) {
		this.datas = datas;
	}
	@Override
	public String toString() {
		return "StnCliEleTable [stno=" + stno + ", stnNo=" + stnNo + ", stnCName=" + stnCName + ", stnEName=" + stnEName
				+ ", tableNo=" + tableNo + ", titleCName=" + titleCName + ", titleEName=" + titleEName
				+ ", lastColName=" + lastColName + ", datas=" + datas + "]\n";
	}
	
	
}