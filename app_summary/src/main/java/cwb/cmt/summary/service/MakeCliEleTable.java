package cwb.cmt.summary.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.dao.DbInteract;
import cwb.cmt.summary.model.CliEleTable;
import cwb.cmt.summary.model.CliEleTable.CliEleData;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;
import cwb.cmt.summary.model.Stations.Station;
import cwb.cmt.summary.model.SummaryModel;

@Service
public class MakeCliEleTable {

	@Autowired
	@Qualifier(value="dbInteraction")
	private DbInteract dbInteract;
	
	private int tableNo;
	private ClimaticElement cliEle;
	private List<Station> stations;
	private int year;
	
	public void setParams(int tableNo, ClimaticElement cliEle, List<Station> stations, int year){
		this.tableNo = tableNo;
		this.cliEle = cliEle;
		this.stations = stations;
		this.year = year;
	}
	
	public CliEleTable getTableData(){
		// put table info
		CliEleTable table = new CliEleTable();
		table.setTableNo(String.valueOf(tableNo));
		table.setTitleChineseName(cliEle.getChineseName());
		table.setTitleEnglishName(cliEle.getEnglishName() + (cliEle.getUnit().equals("none") ? 
										"" : " (" + cliEle.getUnit() +")" ));
		table.setYearRange(String.valueOf(year) + " - " + String.valueOf(year+9));
		table.setLastColName(cliEle.getSummaryName());

		// putData was implemented in the child class
		table.getDatas().addAll(putData());
		return table;
	}
	
	private SummaryModel query(Station stn) {
		return dbInteract.queryForOneStnCliEle(11, stn.getStno(), cliEle.getId());
	}
	
	private List<CliEleData> putData() {
		CliEleData data = null;
		SummaryModel row = null;
		List<CliEleData> list = new ArrayList<>();
		for(Station stn : stations) {
			// dao
			row = query(stn);  
			// put data
			data = new CliEleData();
			data.setStationChineseName(getStationById(row.getStno()).getStnCName());
			data.setStationEnglishName(getStationById(row.getStno()).getStnEName());
			data.setDataJan(format(row.getJan(), false));
			data.setDataFeb(format(row.getFeb(), false));
			data.setDataMar(format(row.getMar(), false));
			data.setDataApr(format(row.getApr(), false));
			data.setDataMay(format(row.getMay(), false));
			data.setDataJun(format(row.getJun(), false));
			data.setDataJul(format(row.getJul(), false));
			data.setDataAug(format(row.getAug(), false));
			data.setDataSep(format(row.getSep(), false));
			data.setDataOct(format(row.getOct(), false));
			data.setDataNov(format(row.getNov(), false));
			data.setDataDec(format(row.getDec(), false));
			data.setDataSummary(format(row.getSummary(), true));
			list.add(data);
		}
		return list;
	}
	
	private String format(String value, boolean isSummary) {
		if(cliEle.getNumOfLinesPerRow() == 1) {
			return new StringBuilder().append("\n")
										.append(value)
										.append("\n").toString();
		} else if (cliEle.getNumOfLinesPerRow() == 2) {
			if(!value.contains("|")) {
				return "\n#\n";
			} else {
				String[] ary = value.split("\\|");
				String data = ary[0];
				String time = ary[1];
				StringBuilder sb = new StringBuilder()
										.append(data)
										.append("\n");
				if(!isSummary) {
					sb.append(getDate(time));
				} else {
					sb.append(getSummaryDate(time));
				}
				return sb.append("\n").toString();
			}
		} else {
			if(!value.contains("|")) {
				return "\n#\n";
			} else {
				String[] ary = value.split("\\|"); 
				String data = ary[0];
				String time = ary[1];
				String data2 = ary[2];
				StringBuilder sb = new StringBuilder()
										.append(data)
										.append("\n");
				if(!isSummary) {
					sb.append(getDate(time));
				} else {
					sb.append(getSummaryDate(time));
				}
				return sb.append("\n").append(data2).toString();
			}
		}
	}
	
	
	private Station getStationById(String stno){
		return stations.stream()
						.filter( f -> f.getStno().equals(stno))
						.findFirst()
						.get();
	}
		
 	private String getSummaryDate(String time){
		// time = 2001-01-25
		if(time.equals("#")){
			return "#";
		} else {
			String[] timeChar = time.split("-");
			String month = "";
			switch(timeChar[1]){
				case "01":
					month = "Jan.";
					break;
				case "02":
					month = "Feb.";
					break;
				case "03":
					month = "Mar.";
					break;	
				case "04":
					month = "Apr.";
					break;
				case "05":
					month = "May";
					break;
				case "06":
					month = "Jun.";
					break;
				case "07":
					month = "Jul.";
					break;	
				case "08":
					month = "Aug.";
					break;	
				case "09":
					month = "Sep.";
					break;	
				case "10":
					month = "Oct.";
					break;	
				case "11":
					month = "Nov.";
					break;
				case "12":
					month = "Dec.";
					break;	
			}
			return timeChar[0]+ "," +month;
		}
	}
	
	private String getDate(String time){
		// time = 2001-01-25
		if(time == null || time.equals("#")){
			return "#";
		} else {
			String[] timeChar = time.split("-");
			return timeChar[0]+ "," +timeChar[2];
		}
	}
	

}
