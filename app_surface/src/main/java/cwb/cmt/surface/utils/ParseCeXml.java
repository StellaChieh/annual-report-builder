package cwb.cmt.surface.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.AuxCliSumList;
import cwb.cmt.surface.model.CeList;
import cwb.cmt.surface.model.CliSum;
import cwb.cmt.surface.model.CliSumList;
import cwb.cmt.surface.model.ClimaticElement;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.model.StationList;

@Service("parseCeXml")
public class ParseCeXml {
	//Climatic Element
	@Resource(name="castorMarshaller4")
	private Unmarshaller unMarshaller4;
	
	@Resource(name="rootPath")
	private String rootPath;
	
	private String ceXmlPath;
	
	@Resource(name="surfaceCeXmlPath")
	public void setCeXmlPath(String ceXmlPath) {
		this.ceXmlPath = Paths.get(rootPath, ceXmlPath).toString();
	}
	//get climate Element from xml
	public List<ClimaticElement> getClimaticElement() throws IOException {
		FileInputStream is = null;
		CeList list = null;
		
		try {
			is = new FileInputStream(ceXmlPath);
			list = (CeList)unMarshaller4.unmarshal(new StreamSource(is));
		} finally {
			if(is != null) {
				is.close();
			}
		}
		return list.getCe();
	}
}
