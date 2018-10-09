package dms.entity;

public class TaskUser {

	private int id;
	private int taskId;
	private int userId;
	private String userName;
	private String isDone; // ÿ���˵�isDoneΪyesʱ�����������
	private String isTransfer; // �жϸ�Ա���Ƿ����ƽ�����
	private int sonId; // �ƽ���������Id
	private String isShow;

	public TaskUser() {

	}

	public TaskUser(int taskId, int userId, String userName) {
		this.taskId = taskId;
		this.userId = userId;
		this.userName = userName;
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

	public String getIsDone() {
		return isDone;
	}

	public void setIsDone(String isDone) {
		this.isDone = isDone;
	}

	public String getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}

	public int getSonId() {
		return sonId;
	}

	public void setSonId(int sonId) {
		this.sonId = sonId;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
