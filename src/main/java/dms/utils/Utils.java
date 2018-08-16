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

	/**
	 * 将String字符串(如"1,2,3,4,5")转换成int数组
	 * 
	 * @return
	 */
	public static int[] transStrToIntArr(String str) {

		String[] tempArr = str.split(",");
		int[] arr = new int[tempArr.length];
		for (int i = 0; i < tempArr.length; i++) {
			arr[i] = Integer.valueOf(tempArr[i]);
		}
		return arr;
	}
}
