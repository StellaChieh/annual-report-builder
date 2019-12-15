package cwb.cmt.surface.utils;


import java.util.LinkedHashMap;
import java.util.Map;

public enum InterLeaf { 
	InterLeaf_ONE("1." + "\n" + "綜  觀  氣  象  站" , "Surface Synoptic Stations"),
	INTERLEAF_TWO("2." + "\n" + "綜  觀  及  自  動  站  觀  測  年  表", "Year Reports of Surface Synoptic and Automatic Weather Stations"),
	INTERLEAF_THREE("3."+ "\n" +"風  花  圖", "Wind Roses"),
	INTERLEAF_FOUR("4." + "\n" + "颱  風  雨  量  圖", "Rainfall Chart of Typhoon"),
	INTERLEAF_FIVE("5." + "\n" + "溫  度  雨  量  圖", "Daily Temp. and Rainfall Chart"),
	INTERLEAF_FIRST("6." + "\n" + "溫  度、雨  量  分  佈  圖", "Monthly Temperature Chart");
	
	private final String number;
	private final String value;
	private static Map<String, String> interLeafMap;
	
	private InterLeaf(String number, String value){
		this.number = number;
		this.value = value;
	}
	
	public String getNumber(){
		return this.number;
	}
	
	public String getValue(){
		return this.value;
	}
	
//	public static String getDescriptionByCode(String number) {
//        if (interLeafMap == null) {
//            initializeMapping();
//        }
//        if (interLeafMap.containsKey(number)) {
//            return interLeafMap.get(number);
//        }
//        return null;
//    }
	
	public static Map<String, String> initializeMapping() {
		interLeafMap = new LinkedHashMap<String, String>();
        for (InterLeaf s : InterLeaf.values()) {
        	interLeafMap.put(s.number, s.value);
        }
        return interLeafMap;
    }
	
	
}
