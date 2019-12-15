package cwb.cmt.summary.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "climaticElement"
})
@XmlRootElement(name = "ClimaticElements")
public class ClimaticElements {

    @XmlElement(name = "ClimaticElement")
    protected List<ClimaticElements.ClimaticElement> climaticElement;

    public List<ClimaticElements.ClimaticElement> getClimaticElement() {
        if (climaticElement == null) {
            climaticElement = new ArrayList<ClimaticElements.ClimaticElement>();
        }
        return this.climaticElement;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "id",
        "chineseName",
        "englishName",
        "unit",
        "contentsGroup",
        "isAutoStnMeasure",
        "numOfLinesPerRow",
        "summaryColumnType",
        "data"
    })
    public static class ClimaticElement {

    	// 氣象要素唯一命名，通常是欄位名
        @XmlElement(name = "Id", required = true)
        protected String id;  
        
        // 氣象要素中文標題
        @XmlElement(name = "ChineseName", required = true)
        protected String chineseName;
        
        // 氣象要素英文標題
        @XmlElement(name = "EnglishName", required = true)
        protected String englishName;
        
        // 氣象要素英文單位
        @XmlElement(name = "Unit", required = true)
        protected String unit;
        
        // 在總目錄裡的目錄結構屬於哪一個gorup
        @XmlElement(name = "ContentsGroup")
        protected int contentsGroup;
        
        // 無人測站是否有觀測此項目
        @XmlElement(name = "IsAutoStnMeasure")
        protected boolean isAutoStnMeasure;
        
        // 書本氣象要素單一cell存有幾排數字。目前分為 1, 2, 3排數字
        @XmlElement(name = "NumOfLinesPerRow")    
        protected int numOfLinesPerRow;
        
        // 書本氣象要素最後一欄應該以何種方式表達？mean, [max, dualMax] => 各自接maxQueryService, dualMaxQueryService
        @XmlElement(name = "SummaryColumnType", required = true)
        protected String summaryColumnType;
        
        
        @XmlElement(name = "Data")
        protected List<ClimaticElements.ClimaticElement.Data> data;
    	
    	public ClimaticElements.ClimaticElement.Data getFirstData(){
    		return data.stream()
					.filter(f -> f.getType().equalsIgnoreCase("firstColumn"))
					.findAny()
					.get();
    	}
    	
    	public Optional<ClimaticElements.ClimaticElement.Data> getTime(){
    		return data.stream()
					.filter(f -> f.getType().equalsIgnoreCase("time"))
					.findAny();
    	}
    	
    	public Optional<ClimaticElements.ClimaticElement.Data> getSecondData(){
    		return data.stream()
					.filter(f -> f.getType().equalsIgnoreCase("secondColumn"))
					.findAny();
    	}
    	
    	// 傳回在書上表格Summary那一欄要表現的名子
    	public String getSummaryName(){
    		if(summaryColumnType.toUpperCase().contains("MEAN")){
    			return "MEAN";
    		} else if(summaryColumnType.toUpperCase().contains("MAX")){
    			return "MAX";
    		} else if(summaryColumnType.toUpperCase().contains("MIN")){
    			return "MIN";
    		} else {
    			return "TOTAL";
    		} 
    	}
    	
        public String getId() {
            return id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public String getChineseName() {
            return chineseName;
        }

        public void setChineseName(String value) {
            this.chineseName = value;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String value) {
            this.englishName = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String value) {
            this.unit = value;
        }

        public int getContentsGroup() {
            return contentsGroup;
        }

        public void setContentsGroup(int value) {
            this.contentsGroup = value;
        }

        public boolean isIsAutoStnMeasure() {
            return isAutoStnMeasure;
        }

        public void setIsAutoStnMeasure(boolean value) {
            this.isAutoStnMeasure = value;
        }

        public int getNumOfLinesPerRow() {
            return numOfLinesPerRow;
        }

        public void setNumOfLinesPerRow(int value) {
            this.numOfLinesPerRow = value;
        }

        public String getSummaryColumnType() {
            return summaryColumnType;
        }

        public void setSummaryColumnType(String value) {
            this.summaryColumnType = value;
        }

        public List<ClimaticElements.ClimaticElement.Data> getData() {
            if (data == null) {
                data = new ArrayList<ClimaticElements.ClimaticElement.Data>();
            }
            return this.data;
        }
        

        @Override
		public String toString() {
			return "ClimaticElement [id=" + id + ", chineseName=" + chineseName + ", englishName=" + englishName
					+ ", unit=" + unit + ", contentsGroup=" + contentsGroup + ", isAutoStnMeasure=" + isAutoStnMeasure
					+ ", numOfLinesPerRow=" + numOfLinesPerRow + ", summaryColumnType=" + summaryColumnType + ", data="
					+ data + "]\n";
		}

		@XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "dbTable",
            "dbColumn",
            "specialNumber",
            "approximation",
            "substituteZero"
        })
        public static class Data {

        	// 要從何資料庫table抓資料
            @XmlElement(name = "DbTable", required = true)
            protected String dbTable;
            
            // 對應的table欄位
            @XmlElement(name = "DbColumn", required = true)
            protected String dbColumn;
            
            // 此欄位的特殊值。time欄位沒有特殊值
            @XmlElement(name = "SpecialNumber")
            protected List<ClimaticElements.ClimaticElement.Data.SpecialNumber> specialNumber;
            
            // 單年度數字是否記到小數點後一位或整數位
            @XmlElement(name = "Approximation")
            protected Integer approximation;
            
            // 遇到0是否要替換成'-'
            @XmlElement(name = "SubstituteZero")
            protected Boolean substituteZero;
            
            // 欄位種類，mainColumn表示為主欄位；secondColumn表次要欄位，其值跟著主欄位走 ;
            // columnTime表示為時間單位，其值跟著主欄位走
            @XmlAttribute(name = "type")
            protected String type;

            public Map<Double, String> getSpecialNumberMap() {
            	return getSpecialNumber().stream()
            						.collect(Collectors.toMap(SpecialNumber::getNumber, SpecialNumber::getSymbol));
            }
            
            public boolean isDecimalType() {
            	return this.approximation == -1;
            }
            
            
            public String getDbTable() {
                return dbTable;
            }

            public void setDbTable(String value) {
                this.dbTable = value;
            }

            public String getDbColumn() {
                return dbColumn;
            }

            public void setDbColumn(String value) {
                this.dbColumn = value;
            }
            
        
            public List<ClimaticElements.ClimaticElement.Data.SpecialNumber> getSpecialNumber() {
                if (specialNumber == null) {
                    specialNumber = new ArrayList<ClimaticElements.ClimaticElement.Data.SpecialNumber>();
                }
                return this.specialNumber;
            }

            public Integer getApproximation() {
                return approximation;
            }

            public void setApproximation(Integer value) {
                this.approximation = value;
            }

            public Boolean isSubstituteZero() {
                return substituteZero;
            }

            public void setSubstituteZero(Boolean value) {
                this.substituteZero = value;
            }

            public String getType() {
                return type;
            }

            public void setType(String value) {
                this.type = value;
            }
      
            @Override
			public String toString() {
				return "Data [dbTable=" + dbTable + ", dbColumn=" + dbColumn + ", specialNumber=" + specialNumber
						+ ", approximation=" + approximation + ", substituteZero=" + substituteZero + ", type=" + type
						+ "]\n";
			}

			@XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "value"
            })
            public static class SpecialNumber {

                @XmlValue
                protected String value;
                @XmlAttribute(name = "number")
                protected Double number;
                @XmlAttribute(name = "symbol")
                protected String symbol;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public Double getNumber() {
                    return number;
                }

                public void setNumber(Double value) {
                    this.number = value;
                }

                public String getSymbol() {
                    return symbol;
                }

                public void setSymbol(String value) {
                    this.symbol = value;
                }

				@Override
				public String toString() {
					return "SpecialNumber [value=" + value + ", number=" + number + ", symbol=" + symbol + "]\n";
				}
                
            }

        }

    }

}
