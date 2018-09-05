package dms.entity;

/**
 * ����ģ�����Ϣ����״̬
 * 
 * @author ACER
 *
 */
public class TaskSaveStatus {

	private int id;
	private int userId;
	private int addStatus; // ��������״̬ 0/1
	private int dealStatus; // ��������״̬ 0/1
	private int transferStatus; // �ƽ�����״̬ 0/1
	private int addId; // ����ķ�����ϢId
	private int deadId; // ����Ĵ�����ϢId
	private int transferId; // ������ƽ���ϢId
	private String isShow;

	public TaskSaveStatus() {

	}

	public TaskSaveStatus(int userId, int addStatus, int dealStatus, int transferStatus, int addId, int deadId,
			int transferId) {
		this.userId = userId;
		this.addStatus = addStatus;
		this.dealStatus = dealStatus;
		this.transferStatus = transferStatus;
		this.addId = addId;
		this.deadId = deadId;
		this.transferId = transferId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAddStatus() {
		return addStatus;
	}

	public void setAddStatus(int addStatus) {
		this.addStatus = addStatus;
	}

	public int getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(int dealStatus) {
		this.dealStatus = dealStatus;
	}

	public int getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(int transferStatus) {
		this.transferStatus = transferStatus;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public int getAddId() {
		return addId;
	}

	public void setAddId(int addId) {
		this.addId = addId;
	}

	public int getDeadId() {
		return deadId;
	}

	public void setDeadId(int deadId) {
		this.deadId = deadId;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

}
