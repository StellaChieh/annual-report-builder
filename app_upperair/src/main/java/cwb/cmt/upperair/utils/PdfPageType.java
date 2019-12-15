package cwb.cmt.upperair.utils;

import java.util.HashMap;
import java.util.Map;

public enum PdfPageType {
	
	COVER("cover", true, "%d年氣候資料年報第二部分-高空資料 Climatological Data Annual Report %d"),
	REFERENCE_NOTE("reference_note", true, "凡例 Reference Note"),
	CONTENTS("contents", true, "目錄 Contents"),
	INTERLEAF("interleaf", true, "%s"),
	STANDARD("standard", false),
	SIGNIFICANT("significant", false),
	COPYRIGHT("copyright", true, "版權頁 Copyright");
	
	private String type;
	private boolean isOutline;
	private String outlineName;
	static private Map<PdfPageType, String> typeNameMap = new HashMap<>();
	
	public static Map<PdfPageType, String> getTypeNameMap(){
		for(PdfPageType p : values()) {
			if(p.isOutline) {
				typeNameMap.put(p, p.outlineName);
			}
		}
		return typeNameMap;
	}
		
	PdfPageType(String type, boolean isOutline){
		this.type = type;
		this.isOutline = isOutline;
	}
	
	PdfPageType(String type, boolean isOutline, String outlineName){
		this.type = type;
		this.isOutline = isOutline;
		this.outlineName = outlineName;
	}
	
	public String getType(){
		return type;
	}
	
	public static PdfPageType get(String type) {
		for(PdfPageType p : PdfPageType.values()) {
			if(p.getType().equals(type)) {
				return p;
			}
		}
		return null;
	}	
	
	
}
