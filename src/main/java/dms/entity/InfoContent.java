package dms.entity;

public class InfoContent {

	private int id;
	private int infoId;
	private int columnId;
	private String flag;
	private String content;
	private String isShow;

	public InfoContent() {

	}

	public InfoContent(int infoId, int columnId, String flag, String content) {
		this.infoId = infoId;
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

	public int getInfoId() {
		return infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
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
