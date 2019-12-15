package cwb.cmt.upperair.dao;

import java.util.List;

import cwb.cmt.upperair.model.Station;

// interface name should be the same as mapper.xml's namespace
public interface StationDao {

	// the function name should be the same as mapper.xml's statement id
	// return type should be the same as mapper.xml's statement "resultType"
	// parameter type should be the same as mapper.xml's "parameterType"
	public List<Station> selectStation(List<Station> stn);

}
