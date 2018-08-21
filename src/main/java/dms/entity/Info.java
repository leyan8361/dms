package dms.entity;

import java.util.List;

public class Info {

	private int id;
	private String name;
	private int creator;
	private String createDate;
	private int columnNum;
	private List<InfoColumn> lic;
	private String isShow;

	public Info() {

	}

	public Info(String name, int creator, String createDate) {
		super();
		this.name = name;
		this.creator = creator;
		this.createDate = createDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	public List<InfoColumn> getLic() {
		return lic;
	}

	public void setLic(List<InfoColumn> lic) {
		this.lic = lic;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
