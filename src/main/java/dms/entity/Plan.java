package dms.entity;

import java.util.List;

/**
 * Ô¤°¸
 * 
 * @author ACER
 *
 */
public class Plan {

	private int id;
	private String name;
	private int lineId;
	private int lineNo;
	private int stationId;
	private String stationName;
	private List<PlanAttach> lpa;
	private String isShow;

	public Plan() {
		super();
	}

	public Plan(String name, int lineId, int lineNo, int stationId, String stationName) {
		super();
		this.name = name;
		this.lineId = lineId;
		this.lineNo = lineNo;
		this.stationId = stationId;
		this.stationName = stationName;
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

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public List<PlanAttach> getLpa() {
		return lpa;
	}

	public void setLpa(List<PlanAttach> lpa) {
		this.lpa = lpa;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
