package cwb.cmt.summary.composeData;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import cwb.cmt.summary.dao.DbInteract;
import cwb.cmt.summary.main.SummaryApplication;
import cwb.cmt.summary.utils.SummaryColumnType;

public class MakeDataServiceManager {

	private static ApplicationContext context;
	
	public static Map<SummaryColumnType, MakeDataService> getMakeDataService () {
		if(context == null) {
			context = SummaryApplication.getApplicationContext();
		}
		Map<SummaryColumnType, MakeDataService> map = new HashMap<>();
		String composeData = "compose%sData";
		String dbInteractionBeanName;
		String composeDataBeanName;
		for(SummaryColumnType type : SummaryColumnType.values()) {
			dbInteractionBeanName = "dbInteraction";
			composeDataBeanName = String.format(composeData
					, turnToFirstLetterUppercase(type.getTypeName()));
			if(type == SummaryColumnType.EVAPA_MAX) {
				dbInteractionBeanName = "evapAMaxDbInteraction";
				composeDataBeanName = "composeMaxData";
			} else if (type == SummaryColumnType.SEA_PRES_MEAN){
				map.put(type, createMakeSeaPresMeanDataService("composeMeanData", dbInteractionBeanName));
				continue;
			} 
			map.put(type, createMakeDataService(composeDataBeanName, dbInteractionBeanName));
		}
		return map;
	}
	
	private static String turnToFirstLetterUppercase(String name) {
		String firstLetter = String.valueOf(name.charAt(0)).toUpperCase();
		return firstLetter + name.substring(1, name.length());
	}
	
	private static MakeDataService createMakeDataService(String composeDataBeanName, String dbInteractionBeanName) {
		MakeDataService service = (MakeDataService)context.getBean("makeDataService");
		ComposeData composeData = (ComposeData)context.getBean(composeDataBeanName);
		DbInteract dbInteract = (DbInteract)context.getBean(dbInteractionBeanName);
		service.setComposeDataService(composeData);
		service.setDbInteraction(dbInteract);
		return service;
	}
	
	private static MakeDataService createMakeSeaPresMeanDataService(String composeDataBeanName, String dbInteractionBeanName) {
		MakeDataService service = (MakeDataService)context.getBean("makeSeaPresMeanDataService");
		ComposeData composeData = (ComposeData)context.getBean(composeDataBeanName);
		DbInteract dbInteract = (DbInteract)context.getBean(dbInteractionBeanName);
		service.setComposeDataService(composeData);
		service.setDbInteraction(dbInteract);
		return service;
	}
}
