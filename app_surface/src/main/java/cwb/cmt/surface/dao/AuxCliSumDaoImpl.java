package cwb.cmt.surface.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import cwb.cmt.surface.model.AuxCliSum;

@Repository("auxclisumDaoImpl")
public class AuxCliSumDaoImpl implements AuxCliSumDao {

	@Resource(name="sqlSessionTemplate")
	SqlSessionTemplate session;


	@Override
	public List<AuxCliSum> selectRadSun(Map<String, Object> AuxCliSumXml) {
		return session.selectList("cwb.cmt.surface.dao.AuxCliSumDao.selectRadSun", AuxCliSumXml);
	}
	
	@Override
	public List<AuxCliSum> selectGlobalRad(Map<String, Object> GlobalRadXml) {
		return session.selectList("cwb.cmt.surface.dao.AuxCliSumDao.selectGlobalRad", GlobalRadXml);
	}
	
	@Override
	public List<AuxCliSum> selectRadHrMax(Map<String, Object> RadMaxXml) {
		return session.selectList("cwb.cmt.surface.dao.AuxCliSumDao.selectRadMax", RadMaxXml);
	}


}
