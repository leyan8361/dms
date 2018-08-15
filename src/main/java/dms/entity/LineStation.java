package dms.entity;

/**
 * 线路下车站
 * 
 * @author ACER
 *
 */
public class LineStation {

	private int id;
	private int lineId;
	private String station;
	private String isShow;

	public LineStation() {
		super();
	}

	public LineStation(int lineId, String station) {
		super();
		this.lineId = lineId;
		this.station = station;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

}
