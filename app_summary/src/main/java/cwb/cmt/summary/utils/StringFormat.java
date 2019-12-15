package cwb.cmt.summary.utils;

import org.apache.commons.lang3.StringUtils;

public class StringFormat {
	
	/*
	 * use apache commonsLang api to center String
	 * @param pageStringSize the total English char this line may contain in specific font size and style
	 */
	public static String centerStrings(String originalString, int pageStringSize){
		String upString = originalString.split("\n")[0];
		String downString = originalString.split("\n")[1];
		String newUpString;
		String newDownString;
		if(upString.length()>= downString.length()){
			newUpString = StringUtils.center(upString, pageStringSize);
			newDownString = StringUtils.center(downString, upString.length()); // apache commonsLang api
			int whiteSpaceSize = (pageStringSize - upString.length())/2;
			return newUpString + "\n" + String.format("%"+whiteSpaceSize+"s", "")+ newDownString;
		} else {
			newDownString = StringUtils.center(downString, pageStringSize);  // apache commonsLang api
			newUpString = StringUtils.center(upString, downString.length());
			int whiteSpaceSize =(int)(pageStringSize - downString.length())/2;
			return String.format("%"+whiteSpaceSize+"s", "")+newUpString +"\n" + newDownString;
		}
	}
}
