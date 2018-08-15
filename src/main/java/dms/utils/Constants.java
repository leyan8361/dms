package dms.utils;

public class Constants {

	// token的密钥
	public static final String tokenSecret = "dms_token";

	// 接口返回的状态码 (成功、接口出错、token信息出错、token过期)
	public static final String successStatus = "0";
	public static final String apiErrorStatus = "1";
	public static final String tokenErrorStatus = "2";
	public static final String tokenOverTimeStatus = "3";

	// 页面中表格的数据行数
	public static final int pageSize = 10;
}
