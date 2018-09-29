package dms.entity;

public class TaskTransferUserSave {

	private int id;
	private int transferSaveId;
	private int userId;
	private String userName;
	private String isShow;

	public TaskTransferUserSave() {

	}

	public TaskTransferUserSave(int transferSaveId, int userId, String userName) {
		this.transferSaveId = transferSaveId;
		this.userId = userId;
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTransferSaveId() {
		return transferSaveId;
	}

	public void setTransferSaveId(int transferSaveId) {
		this.transferSaveId = transferSaveId;
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
