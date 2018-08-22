package dms.entity;

public class TaskAttach {

	private int id;
	private int taskId;
	private String path;
	private String isShow;

	public TaskAttach() {

	}

	public TaskAttach(int taskId, String path) {
		this.taskId = taskId;
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
