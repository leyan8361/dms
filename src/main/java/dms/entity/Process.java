package dms.entity;

import java.util.List;

public class Process {

	private int id;
	private String name;
	private int creator;
	private String createDate;
	private int columnNum;
	private List<ProcessColumn> lpc;
	private String isShow;

	public Process() {

	}

	public Process(String name, int creator, String createDate) {
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

	public List<ProcessColumn> getLpc() {
		return lpc;
	}

	public void setLpc(List<ProcessColumn> lpc) {
		this.lpc = lpc;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
