package cwb.cmt.surface.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import cwb.cmt.surface.model.MeanStationValues;

@Repository("ceDaoImpl")
public class CeDaoImpl implements CeDao {

	@Resource(name="sqlSessionTemplate")
	SqlSessionTemplate session;


	@Override
	public List<MeanStationValues> selectClimaticElement(Map<String, Object> ceXml) {
		return session.selectList("cwb.cmt.surface.dao.CeDao.selectClimaticElement", ceXml);
	}
	
	@Override
	public List<MeanStationValues> selectClimaticElementFlag(Map<String, Object> ceXml) {
		return session.selectList("cwb.cmt.surface.dao.CeDao.selectClimaticElementFlag", ceXml);
	}

	@Override
	public List<MeanStationValues> selectClimaticElementFlagTime(Map<String, Object> ceXml) {
		return session.selectList("cwb.cmt.surface.dao.CeDao.selectClimaticElementFlagTime", ceXml);
	}
	
	@Override
	public List<MeanStationValues> selectClimaticElementTime(Map<String, Object> ceXml) {
		return session.selectList("cwb.cmt.surface.dao.CeDao.selectClimaticElementTime", ceXml);
	}


}
