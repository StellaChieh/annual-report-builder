package cwb.cmt.summary.composeData;

import org.springframework.stereotype.Service;

/*
 * 專門用來處理SeaPres
 * > 800m 的站直接塞"#"
 */
@Service
public class MakeSeaPresMeanDataService extends MakeDataService {

	private boolean altitudeHigherThan800m(String stringAltitude) {
		stringAltitude = stringAltitude.trim().substring(0, stringAltitude.length()-1);
		if(Double.parseDouble(stringAltitude) > 800) {
			return true;
		}
		return false;
	}
	
	// 此觀測項目在無人測站不觀測 && 此測站是無人測站
	// || 海拔高度 > 800m
	@Override
	protected boolean isDummyCondition() {
		return  (!ce.isIsAutoStnMeasure() && station.isAuto())
				 || altitudeHigherThan800m(station.getAltitude());
	}

}
