package dms.entity;

public class UserFunction {

	private int id;
	private int userId;
	private String userName;
	private int pageId;
	private int functionId;
	private String fid; // ҳ�水ťId
	private String functionName;
	private String isShow;

	public UserFunction() {

	}

	public UserFunction(int userId, String userName, int pageId, int functionId, String fid, String functionName) {
		this.userId = userId;
		this.userName = userName;
		this.pageId = pageId;
		this.functionId = functionId;
		this.fid = fid;
		this.functionName = functionName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public int getFunctionId() {
		return functionId;
	}

	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
