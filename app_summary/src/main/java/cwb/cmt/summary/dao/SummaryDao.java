package cwb.cmt.summary.dao;

import java.util.List;
import java.util.Map;

import cwb.cmt.summary.model.SummaryModel;

public interface SummaryDao {
	
	public List<SummaryModel> selectDbSummary(Map<String, Object> params);
	
	public SummaryModel selectOneDbSummary(Map<String, Object> params);
	
	public void insert(List<SummaryModel> entities);
	
	public void clear();
}
