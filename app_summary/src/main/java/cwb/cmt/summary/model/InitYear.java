package cwb.cmt.summary.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InitYear<k, v> extends HashMap<k, v>{

	private static final long serialVersionUID = 1L;
	
	@Override
	public v get(Object key) {
		@SuppressWarnings("unchecked")
		Map<String, String> mapKey = (Map<String, String>)key;
		String stno = (String)mapKey.keySet().toArray()[0];
		String ce = ((String)(mapKey.values().toArray()[0])).toLowerCase();
		return super.get(Collections.singletonMap(stno, ce));
	}
	

}
