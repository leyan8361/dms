package dms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/**
	 * 获取yyyy-MM-dd hh:mm:ss格式的时间
	 * 
	 * @return
	 */
	public static String getNowDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sdf.format(new Date());
	}
}
