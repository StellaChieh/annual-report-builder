package cwb.cmt.upperair.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class SignificantData {

	private String stno;
	private Timestamp time;
	private int sq;
	private int layer;
	private double p;
	private double h;
	private double t;
	private double u;
	private double dd;
	private double ff;
	
	private String printedP;
	private String printedH;
	private String printedT;
	private String printedU;
	private String printedDd;
	private String printedFf;
	
	public List<String> turnToFlapList(){
		return Arrays.asList(printedP, printedH, printedT, printedU, printedDd, printedFf);
	}
	
	public SignificantData() {}
	
	public SignificantData(String stno, Timestamp time) {
		this.stno = stno;
		this.time = time;
	}
	
	public String getStno() {
		return stno;
	}
	public void setStno(String stno) {
		this.stno = stno;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
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
	
	public int getSq() {
		return sq;
	}

	public void setSq(int sq) {
		this.sq = sq;
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
		return "SignificantData [stno=" + stno + ", time=" + time + ", sq=" + sq + ", layer=" + layer 
				+ ", p=" + p + "/" + printedP 
				+ ", h=" + h + "/" + printedH
				+ ", t=" + t + "/" + printedT
				+ ", u=" + u + "/" + printedU
				+ ", dd=" + dd + "/" + printedDd
				+", ff=" + ff + "/" + printedFf
				+ "]\n";
	}


}
