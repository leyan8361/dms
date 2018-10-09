package dms.entity;

import java.util.List;

public class TaskTransferSave {

	private int id;
	private int userId; // “∆Ωª»À
	private int taskId;
	private String content;
	private String deadLine;
	private String attention;
	private String remark;
	private List<TaskTransferUserSave> lttus;
	private List<TaskTransferAttachSave> lttas;
	private String isShow;

	public TaskTransferSave() {

	}

	public TaskTransferSave(int userId, int taskId, String content, String deadLine, String attention, String remark) {
		this.userId = userId;
		this.taskId = taskId;
		this.content = content;
		this.deadLine = deadLine;
		this.attention = attention;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<TaskTransferUserSave> getLttus() {
		return lttus;
	}

	public void setLttus(List<TaskTransferUserSave> lttus) {
		this.lttus = lttus;
	}

	public List<TaskTransferAttachSave> getLttas() {
		return lttas;
	}

	public void setLttas(List<TaskTransferAttachSave> lttas) {
		this.lttas = lttas;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
