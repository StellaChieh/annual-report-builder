package cwb.cmt.upperair.dao;

import java.util.List;
import java.util.Map;

import cwb.cmt.upperair.model.SignificantData;

public interface SignificantDao {

	public List<SignificantData> selectSignificantLevelsData(Map<String,String> map);
	
}
