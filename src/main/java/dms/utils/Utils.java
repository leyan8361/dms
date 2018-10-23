package dms.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import dms.entity.Message;

public class Utils {

	public static void returnErrorMessage(String status, String message, HttpServletResponse res) {

		try {
			Map<String, String> resMap = new HashMap<String, String>();
			resMap.put("status", status);
			resMap.put("info", message);
			res.setHeader("Access-Control-Allow-Origin", "*");
			res.getWriter().write(JSON.toJSONString(resMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
	 * 将逗号分隔的数字字符串转换为int数组
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

	/**
	 * 获取文件创建时间
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 */
	public static String getCreateTime(String path) {

		String createTime = null;
		try {
			Path p = Paths.get(path);
			BasicFileAttributes att = Files.readAttributes(p, BasicFileAttributes.class);// 锟斤拷取锟侥硷拷锟斤拷锟斤拷锟斤拷
			createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(att.creationTime().toMillis()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return createTime;
	}

	/**
	 * 对jsonarray 进行排序
	 * 
	 * @param ja
	 * @return
	 */
	public static JSONArray sortJSONArray(JSONArray array) {

		JSONArray sortedJsonArray = new JSONArray();
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < array.size(); i++) {
			jsonValues.add(array.getJSONObject(i));
		}

		Collections.sort(jsonValues, new Comparator<JSONObject>() {
			// You can change "Name" with "ID" if you want to sort by ID
			private static final String KEY_NAME = "columnId";

			@Override
			public int compare(JSONObject a, JSONObject b) {
				String valA = new String();
				String valB = new String();
				// try {
				// 这里是a、b需要处理的业务，需要根据你的规则进行修改。
				String aStr = a.getString(KEY_NAME);
				valA = aStr.replaceAll("-", "");
				String bStr = b.getString(KEY_NAME);
				valB = bStr.replaceAll("-", "");
				// } catch (JSONException e) {
				// // do something
				// e.printStackTrace();
				// }
				return Integer.valueOf(valA) - Integer.valueOf(valB);
				// if you want to change the sort order, simply use the following:
				// return -valA.compareTo(valB);
			}
		});
		for (int i = 0; i < array.size(); i++) {
			sortedJsonArray.add(jsonValues.get(i));
		}
		return sortedJsonArray;
	}

	/**
	 * 校验日期是否过期
	 * 
	 * @param createDate
	 *            创建日期
	 * @param effectiveDays
	 *            有效天数
	 * @return
	 * @throws ParseException
	 */
	public static boolean judgeIsOverTime(String createDate, int effectiveDays) {

		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			c.add(Calendar.DATE, -effectiveDays); // 算出n天前的日期
			Date date = sdf.parse(createDate);
			String date2Str = sdf.format(c.getTime());
			Date date2 = sdf.parse(date2Str);
			if (date.getTime() >= date2.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {

			e.printStackTrace();
			return false;
		}
	}

	/**
	 * arrayList排序
	 * 
	 * @param list
	 * @return
	 */
	public static List<Message> sortArrayList(List<Message> list) {

		Collections.sort(list, new Comparator<Message>() {

			public int compare(Message m1, Message m2) {
				String sendDate1 = m1.getSendDate();
				String sendDate2 = m2.getSendDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					if (sdf.parse(sendDate1).getTime() < sdf.parse(sendDate2).getTime()) {
						return -1;
					} else {
						return 1;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 1;
			}
		});
		return list;
	}

	public static void main(String[] args) {

	}
}
