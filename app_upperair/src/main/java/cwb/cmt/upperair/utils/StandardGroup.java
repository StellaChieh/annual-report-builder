package cwb.cmt.upperair.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum StandardGroup {

	NLH(1, "SYNOPTIC", StandardLevel.NLH, StandardBookTable.TABLE1, StandardColumn.NLH)
	, SURFACE(2, "SURFACE", StandardLevel.SURFACE, StandardBookTable.TABLE1, StandardColumn.P, StandardColumn.T, StandardColumn.U, StandardColumn.Td, StandardColumn.dd, StandardColumn.ff)
	, P1000(3, "1000 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE1, 1000, StandardColumn.H, StandardColumn.T, StandardColumn.U, StandardColumn.Td, StandardColumn.dd, StandardColumn.ff)
	, P925(4, "925 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE1, 925, StandardColumn.H, StandardColumn.T, StandardColumn.U, StandardColumn.Td, StandardColumn.dd, StandardColumn.ff)
	, P850(5, "850 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE2, 850, StandardColumn.H, StandardColumn.T, StandardColumn.U, StandardColumn.Td, StandardColumn.dd, StandardColumn.ff)
	, P700(6, "700 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE2 ,700, StandardColumn.H, StandardColumn.T, StandardColumn.U, StandardColumn.Td, StandardColumn.dd, StandardColumn.ff)
	, P500(7, "500 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE2, 500, StandardColumn.H, StandardColumn.T, StandardColumn.U, StandardColumn.Td, StandardColumn.dd, StandardColumn.ff)
	, P400(8, "400 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE2, 400, StandardColumn.H, StandardColumn.T, StandardColumn.U, StandardColumn.Td, StandardColumn.dd, StandardColumn.ff)
	, P300(9, "300 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE3, 300, StandardColumn.H, StandardColumn.T, StandardColumn.U, StandardColumn.Td, StandardColumn.dd, StandardColumn.ff)
	, P250(10, "250 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE3, 250, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, P200(11, "200 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE3, 200, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, P150(12, "150 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE3, 150, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, P100(13, "100 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE3, 100, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, P70(14, "70 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE4, 70, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, P50(15, "50 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE4, 50, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, P30(16, "30 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE4, 30, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, P20(17, "20 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE4, 20, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, P10(18, "10 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE4, 10, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, P5(19, "5 hPa", StandardLevel.STANDARD, StandardBookTable.TABLE5, 5, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, TROPI(20, "TROPOPAUSE I", StandardLevel.TROPI, StandardBookTable.TABLE5, StandardColumn.P, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, TROPII(21, "TROPOPAUSE II", StandardLevel.TROPII, StandardBookTable.TABLE5, StandardColumn.P, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff)
	, LAST(22, "LAST LEVEL", StandardLevel.LAST, StandardBookTable.TABLE5, StandardColumn.P, StandardColumn.H, StandardColumn.T, StandardColumn.dd, StandardColumn.ff); 
	
	int order;
	String groupNameInBook;
	StandardLevel level;
	StandardBookTable table;
	int p;
	String columnName;
	StandardColumn field1; StandardColumn field2; StandardColumn field3; StandardColumn field4; StandardColumn field5; StandardColumn field6;
	
	private static Map<Integer, StandardGroup> pressureAndGroupMap = new HashMap<Integer, StandardGroup>();
	private Map<StandardGroup, List<StandardColumn>> groupAndColumnMap = new HashMap<>();
	
	// NLH
	StandardGroup(int order, String groupNameInBook, StandardLevel level, StandardBookTable table, StandardColumn field1){
		this.order = order;
		this.groupNameInBook = groupNameInBook;
		this.level = level;
		this.table = table;
		this.order = order;
		this.field1 = field1;
		groupAndColumnMap.put(this, Arrays.asList(field1));
	}

	// SURFACE
	StandardGroup(int order, String groupNameInBook, StandardLevel level, StandardBookTable table, StandardColumn field1, StandardColumn field2, StandardColumn field3, StandardColumn field4, StandardColumn field5, StandardColumn field6) {
		this.order = order;
		this.groupNameInBook = groupNameInBook;
		this.level = level;
		this.table = table;
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
		this.field4 = field4;
		this.field5 = field5;
		this.field6 = field6;
		groupAndColumnMap.put(this, Arrays.asList(field1, field2, field3, field4, field5, field6));
	}

	// P1000-P300
	StandardGroup(int order, String groupNameInBook, StandardLevel level, StandardBookTable table, int p, StandardColumn field1, StandardColumn field2, StandardColumn field3, StandardColumn field4, StandardColumn field5, StandardColumn field6) {
		this.order = order;
		this.groupNameInBook = groupNameInBook;
		this.level = level;
		this.table = table;
		this.p = p;
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
		this.field4 = field4;
		this.field5 = field5;
		this.field6 = field6;
		groupAndColumnMap.put(this, Arrays.asList(field1, field2, field3, field4, field5, field6));
	}
	
	// P250-P5
	StandardGroup(int order, String groupNameInBook, StandardLevel level, StandardBookTable table, int p, StandardColumn field1, StandardColumn field2, StandardColumn field3, StandardColumn field4) {
		this.order = order;
		this.groupNameInBook = groupNameInBook;
		this.level = level;
		this.table = table;
		this.p = p;
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
		this.field4 = field4;
		groupAndColumnMap.put(this, Arrays.asList(field1, field2, field3, field4));
	}
	
	// TROPI, TROPII, LAST
	StandardGroup(int order, String groupNameInBook, StandardLevel level, StandardBookTable table, StandardColumn field1, StandardColumn field2, StandardColumn field3, StandardColumn field4, StandardColumn field5) {
		this.order = order;
		this.groupNameInBook = groupNameInBook;
		this.level = level;
		this.table = table;
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
		this.field4 = field4;
		this.field3 = field5;
		groupAndColumnMap.put(this, Arrays.asList(field1, field2, field3, field4, field5));
	}
	
	static {
		for(StandardGroup g : values()) {
			if( g !=StandardGroup.NLH 
					&& g!=StandardGroup.SURFACE
					&& g!=StandardGroup.LAST
					&& g!=StandardGroup.TROPI
					&& g!=StandardGroup.TROPII) {
				pressureAndGroupMap.put(g.p, g);
			}
		}
	}
			
	public static StandardGroup LOOKP_FROM_PRESSURE(int p) {
		return pressureAndGroupMap.get(p);
	}
	
	
	public static List<StandardGroup> LOOKUP_FROM_TABLE(StandardBookTable table) {
		List<StandardGroup> groups = new ArrayList<>();
		for(StandardGroup g : values()) {
			if(g.getTable() == table) {
				groups.add(g);
			}
		}
		return groups;
	}
	
	public List<StandardColumn> getColumns(){
		return groupAndColumnMap.get(this);
	}
	
	public StandardBookTable getTable() {
		return this.table;
	}
	
	public String getGroupNameInBook() {
		return this.groupNameInBook;
	}
	
	public StandardLevel getLevel() {
		return this.level;
	}
}
