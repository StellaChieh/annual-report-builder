package cwb.cmt.upperair.model;

import java.util.List;

public class StationList {

	private List<Station> stns;

	public List<Station> getStns() {
		return stns;
	}

	public void setStns(List<Station> stns) {
		this.stns = stns;
	}
	
	@Override
	public String toString() {
		return stns.toString();
	}
}
