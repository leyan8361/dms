package dms.entity;

import java.util.List;

public class Task {

	private int id;
	private String content;
	private String deadLine;
	private int level; // 任务级别 最低为0
	private int parentId; // 父任务Id 没有则为0
	private String attention;
	private String remark;
	private int creator;
	private String createDate;
	private List<TaskAttach> lta;
	private List<TaskUser> ltu;
	private String isDone; // 是否完成
	private String isShow;

	public Task() {

	}

	public Task(String content, String deadLine, int level, int parentId, String attention, String remark, int creator,
			String createDate) {
		this.content = content;
		this.deadLine = deadLine;
		this.level = level;
		this.parentId = parentId;
		this.attention = attention;
		this.remark = remark;
		this.creator = creator;
		this.createDate = createDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
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

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public List<TaskAttach> getLta() {
		return lta;
	}

	public void setLta(List<TaskAttach> lta) {
		this.lta = lta;
	}

	public List<TaskUser> getLtu() {
		return ltu;
	}

	public void setLtu(List<TaskUser> ltu) {
		this.ltu = ltu;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getIsDone() {
		return isDone;
	}

	public void setIsDone(String isDone) {
		this.isDone = isDone;
	}

}
