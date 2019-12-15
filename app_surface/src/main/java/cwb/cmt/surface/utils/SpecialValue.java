package cwb.cmt.surface.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public enum SpecialValue { 
	EquipmentMalfunction("X", "-9.95", "-99.5", "-99.95",
			"-999.95", "-999.5", "-9999.5", "-9999.95", "-9995", "-9999.1"),
	Unknown("/", "-99.7", "-999.7", "-9999.7","-9.7"),
	CancelStation("@", "@"),
	NewStation("~", "~"),
	CeUnknown("/", "-99.7", "-999.7", "-9999.7", null, "-9999.6", "-9997"),
	Trace("T", "-9.8"),
	SubstituteZero("-", "0", "0.0");
	
	private final String number;
	private final String[] value;
	
	
	private SpecialValue(String number, String... value){
		this.number = number;
		this.value = value;
	}
	
	public String getNumber(){
		return this.number;
	}
	
	public String[] getValue(){
		return this.value;
	}
	
	//1.4.1
	public static String conversion(String s) {
		List<String> EquipmentMalfunctionList = Arrays.asList(EquipmentMalfunction.getValue());
		List<String> UnknownList = Arrays.asList(Unknown.getValue());
		
		if (s != null) {
			if (EquipmentMalfunctionList.contains(s)) {
				return EquipmentMalfunction.getNumber();
			}
			else if (UnknownList.contains(s)) {
				return Unknown.getNumber();
			}
			//empty value
			else if (Float.isNaN(Float.parseFloat(s))){
				return " ";
			}
			//abnormal value
			else if (Float.compare(Float.parseFloat(s), 0.0f) < 0) {
				return "X";
			}
			// normal value
			else{
				float normalValue = new BigDecimal(s)
						.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
				return String.valueOf(normalValue);
			}
		}
		return null;
	}
	
	
	//1.4.2
	public static String conversion2(String s, String flag) {
		List<String> EquipmentMalfunctionList = Arrays.asList(EquipmentMalfunction.getValue());
		List<String> UnknownList = Arrays.asList(Unknown.getValue());
		List<String> CancelStationList = Arrays.asList(CancelStation.getValue());
		
		int flagCount = 0;
		if(flag.equals("1")) {
			flagCount++;
		}
		
		if(!CancelStationList.contains(s)) {
			
			if (Float.isNaN(Float.parseFloat(s))) {
				return " ";
			}
			else if (Float.compare(Float.parseFloat(s), 0F) < 0) {
	            // Handle special values: 
	           if (EquipmentMalfunctionList.contains(s)) {
	        	   return "X";
	           }
	           else if (UnknownList.contains(s)) {
	        	   return "/";
	           }
	            // default negative values: 
	           else {
	        	   return "X";
	           }
			 }
	        else {
	        	float normalValue = new BigDecimal(s)
						.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
	        	String value = (flagCount>0)?("*"+String.valueOf(normalValue)):(String.valueOf(normalValue));
				return value;
	        }
		}
		return "@";
	}

	//1.4.3
	public static String conversion3(String s) {
		List<String> EquipmentMalfunctionList = Arrays.asList(EquipmentMalfunction.getValue());
		List<String> UnknownList = Arrays.asList(Unknown.getValue());
		List<String> CancelStationList = Arrays.asList(CancelStation.getValue());
		
		if(!CancelStationList.contains(s)) {
			
			if (Float.isNaN(Float.parseFloat(s))) {
				return " ";
			}
			else if (Float.compare(Float.parseFloat(s), 0F) < 0) {
	            // Handle special values: 
	           if (EquipmentMalfunctionList.contains(s)) {
	        	   return "X";
	           }
	           else if (UnknownList.contains(s)) {
	        	   return "/";
	           }
	            // default negative values: 
	           else {
	        	   return "X";
	           }
			 }
	        else {
	        	float normalValue = new BigDecimal(s)
						.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
				return String.valueOf(normalValue);
	        }
		}
		return "@";
	}
	
	//2.3-2.20
	public static String conversion4(String s, boolean isSubstituteZero, String flag, String precision) {
		List<String> EquipmentMalfunctionList = Arrays.asList(EquipmentMalfunction.getValue());
		List<String> CeUnknownList = Arrays.asList(CeUnknown.getValue());
		List<String> TraceList = Arrays.asList(Trace.getValue());
		List<String> SubstituteZeroList = Arrays.asList(SubstituteZero.getValue());
		
		if(isSubstituteZero && SubstituteZeroList.contains(s)) {
			return SubstituteZero.getNumber();
		}
		else if (EquipmentMalfunctionList.contains(s)) {
			return EquipmentMalfunction.getNumber();
		}
		else if (CeUnknownList.contains(s)) {
			return CeUnknown.getNumber();
		}
		else if (TraceList.contains(s)) {
			return Trace.getNumber();
		}
		else {
			String data = new BigDecimal(s).setScale(Integer.valueOf(precision), BigDecimal.ROUND_HALF_UP).toString();
			if(flag!=null && flag.equals("1")) {
				data= "*"+data;
			}
			return data;
		}
	}
	
}
