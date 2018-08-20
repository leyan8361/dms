package dms.entity;

public class ProcessContent {

	private int id;
	private int processId;
	private int columnId;
	private String flag;
	private String content;
	private String isShow;

	public ProcessContent() {

	}

	public ProcessContent(int processId, int columnId, String flag, String content) {
		this.processId = processId;
		this.columnId = columnId;
		this.flag = flag;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
