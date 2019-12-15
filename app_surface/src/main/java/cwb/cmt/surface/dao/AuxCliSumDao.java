package cwb.cmt.surface.dao;

import java.util.List;
import java.util.Map;

import cwb.cmt.surface.model.AuxCliSum;


public interface AuxCliSumDao {

	List<AuxCliSum> selectRadSun(Map<String, Object> AuxCliSumXml);

	List<AuxCliSum> selectGlobalRad(Map<String, Object> GlobalRadXml);
	
	List<AuxCliSum> selectRadHrMax(Map<String, Object> RadMaxXml);

}
