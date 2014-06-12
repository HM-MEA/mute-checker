package jp.dip.jimanglaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {
	
	static public String getFormatedNowDate(){
		TimeZone tz = TimeZone.getTimeZone("JST");
		Calendar cal = Calendar.getInstance(tz);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		sdf.setTimeZone(tz);
		return sdf.format(cal.getTime());
	}
	
	static public Calendar getParsedCalendar(String str) throws ParseException{
		TimeZone tz = TimeZone.getTimeZone("JST");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		sdf.setTimeZone(tz);
		Calendar cal = Calendar.getInstance(tz);
		cal.setTime(sdf.parse(str));
		return cal;
	}
}
