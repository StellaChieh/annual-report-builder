package cwb.cmt.surface.dao;

import java.util.List;
import java.util.Map;

import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.MeanStationValues;


public interface CeDao {

	List<MeanStationValues> selectClimaticElement(Map<String, Object> ceXml);
	List<MeanStationValues> selectClimaticElementFlag(Map<String, Object> ceXml);
	List<MeanStationValues> selectClimaticElementFlagTime(Map<String, Object> ceXml);
	List<MeanStationValues> selectClimaticElementTime(Map<String, Object> ceXml);
}
