package dms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/**
	 * ��ȡyyyy-MM-dd hh:mm:ss��ʽ��ʱ��
	 * 
	 * @return
	 */
	public static String getNowDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sdf.format(new Date());
	}
}
