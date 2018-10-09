package dms.entity;

/**
 * 不需要了、
 * @author ACER
 *
 */
public class TaskTransferStatus {

	private int id;
	private int taskId;
	private int transferId;
	private String isShow;

	public TaskTransferStatus() {

	}

	public TaskTransferStatus(int taskId, int transferId) {
		this.taskId = taskId;
		this.transferId = transferId;
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

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

}
