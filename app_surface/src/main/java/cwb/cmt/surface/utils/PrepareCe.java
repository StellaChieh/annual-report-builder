package cwb.cmt.surface.utils;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

//import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;

import cwb.cmt.surface.dao.CeDao;
import cwb.cmt.surface.model.CeTables;
import cwb.cmt.surface.model.ClimaticElement;
import cwb.cmt.surface.model.MeanStationValues;


@Service("prepareCe")
public class PrepareCe {

	@Resource(name="ceDaoImpl")
	private CeDao cedao;
	
	@Resource(name="parseCeXml")
	private ParseCeXml parseCeXml;
	
    @Resource(name="year")
    protected int year;
    
    @Resource(name="month")
    protected int month;
    
	//2.3 climatic element: get MeanStationValues
	public  List<MeanStationValues> getMeanStationValues(){
		List<ClimaticElement> ceXml = getCeXml();
		List<MeanStationValues> mergeList = new ArrayList<>();
		mergeList = getParams(ceXml);
		return mergeList;
	}
    
	
	public List<ClimaticElement> getCeXml() {
		// 1. get climatic element from xml
		List<ClimaticElement> ceXml = null;
		
		try {
			ceXml = parseCeXml.getClimaticElement();
//			System.out.println("#PrepareCe_parseCeXml# " + ceXml);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ceXml ;
	}
	
	// 2. call dao;param: parsed
	public List<MeanStationValues> getParams(List<ClimaticElement> ceXml){
		Map<String, Object> params = new HashMap<>();
		List<MeanStationValues> mergeList = new ArrayList<>();
		List<MeanStationValues> ceDao = new ArrayList<>();
		for(ClimaticElement t : ceXml) {
			// when flag is None
			if(t.getFlag().equals("None")) {
				for(CeTables c : t.getTables()) {
					params.put("column", c.getColumnName());
					params.put("table", (year==Year.now().getValue())?
							(c.getTableName().replace("his_", "")):c.getTableName());
					params.put("prefix", c.getStnPrefix() + "%");
					params.put("year", year);
					params.put("month", month);
					if (c.getColumnTime()!=null) {
						params.put("columnTime", c.getColumnTime());
						ceDao = cedao.selectClimaticElementTime(params);
					}
					else {
						ceDao = cedao.selectClimaticElement(params);
					}
					//filter station not exist in #{year}
					for(MeanStationValues dao : ceDao) {
						//dao.getStnEndTime() is empty value
						if("".equals(dao.getStnEndTime())) {
							//setting column parsed from ceXml
							dao.setColumnName(c.getColumnName());
							dao.setColumnId(c.getColumnId());
							dao.setStatisticFunction(c.getStatisticFunction());
							dao.setFlag("None");
							mergeList.add(dao);
						}
						else {
							if(dao.getStnEndTime()==null || 
									Integer.valueOf(dao.getStnEndTime().substring(0, 4).replaceAll("/", ""))>=year){
								//setting column parsed from ceXml
								dao.setColumnName(c.getColumnName());
								dao.setColumnId(c.getColumnId());
								dao.setStatisticFunction(c.getStatisticFunction());
								dao.setFlag("None");
								mergeList.add(dao);
							}
						}
					}
				}
			}
			//when flag is not None
			else {
				params.put("flag", t.getFlag());
				for(CeTables c : t.getTables()) {
					params.put("column", c.getColumnName());
					params.put("table", (year==Year.now().getValue())?
							(c.getTableName().replace("his_", "")):c.getTableName());
					params.put("prefix", c.getStnPrefix() + "%");
					params.put("year", year);
					params.put("month", month);
					if (c.getColumnTime()!=null) {
						params.put("columnTime", c.getColumnTime());
						//xml sql select flag , columnTime
						ceDao = cedao.selectClimaticElementFlagTime(params);
					}
					else {
						//xml sql select flag 
						ceDao = cedao.selectClimaticElementFlag(params);
					}
					//filter station not exist in #{year}
					for(MeanStationValues dao : ceDao) {
						//dao.getStnEndTime() is empty value
						if("".equals(dao.getStnEndTime())) {
							//setting column parsed from ceXml
							dao.setColumnName(c.getColumnName());
							dao.setColumnId(c.getColumnId());
							dao.setStatisticFunction(c.getStatisticFunction());
							mergeList.add(dao);
						}
						else {
							if(dao.getStnEndTime()==null || 
									Integer.valueOf(dao.getStnEndTime().substring(0, 4).replaceAll("/", ""))>=year){
								//setting column parsed from ceXml
								dao.setColumnName(c.getColumnName());
								dao.setColumnId(c.getColumnId());
								dao.setStatisticFunction(c.getStatisticFunction());
								mergeList.add(dao);
							}
						}
					}
				}
			}
		}
		return mergeList;
	}
	
}
