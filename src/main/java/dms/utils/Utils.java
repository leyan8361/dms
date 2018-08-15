package dms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/**
	 * 获取指定格式的时间
	 * 
	 * @return
	 */
	public static String getNowDate(String pattern) {

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}
}
