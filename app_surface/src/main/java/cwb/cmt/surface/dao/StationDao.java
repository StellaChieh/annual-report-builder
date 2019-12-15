package cwb.cmt.surface.dao;

import java.util.List;
import java.util.Map;

import cwb.cmt.surface.model.Station;

public interface StationDao {

	List<Station> selectStation(List<Station> stns);
	
	List<Station> selectOtherStns(Map<String,Integer> params);
}
