package cwb.cmt.summary.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

public class MonthlyData {

	private String stno;
	private Optional<Double> firstValue = Optional.empty();
	private Optional<Double> secondValue = Optional.empty();
	private Optional<LocalDateTime> time = Optional.empty();
	private int beginYear;
	private int endYear;
	
	private Month month;
	
	private BigDecimal fv;
	private BigDecimal sv;
	private LocalDateTime t;
	
	// if fv is null in database, hava empty optional of firstValue
	private void setFirstValue() {
		this.firstValue = fv == null ? Optional.empty() 
									: Optional.of(fv.doubleValue());
	}
	
	// if sv is null in database, hava empty optional of secondValue
	private void setSecondValue() {
		this.secondValue = sv == null ? Optional.empty() 
									: Optional.of(sv.doubleValue());
	}
	
	// if t is null in database, hava empty optional of time
	private void setTime() {
		time = t == null ? Optional.empty() : Optional.ofNullable(t);
	}
	
	/*
	 *  getter and setter
	 */
	public String getStno() {
		return stno;
	}
	public void setStno(String stno) {
		this.stno = stno;
	}
	public Optional<Double> getFirstValue() {
		return firstValue;
	}
	public void setFirstValue(Optional<Double> firstValue) {
		this.firstValue = firstValue;
	}
	public Optional<Double> getSecondValue() {
		return secondValue;
	}
	
	public Optional<LocalDateTime> getTime() {
		return time;
	}
	
	
	public void setFv(BigDecimal fv) {
		this.fv = fv;
		setFirstValue();
	}
	
	public void setSv(BigDecimal sv) {
		this.sv = sv;
		setSecondValue();
	}
	
	public void setT(LocalDateTime time) {
		this.t = time;
		setTime();
	}
	
	public int getBeginYear() {
		return beginYear;
	}
	public void setBeginYear(int beginYear) {
		this.beginYear = beginYear;
	}
	public int getEndYear() {
		return endYear;
	}
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
	public Month getMonth() {
		return month;
	}
	public void setMonth(Month month) {
		this.month = month;
	}
	
	
	@Override
	public String toString() {
		return "MonthlyData [stno=" + stno + ", firstValue=" + fv + ", secondValue=" + sv
				+ ", beginYear=" + beginYear + ", endYear=" + endYear + ", time=" + t + ", month=" + month + "]";
	}
	
}
