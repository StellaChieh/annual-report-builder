package cwb.cmt.upperair.model;

public class StandardNlhData extends StandardParentData {
	
	private String nlh;
	
	public String getNlh() {
		return nlh;
	}

	public void setNlh(String nlh) {
		this.nlh = nlh;
	}
		
	@Override
	public String toString() {
		return "StandardNlhData [stno=" + stno + ", time=" + time + ", level=" + level + ", group=" + group
				+ ", attribute=" + attribute + ", nlh=" + nlh + "]\n";
	}

}
