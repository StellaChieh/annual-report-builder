package cwb.cmt.summary.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.dao.DbInteract;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.model.StnCliEleTable;
import cwb.cmt.summary.model.StnCliEleTable.StnCliEleData;
import cwb.cmt.summary.model.SummaryModel;
import cwb.cmt.summary.utils.RomanNumber;

@Service
public class MakeStnCliEleTable {

	private int stnNo;
	private Station station;
	private int tableNo;
	private ClimaticElement cliEle;

	@Autowired
	@Qualifier(value="dbInteraction")
	private DbInteract dbInteract;

	public void setParams(int stnNo, Station station, int tableNo, ClimaticElement cliEle) {
		this.stnNo = stnNo;
		this.station = station;
		this.tableNo = tableNo;
		this.cliEle = cliEle;
	}

	public StnCliEleTable makeTableData() {
		StnCliEleTable table = new StnCliEleTable();
		table.setStnNo(RomanNumber.toRoman(stnNo)); // turn stnoNo into Roman number
		table.setStnCName(station.getStnCName());
		table.setStnEName(station.getStnEName());
		table.setTableNo(String.valueOf(tableNo));
		table.setTitleCName(cliEle.getChineseName());
		table.setTitleEName(cliEle.getEnglishName() + ((cliEle.getUnit().equalsIgnoreCase("none")) ? // unit
				"" : " (" + cliEle.getUnit() + ")"));
		table.setLastColName(cliEle.getSummaryName());
		table.setDatas(putData());
		return table;
	}

	private List<StnCliEleData> putData() {
		// put data
		List<StnCliEleData> tableDatas = new ArrayList<>();
		for (SummaryModel tmp : query()) {
			StnCliEleData d = new StnCliEleData();
			d.setDataJan(format(tmp.getJan(), false, tmp.getNo()));
			d.setDataFeb(format(tmp.getFeb(), false, tmp.getNo()));
			d.setDataMar(format(tmp.getMar(), false, tmp.getNo()));
			d.setDataApr(format(tmp.getApr(), false, tmp.getNo()));
			d.setDataMay(format(tmp.getMay(), false, tmp.getNo()));
			d.setDataJun(format(tmp.getJun(), false, tmp.getNo()));
			d.setDataJul(format(tmp.getJul(), false, tmp.getNo()));
			d.setDataAug(format(tmp.getAug(), false, tmp.getNo()));
			d.setDataSep(format(tmp.getSep(), false, tmp.getNo()));
			d.setDataOct(format(tmp.getOct(), false, tmp.getNo()));
			d.setDataNov(format(tmp.getNov(), false, tmp.getNo()));
			d.setDataDec(format(tmp.getDec(), false, tmp.getNo()));
			d.setDataSummary(format(tmp.getSummary(), true, tmp.getNo()));
			if (tmp.getNo() > 10) {
				String yearRange = tmp.getBeginYear() + " - " + tmp.getEndYear();
				d.setYearRange(yearRange);
			} else {
				d.setAdYear(String.valueOf(tmp.getBeginYear()));
				d.setChinaYear(String.valueOf(tmp.getBeginYear() - 1911));
			}
			tableDatas.add(d);
		}
		return tableDatas;
	};

	private String format(String value, boolean isSummary, int no) {
		if(cliEle.getNumOfLinesPerRow() == 1) {
			return value;
		} else if (cliEle.getNumOfLinesPerRow() == 2) {
			if(!value.contains("|")) {
				return "#";
			} else {
				String[] ary = value.split("\\|");
				String data = ary[0];
				String time = ary[1];
				return new StringBuilder()
								.append(data)
								.append("\n")
								.append(getDate(time, isSummary, no))
								.toString();
			}
		} else { // cliEle.getNumOfLinesPerRow() == 3
			if(!value.contains("|")) {
				return "#";
			} else {
				String[] ary = value.split("\\|"); 
				String data = ary[0];
				String time = ary[1];
				String data2 = ary[2];
				return new StringBuilder()
								.append(data)
								.append("\n")
								.append(getDate(time, isSummary, no))
								.append("\n")
								.append(data2)
								.toString();
			}
		}
	}
	
	private String getDate(String time, boolean isSummary, int no) {
		if(no > 10) {
			return getSpanYearDate(time);
		} else if (! isSummary) {
			return getSingleYearDate(time);
		} else {
			return getSingleYearSummaryDate(time);
		}
	}
	

	private List<SummaryModel> query() {
		return dbInteract.queryForStnCliEle(station.getStno(), cliEle.getId()).stream()
				.sorted(Comparator.comparing(SummaryModel::getNo)).collect(Collectors.toList());
	}

	private String getSingleYearDate(String time) {
		// time = 2001-01-25
		if (time.equals("#")) {
			return "#";
		} else {
			String[] timeChar = time.split("-");
			return timeChar[2];
		}
	}

	private String getSingleYearSummaryDate(String time) {
		// time = 2001-01-25
		if (time.equals("#")) {
			return "#";
		} else {
			String[] timeChar = time.split("-");
			return timeChar[1] + "/" + timeChar[2];
		}
	}

	private String getSpanYearDate(String time) {
		// time = 2001-01-25
		if (time.equals("#")) {
			return "#";
		} else {
			String[] timeChar = time.split("-");
			return timeChar[0] + ", " + timeChar[2];
		}
	}

}
