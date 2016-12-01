package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static String getCurrentTime() {
        String returnStr = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        returnStr = f.format(date);
        return returnStr;
    }
}
