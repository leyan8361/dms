package dms.entity;

public class TaskTransferAttachSave {

	private int id;
	private int transferSaveId;
	private String path;
	private String isShow;

	public TaskTransferAttachSave() {

	}

	public TaskTransferAttachSave(int transferSaveId, String path) {
		this.transferSaveId = transferSaveId;
		this.path = path;
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
