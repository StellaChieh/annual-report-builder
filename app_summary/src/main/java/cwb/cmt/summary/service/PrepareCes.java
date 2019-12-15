package cwb.cmt.summary.service;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cwb.cmt.summary.config.Config;
import cwb.cmt.summary.model.ClimaticElements;
import cwb.cmt.summary.model.ClimaticElements.ClimaticElement;

@Service
public class PrepareCes {
	
	private String summaryClimaticElementXmlConfigPath;
	private List<ClimaticElement> xmlCes;

	@Autowired
	public PrepareCes(Config config) {
		this.summaryClimaticElementXmlConfigPath = config.getSummaryClimaticElementXmlConfigPath();
	}
	
	// parse external climaticElement config file
	public List<ClimaticElement> getClimaticElements(){
		if(xmlCes == null) {
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(ClimaticElements.class);
				Unmarshaller unM = jaxbContext.createUnmarshaller();
				xmlCes = ((ClimaticElements)unM.unmarshal(new File(this.summaryClimaticElementXmlConfigPath))).getClimaticElement();
				xmlCes = Collections.unmodifiableList(xmlCes);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
		return xmlCes;
	}
	
}