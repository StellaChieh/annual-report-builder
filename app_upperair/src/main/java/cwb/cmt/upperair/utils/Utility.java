package cwb.cmt.upperair.utils;

import java.io.File;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Utility {

	public static int getYearFromTimestamp (Timestamp time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		return cal.get(Calendar.YEAR);
	}
	
	public static int getMonthValueFromTimestamp (Timestamp time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		return cal.get(Calendar.MONTH)+1;
	}
	
	public static int getDateOfMonthFromTimestamp (Timestamp time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static List<YearMonth> getMonthsUntilNow (int year, int month){
		List<YearMonth> list = new ArrayList<>();
		for(int i=1; i<=month; i++) {
			list.add(YearMonth.of(year, i));
		}
		return list;
	}
	
	public static boolean fileIsOpened(String fileAbsolutePath) {
		boolean fileOpened = false;
		// the file we want to check
		File file = new File(fileAbsolutePath);
		// try to rename the file with the same name
		File sameFile = new File(fileAbsolutePath);
		if(file.renameTo(sameFile)) {
			// if the file is renamed
			fileOpened = false;
		} else {
			// if the file isn't renamed
			fileOpened = true;
		}
		return fileOpened;
	}
}
