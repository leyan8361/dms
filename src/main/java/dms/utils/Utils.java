package dms.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

	public static void main(String[] args) {
		System.out.println(JSONObject.parse(""));
	}
}
