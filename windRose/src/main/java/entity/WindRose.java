package entity;

public class WindRose {
	
	private Double wd;			//方位
	private Double level1;		//第一層
	private Double level2;		//第二層
	private Double level3;		//第三層
	private Double staticWind;	//靜風
	
	public Double getWd() {
		return wd;
	}
	public void setWd(Double wd) {
		this.wd = wd;
	}
	public Double getLevel1() {
		return level1;
	}
	public void setLevel1(Double level1) {
		this.level1 = level1;
	}
	public Double getLevel2() {
		return level2;
	}
	public void setLevel2(Double level2) {
		this.level2 = level2;
	}
	public Double getLevel3() {
		return level3;
	}
	public void setLevel3(Double level3) {
		this.level3 = level3;
	}
	public Double getStaticWind() {
		return staticWind;
	}
	public void setStaticWind(Double staticWind) {
		this.staticWind = staticWind;
	}

}
