package cwb.cmt.upperair.model;

import java.sql.Timestamp;

import cwb.cmt.upperair.utils.StandardAttribute;
import cwb.cmt.upperair.utils.StandardGroup;
import cwb.cmt.upperair.utils.StandardLevel;

public class StandardParentData {
	
	protected String stno;
	protected Timestamp time;
	protected StandardLevel level;
	protected StandardGroup group;
	protected StandardAttribute attribute;
	
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
	
	public StandardGroup getGroup() {
		return group;
	}

	public void setGroup(StandardGroup group) {
		this.group = group;
	}

	public StandardLevel getLevel() {
		return level;
	}

	public void setLevel(StandardLevel level) {
		this.level = level;
	}

	public StandardAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(StandardAttribute attribute) {
		this.attribute = attribute;
	}

	
}
