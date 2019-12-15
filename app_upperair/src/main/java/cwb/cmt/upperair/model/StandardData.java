package cwb.cmt.upperair.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cwb.cmt.upperair.utils.StandardColumn;


public class StandardData extends StandardParentData {

	private double p;
	private double h;
	private double t;
	private double u;
	private double td;
	private double dd;
	private double ff;
	private String printedP;
	private String printedH;
	private String printedT;
	private String printedU;
	private String printedTd;
	private String printedDd;
	private String printedFf;
	private static Map<StandardColumn, Field> columnAndFieldMap = new HashMap<>();
	
	static {
		for(StandardColumn column : StandardColumn.values()) {
			try {
				for(Field field : Class.forName("cwb.cmt.upperair.model.StandardData").getDeclaredFields()) {
					if(column.getVariableNameInModelClass()!= null) {
						if( ("printed" + column.getVariableNameInModelClass())
								.equalsIgnoreCase((field.getName()))){
							columnAndFieldMap.put(column, field);
						}	
					} 
				}
			} catch (SecurityException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<String> printPdfValues() {
		List<StandardColumn> containingColumns = group.getColumns();
		List<String> fieldValues = new ArrayList<>();
		containingColumns.forEach(e -> {
			try {
				fieldValues.add((String)(columnAndFieldMap.get(e).get(this)));
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		return fieldValues;
	}
		
	// Getter and setter
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	public double getH() {
		return h;
	}
	public void setH(double h) {
		this.h = h;
	}
	public double getT() {
		return t;
	}
	public void setT(double t) {
		this.t = t;
	}
	public double getU() {
		return u;
	}
	public void setU(double u) {
		this.u = u;
	}
	public double getTd() {
		return td;
	}
	public void setTd(double td) {
		this.td = td;
	}
	public double getDd() {
		return dd;
	}
	public void setDd(double dd) {
		this.dd = dd;
	}
	public double getFf() {
		return ff;
	}
	public void setFf(double ff) {
		this.ff = ff;
	}

	public String getPrintedP() {
		return printedP;
	}
	public void setPrintedP(String printedP) {
		this.printedP = printedP;
	}
	public String getPrintedH() {
		return printedH;
	}
	public void setPrintedH(String printedH) {
		this.printedH = printedH;
	}
	public String getPrintedT() {
		return printedT;
	}
	public void setPrintedT(String printedT) {
		this.printedT = printedT;
	}
	public String getPrintedU() {
		return printedU;
	}
	public void setPrintedU(String printedU) {
		this.printedU = printedU;
	}
	public String getPrintedTd() {
		return printedTd;
	}
	public void setPrintedTd(String printedTd) {
		this.printedTd = printedTd;
	}
	public String getPrintedDd() {
		return printedDd;
	}
	public void setPrintedDd(String printedDd) {
		this.printedDd = printedDd;
	}
	public String getPrintedFf() {
		return printedFf;
	}
	public void setPrintedFf(String printedFf) {
		this.printedFf = printedFf;
	}
	@Override
	public String toString() {
		return "StandardData [stno=" + stno + ", time=" + time + ", level=" + level + ", group=" + group
				+ ", attribute=" + attribute 
				+ ", p=" + p + "/" + printedP 
				+ ", h=" + h + "/" + printedH
				+ ", t=" + t + "/" + printedT
				+ ", u=" + u + "/" + printedU
				+ ", td=" + td + "/" + printedTd
				+ ", dd=" + dd + "/" + printedDd
				+ ", ff=" + ff +  "/" + printedFf +"]\n";
	}

	
}
