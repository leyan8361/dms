package dms.entity;

/**
 * ╫ги╚пео╒
 * 
 * @author ACER
 *
 */
public class Role {

	private int id;
	private String name;
	private String description;
	private int creator;
	private String createDate;
	private String isShow;

	public Role() {

	}

	public Role(String name, String description, int creator, String createDate) {
		this.name = name;
		this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
