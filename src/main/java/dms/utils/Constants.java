package dms.utils;

public class Constants {

	// token����Կ
	public static final String tokenSecret = "dms_token";

	// �ӿڷ��ص�״̬�� (�ɹ����ӿڳ���token��Ϣ����token���ڡ�Ȩ�޲���)
	public static final String successStatus = "0";
	public static final String apiErrorStatus = "1";
	public static final String tokenErrorStatus = "2";
	public static final String tokenOverTimeStatus = "3";
	public static final String authError = "4";

	// websocket�������ݵ�״̬��
	public static final String judgeStatus = "0"; // ����У��Է��Ƿ�����
	public static final String noticeDownStatus = "1"; // ֪ͨ�ͻ�������
	public static final String onlineTip = "2"; // ������Ϣ��ʾ
	public static final String downlineTip = "3"; // ������Ϣ��ʾ
	public static final String judgeStatus2 = "4"; // �ͻ��˷��ظ�����˵�������Ϣ
	public static final String sendStatus = "5"; // ������Ϣ��״̬
	public static final String refreshStatus = "6"; // ֪ͨҳ��ˢ��
	public static final String updateMessageGroupStatus = "7"; // �޸�Ⱥ����Ա��Ϣʱ������Ϣ֪ͨ
	public static final String noticeFIleUploadStatus = "8"; // ֪ͨ�ͻ����ļ��ϴ����

	// ҳ���б�����������
	public static final int pageSize = 10;
}
