package dms.entity;

/**
 * Ô¤°¸¸½¼þ
 * 
 * @author ACER
 *
 */
public class PlanAttach {

	private int id;
	private int planId;
	private String path;
	private String isShow;

	public PlanAttach() {
		super();
	}

	public PlanAttach(int planId, String path) {
		super();
		this.planId = planId;
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
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
