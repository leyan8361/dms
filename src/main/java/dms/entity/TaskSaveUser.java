package dms.entity;

public class TaskSaveUser {

	private int id;
	private int taskSaveId;
	private int userId;
	private String userName;
	private String isShow;

	public TaskSaveUser() {

	}

	public TaskSaveUser(int taskSaveId, int userId, String userName) {
		this.taskSaveId = taskSaveId;
		this.userId = userId;
		this.userName = userName;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
