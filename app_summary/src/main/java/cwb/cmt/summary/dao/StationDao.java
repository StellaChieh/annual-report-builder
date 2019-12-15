package cwb.cmt.summary.dao;

import java.util.List;

import cwb.cmt.summary.model.Stations.Station;

public interface StationDao {
	
	public List<Station> selectStation(List<Station> list);
}