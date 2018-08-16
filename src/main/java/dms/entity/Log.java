package dms.entity;

public class Log {

	private String id;
	private String occurDate;
	private int userId;
	private String userName;
	private String operation;
	private String isShow;

	public Log() {

	}

	public Log(String occurDate, int userId, String userName, String operation) {
		this.occurDate = occurDate;
		this.userId = userId;
		this.userName = userName;
		this.operation = operation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOccurDate() {
		return occurDate;
	}

	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
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

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
