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
import cwb.cmt.surface.model.CliSum;
import cwb.cmt.surface.model.CliSumList;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.model.StationList;

@Service("parseStnXml")
public class ParseStnXml {
	//Station
	@Resource(name="castorMarshaller")
	private Unmarshaller unMarshaller;
	//AuxCliSum
	@Resource(name="castorMarshaller2")
	private Unmarshaller unMarshaller2;
	//Clisum
	@Resource(name="castorMarshaller3")
	private Unmarshaller unMarshaller3;
	
	@Resource(name="rootPath")
	private String rootPath;
	
	private String stnXmlPath;
	
	@Resource(name="surfaceStnXmlPath")
	public void setStnXmlPath(String stnXmlPath) {
		this.stnXmlPath = Paths.get(rootPath, stnXmlPath).toString();
	}
	
	public List<Station> getStns() throws IOException {
		FileInputStream is = null;
		StationList list = null;
		try {
			is = new FileInputStream(stnXmlPath);
			list = (StationList)unMarshaller.unmarshal(new StreamSource(is));
		} finally {
			if(is != null) {
				is.close();
			}
		}
//		System.out.println("list.getStns>> "+list.getStns());
		return list.getStns();
	}
	
	//connect to prepareAuxCliSum.java
	public List<AuxCliSum> getStnsAuxCliSum() throws IOException {
		FileInputStream is = null;
		AuxCliSumList list = null;
		try {
			is = new FileInputStream(stnXmlPath);
			list = (AuxCliSumList)unMarshaller2.unmarshal(new StreamSource(is));
		} finally {
			if(is != null) {
				is.close();
			}
		}
		return list.getAuxclisumStns();
	}
	
	//connect to prepareAuxCliSum.java
	public List<CliSum> getStnsCliSum() throws IOException {
		FileInputStream is = null;
		CliSumList list = null;
		try {
			is = new FileInputStream(stnXmlPath);
			list = (CliSumList)unMarshaller3.unmarshal(new StreamSource(is));
		} finally {
			if(is != null) {
				is.close();
			}
		}
		return list.getClisumStns();
	}
	


}
