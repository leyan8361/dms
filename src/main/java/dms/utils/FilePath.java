package dms.utils;

import java.io.File;

public class FilePath {

	// ��Ŀ¼
	public static String root = File.separator + "application" + File.separator + "shxinzhili" + File.separator + "data"
			+ File.separator + "dms" + File.separator;

	// Ԥ������
	public static String planAttachPath = root + "planAttach" + File.separator;

	// ���̿����
	public static String processAttachPath = root + "processAttach" + File.separator;

	// ���Ͽ⸽��
	public static String infoAttachPath = root + "infoAttach" + File.separator;

	// ���񸽼�
	public static String taskAttachPath = root + "taskAttach" + File.separator;

	// ��Ӫ�¹ʱ��渽��
	public static String accidentReportAttachPath = root + "accidentReportAttach" + File.separator;
}
