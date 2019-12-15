package cwb.cmt.upperair.dao;

import java.util.List;
import java.util.Map;

import cwb.cmt.upperair.model.StandardData;
import cwb.cmt.upperair.model.StandardNlhData;

public interface StandardDao {
	
	public List<StandardNlhData> selectNlhData(Map<String,String> map);
	public List<StandardData> selectSurfaceData(Map<String,String> map);
	public List<StandardData> selectStandardLevelsData(Map<String,String> map);
	public List<StandardData> selectTropopauseIData(Map<String,String> map);
	public List<StandardData> selectTropopauseIIData(Map<String,String> map);
	public List<StandardData> selectlLastLevelData(Map<String,String> map);
	
}
