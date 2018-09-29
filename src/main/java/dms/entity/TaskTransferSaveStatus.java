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
	private String isShow;

	public TaskTransferSaveStatus(int taskId) {
		this.taskId = taskId;
	}

	public TaskTransferSaveStatus(int id, int taskId, String isShow) {
		this.id = id;
		this.taskId = taskId;
		this.isShow = isShow;
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

}
