package dms.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

public class Utils {

	public static void returnErrorMessage(String status,String message,HttpServletResponse res) {
		
		try {
			Map<String, String> resMap = new HashMap<String, String>();
			resMap.put("status", status);
			resMap.put("info", message);
			res.getWriter().write(JSON.toJSONString(resMap));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
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

	/**
	 * ��ȡ�ļ�����ʱ��
	 * 
	 * @param path
	 *            �ļ�·��
	 * @return
	 */
	public static String getCreateTime(String path) {

		String createTime = null;
		try {
			Path p = Paths.get(path);
			BasicFileAttributes att = Files.readAttributes(p, BasicFileAttributes.class);// ��ȡ�ļ�������
			createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(att.creationTime().toMillis()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return createTime;
	}

//	public static void main(String[] args) {
//		String path = "C://hiberfil.sys";
//		// FilePath.accidentReportAttachPath + "1535519898571_fontconfig-user.txt";
//		System.out.println(Utils.getCreateTime(path));
//	}
}
