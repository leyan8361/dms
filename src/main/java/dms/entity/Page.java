package dms.entity;

import java.util.List;

public class Page {

	private int id;
	private String pid; // Ò³Ãæ±àºÅ
	private String title;
	private String level;
	private String parentId;
	private String description;
	private List<PageFunction> lpf;
	private String isShow;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<PageFunction> getLpf() {
		return lpf;
	}

	public void setLpf(List<PageFunction> lpf) {
		this.lpf = lpf;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
