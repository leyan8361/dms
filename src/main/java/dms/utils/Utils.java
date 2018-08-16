package dms.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/**
	 * ��ȡָ����ʽ��ʱ��
	 * 
	 * @return
	 */
	public static String getNowDate(String pattern) {

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}

	/**
	 * ��String�ַ���(��"1,2,3,4,5")ת����int����
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
