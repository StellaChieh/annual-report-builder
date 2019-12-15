package cwb.cmt.summary.model;

import java.util.ArrayList;
import java.util.List;

public class CliEleTable {

	private String tableNo;
	private String titleChineseName;
	private String titleEnglishName;
	private String lastColName;
	private String yearRange;
	private List<CliEleData> datas = new ArrayList<>();

	public static class CliEleData {

		private int stationNo;

		private String stationChineseName;
		private String stationEnglishName;

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

		private String dataSummary; // might be average, max, total... etc

		public int getStationNo() {
			return stationNo;
		}

		public void setStationNo(int stationNo) {
			this.stationNo = stationNo;
		}

		public String getStationChineseName() {
			return stationChineseName;
		}

		public void setStationChineseName(String stationChineseName) {
			this.stationChineseName = stationChineseName;
		}

		public String getStationEnglishName() {
			return stationEnglishName;
		}

		public void setStationEnglishName(String stationEnglishName) {
			this.stationEnglishName = stationEnglishName;
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

	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getTitleChineseName() {
		return titleChineseName;
	}

	public void setTitleChineseName(String titleChineseName) {
		this.titleChineseName = titleChineseName;
	}

	public String getTitleEnglishName() {
		return titleEnglishName;
	}

	public void setTitleEnglishName(String titleEnglishName) {
		this.titleEnglishName = titleEnglishName;
	}

	public String getLastColName() {
		return lastColName;
	}

	public void setLastColName(String lastColName) {
		this.lastColName = lastColName;
	}

	public String getYearRange() {
		return yearRange;
	}

	public void setYearRange(String yearRange) {
		this.yearRange = yearRange;
	}

	public List<CliEleData> getDatas() {
		return datas;
	}

	public void setDatas(List<CliEleData> datas) {
		this.datas = datas;
	}
}
