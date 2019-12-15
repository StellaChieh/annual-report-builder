package cwb.cmt.surface.model;

import java.util.LinkedList;
import java.util.List;

public class ClimaticElement {
	
	private String id;                     	//氣象要素唯一命名，通常是欄位名 
	private String chineseTitle;          	//氣象要素中文標題
	private String englishTitle;		 	//氣象要素英文標題
	private String chineseUnit;		    	//氣象要素中文單位
	private String englishUnit; 	   		//氣象要素英文單位
	private boolean substituteZero;   		//是否要將0取代為-
	private List<CeTables> tables;  
	private String flag;                	//是否有要參照的flag
	private String precision;				//資料四捨五入至第幾位數
	private String numOfTableRow;
	
 	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChineseTitle() {
		return chineseTitle;
	}
	public void setChineseTitle(String chineseTitle) {
		this.chineseTitle = chineseTitle;
	}
	public String getEnglishTitle() {
		return englishTitle;
	}
	public void setEnglishTitle(String englishTitle) {
		this.englishTitle = englishTitle;
	}
	
	public String getChineseUnit() {
		return chineseUnit;
	}
	public void setChineseUnit(String chineseUnit) {
		this.chineseUnit = chineseUnit;
	}
	public String getEnglishUnit() {
		return englishUnit;
	}
	public void setEnglishUnit(String englishUnit) {
		this.englishUnit = englishUnit;
	}
	
	public List<CeTables> getTables() {
		return tables;
	}
	public void setTables(List<CeTables> tables) {
		this.tables = tables;
	}

	public boolean getSubstituteZero() {
		return substituteZero;
	}
	public void setSubstituteZero(boolean substituteZero) {
		this.substituteZero = substituteZero;
	}

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getNumOfTableRow() {
		return numOfTableRow;
	}
	public void setNumOfTableRow(String numOfTableRow) {
		this.numOfTableRow = numOfTableRow;
	}
	
	@Override
	public String toString() {
		return "ClimaticElement [id=" + id + ", chineseTitle=" + chineseTitle +
				", englishTitle=" + englishTitle + ", chineseUnit=" + chineseUnit +
				", englishUnit=" +englishUnit + ", tables=" + tables + 
				", substituteZero=" + substituteZero +
				", flag=" + flag +
				", precision=" + precision +
				", numOfTableRow=" + numOfTableRow +
				"]\n";
	}

}
