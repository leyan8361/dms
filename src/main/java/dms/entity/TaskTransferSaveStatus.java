package dms.entity;

/**
 * 记录任务的移交信息是否有被保存
 * 
 * @author ACER
 *
 */
public class TaskTransferSaveStatus {

	private int id;
	private int taskId;
	private int transferSaveId;
	private int userId; // 移交人的Id
	private String isShow;

	public TaskTransferSaveStatus() {

	}

	public TaskTransferSaveStatus(int taskId, int transferSaveId, int userId) {
		this.taskId = taskId;
		this.transferSaveId = transferSaveId;
		this.userId = userId;
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

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
