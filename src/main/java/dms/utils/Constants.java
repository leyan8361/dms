package dms.utils;

public class Constants {

	// token的密钥
	public static final String tokenSecret = "dms_token";

	// 接口返回的状态码 (成功、接口出错、token信息出错、token过期、权限不够)
	public static final String successStatus = "0";
	public static final String apiErrorStatus = "1";
	public static final String tokenErrorStatus = "2";
	public static final String tokenOverTimeStatus = "3";
	public static final String authError = "4";

	// websocket传输数据的状态码
	public static final String judgeStatus = "0"; // 用于校验对方是否在线
	public static final String noticeDownStatus = "1"; // 通知客户端下线
	public static final String onlineTip = "2"; // 上线消息提示
	public static final String downlineTip = "3"; // 下线消息提示
	public static final String judgeStatus2 = "4"; // 客户端返回给服务端的上线信息
	public static final String sendStatus = "5"; // 发送消息的状态
	public static final String refreshStatus = "6"; // 通知页面刷新
	public static final String updateMessageGroupStatus = "7"; // 修改群组人员信息时发送消息通知
	public static final String noticeFIleUploadStatus = "8"; // 通知客户端文件上传完毕

	// 页面中表格的数据行数
	public static final int pageSize = 10;
}
