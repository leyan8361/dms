package dms.entity;

public class TaskSaveAttach {

	private int id;
	private int taskSaveId;
	private String path;
	private String isShow;

	public TaskSaveAttach() {

	}

	public TaskSaveAttach(int taskSaveId, String path) {
		this.taskSaveId = taskSaveId;
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTaskSaveId() {
		return taskSaveId;
	}

	public void setTaskSaveId(int taskSaveId) {
		this.taskSaveId = taskSaveId;
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
