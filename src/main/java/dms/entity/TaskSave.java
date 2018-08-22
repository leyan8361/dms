package dms.entity;

import java.util.List;

/**
 * 发布任务时保存的信息
 * 
 * @author ACER
 *
 */
public class TaskSave {

	private int id;
	private int userId;
	private String content;
	private String deadLine;
	private String attention;
	private String remark;
	private List<TaskSaveAttach> ltsa;
	private List<TaskSaveUser> ltsu;
	private String isShow;

	public TaskSave() {

	}

	public TaskSave(int userId, String contnet, String deadLine, String attention, String remark) {
		super();
		this.userId = userId;
		this.content = contnet;
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

	public List<TaskSaveAttach> getLtsa() {
		return ltsa;
	}

	public void setLtsa(List<TaskSaveAttach> ltsa) {
		this.ltsa = ltsa;
	}

	public List<TaskSaveUser> getLtsu() {
		return ltsu;
	}

	public void setLtsu(List<TaskSaveUser> ltsu) {
		this.ltsu = ltsu;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
