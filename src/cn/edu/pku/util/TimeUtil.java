package cn.edu.pku.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	
	//num表示距离当前时间的天数
	public static String getDate(int num) {
		Calendar canlendar = Calendar.getInstance();
        canlendar.add(Calendar.DATE, num);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date = canlendar.getTime();
        return f.format(date);
	}
	
	public static int compareDate(String date1, String date2) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date a = new Date();
		try {
			a = f.parse(date1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date b = new Date();
		try {
			b = f.parse(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long diff = a.getTime() - b.getTime();
		if (diff < 0) {
			return -1;
		} else if (diff == 0) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public static void main(String args[]) {}
}
