package cwb.cmt.surface.utils;

import java.util.Date;
import java.util.GregorianCalendar;

public enum Month {
	
	January("Jan."),
	
	Febuary("Feb."),
	
	March("Mar."),
	
	April("Apr."),
	
	May("May."),
	
	June("Jun."),
	
	July("July."),
	
	August("Aug."),
	
	September("Sep."),
	
	October("Oct."),
	
	November("Nov."),
	
	December("Dec.");
	

	private static final int[] daysOfMonth = new int[] {
        31, 28, 31, 30, 31, 30,
        31, 31, 30, 31, 30, 31
    };
	
	/**
	 * provides 'now' time
	 */
	private static final Date Now = new Date();
	
	/**
	 * provides isLeap() function
	 */
	private static final GregorianCalendar Calendar = new GregorianCalendar();
	
	/**
	 * abbreviation of the name of the month
	 */
	private String simpleName;
	
	private Month(String simpleName) {
		this.simpleName = simpleName;
	}
	
	public String getSimpleName() {
		return simpleName;
	}
	
	public String getName() {
		return toString();
	}
	
	public int month() {
		return ordinal()+1;
	}
	
	public int monthIndex() {
		return ordinal();
	}
	
	/**
	 * @param month 0 ~ 11
	 */
	public static Month getByIndex(int index) {
		if (index < 0 || index > 11) {
			throw new IllegalArgumentException("Invalid month index " + index + ". " + 
				"Please specify one that is greater or equal to 0 and less than 12.");
		}
		return values()[index];
	}	
	
	public int daysOfMonth() {
		return daysOfMonth(Now.getYear() + 1900);
	}
	
	public int daysOfMonth(int year) {
		if (Febuary == this) {
			return (!Calendar.isLeapYear(year))? 28:29;
		}
		return daysOfMonth[ordinal()];
	}
}
