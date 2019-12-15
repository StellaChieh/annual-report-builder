package cwb.cmt.surface.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import cwb.cmt.surface.model.Station;

@Repository("stationDaoImpl")
public class StationDaoImpl implements StationDao{

	@Resource(name="sqlSessionTemplate")
	SqlSessionTemplate session;
	
    //Time: year
    @Resource(name="year")
    protected int year;
	
	@Override
	public List<Station> selectStation(List<Station> stns) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.StationDao.selectStation", stns);
	}
	
	public List<Station> selectOtherStns(Map<String, Integer> params) {
		// first param: mapper function name
		// second param: mapper param
		return session.selectList("cwb.cmt.surface.dao.StationDao.selectOtherStns", params);
	}
	
}
