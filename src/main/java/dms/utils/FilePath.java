package dms.utils;

import java.io.File;

public class FilePath {

	// 根目录
	public static String root = File.separator + "application" + File.separator + "shxinzhili" + File.separator + "data"
			+ File.separator + "dms" + File.separator;

	// 预案附件
	public static String planAttachPath = root + "planAttach" + File.separator;

	// 流程库表附件
	public static String processAttachPath = root + "processAttach" + File.separator;

	// 资料库附件
	public static String infoAttachPath = root + "infoAttach" + File.separator;

	// 任务附件
	public static String taskAttachPath = root + "taskAttach" + File.separator;

	// 运营事故报告附件
	public static String accidentReportAttachPath = root + "accidentReportAttach" + File.separator;
}
