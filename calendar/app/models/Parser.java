package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parser {
	
	private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("dd.MM.yy kk:mm");
	private static SimpleDateFormat simpleDayFormatter = new SimpleDateFormat("dd");
	private static SimpleDateFormat simpleMonthFormatter = new SimpleDateFormat("MMMMM");
	private static SimpleDateFormat simpleTwoDigitMonthFormatter = new SimpleDateFormat("MM");
	private static SimpleDateFormat simpleYearFormatter = new SimpleDateFormat("yyyy");
	private static SimpleDateFormat simpleTimelessFormatter = new SimpleDateFormat("dd.MM.yy");
	
	public static Date parseStringToDate(String strDate) {
		Date date = null;
		try {
			date = simpleDateFormatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String parseDateToString(Date date) {
		return simpleDateFormatter.format(date);
	}
	
	public static String parseDateToMonth(Date date) {
		return simpleMonthFormatter.format(date);
	}
	
	public static String parseDateToTwoDigitMonth(Date date) {
		return simpleTwoDigitMonthFormatter.format(date);
	}
	
	public static String parseTwoDigitMonthToMonthAsString(String twoDigitMonth) {
		Date date = null;
		try {
			date = simpleTwoDigitMonthFormatter.parse(twoDigitMonth);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return simpleMonthFormatter.format(date);
	}
	
	public static String parseMonthAsStringToTwoDigitMonth(String month) {
		Date date = null;
		try {
			date = simpleMonthFormatter.parse(month);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return simpleTwoDigitMonthFormatter.format(date);
	}

	public static String parseDateToYear(Date date) {
		return simpleYearFormatter.format(date);
	}

	public static String parseDateToDay(Date date) {
		return simpleDayFormatter.format(date);
	}
	
	public static Date parseDayToDate(String day) {
		Date date = null;
		try {
			date = simpleDayFormatter.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date parseDateToTimeLessDate(Date date) {
		String strDate = simpleDateFormatter.format(date);
		Date timelessDate = null;
		try {
			timelessDate = simpleTimelessFormatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timelessDate;
	}
}
