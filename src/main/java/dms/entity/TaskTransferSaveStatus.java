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
	private String isShow;

	public TaskTransferSaveStatus() {

	}

	public TaskTransferSaveStatus(int taskId, int transferSaveId) {
		this.taskId = taskId;
		this.transferSaveId = transferSaveId;
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

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
